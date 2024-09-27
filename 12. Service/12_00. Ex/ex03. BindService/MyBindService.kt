package com.frost.ex03bindservice
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MyBindService : Service() {
    lateinit var song:String
    var player: MediaPlayer?=null
    val binder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }
    inner class MyBinder: Binder(){
        fun getService():MyBindService = this@MyBindService
    }
    fun getMaxDuration():Int{
        if(player!=null){
            return player!!.duration
        }else{
            return 0
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(player!=null&&player!!.isPlaying){
            val mainBRIntent = Intent("com.example.MP3ACTIVITY")
            mainBRIntent.putExtra("mode", "playing")
            mainBRIntent.putExtra("song",song)
            mainBRIntent.putExtra("currentPos",player!!.currentPosition)
            mainBRIntent.putExtra("duration",player!!.duration)
            sendBroadcast(mainBRIntent)
        }
        return START_STICKY
    }

    fun startPlay(sname:String){
        song = sname
        val songid = resources.getIdentifier(song, "raw", packageName)
        if (player != null && player!!.isPlaying) {
            player!!.stop()
            player!!.reset()
            player!!.release()
            player = null
        }

        player = MediaPlayer.create(this, songid)
        player!!.start()

        val mainBRIntent = Intent("com.example.MP3ACTIVITY")
        mainBRIntent.putExtra("mode", "play")
        mainBRIntent.putExtra("duration",player!!.duration)
        sendBroadcast(mainBRIntent)

        player!!.setOnCompletionListener {
            val mainBRIntent = Intent("com.example.MP3ACTIVITY")
            mainBRIntent.putExtra("mode", "stop")
            sendBroadcast(mainBRIntent)
            stopPlay(song)
        }
    }

    fun stopPlay(song: String) {
        if(player!=null && player!!.isPlaying){
            player!!.stop()
            player!!.reset()
            player!!.release()
            player=null
        }
    }
}