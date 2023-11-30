package com.example.smarthouse

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.smarthouse.Tools.PasswordManager
import com.example.smarthouse.Tools.SharedPreferansesManager
import com.example.smarthouse.Tools.SupabaseManager
import com.example.smarthouse.Tools.UsersManager
import com.example.smarthouse.databinding.ActivityAuthorizationBinding
import com.example.smarthouse.databinding.ActivityMakePinBinding
import io.github.jan.supabase.gotrue.gotrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MakePinActivity : AppCompatActivity() {
    var pin_kod: String = ""
    lateinit var binding: ActivityMakePinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakePinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val manager: PasswordManager = PasswordManager()
        val pin = manager.getFromSharedPreferences(this)
        if (pin != null) {
            binding.btGet.visibility = View.VISIBLE
            binding.tvText.setText("")
        }
    }

    fun onClick(view: View) {
        val buttonText = (view as Button).text.toString()
        pin_kod += buttonText
        when (pin_kod.length) {
            1 -> binding.circle1.setBackgroundResource(R.drawable.circle_background2)
            2 -> binding.circle2.setBackgroundResource(R.drawable.circle_background2)
            3 -> binding.circle3.setBackgroundResource(R.drawable.circle_background2)
            4 -> {
                binding.circle4.setBackgroundResource(R.drawable.circle_background2)
                val userManager = UsersManager()


                val manager: PasswordManager = PasswordManager()
                val pin = manager.getFromSharedPreferences(this)
                if (pin != null) {
                    if (pin == pin_kod) {
                        GlobalScope.launch(Dispatchers.Main) {
                            binding.progressBar.visibility = View.VISIBLE
                            var intent = Intent()
                            if (userManager.getUserInformation().home_address.length > 1) {
                                intent = Intent(this@MakePinActivity, YourHouseActivity::class.java)
                            } else intent =
                                Intent(this@MakePinActivity, AddAddressActivity::class.java)
                            binding.progressBar.visibility = View.GONE
                            startActivity(intent)
                        }
                    } else {
                        pin_kod = ""
                        binding.circle1.setBackgroundResource(R.drawable.circle_background)
                        binding.circle2.setBackgroundResource(R.drawable.circle_background)
                        binding.circle3.setBackgroundResource(R.drawable.circle_background)
                        binding.circle4.setBackgroundResource(R.drawable.circle_background)
                    }

                } else {

                    manager.saveToSharedPreferences(this, pin_kod)
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun onClickExit(view: View) {
        val manager: PasswordManager = PasswordManager()
        manager.saveToSharedPreferences(this, null)
        val sharedPreferansesManager = SharedPreferansesManager()
        sharedPreferansesManager.saveEmailToSharedPreferences(this, null)
        sharedPreferansesManager.savePassToSharedPreferences(this, null)
        GlobalScope.launch {
            val intent = Intent(this@MakePinActivity, AuthorizationActivity::class.java)
            startActivity(intent)
            SupabaseManager.getSupabaseClient().gotrue.logout()
            finish()
        }
    }
}