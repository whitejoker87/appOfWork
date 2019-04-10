package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName

data class RegStartResult(
        @SerializedName("id")val id: Int
)

data class RegUserResult(
        @SerializedName("_id")val _id: Int
)