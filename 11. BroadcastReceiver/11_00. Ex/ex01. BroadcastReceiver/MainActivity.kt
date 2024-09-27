package com.frost.ex01broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.frost.ex01broadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var broadcastReceiver:BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermission()
        initLayout()
        //getMessage(intent)
        checkSettingOverlayWindow(intent)
        //activity가 처음부터 create될 때 실행되는 경우
    }

    override fun onNewIntent(intent: Intent?) {
        //activity가 이미 생성되어 있고 실행중인 경우
        super.onNewIntent(intent)
        checkSettingOverlayWindow(intent)
    }
    fun getMessage(intent:Intent?){
        if(intent!=null){
            if(intent.hasExtra("msgSender") and intent.hasExtra("msgBody")){//확인
                val msgSender = intent.getStringExtra("msgSender")
                val msgBody = intent.getStringExtra("msgBody")
                Toast.makeText(this, "보낸 번호 : "+msgSender+"\n"+ msgBody, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun checkSettingOverlayWindow(intent:Intent?){
        if(Settings.canDrawOverlays(this)){
            getMessage(intent)
        }else{
            val overlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)//화면 전환
            requestSettingLauncher.launch(overlayIntent)
        }
    }

    val requestSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(Settings.canDrawOverlays(this)){
                getMessage(this.intent)
            }else{
                Toast.makeText(this, "권한 승인 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                initPermission()
            }else{
                Toast.makeText(this, "권한 승인 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun permissionAlertDlg() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 문자 수신 권한이 허용되어야 합니다.")
            .setTitle("권한 체크")
            .setPositiveButton("OK"){//허용 버튼 누를 시
                    _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            }.setNegativeButton("Cancel"){
                    dlg, _ ->
                dlg.dismiss()//다이얼로그 종료
            }
        builder.create().show()//builder 객체인 다이얼로그를 만들고 출력
    }

    private fun initPermission() {
        when{
            (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)->{
                Toast.makeText(this, "문자 수신 동의함", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
            android.Manifest.permission.RECEIVE_SMS)->{
                permissionAlertDlg()
            }else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            }
        }
    }

    private fun initLayout() {
        broadcastReceiver = object:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!=null){
                    if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)){
                        Toast.makeText(context, "충전 시작", Toast.LENGTH_SHORT).show()
                    }else if(intent.action.equals(Intent.ACTION_POWER_DISCONNECTED)){
                        Toast.makeText(context, "충전 해제", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        //메세지 수신
        super.onResume()
        val intentFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        //충전기에 꽂을 때의 정보 수신
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        //충전기에 뺐을 때 정보 수신
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        //메세지 수신 해제
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }
}