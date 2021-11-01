package com.example.flo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    private lateinit var banner: HomeFragment.Banner
    private lateinit var homeMainAdapter: HomeMainViewPagerAdapter
    private val handler = Handler(Looper.getMainLooper())
    var currentPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeBtnAlbum01Iv.clipToOutline = true
        binding.homeBtnAlbum02Iv.clipToOutline = true
        binding.homeBtnAlbum03Iv.clipToOutline = true
        binding.homeBtnRecommendAlbum01Iv.clipToOutline = true
        binding.homeBtnRecommendAlbum02Iv.clipToOutline = true
        binding.homeBtnRecommendAlbum03Iv.clipToOutline = true
        binding.homeBtnVideoCollection01Iv.clipToOutline = true
        binding.homeBtnVideoCollection02Iv.clipToOutline = true

        binding.homeBtnAlbum01Iv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
        }

        val bannerAdapter = BannerViewPagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner3))

        binding.homeAdvBannerVp.adapter = bannerAdapter
        binding.homeAdvBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        homeMainAdapter = HomeMainViewPagerAdapter(this)
        homeMainAdapter.addFragment(
            HomeMainFragment(
                R.drawable.img_default_4_x_1,
                R.drawable.aespa,
                R.drawable.img_album_exp,
                "한밤의 북악 스카이웨이\n드라이브 감성 뮤직"
            )
        )
        homeMainAdapter.addFragment(
            HomeMainFragment(
                R.drawable.love,
                R.drawable.img_album_exp2,
                R.drawable.aespa,
                "자기 전에 듣는 센티한 힙합"
            )
        )
        homeMainAdapter.addFragment(
            HomeMainFragment(
                R.drawable.saruru,
                R.drawable.img_album_exp,
                R.drawable.aespa,
                "센티해질 때 듣는 센티한 힙합"
            )
        )
        homeMainAdapter.addFragment(
            HomeMainFragment(
                R.drawable.img_default_4_x_1,
                R.drawable.img_album_exp2,
                R.drawable.img_album_exp,
                "이별 후 홀로서는 그대에게"
            )
        )

        binding.homeBackgroundVp.adapter = homeMainAdapter
        binding.homeBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.springDotsIndicator.setViewPager2(binding.homeBackgroundVp)

        banner = Banner()
        banner.start()


        return binding.root
    }

    inner class Banner() : Thread() {
        override fun run() {
            try {
                while (true) {
                    Thread.sleep(2500)
                    currentPosition++
                    if (currentPosition == homeMainAdapter.itemCount) currentPosition = 0
                    handler.post {
                        binding.homeBackgroundVp.setCurrentItem(currentPosition, true)
                        true
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Interrupt", "Home Fragment 스레드가 종료되었습니다.")
            }
        }


    }

    override fun onDestroy() {
        //화면이 꺼지면 실행되는 부분
        banner.interrupt()
        super.onDestroy()
    }
}
