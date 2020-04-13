package com.example.headyassignment.data.model

data class Category(val id: Long,
                    val name: String,
                    var products: ArrayList<Product>,
                    var subCategories: ArrayList<Category> = ArrayList()) {

    var isAChild = false
}