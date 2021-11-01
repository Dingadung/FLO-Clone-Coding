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
                //시크바 상태 변화시
                Log.d("Progress Song1", "이 코드 실행됨, "+progress.toString())
                song.progress = progress
                song.currentTime = progress*song.playTime /1000
                binding.songStartTimeTv.text =
                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //시크바 터치시
                song.currentTime = song.progress*song.playTime /1000
                binding.songStartTimeTv.text =
                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
                Log.d("Progress Song2", "이 코드 실행됨, 시작")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //시크바 손 뗄때
                Log.d("Progress Song3", "이 코드 실행됨, 끝!")
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
            //isPlaying 이 true 인 상황: 정지 버튼이 보이는 상황에서 재생 버튼으로 변경
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
            //isRandom 이 true 인 상황
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
            //isPlaying 이 false 인 상황: 재생 버튼이 보이는 상황에서 정지 버튼으로 변경
            binding.songPlayerUnlikeOffIb.visibility = View.INVISIBLE
            binding.songPlayerUnlikeOnIb.visibility = View.VISIBLE
        }
    }


    inner class Player(private val playTime:Int) : Thread(){
        //private var second=0

        override fun run() {
            try{
                while(true) {
                    if(song.isOneSongPlaying){ //1곡 재생인 경우
                        if(song.isPlaying){
                            sleep(1000) //1초뒤에 second 1추가
                            song.currentTime++
                            if(song.currentTime>playTime){
                                song.currentTime=0
                            }
                            runOnUiThread {
                                //뷰렌더링
                                binding.songLineSb.progress = song.currentTime*1000/playTime
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
                            }
                        }
                    }

                    else{ //1곡 재생이 아닌 경우
                        if(song.currentTime>=playTime){
                            break
                        }
                        if(song.isPlaying){
                            sleep(1000) //1초뒤에 second 1추가
                            song.currentTime++
                            runOnUiThread {
                                //뷰렌더링
                                binding.songLineSb.progress = song.currentTime*1000/playTime
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)
                            }
                        }
                    }

                    intent.putExtra("isOneSongPlaying", song.isOneSongPlaying)
                    intent.putExtra("currentTime", song.currentTime)
                    Log.d("Intent SongcurrentTime", "이 코드 실행됨,"+song.currentTime.toString())
                }
            }catch(e: InterruptedException){
                setResult(Activity.RESULT_OK, intent)
                Log.d("Interrupt", "스레드가 종료되었습니다.")
            }
        }
    }

    override fun onDestroy() {
        //화면이 꺼지면 실행되는 부분
        player.interrupt()
        super.onDestroy()
    }
}