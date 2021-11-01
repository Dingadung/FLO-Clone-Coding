package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeMainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentlist: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentlist.size //들어갈 프래그먼트 3개

    override fun createFragment(position: Int) = fragmentlist[position]

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)
    }

}