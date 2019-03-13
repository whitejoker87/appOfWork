package ru.jobni.jobni.viewmodel

import ru.jobni.jobni.model.VacancyEntity

data class MainFragmentViewState(val vacancyList: MutableList<VacancyEntity>)