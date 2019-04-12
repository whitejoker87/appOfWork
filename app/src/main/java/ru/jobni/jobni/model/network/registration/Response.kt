package ru.jobni.jobni.model.network.registration

import com.google.gson.annotations.SerializedName

//{"success":true,"result":{"id":7}}
data class ResponseRegStart(
        @SerializedName("success")val success: Boolean,
        @SerializedName("result")val result: RegStartResult

)

/*тут собраны все ответы от апи привязки какой то учетной записи*/
/*{"success":true,"result":{"_id":4350}}*/
/*{"success":false,"errors":{"email":["Это поле не может быть пустым."]}}*/
/*{"success":false,"errors":{"phone":["Номер телефона может содержать только цифры.","Длина номера телефона должна быть 10 цифр."]}}*/
/*{"success":true,"message":["Перейдите на почту для её подтверждения."]}*/
/*{"success":false,"errors":{"uid":["Пользователь уже имеет привязку."]}}*/
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
