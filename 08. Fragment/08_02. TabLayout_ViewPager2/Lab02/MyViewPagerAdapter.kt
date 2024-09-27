package com.frost.lab02tablayout_viewpager2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(fragmentActivity: MainActivity) : FragmentStateAdapter(fragmentActivity) {
    //부모 클래스에 Activity 정보를 인자로 전달해줘야함
    
    //추상클래스 메서드 오버라이딩
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ImageFragment()
            1 -> ItemFragment()
            2 -> TeamFragment()
            else -> ImageFragment()
        }
    }

}