package com.example.headyassignment.ui

import androidx.lifecycle.ViewModel
import com.example.headyassignment.data.source.Repository

abstract class BaseViewModel : ViewModel() {

    protected val repository by lazy {
        Repository.getInstance()
    }
}