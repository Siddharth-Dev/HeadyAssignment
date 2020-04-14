package com.example.headyassignment.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : BaseViewModel(application) {
    val homeTile = "Heady"
    var title: String = homeTile
    val showBack = MutableLiveData<Boolean>()

    fun showBackWithTitle(title: String, enableBack: Boolean) {
        if (enableBack) {
            this.title = title
        } else {
            this.title = homeTile
        }
        showBack.value = enableBack
    }
}