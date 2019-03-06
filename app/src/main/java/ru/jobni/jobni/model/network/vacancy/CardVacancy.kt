package ru.jobni.jobni.model.network.vacancy

import com.google.gson.annotations.SerializedName

data class CardVacancy(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val results: List<ResultsVacancy>
)

data class ResultsVacancy(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("company") val company: Company,
    @SerializedName("update_date_time") val update_date_time: String,
    @SerializedName("salary_level_newbie") val salary_level_newbie: Int,
    @SerializedName("labor_rate") val labor_rate: String,
    @SerializedName("salary_level_experienced") val salary_level_experienced: Int,
    @SerializedName("views") val views: Int,
    @SerializedName("invitations") val invitations: Int,
    @SerializedName("responses") val responses: Int,
    @SerializedName("business_rate") val business_rate: String,
    @SerializedName("monthly_rate") val monthly_rate: Int,
    @SerializedName("rate_per_hour") val rate_per_hour: Int,
    @SerializedName("salary_details") val salary_details: List<String>,
    @SerializedName("format_of_work") val format_of_work: FormatOfWork,
    @SerializedName("employment") val employment: List<Employment>,
    @SerializedName("competences") val competences: List<Competences>,
    @SerializedName("viewed") val viewed: Boolean
)

data class FormatOfWork(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class Employment(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class Competences(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("level_of_proficiency_custom") val level_of_proficiency_custom: Boolean,
    @SerializedName("profession") val profession: Int
)

data class Company(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("number_of") val number_of: String,
    @SerializedName("internationality") val internationality: Boolean,
    @SerializedName("logo") val logo: Logo
)

data class Logo(
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)