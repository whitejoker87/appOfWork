package ru.jobni.jobni.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.jobni.jobni.R
import ru.jobni.jobni.utils.RetrofitQuery
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


class FragmentMain : Fragment() {

    private val BASE_URL = "https://test.jobni.ru/"
    private lateinit var retrofitQuery: RetrofitQuery

    private lateinit var searchReal: AutoCompleteTextView
    private lateinit var searchFake: SearchView

    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    private val DELAY_SERVER_RESPONSE: Long = 1000 // 1 sec

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""

        progressBar = view.findViewById(R.id.search_progress_bar) as ProgressBar

        searchFake = view.findViewById(R.id.search_view_fake) as SearchView
        button = view.findViewById(R.id.search_work) as Button
        val fakeLayout = view.findViewById(R.id.constraint_layout_fake) as ConstraintLayout

        searchReal = view.findViewById(R.id.search_view_real) as AutoCompleteTextView

        initRetrofit()

        initSearchFake(view, fakeLayout)
        initSearch()

        return view
    }

    private fun initSearch() {
        searchReal.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(arg0: AdapterView<*>, arg1: View, arg2: Int, arg3: Long) {
                Toast.makeText(context, "onSuggestionSelect", Toast.LENGTH_SHORT).show()
            }
        }

        searchReal.addTextChangedListener(object : TextWatcher {
            private var timer = Timer()

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            doSearchOnTextChange(s.toString())
                        }
                    },
                    DELAY_SERVER_RESPONSE
                )
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun showProgressBar(searchView: AutoCompleteTextView) {
        val id = searchView.context.resources.getIdentifier("android:id/search_plate", null, null)
        progressBar.animate().setDuration(200).alpha(1f).start()
    }

    fun hideProgressBar(searchView: AutoCompleteTextView) {
        val id = searchView.context.resources.getIdentifier("android:id/search_plate", null, null)
        progressBar.animate().setDuration(200).alpha(0f).start()
    }

    private fun initSearchFake(view: View, fakeLayout: ConstraintLayout) {
        button.setOnClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }

        searchFake.setOnClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }

        searchFake.setOnSearchClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }
    }

    private fun doSearchOnTextChange(query: String) {
        retrofitQuery.loadCompetence(query).enqueue(object : Callback<List<String>> {
            override fun onResponse(@NonNull call: Call<List<String>>, @NonNull response: Response<List<String>>) {
                if (response.body() != null) {

                    val resultList = response.body()

                    val suggestions: List<String> = ArrayList()
                    (suggestions as ArrayList<String>).clear()

                    if (response.body()!!.isEmpty()) {
                        suggestions.add(getString(R.string.search_view_suggestions_empty))
                    } else {
                        for (i in 0 until response.body()!!.size) {
                            suggestions.add(resultList!![i])
                        }
                        val adapter = ArrayAdapter<String>(context!!, R.layout.search_item, R.id.item, suggestions)
                        searchReal.setAdapter<ArrayAdapter<String>>(adapter)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<List<String>>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })

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
}