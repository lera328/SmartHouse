package com.example.smarthouse.Tools

import android.content.Context

class SharedPreferansesManager {
    public fun saveEmailToSharedPreferences(context: Context, value: String?) {
        val sharedPreferences = context.getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", value)
        editor.apply()
    }

    public fun savePassToSharedPreferences(context: Context, value: String?) {
        val sharedPreferences = context.getSharedPreferences("pass", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("pass", value)
        editor.apply()
    }

    public fun getFromSharedPreferences(context: Context, key: String): String? {
        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }
}