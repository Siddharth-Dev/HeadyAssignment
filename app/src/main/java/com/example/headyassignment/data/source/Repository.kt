package com.example.headyassignment.data.source

import com.example.headyassignment.data.model.Category
import com.example.headyassignment.data.model.Product
import com.example.headyassignment.data.model.Variant
import com.example.headyassignment.data.model.Vat
import com.example.headyassignment.data.source.remote.ApiService
import com.example.headyassignment.utils.Utility
import org.json.JSONArray
import org.json.JSONObject

class Repository private constructor(){

    private val TAG = "Repository"

    companion object {
        private var repository: Repository? = null

        fun getInstance() : Repository {
            if (repository == null) {
                repository = Repository()
            }

            return repository!!
        }
    }



    private val apiInterface by lazy {
        ApiService().getApiInterface()
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

    private fun parseAndSaveData(response: String) {
        Utility.log(TAG, "parseAndSaveData: In")

        val mainJson = JSONObject(response)
        val categoriesJson = mainJson.getJSONArray("categories")
        val idForProductMap = HashMap<Long,Product>()
        val idForCategoryMap = HashMap<Long,Category>()
        val categoryIdForSubCategoriesIdMap = HashMap<Long,JSONArray>()

        for (i in 0 until categoriesJson.length()) {
            parseCategory(categoriesJson.getJSONObject(i), idForCategoryMap, categoryIdForSubCategoriesIdMap, idForProductMap)
        }

        val rankingJson = mainJson.getJSONArray("rankings")
        // update each product mapped in ranking json attribute
        updateProductsRankingAttribute(rankingJson, idForProductMap)

        // Fetch all the sub categories obj and assign it to parent category
        parseAndAssignSubCategories(idForCategoryMap, categoryIdForSubCategoriesIdMap)
    }

    private fun parseCategory(categoryJson: JSONObject, idForCategoryMap: HashMap<Long,Category>, categoryIdForSubCategoriesIdMap: HashMap<Long,JSONArray>, idForProductMap: HashMap<Long,Product>) {

        val products = parseProductForCategory(categoryJson, idForProductMap)
        val category = Category(categoryJson.getLong("id"),
                                categoryJson.getString("name"),
                                products)

        idForCategoryMap[category.id] = category

        val childCategoryArray = categoryJson.getJSONArray("child_categories")
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
                val subCategoryList = ArrayList<Category>()

                for (i in 0 until it.length()) {
                    val subCategory = idForCategoryMap[it.getLong(i)]

                    subCategory?.let { category->
                        category.isAChild = true
                        subCategoryList.add(category)
                    }
                }

                category?.subCategories = subCategoryList
            }
        }
    }

    private fun parseProductForCategory(categoryJson: JSONObject, idForProductMap: HashMap<Long,Product>): ArrayList<Product> {
        val products = ArrayList<Product>()
        val productsJson = categoryJson.getJSONArray("products")

        for (i in 0 until productsJson.length()) {
            val productJson = productsJson.getJSONObject(i)
            val taxJson = productJson.getJSONObject("tax")
            val variants = parseVariantForProduct(productJson)
            val vat = Vat(taxJson.getString("name"), taxJson.getDouble("value"))

            val product = Product(productJson.getLong("id"),
                                productJson.getString("name"),
                                productJson.getString("date_added"),
                                variants = variants,
                                tax = vat)

            idForProductMap[product.id] = product
            products.add(product)
        }

        return products
    }

    private fun parseVariantForProduct(productJson: JSONObject): ArrayList<Variant> {
        val variants = ArrayList<Variant>()
        val variantsJson = productJson.getJSONArray("variants")
        for (i in 0 until variantsJson.length()) {
            val variantJson = variantsJson.getJSONObject(i)
            variants.add(Variant(variantJson.getInt("id"),
                                variantJson.getString("color"),
                                variantJson.getInt("size"),
                                variantJson.getDouble("price")))
        }

        return variants
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
}