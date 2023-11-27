package com.example.smarthouse.Adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.DB.Rooms
import com.example.smarthouse.DevicesInRoomActivity
import com.example.smarthouse.R
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.databinding.CardviewItemForMyRoomsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ResyslerAdapterForMyRooms(private val itemList: List<Rooms>) :
    RecyclerView.Adapter<ResyslerAdapterForMyRooms.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = CardviewItemForMyRoomsBinding.bind(view)
        fun bind(item: Rooms) {
            GlobalScope.launch(Dispatchers.Main) {
                val retrievedImageFile = File(view.context.filesDir, "image.jpg")
                val imageManager = ImageManager()

                val dataBaseManager = DataBaseManager()
                val image=dataBaseManager.getRoomTypeImage(item.idOfType)
                imageManager.byteArrayToImage(image, retrievedImageFile)
                val bitmap = BitmapFactory.decodeFile(retrievedImageFile.absolutePath)
                binding.imageViewMyRoom.setImageBitmap(bitmap)
                binding.textViewMyRoom.setText(item.nameOfRoom)
                binding.imageViewMyRoom.setOnClickListener {
                    val intent=Intent(view.context,DevicesInRoomActivity::class.java)
                    intent.putExtra("name", item.nameOfRoom)
                    view.context.startActivity(intent)
                }
            }
        }
    }



override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.cardview_item_for_my_rooms, parent, false)
    return ViewHolder(view)
}

override fun getItemCount(): Int {
    return itemList.size
}

override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = itemList[position]
    holder.bind(item)
}
}
