package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.example.smarthouse.Tools.PasswordManager
import com.example.smarthouse.Tools.SharedPreferansesManager
import com.example.smarthouse.Tools.SupabaseManager
import com.example.smarthouse.Tools.UsersManager
import com.example.smarthouse.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    val userManager = UsersManager()
    val client = SupabaseManager.getSupabaseClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    fun onClickRegistration(view: View) {
        val username = binding.etUserName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val passwordManager = PasswordManager()
        val emailPattern = "[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}".toRegex()
        if (password.isEmpty() || email.isEmpty() || !email.matches(emailPattern)) {
            Toast.makeText(this, "Ошибка! Проверьте введенные данные", Toast.LENGTH_SHORT).show()
        } else {
            GlobalScope.launch {
                userManager.addNewUser(
                    this@RegistrationActivity,
                    email,
                    username,
                    password
                )
                val sharedPreferansesManager = SharedPreferansesManager()
                sharedPreferansesManager.saveEmailToSharedPreferences(
                    this@RegistrationActivity,
                    email
                )
                sharedPreferansesManager.savePassToSharedPreferences(
                    this@RegistrationActivity,
                    password
                )
            }
            val intent = Intent(this, MakePinActivity::class.java)
            startActivity(intent)
        }
    }

    fun onClickLogin(view: View) {
        val intent: Intent = Intent(this, AuthorizationActivity::class.java)
        startActivity(intent)
        finish()
    }
}