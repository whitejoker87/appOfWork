package ru.jobni.jobni.utils

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

class Retrofit : Application() {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://test.jobni.ru/"

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .client(getUnsafeOkHttpClient())
                .build()
        api = retrofit?.create(RetrofitQuery::class.java) //Создаем объект, при помощи которого будем выполнять запросы
    }

    companion object {

        var api: RetrofitQuery? = null
            private set
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
