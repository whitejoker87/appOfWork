package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Registration (
        @SerializedName("email")val email: String,
        @SerializedName("password")val password: String,
        @SerializedName("password_confirm")val password_confirm: String,
        @SerializedName("surname")val surname: String,
        @SerializedName("name")val name: String,
        @SerializedName("middlename")val middlename: String,
        @SerializedName("referer")val referer: String,
//        val photo: String,
        @SerializedName("contacts")val contacts: ArrayList<Contact>
    ):Serializable

data class Contact (
        @SerializedName("contact_type")val contact_type: String,
        @SerializedName("contact")val contact: String
):Serializable