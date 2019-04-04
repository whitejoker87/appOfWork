package ru.jobni.jobni.model.menu.left

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.jobni.jobni.R
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent
import ru.jobni.jobni.model.network.company.CompanyList
import java.util.*
import kotlin.collections.ArrayList

object RepositoryOwner {

    val companyLiveData = MutableLiveData<MutableList<CompanyList>>()
    val receiveCompanyList: MutableList<CompanyList> = mutableListOf()

    val companyLiveDataBalance = MutableLiveData<Int>()
    var receiveCompanyBalance: Int = 0

    val setCompanyList = ArrayList<NavigationParent>()


    init {
        companyLiveData.value = receiveCompanyList
        companyLiveDataBalance.value = receiveCompanyBalance
    }

    fun getCompany(): LiveData<MutableList<CompanyList>> {
        return companyLiveData
    }

    fun getCompanyBalance(): LiveData<Int> {
        return companyLiveDataBalance
    }

    fun saveCompanyList(companyList: ArrayList<CompanyList>) {
        // Отсчистить список при заполнении
        receiveCompanyList.clear()
        receiveCompanyList.addAll(companyList)
        // Обновить лайфдату
        companyLiveData.value = receiveCompanyList
    }

    fun saveCompanyBalance(companyBalance: Int) {
        receiveCompanyBalance = companyBalance
        companyLiveDataBalance.value = receiveCompanyBalance
    }


    /* Блок функций если пользователь авторизован в приложении */
    fun makeNavigationListOwnerAuthOn(): List<NavigationParent> {
        return makeCompanyListAuthOn()
    }

    private fun makeCompanyListAuthOn(): List<NavigationParent> {
        // Отсчистить список при формировании
        setCompanyList.clear()

        setCompanyList.add(NavigationParent(
                "Добавить компанию",
                makeParentOneChildOwnerAuthOn(),
                R.drawable.ic_company
        ))

        companyLiveData.value?.forEach { companyName ->
            setCompanyList.add(NavigationParent(
                    companyName.name,
                    makeParentTwoChildOwnerAuthOn(companyName.id),
                    R.drawable.ic_user
            ))
        }
        return setCompanyList
    }

    private fun makeParentOneChildOwnerAuthOn(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwoChildOwnerAuthOn(parentID: Int): List<NavigationChild> {
        val child1 = NavigationChild(parentID, "Вакансии", 0)
        val child2 = NavigationChild(parentID, "Отзывы", 0)
        val child3 = NavigationChild(parentID, "Профиль", 0)
        val child4 = NavigationChild(parentID, "Финансы", 0)

        return listOf(child1, child2, child3, child4)
    }


    /* Блок функций если пользователь не авторизован в приложении */
    fun makeNavigationListOwnerAuthOff(): List<NavigationParent> {
        return Arrays.asList(
                makeParentOneOwnerAuthOff(),
                makeParentTwoOwnerAuthOff(),
                makeParentThreeOwnerAuthOff()
        )
    }

    private fun makeParentOneOwnerAuthOff(): NavigationParent {
        return NavigationParent(
                "Поиск",
                makeParentOneChildOwnerAuthOff(),
                R.drawable.ic_search
        )
    }

    private fun makeParentOneChildOwnerAuthOff(): List<NavigationChild> {
        val child1 = NavigationChild(1, "Резюме", R.drawable.ic_search)
        val child2 = NavigationChild(2, "Кандидаты", R.drawable.ic_search)
        val child3 = NavigationChild(3, "Вакансии", R.drawable.ic_search)
        val child4 = NavigationChild(4, "Компании", R.drawable.ic_company)

        return listOf(child1, child2, child3, child4)
    }

    private fun makeParentTwoOwnerAuthOff(): NavigationParent {
        return NavigationParent(
                "Добавить компанию",
                makeParentTwoChildOwnerAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentTwoChildOwnerAuthOff(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentThreeOwnerAuthOff(): NavigationParent {
        return NavigationParent(
                "Регистрация",
                makeParentThreeChildOwnerAuthOff(),
                R.drawable.ic_user
        )
    }

    private fun makeParentThreeChildOwnerAuthOff(): List<NavigationChild> {
        return listOf()
    }
}