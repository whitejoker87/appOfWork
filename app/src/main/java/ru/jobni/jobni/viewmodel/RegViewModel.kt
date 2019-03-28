package ru.jobni.jobni.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.model.auth.UserAuth
import ru.jobni.jobni.model.network.registration.RegContactFace
import ru.jobni.jobni.model.network.registration.RegUser
import ru.jobni.jobni.model.network.registration.ResponseReg
import ru.jobni.jobni.utils.Retrofit


class RegViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    private val authUserSessionID = "userSessionID"
    private val authUserName = "userName"
    private val authUserPass = "userPass"

    var sPrefAuthUser = application.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)

    private val regMail = MutableLiveData<String>("mail@ya.ru")
    private val regPassword = MutableLiveData<String>("11111111")
    private val regPassConfirm = MutableLiveData<String>("11111111")
    private val regSurname = MutableLiveData<String>("Иванов")
    private val regName = MutableLiveData<String>("Иван")
    private val regMiddlename = MutableLiveData<String>("Иванович")
    private val regReferer = MutableLiveData<String>("")
    private val regPhoto = MutableLiveData<Drawable>()
    private val regContacts = MutableLiveData<List<String>>(arrayListOf(""))
    private val regPhone = MutableLiveData<String>("")
    private val regPhoneCode = MutableLiveData<String>("")

    private val startNextRegPhase = MutableLiveData<Int>()

    private val isPrivilegesForFileDone = MutableLiveData<Boolean>()

    private val numberOfVisibleItemReg = MutableLiveData<Int>(-1)

    private val resultReg1Success = MutableLiveData<Boolean>(false)
    private val resultReg2Success = MutableLiveData<Boolean>(false)
    private val resultReg3Success = MutableLiveData<Boolean>(false)

    private val resultAuthSuccess = MutableLiveData<Boolean>(false)
    private val typeAddRegFragment = MutableLiveData<String>("mail")

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

    fun setRegContacts(contacts: List<String>) {
        regContacts.value = contacts
    }

    fun getRegContacts(): MutableLiveData<List<String>> = regContacts




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

    fun btnRegUserClick() {

        val user = RegUser(
            regMail.value!!,
            regPassword.value!!,
            regPassConfirm.value!!
        )
        Retrofit.api?.sendRegistrationUser(user)?.enqueue(object : Callback<ResponseReg> {

            override fun onResponse(call: Call<ResponseReg>, response: Response<ResponseReg>) {
                if (response.body() != null) {

                    /*D/OkHttp: <-- 200 https://test.jobni.ru/api/registration_user/ (1833ms)
                        server: nginx/1.14.2
                        date: Sun, 24 Mar 2019 13:27:40 GMT
                        content-type: application/json
                        content-length: 108
                        vary: Accept, Origin, Cookie
                        allow: OPTIONS, POST
                        x-frame-options: DENY
                        strict-transport-security: max-age=3600
                        x-content-type-options: nosniff
                        x-xss-protection: 1; mode=block
                    D/OkHttp: access-control-allow-headers: *
                        {"success":true,"error_text":["Перейдите на почту для её подтверждения."]}
                        <-- END HTTP (108-byte body)*/
                    response.body()?.let {
                        setResultReg1Success(it.success)
                    }
                    Toast.makeText(context, "Успешно отправдены дынные user ${resultReg1Success.value}", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseReg>, t: Throwable) {

            }
        })
    }

    fun btnRegClick() {

        setPrivilegesForFileDone(false)

    }

    fun tempAuthForRegOne() {

        val userData = UserAuth(regMail.value, regPassword.value)

        Retrofit.api?.postAuthData("AuthUser", userData)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                if (response.body() != null) {

                    val resultListHeaders = response.headers().get("Set-Cookie")

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
                        setResultAuthSuccess(true)
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error! in zq rega 1", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun btnRegContactFaceClick() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val contactFace = RegContactFace(
            regSurname.value!!,
            regName.value!!,
            regMiddlename.value!!
        )
        Retrofit.api?.sendRegistrationContactFace(cid, contactFace)?.enqueue(object : Callback<ResponseReg> {

            override fun onResponse(call: Call<ResponseReg>, response: Response<ResponseReg>) {
                if (response.body() != null) {

                    response.body()?.let {
                        setResultReg2Success(it.success)
                    }
                    Toast.makeText(context, "Успешно отправдены дынные user ${resultReg2Success}", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseReg>, t: Throwable) {
                Toast.makeText(context, "Error! in zq rega 222", Toast.LENGTH_SHORT).show()
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

    fun btnAddContactClick() {
        val listContacts = regContacts.value as MutableList
        listContacts.add("")
        setRegContacts(listContacts)
    }

}