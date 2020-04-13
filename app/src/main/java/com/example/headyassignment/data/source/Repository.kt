package com.example.headyassignment.data.source

import com.example.headyassignment.data.source.remote.ApiService
import com.example.headyassignment.utils.Utility

class Repository private constructor(){

    private val TAG = "Repository"

    companion object {
        private var repository: Repository? = null

        fun getInstance() : Repository {
            if (repository == null) {
                repository = Repository()
            }

            return repository!!
        }
    }



    private val apiInterface by lazy {
        ApiService().getApiInterface()
    }


    suspend fun fetchData() {

        // TODO check DB else call api

       val response = apiInterface.getData()

        if (response.isSuccessful) {

        } else {
            val msg = response.errorBody()?.string() ?: response.message()
            Utility.log(TAG, "fetchData: failed to get data. Error - $msg")
        }
    }
}