package com.example.headyassignment.data.source.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Variant(@PrimaryKey
                   var id: Long,
                   var color: String,
                   var size: Int,
                   var price: Double,
                   var productId: Long)