package ru.jobni.jobni.model.menuleft

import ru.jobni.jobni.R
import java.util.*

object RepositoryOwner {

    fun makeNavigationListOwner(): List<NavigationParent> {
        return Arrays.asList(
            makeParentOneOwner(),
            makeParentTwoOwner()
        )
    }

    private fun makeParentOneOwner(): NavigationParent {
        return NavigationParent(
            "Добавить компанию",
            makeParentOneChildOwner(),
            R.drawable.ic_company
        )
    }

    private fun makeParentOneChildOwner(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwoOwner(): NavigationParent {
        return NavigationParent(
            "Компания 1",
            makeParentTwoChildOwner(),
            R.drawable.ic_user
        )
    }

    private fun makeParentTwoChildOwner(): List<NavigationChild> {
        val child1 = NavigationChild("Вакансии", 0)
        val child2 = NavigationChild("Отзывы", 0)
        val child3 = NavigationChild("Профиль", 0)
        val child4 = NavigationChild("Баланс: 1000 Руб", 0)
        val child5 = NavigationChild("Пополнить баланс", 0)
        val child6 =
            NavigationChild("История платежей", R.drawable.ic_user)
        val child7 = NavigationChild("Оказанные услуги", 0)

        return listOf(child1, child2, child3, child4, child5, child6, child7)
    }
}