package com.example.bulletinboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bulletinboard.data.Ad
import com.example.bulletinboard.databinding.AdListItemBinding

class AdsRcAdapter : RecyclerView.Adapter<AdsRcAdapter.AdHolder>() {
    val adArray = ArrayList<Ad>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context))
        return AdHolder(binding)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }

    fun updateAdapter(newList : List<Ad>){
        adArray.clear()
        adArray.addAll(newList)
        notifyDataSetChanged()

    }

    class AdHolder(val binding : AdListItemBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(ad : Ad) {
            binding.apply {
                tvDescription.text = ad.description
                tvPrice.text = ad.price

            }

        }

    }


}