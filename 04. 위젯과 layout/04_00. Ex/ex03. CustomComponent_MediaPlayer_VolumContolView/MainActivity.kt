package com.frost.ex03customcomponent_mediaplayer_volumcontolview

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.ex03customcomponent_mediaplayer_volumcontolview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var mediaPlayer: MediaPlayer?=null//0~1사이의 값
    var vol = 0.5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initLayout()

    }

    private fun initLayout() {
        binding.apply { //binding 생략 가능
            imageView.setVolumeListener(object:VolumeControlView.VolumeListener{
                override fun onChanged(angle: Float) {
                    vol= if(angle>=0){ //값에 맞게 변환
                        angle/360
                    }else{
                        (360+angle)/360
                    }
                    mediaPlayer?.setVolume(vol, vol)
                }
            })
            playBtn.setOnClickListener{
                if(mediaPlayer==null){
                    mediaPlayer=MediaPlayer.create(this@MainActivity, R.raw.song)
                    //미디어 플레이어 객체 생성
                    //그냥 this는 ActivityMainBinding 객체의 this를 의미(해당 객체 내부 -> apply로 묶어서)
                    //MainActivity의 this를 이용해야함(MainActivity는 context를 상속받아서 만든 클래스)
                    mediaPlayer?.setVolume(vol, vol)
                }
                mediaPlayer?.start()
            }
            pauseBtn.setOnClickListener{
                mediaPlayer?.pause()//null이 아니면 pause
            }
            stopBtn.setOnClickListener{
                mediaPlayer?.stop()//null이 아니면 stop
                mediaPlayer?.release()//객체에서 해제
                mediaPlayer=null//다시 play하기 위해서 일단 null로 초기화
            }
        }
    }
}