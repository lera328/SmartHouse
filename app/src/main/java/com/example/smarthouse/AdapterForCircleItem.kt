package com.example.smarthouse

import android.graphics.Picture
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterForCircleItem (private val itemList:List<ItemData>): RecyclerView.Adapter<AdapterForCircleItem.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Установка изображения на круглом фоне серого цвета
        //holder.imageView.setImageResource(item.imageResId)

        // Установка текста под картинкой
        holder.textView.text = item.text
    }
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
        }
}

data class ItemData(val text:String, val picture: Picture) {

}
