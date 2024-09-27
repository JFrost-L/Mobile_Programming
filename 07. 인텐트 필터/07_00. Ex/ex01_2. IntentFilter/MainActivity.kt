package com.frost.myappexample

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.frost.myappexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            getAppListAction()
        }
    }
    fun getAppListAction(){
        val intent = Intent("com.frost.myapplist")

        when{
            ActivityCompat.checkSelfPermission(this,
            "com.frost.ex01intentfilter")==PackageManager.PERMISSION_GRANTED-> {
                startActivity(intent)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
            "com.frost.ex01intentfilter")->{
                appListAlertDlg()
            }
            else ->{
                requestPermissionLauncher.launch("com.frost.ex01intentfilter")
            }
        }
    }
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            //수행하고픈 콜백함수 등록
            if(it){//허용되면 실행
                Toast.makeText(this, "권한 승인이 승인되었습니다.", Toast.LENGTH_SHORT).show()
            }else{//거부되면 메세지
                Toast.makeText(this, "권한 승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    private fun appListAlertDlg() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 CALL_PHONE 권한이 허용되어야 합니다.")
            .setTitle("권한체크")
            .setPositiveButton("OK"){//허용 버튼 누를 시
                    _, _ ->
                requestPermissionLauncher.launch("com.frost.ex01intentfilter")
            }.setNegativeButton("Cancel"){
                    dlg, _ ->
                dlg.dismiss()//다이얼로그 종료
            }
        builder.create().show()//builder 객체인 다이얼로그를 만들고 출력
    }
}