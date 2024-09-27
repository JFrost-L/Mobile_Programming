package com.frost.ex02

import android.graphics.Point
import android.graphics.PointF
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.core.graphics.minus
import com.frost.ex02.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var coorDown: Float = 0.0f
    var coorUp: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
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

