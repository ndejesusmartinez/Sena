package com.projectSena.sena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.projectSena.sena.models.GetUserResponse
import com.projectSena.sena.models.User
import com.projectSena.sena.utils.LoadingDialog
import com.projectSena.sena.utils.mySharedPreference
import com.projectSena.sena.utils.utils
import java.nio.charset.Charset

class perfileActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    lateinit var sharedPreference: mySharedPreference
    lateinit var getUserResponse: GetUserResponse
    lateinit var user: User

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfile)

        loadingDialog = LoadingDialog(this)
        sharedPreference = mySharedPreference()
        getData()
    }

    fun getData() {
        loadingDialog.show()
        val queue = Volley.newRequestQueue(this)
        val url = sharedPreference.getAPIURL(this) + "/api/v1/getUser"

        val Username = findViewById<TextView>(R.id.Username)
        val email = findViewById<TextView>(R.id.email)
        val role = findViewById<TextView>(R.id.Role)
        val status = findViewById<TextView>(R.id.status)

        val requestBody = ""
        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    getUserResponse = Gson().fromJson(response, GetUserResponse::class.java)
                    if (getUserResponse.status == "success") {
                        loadingDialog.dismiss()
                        Username.text = "Nombre de usuario: "+getUserResponse.user.name
                        email.text = "Correo: "+getUserResponse.user.email
                        role.text = "Rol: " + getUserResponse.user.role
                        status.text = "Estado del usuario: "+getUserResponse.user.status

                    } else {

                        utils.showAlert(this, "Error", getUserResponse.message)
                    }
                },
                Response.ErrorListener { error ->
                    Log.i("myLog", "error = " + error)
                    utils.showAlert(this, "ErrorListener", error.toString())
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] =
                        sharedPreference.getAccessToken(applicationContext)
                    //Log.i("myLog", "headers:= " + headers)
                    return headers
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)

    }
    /*override fun onBackPressed() {
        val intent: Intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }*/
}