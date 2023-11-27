package com.example.smarthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Adapters.RecyclerAdapterForChooseRoom
import com.example.smarthouse.databinding.ActivityChooseRoomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChooseRoomActivity : AppCompatActivity() {
    lateinit var binding:ActivityChooseRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChooseRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = GridLayoutManager(this, 3) // Устанавливаем LayoutManager на GridLayout с 3 колонками
        val dataBaseManager=DataBaseManager()
        GlobalScope.launch(Dispatchers.Main) {
        val items =dataBaseManager.getAllRoomTypes()
        val adapter = RecyclerAdapterForChooseRoom(items)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter}
    }
}