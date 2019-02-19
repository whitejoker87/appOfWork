package ru.jobni.jobni.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Results {

    @SerializedName("id")
    @Expose
    val id: Int = 0

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("logo")
    @Expose
    val logo: String? = null

    @SerializedName("labor_rate")
    @Expose
    val labor_rate: String? = null

    @SerializedName("business_rate")
    @Expose
    val business_rate: String? = null

    @SerializedName("about_company_short")
    @Expose
    val about_company_short: String? = null

    @SerializedName("number_of")
    @Expose
    val number_of: String? = null

    @SerializedName("internationality")
    @Expose
    val internationality: String? = null

    @SerializedName("age")
    @Expose
    val age: Int = 0
}
