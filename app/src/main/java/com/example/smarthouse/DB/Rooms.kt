package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class Rooms (val nameOfRoom:String, val idOfType:Int, val userId:String)
