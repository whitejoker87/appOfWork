package ru.jobni.jobni.model.network.company

import com.google.gson.annotations.SerializedName

data class CompanyVacancyList(
        @SerializedName("id") val id : Int,
        @SerializedName("name") val name : String,
        @SerializedName("archival") val archival : Boolean
)