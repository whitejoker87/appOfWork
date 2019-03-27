package ru.jobni.jobni.model.menu.right

import com.google.gson.annotations.SerializedName
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
        val headers = ArrayList<String>()
        list.forEach { str: Any ->
            if (str is String) headers.add(str)
            else when (str) {
                is Zarplata -> headers.add("Зарплата")
                is Social_packet -> headers.add("Социальный пакет")
                is Auto -> headers.add("Авто")
                is Raiting -> headers.add("Рейтинг")
            }
        }
        return Arrays.asList(
                list.forEach { str: Any ->
                    if (str is String) headers.add(str)
                    else makeParentOne(str)





//                        when (str) {
//                        is Zarplata -> headers.add("Зарплата")
//                        is Social_packet -> headers.add("Социальный пакет")
//                        is Auto -> headers.add("Авто")
//                        is Raiting -> headers.add("Рейтинг")
//                    }
                }
//                makeParentOne(),
//                makeParentTwo(),
//                makeParentThree(),
//                makeParentFour()
//        )
    }

    private fun makeParentOne(str: Any): NavigationParent {
        return NavigationParent(
                        when (str) {
                        is Zarplata -> "Зарплата"
                        is Social_packet -> "Социальный пакет"
                        is Auto -> "Авто"
                        is Raiting -> "Рейтинг"
                            else -> "Опять что-то новое"
                    },
                makeParentOneChild(str),
                R.drawable.ic_user
        )
    }

    private fun makeParentOneChild(str: Any): List<NavigationChild> {
        when (str) {
            is Zarplata -> {
                val(salary_level_newbie,
                        salary_level_experienced,
                        monthly_rate,
                        rate_per_hour) = str as Zarplata
                return listOf(NavigationChild(salary_level_newbie, 0),
                        NavigationChild(salary_level_experienced, 0),
                        NavigationChild(monthly_rate, 0),
                        NavigationChild(rate_per_hour, 0))
            }
            is Social_packet -> "Социальный пакет"
            is Auto -> "Авто"
            is Raiting -> "Рейтинг"
            else -> "Опять что-то новое"
        }
        return listOf(val (competence,
                languages,
                work_places,
                employment,
                format_of_work,
                field_of_activity,
                age_company,
                required_number_of_people,
                zarplata, social_packet,
                auto,
                raiting) = str)
    }

    private fun makeParentTwo(): NavigationParent {
        return NavigationParent(
                "Отзывы",
                makeParentTwoChild(),
                R.drawable.ic_user
        )
    }

    private fun makeParentTwoChild(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentThree(): NavigationParent {
        return NavigationParent(
                "Профиль",
                makeParentThreeChild(),
                R.drawable.ic_user
        )
    }

    private fun makeParentThreeChild(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentFour(): NavigationParent {
        return NavigationParent(
                "Финансы",
                makeParentFourChild(),
                R.drawable.ic_user
        )
    }
    
    private fun makeParentFourChild(): List<NavigationChild> {
        val child1 = NavigationChild("Баланс: 1000 Руб", 0)
        val child2 = NavigationChild("Пополнить баланс", 0)
        val child3 = NavigationChild("История платежей", 0)
        val child4 =
                NavigationChild("Оказанные услуги", R.drawable.ic_user)
        val child5 = NavigationChild("Оказанные услуги (детально)", 0)
        val child6 = NavigationChild("Рефальная программа", 0)
        val child7 = NavigationChild("Cashback", 0)

        return listOf(child1, child2, child3, child4, child5, child6, child7)
    }
}

