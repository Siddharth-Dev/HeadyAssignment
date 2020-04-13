package com.example.headyassignment.data.model

data class Product(val id: Long,
                   val name: String,
                   val dateAdded: String,
                   var viewCout: Long = 0,
                   var orderedCout: Long = 0,
                   var shareCout: Long = 0,
                   var variants: List<Variant>,
                   var tax: Vat)