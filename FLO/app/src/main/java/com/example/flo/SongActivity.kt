package com.example.flo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    private val song: Song = Song()
    private lateinit var player:Player
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        player = Player(song.playTime)
        player.start()


        binding.songAlbumImgIv.clipToOutline = true

        binding.songNuguDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniPlayIb.setOnClickListener {
            song.isPlaying=true
            setPlayerStatus(song.isPlaying)
        }

        binding.songMiniPauseIb.setOnClickListener {
            song.isPlaying=false
            setPlayerStatus(song.isPlaying)
        }

        binding.songNuguRepeatInactiveIb.setOnClickListener {
            setRepeatStatusSeq(0)
        }
        binding.songNuguRepeatOn1Ib.setOnClickListener {
            setRepeatStatusSeq(1)
        }
        binding.songNuguRepeatOnIb.setOnClickListener {
            setRepeatStatusSeq(2)
        }

        binding.songNuguRandomInactiveIb.setOnClickListener {
            setRandomStatus(false)
        }

        binding.songNuguRandomOnIb.setOnClickListener {
            setRandomStatus(true)
        }

        binding.songMyLikeOffIb.setOnClickListener {
            setLikeStatus(false)
        }

        binding.songMyLikeOnIb.setOnClickListener {
            setLikeStatus(true)
        }

        binding.songPlayerUnlikeOffIb.setOnClickListener {
            setUnlikeStatus(false)
        }
        binding.songPlayerUnlikeOnIb.setOnClickListener {
            setUnlikeStatus(true)
        }

        //////////////SeekBar
        binding.songLineSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //????????? ?????? ?????????
                Log.d("Progress Song1", "??? ?????? ?????????, "+progress.toString())
                song.progress = progress
                song.currentTime = progress*song.playTime /1000
                binding.songStartTimeTv.text =
                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //????????? ?????????
                song.currentTime = song.progress*song.playTime /1000
                binding.songStartTimeTv.text =
                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
                Log.d("Progress Song2", "??? ?????? ?????????, ??????")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //????????? ??? ??????
                Log.d("Progress Song3", "??? ?????? ?????????, ???!")
            }
        })

    }

    private fun initSong(){
        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")
            && intent.hasExtra("currentTime")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.currentTime = intent.getIntExtra("currentTime", 0)


            binding.songLineSb.progress = song.currentTime*1000/song.playTime
            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
            setPlayerStatus(song.isPlaying)
            binding.songStartTimeTv.text =
                String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            //isPlaying ??? true ??? ??????: ?????? ????????? ????????? ???????????? ?????? ???????????? ??????
            binding.songMiniPlayIb.visibility = View.GONE
            binding.songMiniPauseIb.visibility = View.VISIBLE
        } else {

            binding.songMiniPlayIb.visibility = View.VISIBLE
            binding.songMiniPauseIb.visibility = View.GONE
        }
        intent.putExtra("isPlayingSong", isPlaying)
        setResult(Activity.RESULT_OK, intent)
    }

    fun setRepeatStatusSeq(seq: Int) {
        if (seq == 0) {
            song.isOneSongPlaying=true
            binding.songNuguRepeatInactiveIb.visibility = View.INVISIBLE
            binding.songNuguRepeatOn1Ib.visibility = View.VISIBLE
        } else if (seq == 1) {
            song.isOneSongPlaying=false
            binding.songNuguRepeatOnIb.visibility = View.VISIBLE
            binding.songNuguRepeatOn1Ib.visibility = View.GONE
        } else {
            binding.songNuguRepeatOnIb.visibility = View.GONE
            binding.songNuguRepeatInactiveIb.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isRandom: Boolean) {
        if (isRandom) {
            //isRandom ??? true ??? ??????
            binding.songNuguRandomOnIb.visibility = View.GONE
        } else {
            binding.songNuguRandomInactiveIb.visibility = View.INVISIBLE
            binding.songNuguRandomOnIb.visibility = View.VISIBLE
        }
    }


    fun setLikeStatus(isLike: Boolean) {
        if (isLike) {
            binding.songMyLikeOffIb.visibility = View.VISIBLE
            binding.songMyLikeOnIb.visibility = View.GONE

        } else {
            binding.songMyLikeOffIb.visibility = View.INVISIBLE
            binding.songMyLikeOnIb.visibility = View.VISIBLE

        }

    }

    fun setUnlikeStatus(isUnlike: Boolean) {
        if (isUnlike) {
            binding.songPlayerUnlikeOffIb.visibility = View.VISIBLE
            binding.songPlayerUnlikeOnIb.visibility = View.GONE
        } else {
            //isPlaying ??? false ??? ??????: ?????? ????????? ????????? ???????????? ?????? ???????????? ??????
            binding.songPlayerUnlikeOffIb.visibility = View.INVISIBLE
            binding.songPlayerUnlikeOnIb.visibility = View.VISIBLE
        }
    }


    inner class Player(private val playTime:Int) : Thread(){
        //private var second=0

        override fun run() {
            try{
                while(true) {
                    if(song.isOneSongPlaying){ //1??? ????????? ??????
                        if(song.isPlaying){
                            sleep(1000) //1????????? second 1??????
                            song.currentTime++
                            if(song.currentTime>playTime){
                                song.currentTime=0
                            }
                            runOnUiThread {
                                //????????????
                                binding.songLineSb.progress = song.currentTime*1000/playTime
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
                            }
                        }
                    }

                    else{ //1??? ????????? ?????? ??????
                        if(song.currentTime>=playTime){
                            break
                        }
                        if(song.isPlaying){
                            sleep(1000) //1????????? second 1??????
                            song.currentTime++
                            runOnUiThread {
                                //????????????
                                binding.songLineSb.progress = song.currentTime*1000/playTime
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
                            }
                        }
                    }

                    intent.putExtra("isOneSongPlaying", song.isOneSongPlaying)
                    intent.putExtra("currentTime", song.currentTime)
                    Log.d("Intent SongcurrentTime", "??? ?????? ?????????,"+song.currentTime.toString())
                }
            }catch(e: InterruptedException){
                setResult(Activity.RESULT_OK, intent)
                Log.d("Interrupt", "???????????? ?????????????????????.")
            }
        }
    }

    override fun onDestroy() {
        //????????? ????????? ???????????? ??????
        player.interrupt()
        super.onDestroy()
    }
}