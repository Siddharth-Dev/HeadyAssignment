package com.example.headyassignment.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.headyassignment.data.source.db.entity.ProductWithVariants

@Dao
interface ProductWithVariantDao {

    @Query("SELECT * FROM Product WHERE id = :id")
    fun getProductForId(id: Long): ProductWithVariants?

    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    fun getProductsForCategory(categoryId: Long): List<ProductWithVariants>?
}