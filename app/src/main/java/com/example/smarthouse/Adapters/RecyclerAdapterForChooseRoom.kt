package com.example.smarthouse.Adapters

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.DB.TypesOfRoom
import com.example.smarthouse.DB.TypesOfRoomWithId
import com.example.smarthouse.DB.typesOfDevicesWithId
import com.example.smarthouse.R
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.YourHouseActivity
import com.example.smarthouse.databinding.CardviewItemForChooseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class RecyclerAdapterForChooseRoom(private val itemList: List<TypesOfRoomWithId>) :
    RecyclerView.Adapter<RecyclerAdapterForChooseRoom.ViewHolder>() {
    var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = CardviewItemForChooseBinding.bind(view)
        val imageManager = ImageManager()
        val dataBaseManager = DataBaseManager()

        fun bind(item: TypesOfRoomWithId, position: Int) {
            val retrievedImageFile = File(view.context.filesDir, "image.jpg")

            GlobalScope.launch(Dispatchers.Main) {
                binding.textV.setText(item.type)
                val images = dataBaseManager.getMyRoomIconFromId(item.image)
                if (selectedPosition == position) {
                    imageManager.byteArrayToImage(
                        images.blue,
                        retrievedImageFile
                    ) // Установка цвета фона для выбранного элемента
                } else {
                    imageManager.byteArrayToImage(
                        images.white,
                        retrievedImageFile
                    ) // Сброс цвета фона для остальных элементов
                }
                //imageManager.byteArrayToImage(images.white, retrievedImageFile)
                val bitmap = BitmapFactory.decodeFile(retrievedImageFile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)
                binding.textV.setText(item.type)
            }
            binding.imageView.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    val previouslySelectedItemPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previouslySelectedItemPosition)
                    notifyItemChanged(selectedPosition)
                    Toast.makeText(view.context, getSelectedIem()?.type, Toast.LENGTH_SHORT).show()
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
        holder.bind(item, position)
    }

    fun getSelectedIem(): TypesOfRoomWithId? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            itemList[selectedPosition]
        } else {
            null
        }
    }
}