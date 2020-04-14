package com.example.headyassignment.data.source.db.entity

import androidx.room.Embedded
import androidx.room.Relation

class CategoryWithChildren {

    @Embedded
    var category: Category? = null

    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = Product::class)
    var products: List<ProductWithVariants> = ArrayList()

    @Relation(parentColumn = "id", entityColumn = "parentId", entity = Category::class)
    var subCategories: List<CategoryWithChildren> = ArrayList()
}