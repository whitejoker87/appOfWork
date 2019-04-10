package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthMailru(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsMailru
)

data class ErrorsMailru(
        @SerializedName("social_account") val social_account: List<String>,
        @SerializedName("authorization") val authorization: List<String>
)

data class AuthMailruJobni(
        @SerializedName("uid") val uid: String,
        @SerializedName("provider") val provider: String,
        @SerializedName("access_token") val access_token: String
)