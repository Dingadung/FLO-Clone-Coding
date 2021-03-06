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
            // ?????? ????????? ???????????? ?????? ????????? ???????????? ??????.
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
                //????????? ?????? ?????????
                Log.d("Progress Main1", "??? ?????? ?????????, " + progress.toString())
                song.currentTime = progress * song.playTime / 1000
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //????????? ?????????
                Log.d("Progress Main2", "??? ?????? ?????????, ??????")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //????????? ??? ??????
                Log.d("Progress Main3", "??? ?????? ?????????, ???!")
            }
        })


        // ??????
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
            //isPlaying ??? true ??? ??????: ?????? ????????? ????????? ???????????? ?????? ???????????? ??????
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
                    if (song.isOneSongPlaying) { //1??? ????????? ??????
                        if (song.isPlaying) {
                            sleep(1000) //1????????? second 1??????
                            song.currentTime++
                            if (song.currentTime > playTime) {
                                song.currentTime = 0
                            }
                            runOnUiThread {
                                //????????????
                                binding.mainLineSb.progress = song.currentTime * 1000 / playTime
                            }
                        }
                    } else {
                        if (song.currentTime >= playTime) {
                            break
                        }
                        if (song.isPlaying) {
                            sleep(1000) //1????????? second 1??????
                            song.currentTime++
                            runOnUiThread {
                                //????????????
                                binding.mainLineSb.progress = (song.currentTime * 1000) / playTime
                            }
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Interrupt", "???????????? ?????????????????????.")
            }

        }
    }

    override fun onDestroy() {
        //????????? ????????? ???????????? ??????
        player.interrupt()
        intent.putExtra("currentPlayTime", song.currentTime)
        super.onDestroy()
    }
}

