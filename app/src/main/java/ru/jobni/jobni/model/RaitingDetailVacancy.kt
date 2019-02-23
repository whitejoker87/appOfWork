package ru.jobni.jobni.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RaitingDetailVacancy {

    @SerializedName("labor_rating")
    @Expose
    var laborRating: String? = null
    @SerializedName("business_rating")
    @Expose
    var businessRating: String? = null

}
