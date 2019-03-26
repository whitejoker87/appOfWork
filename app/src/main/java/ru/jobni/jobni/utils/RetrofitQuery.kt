package ru.jobni.jobni.utils

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.jobni.jobni.model.auth.UserAuth
import ru.jobni.jobni.model.network.vacancy.CardVacancy
import ru.jobni.jobni.model.network.vacancy.CardVacancyDetail
import ru.jobni.jobni.model.network.vacancy.DetailVacancy

interface RetrofitQuery {
    @GET("api/filter/detail/vacancy")
    fun loadDetailVacancy(): Call<DetailVacancy>

    @GET("api/search_competence_by_vacancy/")
    fun loadCompetence(@Query("name") competenceName: String, @Query("count") count: Int): Call<List<String>>

    @GET("api/vacancy/")
    fun loadVacancy(@Query("limit") competenceLimit: Int, @Query("offset") competenceOffset: Int): Call<CardVacancy>

    @GET("api/vacancy/")
    fun loadVacancyByCompetence(@Query("by_competence") competenceName: String): Call<CardVacancy>

    @GET("api/vacancy/{id}/details/")
    fun loadVacancyCard(@Path("id") id: Int, @Query("detail") detail: Int): Call<CardVacancyDetail>

    @GET("api/tags/")
    fun getAuthData(@Header("Cookie") h1:String):Call<UserAuth>

    @POST("api/authorization/")
    fun postAuthData(@Header("Authorization") basicAuth: String, @Body user: UserAuth): Call<UserAuth>

    @Multipart
    @POST("api/registration/")
    fun sendRegistrationData(/*@Part("info")  info: RequestBody*/@Part info: MultipartBody.Part, @Part image: MultipartBody.Part): Call<ResponseBody>
}
