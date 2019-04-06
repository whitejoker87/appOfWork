package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthPhone(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsPhone
)

data class ErrorsPhone(
        @SerializedName("phone") val phone: List<String>,
        @SerializedName("password") val password: List<String>,
        @SerializedName("authorization") val authorization: List<String>
)