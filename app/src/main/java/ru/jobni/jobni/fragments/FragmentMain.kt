package ru.jobni.jobni.fragments

import android.app.SearchManager
import android.content.Context
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.*
import android.widget.Button
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.jobni.jobni.R
import ru.jobni.jobni.model.*
import ru.jobni.jobni.utils.CompanyRequest
import ru.jobni.jobni.utils.RetrofitQuery
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.reflect.full.memberProperties


class FragmentMain : Fragment() {

    private val BASE_URL = "https://test.jobni.ru/"
    private lateinit var retrofitQuery: RetrofitQuery

    private var lastRequestTime = 0L
    private var requestTimeout = 2000L // 2 sec

    private lateinit var searchItem: SearchView
    private val SEARCH_VIEW_LIMIT_COUNT = 10

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnList: Button

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView
    private val  headerList = ArrayList<String>()
    private val childList = HashMap<String, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""

        drawerLayout = view.findViewById(R.id.drawer_layout)

        btnList = view.findViewById(R.id.btn_list)
        btnList.setOnClickListener { openRightMenu() }

        expandableListView = view.findViewById(R.id.exp_list_view)

        val navigationView: NavigationView = view.findViewById(R.id.navigation_view);

        searchItem = view.findViewById(R.id.search_view) as SearchView
        searchItem.queryHint = getString(R.string.search_view_hint)

        initRetrofit()

        search()

        return view
    }

    private fun search() {
        val (suggestionAdapter: CursorAdapter, suggestions: List<String>) = searchSuggestion()

        searchItem.suggestionsAdapter = suggestionAdapter

        searchItem.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                Toast.makeText(context, "onSuggestionSelect", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                searchItem.setQuery((suggestions as ArrayList<String>)[position], false)
                searchItem.clearFocus()
                doSearchOnClick(suggestions[position])
                Toast.makeText(context, "onSuggestionClick", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(context, "onQueryTextSubmit", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                doSearchOnTextChange(query, suggestions, suggestionAdapter)
                return true
            }
        })
    }

    private fun searchSuggestion(): Pair<CursorAdapter, List<String>> {
        val suggestionAdapter: CursorAdapter = SimpleCursorAdapter(
            context,
            android.R.layout.simple_list_item_1,
            null,
            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            intArrayOf(android.R.id.text1),
            0
        )

        val suggestions: List<String> = ArrayList()
        return Pair(suggestionAdapter, suggestions)
    }

    private fun doSearchOnClick(query: String) {
        //"not implemented"
    }

    private fun doSearchOnTextChange(
        query: String,
        suggestions: List<String>,
        suggestionAdapter: CursorAdapter
    ) {
        // Частота запросов на сервер для формирования списка
        if (System.currentTimeMillis() - lastRequestTime > requestTimeout) {
            lastRequestTime = System.currentTimeMillis()

            retrofitQuery.loadCompany(query).enqueue(object : Callback<CompanyRequest> {
                override fun onResponse(@NonNull call: Call<CompanyRequest>, @NonNull response: Response<CompanyRequest>) {
                    if (response.body() != null) {

                        val resultList = arrayOf(response.body()?.results)
                        val resultListItem = resultList[0]

                        (suggestions as ArrayList<String>).clear()

                        if (response.body()?.results!!.isEmpty()) {
                            suggestions.add(getString(R.string.search_view_suggestions_empty))
                        } else {
                            for (i in 0 until resultListItem!!.size) {
                                if (i < SEARCH_VIEW_LIMIT_COUNT) {
                                    val result = resultListItem[i]
                                    suggestions.add(result.name.toString())
                                }
                            }
                        }

                        val columns = arrayOf(
                            BaseColumns._ID,
                            SearchManager.SUGGEST_COLUMN_TEXT_1,
                            SearchManager.SUGGEST_COLUMN_INTENT_DATA
                        )

                        val cursor = MatrixCursor(columns)

                        for (i in 0 until suggestions.size) {
                            val tmp = arrayOf(Integer.toString(i), suggestions[i], suggestions[i])
                            cursor.addRow(tmp)
                        }
                        suggestionAdapter.swapCursor(cursor)
                    }
                }

                override fun onFailure(@NonNull call: Call<CompanyRequest>, @NonNull t: Throwable) {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) //Базовая часть адреса
            .addConverterFactory(GsonConverterFactory.create()) //Конвертер для преобразования JSON'а в объекты
            .client(getUnsafeOkHttpClient())
            .build()

        //Создаем объект, при помощи которого будем выполнять запросы
        retrofitQuery = retrofit.create(RetrofitQuery::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            // Эмуляция положительных запросов при отсутствующем сертификате на сервере
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Менеджер для запросов
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // SSL Socket для менеджера
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { _, _ -> true }
            return builder
                //Для дебага запросов Retrofit GET/POST
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun prepareListData() {

        val top250 = ArrayList<String>()
        top250.add("The Shawshank Redemption")
        top250.add("The Godfather")
        top250.add("The Godfather: Part II")
        top250.add("Pulp Fiction")
        top250.add("The Good, the Bad and the Ugly")
        top250.add("The Dark Knight")
        top250.add("12 Angry Men")

        headerList.forEach {  str ->
            childList.put(str, top250)
        }
    }

    fun openRightMenu() {
        retrofitQuery.loadDetailVacancy().enqueue(object : Callback<DetailVacancy> {
            override fun onResponse(@NonNull call: Call<DetailVacancy>, @NonNull response: Response<DetailVacancy>) {
                if (response.body() != null) {
                    val(competence,languages,work_places,employment,format_of_work,field_of_activity,age_company,required_number_of_people, zarplata, social_packet,auto,raiting) = response.body()!!
//                      Вариант выдвчи инфы о полях класса
//                    DetailVacancy::class.memberProperties.forEach { member ->
//                        val name = member.name
//                        val value = member.get(instance) as String
//
//                        findTextViewByName(name).text = value
//                    }
                    val detailList: MutableList<Any> = mutableListOf(competence,languages,work_places,employment,format_of_work,field_of_activity,age_company,required_number_of_people, zarplata, social_packet,auto,raiting)
                    detailList.forEach { str:Any ->
                            if (str is String)headerList.add(str)
                            else when(str) {
                                is Zarplata -> headerList.add("Зарплата")
                                is Social_packet -> headerList.add("Социальный пакет")
                                is Auto -> headerList.add("Авто")
                                is Raiting -> headerList.add("Рейтинг")
                            }
                    }

                    prepareListData()

                    expandableListAdapter = ExpandableListAdapter(activity as Context, headerList, childList)
                    expandableListView.setAdapter(expandableListAdapter)
                }
            }

            override fun onFailure(@NonNull call: Call<DetailVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error in download for menu!", Toast.LENGTH_LONG).show()
            }
        })
        drawerLayout.openDrawer(GravityCompat.END)
    }

}