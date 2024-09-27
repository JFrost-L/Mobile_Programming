package com.frost.ex02communicationfragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.frost.ex02communicationfragment.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    //viewBinding이 조금 다름 (activity&view와 lifecycle이 다르기에 메모리 누수 방지)
    var binding: FragmentImageBinding?=null
    //viewModel 객체로 값 주고 받기
    val model:MyViewModel by activityViewModels()
    val imgList = arrayListOf<Int>(R.drawable.img1, R.drawable.img2, R.drawable.img3)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(layoutInflater, container, false)
        return binding!!.root//null 값 아닌 것을 보장해야함
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view 생성시 초기화하기
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            imageView.setOnClickListener{
                val fragment=requireActivity().supportFragmentManager.beginTransaction()
                //requireActivity()를 통해서 activity 정보를 access
                fragment.addToBackStack(null)
                val textFragment = TextFragment()
                fragment.replace(R.id.frameLayout, textFragment)//fragment 변화
                fragment.commit()
            }
            radioGroup.setOnCheckedChangeListener{ group, checkedId ->
                when(checkedId){
                    R.id.radioButton1 -> {
                        model.setLiveData(0)
                    }
                    R.id.radioButton2 -> {
                        model.setLiveData(1)
                    }
                    R.id.radioButton3 -> {
                        model.setLiveData(2)
                    }
                }
                imageView.setImageResource(imgList[model.selectedNum.value!!])
            }
        }
    }

    override fun onDestroyView() {
        //destroy가 되면 binding을 null로 만들어서 가비지 컬렉터가 메모리 누수 방지하도록 설정
        super.onDestroyView()
        binding=null
    }
}