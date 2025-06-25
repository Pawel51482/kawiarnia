package com.example.coffe_shop.data

data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)

