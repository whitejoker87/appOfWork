package ru.jobni.jobni.model.network.company

import com.google.gson.annotations.SerializedName

data class CompanyList(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsCompanyList,

        @SerializedName("count") val count: Int,
        @SerializedName("next") val next: String,
        @SerializedName("previous") val previous: String,
        @SerializedName("results") val results: ArrayList<ResultsCompanyList>
)

data class ResultsCompanyList(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("archival") val archival: Boolean
)

data class ErrorsCompanyList (
        @SerializedName("unknown") val unknown : List<String>
)