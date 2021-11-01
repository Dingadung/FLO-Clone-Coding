package com.example.flo

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK
import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var player: MainActivity.Player
    private val handler = Handler(Looper.getMainLooper())
    private var song: Song = Song()
    private lateinit var getResult: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, SongActivity::class.java)

        song = Song(
            binding.mainMiniplayerTitleTv.text.toString(),
            binding.mainMiniplayerSingerTv.text.toString(),
            213,
            false
        )

        getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data!!.getBooleanExtra("isPlayingSong", true)) {
                        song.isPlaying = true
                        setPlayerStatus(song.isPlaying)
                    } else {
                        song.isPlaying = false
                        setPlayerStatus(song.isPlaying)
                    }
                    song.currentTime = data.getIntExtra("currentTime", 0)
                    binding.mainLineSb.progress = (song.currentTime * 1000) / song.playTime
                    song.isOneSongPlaying = data.getBooleanExtra("isOneSongPlaying", false)
                    Log.d("Intent Main currentTime", "Song to Main" + song.currentTime.toString())
                }
            }


        binding.mainPlayerLayout.setOnClickListener {
            // 안에 정보를 담으려고 위의 코드를 나누어서 썼음.
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("currentTime", song.currentTime)
            getResult.launch(intent)
        }


        player = Player(song.playTime)
        player.start()


        // SeekBar
        binding.mainLineSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //시크바 상태 변화시
                Log.d("Progress Main1", "이 코드 실행됨, " + progress.toString())
                song.currentTime = progress * song.playTime / 1000
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //시크바 터치시
                Log.d("Progress Main2", "이 코드 실행됨, 시작")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //시크바 손 뗄때
                Log.d("Progress Main3", "이 코드 실행됨, 끝!")
            }
        })


        // 기본
        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            song.isPlaying = true
            setPlayerStatus(song.isPlaying)
        }
        binding.mainPauseBtn.setOnClickListener {
            song.isPlaying = false
            setPlayerStatus(song.isPlaying)
        }


    }


    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            //isPlaying 이 true 인 상황: 정지 버튼이 보이는 상황에서 재생 버튼으로 변경
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }


    inner class Player(private val playTime: Int) : Thread() {

        override fun run() {
            try {
                while (true) {
                    if (song.isOneSongPlaying) { //1곡 재생인 경우
                        if (song.isPlaying) {
                            sleep(1000) //1초뒤에 second 1추가
                            song.currentTime++
                            if (song.currentTime > playTime) {
                                song.currentTime = 0
                            }
                            runOnUiThread {
                                //뷰렌더링
                                binding.mainLineSb.progress = song.currentTime * 1000 / playTime
                            }
                        }
                    } else {
                        if (song.currentTime >= playTime) {
                            break
                        }
                        if (song.isPlaying) {
                            sleep(1000) //1초뒤에 second 1추가
                            song.currentTime++
                            runOnUiThread {
                                //뷰렌더링
                                binding.mainLineSb.progress = (song.currentTime * 1000) / playTime
                            }
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Interrupt", "스레드가 종료되었습니다.")
            }

        }
    }

    override fun onDestroy() {
        //화면이 꺼지면 실행되는 부분
        player.interrupt()
        intent.putExtra("currentPlayTime", song.currentTime)
        super.onDestroy()
    }
}

