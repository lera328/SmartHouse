package com.example.smarthouse.Tools

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smarthouse.DB.User
import com.example.smarthouse.DB.device_parameters
import com.example.smarthouse.DB.userInformation
import com.example.smarthouse.DB.userInformationWithId
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Returning

class UsersManager() {
    public suspend fun addNewUser(
        activity: Activity,
        email_: String,
        username: String,
        password_: String
    ) {
        try {

            val user_ = SupabaseManager.getSupabaseClient().gotrue.signUpWith(Email) {
                email = email_
                password = password_
            }
            SupabaseManager.getSupabaseClient().gotrue.loginWith(Email) {
                email = email_
                password = password_
            }
            val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
                updateSession = true
            )
            val user =
                User(id = get_user.id, email = email_, username = username, password = password_)

            SupabaseManager.getSupabaseClient().postgrest["user"].insert(
                user,
                returning = Returning.REPRESENTATION
            )

            val user_information =
                userInformation("", user.id, " ")

            SupabaseManager.getSupabaseClient().postgrest["userInformation"].insert(
                user_information,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            activity.runOnUiThread {
                Toast.makeText(
                    activity.applicationContext,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("aaa", e.message.toString())
            }
        }
    }

    public fun saveToSharedPreferences(context: Context, value: String, key: String) {
        val sharedPreferences =
            context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    public fun getFromSharedPreferences(context: Context, key: String): String? {
        val sharedPreferences =
            context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    public suspend fun Update() {
        SupabaseManager.getSupabaseClient().postgrest["users"].update(
            {
                set("username", "Australia")
            }
        ) {
            eq("username", "боря")
        }.decodeSingle<User>()
    }

    public suspend fun getUser(email_: String, password_: String): Boolean {
        try {
            val user = SupabaseManager.getSupabaseClient().postgrest["user"].select(
                columns = Columns.list("id,email,username, password")
            ) {
                eq("email", email_)
                eq("password", password_)
            }.decodeSingle<User>()

            if (user != null) {
                return true
            }
        } catch (e: NoSuchElementException) {
            return false
            //println("Ошибка: список пустой")
        } catch (e: Exception) {
            return false
            //println("Произошла ошибка: ${e.message}")
        }
        return false
    }

    public suspend fun changeUserAdress(address: String) {
        val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
            updateSession = true
        )

        val getInf = SupabaseManager.getSupabaseClient().postgrest["usersInformation"].select(
            columns = Columns.list("id,home_address, userId, image")
        ) {
            eq("userId", get_user.id)

        }.decodeSingle<userInformationWithId>()
        val userInformation = userInformation(address, get_user.id, getInf.image)
        SupabaseManager.getSupabaseClient().postgrest["usersInformation"].insert(
            userInformation,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["usersInformation"].delete {
            eq("id", getInf.id)
        }
    }

    public suspend fun changeUserImage(image: String) {
        val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
            updateSession = true
        )

        val getInf = SupabaseManager.getSupabaseClient().postgrest["usersInformation"].select(
            columns = Columns.list("id,home_address, userId, image")
        ) {
            eq("userId", get_user.id)

        }.decodeSingle<userInformationWithId>()
        val userInformation = userInformation(getInf.home_address, get_user.id, image)
        SupabaseManager.getSupabaseClient().postgrest["usersInformation"].insert(
            userInformation,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["usersInformation"].delete {
            eq("id", getInf.id)
        }
    }

    public suspend fun changeUserInformation(
        home_address: String,
        username: String,
        email: String
    ) {
        val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
            updateSession = true
        )

        val getInf = SupabaseManager.getSupabaseClient().postgrest["usersInformation"].select(
            columns = Columns.list("id,home_address, userId, image")
        ) {
            eq("userId", get_user.id)

        }.decodeSingle<userInformationWithId>()
        val userInformation = userInformation(home_address, get_user.id, getInf.image)
        SupabaseManager.getSupabaseClient().postgrest["usersInformation"].insert(
            userInformation,
            returning = Returning.REPRESENTATION
        )
        SupabaseManager.getSupabaseClient().postgrest["usersInformation"].delete {
            eq("id", getInf.id)
        }
        SupabaseManager.getSupabaseClient().postgrest["user"].update(
            {
                set("email", email)
                set("username", username)
            }
        ) {
            eq("id", get_user.id)
        }
    }

    public suspend fun getUserInformation(): userInformationWithId {
        val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
            updateSession = true
        )

        val getInf = SupabaseManager.getSupabaseClient().postgrest["usersInformation"].select(
            columns = Columns.list("id,home_address, userId, image")
        ) {
            eq("userId", get_user.id)

        }.decodeSingle<userInformationWithId>()
        return getInf
    }

    public suspend fun getUser(): User {
        val get_user = SupabaseManager.getSupabaseClient().gotrue.retrieveUserForCurrentSession(
            updateSession = true
        )
        val user = SupabaseManager.getSupabaseClient().postgrest["user"].select(
            columns = Columns.list("id,email,username, password")
        ) {
            eq("id", get_user.id)
        }.decodeSingle<User>()
        return user
    }


}