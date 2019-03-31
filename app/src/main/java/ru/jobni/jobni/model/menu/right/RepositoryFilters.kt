package ru.jobni.jobni.model.menu.right

import ru.jobni.jobni.R
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent
import ru.jobni.jobni.model.network.vacancy.*
import java.util.*

object RepositoryFilters {

    fun makeNavigationListFilters(list: MutableList<Any>): List<NavigationParent> {
        val headers = ArrayList<NavigationParent>()
        list.forEach { str: Any ->
            headers.add(makeParent(str))
        }
        return headers
    }

    private fun makeParent(str: Any): NavigationParent {
        return NavigationParent(
                when (str) {
                    is String -> str
                    is PresenceGeography -> "География"
                    is ProfessionCompetence -> "Професии и компетенции"
                    is DesiredSalaryLevel -> "Зарплата"
                    is SocialPacket -> "Социальный пакет"
                    is More -> "Боооооольше"
                    is Auto -> "Авто"
                    is Raiting -> "Рейтинг"
                    is ViewsResponsesInvitations -> "Количество того и сего"
                    else -> "Опять что-то новое"
                },
                makeChild(str),
                R.drawable.ic_user
        )
    }

    private fun makeChild(str: Any): List<NavigationChild> {
        when (str) {
            is String -> return listOf()
            is PresenceGeography -> {
                val (country,
                        region,
                        city) = str
                return listOf(NavigationChild(1,country, 0),
                        NavigationChild(2,region, 0),
                        NavigationChild(3,city, 0))
            }
            is ProfessionCompetence -> {
                val (profession,
                        competence) = str
                return listOf(NavigationChild(1,profession, 0),
                        NavigationChild(2,competence, 0))
            }
            is DesiredSalaryLevel -> {
                val (salary_level_newbie,
                        salary_level_experienced,
                        monthly_rate,
                        rate_per_hour) = str
                return listOf(NavigationChild(1,salary_level_newbie, 0),
                        NavigationChild(2,salary_level_experienced, 0),
                        NavigationChild(3,monthly_rate, 0),
                        NavigationChild(4,rate_per_hour, 0))
            }
            is SocialPacket -> {
                val (housing,
                        travel,
                        meals,
                        gym,
                        childcare_facilities,
                        training) = str
                return listOf(NavigationChild(1,housing, 0),
                        NavigationChild(2,travel, 0),
                        NavigationChild(3,meals, 0),
                        NavigationChild(4,gym, 0),
                        NavigationChild(5,childcare_facilities, 0),
                        NavigationChild(6,training, 0))
            }
            is More -> {
                val (online,
                        corporate_events,
                        cellular_communication,
                        official_vehicles,
                        gasoline,
                        depreciation,
                        paid_holiday,
                        payment_of_sick_leave,
                        payment_of_maternity_allowances,
                        internationality,
                        notification,
                        viewed) = str
                return listOf(NavigationChild(1,online, 0),
                        NavigationChild(2,corporate_events, 0),
                        NavigationChild(3,cellular_communication, 0),
                        NavigationChild(4,official_vehicles, 0),
                        NavigationChild(5,gasoline, 0),
                        NavigationChild(6,depreciation, 0),
                        NavigationChild(7,paid_holiday, 0),
                        NavigationChild(8,payment_of_sick_leave, 0),
                        NavigationChild(9,payment_of_maternity_allowances, 0),
                        NavigationChild(10,internationality, 0),
                        NavigationChild(11,notification, 0),
                        NavigationChild(12,viewed, 0))
            }
            is Auto -> {
                val (list,
                        int) = str
                val (body_type,
                        fuel_type,
                        driver_license_categories) = list
                val (year_of_manufacture,
                        fuel_consumption_city,
                        fuel_consumption_highway,
                        cargo_capacity,
                        capacity_liters,
                        capacity_of_people) = int
                return listOf(NavigationChild(1,body_type, 0),
                        NavigationChild(2,fuel_type, 0),
                        NavigationChild(3,driver_license_categories, 0),
                        NavigationChild(4,year_of_manufacture, 0),
                        NavigationChild(5,fuel_consumption_city, 0),
                        NavigationChild(6,fuel_consumption_highway, 0),
                        NavigationChild(7,cargo_capacity, 0),
                        NavigationChild(8,capacity_liters, 0),
                        NavigationChild(9,capacity_of_people, 0))
            }
            is Raiting -> {
                val (labor_rating,
                        business_rating) = str
                return listOf(NavigationChild(1,labor_rating, 0),
                        NavigationChild(2,business_rating, 0))
            }
            is ViewsResponsesInvitations -> {
                val (number_of_views,
                        number_of_responses,
                        number_of_invitations) = str
                return listOf(NavigationChild(1,number_of_views, 0),
                        NavigationChild(2,number_of_responses, 0),
                        NavigationChild(3,number_of_invitations, 0))
            }
            else -> return listOf(NavigationChild(1,"Опять что-то новое", 0))
        }
    }
}

