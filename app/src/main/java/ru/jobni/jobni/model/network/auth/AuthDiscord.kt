package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthDiscord(

        // Блок кода для обрабоки access_token
        @SerializedName("access_token") val access_token : String,
        @SerializedName("scope") val scope : String,
        @SerializedName("token_type") val token_type : String,
        @SerializedName("expires_in") val expires_in : Int,
        @SerializedName("refresh_token") val refresh_token : String,
        @SerializedName("userid") val userid : String,

        @SerializedName("error") val error : String,
        @SerializedName("error_description") val error_description : String,

        // Блок кода для обрабоки id пользователя
        @SerializedName("username") val username : String,
        @SerializedName("locale") val locale : String,
        @SerializedName("mfa_enabled") val mfa_enabled : Boolean,
        @SerializedName("flags") val flags : Int,
        @SerializedName("avatar") val avatar : String,
        @SerializedName("discriminator") val discriminator : Int,
        @SerializedName("id") val id : Long,

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