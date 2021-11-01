package com.example.flo

data class Song(
    var title:String="",
    var singer: String="",
    var playTime : Int = 0,
    var isPlaying : Boolean = false,
    var currentTime : Int =0,
    var isOneSongPlaying : Boolean = false,
    var progress : Int =0
)
