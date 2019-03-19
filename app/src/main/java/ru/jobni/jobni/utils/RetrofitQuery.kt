package ru.jobni.jobni.utils

import retrofit2.Call
import retrofit2.http.*
import ru.jobni.jobni.UserCredential
import ru.jobni.jobni.model.network.vacancy.CardVacancy
import ru.jobni.jobni.model.network.vacancy.CardVacancyDetail
import ru.jobni.jobni.model.network.vacancy.DetailVacancy


interface RetrofitQuery {
    @GET("api/filter/detail/vacancy")
    fun loadDetailVacancy(): Call<DetailVacancy>

    @GET("api/search_competence_by_vacancy/")
    fun loadCompetence(@Query("name") competenceName: String, @Query("count") count: Int): Call<List<String>>

    @GET("api/vacancy/")
    fun loadVacancy(): Call<CardVacancy>

    @GET("api/vacancy/")
    fun loadVacancyNext(@Query("limit") competenceLimit: Int, @Query("offset") competenceOffset: Int): Call<CardVacancy>

    @GET("api/vacancy/")
    fun loadVacancyByCompetence(@Query("by_competence") competenceName: String): Call<CardVacancy>

    @GET("api/vacancy/{id}/details/")
    fun loadVacancyCard(@Path("id") id: Int, @Query("detail") detail: Int): Call<CardVacancyDetail>

//    @FormUrlEncoded
//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    @POST("api/authorization/")

    @GET("api/tags/")
    fun getNewsFeed(@Header("Authorization") h1:String):Call<UserCredential>

    @POST("api/authorization/")
    fun login(@Header("Authorization") basicAuth: String, @Body userCred: UserCredential): Call<UserCredential>
}
