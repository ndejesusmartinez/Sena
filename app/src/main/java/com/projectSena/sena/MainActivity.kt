package com.projectSena.sena

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.projectSena.sena.utils.mySharedPreference
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timer().schedule(3000){


            val accessToken: String = mySharedPreference().getAccessToken(applicationContext)
            if(accessToken.isEmpty()){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }else{
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            }

            finish()
        }
    }
}