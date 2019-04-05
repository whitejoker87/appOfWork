package ru.jobni.jobni.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.jobni.jobni.model.auth.mail.UserMailAuth
import ru.jobni.jobni.model.auth.phone.UserPhoneAuth
import ru.jobni.jobni.model.network.company.CompanyList
import ru.jobni.jobni.model.network.company.CompanyVacancyList
import ru.jobni.jobni.model.network.registration.*
import ru.jobni.jobni.model.network.vacancy.CardVacancy
import ru.jobni.jobni.model.network.vacancy.CardVacancyDetail
import ru.jobni.jobni.model.network.vacancy.DetailVacancy

interface RetrofitQuery {

    /*API registration*/
    @POST("api/registration_user/")
    fun postRegistrationUser(): Call<ResponseRegPass>

    @POST("api/registration_user/")
    fun sendRegistrationUser(@Body user: RegUser): Call<ResponseRegPass>

    @POST("api/bind_email/")
    fun sendBindEmail(@Header("Cookie") sessionID: String, @Body bindEmail: BindEmail): Call<ResponseReg>

    @POST("api/validate_email_code/")
    fun validateMailCode(@Header("Cookie") sessionID: String, @Body mailCode: MailCode): Call<ResponseRegConfirmMail>

    @POST("api/registration_contactface/")
    fun sendRegistrationContactFace(@Header("Cookie") sessionID: String, @Body contactFace: RegContactFace): Call<ResponseRegContacts>

    @POST("api/registration_contactfacecontact/")
    fun sendRegistrationContactFaceContact(@Header("Cookie") sessionID: String, @Body contacts: RegContactFaceContact): Call<ResponseReg>

    @GET("api/type_authorization/?format=json/")
    fun getContactsForReg(@Header("Cookie") sessionID: String): Call<ResponseReg>

    @POST("api/accounts/{type_social}/login/")
    fun postSocialReg(@Header("Cookie") sessionID: String, @Path("type_social") type_social: String, @Query("process") process: String): Call<ResponseBody>

    /*заготовка для отправки картинки*/
//    @Multipart
//    @POST("api/registration/")
//    fun sendRegistrationData(/*@Part("info")  info: RequestBody*/@Part info: MultipartBody.Part, @Part image: MultipartBody.Part): Call<ResponseBody>


    @POST("api/registration/social_account/")
    fun postVKAuthData(@Header("Cookie") sessionID: String, @Body userMail: RegVK): Call<ResponseBody>

    @POST("api/accounts/vk/login/?process=login")
    fun postVKAuthUP(@Header("Cookie") sessionID: String): Call<ResponseBody>

    /*API authorization*/
    @POST("api/authorization/mailbox/")
    fun postAuthData(@Header("Authorization") basicAuth: String, @Body userMail: UserMailAuth): Call<ResponseBody>

    @POST("api/authorization/phone/")
    fun postAuthDataPhone(@Header("Authorization") basicAuth: String, @Body userPhone: UserPhoneAuth): Call<ResponseBody>

    /*API vacancy*/
    @GET("api/filter/detail/vacancy/")
    fun loadDetailVacancy(): Call<DetailVacancy>

    @GET("api/search_competence_by_vacancy/")
    fun loadCompetence(@Query("name") competenceName: String, @Query("count") count: Int): Call<List<String>>

    @GET("api/vacancy/")
    fun loadVacancy(@Query("limit") competenceLimit: Int, @Query("offset") competenceOffset: Int): Call<CardVacancy>

    @GET("api/vacancy/")
    fun loadVacancyByCompetence(@Query("by_competence") competenceName: String): Call<CardVacancy>

    @GET("api/vacancy/{id}/details/")
    fun loadVacancyCard(@Path("id") id: Int, @Query("detail") detail: Int): Call<CardVacancyDetail>

    @GET("api/company/{id}/get_short_representation_of_vacancies/")
    fun ownerOrWorkerCompanyVacancy(@Header("Cookie") sessionID: String, @Path("id") id: Int): Call<ArrayList<CompanyVacancyList>>

    /*API company*/
    @GET("api/company/get_short_representation_of_companies/")
    fun ownerOrWorker(@Header("Cookie") sessionID: String): Call<ArrayList<CompanyList>>

    @GET("api/company/{id}/balance/")
    fun ownerOrWorkerBalance(@Header("Cookie") sessionID: String, @Path("id") id: Int): Call<Int>

}


