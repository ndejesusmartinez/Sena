package com.projectSena.sena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.adnantech.chatapp_free_version.models.GeneralResponse
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

class HomeActivityDelivery : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    lateinit var sharedPreference: mySharedPreference
    lateinit var getUserResponse: GetUserResponse
    lateinit var user: User

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_delivery)

        loadingDialog = LoadingDialog(this)

        sharedPreference = mySharedPreference()

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)
    }

    fun doLogout(){
        val queue = Volley.newRequestQueue(this)
        val url = sharedPreference.getAPIURL(this) + "/api/v1/logout"
        val requestBody = ""
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            val generalResponse: GeneralResponse = Gson().fromJson(response, GeneralResponse::class.java)
            utils.showAlert(this, "Logout", generalResponse.message, Runnable {
                if (generalResponse.status == "success"){
                    Log.i("myLog", "OK = "+ generalResponse.status)
                    sharedPreference.removeAccessToken(this,)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            })
        }, Response.ErrorListener { error ->
            Log.i("myLog", "error = " + error)
            utils.showAlert(this,"Error: "+ error)

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Authorization"] = sharedPreference.getAccessToken(applicationContext)
                return headers
            }
            override fun getBody(): ByteArray {
                return requestBody.toByteArray(Charset.defaultCharset())
            }
        }
        queue.add(stringRequest)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_logout){
            doLogout()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}