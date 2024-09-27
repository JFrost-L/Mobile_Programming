package com.frost.ex02textinputlayout_textinputedittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.frost.ex02textinputlayout_textinputedittext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.emailText.addTextChangedListener{//이메일 입력 이벤트 처리
            if(it.toString().contains("@")){//@가 포함되어 있는지
                binding.textInputLayout.error=null
            }else{
                binding.textInputLayout.error="이메일 형식이 올바르지 않습니다."
            }
        }
    }
}