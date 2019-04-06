package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class Authentication(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ListErrors
)

data class ListErrors(
        @SerializedName("phone") val phone: List<String>,
        @SerializedName("password") val password: List<String>
)