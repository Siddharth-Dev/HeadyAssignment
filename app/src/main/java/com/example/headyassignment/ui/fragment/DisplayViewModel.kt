package com.example.headyassignment.ui.fragment

import android.app.Application
import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.headyassignment.data.model.Item
import com.example.headyassignment.data.source.db.entity.Product
import com.example.headyassignment.ui.BaseViewModel
import kotlinx.coroutines.launch

class DisplayViewModel(application: Application): BaseViewModel(application) {

    val isLoading = ObservableBoolean(false)
    val categories: ArrayList<Item> = ArrayList()
    val notifyChange = MutableLiveData<Boolean>()
    private var categorySelectedId: Long = -1
    var categorySelectedName: String? = ""
    var showProduct = false
    var showSubCategory = false
    var productsSortOrder = 0

    fun withArgs(arguments: Bundle?) {
        categorySelectedId = arguments?.getLong(KEY_ID, -1) ?: -1
        categorySelectedName = arguments?.getString(KEY_NAME)
        showSubCategory = arguments?.getBoolean(KEY_SHOW_SUB_CATEGORY, false) ?: false
        showProduct = arguments?.getBoolean(KEY_SHOW_PRODUCTS, false) ?: false
    }

    fun showGridView() = categorySelectedId > 0

    fun onSortOrderChanged(order: Int) {
        productsSortOrder = order
        getData()
    }

    fun getData() {
        viewModelScope.launch {

            isLoading.set(true)
            var items: List<Item>? = null

            items = when {
                showSubCategory -> repository.getCategoriesForParent(categorySelectedId)
                showProduct -> repository.getProductsForCategory(productsSortOrder, categorySelectedId)
                else -> repository.fetchData()
            }

            isLoading.set(false)
            items?.let {
                categories.clear()
                categories.addAll(it)

                notifyChange.value = true
            }
        }
    }
}