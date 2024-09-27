package com.frost.ex01staticfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.ex01staticfragment.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    lateinit var binding:ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}