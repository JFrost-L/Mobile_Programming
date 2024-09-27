package com.frost.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.frost.ex01recyclerview.MyData
import com.frost.ex01recyclerview.MyDataAdapter
import com.frost.recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var data:ArrayList<MyData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
    }
    //자동으로 메뉴를 생성 및 부착
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
        //menuInflater 객체로 inflate()하기
        //만든 객체 연결
        return true
    }
    //메뉴 변경 이벤트 처리 - 리사이클러뷰 Layout 조절
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           R.id.menuitem1 -> {
                binding.recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false)
           }
            R.id.menuitem2 -> {
                binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
            }
            R.id.menuitem3 -> {
                binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL)
            }
        }
        return true
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = MyDataAdapter(data)//어댑터 클래스 만들기
    }

    private fun initData() {
        data.add(MyData("item1", 10))
        data.add(MyData("item2", 20))
        data.add(MyData("item3", 30))
        data.add(MyData("item4", 15))
        data.add(MyData("item5", 50))
        data.add(MyData("item6", 31))
    }
}