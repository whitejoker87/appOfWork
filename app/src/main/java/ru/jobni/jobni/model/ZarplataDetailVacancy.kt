package ru.jobni.jobni.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ZarplataDetailVacancy {

    @SerializedName("salary_level_newbie")
    @Expose
    var salaryLevelNewbie: String? = null
    @SerializedName("salary_level_experienced")
    @Expose
    var salaryLevelExperienced: String? = null
    @SerializedName("monthly_rate")
    @Expose
    var monthlyRate: String? = null
    @SerializedName("rate_per_hour")
    @Expose
    var ratePerHour: String? = null
}

