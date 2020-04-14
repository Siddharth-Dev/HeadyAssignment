package com.example.headyassignment.data.source.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.headyassignment.data.model.Item

class ProductWithVariants : Item {

    @Embedded
    var product: Product? = null

    @Relation(parentColumn = "id", entityColumn = "productId", entity = Variant::class)
    var variants: List<Variant> = ArrayList()

    override fun isCategory() = false

    override fun getDisplayName() = product?.name

    override fun getItemId() = product?.id

    override fun hasSubItems() = false

    fun getTaxInfo(): String = "${product?.tax?.taxName}: ${product?.tax?.value}%"

    fun getSizeDetail(): String? {
        return "Size: ${variants.filter { it.size>0 }.map { it.size }.joinToString()}"
    }

    fun getColorDetail(): String {
        return "Color: ${variants.map { it.color }.joinToString()}"
    }

    fun getLowestPrice(): String {
        val price = variants.sortedBy { it.price }[0].price
        return "Price: Rs.$price"
    }

    fun isSizeApplicable() = if (variants.isNotEmpty()) variants[0].size>0 else false

    fun getRank(order: Int): String {
        return when (order) {
            0 -> "Views: ${product?.viewCout}"
            1 -> "Ordered: ${product?.orderedCout}"
            else -> "Shared: ${product?.shareCout}"
        }
    }
}