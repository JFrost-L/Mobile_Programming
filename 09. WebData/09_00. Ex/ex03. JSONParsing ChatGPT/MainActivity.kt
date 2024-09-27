package com.frost.ex03jsonparsing_chatgpt

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.frost.ex03jsonparsing_chatgpt.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val url = "https://api.openai.com/v1.completions"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            sendBtn.setOnClickListener {
                responseTV.text="잠시 기다려주세요..."
                if(userQuestion.text.toString().length>0){
                    getResponse(userQuestion.text.toString())
                }else{
                    Toast.makeText(this@MainActivity, "질문을 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getResponse(query: String) {
        binding.questionTV.text=query
        binding.userQuestion.setText("")
        
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        //Json 객체 생성
        jsonObject.put("model", "text-davinci-003")//모델
        jsonObject.put("prompt",query) //질의문
        jsonObject.put("temperature", 0)//무작위성(높으면 무작위성 높고 창의성 높음)
        jsonObject.put("max_tokens", 200)//최대 토큰 수
        
        //Request객체를 JsonObjectRequest로 만듦
        val postRequest = object:JsonObjectRequest(Method.POST, url, jsonObject,
        Response.Listener {//접속 성공시 실행할 내용 
            //Log.i("check",it.toString())
            val msg = it.getJSONArray("choices")//해당 ket의 JSONArray 얻어서
                .getJSONObject(0)//첫 Json object
                .getString("text")//text에 해당하는 key 얻기

        },
        Response.ErrorListener { //접속 실패시 실행할 내용
            Log.e("Error", it.message.toString())
        }){//getHeaders() 메서드 오버라이딩 (POST 형식으로 사용하기 때문에 Header 정보를 바꿀 필요가 있음)
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                //header에 정보를 담아서 리턴
                header["Content-Type"]="application/json"
                header["Authorization"]="Bearer your-api-key"//나의 키정보를 넣어야함
                return header
            }
        }
        queue.add(postRequest)
    }
}