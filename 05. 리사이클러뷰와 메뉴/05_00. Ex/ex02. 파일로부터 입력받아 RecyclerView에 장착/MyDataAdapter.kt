package com.frost.voca

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frost.voca.databinding.RowBinding

class MyDataAdapter (val items:ArrayList<MyData>):RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {
    //어댑터 클래스 생성시 아이템을 받아야함
    //어댑터 클래스는 RecyclerView의 어댑터를 상속받아서 만들어야 함
    // 그 때 상속할 어댑터에는 ViewHolder클래스가 들어가야함

    interface OnItemClickListener{
        //클릭 리스너 인터페이스 선언 : 완전히 독립적인 클래스로 구현하기 위함
        //메인에서 이용할 일종의 틀을 설계
        fun OnItemClick(data:MyData)
        //클릭 리스너 사용시 수행할 이벤트
        //매개변수에는 이 어댑터에서 연동 가능한 것을 넣기
    }
    var itemClickListener:OnItemClickListener?=null
    //누군가가 이것을 구현할테니 일단 객체 선언

    inner class ViewHolder(val binding: RowBinding):RecyclerView.ViewHolder(binding.root){
        //viewHolder 클래스는 inner 클래스로 만들었고 들어오는 데이터는 binding객체가 들어옴
        //해당 ViewHolder 클래스도 RecyclerView의 ViewHolder를 상속받아 생성

        //위치는 view가 만들어졌을 때가 좋으니 ViewHolder 클래스 내부에 수행
        //view의 아이템 클릭시 itemClickListener 이벤트 처리하기 위해서 여기서 호출 시점 결정
        init {
          binding.textView.setOnClickListener{
              itemClickListener?.OnItemClick(items[adapterPosition])
              //포지션은 자동으로 가져옴
              //이 adatperView를 부착하고 있는 누군가가(MainActivity) 구현할 것
          }
        }
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
        holder.binding.textView.text = items[position].word
    }
}