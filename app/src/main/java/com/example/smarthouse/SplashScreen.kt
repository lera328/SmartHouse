package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // время показа заставки в миллисекундах
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Создаем новый объект Handler
        Handler().postDelayed({
            // Этот метод будет вызван после завершения времени показа заставки
            // Здесь вы можете перейти к основной активности вашего приложения или выполнить другие действия
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}