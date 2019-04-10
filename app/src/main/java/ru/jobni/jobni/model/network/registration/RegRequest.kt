package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName

data class RegContact (
        @SerializedName("contact_id")val contact_id: Int,
        @SerializedName("contact_type")val contact_type: String,
        @SerializedName("contact")val contact: String
)

data class RegContactWithoutId (
    @SerializedName("contact_type")val contact_type: String,
    @SerializedName("contact")val contact: String
)

data class RegPass(
        @SerializedName("password")val password: String,
        @SerializedName("password_confirm")val password_confirm: String
)

data class RegBindEmail(
        @SerializedName("email")val email: String
)

data class RegBindPhone(
    @SerializedName("phone")val phone: String
)

data class RegConfirmCode(
        @SerializedName("code")val code: String
)

data class RegSocial(
        @SerializedName("uid")val uid: String,
        @SerializedName("provider")val provider: String,
        @SerializedName("access_token")val access_token: String
)

data class RegContactFace(
        @SerializedName("surname")val surname: String,
        @SerializedName("name")val name: String,
        @SerializedName("middlename")val middlename: String
)

data class RegContactFaceContact(
        @SerializedName("consent_to_data_storage_and_protection")val protection: Boolean,
        @SerializedName("consent_public_offers")val offers: Boolean,
        @SerializedName("contacts")val contacts: ArrayList<Any>
)