package ru.jobni.jobni.model.menuleft

import ru.jobni.jobni.R
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
                R.drawable.ic_user
        )
    }

    private fun makeParentOneChildAuthOff(): List<NavigationChild> {
        val child1 = NavigationChild("Вакансии", 0)
        val child2 = NavigationChild("Компании", 0)
        val child3 = NavigationChild("Резюме", 0)
        val child4 = NavigationChild("Кандидаты", 0)

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

