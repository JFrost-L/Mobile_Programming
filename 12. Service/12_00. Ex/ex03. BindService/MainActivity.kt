package com.frost.ex03bindservice

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import com.frost.ex03bindservice.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var songlist:Array<String>
    lateinit var song:String

    var runThread=false
    var thread:ProgressThread?=null

    lateinit var myBindService: MyBindService
    var mBound=false

    var connection = object:ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBindService.MyBinder
            myBindService = binder.getService()
            mBound=true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound=false
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        songlist= resources.getStringArray(R.array.songlist)
        song = songlist[0]

        binding.apply {
            listview.setOnItemClickListener { parent, view, position, id ->
                song = songlist[position]
                startPlay()
            }

            btnplay.setOnClickListener {
                startPlay()
            }

            btnstop.setOnClickListener {
                stopPlay()
            }
        }
        val intent = Intent(this, MyBindService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mBound){
            stopPlay()
            unbindService(connection)
        }
        mBound=false
    }


    private fun stopPlay() {
        if(mBound){
            myBindService.stopPlay(song)
        }
        runThread = false
        binding.progressBar.progress=0
    }

    private fun startPlay() {
         runThread=true
        if(thread==null || !thread!!.isAlive){
            if(mBound) {
                myBindService.startPlay(song)
                binding.progressBar.max=myBindService.getMaxDuration()
                binding.progressBar.progress = 0
                thread = ProgressThread()
                thread!!.start()
            }else{
                myBindService.stopPlay(song)
                myBindService.startPlay(song)
                binding.progressBar.max=myBindService.getMaxDuration()
                binding.progressBar.progress = 0
            }

        }
    }

    inner class ProgressThread:Thread(){
        override fun run() {
            while(runThread){
                binding.progressBar.incrementProgressBy(1000)
                //progressBar 1초씩 증가
                SystemClock.sleep(1000)
                if(binding.progressBar.progress == binding.progressBar.max) {//끝난 경우
                    runThread = false
                    binding.progressBar.progress=0
                }
            }
        }
    }
}