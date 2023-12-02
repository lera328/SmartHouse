package com.example.smarthouse.Tools

import android.content.Context

public class PasswordManager {
    public fun saveToSharedPreferences(context: Context, value: String?) {
        val sharedPreferences =
            context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("pin", value)
        editor.apply()
    }
    public fun getFromSharedPreferences(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("pin", null)
    }

}