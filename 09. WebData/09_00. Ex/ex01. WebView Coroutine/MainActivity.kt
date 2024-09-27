package com.frost.ex01webview_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.frost.ex01webview_coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val scope = CoroutineScope(Dispatchers.Main)
    //Main 스레드에서만 UI 상태 변경 가능
    lateinit var requestQueue:RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initLayout()
        initLayout2()
    }

    private fun initLayout2() {
        requestQueue = Volley.newRequestQueue(this)
        binding.apply {
            button.setOnClickListener { 
                progressBar.visibility=View.VISIBLE
                val stringRequest = StringRequest(//받고자 하는 타입의 request 객체 생성
                    Request.Method.GET,
                    editText.text.toString(),//String 타입의 url 정보
                    Response.Listener {//성공시의 콜백함수 => 2번째 인자인 String 객체 그대로 들어옴 
                        textView.text = String(it.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                        //인코딩 모드 변경해서 text에 저장
                        progressBar.visibility=View.GONE
                    },
                    Response.ErrorListener {//실패시의 콜백함수 
                        textView.text = it.message
                    }
                )
                requestQueue.add(stringRequest)//큐에 추가
            }
        }
    }

    private fun initLayout() {
        binding.apply {
            Log.i("CheckScope", Thread.currentThread().name) // 메인 스레드에 해당
            button.setOnClickListener {
                scope.launch {//시작
                    Log.i("CheckScope", Thread.currentThread().name)// 메인 스레드에 해당
                    progressBar.visibility = View.VISIBLE
                    var data = ""
                    withContext(Dispatchers.IO){//이미 await() 자동으로 갖음
                        data=loadNetwork(URL(editText.text.toString()))
                    }
                    /*
                    CoroutineScope(Dispatchers.IO).async {//비동기식으로 코루틴 객체 생성
                        Log.i("CheckScope", Thread.currentThread().name) //DefaultDispacter 스레드에 해당
                        data=loadNetwork(URL(editText.text.toString()))
                    }.await()//기다리라고 설정
                    */
                    textView.text = data
                    progressBar.visibility=View.GONE
                }
            }
        }
    }
    fun loadNetwork(url: URL):String{
        var result = ""
        val connect = url.openConnection() as HttpURLConnection//객체 생성
        connect.connectTimeout=4000
        connect.readTimeout = 1000
        connect.requestMethod="GET"
        connect.connect()
        val responseCode = connect.responseCode
        if(responseCode==HttpURLConnection.HTTP_OK){
            result = streamToString(connect.inputStream)
        }
        return result
    }

    private fun streamToString(inputStream: InputStream?): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var result = ""
        try {
            do{
                line = bufferReader.readLine()
                if(line!=null){
                    result+=line
                }
            }while(line!=null)
            Log.i("close", "Close")
            inputStream?.close()
        }catch(ex: Exception){
            Log.e("error","읽기 실패")
        }
        return result
    }
}