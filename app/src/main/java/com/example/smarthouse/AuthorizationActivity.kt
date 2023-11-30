package com.example.smarthouse

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.smarthouse.DB.TypesOfRoom
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.Tools.SharedPreferansesManager
import com.example.smarthouse.Tools.SupabaseManager
import com.example.smarthouse.Tools.UsersManager
import com.example.smarthouse.databinding.ActivityAddAddressBinding
import com.example.smarthouse.databinding.ActivityAuthorizationBinding
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class AuthorizationActivity : AppCompatActivity() {
    var client = SupabaseManager.getSupabaseClient()
    lateinit var binding: ActivityAuthorizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    fun onClickLogin(view: View) {
        val email_ = binding.etEmail.text.toString()
        val password_ = binding.etPassword.text.toString()
        val usersManager = UsersManager()

        val emailPattern = "[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}".toRegex()
        if (password_.isEmpty() || email_.isEmpty() || !email_.matches(emailPattern)) {
            Toast.makeText(this, "Ошибка! Проверьте введенные данные", Toast.LENGTH_SHORT).show()
        } else {
            GlobalScope.launch {

                if (usersManager.getUser(email_, password_)) {
                    SupabaseManager.getSupabaseClient().gotrue.loginWith(Email) {
                        email = email_
                        password = password_
                    }
                    val sharedPreferansesManager = SharedPreferansesManager()
                    sharedPreferansesManager.saveEmailToSharedPreferences(
                        this@AuthorizationActivity,
                        email_
                    )
                    sharedPreferansesManager.savePassToSharedPreferences(
                        this@AuthorizationActivity,
                        password_
                    )
                    val intent: Intent =
                        Intent(this@AuthorizationActivity, MakePinActivity::class.java)
                    startActivity(intent)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@AuthorizationActivity,
                            "Неправильный логин или пароль",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun onClickRegistration(view: View) {
        val intent: Intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

}