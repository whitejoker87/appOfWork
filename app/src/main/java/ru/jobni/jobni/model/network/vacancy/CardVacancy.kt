package ru.jobni.jobni.model.network.vacancy

import com.google.gson.annotations.SerializedName

data class CardVacancy(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val results: List<ResultsVacancy>
)

data class ResultsVacancy(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("company") val company : Company,
    @SerializedName("update_date_time") val update_date_time : String,
    @SerializedName("salary_level_newbie") val salary_level_newbie : Int,
    @SerializedName("labor_rate") val labor_rate : String,
    @SerializedName("salary_level_experienced") val salary_level_experienced : Int,
    @SerializedName("views") val views : Int,
    @SerializedName("invitations") val invitations : Int,
    @SerializedName("responses") val responses : Int,
    @SerializedName("interviews") val interviews : Int,
    @SerializedName("business_rate") val business_rate : String,
    @SerializedName("monthly_rate") val monthly_rate : String,
    @SerializedName("rate_per_hour") val rate_per_hour : String,
    @SerializedName("salary_details") val salary_details : List<SalaryDetails>,
    @SerializedName("status") val status : String,
    @SerializedName("format_of_work") val format_of_work : FormatOfWork,
    @SerializedName("employment") val employment : List<Employment>,
    @SerializedName("competences") val competences : List<Competences>,
    @SerializedName("viewed") val viewed : Boolean,
    @SerializedName("tags") val tags : List<String>
)

data class FormatOfWork(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)

data class Employment(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)

data class Competences(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("level_of_proficiency_custom") val level_of_proficiency_custom : Boolean
)

data class Company(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("number_of") val number_of : String,
    @SerializedName("internationality") val internationality : Boolean,
    @SerializedName("logo") val logo : Logo
)

data class Logo (
    @SerializedName("image") val image : String,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String
)

data class SalaryDetails (
    @SerializedName("type") val type : String,
    @SerializedName("gain_type") val gain_type : String,
    @SerializedName("condition") val condition : String,
    @SerializedName("value_percentage") val value_percentage : Int,
    @SerializedName("value_currency") val value_currency : Int,
    @SerializedName("comment") val comment : String
)