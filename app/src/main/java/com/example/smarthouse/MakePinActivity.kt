package com.example.smarthouse

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.smarthouse.Tools.PasswordManager
import com.example.smarthouse.Tools.SharedPreferansesManager
import com.example.smarthouse.databinding.ActivityAuthorizationBinding
import com.example.smarthouse.databinding.ActivityMakePinBinding

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
            1 -> binding.circle1.setBackgroundColor(Color.WHITE)
            2 -> binding.circle2.setBackgroundColor(Color.WHITE)
            3 -> binding.circle3.setBackgroundColor(Color.WHITE)
            4 -> {
                binding.circle4.setBackgroundColor(Color.WHITE)
                val intent = Intent(this, AddAddressActivity::class.java)
                val manager: PasswordManager = PasswordManager()
                val pin = manager.getFromSharedPreferences(this)
                if (pin != null) {
                    if (pin == pin_kod)
                        startActivity(intent)
                } else {
                    manager.saveToSharedPreferences(this, pin_kod)
                    startActivity(intent)
                }
            }
        }
    }

    fun onClickExit(view: View) {
        val manager: PasswordManager = PasswordManager()
        manager.saveToSharedPreferences(this,null)
        val sharedPreferansesManager=SharedPreferansesManager()
        sharedPreferansesManager.saveEmailToSharedPreferences(this, null)
        sharedPreferansesManager.savePassToSharedPreferences(this, null)
        val intent=Intent(this, ActivityAuthorizationBinding::class.java)
    }
}