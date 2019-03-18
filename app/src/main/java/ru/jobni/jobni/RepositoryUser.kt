package ru.jobni.jobni

import java.util.*

object RepositoryUser {

    fun makeNavigationListUser(): List<NavigationParent> {
        return Arrays.asList(
            makeParentOne(),
            makeParentTwo(),
            makeParentThree(),
            makeParentFour()
        )
    }

    private fun makeParentOne(): NavigationParent {
        return NavigationParent("Резюме", makeParentOneChild(), R.drawable.ic_user)
    }

    private fun makeParentOneChild(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwo(): NavigationParent {
        return NavigationParent("Отзывы", makeParentTwoChild(), R.drawable.ic_user)
    }

    private fun makeParentTwoChild(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentThree(): NavigationParent {
        return NavigationParent("Профиль", makeParentThreeChild(), R.drawable.ic_user)
    }

    private fun makeParentThreeChild(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentFour(): NavigationParent {
        return NavigationParent("Финансы", makeParentFourChild(), R.drawable.ic_user)
    }
    
    private fun makeParentFourChild(): List<NavigationChild> {
        val child1 = NavigationChild("Баланс: 1000 Руб", 0)
        val child2 = NavigationChild("Пополнить баланс", 0)
        val child3 = NavigationChild("История платежей", 0)
        val child4 = NavigationChild("Оказанные услуги", R.drawable.ic_user)
        val child5 = NavigationChild("Оказанные услуги (детально)", 0)
        val child6 = NavigationChild("Рефальная программа", 0)
        val child7 = NavigationChild("Cashback", 0)

        return listOf(child1, child2, child3, child4, child5, child6, child7)
    }
}

