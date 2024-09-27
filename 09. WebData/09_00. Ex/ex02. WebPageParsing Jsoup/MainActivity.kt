package com.frost.ex02webpageparsing_jsoup

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.ex02webpageparsing_jsoup.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var adapter: MyDataAdapter
    val url = "https://news.daum.net"
    val scope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }
    fun getnews(){
        scope.launch {//IO 스코프라서 Main 스레드 내용 갱신 불가능 => withContext로 잠시 변경
            adapter.items.clear()
            //호출할 때마다 배열 비워서 다시 생성
            
            //jsoup으로 network의 정보 갖고 오기
            //document 내용에서 파싱하기
            val doc = Jsoup.connect(url).get()
            //Log.i("check",doc.toString())

            val headlines = doc.select("ul.list_newsissue>li>div.item_issue>div>strong.tit_g>a")
            //select로 원하는 element 파싱
            for(news in headlines){
                adapter.items.add(MyData(news.text(), news.absUrl("href")))
            }
            withContext(Dispatchers.Main){//잠시 Main 스레드로 변경
                adapter.notifyDataSetChanged()
                //UI 갱신
                binding.swipe.isRefreshing=false
                //갱신이 종료되었으니까 화면에서 안보이게 설정
            }
        }
    }
    private fun initLayout() {
        binding.swipe.setOnRefreshListener {//새로고침 콜백 함수 
            binding.swipe.isRefreshing=true
            //true 설정시 progressBar가 화면에 보여짐
            getnews()
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        //리사이클러뷰의 row마다 구분자로 사용 가능한 decoration 추가
        adapter = MyDataAdapter(ArrayList<MyData>())
        adapter.itemClickListener = object:MyDataAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent = Intent(ACTION_VIEW, Uri.parse((adapter.items[position].url)))
                startActivity(intent)
            }
        }
        binding.recyclerView.adapter=adapter
        getnews()
    }
}