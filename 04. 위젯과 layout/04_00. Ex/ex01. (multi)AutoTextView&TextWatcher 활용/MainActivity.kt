package com.frost.ex01

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.frost.ex01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    val countries = mutableListOf<String>(
        "Afghanistan", "Albania", "Algeria",
        "American Samoa", "Andorra", "Bahrain",
        "Barbados","Belarus","Belgium","CABelgium", "한국","중국","일본","미국"
    )
    lateinit var countries2:Array<String>

    lateinit var adapter1 : ArrayAdapter<String>
    lateinit var adapter2 : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()

    }

    private fun initLayout() {
        countries2 = resources.getStringArray(R.array.coutries_array)
        //string파일의 item들을 배열에 대입
        adapter1 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,countries)
        //어댑터 생성
        adapter2 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,countries2)
        //어댑터 생성

        binding.autoCompleteTextView.setAdapter(adapter1)
        //어댑터1 연결
        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            //클릭 이벤트 -> 클릭한 것 잠시 띄우기
            val item = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "선택된 나라 : $item",Toast.LENGTH_SHORT).show()
        }


        binding.multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        //콤마 토크나이저 세팅
        binding.multiAutoCompleteTextView.setAdapter(adapter2)
        //어댑터2 연결

        /* 1. 람다함수로 TextWatcher 처리하기
        binding.editText.addTextChangedListener(object:TextWatcher{//editText의 text인 뷰가 변화될 때 이벤트 처리
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {//editText의 text 변화될 때 호출
                val str = s.toString()
                binding.button.isEnabled = str.isNotEmpty()
            }
        })
        */

        /*
        //2. TextWatcher 객체에 addTextChangedListener()을 이용해서 해당하는 3개의 메서드 모두 정의된 상태로 시작
        //필요한 것만 overriding하기
        binding.editText.addTextChangedListener(
            afterTextChanged = {
                val str = it.toString()
                binding.button.isEnabled = str.isNotEmpty()
            }
        )
        */
        //3. TextWatcher 객체에서 addTextChangedListener{}는 afterTextChanged{}의 정의와 동일
        //afterTextChanged{}라고 생각하고 body 작성(afterTextChanged을 제일 많이 사용함)
        binding.editText.addTextChangedListener {
            val str = it.toString()
            binding.button.isEnabled = str.isNotEmpty()
        }
        binding.button.setOnClickListener {
            adapter1.add(binding.editText.text.toString())
            //adapterView에 data 추가 -> adapter에 add하기, 해당 자료구조에 add X
            adapter1.notifyDataSetChanged()
            //data set이 변경되었을 때 화면에 보여주는 내용을 갱신
            binding.editText.text.clear()
            //editText 비우기
        }
    }
}