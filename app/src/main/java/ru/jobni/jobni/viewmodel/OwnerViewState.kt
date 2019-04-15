package ru.jobni.jobni.viewmodel

import ru.jobni.jobni.model.network.company.ResultsCompanyList

data class OwnerViewState(val companyList: MutableList<ResultsCompanyList>)