package com.example.headyassignment.data.source

import android.content.Context
import com.example.headyassignment.data.source.db.AppDatabase
import com.example.headyassignment.data.source.db.dao.CategoryDao
import com.example.headyassignment.data.source.db.dao.ProductDao
import com.example.headyassignment.data.source.db.dao.VariantDao
import com.example.headyassignment.data.source.db.entity.Category
import com.example.headyassignment.data.source.db.entity.Product
import com.example.headyassignment.data.source.db.entity.Variant
import com.example.headyassignment.data.source.db.entity.Vat
import com.example.headyassignment.data.source.remote.ApiService
import com.example.headyassignment.utils.Utility
import org.json.JSONArray
import org.json.JSONObject

class Repository private constructor(context: Context){

    private val TAG = "Repository"

    companion object {
        private var repository: Repository? = null

        fun getInstance(context: Context) : Repository {
            if (repository == null) {
                repository = Repository(context)
            }

            return repository!!
        }
    }

    private val apiInterface by lazy {
        ApiService().getApiInterface()
    }

    private val database by lazy {
        AppDatabase.getDatabase(context.applicationContext)
    }

    suspend fun fetchData() {

        // TODO check DB else call api

       val response = apiInterface.getData()

        if (response.isSuccessful) {

            val responseData = response.body()?.string()
            responseData?.let {
                parseAndSaveData(responseData)
            }
        } else {
            val msg = response.errorBody()?.string() ?: response.message()
            Utility.log(TAG, "fetchData: failed to get data. Error - $msg")
        }
    }

    private suspend fun parseAndSaveData(response: String) {
        Utility.log(TAG, "parseAndSaveData: In")

        val mainJson = JSONObject(response)
        val categoriesJson = mainJson.getJSONArray("categories")
        val idForProductMap = HashMap<Long, Product>()
        val idForCategoryMap = HashMap<Long, Category>()
        val categoryIdForSubCategoriesIdMap = HashMap<Long,JSONArray>()

        for (i in 0 until categoriesJson.length()) {
            parseCategory(categoriesJson.getJSONObject(i), idForCategoryMap, categoryIdForSubCategoriesIdMap, idForProductMap)
        }

        val rankingJson = mainJson.getJSONArray("rankings")
        // update each product mapped in ranking json attribute
        updateProductsRankingAttribute(rankingJson, idForProductMap)

        // Fetch all the sub categories obj and assign it to parent category
        parseAndAssignSubCategories(idForCategoryMap, categoryIdForSubCategoriesIdMap)

        // Save products in DB
        saveProductsInDb(idForProductMap)

        // Save categories in DB
        saveCategoryInDb(idForCategoryMap)
    }

    private suspend fun parseCategory(categoryJson: JSONObject, idForCategoryMap: HashMap<Long, Category>, categoryIdForSubCategoriesIdMap: HashMap<Long,JSONArray>, idForProductMap: HashMap<Long, Product>) {

        val childCategoryArray = categoryJson.getJSONArray("child_categories")

        val category = Category(
            categoryJson.getLong("id"),
            categoryJson.getString("name"),
            categoryJson.getJSONArray("products").length().toLong(),
            childCategoryArray.length().toLong(),
            null
        )

        parseProductForCategory(categoryJson, idForProductMap)

        idForCategoryMap[category.id] = category

        if (childCategoryArray.length()>0) {
            categoryIdForSubCategoriesIdMap[category.id] = childCategoryArray
        }
    }

    private fun parseAndAssignSubCategories(idForCategoryMap: HashMap<Long, Category>, categoryIdForSubCategoriesIdMap: HashMap<Long, JSONArray>) {
        // As all categories are created add there sub categories
        // First get the sub category ids, then fetch those category
        // from map and assign it to parent
        // also mark the sub category as child to help us with DB query
        for (id in categoryIdForSubCategoriesIdMap.keys) {
            val category = idForCategoryMap[id]
            val subIdsArray = categoryIdForSubCategoriesIdMap[id]

            subIdsArray?.let {

                for (i in 0 until it.length()) {
                    val subCategory = idForCategoryMap[it.getLong(i)]
                    subCategory?.parentId = category?.id
                }
            }
        }
    }

    private suspend fun parseProductForCategory(categoryJson: JSONObject, idForProductMap: HashMap<Long, Product>) {
        val productsJson = categoryJson.getJSONArray("products")
        val categoryId = categoryJson.getLong("id");

        for (i in 0 until productsJson.length()) {
            val productJson = productsJson.getJSONObject(i)
            val taxJson = productJson.getJSONObject("tax")

            val vat = Vat(
                taxJson.getString("name"),
                taxJson.getDouble("value")
            )

            val product = Product(
                productJson.getLong("id"),
                productJson.getString("name"),
                productJson.getString("date_added"),
                categoryId = categoryId,
                tax = vat)

            idForProductMap[product.id] = product

            parseVariantForProduct(productJson)
        }
    }

    private suspend fun parseVariantForProduct(productJson: JSONObject) {
        val productId = productJson.getLong("id")
        val variantsJson = productJson.getJSONArray("variants")

        for (i in 0 until variantsJson.length()) {
            val variantJson = variantsJson.getJSONObject(i)
            val variant = Variant(variantJson.getLong("id"),
                                variantJson.getString("color"),
                                variantJson.getInt("size"),
                                variantJson.getDouble("price"),
                                productId)

            // Save variant in DB
            saveVariantInDb(variant)
        }
    }

    private fun updateProductsRankingAttribute(rankingJSONArray: JSONArray, idForProductMap: HashMap<Long, Product>) {

        if (rankingJSONArray.length() > 0) {
            val rankingJson = rankingJSONArray.getJSONObject(0)
            val productRankingJson = rankingJson.getJSONArray("products")
            for (i in 0 until productRankingJson.length()) {
                val orderJson = productRankingJson.getJSONObject(i)
                val product = idForProductMap[orderJson.getLong("id")]
                product?.let {
                    it.viewCout = orderJson.getLong("view_count")
                }
            }
        }

        if (rankingJSONArray.length() > 1) {
            val rankingJson = rankingJSONArray.getJSONObject(1)
            val productRankingJson = rankingJson.getJSONArray("products")
            for (i in 0 until productRankingJson.length()) {
                val orderJson = productRankingJson.getJSONObject(i)
                val product = idForProductMap[orderJson.getLong("id")]
                product?.let {
                    it.orderedCout = orderJson.getLong("order_count")
                }
            }
        }

        if (rankingJSONArray.length() > 2) {
            val rankingJson = rankingJSONArray.getJSONObject(2)
            val productRankingJson = rankingJson.getJSONArray("products")
            for (i in 0 until productRankingJson.length()) {
                val orderJson = productRankingJson.getJSONObject(i)
                val product = idForProductMap[orderJson.getLong("id")]
                product?.let {
                    it.shareCout = orderJson.getLong("shares")
                }
            }
        }
    }

    private suspend fun saveVariantInDb(variant: Variant) {
        database.variantDao().insertVariant(variant)
    }

    private suspend fun saveProductsInDb(idForProductMap: HashMap<Long, Product>) {
        for (id in idForProductMap.keys) {
            idForProductMap[id]?.let {
                database.productDao().insertProduct(it)
            }
        }
    }

    private suspend fun saveCategoryInDb(idForCategoryMap: HashMap<Long, Category>) {

        for (id in idForCategoryMap.keys) {
            idForCategoryMap[id]?.let {
                database.categoryDao().insertCategory(it)
            }
        }
    }
}