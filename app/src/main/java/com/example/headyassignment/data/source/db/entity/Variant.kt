package com.example.headyassignment.data.source.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Variant(@PrimaryKey
                   var id: Long,
                   var color: String,
                   var size: Int,
                   var price: Double,
                   var productId: Long) {

    override fun toString(): String {
        return "Color: $color${if (size>0) ", Size: $size" else ""}, Price: Rs.${getDisplayPrice()}"
    }

    private fun getDisplayPrice(): String {
        val priceLong = price.toLong()
        return if (price > priceLong) "%.2f".format(priceLong) else priceLong.toString()
    }
}