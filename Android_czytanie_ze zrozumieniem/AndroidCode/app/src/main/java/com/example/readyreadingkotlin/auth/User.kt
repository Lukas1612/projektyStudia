package com.example.readyreadingkotlin.auth

import com.google.gson.annotations.SerializedName

data class User (
        @SerializedName("password")
        var password: String,

        @SerializedName("name")
        var name: String
)