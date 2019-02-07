package ru.jobni.jobni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // setTheme(R.style.AppTheme_Launcher)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        switchToMainActivity()

        //scheduleSplashScreen()
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
