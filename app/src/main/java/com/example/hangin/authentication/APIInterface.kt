package com.example.hangin.authentication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    @POST("api/users/register")
    @Headers("Content-Type: application/json")
    fun createUser(@Body register: UserRegistration): Call<UserRegistration>

    @POST("api/users/login")
    @Headers("Content-Type: application/json")
    fun checkUser(@Body login: UserLogin): Call<UserLogin>
}