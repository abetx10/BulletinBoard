package com.example.bulletinboard.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bulletinboard.R
import com.example.bulletinboard.frag.SelectImageItem


class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    val mainArray = ArrayList<SelectImageItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item, parent, false)
        return ImageHolder(view)

    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position].imageUri)
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }
    class ImageHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var imItem : ImageView
        fun setData(uri : String) {
            imItem = itemView.findViewById(R.id.imItem)
            imItem.setImageURI(Uri.parse(uri))

        }

    }

    fun updateAdapter(newList : List<SelectImageItem>){
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()

    }
}