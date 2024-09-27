package com.frost.ex01log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val logTag = "MSGCHECK"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(logTag, "onCreate")
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Log.i(logTag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(logTag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(logTag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(logTag, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(logTag, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(logTag, "onDestroy")
    }
}