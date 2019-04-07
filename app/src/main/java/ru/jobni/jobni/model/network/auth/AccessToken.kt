package ru.jobni.jobni.model.network.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AccessToken {

    @SerializedName("access_token")
    @Expose
    var access_token: String? = null

}
