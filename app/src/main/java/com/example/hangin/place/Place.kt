package com.example.hangin.place

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Place(
    @SerializedName("image")
    var image: String,
    @SerializedName("coverImage")
    var coverImage: String,
    @SerializedName("rating")
    var rating: Float,
    @SerializedName("totalRating")
    var totalRating: Float,
    @SerializedName("isActive")
    var isActive: Boolean,
    @SerializedName("_id")
    var _id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lng")
    var lng: Double,
    @SerializedName("hourPrice")
    var hourPrice: Float,
    @SerializedName("dayPrice")
    var dayPrice: Float,
    @SerializedName("capacity")
    var capacity: Int
) : Serializable