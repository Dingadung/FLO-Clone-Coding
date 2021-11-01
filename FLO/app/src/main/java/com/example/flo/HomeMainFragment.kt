package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding
import com.example.flo.databinding.FragmentHomeMainBinding

class HomeMainFragment(val imgRes : Int, val smallImgRes1 : Int, val smallImgRes2: Int, val titleText : String) : Fragment() {
    lateinit var binding: FragmentHomeMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeMainBinding.inflate(inflater, container, false)

        binding.homeBackgroundIv.setImageResource(imgRes)
        binding.homeAlbumExp2Iv.setImageResource(smallImgRes1)
        binding.homeAlbumExpIv.setImageResource(smallImgRes2)
        binding.homeTitleTv.text = titleText

        return binding.root
    }
}