package com.adnantech.chatapp_free_version.models

import com.projectSena.sena.models.User

class GeneralResponse {
    lateinit var status: String
    lateinit var message: String
    lateinit var users: ArrayList<User>
}