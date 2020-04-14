package com.example.headyassignment.data.source.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.headyassignment.data.source.db.dao.*
import com.example.headyassignment.data.source.db.entity.Category
import com.example.headyassignment.data.source.db.entity.Product
import com.example.headyassignment.data.source.db.entity.Variant

@Database(entities = [Category::class, Product::class, Variant::class], version = 1, exportSchema = false)
internal abstract class AppDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun variantDao(): VariantDao
    abstract fun categoryWithChildrenDao(): CategoryWithChildrenDao
    abstract fun productWithVariantsDao(): ProductWithVariantDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        const val NAME = "AppDb"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}