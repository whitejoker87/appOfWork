package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName

//data class Registration (
//        @SerializedName("email")val email: String,
//        @SerializedName("password")val password: String,
//        @SerializedName("password_confirm")val password_confirm: String,
//        @SerializedName("surname")val surname: String,
//        @SerializedName("name")val name: String,
//        @SerializedName("middlename")val middlename: String,
//        @SerializedName("referer")val referer: String,
////        val photo: String,
//        @SerializedName("contacts")val contacts: ArrayList<Contact>
//    ):Serializable

data class Contact (
        @SerializedName("contact_id")val contact_id: Int,
        @SerializedName("contact_type")val contact_type: String,
        @SerializedName("contact")val contact: String
)

data class ContactWithoutId (
    @SerializedName("contact_type")val contact_type: String,
    @SerializedName("contact")val contact: String
)

data class RegPass(
        @SerializedName("password")val password: String,
        @SerializedName("password_confirm")val password_confirm: String
)

data class BindEmail(
        @SerializedName("email")val email: String
)

data class BindPhone(
    @SerializedName("phone")val phone: String
)

data class ConfirmCode(
        @SerializedName("code")val code: String
)

data class RegContactFace(
        @SerializedName("surname")val surname: String,
        @SerializedName("name")val name: String,
        @SerializedName("middlename")val middlename: String
)

data class RegContactFaceContact(
        @SerializedName("consent_to_data_storage_and_protection")val protection: Boolean,
        @SerializedName("consent_public_offers")val offers: Boolean,
        @SerializedName("contacts")val contacts: ArrayList<Any>
)






//{"success":true,"result":{"id":7}}
data class ResponseRegStart(
        @SerializedName("success")val success: Boolean,
        @SerializedName("result")val result: RegStartResult

)

/*{"success":true,"result":{"_id":4350}}*/
/*{"success":false,"errors":{"email":["Это поле не может быть пустым."]}}*/
/*{"success":false,"errors":{"phone":["Номер телефона может содержать только цифры.","Длина номера телефона должна быть 10 цифр."]}}*/
/*{"success":true,"message":["Перейдите на почту для её подтверждения."]}*/
data class ResponseRegUser(
        @SerializedName("success")val success: Boolean,
        @SerializedName("result")val result: RegUserResult,
        @SerializedName("errors")val errors: RegUserErrors,
        @SerializedName("message")val message: List<String>


)

//{"success":true}
/*{"success":false,"errors":{"non_field_errors":["Учетные данные не были предоставлены."]}}*/
data class ResponseRegPass(
        @SerializedName("success")val success: Boolean,
        @SerializedName("errors")val errors: RegPassErrors

)
//{"success":true,"result":{"id":8}}
//{"success":false,"errors":{"non_field_errors":["Учетные данные не были предоставлены."]}}
//{"success":false,"errors":{"code":["Время жизни кода активации вышло, зарегистрируйтесь заново"]}}
data class ResponseRegConfirm(
    @SerializedName("success")val success: Boolean,
    @SerializedName("errors")val errors: RegPassErrors,
    @SerializedName("result")val result: RegStartResult
)

/*{"success":true}
{"success":false,
"errors":{"name":["Это поле не может быть пустым."],
"surname":["Это поле не может быть пустым."],
"middlename":["Это поле не может быть пустым."]}}*/
data class ResponseRegContactFace(
    @SerializedName("success")val success: Boolean,
    @SerializedName("errors")val errors: RegContactFaceErrors
)
/*{"success":true,
"result":
{"name":"Иван",
"surname":"Иванов",
"middlename":"Иванович",
"referer":"",
"photo":"",
"consent_to_data_storage_and_protection":false,
"consent_public_offers":false,
"login":"16",
"password":true,
"contacts":[{
"contact_type":"mail",
"contact_id":9,
"contact":"3@3.ru",
"incomplete_registration":true}]}}*/
data class ResponseRegGetContacts(
    @SerializedName("success")val success: Boolean,
    @SerializedName("result")val result: RegGetContactsResult
)
//{"success":false,"errors":{"contact_face":["Нельзя повторно пройти регистрацию"]}}
//{"success":true}
data class ResponseRegContactFaceContacts(
    @SerializedName("success")val success: Boolean,
    @SerializedName("errors")val errors: RegContactFaceContactsErrors
)



data class RegStartResult(
        @SerializedName("id")val id: Int
)

data class RegUserResult(
        @SerializedName("_id")val _id: Int
)

data class RegUserErrors(
        @SerializedName("email")val email: List<String>,
        @SerializedName("phone")val phone: List<String>

)

data class RegPassErrors(
    @SerializedName("non_field_errors")val non_field_errors: List<String>,
    @SerializedName("code")val code: List<String>
)

data class RegContactFaceErrors(
    @SerializedName("name")val name: List<String>,
    @SerializedName("surname")val surname: List<String>,
    @SerializedName("middlename")val middlename: List<String>
)

data class RegGetContactsResult(
    @SerializedName("name")val name: String,
    @SerializedName("surname")val surname: String,
    @SerializedName("middlename")val middlename: String,
    @SerializedName("referer")val referer: String,
    @SerializedName("photo")val photo: String,
    @SerializedName("consent_to_data_storage_and_protection")val consent_to_data_storage_and_protection: Boolean,
    @SerializedName("consent_public_offers")val consent_public_offers: Boolean,
    @SerializedName("login")val login: String,
    @SerializedName("password")val password: Boolean,
    @SerializedName("contacts")val contacts: List<ContactOptions>

)

data class RegContactFaceContactsErrors(
    @SerializedName("contact_face")val contact_face: List<String>
)



data class ContactOptions(
    @SerializedName("contact_type")val contact_type: String,
    @SerializedName("contact_id")val contact_id: Int,
    @SerializedName ("contact")val contact: String,
    @SerializedName("incomplete_registration")val incomplete_registration: Boolean
)
