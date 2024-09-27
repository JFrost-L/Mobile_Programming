package com.frost.ex02communicationfragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.frost.ex02communicationfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val myViewModel:MyViewModel by viewModels()//ktx library를 dependency에 추가해서 이용!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        myViewModel.setLiveData(0)//처음 상태가 첫 이미지로 매핑
        
        val fragment=supportFragmentManager.beginTransaction()
        val imageFragment = ImageFragment()
        fragment.replace(R.id.frameLayout, imageFragment)//fragment 변화
        fragment.commit()
    }
}