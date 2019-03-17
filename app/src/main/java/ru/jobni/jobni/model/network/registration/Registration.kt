package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName

data class Registration (
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("password_confirm") val password_confirm: String,
        @SerializedName("surname") val surname: String,
        @SerializedName("name") val name: String,
        @SerializedName("middlename") val middlename: String,
        @SerializedName("referer") val referer: String,
        @SerializedName("photo") val photo: String,
        @SerializedName("contacts") val contacts: List<String>
    )