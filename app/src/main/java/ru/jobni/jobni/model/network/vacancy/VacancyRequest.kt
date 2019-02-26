package ru.jobni.jobni.model.network.vacancy

data class VacancyRequest(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<ResultsVacancy>
)

data class ResultsVacancy(
    val id: Int,
    val name: String,
    val company: Company,
    val update_date_time: String,
    val salary_level_newbie: Int,
    val labor_rate: String,
    val salary_level_experienced: Int,
    val views: Int,
    val invitations: Int,
    val responses: Int,
    val business_rate: String,
    val monthly_rate: Int,
    val rate_per_hour: Int,
    val salary_details: List<SalaryDetails>,
    val format_of_work: FormatOfWork,
    val employment: List<Employment>,
    val competences: List<Competences>
)

data class SalaryDetails(
    val type: Int,
    val condition: String,
    val gain_type: Int,
    val value_percentage: Int,
    val value_currency: Int,
    val comment: String
)

data class Profession(
    val id: Int,
    val name: String
)

data class FormatOfWork(
    val id: Int,
    val name: String
)

data class Employment(
    val id: Int,
    val name: String
)

data class Competences(
    val id: Int,
    val name: String,
    val profession: Profession
)

data class Company(
    val id: Int,
    val name: String
)