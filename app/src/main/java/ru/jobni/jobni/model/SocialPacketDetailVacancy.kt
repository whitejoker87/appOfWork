package ru.jobni.jobni.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SocialPacketDetailVacancy {

    @SerializedName("housing")
    @Expose
    var housing: String? = null
    @SerializedName("travel")
    @Expose
    var travel: String? = null
    @SerializedName("meals")
    @Expose
    var meals: String? = null
    @SerializedName("gym")
    @Expose
    var gym: String? = null
    @SerializedName("childcare_facilities")
    @Expose
    var childcareFacilities: String? = null
    @SerializedName("training")
    @Expose
    var training: String? = null

}
