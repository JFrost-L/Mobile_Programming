package com.frost.lab_assignment01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.frost.lab_assignment01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var checkBoxArr:ArrayList<CheckBox>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init(){
        checkBoxArr = arrayListOf(binding.check1, binding.check2, binding.check3, binding.check4,
            binding.check5, binding.check6, binding.check7, binding.check8, binding.check9, binding.check10)
        for(checkbox in checkBoxArr){
            checkbox.setOnCheckedChangeListener { button, isChecked ->
                val imageId = resources.getIdentifier(button.text.toString(), "id",packageName)
                val imageView = findViewById<ImageView>(imageId)
                if(isChecked){
                    imageView.visibility= View.VISIBLE
                }else{
                    imageView.visibility = View.INVISIBLE
                }
            }
        }
    }
}