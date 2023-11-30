package com.example.smarthouse

import android.content.Intent
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.example.smarthouse.DB.device_parametersWithId
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.databinding.ActivityRefactDeviceParametrBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class RefactDeviceParametrActivity : AppCompatActivity() {
    lateinit var binding: ActivityRefactDeviceParametrBinding
    var deviceTypeId: Int = 0
    private var deviceId = 0
    val dataBaseManager = DataBaseManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRefactDeviceParametrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deviceId = intent.getIntExtra("id", 0)
        val deviceName = intent.getStringExtra("name")
        deviceTypeId = intent.getIntExtra("type", 0)
        var f = true
        val dataBaseManager = DataBaseManager()
        binding.tvDeviceName.setText(deviceName)
        binding.seekBarStandart.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var text = ""
                when (deviceTypeId) {
                    9 -> text = "Температура ${progress.toString()} С"
                    6 -> text = "Мощность ${progress.toString()} %"
                    27 -> text = "Мощность ${progress.toString()} %"
                    7 -> text = "Яркость ${progress.toString()} %"
                    8 -> text = "Температура ${progress.toString()} С"
                }
                binding.tvStandart.setText(text)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding.seekBarMaybe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvMaybe.setText("Мощность ${progress.toString()} %")

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val params = dataBaseManager.getDeviceParameters(deviceId)
            val deviceType = dataBaseManager.getDeviceType(deviceTypeId)

            val retImageFile = File(this@RefactDeviceParametrActivity.filesDir, "image.jpg")
            val imageManager = ImageManager()
            val icons = dataBaseManager.getDevicesIcon(deviceType.iconId)
            val image = icons.blue
            imageManager.byteArrayToImage(image, retImageFile)
            val bitmap = BitmapFactory.decodeFile(retImageFile.absolutePath)
            binding.imageView5.setImageBitmap(bitmap)
            binding.textView13.setText(deviceType.type)
            if (params.status) {
                binding.switch1.isChecked = true
            } else {
                binding.tvStandart.visibility = View.GONE
                binding.tvMaybe.visibility = View.GONE
                binding.seekBarMaybe.visibility = View.GONE
                binding.seekBarStandart.visibility = View.GONE
            }

            if (params.temperature >= 0) {
                binding.seekBarStandart.max = 35
                binding.seekBarStandart.progress = params.temperature
                binding.tvStandart.setText("Температура ${params.temperature} С")
                f = false
            }
            if (params.brightness >= 0) {
                binding.seekBarStandart.max = 100
                binding.seekBarStandart.progress = params.brightness
                binding.tvStandart.setText("Яркость ${params.brightness} %")
            }
            if (params.power >= 0) {
                if (f) {
                    binding.seekBarStandart.max = 100
                    binding.seekBarStandart.progress = params.power
                    binding.tvStandart.setText("Мощность ${params.power} %")
                } else {
                    binding.seekBarMaybe.max = 100
                    binding.seekBarMaybe.progress = params.power
                    binding.tvMaybe.setText("Мощность ${params.power} %")
                }
            }
            binding.progressBar.visibility = View.GONE
        }

        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            GlobalScope.launch(Dispatchers.Main) {
                val params = dataBaseManager.changeDeviceStatus(deviceId, isChecked)

                if (isChecked) {
                    binding.tvStandart.visibility = View.VISIBLE
                    binding.seekBarStandart.visibility = View.VISIBLE
                    if (params.power >= 0 && params.temperature >= 0) {
                        binding.tvMaybe.visibility = View.VISIBLE
                        binding.seekBarMaybe.visibility = View.VISIBLE
                    }
                } else {
                    binding.tvStandart.visibility = View.GONE
                    binding.tvMaybe.visibility = View.GONE
                    binding.seekBarMaybe.visibility = View.GONE
                    binding.seekBarStandart.visibility = View.GONE
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveDataToDatabase()
    }

    private fun saveDataToDatabase() {
        val dataBaseManager = DataBaseManager()
        var brighnes = -1
        var temperature = -1
        var power = -1
        when (deviceTypeId) {
            9 -> {
                temperature = binding.seekBarStandart.progress
                power = binding.seekBarMaybe.progress
            }

            6 -> {
                power = binding.seekBarStandart.progress
            }

            27 -> power = binding.seekBarStandart.progress
            7 -> brighnes = binding.seekBarStandart.progress
            8 -> temperature = binding.seekBarStandart.progress
        }
        GlobalScope.launch(Dispatchers.Main) {
            dataBaseManager.changeDeviceParameters(deviceId, brighnes, temperature, power)
        }
    }

    fun onClickEx(view: View) {
        val intent = Intent(this@RefactDeviceParametrActivity, DevicesInRoomActivity::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            val id = dataBaseManager.getRoomIdFromDeviceId(deviceId)
            intent.putExtra("id", id)
            startActivity(intent)
            finish()
        }


    }
}
