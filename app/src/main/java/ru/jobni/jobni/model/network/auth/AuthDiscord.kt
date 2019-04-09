package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthDiscord(

        @SerializedName("access_token") val access_token : String,
        @SerializedName("scope") val scope : String,
        @SerializedName("token_type") val token_type : String,
        @SerializedName("expires_in") val expires_in : Int,
        @SerializedName("refresh_token") val refresh_token : String,

        @SerializedName("error") val error : String,
        @SerializedName("error_description") val error_description : String,

        // Jobni block
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsDiscord
)

data class ErrorsDiscord(
        @SerializedName("social_account") val social_account : List<String>,
        @SerializedName("authorization") val authorization: List<String>
)

data class AuthDiscordJobni(
        @SerializedName("uid")val uid: String,
        @SerializedName("provider")val provider: String,
        @SerializedName("access_token")val access_token: String
)