package ru.jobni.jobni.model.menu.left

import ru.jobni.jobni.R
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent
import java.util.*

object RepositoryUser {

    /* Блок функций если пользователь авторизован в приложении */
    fun makeNavigationListUserAuthOn(): List<NavigationParent> {
        return Arrays.asList(
                makeParentOneAuthOn(),
                makeParentTwoAuthOn(),
                makeParentThreeAuthOn(),
                makeParentFourAuthOn()
        )
    }

    private fun makeParentOneAuthOn(): NavigationParent {
        return NavigationParent(
                "Резюме",
                makeParentOneChildAuthOn(),
                R.drawable.ic_user
        )
    }

    private fun makeParentOneChildAuthOn(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwoAuthOn(): NavigationParent {
        return NavigationParent(
                "Отзывы",
                makeParentTwoChildAuthOn(),
                R.drawable.ic_user
        )
    }

    private fun makeParentTwoChildAuthOn(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentThreeAuthOn(): NavigationParent {
        return NavigationParent(
                "Профиль",
                makeParentThreeChildAuthOn(),
                R.drawable.ic_user
        )
    }

    private fun makeParentThreeChildAuthOn(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentFourAuthOn(): NavigationParent {
        return NavigationParent(
                "Финансы",
                makeParentFourChildAuthOn(),
                R.drawable.ic_user
        )
    }

    private fun makeParentFourChildAuthOn(): List<NavigationChild> {
        val child1 = NavigationChild(1, "Баланс: 1000 Руб", "", 0)
        val child2 = NavigationChild(2, "Пополнить баланс", "", 0)
        val child3 = NavigationChild(3, "История платежей", "", 0)
        val child4 = NavigationChild(4, "Оказанные услуги", "", R.drawable.ic_user)
        val child5 = NavigationChild(5, "Оказанные услуги (детально)", "", 0)
        val child6 = NavigationChild(6, "Рефальная программа", "", 0)
        val child7 = NavigationChild(7, "CashBack", "", 0)

        return listOf(child1, child2, child3, child4, child5, child6, child7)
    }


    /* Блок функций если пользователь не авторизован в приложении */
    fun makeNavigationListUserAuthOff(): List<NavigationParent> {
        return Arrays.asList(
                makeParentOneAuthOff(),
                makeParentTwoAuthOff()
        )
    }

    private fun makeParentOneAuthOff(): NavigationParent {
        return NavigationParent(
                "Поиск",
                makeParentOneChildAuthOff(),
                R.drawable.ic_search
        )
    }

    private fun makeParentOneChildAuthOff(): List<NavigationChild> {
        val child1 = NavigationChild(1, "Вакансии", "", R.drawable.ic_search)
        val child2 = NavigationChild(2, "Компании", "", R.drawable.ic_company)
        val child3 = NavigationChild(3, "Резюме", "", R.drawable.ic_search)
        val child4 = NavigationChild(4, "Кандидаты", "", R.drawable.ic_search)

        return listOf(child1, child2, child3, child4)
    }

    private fun makeParentTwoAuthOff(): NavigationParent {
        return NavigationParent(
                "Регистрация",
                makeParentTwoChildAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentTwoChildAuthOff(): List<NavigationChild> {
        return listOf()
    }
}

