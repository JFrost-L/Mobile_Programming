package com.frost.lab02explicitintent

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.lab02explicitintent.databinding.ActivityAddVocBinding
import java.io.PrintStream

class AddVocActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddVocBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.addbtn.setOnClickListener {
            val word = binding.word.text.toString()
            val meaning = binding.meaning.text.toString()

            val output = PrintStream(openFileOutput("voc.txt", Context.MODE_APPEND))
            //파일 출력을 위한 output 객체 open해서 append모드로 추가
            output.println(word)
            output.println(meaning)
            //해당 파일에 출력하기
            output.close()
            //파일 닫기

            val i = Intent()
            i.putExtra("voc", MyData(word, meaning))
            //인텐트 생성해서 데이터 담기
            setResult(Activity.RESULT_OK, i)
            //OK 라는 result값 넘겨서 해당 콜백함수 실행하고 데이터 담은 인텐트 정보 전달
            finish()
        }
        binding.cancelbtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            //캔슬되었다는 result값 넘기고
            finish()
            //종료
        }
    }
}