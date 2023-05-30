package com.projectSena.sena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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

class RegisterActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var Password: EditText
    lateinit var email: EditText
    lateinit var registrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.Username)
        Password = findViewById(R.id.Password)
        email = findViewById(R.id.email)
        registrar = findViewById(R.id.registrar)

        registrar.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = utils.apiUrl + "/api/v1/addUser"

            val requestBody: String = "email=" + email.text + "&Password=" + Password.text + "&name=" + name.text

            val stringRequest: StringRequest = object: StringRequest(Method.POST, url, Response.Listener { response->
                Log.i("myLog", response)
                val LoginModel: LoginModel = Gson().fromJson(response, LoginModel::class.java)
                if (LoginModel.status == "success"){
                    utils.showAlert(this,"Usuario creado Ccorrectamente","Inicia sesion con tus datos de registro")
                    val accessToken: String = mySharedPreference().getAccessToken(applicationContext)
                    if(accessToken.isEmpty()){
                        utils.showAlert(this,"Usuario creado Ccorrectamente","Inicia sesion con tus datos de registro")
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                    }else{
                        utils.showAlert(this,"Usuario creado Ccorrectamente")
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    }
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