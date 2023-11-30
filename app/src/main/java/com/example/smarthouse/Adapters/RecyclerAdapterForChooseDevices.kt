package com.example.smarthouse.Adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.ChooseDeviceActivity
import com.example.smarthouse.DB.typesOfDevices
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

class RecyclerAdapterForChooseDevices(private val items: List<typesOfDevicesWithId>) :
    RecyclerView.Adapter<RecyclerAdapterForChooseDevices.ViewHolder>() {
    private var selectedPosition=RecyclerView.NO_POSITION


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = CardviewItemForChooseBinding.bind(view)
        val dataBaseManager = DataBaseManager()
        val imageManager = ImageManager()

        fun bind(item: typesOfDevicesWithId, position: Int) {



            GlobalScope.launch(Dispatchers.Main) {
                val icons = dataBaseManager.getDevicesIcon(item.iconId)
                val retrievedImageFile = File(view.context.filesDir, "image.jpg")
                if (selectedPosition == position) {
                    imageManager.byteArrayToImage(icons.blue, retrievedImageFile) // Установка цвета фона для выбранного элемента
                } else {
                    imageManager.byteArrayToImage(icons.white, retrievedImageFile) // Сброс цвета фона для остальных элементов
                }

                val bitmap_white = BitmapFactory.decodeFile(retrievedImageFile.absolutePath)
                binding.imageView.setImageBitmap(bitmap_white)
                binding.textV.setText(item.type)
            }



            view.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    val previouslySelectedItemPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previouslySelectedItemPosition)
                    notifyItemChanged(selectedPosition)
                    Toast.makeText(view.context,getSelectedIem()?.type,Toast.LENGTH_SHORT).show()
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
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)
    }
    fun getSelectedIem(): typesOfDevicesWithId? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            items[selectedPosition]
        } else {
            null
        }
    }
}