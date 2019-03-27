package ru.jobni.jobni.model.menu.left

import ru.jobni.jobni.R
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent

object RepositoryOwner {

    fun makeNavigationListOwner(): List<NavigationParent> {
        return makeCompanyList()
    }

    private fun makeCompanyList(): List<NavigationParent> {
        val receiveCompanyList = listOf("Компания 1", "Компания 2")
        val setCompanyList = ArrayList<NavigationParent>()

        setCompanyList.add(NavigationParent(
                "Добавить компанию",
                makeParentOneChildOwner(),
                R.drawable.ic_company
        ))

        receiveCompanyList.forEach { companyName ->
            setCompanyList.add(NavigationParent(
                    companyName,
                    makeParentTwoChildOwner(),
                    R.drawable.ic_user
            ))
        }
        return setCompanyList
    }

    private fun makeParentOneChildOwner(): List<NavigationChild> {
        return listOf()
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