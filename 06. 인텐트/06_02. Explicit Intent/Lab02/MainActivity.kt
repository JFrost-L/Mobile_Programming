package com.frost.lab02explicitintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.lab02explicitintent.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val data:ArrayList<MyData> = ArrayList()
    lateinit var adapter:MyDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initRecyclerView()
    }

    private fun initData() {
        try {
            //내가 단어를 추가해 파일에서 읽어오기
            val scan2 = Scanner(openFileInput("voc.txt"))
            readScanFile(scan2)
            scan2.close()
        }catch(ex:Exception){
        }
        val scan = Scanner(resources.openRawResource(R.raw.words))
        //resources의 rawResource를 open해서 words.txt 진입
        readScanFile(scan)
        scan.close()

    }

    private fun readScanFile(scan: Scanner) {
        //scan로 객체로 파일을 읽어서 data에 추가하는 메서드
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            data.add(MyData(word, meaning))
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        adapter = MyDataAdapter(data)//명시적으로 adapter 객체 생성 후 사용
        adapter.itemClickListener = object :MyDataAdapter.OnItemClickListener{
            //adapter 객체의 item 클릭시 리스너 초기화... 익명 객체로(object)
            override fun OnItemClick(data: MyData) {
                //mainActivity에서 data를 받음
                Toast.makeText(this@MainActivity, data.meaning, Toast.LENGTH_SHORT).show()
                //그냥 this는 안돼 그냥 this는 mainActivity가 아닌 adapter의 this를 의미
            }
        }
        binding.recyclerView.adapter = adapter
        //어댑터 연결
    }
}