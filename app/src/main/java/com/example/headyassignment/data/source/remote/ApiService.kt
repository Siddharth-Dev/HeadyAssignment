package com.example.headyassignment.data.source.remote

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

internal class ApiService() {

    val apiBasePath = "https://stark-spire-93433.herokuapp.com/"

    fun getApiInterface(): ApiInterface {
        val builder = getHttpClientBuilder()
        val retrofit = Retrofit.Builder()
                .baseUrl(apiBasePath)
                .client(builder.build())
                .build()

        return retrofit.create(ApiInterface::class.java)
    }

    private fun getHttpClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()

        val protoList = ArrayList<Protocol>()
        protoList.add(Protocol.HTTP_2)
        protoList.add(Protocol.HTTP_1_1)
        builder.protocols(protoList)
        //validate connection pool
        builder.connectionPool(ConnectionPool(0, 1, TimeUnit.MILLISECONDS))
        builder.retryOnConnectionFailure(true)
        val timeout = 30
        builder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)

        return builder
    }
}