package com.frost.ex01service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.frost.ex01service.databinding.ActivityMainBinding

class MyService : Service() {
    //manifest에 무조건 등록되어 있어야 함
    var thread:Thread?=null
    var num =0
    override fun onCreate() {
        super.onCreate()
        Log.i("MyService", "onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MyService", "onStartCommand()")
        if(thread==null){
            thread = object:Thread("MyThread"){
                override fun run() {
                    try{
                        for(i in 0..10){
                            Log.i("MyThread $num", "Count : $i")
                            Thread.sleep(1000)
                        }
                    }catch(e:InterruptedException){
                        Thread.currentThread().interrupt()
                    }
                    thread=null
                }
            }
            thread!!.start()
            num++
        }

        return START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyService", "onDestroy()")
        if(thread !=null){
            thread!!.interrupt()
        }
        thread=null
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}