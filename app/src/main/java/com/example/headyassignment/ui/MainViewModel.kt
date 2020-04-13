package com.example.headyassignment.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    fun fetchData() {
        viewModelScope.launch {
            repository.fetchData()
        }
    }
}