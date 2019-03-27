package ru.jobni.jobni.model.menu.right

import ru.jobni.jobni.R
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent
import ru.jobni.jobni.model.network.vacancy.Auto
import ru.jobni.jobni.model.network.vacancy.Raiting
import ru.jobni.jobni.model.network.vacancy.Social_packet
import ru.jobni.jobni.model.network.vacancy.Zarplata
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
                    is Zarplata -> "Зарплата"
                    is Social_packet -> "Социальный пакет"
                    is Auto -> "Авто"
                    is Raiting -> "Рейтинг"
                    else -> "Опять что-то новое"
                },
                makeChild(str),
                R.drawable.ic_user
        )
    }

    private fun makeChild(str: Any): List<NavigationChild> {
        when (str) {
            is String -> return listOf()
            is Zarplata -> {
                val (salary_level_newbie,
                        salary_level_experienced,
                        monthly_rate,
                        rate_per_hour) = str
                return listOf(NavigationChild(salary_level_newbie, 0),
                        NavigationChild(salary_level_experienced, 0),
                        NavigationChild(monthly_rate, 0),
                        NavigationChild(rate_per_hour, 0))
            }
            is Social_packet -> {
                val (housing,
                        travel,
                        meals,
                        gym,
                        childcare_facilities,
                        training) = str
                return listOf(NavigationChild(housing, 0),
                        NavigationChild(travel, 0),
                        NavigationChild(meals, 0),
                        NavigationChild(gym, 0),
                        NavigationChild(childcare_facilities, 0),
                        NavigationChild(training, 0))
            }
            is Auto -> {
                val (year_of_manufacture,
                        fuel_consumption_city,
                        fuel_consumption_highway,
                        cargo_capacity,
                        capacity_of_people) = str
                return listOf(NavigationChild(year_of_manufacture, 0),
                        NavigationChild(fuel_consumption_city, 0),
                        NavigationChild(fuel_consumption_highway, 0),
                        NavigationChild(cargo_capacity, 0),
                        NavigationChild(capacity_of_people, 0))
            }
            is Raiting -> {
                val (labor_rating,
                        business_rating) = str
                return listOf(NavigationChild(labor_rating, 0),
                        NavigationChild(business_rating, 0))
            }
            else -> return listOf(NavigationChild("Опять что-то новое", 0))
        }
    }
}

