package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class devices_icons_ (val white:String, val blue:String, val grey:String)
@Serializable
data class devices_icons_withId (val id:Int, val white:String, val blue:String, val grey:String)
@Serializable
data class room_icons ( val blue:String, val grey: String, val white: String)
@Serializable
data class room_iconsWithId (val id:Int, val blue:String, val grey: String, val white: String)