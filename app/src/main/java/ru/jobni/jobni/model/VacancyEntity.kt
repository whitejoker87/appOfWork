package ru.jobni.jobni.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VacancyEntity(
        val id: Int,
        val vacancyName: String,
        val companyName: String,
        val salaryLevelNewbie: String,
        val salaryLevelExperienced: String,
        val formatOfWork: String,
        val employmentList: List<String>,
        val competenceList: List<String>,
        val companyDescription: String,
        val vacancyDescription: String,
        val requirementsDescription: String,
        val dutiesDescription: String
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VacancyEntity

        if (id != other.id) return false
        return true
    }

    override fun hashCode() = id.hashCode()
}