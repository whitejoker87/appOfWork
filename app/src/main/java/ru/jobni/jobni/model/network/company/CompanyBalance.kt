package ru.jobni.jobni.model.network.company

import com.google.gson.annotations.SerializedName

data class CompanyBalance(

        @SerializedName("success") val success: Boolean,
        @SerializedName("errors") val errors: ErrorsCompanyBalance,
        @SerializedName("result") val result: ResultCompanyBalance
)

data class ErrorsCompanyBalance(
        @SerializedName("unknown") val unknown : List<String>
)

data class ResultCompanyBalance(
        @SerializedName("balance") val balance: Int
)