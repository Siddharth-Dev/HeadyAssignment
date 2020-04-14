package com.example.headyassignment.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.headyassignment.data.source.db.entity.ProductWithVariants

@Dao
interface ProductWithVariantDao {

    @Transaction
    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getProductForId(id: Long): ProductWithVariants?

    @Transaction
    @Query("SELECT * FROM Product WHERE categoryId = :categoryId ORDER BY viewCout DESC")
    suspend fun getProductsForCategoryOrderByView(categoryId: Long): List<ProductWithVariants>?

    @Transaction
    @Query("SELECT * FROM Product WHERE categoryId = :categoryId ORDER BY orderedCout DESC")
    suspend fun getProductsForCategoryOrderByOrdered(categoryId: Long): List<ProductWithVariants>?

    @Transaction
    @Query("SELECT * FROM Product WHERE categoryId = :categoryId ORDER BY shareCout DESC")
    suspend fun getProductsForCategoryOrderByShared(categoryId: Long): List<ProductWithVariants>?
}