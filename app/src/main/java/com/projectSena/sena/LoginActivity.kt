package com.projectSena.sena

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.projectSena.sena.models.LoginModel
import com.projectSena.sena.utils.mySharedPreference
import com.projectSena.sena.utils.utils
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {
    lateinit var Username: EditText
    lateinit var Password: EditText
    lateinit var email: EditText
    lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Username = findViewById(R.id.Username)

        Password = findViewById(R.id.Password)
        email = findViewById(R.id.email)
        login = findViewById(R.id.login)
        login.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = utils.apiUrl + "/login"

            val requestBody: String = "email=" + email.text + "&Password=" + Password.text

            val stringRequest: StringRequest = object: StringRequest(Method.POST, url, Response.Listener { response->
                Log.i("myLog", response)
                val LoginModel: LoginModel = Gson().fromJson(response,LoginModel::class.java)
                if (LoginModel.status == "success"){
                    val preference: mySharedPreference = mySharedPreference()
                    preference.setAccessToken(this, LoginModel.accessToken)
                    startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                }else{
                    utils.showAlert(this,"Error", LoginModel.message)
                }
            }, Response.ErrorListener { error ->  
                Log.i("mylog", error.toString())
            }){
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
            queue.add(stringRequest)
        }
    }
}