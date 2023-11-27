package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Adapters.ResyslerAdapterForMyRooms
import com.example.smarthouse.databinding.ActivityYourHouseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class YourHouseActivity : AppCompatActivity() {
    lateinit var binding:ActivityYourHouseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityYourHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickBtPlus(view: View) {
        val intent= Intent(this,ChooseRoomActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        val dataBaseManager= DataBaseManager()
        GlobalScope.launch(Dispatchers.Main) {
            val items =dataBaseManager.getAllMyRooms()
            val adapter = ResyslerAdapterForMyRooms(items)
            val layoutManager = GridLayoutManager(this@YourHouseActivity, 1)
            binding.recyclerViewRooms.layoutManager = layoutManager
            binding.recyclerViewRooms.adapter = adapter}
    }
}