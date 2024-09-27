package com.frost.lab_assignment02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.frost.lab_assignment02.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var coorDown: Float = 0.0f
    var coorUp: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.imageView.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    coorDown = event.x!!
                }
                MotionEvent.ACTION_UP -> {
                    coorUp = event.x!!
                    if(coorDown>=coorUp){
                        when(binding.radioGroup.checkedRadioButtonId){
                            binding.radioButton1.id-> {
                                binding.imageView.setImageResource(R.drawable.img2)
                                binding.radioGroup.check(binding.radioButton2.id)
                            }
                            binding.radioButton2.id-> {
                                binding.imageView.setImageResource(R.drawable.img3)
                                binding.radioGroup.check(binding.radioButton3.id)

                            }
                        }
                    }else{
                        when(binding.radioGroup.checkedRadioButtonId){
                            binding.radioButton2.id-> {
                                binding.imageView.setImageResource(R.drawable.img1)
                                binding.radioGroup.check(binding.radioButton1.id)
                            }
                            binding.radioButton3.id-> {
                                binding.imageView.setImageResource(R.drawable.img2)
                                binding.radioGroup.check(binding.radioButton2.id)
                            }
                        }
                    }
                }
            }
            true // or false
        }
    }

    fun init() {
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