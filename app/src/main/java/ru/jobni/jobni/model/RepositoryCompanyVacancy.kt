package ru.jobni.jobni.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object RepositoryCompanyVacancy {

    private val companyVacancyLiveData = MutableLiveData<MutableList<CompanyVacancyEntity>>()

    private val companyVacancies: MutableList<CompanyVacancyEntity> = mutableListOf()

    init {
        companyVacancyLiveData.value = companyVacancies
    }

    fun clearRepository() {
        return companyVacancyLiveData.value!!.clear()
    }

    fun getSize(): Int {
        return companyVacancyLiveData.value!!.size
    }

    fun getCompanyVacancy(): LiveData<MutableList<CompanyVacancyEntity>> {
        return companyVacancyLiveData
    }

    fun saveCompanyVacancy(vacancy: CompanyVacancyEntity) {
        addOrReplace(vacancy)
        companyVacancyLiveData.value = companyVacancies
    }

    private fun addOrReplace(vacancy: CompanyVacancyEntity) {
        for (i in 0 until companyVacancies.size) {
            if (companyVacancies[i] == vacancy) {
                companyVacancies[i] = vacancy
                return
            }
        }
        companyVacancies.add(vacancy)
    }
}