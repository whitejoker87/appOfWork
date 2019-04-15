package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthSocial(

        @SerializedName("success") val success: Boolean,
        @SerializedName("next") val next: String,
        @SerializedName("previous") val previous: String,
        @SerializedName("count") val count: Int,
        @SerializedName("results") val results: List<ResultSocial>,
        @SerializedName("detail") val detail: List<String>,
        @SerializedName("errors") val errors : ErrorsSocial
)

data class ResultSocial(

        @SerializedName("id") val id: Int,
        @SerializedName("provider") val provider: String,
        @SerializedName("uid") val uid: Float,
        @SerializedName("last_login") val last_login: String,
        @SerializedName("date_joined") val date_joined: String
)

data class ErrorsSocial (

        @SerializedName("non_field_errors") val non_field_errors : List<String>,
        @SerializedName("account") val account : List<String>
)