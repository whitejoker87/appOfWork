package ru.jobni.jobni.model.menuleft

import androidx.annotation.NonNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.network.company.CompanyVacancy
import ru.jobni.jobni.model.network.company.ResultsCompany
import ru.jobni.jobni.utils.Retrofit

object RepositoryOwner {

    fun makeNavigationListOwnerAuthOn(): List<NavigationParent> {
        return makeCompanyListAuthOn()
    }

    private fun makeCompanyListAuthOn(): List<NavigationParent> {

//        val receiveCompanyList = listOf("Компания 1", "Компания 2")
        val setCompanyList = ArrayList<NavigationParent>()
        val receiveCompanyList: MutableList<String> = mutableListOf()

        Retrofit.api?.ownerOrWorker()?.enqueue(object : Callback<CompanyVacancy> {
            override fun onResponse(@NonNull call: Call<CompanyVacancy>, @NonNull response: Response<CompanyVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsCompany> = response.body()!!.results

                    for (i in 0 until resultList.size) {
                        receiveCompanyList.add(resultList[i].name)
                    }
//                    println("1 $receiveCompanyList")
                }
            }

            override fun onFailure(@NonNull call: Call<CompanyVacancy>, @NonNull t: Throwable) {}
        })

        setCompanyList.add(NavigationParent(
                "Добавить компанию",
                makeParentOneChildOwnerAuthOn(),
                R.drawable.ic_company
        ))

        receiveCompanyList.forEach { companyName ->
            setCompanyList.add(NavigationParent(
                    companyName,
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
        val child4 = NavigationChild("Баланс: 1000 Руб", 0)
        val child5 = NavigationChild("Пополнить баланс", 0)
        val child6 =
                NavigationChild("История платежей", R.drawable.ic_user)
        val child7 = NavigationChild("Оказанные услуги", 0)

        return listOf(child1, child2, child3, child4, child5, child6, child7)
    }




    fun makeNavigationListOwnerAuthOff(): List<NavigationParent> {
        return makeCompanyListAuthOff()
    }

    private fun makeCompanyListAuthOff(): List<NavigationParent> {

//        val receiveCompanyList = listOf("Компания 1", "Компания 2")
        val setCompanyList = ArrayList<NavigationParent>()
        val receiveCompanyList: MutableList<String> = mutableListOf()

        Retrofit.api?.ownerOrWorker()?.enqueue(object : Callback<CompanyVacancy> {
            override fun onResponse(@NonNull call: Call<CompanyVacancy>, @NonNull response: Response<CompanyVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsCompany> = response.body()!!.results

                    for (i in 0 until resultList.size) {
                        receiveCompanyList.add(resultList[i].name)
                    }
//                    println("1 $receiveCompanyList")
                }
            }

            override fun onFailure(@NonNull call: Call<CompanyVacancy>, @NonNull t: Throwable) {}
        })

        setCompanyList.add(NavigationParent(
                "Добавить компанию",
                makeParentOneChildOwnerAuthOff(),
                R.drawable.ic_company
        ))

        receiveCompanyList.forEach { companyName ->
            setCompanyList.add(NavigationParent(
                    companyName,
                    makeParentTwoChildOwnerAuthOff(),
                    R.drawable.ic_user
            ))
        }
        return setCompanyList
    }

    private fun makeParentOneChildOwnerAuthOff(): List<NavigationChild> {
        return listOf()
    }

    private fun makeParentTwoChildOwnerAuthOff(): List<NavigationChild> {
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