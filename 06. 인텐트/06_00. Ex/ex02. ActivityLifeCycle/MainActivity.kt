package com.frost.ex02activitylifecycle

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.ex02activitylifecycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var mediaPlayer: MediaPlayer?=null//0~1사이의 값
    var vol = 0.5f
    var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()

    }
    //lifeStyle 조작
    override fun onResume() {
        //running 진입 전에 자동 호출 
        super.onResume()
        if(flag)//이전에 mediaPlayer가 pause였다가 running이 되었을 때의 상태 반영
            mediaPlayer?.start()
    }

    override fun onPause() {
        //running 탈출 전에 자동 호출
        super.onPause()
        mediaPlayer?.pause()
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
                    mediaPlayer= MediaPlayer.create(this@MainActivity, R.raw.song)
                    //미디어 플레이어 객체 생성
                    //그냥 this는 ActivityMainBinding 객체의 this를 의미(해당 객체 내부 -> apply로 묶어서)
                    //MainActivity의 this를 이용해야함(MainActivity는 context를 상속받아서 만든 클래스)
                    mediaPlayer?.setVolume(vol, vol)
                }
                mediaPlayer?.start()
                flag=true
            }
            pauseBtn.setOnClickListener{
                mediaPlayer?.pause()//null이 아니면 pause
                flag=false
            }
            stopBtn.setOnClickListener{
                mediaPlayer?.stop()//null이 아니면 stop
                mediaPlayer?.release()//객체에서 해제
                mediaPlayer=null//다시 play하기 위해서 일단 null로 초기화
                flag=false
            }
        }
    }
}