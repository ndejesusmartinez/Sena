package com.projectSena.sena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.projectSena.sena.models.FetchContactsResponse
import com.projectSena.sena.models.ListServices
import com.projectSena.sena.models.ListUsers
import com.projectSena.sena.utils.LoadingDialog
import com.projectSena.sena.utils.mySharedPreference
import com.projectSena.sena.utils.utils
import java.nio.charset.Charset

class ServicesActivity : AppCompatActivity() {

    private lateinit var listViewServices: ListView
    private lateinit var adapter: ArrayAdapter<String>
    lateinit var sharedPreference: mySharedPreference
    private lateinit var loadingDialog: LoadingDialog
    private var services: ArrayList<ListServices> = ArrayList()
    lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        loadingDialog = LoadingDialog(this)
        listViewServices = findViewById(R.id.listViewServices)
        sharedPreference = mySharedPreference()
        obtenerRegistros()

        register = findViewById(R.id.register)
        register.setOnClickListener {
            utils.showAlert(this,"Registro de nuevo servicio", "Por aqui va")
        }
    }

    fun obtenerRegistros(): List<String> {
        loadingDialog.show()
        val queue = Volley.newRequestQueue(this)
        val url = sharedPreference.getAPIURL(this)+"/api/v1/allServices"
        val requestBody = ""

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val fetchContactsResponse: FetchContactsResponse =
                    Gson().fromJson(response, FetchContactsResponse::class.java)
                if (fetchContactsResponse.status == "success") {
                    loadingDialog.dismiss()
                    utils.showAlert(this,"Servicios", fetchContactsResponse.message)
                    services = fetchContactsResponse.services

                    val arrayList = ArrayList<String>()
                    val listView = findViewById<ListView>(R.id.listViewServices)
                    for (element in services) {
                        arrayList.add("Nombre del servicio: "+element.nameService+"\n"+element.createdAt)
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