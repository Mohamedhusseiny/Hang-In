package com.example.hangin.place

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("image")
    var image: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("rating")
    var rating: Float
)