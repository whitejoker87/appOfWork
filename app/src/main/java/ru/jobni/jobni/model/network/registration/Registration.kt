package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header

//data class Registration (
//        @SerializedName("email")val email: String,
//        @SerializedName("password")val password: String,
//        @SerializedName("password_confirm")val password_confirm: String,
//        @SerializedName("surname")val surname: String,
//        @SerializedName("name")val name: String,
//        @SerializedName("middlename")val middlename: String,
//        @SerializedName("referer")val referer: String,
////        val photo: String,
//        @SerializedName("contacts")val contacts: ArrayList<Contact>
//    ):Serializable

data class Contact (
        @SerializedName("contact_type")val contact_type: String,
        @SerializedName("contact")val contact: String
)

data class RegUser(
        @SerializedName("password")val password: String,
        @SerializedName("password_confirm")val password_confirm: String
)

data class BindEmail(
        @SerializedName("email")val email: String
)

data class MailCode(
        @SerializedName("code")val code: String
)

data class RegContactFace(
        @SerializedName("surname")val surname: String,
        @SerializedName("name")val name: String,
        @SerializedName("middlename")val middlename: String
)

data class RegContactFaceContact(
        @SerializedName("consent_to_data_storage_and_protection")val protection: Boolean,
        @SerializedName("consent_public_offers")val offers: Boolean,
        @SerializedName("contacts")val contacts: ArrayList<Contact>
)

data class ResponseReg(
        @SerializedName("success")val success: Boolean,
        @SerializedName("error_text")val error_text:List<String>
)

data class ResponseRegPass(
        @SerializedName("success")val success: Boolean,
        @SerializedName("error_text")val error_text: ListErrors

)

data class ListErrors(
        @SerializedName("password")val password: List<String>,
        @SerializedName("password_general")val password_general:List<String>
)