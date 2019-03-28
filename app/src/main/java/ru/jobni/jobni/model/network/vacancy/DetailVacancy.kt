package ru.jobni.jobni.model.network.vacancy

import com.google.gson.annotations.SerializedName

data class DetailVacancy(

        @SerializedName("language_and_level_of_proficiency") val language_and_level_of_proficiency : String,
        @SerializedName("work_places") val work_places : String,
        @SerializedName("employment") val employment : String,
        @SerializedName("format_of_work") val format_of_work : String,
        @SerializedName("field_of_activity") val field_of_activity : String,
        @SerializedName("age") val age : String,
        @SerializedName("required_number_of_people") val required_number_of_people : String,
        @SerializedName("update_time") val update_time : String,
        @SerializedName("presence_geography") val presence_geography : PresenceGeography,
        @SerializedName("profession_and_competence") val profession_and_competence : ProfessionCompetence,
        @SerializedName("desired_salary_level") val desired_salary_level : DesiredSalaryLevel,
        @SerializedName("social_packet") val social_packet : SocialPacket,
        @SerializedName("more") val more : More,
        @SerializedName("auto") val auto : Auto,
        @SerializedName("raiting") val raiting : Raiting,
        @SerializedName("views_responses_invitations_int") val views_responses_invitations_int : ViewsResponsesInvitations
)

data class PresenceGeography(

        @SerializedName("country") val country : String,
        @SerializedName("region") val region : String,
        @SerializedName("city") val city : String
)

data class DesiredSalaryLevel(
        @SerializedName("desired_salary_level_newbie") val desired_salary_level_newbie: String,
        @SerializedName("desired_salary_level_experienced") val desired_salary_level_experienced: String,
        @SerializedName("monthly_rate") val monthly_rate: String,
        @SerializedName("rate_per_hour") val rate_per_hour: String
)

data class SocialPacket(

        @SerializedName("housing") val housing: String,
        @SerializedName("travel") val travel: String,
        @SerializedName("meals") val meals: String,
        @SerializedName("gym") val gym: String,
        @SerializedName("childcare_facilities") val childcare_facilities: String,
        @SerializedName("training") val training: String
)

data class Auto(

        @SerializedName("list") val list : ListofCar,
        @SerializedName("int") val int : IntOfCar
)

data class Raiting(

    @SerializedName("labor_rating") val labor_rating: String,
    @SerializedName("business_rating") val business_rating: String
)



data class More(
        @SerializedName("online") val online : String,
        @SerializedName("corporate_events") val corporate_events : String,
        @SerializedName("cellular_communication") val cellular_communication : String,
        @SerializedName("official_vehicles") val official_vehicles : String,
        @SerializedName("gasoline") val gasoline : String,
        @SerializedName("depreciation") val depreciation : String,
        @SerializedName("paid_holiday") val paid_holiday : String,
        @SerializedName("payment_of_sick_leave") val payment_of_sick_leave : String,
        @SerializedName("payment_of_maternity_allowances") val payment_of_maternity_allowances : String,
        @SerializedName("internationality") val internationality : String,
        @SerializedName("notification") val notification : String,
        @SerializedName("viewed") val viewed : String
)

data class ViewsResponsesInvitations(

        @SerializedName("number_of_views") val number_of_views: String,
        @SerializedName("number_of_responses") val number_of_responses: String,
        @SerializedName("number_of_invitations") val number_of_invitations: String
)

data class IntOfCar (

        @SerializedName("year_of_manufacture") val year_of_manufacture : String,
        @SerializedName("fuel_consumption_city") val fuel_consumption_city : String,
        @SerializedName("fuel_consumption_highway") val fuel_consumption_highway : String,
        @SerializedName("cargo_capacity") val cargo_capacity : String,
        @SerializedName("capacity_liters") val capacity_liters : String,
        @SerializedName("capacity_of_people") val capacity_of_people : String
)

data class ListofCar (

        @SerializedName("body_type") val body_type : String,
        @SerializedName("fuel_type") val fuel_type : String,
        @SerializedName("driver_license_categories") val driver_license_categories : String
)

data class ProfessionCompetence (

        @SerializedName("profession") val profession : String,
        @SerializedName("competence") val competence : String
)



