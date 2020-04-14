package com.example.headyassignment.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.headyassignment.data.source.db.entity.CategoryWithChildren

@Dao
interface CategoryWithChildrenDao {

    @Query("SELECT * FROM Category WHERE id = :id")
    fun getCategoryForId(id: Long): CategoryWithChildren?

    @Query("SELECT * FROM Category WHERE parentId = null")
    fun getTopLevelCategories(): List<CategoryWithChildren>?
}