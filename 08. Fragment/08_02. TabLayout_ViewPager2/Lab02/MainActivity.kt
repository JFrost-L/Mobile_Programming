package com.frost.lab02tablayout_viewpager2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.lab02tablayout_viewpager2.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    val textArray = arrayListOf<String>("이미지", "리스트", "팀 소개")
    val imgArray = arrayListOf<Int>(R.drawable.baseline_hail_24, R.drawable.baseline_hiking_24
    , R.drawable.baseline_cookie_24)
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewpager){
            tab, position ->
            tab.text = textArray[position]
            tab.setIcon(imgArray[position])
        }//세 번째 인자는 추상클래스인데 그 클래스에는 구현해야하는 메서드가 1개라서 그냥 {}로 body 정의해서 인자 전달
            .attach()
    }
}