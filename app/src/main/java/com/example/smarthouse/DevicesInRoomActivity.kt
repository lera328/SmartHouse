package com.example.smarthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smarthouse.databinding.ActivityDevicesInRoomBinding

class DevicesInRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityDevicesInRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevicesInRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        val nameOfRoom = intent.getStringExtra("name")
        binding.textView11.setText("Устройства в " + nameOfRoom)
    }

    fun onClickBtPlus(view: View) {}
}