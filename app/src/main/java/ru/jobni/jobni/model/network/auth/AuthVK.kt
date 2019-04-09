package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthVK(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsVK
)

data class ErrorsVK(
        @SerializedName("social_token") val social_token : List<String>,
        @SerializedName("social_account") val social_account : List<String>,
        @SerializedName("authorization") val authorization: List<String>
)

data class AuthVKJobni(
        @SerializedName("uid")val uid: String,
        @SerializedName("provider")val provider: String,
        @SerializedName("access_token")val access_token: String
)