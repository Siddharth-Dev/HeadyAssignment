package com.example.headyassignment.data.model

interface Item {
    fun getDisplayName(): String?
    fun isCategory(): Boolean
    fun getItemId(): Long?
    fun hasSubItems(): Boolean
}