package com.shv.android.meetingreminder.data.network.api
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

object ApiFactory {

    private const val BASE_URL = "https://randomuser.me/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}