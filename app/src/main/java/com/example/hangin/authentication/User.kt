package com.example.hangin.authentication

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)

data class UserRegistration(
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("password")
    var password: String
)