package com.frost.lab02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frost.lab02.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val data:ArrayList<MyData> = ArrayList()
    lateinit var adapter: MyDataAdapter
    lateinit var tts: TextToSpeech
    var isTTSReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initRecyclerView()
        initTTS()
    }

    private fun initTTS() {
        tts = TextToSpeech(this){
            //함수가 두번째 인자면 밖으로 따로 빼서 사용 가능
            isTTSReady = true //TTS 준비 되었다는 true
            tts.language = Locale.US//영어로 언어 설정
            //아이템 클릭시 콜백함수로서 읽어줄 것!
        }
    }

    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.words))
        //resources의 rawResource를 open해서 words.txt 진입
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            data.add(MyData(word, meaning))
        }
        scan.close()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        adapter = MyDataAdapter(data)//명시적으로 adapter 객체 생성 후 사용
        adapter.itemClickListener = object : MyDataAdapter.OnItemClickListener{
            //adapter 객체의 item 클릭시 리스너 초기화... 익명 객체로(object)
            override fun OnItemClick(data: MyData, position: Int) {
                //mainActivity에서 data를 받음
                if(isTTSReady){
                    tts.speak(data.word, TextToSpeech.QUEUE_ADD, null, null)
                    //클릭 할 때마다 word를 Queue에 추가
                    //클릭할 때 TextToSpeech 콜백함수로 활용
                }
                Toast.makeText(this@MainActivity, data.meaning, Toast.LENGTH_SHORT).show()
                 //그냥 this는 안돼 그냥 this는 mainActivity가 아닌 adapter의 this를 의미
            }
        }
        binding.recyclerView.adapter = adapter
        //어댑터 연결

        //1. 콜백 익명 객체 생성
        val simpleCallback = object:ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT
        ){
            override fun onMove(//Drag&Drop 시 호출됨
                recyclerView: RecyclerView,//기능 recyclerView 설정
                viewHolder: RecyclerView.ViewHolder,//Drag 할 viewHolder
                target: RecyclerView.ViewHolder//Drag 될 viewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Swipe 시 호출됨
                //recyclerView의 data는 Adapter 클래스에서 관리 중이라 
                // Adapter 클래스에서 삭제해주면서 layout 갱신
                adapter.removeItem(viewHolder.adapterPosition)
                //해당 위치의 data 삭제되었음을 알리면 어댑터에서 알아서 삭제하고 갱신해줌
            }
        }
        
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        //방금 만든 콜백 익명 객체인 해당 기능을 갖는 아이템 터치 helper 객체 생성
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        //recyclerView에 attach하기
    }

    override fun onStop() {
        //앱이 멈춰있는 상태일 때 TTS 서비스도 멈추기
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        //완전히 앱이 종료되었을 때 TTS 서비스도 shutdown해주기 for 안전한 사용
        super.onDestroy()
        tts.shutdown()
    }
}