package com.frost.ex01firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.frost.ex01firebase.databinding.RowBinding

class MyProductAdapter(options : FirebaseRecyclerOptions<Product>)
    : FirebaseRecyclerAdapter<Product, MyProductAdapter.ViewHolder>(options) {
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model:Product) {
        holder.binding.apply {
            pId.text = model.pId.toString()
            pName.text = model.pName
            pQuantity.text = model.pQuantity.toString()
        }
    }
}