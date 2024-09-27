package com.frost.ex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.frost.ex01.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)//해당 파일로부터 ContentView 설정
        //root는 constraint layout을 의미

        //init()
        init2()
    }
    fun init2() {
        //binding으로 위젯 객체 접근 가능
        binding.button.setOnClickListener {
            val bmi = binding.weight.text.toString().toDouble() / (binding.height.text.toString()
                .toDouble() / 100).pow(2.0)
            var resultString: String
            when {
                bmi >= 35 -> {
                    resultString = "고도 비만"
                    binding.imageView.setImageResource(R.drawable.baseline_sentiment_dissatisfied_24)
                }
                bmi > 23 -> {
                    resultString = "과체중"
                    binding.imageView.setImageResource(R.drawable.baseline_sentiment_neutral_24)
                }
                bmi > 18.5 -> {
                    resultString = "정상"
                    binding.imageView.setImageResource(R.drawable.baseline_sentiment_satisfied_24)
                }
                else -> {
                    resultString = "저체중"
                    binding.imageView.setImageResource(R.drawable.baseline_sentiment_dissatisfied_24)
                }
            }
        }
    }
    fun init(){
        val button = findViewById<Button>(R.id.button)
        val weight = findViewById<EditText>(R.id.weight)
        val height = findViewById<EditText>(R.id.height)
        val imageView = findViewById<ImageView>(R.id.imageView)

        button.setOnClickListener{
            val bmi = weight.text.toString().toDouble()/(height.text.toString().toDouble()/100).pow(2.0)
            var resultString:String
            when{
                bmi>=35 ->{
                    resultString="고도 비만"
                    imageView.setImageResource(R.drawable.baseline_sentiment_dissatisfied_24)
                }
                bmi>23 ->{
                    resultString="과체중"
                    imageView.setImageResource(R.drawable.baseline_sentiment_neutral_24)
                }
                bmi>18.5 ->{
                    resultString="정상"
                    imageView.setImageResource(R.drawable.baseline_sentiment_satisfied_24)
                }
                else ->{
                    resultString="저체중"
                    imageView.setImageResource(R.drawable.baseline_sentiment_dissatisfied_24)
                }
            }
            Toast.makeText(this, resultString,Toast.LENGTH_SHORT).show()
        }
    }
}