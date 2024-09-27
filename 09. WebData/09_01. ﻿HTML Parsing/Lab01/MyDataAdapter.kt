package com.frost.lab01html_parsing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.frost.lab01html_parsing.databinding.RowBinding

class MyDataAdapter (val items:ArrayList<MyData>):RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(data:MyData, position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.newstitle.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.newstitle.text = items[position].title
    }
}