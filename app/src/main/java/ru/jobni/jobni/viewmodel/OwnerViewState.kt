package ru.jobni.jobni.viewmodel

import ru.jobni.jobni.model.network.company.CompanyList

data class OwnerViewState(val companyList: MutableList<CompanyList>)