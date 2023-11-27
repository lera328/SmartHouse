package com.example.smarthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthouse.databinding.ActivityChooseDeviceBinding

class ChooseDeviceActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChooseDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}