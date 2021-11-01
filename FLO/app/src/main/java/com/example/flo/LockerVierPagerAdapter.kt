package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVierPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 //들어갈 프래그먼트 3개

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StoredSongFragment()//저장한 곡 프래그먼트
            else -> SongFileFragment()//음악파일 프래그먼트
        }
    }
}