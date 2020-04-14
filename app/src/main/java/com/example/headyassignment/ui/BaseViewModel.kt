package com.example.headyassignment.ui

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.headyassignment.data.model.Item
import com.example.headyassignment.data.source.Repository

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val KEY_ID = "id"
    protected val KEY_SHOW_SUB_CATEGORY = "showSubCategories"
    protected val KEY_SHOW_PRODUCTS = "showProducts"
    protected val KEY_NAME = "name"

    protected val repository by lazy {
        Repository.getInstance(application.applicationContext)
    }

    fun getBundleForItem(item: Item): Bundle {
        return Bundle().apply {
            putLong(KEY_ID, item.getItemId() ?: -1)
            putString(KEY_NAME, item.getDisplayName())
            if (item.isCategory() && item.hasSubItems()) {
                putBoolean(KEY_SHOW_SUB_CATEGORY, true)
            } else {
                putBoolean(KEY_SHOW_PRODUCTS, true)
            }
        }
    }

    val itemClicked = MutableLiveData<Item>()

    fun onItemClicked(item: Item) {
        itemClicked.value = item
    }
}