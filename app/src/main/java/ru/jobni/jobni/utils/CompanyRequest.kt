package ru.jobni.jobni.utils

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.jobni.jobni.model.Results

class CompanyRequest {

    @SerializedName("count")
    @Expose
    val count: Int = 0

    @SerializedName("next")
    @Expose
    val next: Int = 0

    @SerializedName("previous")
    @Expose
    val previous: Int = 0

    @SerializedName("results")
    @Expose
    val results: Array<Results>? = null
}
