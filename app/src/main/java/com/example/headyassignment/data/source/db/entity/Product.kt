package com.example.headyassignment.data.source.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(@PrimaryKey
                   var id: Long,
                   var name: String,
                   var dateAdded: String,
                   var viewCout: Long = 0,
                   var orderedCout: Long = 0,
                   var shareCout: Long = 0,
                   var categoryId: Long,
                   @Embedded
                   var tax: Vat)