package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongFilesBinding
import com.example.flo.databinding.FragmentVideoBinding

class SongFileFragment : Fragment() {
    lateinit var binding : FragmentSongFilesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongFilesBinding.inflate(inflater, container, false)
        return binding.root
    }
}