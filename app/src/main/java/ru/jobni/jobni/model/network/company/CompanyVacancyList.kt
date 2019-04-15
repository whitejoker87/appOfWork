package ru.jobni.jobni.model.network.company

import com.google.gson.annotations.SerializedName

data class CompanyVacancyList(
        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsCompanyVacancyList,

        @SerializedName("count") val count: Int,
        @SerializedName("next") val next: String,
        @SerializedName("previous") val previous: String,
        @SerializedName("results") val results: ArrayList<ResultsCompanyVacancyList>
)

data class ResultsCompanyVacancyList(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("archival") val archival: Boolean
)

data class ErrorsCompanyVacancyList(
        @SerializedName("unknown") val unknown : List<String>
)