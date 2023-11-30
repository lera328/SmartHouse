package com.example.smarthouse.Adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.DB.Rooms
import com.example.smarthouse.DB.device_parametersWithId
import com.example.smarthouse.DB.devices
import com.example.smarthouse.DB.devicesWithId
import com.example.smarthouse.R
import com.example.smarthouse.RefactDeviceParametrActivity
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.databinding.ActivityRefactDeviceParametrBinding
import com.example.smarthouse.databinding.CardviewItemForMyRoomsBinding
import com.example.smarthouse.databinding.ItemForDeviceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class RecyclerAdapterForMyDevices(private val items: List<devicesWithId>) :
    RecyclerView.Adapter<RecyclerAdapterForMyDevices.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemForDeviceBinding.bind(view)
        val dataBaseManager = DataBaseManager()
        fun bind(item: devicesWithId) {
            GlobalScope.launch(Dispatchers.Main) {
                val retImageFile = File(view.context.filesDir, "image.jpg")
                val imageManager = ImageManager()

                val icons = dataBaseManager.getDevicesIcon(item.icon)
                val image = icons.grey
                imageManager.byteArrayToImage(image, retImageFile)
                val bitmap = BitmapFactory.decodeFile(retImageFile.absolutePath)
                binding.imageView5.setImageBitmap(bitmap)
                binding.textView13.setText(item.name)

                val parametrs: device_parametersWithId =
                    dataBaseManager.getDeviceParameters(item.id)
                var par_text = ""
                binding.switch1.isChecked = parametrs.status
                if (parametrs.status) binding.textView14.visibility = View.VISIBLE

                if (parametrs.brightness >= 0) par_text += "Яркость ${parametrs.brightness}%+\n"
                if (parametrs.power >= 0) par_text += "Мощность ${parametrs.power}%\n"
                if (parametrs.temperature >= 0) par_text += "Температура ${parametrs.temperature} C"

                binding.textView14.setText(par_text)
            }
            view.setOnClickListener {
                val intent = Intent(view.context, RefactDeviceParametrActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("name", item.name)
                intent.putExtra("type", item.typeId)
                view.context.startActivity(intent)
            }
            binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
                GlobalScope.launch(Dispatchers.Main) {
                    dataBaseManager.changeDeviceStatus(item.id, isChecked)
                }
                if(isChecked) binding.textView14.visibility = View.VISIBLE
                else binding.textView14.visibility = View.GONE
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_device, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}