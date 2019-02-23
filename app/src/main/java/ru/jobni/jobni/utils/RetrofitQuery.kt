package ru.jobni.jobni.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitQuery {
    @GET("api/search_competence_by_vacancy/")
    fun loadCompetence(@Query("name") competenceName: String): Call<List<String>>
}
