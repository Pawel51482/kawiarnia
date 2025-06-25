package com.example.coffe_shop.network

import com.example.coffe_shop.data.LoginRequest
import com.example.coffe_shop.data.LoginResponse
import com.example.coffe_shop.data.RegisterRequest
import com.example.coffe_shop.models.Coffee
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("token")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("/coffees")
    suspend fun getCoffees(): List<Coffee>

}
