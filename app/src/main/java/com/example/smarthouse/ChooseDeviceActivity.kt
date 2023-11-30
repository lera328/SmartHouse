package com.example.smarthouse

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthouse.Adapters.RecyclerAdapterForChooseDevices
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.databinding.ActivityChooseDeviceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.DatabaseMetaData

class ChooseDeviceActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseDeviceBinding
    lateinit var adapter: RecyclerAdapterForChooseDevices
    var idOfRoom: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()

        idOfRoom = intent.getIntExtra("roomId", 0)
        //Toast.makeText(this@ChooseDeviceActivity, idOfRoom.toString(), Toast.LENGTH_SHORT).show()
        val dataBaseManager = DataBaseManager()
        GlobalScope.launch(Dispatchers.Main) {
            val items = dataBaseManager.getDevicesTypes()
            adapter = RecyclerAdapterForChooseDevices(items)
            val layoutManager = GridLayoutManager(this@ChooseDeviceActivity, 3)
            binding.recyclerViewDevices.adapter = adapter
            binding.recyclerViewDevices.layoutManager = layoutManager

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun onClick(view: View) {
        Toast.makeText(this@ChooseDeviceActivity, idOfRoom.toString(), Toast.LENGTH_SHORT).show()
        if (binding.editTextText2.text.length > 1 && adapter.getSelectedIem() != null) {


            val dataBaseManager = DataBaseManager()

            GlobalScope.launch(Dispatchers.Main) {
                dataBaseManager.addNewDeviceToRoom(
                    this@ChooseDeviceActivity, binding.editTextText2.text.toString(),
                    adapter.getSelectedIem()!!.iconId, idOfRoom, adapter.getSelectedIem()!!.id
                )
                val intent = Intent(this@ChooseDeviceActivity, DevicesInRoomActivity::class.java)
                intent.putExtra("id", idOfRoom)
                startActivity(intent)
            }
        } else Toast.makeText(this, "Введите имя и выберите тип комнаты", Toast.LENGTH_SHORT).show()
    }

    fun onClickEx(view: View) {
        val intent=Intent(this, DevicesInRoomActivity::class.java)
        intent.putExtra("id", idOfRoom)
        startActivity(intent)
    }
}