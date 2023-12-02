package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class TypesOfRoom(val type: String, val image: Int)

@Serializable
data class TypesOfRoomWithId(val id: Int, val type: String, val image: Int)
