package com.example.coffe_shop.network

import com.example.coffe_shop.data.LoginRequest
import com.example.coffe_shop.data.LoginResponse
import com.example.coffe_shop.data.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("token")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

}
