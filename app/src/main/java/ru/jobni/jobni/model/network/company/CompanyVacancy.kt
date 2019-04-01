package ru.jobni.jobni.model.network.company

import com.google.gson.annotations.SerializedName

data class CompanyVacancy(
        @SerializedName("count") val count: Int,
        @SerializedName("next") val next: String,
        @SerializedName("previous") val previous: String,
        @SerializedName("results") val results: ArrayList<ResultsCompany>
)

data class ResultsCompany(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("logo") val logo: Logo,
        @SerializedName("labor_rate") val labor_rate: String,
        @SerializedName("business_rate") val business_rate: String,
        @SerializedName("tags") val tags: List<String>,
        @SerializedName("status") val status: String,
        @SerializedName("about_company_short") val about_company_short: String,
        @SerializedName("number_of") val number_of: Int,
        @SerializedName("internationality") val internationality: Boolean,
        @SerializedName("age") val age: Int,
        @SerializedName("views") val views: Int,
        @SerializedName("interview") val interview: Int,
        @SerializedName("responses") val responses: Int,
        @SerializedName("online") val online: Int,
        @SerializedName("viewed") val viewed: Boolean
)

data class Logo(
        @SerializedName("image") val image: String,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String
)