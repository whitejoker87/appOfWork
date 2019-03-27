package ru.jobni.jobni.model.menuleft

import ru.jobni.jobni.R
import java.util.*

object RepositoryUser {

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


    fun makeNavigationListUserAuthOff(): List<NavigationParent> {
        return Arrays.asList(
                makeParentOneAuthOff(),
                makeParentTwoAuthOff(),
                makeParentThreeAuthOff(),
                makeParentFourAuthOff()
        )
    }

    private fun makeParentOneAuthOff(): NavigationParent {
        return NavigationParent(
                "Резюме",
                makeParentOneChildAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentOneChildAuthOff(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwoAuthOff(): NavigationParent {
        return NavigationParent(
                "Отзывы",
                makeParentTwoChildAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentTwoChildAuthOff(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentThreeAuthOff(): NavigationParent {
        return NavigationParent(
                "Профиль",
                makeParentThreeChildAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentThreeChildAuthOff(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentFourAuthOff(): NavigationParent {
        return NavigationParent(
                "Финансы",
                makeParentFourChildAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentFourChildAuthOff(): List<NavigationChild> {
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

