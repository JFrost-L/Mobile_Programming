package com.frost.lab_assignment01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.lab_assignment01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.radioButton1.id ->{
                    binding.imageView.setImageResource(R.drawable.img1)
                }
                binding.radioButton2.id->{
                    binding.imageView.setImageResource(R.drawable.img2)
                }
                binding.radioButton3.id->{
                    binding.imageView.setImageResource(R.drawable.img3)
                }
            }
        }
    }
}