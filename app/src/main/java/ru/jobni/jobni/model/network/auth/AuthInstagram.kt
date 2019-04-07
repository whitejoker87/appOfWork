package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthInstagram(
        @SerializedName("error_type") val error_type : String,
        @SerializedName("code") val code : Int,
        @SerializedName("error_message") val error_message : String
)