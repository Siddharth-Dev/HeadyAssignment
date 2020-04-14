package com.example.headyassignment.data.source.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.headyassignment.data.source.db.entity.Category
import com.example.headyassignment.data.source.db.entity.Variant

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)
}