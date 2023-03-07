package com.example.todo_list_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val i = Intent(this@SplashActivity, MainActivity::class.java)
        Handler().postDelayed(Runnable {
            startActivity(i)
            finish()
        }, 2000)
    }
}