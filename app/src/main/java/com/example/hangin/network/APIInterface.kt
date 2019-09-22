package com.example.hangin.network

import com.example.hangin.authentication.UserLogin
import com.example.hangin.authentication.UserRegistration
import com.example.hangin.place.Place
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    @POST("api/users/register")
    @Headers("Content-Type: application/json")
    fun createUser(@Body register: UserRegistration): Call<UserRegistration>

    @POST("api/users/login")
    @Headers("Content-Type: application/json")
    fun checkUser(@Body login: UserLogin): Call<UserLogin>

    @GET("api/places")
    fun getPlaces(): Call<ArrayList<Place>>
}