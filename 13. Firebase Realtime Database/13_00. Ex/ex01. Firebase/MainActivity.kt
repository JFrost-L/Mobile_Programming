package com.frost.ex01firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.frost.ex01firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var layoutManager:LinearLayoutManager
    lateinit var adapter:MyProductAdapter
    lateinit var rdb:DatabaseReference
    var findQuery = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rdb= Firebase.database.getReference("Products/items")//참조 객체
        val query = rdb.limitToLast(50)//최근에 질의한 것 중 50개 갖고 오는 질의문

        val option = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()
        //query객체로부터 옵션 객체 생성

        adapter = MyProductAdapter(option)
        //option 객체로 adapter 객체 생성
        adapter.itemClickListener=object:MyProductAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                binding.apply {
                    pIdEdit.setText(adapter.getItem(position).pId.toString())
                    pNameEdit.setText(adapter.getItem(position).pName)
                    pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
                }
            }
        }
        binding.apply {
            recyclerView.layoutManager=layoutManager
            recyclerView.adapter = adapter

            insertBtn.setOnClickListener {
                initQuery()
                val item = Product(pIdEdit.text.toString().toInt(),
                pNameEdit.text.toString(), pQuantityEdit.text.toString().toInt())
                //rdb객체에 삽입
                rdb.child(pIdEdit.text.toString()).setValue(item)
                clearInput()
            }

            findBtn.setOnClickListener {
                if(!findQuery){//find인 경우와 아닌 경우의 recyclerView 구분
                    findQuery=true
                }
                if(adapter!=null){//null이 아니면 진행중이였다는 것이니 일단 중지
                    adapter.stopListening()
                }
                val query = rdb.orderByChild("pname").equalTo(pNameEdit.text.toString())
                //이름으로 찾기

                val option = FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(query, Product::class.java)
                    .build()
                //query객체로부터 옵션 객체 생성

                adapter = MyProductAdapter(option)
                //option 객체로 adapter 객체 생성
                adapter.itemClickListener=object:MyProductAdapter.OnItemClickListener{
                    override fun OnItemClick(position: Int) {
                        binding.apply {
                            pIdEdit.setText(adapter.getItem(position).pId.toString())
                            pNameEdit.setText(adapter.getItem(position).pName)
                            pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
                        }
                    }
                }
                //내가 설정한 adapter를 붙이기
                recyclerView.adapter=adapter
                adapter.startListening()
                //다시 시작
            }

            deleteBtn.setOnClickListener {
                initQuery()
                rdb.child(pIdEdit.text.toString()).removeValue()
                //id 기반으로 key를 삼았으니 해당 value를 지우겠다는 의미
                clearInput()
            }

            updateBtn.setOnClickListener {
                initQuery()
                rdb.child(pIdEdit.text.toString())
                    .child("pquantity")
                    .setValue(pQuantityEdit.text.toString().toInt())
                //해당 id의 Quantity만 변경
                clearInput()
            }
        }
    }
    fun initQuery(){
        if(findQuery){//find인 경우와 아닌 경우의 recyclerView 구분
            //이 때는 find가 아닌 경우
            findQuery=false
            if(adapter!=null){//null이 아니면 진행중이였다는 것이니 일단 중지
                adapter.stopListening()
            }
            val query = rdb.limitToLast(50)
            //이름으로 찾기

            val option = FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product::class.java)
                .build()
            //query객체로부터 옵션 객체 생성

            adapter = MyProductAdapter(option)
            //option 객체로 adapter 객체 생성
            adapter.itemClickListener=object:MyProductAdapter.OnItemClickListener{
                override fun OnItemClick(position: Int) {
                    binding.apply {
                        pIdEdit.setText(adapter.getItem(position).pId.toString())
                        pNameEdit.setText(adapter.getItem(position).pName)
                        pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
                    }
                }
            }
            //내가 설정한 adapter를 붙이기
            binding.recyclerView.adapter=adapter
            adapter.startListening()
            //다시 시작
        }
    }
    fun clearInput(){//입력한 것 삭제
        binding.apply {
            pIdEdit.text.clear()
            pNameEdit.text.clear()
            pQuantityEdit.text.clear()
        }
    }

    override fun onStart() {//변화된 내용 자동 감지해서 동기화 수행
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {//변화된 내용 자동 감지해서 동기화 수행
        super.onStop()
        adapter.stopListening()
    }
}