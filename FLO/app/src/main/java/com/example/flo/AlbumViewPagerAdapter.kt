package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewPagerAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 //들어갈 프래그먼트 3개

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> SongFragment()//수록곡 프래그먼트
            1 -> DetailFragment()//상세정보 프래그먼트
            else -> VideoFragment()//영상 프래그먼트
        }
    }
}