package com.example.headyassignment.data.source.db.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(@PrimaryKey
                    var id: Long,
                    var name: String,
                    var productCount: Long,
                    var subCategoriesCount: Long,
                    @Nullable
                    var parentId: Long?)