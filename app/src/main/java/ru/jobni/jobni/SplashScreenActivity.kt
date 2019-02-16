package ru.jobni.jobni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Bundle - свежий старт активности. А нам нужно загружаться только в 1 раз - поэтому проверка
        // TODO: Изучить Bundle

        if (savedInstanceState == null) {
            switchToMainActivity()
        }
    }

    private fun switchToMainActivity(){
        val duration = 2000L

        /*
        // Вариант с блокировкой экрана (использование одного потока)
        Thread.sleep(duration)
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        */

        // Вариант без блокировки блокировки (использование альтернативного потока)
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }, duration)
    }
}