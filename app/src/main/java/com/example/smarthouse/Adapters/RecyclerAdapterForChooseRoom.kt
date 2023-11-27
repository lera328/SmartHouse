package com.example.smarthouse.Adapters

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.DB.TypesOfRoom
import com.example.smarthouse.R
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.YourHouseActivity
import com.example.smarthouse.databinding.CardviewItemForChooseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class RecyclerAdapterForChooseRoom(private val itemList: List<TypesOfRoom>) :
    RecyclerView.Adapter<RecyclerAdapterForChooseRoom.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding=CardviewItemForChooseBinding.bind(view)
        fun bind(item:TypesOfRoom){
            val retrievedImageFile = File(view.context.filesDir, "image.jpg")
            val imageManager = ImageManager()
            imageManager.byteArrayToImage(item.image, retrievedImageFile)
            val bitmap = BitmapFactory.decodeFile(retrievedImageFile.absolutePath)
            binding.imageView.setImageBitmap(bitmap)
            binding.text.setText(item.type)
            binding.imageView.setOnClickListener {
                val dataBaseManager= DataBaseManager()
                GlobalScope.launch(Dispatchers.Main) {
                    dataBaseManager.addNewRoom(
                        view.context as Activity,
                        item.type,
                        dataBaseManager.getRoomTypeId(item.type)
                    )
                    val intent=Intent(view.context, YourHouseActivity::class.java)
                    intent.putExtra("Selected", item.type)
                    view.context.startActivity(intent)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_item_for_choose, parent, false)
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