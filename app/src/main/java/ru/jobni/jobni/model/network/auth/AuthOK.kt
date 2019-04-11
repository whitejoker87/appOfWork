package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.SerializedName

data class AuthOK(

        // Блок кода для обрабоки id пользователя
        @SerializedName("uid") val uid : Long,
        @SerializedName("birthday") val birthday : String,
        @SerializedName("birthdaySet") val birthdaySet : Boolean,
        @SerializedName("age") val age : Int,
        @SerializedName("first_name") val first_name : String,
        @SerializedName("last_name") val last_name : String,
        @SerializedName("name") val name : String,
        @SerializedName("locale") val locale : String,
        @SerializedName("gender") val gender : String,
        @SerializedName("has_email") val has_email : Boolean,
        @SerializedName("location") val location : Location,
        @SerializedName("online") val online : String,
        @SerializedName("pic_1") val pic_1 : String,
        @SerializedName("pic_2") val pic_2 : String,
        @SerializedName("pic_3") val pic_3 : String,

        @SerializedName("error_code") val error_code : Int,
        @SerializedName("error_msg") val error_msg : String,
        @SerializedName("error_data") val error_data : String,

        // Jobni block
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsOK
)

data class ErrorsOK(
        @SerializedName("social_account") val social_account : List<String>,
        @SerializedName("authorization") val authorization: List<String>
)

data class AuthOKJobni(
        @SerializedName("uid")val uid: String,
        @SerializedName("provider")val provider: String,
        @SerializedName("access_token")val access_token: String
)

data class Location (
        @SerializedName("city") val city : String,
        @SerializedName("country") val country : String,
        @SerializedName("countryCode") val countryCode : String,
        @SerializedName("countryName") val countryName : String
)