package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class User(val id:String, val email: String, val username:String,
    val password:String)

