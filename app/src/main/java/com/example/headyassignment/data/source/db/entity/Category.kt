package com.example.headyassignment.data.source.db.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.headyassignment.data.model.Item

@Entity
data class Category(@PrimaryKey
                    var id: Long,
                    var name: String,
                    var productCount: Long,
                    var subCategoriesCount: Long,
                    @Nullable
                    var parentId: Long?): Item {

    override fun getDisplayName() = name

    override fun isCategory() = true

    override fun getItemId() = id

    override fun hasSubItems() = subCategoriesCount > 0
}