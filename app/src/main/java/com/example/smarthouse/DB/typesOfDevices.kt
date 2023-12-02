package com.example.smarthouse.DB

import kotlinx.serialization.Serializable

@Serializable
data class typesOfDevices(val type: String, val iconId: Int)

@Serializable
data class typesOfDevicesWithId(val id: Int, val type: String, val iconId: Int)

@Serializable
data class DeviceIcon(val large_image: String, val middle_image: String, val small_image: String)

@Serializable
data class DeviceIconWithId(
    val id: Int,
    val large_image: String,
    val middle_image: String,
    val small_image: String
)
