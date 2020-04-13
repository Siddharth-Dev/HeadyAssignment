package com.example.headyassignment.data.source.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

internal interface ApiInterface {

    @GET("json")
    suspend fun getData(): Response<ResponseBody>
}