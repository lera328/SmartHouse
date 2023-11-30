package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthouse.Adapters.RecyclerAdapterForChooseDevices
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Adapters.RecyclerAdapterForChooseRoom
import com.example.smarthouse.databinding.ActivityChooseRoomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChooseRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseRoomBinding
    lateinit var adapter: RecyclerAdapterForChooseRoom
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.VISIBLE
        val layoutManager =
            GridLayoutManager(this, 3) // Устанавливаем LayoutManager на GridLayout с 3 колонками
        val dataBaseManager = DataBaseManager()
        GlobalScope.launch(Dispatchers.Main) {
            val items = dataBaseManager.getAllRoomTypes()
            adapter = RecyclerAdapterForChooseRoom(items)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
        }
        binding.progressBar.visibility = View.GONE
    }

    fun onClickSave(view: View) {
        binding.progressBar.visibility = View.VISIBLE
        if (binding.editTextText2.text.length > 1 && adapter.getSelectedIem() != null) {

            val dataBaseManager = DataBaseManager()

            GlobalScope.launch(Dispatchers.Main) {
                dataBaseManager.addNewRoom(
                    this@ChooseRoomActivity, binding.editTextText2.text.toString(),
                    adapter.getSelectedIem()!!.id
                )
                val intent = Intent(this@ChooseRoomActivity, YourHouseActivity::class.java)
                binding.progressBar.visibility=View.GONE
                startActivity(intent)
                finish()
            }
        } else {binding.progressBar.visibility=View.GONE
            Toast.makeText(this, "Введите имя и выберите тип комнаты", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickExit(view: View) {
        val intent = Intent(this, YourHouseActivity::class.java)
        startActivity(intent)
        finish()
    }
}
