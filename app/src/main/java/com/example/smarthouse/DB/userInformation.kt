package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class userInformation(val userId:Int, val home_address:String)
