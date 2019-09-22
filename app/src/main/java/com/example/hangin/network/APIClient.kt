package com.example.hangin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://node-hangin.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}