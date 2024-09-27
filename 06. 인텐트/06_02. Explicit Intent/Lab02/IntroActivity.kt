package com.frost.lab02explicitintent

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.frost.lab02explicitintent.databinding.ActivityIntroBinding
import com.frost.lab02explicitintent.databinding.ActivityMainBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    val launcher = //인텐트로 상호작용할 launcher 객체 생성
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //콜백 함수 등록
            if(it.resultCode == Activity.RESULT_OK){
                val voc = it.data?.getSerializableExtra("voc") as MyData
                //인텐트 정보를 반환
                Toast.makeText(this, voc.word + "추가됨", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.voclistbtn.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        binding.addvocbtn.setOnClickListener {
            val i = Intent(this, AddVocActivity::class.java)
            launcher.launch(i)
            //인텐트를 담아서 만든 런처 객체 launch 하기
            //그러면 콜백함수로 startActivityForResult 함수가 호출
            //이렇게 전용함수로 만든 것 실행
        }
    }
}