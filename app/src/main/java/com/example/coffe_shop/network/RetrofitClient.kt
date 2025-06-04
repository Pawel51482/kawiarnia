package com.example.coffe_shop.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        //.baseUrl("http://10.0.2.2:8000/") // emulator -> backend
        .baseUrl("http://192.168.1.23:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}
