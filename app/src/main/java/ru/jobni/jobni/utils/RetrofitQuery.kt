package ru.jobni.jobni.utils

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.jobni.jobni.model.auth.mail.UserMailAuth
import ru.jobni.jobni.model.auth.phone.UserPhoneAuth
import ru.jobni.jobni.model.network.auth.*
import ru.jobni.jobni.model.network.company.CompanyList
import ru.jobni.jobni.model.network.company.CompanyVacancyList
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

    @POST("api/registration/bind_phone/")
    fun sendBindPhone(@Header("Cookie") sessionID: String, @Body bindPhone: BindPhone): Call<ResponseRegUser>

    @POST("api/registration/email_code/")
    fun validateMailCode(@Header("Cookie") sessionID: String, @Body confirmCode: ConfirmCode): Call<ResponseRegConfirm>

    @POST("api/registration/phone_code/")
    fun validatePhoneCode(@Header("Cookie") sessionID: String, @Body confirmCode: ConfirmCode): Call<ResponseRegConfirm>

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

    /*старый нерабочий вариант*/
//    @POST("api/registration/photo/")
//    fun postPhotoReg(@Header("Cookie") sessionID: String, @Body file: RequestBody): Call<ResponseBody>

    @Multipart
    @POST("api/registration/photo/")
    fun postPhotoReg(@Header("Cookie") sessionID: String, @Part image: MultipartBody.Part): Call<ResponseBody>
    /*заготовка для отправки картинки*/
//    @Multipart
//    @POST("api/registration/")
//    fun sendRegistrationData(/*@Part("info")  info: RequestBody*/@Part info: MultipartBody.Part, @Part image: MultipartBody.Part): Call<ResponseBody>

    /*API VK*/
    @POST("api/registration/social_account/")
    fun postVKReg(@Header("Cookie") sessionID: String, @Body userMail: RegVK): Call<ResponseBody>

    @POST("api/authorization/social_account/")
    fun postVKAuth(@Body userMail: AuthVKJobni): Call<AuthVK>

    /*API Instagram*/
    @FormUrlEncoded
    @POST("https://api.instagram.com/oauth/access_token")
    fun getInstagramAccessToken(
            @Field("client_id") client_id: String,
            @Field("client_secret") client_secret: String,
            @Field("grant_type") grant_type: String,
            @Field("redirect_uri") redirect_uri: String,
            @Field("code") code: String
    ): Call<AuthInstagram>

    @POST("api/accounts/instagram/login/?process=login")
    fun postInstagramAuth(@Header("Cookie") sessionID: String): Call<ResponseBody>

    /*API Discord*/
    @POST("api/authorization/social_account/")
    fun postDiscordAuth(@Body userDiscord: AuthDiscordJobni): Call<AuthDiscord>

    @FormUrlEncoded
    @POST("https://discordapp.com/api/v6/oauth2/token")
    fun getDiscordAccessToken(
            @Field("client_id") client_id: String,
            @Field("client_secret") client_secret: String,
            @Field("grant_type") grant_type: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirect_uri: String,
            @Field("scope") scope: String
    ): Call<AuthDiscord>

    /*API authorization*/
    @POST("api/authorization/mailbox/")
    fun postAuthData(@Header("Authorization") basicAuth: String, @Body userMail: UserMailAuth): Call<AuthMail>

    @POST("api/authorization/phone/")
    fun postAuthDataPhone(@Header("Authorization") basicAuth: String, @Body userPhone: UserPhoneAuth): Call<AuthPhone>

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


