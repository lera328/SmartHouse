package com.example.smarthouse.Tools

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.smarthouse.DB.DeviceIcon
import com.example.smarthouse.DB.DeviceIconWithId
import com.example.smarthouse.DB.Rooms
import com.example.smarthouse.DB.TypesOfRoom
import com.example.smarthouse.DB.TypesOfRoomWithId
import com.example.smarthouse.DB.User
import com.example.smarthouse.DB.typesOfDevices
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Returning

class DataBaseManager {
    public suspend fun addNewRoomType(
        activity: Activity,
        type: String,
        image: String
    ) {
        try {
            val type = TypesOfRoom(type = type, image = image)
            SupabaseManager.getSupabaseClient().postgrest["typesOfRoom"].insert(
                type,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("aaa", e.message.toString())
            }
        }
    }

    public suspend fun getRoomTypeImage(id:Int): String {

        val type = SupabaseManager.getSupabaseClient().postgrest["typesOfRoom"].select(
            columns = Columns.list("type, image")
        ) {
            eq("id", id)
        }.decodeSingle<TypesOfRoom>()
        return type.image

    }
    public suspend fun getRoomTypeId(type: String): Int {

        val type = SupabaseManager.getSupabaseClient().postgrest["typesOfRoom"].select(
            columns = Columns.list("id, type, image")
        ) {
            eq("type", type)
        }.decodeSingle<TypesOfRoomWithId>()
        return type.id

    }

    public suspend fun getAllRoomTypes(): List<TypesOfRoom> {
        val typesList = SupabaseManager.getSupabaseClient().postgrest["typesOfRoom"]
            .select(columns = Columns.list("type, image"))
            .decodeList<TypesOfRoom>()
        return typesList
    }

    public suspend fun addNewRoom(
        activity: Activity,
        nameOfRoom: String,
        idOfType: Int
    ) {
        try {
            val user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(updateSession = true)
            val room = Rooms( nameOfRoom=nameOfRoom, idOfType=idOfType, userId = user.id)
            SupabaseManager.getSupabaseClient().postgrest["rooms"].insert(
                room,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("aaa", e.message.toString())
            }
        }
    }

    public suspend fun getAllMyRooms(): List<Rooms> {
        val user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(updateSession = true)
        val roomList = SupabaseManager.getSupabaseClient().postgrest["rooms"]
            .select(columns = Columns.list("nameOfRoom, idOfType")){
                eq("userID", user.id)
            }
            .decodeList<Rooms>()
        return roomList
    }

    public suspend fun addNewDeviceType(
        activity: Activity,
        type:String,
        image1: String,
        image2: String,
        image3: String
    ) {
        try {

            val icons=DeviceIcon(large_image = image1, middle_image = image2, small_image = image3)
            SupabaseManager.getSupabaseClient().postgrest["devices_icons"].insert(
                icons,
                returning = Returning.REPRESENTATION
            )

            val iconId = SupabaseManager.getSupabaseClient().postgrest["devices_icons"].select(
                columns = Columns.list("id, large_image, middle_image, small_image")
            ) {
                eq("large_image", image1)
            }.decodeSingle<DeviceIconWithId>()


            val type = typesOfDevices(type = type, iconId = iconId.id)
            SupabaseManager.getSupabaseClient().postgrest["typesOfDevices"].insert(
                type,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("aaa", e.message.toString())
            }
        }
    }


}