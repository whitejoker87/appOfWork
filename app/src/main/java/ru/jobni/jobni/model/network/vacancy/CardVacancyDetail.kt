package ru.jobni.jobni.model.network.vacancy

import com.google.gson.annotations.SerializedName

data class CardVacancyDetail(
    @SerializedName("detail") val detail : Detail
)

data class Detail (
    @SerializedName("description") val description : String,
    @SerializedName("company_description") val company_description : String,
    @SerializedName("requirements") val requirements : String,
    @SerializedName("duties") val duties : String
)