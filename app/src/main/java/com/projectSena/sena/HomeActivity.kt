package com.projectSena.sena

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adnantech.chatapp_free_version.models.GeneralResponse
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.projectSena.sena.models.User
import com.projectSena.sena.models.getUserModel
import com.projectSena.sena.utils.mySharedPreference
import com.projectSena.sena.utils.utils
import org.json.JSONArray

class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    lateinit var sharedPreference: mySharedPreference
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreference = mySharedPreference()

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
        R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        getData()
    }

    fun getData(){
        val queue = Volley.newRequestQueue(this)
        val url = utils.apiUrl + "getUser"

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val getUserModel: getUserModel = Gson().fromJson(response, getUserModel::class.java)
                if (getUserModel.status == "success"){
                    user = getUserModel.user
                    val headerView: View = navigationView.getHeaderView(0)
                    val name: TextView = headerView.findViewById(R.id.name)
                    name.text = user.name

                    getContactsPermission()
                }else{
                    utils.showAlert(this, "Error", getUserModel.message)
                }
            },
            Response.ErrorListener { error ->
                
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer" + sharedPreference.getAccessToken(applicationContext)
                return headers
            }
        }
        queue.add(stringRequest)
    }

    fun getContactsPermission(){
        val permission = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.READ_CONTACTS)
        if (permission == PackageManager.PERMISSION_GRANTED){
            getContacts()
        }else{
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_CONTACTS),
                565)
        }
     }

    fun getContacts(){
        val contacts: JSONArray = JSONArray()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 565){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                //
            }else{
                getContacts()
            }
        }
    }

    fun doLogout(){
        val queue = Volley.newRequestQueue(this)
        val url = utils.apiUrl + "/logout"

        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            val generalResponse: GeneralResponse = Gson().fromJson(response, GeneralResponse::class.java)
            utils.showAlert(this, "Logout", generalResponse.message, Runnable {
                if (generalResponse.status == "success"){
                    sharedPreference.removeAccessToken(this,)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            })
        }, Response.ErrorListener { error ->  

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer" +sharedPreference.getAccessToken(applicationContext)
                return headers
            }
        }
        queue.add(stringRequest)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
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