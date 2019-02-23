package ru.jobni.jobni.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.jobni.jobni.model.DetailVacancy

interface RetrofitQuery {
    @GET("api/company/")
    fun loadCompany(@Query("by_name") companyName: String): Call<CompanyRequest>
    @GET("api/filter/detail/vacancy")
    fun loadDetailVacancy(): Call<DetailVacancy>
}
