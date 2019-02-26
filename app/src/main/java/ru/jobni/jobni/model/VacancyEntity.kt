package ru.jobni.jobni.model

data class VacancyEntity(
    val vacancyName: String,
    val companyName: String,
    val salaryLevelNewbie: String,
    val salaryLevelExperienced: String,
    val formatOfWork: String,
    val employmentList: List<String>,
    val competenceList: List<String>
)