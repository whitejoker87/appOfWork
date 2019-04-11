package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthInstagram(

        @SerializedName("access_token") val access_token: String,
        @SerializedName("user") val user: User,

        @SerializedName("error_type") val error_type: String,
        @SerializedName("code") val code: Int,
        @SerializedName("error_message") val error_message: String,

        // Jobni block
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsInstagram
)

data class ErrorsInstagram(
        @SerializedName("social_account") val social_account : List<String>,
        @SerializedName("authorization") val authorization: List<String>
)

data class AuthInstagramJobni(
        @SerializedName("uid")val uid: String,
        @SerializedName("provider")val provider: String,
        @SerializedName("access_token")val access_token: String
)

data class User(

        @SerializedName("id") val id: Long,
        @SerializedName("username") val username: String,
        @SerializedName("profile_picture") val profile_picture: String,
        @SerializedName("full_name") val full_name: String,
        @SerializedName("bio") val bio: String,
        @SerializedName("website") val website: String,
        @SerializedName("is_business") val is_business: Boolean
)