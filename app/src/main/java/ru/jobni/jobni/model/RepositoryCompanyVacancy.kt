package ru.jobni.jobni.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object RepositoryCompanyVacancy {

    private val companyVacancyLiveData = MutableLiveData<MutableList<VacancyEntity>>()

    private val companyVacancies: MutableList<VacancyEntity> = mutableListOf()

    init {
        companyVacancyLiveData.value = companyVacancies
    }

    fun clearRepository() {
        return companyVacancyLiveData.value!!.clear()
    }

    fun getSize(): Int {
        return companyVacancyLiveData.value!!.size
    }

    fun getCompanyVacancy(): LiveData<MutableList<VacancyEntity>> {
        return companyVacancyLiveData
    }

    fun saveCompanyVacancy(vacancy: VacancyEntity) {
        addOrReplace(vacancy)
        companyVacancyLiveData.value = companyVacancies
    }

    private fun addOrReplace(vacancy: VacancyEntity) {
        for (i in 0 until companyVacancies.size) {
            if (companyVacancies[i] == vacancy) {
                companyVacancies[i] = vacancy
                return
            }
        }
        companyVacancies.add(vacancy)
    }
}