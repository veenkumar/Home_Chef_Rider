package com.veen.homechefrider.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.veen.homechefrider.R
import com.veen.homechefrider.utils.AppUtils

class Splash : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val getsaveLogin = AppUtils.getsaveLogin(applicationContext)
        if (getsaveLogin!="") {
            Handler().postDelayed(
                {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, SPLASH_TIME_OUT
            )
        } else {
            Handler().postDelayed(
                {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }, SPLASH_TIME_OUT
            )
        }
    }
}