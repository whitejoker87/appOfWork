package ru.jobni.jobni.model

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
)