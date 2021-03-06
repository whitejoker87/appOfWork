package ru.jobni.jobni.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.BuildConfig
import ru.jobni.jobni.R
import ru.jobni.jobni.model.network.registration.*
import ru.jobni.jobni.utils.Retrofit
import ru.jobni.jobni.utils.getRealPath
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    private val authUserSessionID = "userSessionID"
    private val authUserName = "userName"
    private val authUserPass = "userPass"

    var sPrefAuthUser = application.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)

    private val regMail = MutableLiveData<String>("anonimalesha@mail.ru")
    private val regPassword = MutableLiveData<String>("namenamename")
    private val regPassConfirm = MutableLiveData<String>("namenamename")
    private val regSurname = MutableLiveData<String>("Иванов")
    private val regName = MutableLiveData<String>("Иван")
    private val regMiddlename = MutableLiveData<String>("Иванович")
    private val regReferer = MutableLiveData<String>("")
    private val regPhoto = MutableLiveData<Drawable>()
    /*Ниже 3 типа листов контактов.
    Соззданы для того что бы корректно работал биндинг при заполнении
    (двухсторонний биндинг в отношении списка из RegContact отказался работать
    и было сделано 3 списка)*/
    /*Список контактов*/
    private val regContacts = MutableLiveData<ArrayList<String>>(arrayListOf())
    /*Список id контактов*/
    private val regContactsId = MutableLiveData<ArrayList<Int>>(arrayListOf())
    /*Список типов контактов*/
    private val regContactsType = MutableLiveData<ArrayList<String>>(arrayListOf())
    private val regPhone = MutableLiveData<String>("")
    private val regPhoneCode = MutableLiveData<String>("")
    private val regMailCode = MutableLiveData<String>("")
    private val regDataProtection = MutableLiveData<Boolean>(false)
    private val regPublicOffers = MutableLiveData<Boolean>(false)

    private val startNextRegPhase = MutableLiveData<Int>()

    private val isPrivilegesForFileDone = MutableLiveData<Boolean>()

    private val numberOfVisibleItemReg = MutableLiveData<Int>(-1)

    private val resultReg1Success =
        MutableLiveData<Boolean>(false)//флаг пройденного начала регистрации. получен первый sessionid
    private val resultReg2Success = MutableLiveData<Boolean>(false)//флаг зарегистрированного пароля и кода подтверждения.  получен второй sessionid
    private val resultReg3Success = MutableLiveData<Boolean>(false)//флаг отправленных ФИО
    private val resultReg4Success = MutableLiveData<Boolean>(false)//флаг законченной регистрации

    private val typeAddRegFragment = MutableLiveData<String>("")

    private val socialRegStart = MutableLiveData<Boolean>(false)

    /*For observe to launch fragment*/
    private val fragmentLaunch = MutableLiveData<String>()

    /*For observe to launch camera intent*/
    private val photoLaunch = MutableLiveData<String>()

    /*photo bitmap for iv*/
    private val bitmapPhoto = MutableLiveData<Bitmap>()

    /*Access of dialog load photo*/
    private val isPhotoDialogEnabled = MutableLiveData<Boolean>()

    /*параметр uri для загружаемого фото*/
    private var outputPhotoUri = MutableLiveData<Uri>(Uri.EMPTY)
    /*параментр для запука активити(для фото)*/
    private val activityLaunch = MutableLiveData<Intent>()
    /*путь до файла с фото*/
    private var mCurrentPhotoPath: String? = ""
    /*файл с фото*/
    private var currentPhoto = File("")

    private val urlWebViewSocial = MutableLiveData<String>()

    private val updateReg1 = MutableLiveData<Boolean>()

    private val toastText = MutableLiveData<String>()

    fun setRegMail(mail: String) {
        regMail.value = mail
    }

    fun getRegMail(): MutableLiveData<String> = regMail

    fun setRegPassword(pass: String) {
        regPassword.value = pass
    }

    fun getRegPassword(): MutableLiveData<String> = regPassword

    fun setRegPassConfirm(passConfirm: String) {
        regPassConfirm.value = passConfirm
    }

    fun getRegPassConfirm(): MutableLiveData<String> = regPassConfirm

    fun setRegSurname(surname: String) {
        regSurname.value = surname
    }

    fun getRegSurname(): MutableLiveData<String> = regSurname

    fun setRegName(name: String) {
        regName.value = name
    }

    fun getRegName(): MutableLiveData<String> = regName

    fun setRegMiddlename(middlename: String) {
        regMiddlename.value = middlename
    }

    fun getRegMiddlename(): MutableLiveData<String> = regMiddlename

    fun setRegReferer(referer: String) {
        regReferer.value = referer
    }

    fun getRegReferer(): MutableLiveData<String> = regReferer

    fun setRegPhoto(photo: Drawable) {
        regPhoto.value = photo
    }

    fun getRegPhoto(): MutableLiveData<Drawable> = regPhoto

    fun setRegPhone(phone: String) {
        regPhone.value = phone
    }

    fun getRegPhone(): MutableLiveData<String> = regPhone

    fun setRegPhoneCode(phoneCode: String) {
        regPhoneCode.value = phoneCode
    }

    fun getRegPhoneCode(): MutableLiveData<String> = regPhoneCode


    fun setRegMailCode(mailCode: String) {
        regMailCode.value = mailCode
    }

    fun getRegMailCode(): MutableLiveData<String> = regMailCode


    fun setRegContacts(contacts: ArrayList<String>) {
        regContacts.value = contacts
    }

    fun getRegContacts(): MutableLiveData<ArrayList<String>> = regContacts


    fun setRegContactsId(ids: ArrayList<Int>) {
        regContactsId.value = ids
    }

    fun getRegContactsId(): MutableLiveData<ArrayList<Int>> = regContactsId


    fun setRegContactsType(contactsType: ArrayList<String>) {
        regContactsType.value = contactsType
    }

    fun getRegContactsType(): MutableLiveData<ArrayList<String>> = regContactsType


    fun setRegDataProtection(isDataProtection: Boolean) {
        regDataProtection.value = isDataProtection
    }

    fun isRegDataProtection(): MutableLiveData<Boolean> = regDataProtection


    fun setRegPublicOffers(isPublicOffers: Boolean) {
        regPublicOffers.value = isPublicOffers
    }

    fun isRegPublicOffers(): MutableLiveData<Boolean> = regPublicOffers


    fun setStartNextRegPhase(nextPhase: Int) {
        startNextRegPhase.value = nextPhase
    }

    fun getStartNextRegPhase(): MutableLiveData<Int> = startNextRegPhase

    fun setPrivilegesForFileDone(isDone: Boolean) {
        isPrivilegesForFileDone.value = isDone
    }

    fun isPrivilegesForFileDone(): MutableLiveData<Boolean> = isPrivilegesForFileDone

    fun getNumberOfVisibleItemReg(): MutableLiveData<Int> = numberOfVisibleItemReg

    fun setNumberOfVisibleItemReg(numberItem: Int) {
        numberOfVisibleItemReg.value = numberItem
    }

    fun getResultReg1Success(): MutableLiveData<Boolean> = resultReg1Success

    fun setResultReg1Success(success: Boolean) {
        resultReg1Success.value = success
    }

    fun getResultReg2Success(): MutableLiveData<Boolean> = resultReg2Success

    fun setResultReg2Success(success: Boolean) {
        resultReg2Success.value = success
    }

    fun getResultReg3Success(): MutableLiveData<Boolean> = resultReg3Success

    fun setResultReg3Success(success: Boolean) {
        resultReg3Success.value = success
    }

    fun getResultReg4Success(): MutableLiveData<Boolean> = resultReg4Success

    fun setResultReg4Success(success: Boolean) {
        resultReg4Success.value = success
    }


    fun getTypeAddRegFragment(): MutableLiveData<String> = typeAddRegFragment

    fun setTypeAddRegFragment(type: String) {
        typeAddRegFragment.value = type
    }


    fun getSocialRegStart(): MutableLiveData<Boolean> = socialRegStart

    fun setSocialRegStart(isStart: Boolean) {
        socialRegStart.value = isStart
    }


    fun setFragmentLaunch(setLaunch: String) {
        fragmentLaunch.value = setLaunch
    }

    fun getFragmentLaunch(): MutableLiveData<String> = fragmentLaunch

    fun setPhotoLaunch(setPhoto: String) {
        photoLaunch.value = setPhoto
    }

    fun getPhotoLaunch(): MutableLiveData<String> = photoLaunch

    fun setBitmapPhoto(setBitmap: Bitmap) {
        bitmapPhoto.value = setBitmap
    }

    fun getBitmapPhoto(): MutableLiveData<Bitmap> = bitmapPhoto

    fun setUpdateReg1(updated: Boolean) {
        updateReg1.value = updated
    }

    fun isUpdateReg1(): MutableLiveData<Boolean> = updateReg1

    fun setOutputPhotoUri(setUri: Uri) {
        outputPhotoUri.value = setUri
    }

    fun getOutputPhotoUri(): MutableLiveData<Uri> = outputPhotoUri


    fun setActivityLaunch(cameraIntent: Intent) {
        activityLaunch.value = cameraIntent
    }

    fun getActivityLaunch(): MutableLiveData<Intent> = activityLaunch


    fun setCurrentPhotoPath(mCurrentPhotoPath: String) {
        this.mCurrentPhotoPath = mCurrentPhotoPath
    }

    fun getCurrentPhotoPath(): String? = mCurrentPhotoPath


    fun setCurrentPhoto(photo: File) {
        currentPhoto = photo
    }

    fun getCurrentPhoto(): File? = currentPhoto

    fun setUrlWebViewSocial(url: String) {
        urlWebViewSocial.value = url
    }

    fun getUrlWebViewSocial(): MutableLiveData<String> = urlWebViewSocial


    fun setPhotoDialogEnabled(dialogEnabled: Boolean) {
        isPhotoDialogEnabled.value = dialogEnabled
    }

    fun isPhotoDialogEnabled(): MutableLiveData<Boolean> = isPhotoDialogEnabled


    fun setToastText(text: String) {
        toastText.value = text
    }

    fun getToastText(): MutableLiveData<String> = toastText


    /*other methods*/
    /*При привязке первой учетной записи получаем sessionid и пароль последующие нет*/
    fun regOrBind(type: String) {
        if (getRegPassword().value!!.isEmpty() || getRegPassConfirm().value!!.isEmpty()) {
            setToastText("Необходимо ввести пароль и его подтверждение!")
        }
        else if (resultReg1Success.value!!) {//если уже есть сессия
            when (type) {
                "mail" -> postBindEmail()
                "phone" -> postBindPhone()
                "soc" -> setSocialRegStart(true)
            }
        } else startRegistration()
    }


    /*для выполнения 1 этапа регистрации(пустой запрос для старта)*/
    fun startRegistration() {

        Retrofit.api?.postRegistrationUser()?.enqueue(object : Callback<ResponseRegStart> {
            /**/
            override fun onResponse(call: Call<ResponseRegStart>, response: Response<ResponseRegStart>) {
                /*"success":true,"result":{"id":4}}
                * Set-Cookie: sessionid=1wutajt6fj109uqu9qzbnufd2ir9k7v3; expires=Tue, 16 Apr 2019 15:52:14 GMT; Max-Age=1209600; Path=/ */
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        getSIDFromRegOne(response.headers())
                    } else if (!(response.body()!!.success)) {
                        //Toast.makeText(context, "Неудача первого этапа ${response.body()!!.error_text}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            /**/
            override fun onFailure(call: Call<ResponseRegStart>, t: Throwable) {
                Toast.makeText(context, "Неудача первого этапа onFailure", Toast.LENGTH_LONG).show()
            }
        })
    }

//    fun btnRegClick() {
//
//        setPrivilegesForFileDone(false)
//
//    }

    /*Для сохранения Session ID полученного из первого этапа*/
    fun getSIDFromRegOne(responseHeaders: okhttp3.Headers) {

        val resultListHeaders = responseHeaders.get("Set-Cookie")

        /* Пример ответа от АПИ
        set-cookie: sessionid=26jmvokos705ehtv7l2fe86fmuwem5n3; expires=Wed, 03 Apr 2019 09:33:23 GMT; Max-Age=1209600; Path=/
        Нам нужно выделить из этой строки sessionid
        На выходе получаем 26jmvokos705ehtv7l2fe86fmuwem5n3 */

        val sessionID = resultListHeaders?.substringBefore(";")?.substringAfter("=")

        val editor = sPrefAuthUser.edit()
        editor?.putString(authUserSessionID, sessionID)
        editor?.putString(authUserName, regMail.value)
        editor?.putString(authUserPass, regPassword.value)
        editor?.apply()

        if (sessionID != null) {
            //setResultAuthSuccess(true)
            if (!getResultReg1Success().value!!) setResultReg1Success(true) //изменяем флаг первого этапа для запуска  регистрации пароля
            //postBindEmail()

        } else Toast.makeText(context, "Ошибка при получении СИД", Toast.LENGTH_LONG).show()
    }

    /*Для 1 этапа регистрации для отправки почты*/
    fun postBindEmail() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val bindEmail = RegBindEmail(
            regMail.value!!
        )

        Retrofit.api?.sendBindEmail(cid, bindEmail)?.enqueue(object : Callback<ResponseRegUser> {
            /*{"email":"1@1.ru"}*/
            override fun onResponse(call: Call<ResponseRegUser>, response: Response<ResponseRegUser>) {
                /*{"success":true,"message":["Перейдите на почту для её подтверждения."]}*/

                /*{"success":true,"result":{"_id":4350}}*/
                /*{"success":false,"errors":{"email":["Это поле не может быть пустым."]}}*/
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        Toast.makeText(context, "Почта в норме! ${response.body()!!.message}", Toast.LENGTH_LONG).show()
                        if (!getResultReg2Success().value!!) postPassword() //работает только для первой реги
                    } else {
                        Toast.makeText(context, "Почта не очень! ${response.body()!!.errors.email}", Toast.LENGTH_LONG)
                            .show()
                        if (!getResultReg2Success().value!!) setResultReg1Success(false)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRegUser>, t: Throwable) {
                if (!getResultReg2Success().value!!) setResultReg1Success(false)
            }
        })
    }

    /*Для 1 этапа регистрации для отправки телефона*/
    fun postBindPhone() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val bindPhone = RegBindPhone(
            regPhone.value!!
        )

        Retrofit.api?.sendBindPhone(cid, bindPhone)?.enqueue(object : Callback<ResponseRegUser> {
            /**/
            override fun onResponse(call: Call<ResponseRegUser>, response: Response<ResponseRegUser>) {
                /*{"success":true,"result":{"_id":4968}}*/
                /*{"success":false,"errors":{"phone":["Номер телефона может содержать только цифры.","Длина номера телефона должна быть 10 цифр."]}}*/
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        Toast.makeText(context, "телефон в норме! ${response.body()!!.result._id}", Toast.LENGTH_LONG)
                            .show()
                        if (!getResultReg2Success().value!!) postPassword()//работает только для первой реги
                    } else {
                        Toast.makeText(
                            context,
                            "телефон не очень! ${response.body()!!.errors.email}",
                            Toast.LENGTH_LONG
                        ).show()
                        if (!getResultReg2Success().value!!) setResultReg1Success(false)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRegUser>, t: Throwable) {
                if (!getResultReg2Success().value!!) setResultReg1Success(false)
            }
        })
    }


//    /*Для первого этапа регистрации для соцсетей*/
//    fun sendSocialData(userLogin: String, provider: String, accessToken: String) {
//
//        val id = sPrefAuthUser.getString(authUserSessionID, null)
//        val cid = String.format("%s%s", "sessionid=", id)
//
//        val contactFace = RegSocial(
//            userLogin,
//            provider,
//            accessToken
//        )
//
//        Retrofit.api?.postSocialReg(cid, contactFace)?.enqueue(object : Callback<ResponseRegUser> {
//            override fun onResponse(call: Call<ResponseRegUser>, response: Response<ResponseRegUser>) {
//                if (response.body() != null) {
//                    if (response.body()!!.success)
//                    else Toast.makeText(context, "Error! ${response.body()!!.errors.uid}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseRegUser>, t: Throwable) {
//                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


    /*для выполнения 2 этапа регистрации(отправка паролей)*/
    fun postPassword() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val pass = RegPass(
            regPassword.value!!,
            regPassConfirm.value!!
        )
        Retrofit.api?.sendRegistrationPass(cid, pass)?.enqueue(object : Callback<ResponseRegPass> {
            /*{"password":"namenamename","password_confirm":"namenamename"}*/
            override fun onResponse(call: Call<ResponseRegPass>, response: Response<ResponseRegPass>) {
                /*{"success":true}*/
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        getSIDFromRegOne(response.headers())
                        Toast.makeText(context, "Пароль в порядке!", Toast.LENGTH_LONG).show()
                        if (getSocialRegStart().value!!) {
                            if (getRegReferer().value!!.isNotEmpty()) sendRegReferer()
                            else setResultReg2Success(true)
                        }//завершаем этап пароля только для соцсетей(т.к не ткода подтверждения)
                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "Пароль не принят! ${response.body()!!.errors}", Toast.LENGTH_LONG)
                            .show()
                        setResultReg2Success(false)
                    }
                }
            }
            /*Пример отрицательного ответа
            {"success":false,"errors":{"non_field_errors":["Учетные данные не были предоставлены."]}}*/

            override fun onFailure(call: Call<ResponseRegPass>, t: Throwable) {
                Toast.makeText(context, "Пароль не принят!", Toast.LENGTH_LONG).show()
                setResultReg2Success(false)
            }
        })
    }

    /*Для этапа подтверждения почты во время регистрации*/
    fun confirmEmail() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val mailCode = RegConfirmCode(
            regMailCode.value!!
        )

        Retrofit.api?.validateMailCode(cid, mailCode)?.enqueue(object : Callback<ResponseRegConfirm> {
            /*{"code":"0000"}*/
            override fun onResponse(call: Call<ResponseRegConfirm>, response: Response<ResponseRegConfirm>) {
                /*после неправильной отправки кода
                * {"success":false,"errors":{"non_field_errors":["Учетные данные не были предоставлены."]}}*/
                /*После правильной отправки
                * {{"success":true,"result":{"id":8}}*/
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        if (getRegReferer().value!!.isNotEmpty()) sendRegReferer()
                        else setResultReg2Success(true)//завершение этапа пароля и кода и переход к этапу ФИО
                        Toast.makeText(context, "Код подтвержден!", Toast.LENGTH_LONG).show()

                    } else Toast.makeText(context, "Код лох! ${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                }
            }

            /*отправил неправильный код
            {"success":false,"error_text":"Время жизни кода активации вышло, зарегистрируйтесь заново"}*/
            override fun onFailure(call: Call<ResponseRegConfirm>, t: Throwable) {
                Toast.makeText(context, "Код не работает!", Toast.LENGTH_LONG).show()
            }

        })

    }

    /*Для этапа подтверждения телефона во время регистрации*/
    fun confirmPhone() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val mailCode = RegConfirmCode(
            regPhoneCode.value!!
        )

        Retrofit.api?.validatePhoneCode(cid, mailCode)?.enqueue(object : Callback<ResponseRegConfirm> {
            /*{"code":"0000"}*/
            override fun onResponse(call: Call<ResponseRegConfirm>, response: Response<ResponseRegConfirm>) {
                /*после неправильной отправки кода
                * {"success":false,"errors":{"code":["Время жизни кода активации вышло, зарегистрируйтесь заново"]}}*/
                /*После правильной отправки
                * {{"success":true,"result":{"id":8}}*/
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        if (getRegReferer().value!!.isNotEmpty()) sendRegReferer()
                        else setResultReg2Success(true) //завершение этапа пароля и кода и переход к этапу ФИО
                        Toast.makeText(context, "Код подтвержден!", Toast.LENGTH_LONG).show()

                    } else Toast.makeText(context, "Код лох! ${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                }
            }

            /*отправил неправильный код
            {"success":false,"error_text":"Время жизни кода активации вышло, зарегистрируйтесь заново"}*/
            override fun onFailure(call: Call<ResponseRegConfirm>, t: Throwable) {
                Toast.makeText(context, "Код не работает!", Toast.LENGTH_LONG).show()
            }

        })

    }

    /*Выполнение этапа отправки referer*/
    fun sendRegReferer() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val referer = RegReferer(
            regReferer.value!!
        )
        Retrofit.api?.sendRegistrationReferer(cid, referer)?.enqueue(object : Callback<ResponseRegReferer> {

            override fun onResponse(call: Call<ResponseRegReferer>, response: Response<ResponseRegReferer>) {
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        setResultReg2Success(true)//переход ко 2 этапу реги
                        Toast.makeText(context, "Успешно добавлена рефералка!", Toast.LENGTH_LONG).show()
                    }//{"success":false,"errors":{"referer":["Указан неверный реферал"]}}
                    else Toast.makeText(context, "Безуспешно не добавлено ничего раеферального", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegReferer>, t: Throwable) {
                Toast.makeText(context, "Error! in рефа", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*Для выполнения 3 этапа регистрации(Имя Фамилия Отчество)*/
    fun btnRegContactFaceClick() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val contactFace = RegContactFace(
            regSurname.value!!,
            regName.value!!,
            regMiddlename.value!!
        )
        Retrofit.api?.sendRegistrationContactFace(cid, contactFace)?.enqueue(object : Callback<ResponseRegContactFace> {

            override fun onResponse(call: Call<ResponseRegContactFace>, response: Response<ResponseRegContactFace>) {
                if (response.body() != null) {
                    /*{"success":true}
                    {"success":false,
                    "errors":{"name":["Это поле не может быть пустым."],
                    "surname":["Это поле не может быть пустым."],
                    "middlename":["Это поле не может быть пустым."]}}*/
                    if (response.body()!!.success) {
                        setResultReg3Success(response.body()!!.success)
                        Toast.makeText(context, "Успешно добавлено контактное лицо!", Toast.LENGTH_LONG).show()
                    } else Toast.makeText(context, "Безуспешно не добавлено контактное лицо", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegContactFace>, t: Throwable) {
                Toast.makeText(context, "Error! in zq rega 222", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*для выполнения 4 этапа регистрации(контакты)*/
    fun btnRegContactFaceContactClick() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val contacts = ArrayList<Any>()//RegContact нужно было посылать с 2 и 3 полями
        val contactsString = regContacts.value!!

        /*Формируем из 3 листов Livedata один для отправки на сервер*/
        for (i in contactsString.indices) {
//            if (regContactsId.value!![i] > 0) contacts.add(
//                RegContact(
//                    regContactsId.value!![i],
//                    regContactsType.value!![i],
//                    contactsString[i]
//                )
//            )
//            else {
                contacts.add(
                    RegContactWithoutId(
                        regContactsType.value!![i],
                        contactsString[i]))
//            }
        }

        val contactFaceContacts = RegContactFaceContact(
            regDataProtection.value!!,
            regPublicOffers.value!!,
            contacts
        )
        Retrofit.api?.sendRegistrationContactFaceContact(cid, contactFaceContacts)
            ?.enqueue(object : Callback<ResponseRegContactFaceContacts> {

                override fun onResponse(
                    call: Call<ResponseRegContactFaceContacts>,
                    response: Response<ResponseRegContactFaceContacts>
                ) {
                    if (response.body() != null) {
                        /*{"success":true}
                        * {"success":false,"errors":{"contact_face":["Нельзя повторно пройти регистрацию"]}}*/
                        response.body()?.let {
                            if (it.success) {
                                Toast.makeText(
                                    context,
                                    "Успешно добавлены контакты всякие!!!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                setResultReg4Success(it.success)
                                /*{"contact":{"success":false,"error_text":["Это поле не может быть пустым."]}}*/
                            } else Toast.makeText(
                                context,
                                "Ahtung!!! контакты не отправлены",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }
                }

                /**/
                override fun onFailure(call: Call<ResponseRegContactFaceContacts>, t: Throwable) {
                    Toast.makeText(context, "Ahtung!!! нет согласия на обработку", Toast.LENGTH_SHORT).show()
                }
            })
    }

    /*Для выполнения отправки фото при регистрации*/
    fun regContactPhotoSend() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)
        val file: File
        if (!getCurrentPhoto()!!.path.equals("")) file = getCurrentPhoto()!!

        /*if (getPhotoLaunch().value.equals("camera"))*/ //file = File(getOutputPhotoUri().value!!.path)
        else file = File(getRealPath(context, getOutputPhotoUri().value!!))
//        val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
        val image = MultipartBody.Part.createFormData(
            "photo",
            "photo.jpg",
            RequestBody.create(MediaType.parse("image/jpg"), file)
        )
        Retrofit.api?.postPhotoReg(cid, image)?.enqueue(object : Callback<ResponseRegStart> {

            override fun onResponse(call: Call<ResponseRegStart>, response: Response<ResponseRegStart>) {
                if (response.body() != null)
                    if (response.body()!!.success) Toast.makeText(
                        context,
                        "Фото добавлено",
                        Toast.LENGTH_LONG
                    ).show()

            }

            override fun onFailure(call: Call<ResponseRegStart>, t: Throwable) {

            }
        });

    }

//
//
//    @SuppressLint("ResourceType")
//    fun registration() {
////        val res = context.resources
////        try {
////            setRegPhoto(Drawable.createFromXml(res, res.getXml(ru.jobni.jobni.R.drawable.ic_action_name)))
////        } catch (ex: Exception) {
////            Log.e("Error", "Exception loading drawable")
////        }
//
//        val contact = RegContact("tel", regContact.value!!)
//        val contacts = arrayListOf(contact)
//        val registration = Registration(
//            regMail.value!!,
//            regPassword.value!!,
//            regPassConfirm.value!!,
//            regSurname.value!!,
//            regName.value!!,
//            regMiddlename.value!!,
//            regReferer.value!!,
//            contacts
//        )
//
//        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val info1 = File(path, "info")
//        try {
//            val f = FileOutputStream(info1)
//            val o = ObjectOutputStream(f)
//
//            // Write objects to file
//            o.writeObject(registration)
//
//            o.close()
//            f.close()
//        } catch (e: FileNotFoundException) {
//            System.out.println("File not found")
//        } catch (e: IOException) {
//            System.out.println("Error initializing stream")
//        } catch (e: ClassNotFoundException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
////        val cacheDir: File
////        val adCacheDir: File
////        val sdState = Environment.getExternalStorageState()
////        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
////            val sdDir = Environment.getExternalStorageDirectory();
////            cacheDir = File(sdDir,"data/gr");
////        }
////        else
////            cacheDir = context.getExternalFilesDir(sdState)!!
////
////        if(!cacheDir.exists())
////            cacheDir.mkdirs();
////
////
////        if(sdState.equals(Environment.MEDIA_MOUNTED)){
////            val adSdDir = Environment.getExternalStorageDirectory();
////            adCacheDir = File(adSdDir,"data/ad");
////        }else
////            adCacheDir = context.getExternalFilesDir(sdState)!!
////
////        if(!adCacheDir.exists())
////            adCacheDir.mkdirs()
//
//        val image2 = File(path,"image")
//        try {
//            val inputStream: InputStream = context.resources.openRawResource(R.drawable.ic_action_name);
//            val out: OutputStream = FileOutputStream(image2);
//            val buf = ByteArray(1024)
//            var len: Int = 1
//            while (len > 0) {
//                len = inputStream.read(buf)
//                if (len > 0)out.write(buf, 0, len)
//            }
//
//            out.close();
//            inputStream.close();
//        } catch (e: FileNotFoundException) {
//            System.out.println("File not found")
//        } catch (e: IOException) {
//            System.out.println("Error initializing stream")
//        } catch (e: ClassNotFoundException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
////        val reqFile = RequestBody.create(MediaType.parse("application/json"), file)
////        val body = MultipartBody.Part.createFormData("info", file.name, reqFile)
//
//        //val info = RequestBody.create(MediaType.parse("multipart/form-data"), info1)
//        val image = MultipartBody.Part.createFormData(
//            "image",
//            "image",
//            RequestBody.create(MediaType.parse("image/jpg"), image2)
//        )
//        val info = MultipartBody.Part.createFormData(
//            "info",
//            "info",
//            RequestBody.create(MediaType.parse("multipart/form-data"), info1)
//        )
//
//        Retrofit.api?.sendRegistrationData(info, image)?.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
//                if (response.body() != null) {
//
//                }
//            }
//
//            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
//                //Toast.makeText(applicationContext, "Error in download for menu!", Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    /*Для добавления контакта в список на 4 этапе регистрации*/
    fun btnAddContactClick(type: String) {
        regContactsId.add(0)
        regContactsType.add(type)
        regContacts.add("")
    }

    /*Для загрузки прикрепленных контактов для испольщования в 4 этапе(старая версия)*/
//    fun getContactsForReg3Old() {
//
//        val id = sPrefAuthUser.getString(authUserSessionID, null)
//        val cid = String.format("%s%s", "sessionid=", id)
//
//        Retrofit.api?.getContactsForReg(cid)?.enqueue(object : Callback<ResponseRegGetContacts> {
//
//            /*{"success":true,
//            "result":
//            {"name":"Иван",
//            "surname":"Иванов",
//            "middlename":"Иванович",
//            "referer":"",
//            "photo":"",
//            "consent_to_data_storage_and_protection":false,
//            "consent_public_offers":false,
//            "login":"16",
//            "password":true,
//            "contacts":[{
//            "contact_type":"mail",
//            "contact_id":9,
//            "contact":"3@3.ru",
//            "incomplete_registration":true}]}}*/
//            override fun onResponse(call: Call<ResponseRegGetContacts>, response: Response<ResponseRegGetContacts>) {
//                if (response.body() != null) {
//                    if (response.body()!!.success) {
//                        response.body()!!.result.contacts.forEach {
//                            regContactsId.add(it.contact_id)
//                            regContactsType.add(it.contact_type)
//                            regContacts.add(it.contact)
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseRegGetContacts>, t: Throwable) {
//
//            }
//        })
//
//    }

    /*Для загрузки прикрепленных контактов для испольщования в 4 этапе(актуально)*/
    fun getContactsForReg3() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.getContactsForReg(cid)?.enqueue(object : Callback<ResponseRegGetContacts> {

            /*{"success":true,
            "next":null,
            "previous":null,
            "count":1,
            "results":
            [{"contact_id":132,
            "contact_type":"phone",
            "contact":"1231231230",
            "incomplete_registration":true}]}*/
            override fun onResponse(call: Call<ResponseRegGetContacts>, response: Response<ResponseRegGetContacts>) {
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        response.body()!!.results.forEach {
                            //regContactsId.add(it.contact_id)
                            regContactsType.add(it.contact_type)
                            regContacts.add(it.contact)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRegGetContacts>, t: Throwable) {

            }
        })

    }

    /*запуск запроса данных для диалога регистрации webview*/
    fun getSocialReg(typeReg: String) {
        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)
        val process = "connect"

        val provider: String
        when (typeReg) {
            "RegVK" -> provider = "vk"
            "RegOK" -> provider = "odnoklassniki"
            "RegInst" -> provider = "instagram"
            "RegTel" -> provider = "telegram"
            "RegGoogle" -> provider = "google"
            "RegFB" -> provider = "facebook"
            "RegMailRu" -> provider = "mailru"
            "RegDiscord" -> provider = "discord"
            "RegMic" -> provider = "microsoft"
            else -> provider = ""
        }

        val requestUrl = "https://dev.jobni.ru/api/accounts/${provider}/login/?process=${process}"
        setUrlWebViewSocial(requestUrl)

//        Retrofit.api?.getDataSocialReg(cid, provider, process)?.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.body() != null) {
//                    val getUrl = response.raw().request().url().toString()
//                    setUrlWebViewSocial(getUrl)
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(context, "Error onGetAuthSocial!", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    /*открываем камеру для фото*/
    fun openCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Метод resolveActivity() поможет проверить активности, способное сделать фотографию.
        // Если подходящего приложения не найдётся, то мы можем сделать кнопку для съёмки недоступной.
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            // создать файл для фотографии
            var photoFile: File? = null
            try {
                photoFile = createImageFile(context.baseContext)
            } catch (ex: IOException) {
                // ошибка, возникшая в процессе создания файла
            }

            // если файл создан, запускаем приложение камеры
            if (photoFile != null) {
                setOutputPhotoUri(
                    FileProvider.getUriForFile(
                        context.applicationContext,
                        BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
                        photoFile
                    )
                )
                setCurrentPhoto(photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputPhotoUri.value)
                //cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setActivityLaunch(cameraIntent)
            }
        }
    }

    /*создание файла для фото*/
    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {

        // создание файла с уникальным именем
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "CAM" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        //        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //        StrictMode.setVmPolicy(builder.build());

        val image = File.createTempFile(
            imageFileName, /* префикс */
            ".jpg", /* расширение */
            storageDir      /* директория */
        )

        //        ContentValues values = new ContentValues();
        //        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        //        values.put(MediaStore.Images.Media.MIME_TYPE, "image/ipeg");
        //        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        //
        //        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // сохраняем пусть для использования с интентом ACTION_VIEW
        //setOutputPhotoUri(Uri.fromFile(image))
        //setOutputPhotoUri(Uri.parse("file://" + image.absolutePath))
        return image
    }

    /*слушатель нажатия на пункт fab в 3 этапе реги*/
    val onClickSpeedDialViewItem = object : SpeedDialView.OnActionSelectedListener {
        override fun onActionSelected(speedDialActionItem: SpeedDialActionItem): Boolean {
            when(speedDialActionItem.id) {
                R.id.action_user -> {
                    btnAddContactClick("email")
                    return false
                }
                R.id.action_phone -> {
                    btnAddContactClick("phone")
                    return false
                }
                R.id.action_skype -> {
                    btnAddContactClick("skype")
                    return false
                }
                R.id.action_phone_home -> {
                    btnAddContactClick("home_phone")
                    return false
                }
                else -> return false
            }
        }
    }

    //для сброса значений регистрации при запуске ее повторно например
    fun clearRegValues() {//Event wrapper по этой теме хорошо
        regMail.value = ""
        regPassword.value = ""
        regPassConfirm.value = ""
        regSurname.value = ""
        regName.value = ""
        regMiddlename.value = ""
        regReferer.value = ""

        regContacts.value = arrayListOf()
        regContactsId.value = arrayListOf()
        regContactsType.value = arrayListOf()
        regPhone.value = ""
        regPhoneCode.value = ""
        regMailCode.value = ""
        regDataProtection.value = false
        regPublicOffers.value = false

        numberOfVisibleItemReg.value = -1

        resultReg1Success.value = false
        resultReg2Success.value = false
        resultReg3Success.value = false
        resultReg4Success.value = false

        typeAddRegFragment.value = ""

        socialRegStart.value = false

        photoLaunch.value = ""

        isPhotoDialogEnabled.value = false

        outputPhotoUri.value = Uri.EMPTY

        mCurrentPhotoPath = ""
        currentPhoto = File("")

        urlWebViewSocial.value = ""
    }

}