package com.example.smarthouse

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.smarthouse.DB.TypesOfRoom
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.Tools.SharedPreferansesManager
import com.example.smarthouse.Tools.SupabaseManager
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
        val sharedPreferansesManager = SharedPreferansesManager()
        val email_=sharedPreferansesManager.getFromSharedPreferences(this,"email")
        val pass=sharedPreferansesManager.getFromSharedPreferences(this, "pass")
        if (email_ != null && pass!=null) {
            GlobalScope.launch(Dispatchers.Main) {
                SupabaseManager.getSupabaseClient().gotrue.loginWith(Email) {
                    email = email_
                    password = pass
                }
                val intent=Intent(this@AuthorizationActivity, MakePinActivity::class.java)
                startActivity(intent)
            }
        }

//картинки так будем брать
        //GlobalScope.launch(Dispatchers.Main) {
        //    val dataBaseManager = DataBaseManager()
        //    val imageManager = ImageManager()
        //    val stringImage = withContext(Dispatchers.IO) {
        //        dataBaseManager.getRoomType()
        //    }
        //    val retrievedImageFile = File(this@AuthorizationActivity.filesDir, "image.jpg")
        //    imageManager.byteArrayToImage(stringImage, retrievedImageFile)
        //    val bitmap = BitmapFactory.decodeFile(retrievedImageFile.absolutePath)
        //    binding.imageView.setImageBitmap(bitmap)
        //}
    }

    fun onClickLogin(view: View) {
        val intent: Intent = Intent(this, MakePinActivity::class.java)
        startActivity(intent)
    }

    fun onClickRegistration(view: View) {
        val intent: Intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}