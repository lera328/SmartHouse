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

class DevicesInRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityDevicesInRoomBinding
    var idOfRoom:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevicesInRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        val nameOfRoom = intent.getStringExtra("name")
        //idOfRoom= intent.getIntExtra("id",0)
        binding.tvDeviceName.setText("Устройства в " + nameOfRoom)
    }

    override fun onResume() {
        super.onResume()
        idOfRoom=intent.getIntExtra("id",0)

        val dataBaseManager= DataBaseManager()
        GlobalScope.launch(Dispatchers.Main) {
            binding.tvDeviceName.setText(dataBaseManager.getRoomName(idOfRoom))
            val items =dataBaseManager.getDevicesInRoom(idOfRoom)
            val adapter = RecyclerAdapterForMyDevices(items)
            val layoutManager = GridLayoutManager(this@DevicesInRoomActivity, 2)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = adapter}
    }

    fun onClickBtPlus(view: View) {
        val intent= Intent(this,ChooseDeviceActivity::class.java)
        //var idOfRoom=intent.getIntExtra("id",0)
        Toast.makeText(this@DevicesInRoomActivity, idOfRoom.toString(), Toast.LENGTH_SHORT).show()
        intent.putExtra("roomId", idOfRoom)
        startActivity(intent)
    }
}