package ru.jobni.jobni.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object RepositoryVacancyEntity {

    private val vacancyLiveData = MutableLiveData<MutableList<VacancyEntity>>()

    private val vacancies: MutableList<VacancyEntity> = mutableListOf()

    init {
        vacancyLiveData.value = vacancies
    }

    fun clearRepository() {
        return vacancyLiveData.value!!.clear()
    }

    fun getSize(): Int {
        return vacancyLiveData.value!!.size
    }

    fun getVacancy(): LiveData<MutableList<VacancyEntity>> {
        return vacancyLiveData
    }

    fun saveVacancy(vacancy: VacancyEntity) {
        addOrReplace(vacancy)
        vacancyLiveData.value = vacancies
    }

    private fun addOrReplace(vacancy: VacancyEntity) {
        for (i in 0 until vacancies.size) {
            if (vacancies[i] == vacancy) {
                vacancies[i] = vacancy
                return
            }
        }
        vacancies.add(vacancy)
    }
}