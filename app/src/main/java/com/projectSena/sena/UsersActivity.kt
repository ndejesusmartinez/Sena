package com.projectSena.sena

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.projectSena.sena.models.FetchContactsResponse
import com.projectSena.sena.models.ListUsers
import com.projectSena.sena.utils.LoadingDialog
import com.projectSena.sena.utils.mySharedPreference
import com.projectSena.sena.utils.utils
import java.nio.charset.Charset

class UsersActivity : AppCompatActivity() {

    private lateinit var listViewUsuarios: ListView
    private lateinit var adapter: ArrayAdapter<String>
    lateinit var sharedPreference: mySharedPreference
    private var users: ArrayList<ListUsers> = ArrayList()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        loadingDialog = LoadingDialog(this)
        sharedPreference = mySharedPreference()
        listViewUsuarios = findViewById(R.id.listViewUsuarios)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        obtenerRegistros()
    }

    fun obtenerRegistros(): List<String> {
        loadingDialog.show()
        val queue = Volley.newRequestQueue(this)
        val url = sharedPreference.getAPIURL(this)+"/api/v1/allUsers"
        val requestBody = ""
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val fetchContactsResponse: FetchContactsResponse =
                    Gson().fromJson(response, FetchContactsResponse::class.java)
                if (fetchContactsResponse.status == "success") {
                    loadingDialog.dismiss()
                    utils.showAlert(this,"Usuarios", fetchContactsResponse.message)
                    users = fetchContactsResponse.users
                    val preference: mySharedPreference = mySharedPreference()
                    preference.setUsurious(this, users)

                    val arrayList = ArrayList<String>()
                    val listView = findViewById<ListView>(R.id.listViewUsuarios)
                    for (element in users) {
                        arrayList.add("Nombre: "+element.name+"\n"+"Rol: "+element.role+"\n"+"Estado: "+ element.status)
                    }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
                    listView.adapter = adapter
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


        val da = sharedPreference.getUsurious(this)

        val registros = listOf(da)
        return registros
    }


}