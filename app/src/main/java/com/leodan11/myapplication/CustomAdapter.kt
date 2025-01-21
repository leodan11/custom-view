package com.leodan11.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter (private var listData: List<String> = listOf()): RecyclerView.Adapter<CustomAdapter.HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.header_item, parent, false)
        return HeaderViewHolder(view)
    }

    /* Binds number of flowers to the header. */
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    /* Returns number of items, since there is only one item in the header return one  */
    override fun getItemCount(): Int {
        return listData.size
    }

    /* Updates header to display number of flowers when a flower is added or subtracted. */
    @SuppressLint("NotifyDataSetChanged")
    fun updateFlowerCount(updatedFlowerCount: List<String>) {
        listData = updatedFlowerCount
        notifyDataSetChanged()
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val flowerNumberTextView: TextView = itemView.findViewById(R.id.flower_text)

        fun bind(flowerCount: String) {
            flowerNumberTextView.text = flowerCount
        }
    }

}