package com.frost.ex02pendingintent

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.frost.ex02pendingintent.databinding.ActivityMainBinding
import com.frost.ex02pendingintent.databinding.MytimepcikerdlgBinding
import java.util.*

class MainActivity : AppCompatActivity(), OnTimeSetListener {
    lateinit var binding: ActivityMainBinding
    var msg:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initLayout()
        initLayout2()
    }

    private fun initLayout2() {
        binding.calendarView.setOnDateChangeListener { view,
                                                       year, month, dayOfMonth ->
            msg = "${year}년 ${month+1}월 ${dayOfMonth}일"
            //월은 0부터 시작
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val dlgBinding = MytimepcikerdlgBinding.inflate(layoutInflater)
            dlgBinding.timerPicker.hour = hour
            dlgBinding.timerPicker.minute = minute

            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgBinding.root)
            //내가 만든 레이아웃을 기반으로 다이얼로그 빌더 생성
                .setPositiveButton("추가"){_,_ ->
                    msg += " ${dlgBinding.timerPicker.hour}시 ${dlgBinding.timerPicker.minute}분 => "
                    msg += dlgBinding.message.text.toString()
                    //메세지 결합하기
                    val timerTask = object:TimerTask(){
                        override fun run() {
                            makeNotification()
                        }
                    }
                    val timer = Timer()
                    timer.schedule(timerTask, 2000)
                    //2초후에 run()을 실행
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소"){ _, _ ->

                }
                .show()
        }
    }

    private fun initLayout() {
        binding.calendarView.setOnDateChangeListener { view,
                                                       year, month, dayOfMonth ->
            msg = "${year}년 ${month+1}월 ${dayOfMonth}일"
            //월은 0부터 시작
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, this, hour, minute, true)
            //두 번째 인자인 listener는 해당 클래스에서 interface를 상속받아서 this로 유지
            timePicker.show()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //시간 정보 세팅하면 발생하는 콜백함수
        if(view!=null){
            msg += " ${hourOfDay}시 ${minute}분"

            val timerTask = object:TimerTask(){
                override fun run() {
                    makeNotification()
                }
            }
            val timer = Timer()
            timer.schedule(timerTask, 2000)
            //2초후에 run()을 실행
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeNotification() {
        val id = "MyChannel"
        val name = "TimeCheckChannel"
        
        //채널 객체
        val notificationChannel = NotificationChannel(id, name,
        NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        
        //빌더 객체
        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.baseline_access_alarm_24)//이건 필수
            .setContentTitle("일정 알림")
            .setContentText(msg)
            .setAutoCancel(true)
        
        //넘어갈 인텐트 생성 및 정보 설정
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("time",msg)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        //팬딩 인탠트 만들어서 builder에 인텐트 세팅
        val pendingIntent = PendingIntent.getActivity(this, 1,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        val notification = builder.build()

        //매니저 객체
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10, notification)
    }
}