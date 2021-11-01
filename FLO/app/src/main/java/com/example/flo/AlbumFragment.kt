package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding

    val information = arrayListOf("수록곡", "상세정보", "영상")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumArrowIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }

        binding.albumMyLikeOffIv.setOnClickListener {
            setLikeStatus(false)
        }

        binding.albumMyLikeOffIv.setOnClickListener {
            setLikeStatus(true)
        }

        val albumAdapter = AlbumViewPagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTl, binding.albumContentVp){ // 텝 레이아웃에 어떠한 텍스트가 들어가야하는지 정해줘야함
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }



    fun setLikeStatus(isLike : Boolean){
        if(isLike){
            //isPlaying 이 true 인 상황: 정지 버튼이 보이는 상황에서 재생 버튼으로 변경
            binding.albumMyLikeOffIv.visibility=View.VISIBLE
            binding.albumMyLikeOnIv.visibility=View.GONE
        }
        else{
            //isPlaying 이 false 인 상황: 재생 버튼이 보이는 상황에서 정지 버튼으로 변경
            binding.albumMyLikeOffIv.visibility=View.INVISIBLE
            binding.albumMyLikeOnIv.visibility=View.VISIBLE
        }
    }
}