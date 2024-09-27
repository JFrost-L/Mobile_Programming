package com.frost.ex01recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frost.recyclerview.databinding.RowBinding

class MyDataAdapter (val items:ArrayList<MyData>):RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {
    //어댑터 클래스 생성시 아이템을 받아야함
    //어댑터 클래스는 RecyclerView의 어댑터를 상속받아서 만들어야 함
    // 그 때 상속할 어댑터에는 ViewHolder클래스가 들어가야함
    inner class ViewHolder(val binding: RowBinding):RecyclerView.ViewHolder(binding.root){
        //viewHolder 클래스는 inner 클래스로 만들었고 들어오는 데이터는 binding객체가 들어옴
        //해당 ViewHolder 클래스도 RecyclerView의 ViewHolder를 상속받아 생성
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //RowBinding 객체 만들어서 ViewHolder 생성
        val view = RowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //items의 데이터 연결
        holder.binding.textView.text = items[position].textString
        holder.binding.textView.textSize = items[position].textPt.toFloat()
        //textSize는 float 타입이라 int에서 타입 변환
    }
}