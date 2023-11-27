package com.example.smarthouse.Tools

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smarthouse.DB.User
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
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
}