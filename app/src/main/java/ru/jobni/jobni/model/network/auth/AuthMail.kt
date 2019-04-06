package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthMail(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsMail
)

data class ErrorsMail(
        @SerializedName("email") val email: List<String>,
        @SerializedName("password") val password: List<String>,
        @SerializedName("authorization") val authorization: List<String>
)