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

    /*API registration*/
    @POST("api/registration/user/")
    fun postRegistrationUser(): Call<ResponseRegStart>

    @POST("api/registration/bind_email/")
    fun sendBindEmail(@Header("Cookie") sessionID: String, @Body bindEmail: BindEmail): Call<ResponseRegUser>

    @POST("api/registration/email_code/")
    fun validateMailCode(@Header("Cookie") sessionID: String, @Body mailCode: MailCode): Call<ResponseRegConfirmMail>

    @POST("api/registration/password/")
    fun sendRegistrationPass(@Header("Cookie") sessionID: String, @Body pass: RegPass): Call<ResponseRegPass>

    @POST("api/registration/contact_face/")
    fun sendRegistrationContactFace(@Header("Cookie") sessionID: String, @Body contactFace: RegContactFace): Call<ResponseRegContactFace>

    @POST("api/registration/contact_face_contact/")
    fun sendRegistrationContactFaceContact(@Header("Cookie") sessionID: String, @Body contacts: RegContactFaceContact): Call<ResponseRegContactFaceContacts>

    @GET("api/registration/get_stages/")
    fun getContactsForReg(@Header("Cookie") sessionID: String): Call<ResponseRegGetContacts>

    @POST("api/accounts/{type_social}/login/")
    fun postSocialReg(@Header("Cookie") sessionID: String, @Path("type_social") type_social: String, @Query("process") process: String): Call<ResponseBody>

    /*заготовка для отправки картинки*/
//    @Multipart
//    @POST("api/registration/")
//    fun sendRegistrationData(/*@Part("info")  info: RequestBody*/@Part info: MultipartBody.Part, @Part image: MultipartBody.Part): Call<ResponseBody>


    /*API authorization*/
    @POST("api/authorization/")
    fun postAuthData(@Header("Authorization") basicAuth: String, @Body user: UserAuth): Call<ResponseBody>

    /*API vacancy*/
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

    @GET("api/vacancy/")
    fun ownerOrWorkerCompany(@Header("Cookie") sessionID: String, @Query("company") detail: Int): Call<CardVacancy>

    /*API company*/
    @GET("api/company/?owner_or_worker=1")
    fun ownerOrWorker(@Header("Cookie") sessionID: String): Call<CompanyVacancy>

    @GET("api/company/{id}/balance/")
    fun ownerOrWorkerBalance(@Header("Cookie") sessionID: String, @Path("id") id: Int): Call<Int>

}


