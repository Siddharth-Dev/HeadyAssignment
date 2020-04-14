package com.example.headyassignment.data.source.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction
import com.example.headyassignment.data.model.Item

class CategoryWithChildren : Item {

    @Embedded
    var category: Category? = null

    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = Product::class)
    var products: List<Product> = ArrayList()

    @Relation(parentColumn = "id", entityColumn = "parentId", entity = Category::class)
    var subCategories: List<Category> = ArrayList()

    override fun getDisplayName() = category?.name

    override fun isCategory() = true

    override fun getItemId() = category?.id

    override fun hasSubItems() = category?.subCategoriesCount ?: 0 > 0
}