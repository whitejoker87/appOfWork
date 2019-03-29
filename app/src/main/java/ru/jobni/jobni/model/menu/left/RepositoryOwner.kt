package ru.jobni.jobni.model.menu.left

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.jobni.jobni.R
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent
import ru.jobni.jobni.model.network.company.ResultsCompany
import java.util.*
import kotlin.collections.ArrayList

object RepositoryOwner {

    val companyLiveData = MutableLiveData<MutableList<ResultsCompany>>()
    val receiveCompanyList: MutableList<ResultsCompany> = mutableListOf()

    val companyLiveDataBalance = MutableLiveData<Int>()
    var receiveCompanyBalance: Int = 0

    val setCompanyList = ArrayList<NavigationParent>()


    init {
        companyLiveData.value = receiveCompanyList
        companyLiveDataBalance.value = receiveCompanyBalance
    }

    fun getCompany(): LiveData<MutableList<ResultsCompany>> {
        return companyLiveData
    }

    fun getCompanyBalance(): LiveData<Int> {
        return companyLiveDataBalance
    }

    fun saveCompanyList(companyList: ArrayList<ResultsCompany>) {
        // Отсчистить список при заполнении
        receiveCompanyList.clear()
        receiveCompanyList.addAll(companyList)
        // Обновить лайфдату
        companyLiveData.value = receiveCompanyList
    }

    fun saveCompanyBalance(companyBalance: Int) {
        println("1 " + companyBalance)

        receiveCompanyBalance = companyBalance
        println("2 " + receiveCompanyBalance)

        companyLiveDataBalance.value = receiveCompanyBalance
        println("3 " + companyLiveDataBalance.value)
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
                    makeParentTwoChildOwnerAuthOn(),
                    R.drawable.ic_user
            ))
        }
        return setCompanyList
    }

    private fun makeParentOneChildOwnerAuthOn(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwoChildOwnerAuthOn(): List<NavigationChild> {
        val child1 = NavigationChild("Вакансии", 0)
        val child2 = NavigationChild("Отзывы", 0)
        val child3 = NavigationChild("Профиль", 0)
        val child4 = NavigationChild("Баланс: " + companyLiveDataBalance.value, 0)
        val child5 = NavigationChild("Пополнить баланс", 0)
        val child6 = NavigationChild("История платежей", R.drawable.ic_user)
        val child7 = NavigationChild("Оказанные услуги", 0)

        return listOf(child1, child2, child3, child4, child5, child6, child7)
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
        val child1 = NavigationChild("Резюме", R.drawable.ic_search)
        val child2 = NavigationChild("Кандидаты", R.drawable.ic_search)
        val child3 = NavigationChild("Вакансии", R.drawable.ic_search)
        val child4 = NavigationChild("Компании", R.drawable.ic_company)

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