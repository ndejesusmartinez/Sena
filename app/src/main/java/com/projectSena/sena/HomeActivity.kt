package com.projectSena.sena

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.projectSena.sena.models.GetUserResponse
import com.projectSena.sena.models.User
import com.projectSena.sena.models.getUserModel
import com.projectSena.sena.utils.LoadingDialog
import com.projectSena.sena.utils.mySharedPreference
import com.projectSena.sena.utils.utils
import org.json.JSONArray
import java.nio.charset.Charset

class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    lateinit var sharedPreference: mySharedPreference
    lateinit var getUserResponse: GetUserResponse
    lateinit var user: User

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

        getUser()
    }

    fun getData(){
        val queue = Volley.newRequestQueue(this)
        //val url: String = utils.apiUrl + "/api/v1/getUser"
        val url = sharedPreference.getAPIURL(this)+"/api/v1/getUser"
        val requestBody = ""
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val getUserModel: getUserModel = Gson().fromJson(response, getUserModel::class.java)
                Log.i("mylog", "E:" + getUserModel)
                if (getUserModel.status == "success"){
                    Log.i("mylog", "status ok:" + getUserModel.status)
                    /*user = getUserModel.user
                    val headerView: View = navigationView.getHeaderView(0)
                    val name: TextView = headerView.findViewById(R.id.name)
                    name.text = user.name

                    getContactsPermission()*/
                }else{
                    utils.showAlert(this, "Error", getUserModel.message)
                }
            },
            Response.ErrorListener { error ->
                Log.i("mylog", "ErrorListener2: "+error.toString())
            }
        ){
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

    fun getUser() {
        loadingDialog.show()

        val queue = Volley.newRequestQueue(this)
        val url = sharedPreference.getAPIURL(this) + "/api/v1/getUser"

        val requestBody = ""
        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    getUserResponse = Gson().fromJson(response, GetUserResponse::class.java)
                    if (getUserResponse.status == "success") {
                        loadingDialog.dismiss()
                        utils.showAlert(this, "Ingreso correctamente", "Usuario: "+getUserResponse.user.name)
                    } else {
                        loadingDialog.dismiss()
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