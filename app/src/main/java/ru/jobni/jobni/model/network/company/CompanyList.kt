package ru.jobni.jobni.model.network.company

import com.google.gson.annotations.SerializedName

data class CompanyList(
        @SerializedName("id") val id : Int,
        @SerializedName("name") val name : String,
        @SerializedName("archival") val archival : Boolean
)