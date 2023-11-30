package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class devices (val name:String, val typeId:Int, val icon:Int, val roomId:Int )
@Serializable
data class devicesWithId (val id:Int, val name:String, val typeId:Int, val icon:Int, val roomId:Int )
@Serializable
data class device_parameters ( val device_id:Int, val brightness:Int, val temperature:Int, val power:Int, val status:Boolean )
@Serializable
data class device_parametersWithId (val id:Int, val device_id:Int, val brightness:Int, val temperature:Int, val power:Int, val status:Boolean )