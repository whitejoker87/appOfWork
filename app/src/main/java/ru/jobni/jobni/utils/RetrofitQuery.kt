package ru.jobni.jobni.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.jobni.jobni.model.auth.UserAuth
import ru.jobni.jobni.model.network.company.CompanyVacancy
import ru.jobni.jobni.model.network.registration.*
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

    @POST("api/authorization/")
    fun postAuthData(@Header("Authorization") basicAuth: String, @Body user: UserAuth): Call<ResponseBody>

    @POST("api/registration_user/")
    fun sendRegistrationUser(@Body user: RegUser): Call<ResponseRegPass>

    @POST("api/bind_email/")
    fun sendBindEmail(@Header("Cookie") sid: String, @Body bindEmail: BindEmail): Call<ResponseReg>

    @POST("api/validate_email_code/")
    fun validateMailCode(@Header("Cookie") sid: String, @Body mailCode: MailCode): Call<ResponseRegConfirmMail>

    @POST("api/registration_contactface/")
    fun sendRegistrationContactFace(@Header("Cookie") sessionID: String, @Body contactFace: RegContactFace): Call<ResponseRegContacts>

    @POST("api/registration_contactfacecontact/")
    fun sendRegistrationContactFaceContact(@Header("Cookie") sid:String, @Body contacts: RegContactFaceContact): Call<ResponseReg>

    @GET("api/type_authorization/?format=json/")
    fun getContactsForReg(@Header("Cookie") sid:String): Call<ResponseReg>

//    @Multipart
//    @POST("api/registration/")
//    fun sendRegistrationData(/*@Part("info")  info: RequestBody*/@Part info: MultipartBody.Part, @Part image: MultipartBody.Part): Call<ResponseBody>

    @GET("api/company/?owner_or_worker=1")
    fun ownerOrWorker(@Header("Cookie") sessionID: String): Call<CompanyVacancy>

    @GET("api/company/{id}/balance/")
    fun ownerOrWorkerBalance(@Header("Cookie") sessionID: String, @Path("id") id: Int): Call<Int>

    @GET("api/vacancy/")
    fun ownerOrWorkerCompany(@Header("Cookie") sessionID: String, @Query("company") detail: Int): Call<CardVacancy>
}


