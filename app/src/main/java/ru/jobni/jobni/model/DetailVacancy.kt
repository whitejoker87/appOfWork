package ru.jobni.jobni.model

import com.google.gson.annotations.SerializedName

data class DetailVacancy (

    @SerializedName("competence") val competence : String,
    @SerializedName("languages") val languages : String,
    @SerializedName("work_places") val work_places : String,
    @SerializedName("employment") val employment : String,
    @SerializedName("format_of_work") val format_of_work : String,
    @SerializedName("field_of_activity") val field_of_activity : String,
    @SerializedName("age_company") val age_company : String,
    @SerializedName("required_number_of_people") val required_number_of_people : String,
    @SerializedName("zarplata") val zarplata : Zarplata,
    @SerializedName("social_packet") val social_packet : Social_packet,
    @SerializedName("auto") val auto : Auto,
    @SerializedName("raiting") val raiting : Raiting
)

data class Auto (

    @SerializedName("year_of_manufacture") val year_of_manufacture : String,
    @SerializedName("fuel_consumption_city") val fuel_consumption_city : String,
    @SerializedName("fuel_consumption_highway") val fuel_consumption_highway : String,
    @SerializedName("cargo_capacity") val cargo_capacity : String,
    @SerializedName("capacity_of_people") val capacity_of_people : String
)

data class Raiting (

    @SerializedName("labor_rating") val labor_rating : String,
    @SerializedName("business_rating") val business_rating : String
)

data class Social_packet (

    @SerializedName("housing") val housing : String,
    @SerializedName("travel") val travel : String,
    @SerializedName("meals") val meals : String,
    @SerializedName("gym") val gym : String,
    @SerializedName("childcare_facilities") val childcare_facilities : String,
    @SerializedName("training") val training : String
)

data class Zarplata (

    @SerializedName("salary_level_newbie") val salary_level_newbie : String,
    @SerializedName("salary_level_experienced") val salary_level_experienced : String,
    @SerializedName("monthly_rate") val monthly_rate : String,
    @SerializedName("rate_per_hour") val rate_per_hour : String
)

