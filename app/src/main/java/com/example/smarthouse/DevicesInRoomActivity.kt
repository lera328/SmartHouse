package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthouse.Adapters.RecyclerAdapterForMyDevices
import com.example.smarthouse.Adapters.ResyslerAdapterForMyRooms
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.databinding.ActivityDevicesInRoomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

class DevicesInRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityDevicesInRoomBinding
    var idOfRoom: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevicesInRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        try {
            idOfRoom = intent.getIntExtra("id", 0)

            val dataBaseManager = DataBaseManager()
            GlobalScope.launch(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvDeviceName.setText("Устройства в " + dataBaseManager.getRoomName(idOfRoom))
                val items = dataBaseManager.getDevicesInRoom(idOfRoom)
                val adapter = RecyclerAdapterForMyDevices(items)
                val layoutManager = GridLayoutManager(this@DevicesInRoomActivity, 2)
                binding.recycler.layoutManager = layoutManager
                binding.recycler.adapter = adapter
                binding.progressBar.visibility = View.GONE
            }
        }catch (e: TimeoutException) {
                Toast.makeText(this,"Произошло исключение: ${e.message}",Toast.LENGTH_SHORT).show()
            }
        catch (e: Exception) {
            Toast.makeText(this,"Произошло исключение: ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickBtPlus(view: View) {
        val intent = Intent(this, ChooseDeviceActivity::class.java)
        intent.putExtra("roomId", idOfRoom)
        startActivity(intent)
    }

    fun onClickExit(view: View) {
        val intent = Intent(this, YourHouseActivity::class.java)
        startActivity(intent)
    }
}