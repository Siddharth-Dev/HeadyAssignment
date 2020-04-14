package com.example.headyassignment.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.headyassignment.data.source.db.entity.CategoryWithChildren

@Dao
interface CategoryWithChildrenDao {

    @Transaction
    @Query("SELECT * FROM Category WHERE id = :id")
    suspend fun getCategoryForId(id: Long): CategoryWithChildren?

    @Transaction
    @Query("SELECT * FROM Category WHERE parentId is null")
    suspend fun getTopLevelCategories(): List<CategoryWithChildren>?

    @Transaction
    @Query("SELECT * FROM Category WHERE parentId = :parentId")
    suspend fun getCategoriesForParent(parentId: Long): List<CategoryWithChildren>?
}