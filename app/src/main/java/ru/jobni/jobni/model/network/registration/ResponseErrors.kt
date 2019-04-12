package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName

data class RegUserErrors(
        @SerializedName("email")val email: List<String>,
        @SerializedName("phone")val phone: List<String>,
        @SerializedName("uid")val uid: List<String>

)

data class RegPassErrors(
        @SerializedName("non_field_errors")val non_field_errors: List<String>,
        @SerializedName("code")val code: List<String>
)

data class RegContactFaceErrors(
        @SerializedName("name")val name: List<String>,
        @SerializedName("surname")val surname: List<String>,
        @SerializedName("middlename")val middlename: List<String>
)

data class RegGetContactsResult(
        @SerializedName("name")val name: String,
        @SerializedName("surname")val surname: String,
        @SerializedName("middlename")val middlename: String,
        @SerializedName("referer")val referer: String,
        @SerializedName("photo")val photo: String,
        @SerializedName("consent_to_data_storage_and_protection")val consent_to_data_storage_and_protection: Boolean,
        @SerializedName("consent_public_offers")val consent_public_offers: Boolean,
        @SerializedName("login")val login: String,
        @SerializedName("password")val password: Boolean,
        @SerializedName("contacts")val contacts: List<ContactOptions>

)

data class RegContactFaceContactsErrors(
        @SerializedName("contact_face")val contact_face: List<String>
)

data class ContactOptions(
        @SerializedName("contact_type")val contact_type: String,
        @SerializedName("contact_id")val contact_id: Int,
        @SerializedName ("contact")val contact: String,
        @SerializedName("incomplete_registration")val incomplete_registration: Boolean
)