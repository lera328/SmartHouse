package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.view.View
import com.example.smarthouse.Tools.UsersManager
import com.example.smarthouse.databinding.ActivityAddAddressBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddAddressActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddAddressBinding
    val userManager = UsersManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility=View.VISIBLE
            if (userManager.getUserInformation().home_address.length > 1) {
                val intent = Intent(this@AddAddressActivity, YourHouseActivity::class.java)
                startActivity(intent)
                this@AddAddressActivity.finish()
            }
            binding.progressBar.visibility=View.GONE
        }
    }

    fun onClick(view: View) {
        if (binding.editTextText.text.length > 1) {
            GlobalScope.launch(Dispatchers.Main) {
                userManager.changeUserAdress(binding.editTextText.text.toString())
                val intent = Intent(this@AddAddressActivity, YourHouseActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
}