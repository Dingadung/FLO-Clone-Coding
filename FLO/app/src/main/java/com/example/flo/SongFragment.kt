package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.albumLilacLayoutCl.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()//앨범프래그먼트가 있는 곳은 액티비티 위
        }

        binding.albumToggleOffIb.setOnClickListener {
            setToggleStatus(false)
        }
        binding.albumToggleOnIb.setOnClickListener {
            setToggleStatus(true)
        }

        return binding.root
    }

    fun setToggleStatus(isToggleOn : Boolean){
        if(isToggleOn){
            //isRandom 이 true 인 상황
            binding.albumToggleOffIb.visibility=View.VISIBLE
            binding.albumToggleOnIb.visibility=View.GONE
        }
        else{
            binding.albumToggleOffIb.visibility=View.INVISIBLE
            binding.albumToggleOnIb.visibility=View.VISIBLE
        }
    }
}