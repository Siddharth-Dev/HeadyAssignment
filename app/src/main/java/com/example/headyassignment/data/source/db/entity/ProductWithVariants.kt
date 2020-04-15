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

    fun getProductVariantDetails(): String {
        return variants.joinToString(separator = "\n") { it.toString() }
    }

    fun getRank(order: Int): String {
        return when (order) {
            0 -> "Views: ${product?.viewCout}"
            1 -> "Ordered: ${product?.orderedCout}"
            else -> "Shared: ${product?.shareCout}"
        }
    }
}