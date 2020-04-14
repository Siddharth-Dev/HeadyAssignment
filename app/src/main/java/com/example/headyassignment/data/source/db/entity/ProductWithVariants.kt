package com.example.headyassignment.data.source.db.entity

import androidx.room.Embedded
import androidx.room.Relation

class ProductWithVariants {

    @Embedded
    var product: Product? = null

    @Relation(parentColumn = "id", entityColumn = "productId", entity = Variant::class)
    var variants: List<Variant> = ArrayList()

}