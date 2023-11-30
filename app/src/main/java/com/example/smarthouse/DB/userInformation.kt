package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class userInformation(val home_address:String, val userId:String, val image: String?)
@Serializable
data class userInformationWithId(val id:Int, val home_address:String, val userId:String, val image:String)
