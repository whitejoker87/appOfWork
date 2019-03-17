package ru.jobni.jobni.viewmodel

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.model.network.registration.Registration
import ru.jobni.jobni.model.network.vacancy.DetailVacancy
import ru.jobni.jobni.utils.Retrofit

class RegViewModel: ViewModel() {

    private val regMail = MutableLiveData<String>("")

    fun setRegMail(mail: String) {
        regMail.value = mail
    }

    fun getRegMail(): MutableLiveData<String> = regMail

    private val regPassword = MutableLiveData<String>("")

    fun setRegPassword(pass: String) {
        regPassword.value = pass
    }

    fun getRegPassword(): MutableLiveData<String> = regPassword

    private val regPassConfirm = MutableLiveData<String>("")

    fun setRegPassConfirm(passConfirm: String) {
        regPassConfirm.value = passConfirm
    }

    fun getRegPassConfirm(): MutableLiveData<String> = regPassConfirm

    private val regSurname = MutableLiveData<String>("")

    fun setRegSurname(surname: String) {
        regSurname.value = surname
    }

    fun getRegSurname(): MutableLiveData<String> = regSurname

    private val regName = MutableLiveData<String>("")

    fun setRegName(name: String) {
        regName.value = name
    }

    fun getRegName(): MutableLiveData<String> = regName

    private val regMiddlename = MutableLiveData<String>("")

    fun setRegMiddlename(middlename: String) {
        regMiddlename.value = middlename
    }

    fun getRegMiddlename(): MutableLiveData<String> = regMiddlename

    private val regReferer = MutableLiveData<String>("")

    fun setRegReferer(referer: String) {
        regReferer.value = referer
    }

    fun getRegReferer(): MutableLiveData<String> = regReferer

    private val regPhoto = MutableLiveData<String>("")

    fun setRegPhoto(photo: String) {
        regPhoto.value = photo
    }

    fun getRegPhoto(): MutableLiveData<String> = regPhoto

    private val regContact = MutableLiveData<String>("")

    fun setRegContact(contact: String) {
        regContact.value = contact
    }

    fun getRegContact(): MutableLiveData<String> = regContact

//    private val regContacts = MutableLiveData<List<String>>()
//
//    fun setRegContacts(contacts: List<String>) {
//        regContacts.value = contacts
//    }
//
//    fun getRegContacts(): MutableLiveData<List<String>> = regContacts

    private val startNextRegPhase = MutableLiveData<Int>()

    fun setStartNextRegPhase(nextPhase: Int) {
        startNextRegPhase.value = nextPhase
    }

    fun getStartNextRegPhase(): MutableLiveData<Int> = startNextRegPhase

    fun btnRegClick() {
        val contacts = listOf(regContact.value!!)
        val registration = Registration(regMail.value!!,
            regPassword.value!!,
            regPassConfirm.value!!,
            regSurname.value!!,
            regName.value!!,
            regMiddlename.value!!,
            regReferer.value!!,
            regPhoto.value!!,
            contacts)
        Retrofit.api?.sendRegistrationData(registration)?.enqueue(object : Callback<String> {
            override fun onResponse(@NonNull call: Call<String>, @NonNull response: Response<String>) {
                if (response.body() != null) {

                }
            }

            override fun onFailure(@NonNull call: Call<String>, @NonNull t: Throwable) {
                //Toast.makeText(applicationContext, "Error in download for menu!", Toast.LENGTH_LONG).show()
            }
        })
    }

}