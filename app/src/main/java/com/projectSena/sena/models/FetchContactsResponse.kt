package com.projectSena.sena.models

class FetchContactsResponse {
    lateinit var status: String
    lateinit var message: String
    lateinit var users: ArrayList<ListUsers>
    lateinit var services: ArrayList<ListServices>
}