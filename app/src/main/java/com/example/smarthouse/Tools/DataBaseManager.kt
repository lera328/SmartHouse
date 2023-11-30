package com.example.smarthouse.Tools

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.smarthouse.DB.DeviceIcon
import com.example.smarthouse.DB.DeviceIconWithId
import com.example.smarthouse.DB.Rooms
import com.example.smarthouse.DB.RoomsWithId
import com.example.smarthouse.DB.TypesOfRoom
import com.example.smarthouse.DB.TypesOfRoomWithId
import com.example.smarthouse.DB.User
import com.example.smarthouse.DB.device_parameters
import com.example.smarthouse.DB.device_parametersWithId
import com.example.smarthouse.DB.devices
import com.example.smarthouse.DB.devicesWithId
import com.example.smarthouse.DB.devices_icons_
import com.example.smarthouse.DB.devices_icons_withId
import com.example.smarthouse.DB.typesOfDevices
import com.example.smarthouse.DB.typesOfDevicesWithId
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

    public suspend fun getRoomTypeImage(id: Int): String {

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
            val user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
                updateSession = true
            )
            val room = Rooms(nameOfRoom = nameOfRoom, idOfType = idOfType, userID = user.id)
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

    public suspend fun getAllMyRooms(): List<RoomsWithId> {
        val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
            updateSession = true
        )
        val roomList = SupabaseManager.getSupabaseClient().postgrest["rooms"]
            .select(columns = Columns.list("id, nameOfRoom, idOfType,userID")) {
                eq("userID", get_user.id)
            }
            .decodeList<RoomsWithId>()
        return roomList
    }

    public suspend fun addNewDeviceType(
        activity: Activity,
        type: String,
        image1: String,
        image2: String,
        image3: String
    ) {
        try {

            val icons =
                DeviceIcon(large_image = image1, middle_image = image2, small_image = image3)
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

    public suspend fun addNewIcon(
        activity: Activity,
        image1: String,
        image2: String,
        image3: String
    ) {
        try {

            val icons = devices_icons_(white = image1, blue = image2, grey = image3)
            SupabaseManager.getSupabaseClient().postgrest["devices_icons_"].insert(
                icons,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("aaa", e.message.toString())
            }
        }
    }

    public suspend fun getDevicesInRoom(roomId_: Int): List<devicesWithId> {
        //val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
        //    updateSession = true
        //)
        val deviceList = SupabaseManager.getSupabaseClient().postgrest["devices"]
            .select(columns = Columns.list("id, name, typeId, icon, roomId")) {
                eq("roomId", roomId_)
            }
            .decodeList<devicesWithId>()
        return deviceList
    }

    public suspend fun getDevicesIcon(id: Int): devices_icons_withId {
        //val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
        //    updateSession = true
        //)
        val deviceList = SupabaseManager.getSupabaseClient().postgrest["devices_icons_"]
            .select(columns = Columns.list("id, white, blue, grey")) {
                eq("id", id)
            }
            .decodeSingle<devices_icons_withId>()
        return deviceList
    }

    public suspend fun getDevicesTypes(): List<typesOfDevicesWithId> {
        //val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
        //    updateSession = true
        //)
        val deviceList = SupabaseManager.getSupabaseClient().postgrest["typesOfDevices"]
            .select(columns = Columns.list("id, type, iconId"))
            .decodeList<typesOfDevicesWithId>()
        return deviceList
    }

    public suspend fun getDeviceType(id:Int): typesOfDevicesWithId {
        //val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
        //    updateSession = true
        //)
        val deviceType = SupabaseManager.getSupabaseClient().postgrest["typesOfDevices"]
            .select(columns = Columns.list("id, type, iconId")){
                eq("id", id)
            }
            .decodeSingle<typesOfDevicesWithId>()
        return deviceType
    }

    public suspend fun addNewDeviceToRoom(
        activity: Activity,
        name: String,
        iconId: Int,
        roomId: Int,
        typeId: Int
    ) {

        try {

            val device = devices(name = name, icon = iconId, roomId = roomId, typeId = typeId)
            SupabaseManager.getSupabaseClient().postgrest["devices"].insert(
                device,
                returning = Returning.REPRESENTATION
            )
            val device_get = SupabaseManager.getSupabaseClient().postgrest["devices"]
                .select(columns = Columns.list("id, name, icon, roomId, typeId")) {
                    eq("name", device.name)
                    eq("roomId", device.roomId)
                    eq("typeId", device.typeId)
                }.decodeSingle<devicesWithId>()

            var brightness: Int = -1
            var temperature: Int = -1
            var power: Int = -1
            when (typeId) {
                9 -> {
                    temperature = 0
                    power = 0
                }
                6 -> power = 0
                27 -> power = 0
                7 -> brightness = 0
                8 -> temperature = 0
            }
            val devicePar = device_parameters(
                device_id = device_get.id,
                brightness = brightness,
                temperature = temperature,
                power = power,
                status = false
            )

            SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
                devicePar,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("aaa", e.message.toString())
            }
        }
    }

    public suspend fun getRoomName(id: Int): String {
        //val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
        //    updateSession = true
        //)
        val room = SupabaseManager.getSupabaseClient().postgrest["rooms"]
            .select(columns = Columns.list("id, nameOfRoom, idOfType, userID")) {
                eq("id", id)
            }
            .decodeSingle<RoomsWithId>()
        return room.nameOfRoom
    }
     public suspend fun getDeviceParameters(id:Int):device_parametersWithId{
         val device_parameter = SupabaseManager.getSupabaseClient().postgrest["device_parameters"]
             .select(columns = Columns.list("id, device_id, brightness, temperature, power, status")) {
                 eq("device_id", id)

             }.decodeSingle<device_parametersWithId>()
         return  device_parameter
     }

    public suspend fun changeDeviceStatus(id:Int, status:Boolean): device_parameters {
        val params=getDeviceParameters(id)

        val devicePar = device_parameters(
            device_id = id,
            brightness = params.brightness,
            temperature = params.temperature,
            power = params.power,
            status = status
        )

        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
            devicePar,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].delete {
            eq("id", params.id)
        }
        return devicePar
    }

    public suspend fun changeDeviceParameters(id:Int, brightness:Int, temperature:Int, power:Int){
        val params=getDeviceParameters(id)

        val devicePar = device_parameters(
            device_id = id,
            brightness = brightness,
            temperature = temperature,
            power = power,
            status = params.status
        )

        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
            devicePar,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].delete {
            eq("id", params.id)
        }
    }

    public suspend fun changeDeviceParametersBrigh(id:Int, brightness:Int){
        val params=getDeviceParameters(id)

        val devicePar = device_parameters(
            device_id = id,
            brightness = brightness,
            temperature = params.temperature,
            power = params.power,
            status = params.status
        )

        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
            devicePar,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].delete {
            eq("id", params.id)
        }
    }

    public suspend fun changeDeviceParametersTemp(id:Int, temperature:Int){
        val params=getDeviceParameters(id)

        val devicePar = device_parameters(
            device_id = id,
            brightness = params.brightness,
            temperature = temperature,
            power = params.power,
            status = params.status
        )

        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
            devicePar,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].delete {
            eq("id", params.id)
        }
    }

    public suspend fun changeDeviceParametersPow(id:Int, power:Int){
        val params=getDeviceParameters(id)

        val devicePar = device_parameters(
            device_id = id,
            brightness = params.brightness,
            temperature = params.temperature,
            power = power,
            status = params.status
        )

        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
            devicePar,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].delete {
            eq("id", params.id)
        }
    }

    public suspend fun changeDeviceParametersTemPower(id:Int, temperature:Int, power:Int){
        val params=getDeviceParameters(id)

        val devicePar = device_parameters(
            device_id = id,
            brightness = params.brightness,
            temperature = temperature,
            power = power,
            status = params.status
        )

        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].insert(
            devicePar,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["device_parameters"].delete {
            eq("id", params.id)
        }
    }
}