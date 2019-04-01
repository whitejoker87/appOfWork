package ru.jobni.jobni.viewmodel

import android.app.Activity
import android.app.Application
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.model.network.registration.*
import ru.jobni.jobni.utils.Retrofit


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
    (двухсторонний биндинг в отношении списка из Contact отказался работать
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

    private val resultReg1Success = MutableLiveData<Boolean>(false)
    private val resultReg2Success = MutableLiveData<Boolean>(false)
    private val resultReg3Success = MutableLiveData<Boolean>(false)

    private val resultAuthSuccess = MutableLiveData<Boolean>(false)
    private val typeAddRegFragment = MutableLiveData<String>("")

    private val vkRegStart = MutableLiveData<Boolean>()

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

    fun getResultAuthSuccess(): MutableLiveData<Boolean> = resultAuthSuccess

    fun setResultAuthSuccess(success: Boolean) {
        resultAuthSuccess.value = success
    }

    fun getTypeAddRegFragment(): MutableLiveData<String> = typeAddRegFragment

    fun setTypeAddRegFragment(type: String) {
        typeAddRegFragment.value = type
    }


    fun isVkRegStart(): MutableLiveData<Boolean> = vkRegStart

    fun setVkRegStart(isStart: Boolean) {
        vkRegStart.value = isStart
    }


    /*для выполнения 1 этапа регистрации(отправка паролей)*/
    fun btnRegUserClick() {

        val user = RegUser(
            regPassword.value!!,
            regPassConfirm.value!!
        )
        Retrofit.api?.sendRegistrationUser(user)?.enqueue(object : Callback<ResponseRegPass> {
            /*{"password":"namenamename","password_confirm":"namenamename"}*/
            override fun onResponse(call: Call<ResponseRegPass>, response: Response<ResponseRegPass>) {
                /*strict-transport-security: max-age=3600
                    x-content-type-options: nosniff
                    x-xss-protection: 1; mode=block
                    set-cookie: sessionid=kqh7bd5llhi6ry76fp543ft6biw475fd; expires=Sat, 13 Apr 2019 22:12:06 GMT; Max-Age=1209600; Path=/
                    access-control-allow-headers: *
                    {"success":true,"error_text":{}}*/
                if (response.body() != null) {
                        if (response.body()!!.success) {
                            //setResultReg1Success(response.body()!!.success)
                            getSIDFromRegOne(response.headers())
                            Toast.makeText(context, "Пароль в порядке!", Toast.LENGTH_LONG).show()
                        } else if (!(response.body()!!.success)) {
                            Toast.makeText(context, "Пароль не принят! ${response.body()!!.error_text}", Toast.LENGTH_LONG).show()
                        }
                }
            }
            /*Пример отрицательного ответа
            {"success":false,
            "error_text":
                {"password":
                    [
                        "Введённый пароль слишком короткий. Он должен содержать как минимум 8 символов.",
                        "Введённый пароль слишком широко распространён."
                    ],
                "password_general":
                    []
                }
            }*/

            override fun onFailure(call: Call<ResponseRegPass>, t: Throwable) {
                Toast.makeText(context, "Пароль не принят!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun btnRegClick() {

        setPrivilegesForFileDone(false)

    }

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
            //setResultReg1Success(true)
            Toast.makeText(context, "А вот тебе сид ${sessionID}", Toast.LENGTH_LONG).show()
            setBindEmail()
        } else Toast.makeText(context, "Ошибка при получении СИД", Toast.LENGTH_LONG).show()
    }

//    fun tempAuthForRegOne() {
//
//        val userData = UserAuth(regMail.value, regPassword.value)
//
//        Retrofit.api?.postAuthData("AuthUser", userData)?.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
//                if (response.body() != null) {
//
//                    val resultListHeaders = response.headers().get("Set-Cookie")
//
//                    /* Пример ответа от АПИ
//                    set-cookie: sessionid=26jmvokos705ehtv7l2fe86fmuwem5n3; expires=Wed, 03 Apr 2019 09:33:23 GMT; Max-Age=1209600; Path=/
//                    Нам нужно выделить из этой строки sessionid
//                    На выходе получаем 26jmvokos705ehtv7l2fe86fmuwem5n3 */
//
//                    val sessionID = resultListHeaders?.substringBefore(";")?.substringAfter("=")
//
//                    val editor = sPrefAuthUser.edit()
//                    editor?.putString(authUserSessionID, sessionID)
//                    editor?.putString(authUserName, regMail.value)
//                    editor?.putString(authUserPass, regPassword.value)
//                    editor?.apply()
//
//                    if (sessionID != null) {
//                        setResultAuthSuccess(true)
//                    }
//                }
//            }
//
//            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
//                Toast.makeText(context, "Error! in zq rega 1", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


    /*Для 1 этапа регистрации для отправки почты*/
    fun setBindEmail() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val bindEmail = BindEmail(
            regMail.value!!
        )

        Retrofit.api?.sendBindEmail(cid, bindEmail)?.enqueue(object : Callback<ResponseReg> {
            /*{"email":"1@1.ru"}*/
            override fun onResponse(call: Call<ResponseReg>, response: Response<ResponseReg>) {
                /*{"success":true,"error_text":["Перейдите на почту для её подтверждения."]}*/
                /*{"email":["Введите корректный адрес электронной почты."]}*/
                if (response.body() != null) {
                    if (response.body()!!.success){
                        Toast.makeText(context, "Почта в норме! ${response.body()!!.error_text}", Toast.LENGTH_LONG).show()

                    } else Toast.makeText(context, "Почта не очень! ${response.body()!!.error_text}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseReg>, t: Throwable) {

            }

        })

    }

    /*Для этапа подтверждения почты во время регистрации*/
    fun confirmEmail() {

        val listContacts = regContacts.value
        val listContactsId = regContactsId.value
        val listContactsType = regContactsType.value

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val mailCode = MailCode(
                regMailCode.value!!
        )

        Retrofit.api?.validateMailCode(cid, mailCode)?.enqueue(object : Callback<ResponseRegConfirmMail> {
            /*{"code":"0000"}*/
            override fun onResponse(call: Call<ResponseRegConfirmMail>, response: Response<ResponseRegConfirmMail>) {
                /*после неправильной отправки кода
                * {"detail":"Учетные данные не были предоставлены."}*/
                /*После правильной отправки
                * {"success":true,"error_text":{},"id":55}*/
                if (response.body() != null) {

                    if (response.body()!!.success){
                        Toast.makeText(context, "Код подтвержден! ${response.body()!!.error_text}", Toast.LENGTH_LONG).show()

                        listContacts!!.add(regMail.value!!)
                        listContactsId!!.add(response.body()!!.id)
                        listContactsType!!.add("main")//пока апи работает так
                        setRegContacts(listContacts)
                        setRegContactsId(listContactsId)
                        setRegContactsType(listContactsType)

                    } else Toast.makeText(context, "Код лох! ${response.body()!!.error_text}", Toast.LENGTH_LONG).show()
                }
            }

            /*отправил неправильный код
            {"success":false,"error_text":"Время жизни кода активации вышло, зарегистрируйтесь заново"}*/
            override fun onFailure(call: Call<ResponseRegConfirmMail>, t: Throwable) {
                Toast.makeText(context, "Код не работает!", Toast.LENGTH_LONG).show()
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
        Retrofit.api?.sendRegistrationContactFace(cid, contactFace)?.enqueue(object : Callback<ResponseRegContacts> {

            /*{"contacts":
            [
            {"id":87,
            "contact_type":"Почта",
            "contact":"anonimalesha@mail.ru"}
            ],
            "result":
            {"success":true,
            "error_text":[]}}*/

            override fun onResponse(call: Call<ResponseRegContacts>, response: Response<ResponseRegContacts>) {
                if (response.body() != null) {
                    /*{"success":false,"error_text":"Заполните необходимые поля."}*/
                    if (response.body()!!.result != null && response.body()!!.result.success) {
                        setResultReg2Success(response.body()!!.result.success)
                        Toast.makeText(context, "Успешно добавлено контактное лицо!", Toast.LENGTH_LONG).show()
                    } else Toast.makeText(context, "Безуспешно не добавлено контактное лицо", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegContacts>, t: Throwable) {
                Toast.makeText(context, "Error! in zq rega 222", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*для выполнения 4 этапа регистрации(контакты)*/
    fun btnRegContactFaceContactClick() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val contacts = ArrayList<Contact>()
        val contactsString = regContacts.value!!

        /*Формируем из 3 листов Livedata один для отправки на сервер*/
        for (i in contactsString.indices) {
            contacts.add(Contact(regContactsId.value!![i], regContactsType.value!![i], contactsString[i]))
        }

        val contactFaceContacts = RegContactFaceContact(
                regDataProtection.value!!,
                regPublicOffers.value!!,
                contacts
        )
        Retrofit.api?.sendRegistrationContactFaceContact(cid, contactFaceContacts)?.enqueue(object : Callback<ResponseReg> {

            override fun onResponse(call: Call<ResponseReg>, response: Response<ResponseReg>) {
                if (response.body() != null) {

                    response.body()?.let {
                        if (it.success) {
                            setResultReg3Success(it.success)
                            Toast.makeText(context, "Успешно добавлено контактное лицо ${resultReg3Success}", Toast.LENGTH_LONG)
                                    .show()
                            /*{"contact":{"success":false,"error_text":["Это поле не может быть пустым."]}}*/
                        } else Toast.makeText(context, "Ahtung!!! контакты не отправлены", Toast.LENGTH_SHORT).show()

                    }

                }
            }
            /*
            * {"success":false,"error_text":"Невозможно сохранить не приняв согласия на обработку персональных данных"}*/
            override fun onFailure(call: Call<ResponseReg>, t: Throwable) {
                Toast.makeText(context, "Ahtung!!! нет согласия на обработку", Toast.LENGTH_SHORT).show()
            }
        })
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
//        val contact = Contact("tel", regContact.value!!)
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
    fun btnAddContactClick() {
        val listContacts = regContacts.value
        val listContactsType = regContactsType.value
        listContacts!!.add("")
        listContactsType!!.add("")
        setRegContacts(listContacts)
    }

    /*Для загрузки прикрепленных контактов для испольщования в 4 этапе(пока костыль)*/
    fun getContactsForReg3() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val contactFace = RegContactFace(
                regSurname.value!!,
                regName.value!!,
                regMiddlename.value!!
        )
        Retrofit.api?.sendRegistrationContactFace(cid, contactFace)?.enqueue(object : Callback<ResponseRegContacts> {

            /*{"contacts":
            [
            {"id":87,
            "contact_type":"Почта",
            "contact":"anonimalesha@mail.ru"}
            ],
            "result":
            {"success":true,
            "error_text":[]}}*/

            override fun onResponse(call: Call<ResponseRegContacts>, response: Response<ResponseRegContacts>) {
                if (response.body() != null) {
                    /*{"success":false,"error_text":"Заполните необходимые поля."}*/
                    if (response.body()!!.result != null && response.body()!!.contacts.isNotEmpty() && response.body()!!.result.success) {
                        regContactsId.add(response.body()!!.contacts[0].id)
                        if (response.body()!!.contacts[0].contact_type.equals("Почта")) regContactsType.add("mail")
                        regContacts.add(response.body()!!.contacts[0].contact)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRegContacts>, t: Throwable) {

            }
        })

    }

    fun getVKReg(){
        setVkRegStart(true)
    }

}