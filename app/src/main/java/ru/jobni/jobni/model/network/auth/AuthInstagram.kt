package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthInstagram(

        @SerializedName("access_token") val access_token: String,
        @SerializedName("user") val user: User,

        @SerializedName("error_type") val error_type: String,
        @SerializedName("code") val code: Int,
        @SerializedName("error_message") val error_message: String
)

data class User(

        @SerializedName("id") val id: Int,
        @SerializedName("username") val username: String,
        @SerializedName("profile_picture") val profile_picture: String,
        @SerializedName("full_name") val full_name: String,
        @SerializedName("bio") val bio: String,
        @SerializedName("website") val website: String,
        @SerializedName("is_business") val is_business: Boolean
)