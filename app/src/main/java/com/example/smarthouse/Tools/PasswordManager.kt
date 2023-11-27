package com.example.smarthouse.Tools

import android.content.Context

public class PasswordManager {
    public fun saveToSharedPreferences(context: Context, value: String?) {
        val sharedPreferences = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("pin", value)
        editor.apply()
    }
    public fun getFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("pin", null)
    }
    public fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
        return emailRegex.matches(email)
    }

   public fun isValidPassword(password: String): Boolean {
        // Реализация проверки пароля по вашим требованиям
        // Например, проверка на длину, наличие цифр, букв и специальных символов
        return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isLetter() }
    }

    public fun isValidUsername(username: String): Boolean {
        return username.length > 3
    }
}