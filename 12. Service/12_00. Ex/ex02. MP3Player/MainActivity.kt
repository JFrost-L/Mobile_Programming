package com.frost.ex02mp3player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.frost.ex02mp3player.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var songlist:Array<String>
    lateinit var song:String

    var runThread=false
    var thread:ProgressThread?=null

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
        registerReceiver(receiver, IntentFilter("com.example.MP3ACTIVITY"))

        val intent = Intent(this, MyService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun stopPlay() {
        val serviceBRIntent = Intent("com.example.MP3SERVICE")
        serviceBRIntent.putExtra("mode", "stop")
        sendBroadcast(serviceBRIntent)

        runThread = false
        binding.progressBar.progress=0
    }

    private fun startPlay() {
        val serviceBRIntent = Intent("com.example.MP3SERVICE")
        serviceBRIntent.putExtra("mode", "play")
        serviceBRIntent.putExtra("song",song)
        sendBroadcast(serviceBRIntent)

        runThread=true
        if(thread==null || !thread!!.isAlive){
            binding.progressBar.progress=0
            thread = ProgressThread()
            thread!!.start()
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

    var receiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val mode = intent!!.getStringExtra("mode")
            if(mode!=null){
                when(mode){
                    "play"->{
                        binding.progressBar.progress=0
                        binding.progressBar.max = intent.getIntExtra("duration", -1)
                    }
                    "stop"->{
                        runThread = false
                        binding.progressBar.progress=0
                    }
                    "playing"->{
                        runThread=true
                        binding.progressBar.max = intent.getIntExtra("duration",-1)
                        binding.progressBar.progress = intent.getIntExtra("currentPos",-1)
                        song = intent.getStringExtra("song")!!
                        if(thread==null || !thread!!.isAlive){
                            thread = ProgressThread()
                            thread!!.start()
                        }
                    }
                }
            }
        }
    }
}