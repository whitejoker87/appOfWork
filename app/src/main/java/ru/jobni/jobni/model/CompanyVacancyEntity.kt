package ru.jobni.jobni.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyVacancyEntity(
        val id: Int,
        val vacancyName: String,
        val vacancyArchival: Boolean
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompanyVacancyEntity

        if (id != other.id) return false
        return true
    }

    override fun hashCode() = id.hashCode()
}