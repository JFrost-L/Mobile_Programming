package com.frost.lab01

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.frost.lab01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        checkPermissions()
    }
    val permissions = arrayOf(android.Manifest.permission.CALL_PHONE,
                                    android.Manifest.permission.CAMERA)
    //permissions 배열 설정

    val multiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){map ->
        //요청하고자 하는 복수의 권한 callback 함수를 매개변수로 등록 : ActivityResultContracts.RequestMultiplePermissions()
        //multiple이라서 매개변수가 map 타입으로 data가 들어옴
        val resultPermission = map.all{ entry ->
            //여기서 매개변수는 Map의 Entry 정보 한 개
            entry.value
            //entry의 모든 value가 true이면 true를 반환
            //하나라도 false이면 false를 반환
            //즉, 들어온 권한들은 map entry 하나 하나씩인데 그 map entry의 value가 모두 true이어야 함.
        }
        if(!resultPermission){
            //하나라도 권한이 false이면 종료
            //finish()
            
            Toast.makeText(this, "모든 권한 승인됨",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLayout() {
        binding.apply{
            btnCall.setOnClickListener {
                callAction()
            }
            btnMsg.setOnClickListener {
                val msg = Uri.parse("sms:010-1234-1234")
                val msgIntent = Intent(Intent.ACTION_SENDTO, msg)
                //메세지 보낼 수 있는 앱 실행
                msgIntent.putExtra("sms_body", "빨리 다음꺼 하자....")
                //해당 메세지 칸에 해당 내용 들어감
                startActivity(msgIntent)
            }
            btnWeb.setOnClickListener {
                val web = Uri.parse("http://www.naver.com")
                val webIntent = Intent(Intent.ACTION_VIEW, web)
                //해당 프로토콜로 들어갈 수 있는 앱 실행
                startActivity(webIntent)
            }
            btnMap.setOnClickListener {
                val location = Uri.parse("geo:37.543684, 127.077130?z=16")
                val mapIntent = Intent(Intent.ACTION_CALL, location)
                //지도를 보여줄 수 있는 앱 실행
                startActivity(mapIntent)
            }
            btnCamera.setOnClickListener {
                cameraAction()
            }
        }
    }
    private fun checkPermissions(){
        //모든 권한 허용되었는지 체크
        when{
            (ActivityCompat.checkSelfPermission(this, //CALL_PHONE permission 허락된 경우
                android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED)
            &&
            (ActivityCompat.checkSelfPermission(this, //CAMERA permission 허락된 경우
                android.Manifest.permission.CAMERA)
                    //checkSelfPermission() 현재 권한 정보 허용되었는지 체크하는 함수
                    == PackageManager.PERMISSION_GRANTED)-> {
                        Toast.makeText(this, "모든 권한 승인됨",Toast.LENGTH_SHORT).show()
            }

            (ActivityCompat.shouldShowRequestPermissionRationale(this,
                //명시적으로 CALL_PHONE permission 거부된 경우
                android.Manifest.permission.CALL_PHONE))
            ||
            (ActivityCompat.shouldShowRequestPermissionRationale(this,
                //명시적으로 CAMERA permission 거부된 경우
                android.Manifest.permission.CAMERA))
            ->{
                //거부된 경우 내용 작성
                permissionCheckAlertDlg()
            }

            else ->{//이 부분은 아직 권한 설정에 해당하는 상황이 허용이나 거부되지 않은 상황일 때 즉, 처음 실행시
                //따라서 권한 허용을 요청해야 함
                //permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                //permissionLauncher 객체를 통해서 권한 승인 여부를 받음
                //launch()로 해당 콜백함수 실행 매개변수로는 요청하는 permission 정보를 등록
                
                multiplePermissionsLauncher.launch(permissions)
                //모두 다 허용되는 경우를 요구
            }
        }
    }

    private fun permissionCheckAlertDlg() {
        val builder = AlertDialog.Builder(this)
        //AlertDialog의 Builder 객체 설정
        builder//builder 객체 속성 설정
            .setMessage("반드시 CALL_PHONE 권한과 CAMERA 권한이 모두 허용되어야 합니다.")//다이얼로그 내용 설정
            .setTitle("권한 체크")//다이얼로그 제목 설정
            .setPositiveButton("OK"){
                //OK 버튼 누를 시
                    _, _ ->//인자는 사용하지 않을거라서 _로 처리
                multiplePermissionsLauncher.launch(permissions)
                //이번에는 multiplePermission을 구동하는 것이라 매개변수에 해당 권한의 배열 설정
                //다시 요청하도록 수행
            }.setNegativeButton("Cancel"){
                //Cancel 버튼 누를 시
                    dlg, _ ->//dlg 인자는 dialog 관련 정보, 뒤는 사용하지 않을 인자
                dlg.dismiss()
                //finish()는 앱이 종료가 되는 것이고
                //dialog는 dismiss로 종료
            }
        //chain으로 정보 설정 가능
        val dlg = builder.create()
        //builder 객체 자체는 정보만 있는 것이고 create해야 생성됨
        dlg.show()
        //show()를 통해서 화면에 보여짐
    }
    fun allPermissionGranted()=permissions.all{
        //it은 permissions 배열의 원소 각각 하나이고
        //all을 통해서 모든 배열의 원소 체크
        ActivityCompat.checkSelfPermission(this, it)==PackageManager.PERMISSION_GRANTED
    }
    fun callphonePermissionGranted()=ActivityCompat.checkSelfPermission(this, //CALL_PHONE permission 허락된 경우
        android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    fun cameraPermissionGranted()=ActivityCompat.checkSelfPermission(this, //CALL_PHONE permission 허락된 경우
        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    //camera와 callphone 각각의 권한이 체크가 되었는지 확인하는 함수 생성

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
    //요청하고자 하는 단일 권한 callback함수를 매개변수로 등록 : ActivityResultContracts.RequestPermission()
    //이후 해당 body 부분에 수행할 내용 작성
        if(it){//it로 권한 승인 여부를 boolean 타입으로 받음
            callAction()
        }else{
            Toast.makeText(this, "권한 승인이 거부되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun callAlertDlg()
    {
        val builder = AlertDialog.Builder(this)
        //AlertDialog의 Builder 객체 설정
        builder//builder 객체 속성 설정
            .setMessage("반드시 CALL_PHONE 권한이 허용되어야 합니다.")//다이얼로그 내용 설정
            .setTitle("권한 체크")//다이얼로그 제목 설정
            .setPositiveButton("OK"){
                //OK 버튼 누를 시
                    _, _ ->//인자는 사용하지 않을거라서 _로 처리
                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                //다시 요청하도록 수행
            }.setNegativeButton("Cancel"){
                //Cancel 버튼 누를 시
                    dlg, _ ->//dlg 인자는 dialog 관련 정보, 뒤는 사용하지 않을 인자
                dlg.dismiss()
                //finish()는 앱이 종료가 되는 것이고
                //dialog는 dismiss로 종료
            }
        //chain으로 정보 설정 가능
        val dlg = builder.create()
        //builder 객체 자체는 정보만 있는 것이고 create해야 생성됨
        dlg.show()
        //show()를 통해서 화면에 보여짐
    }

    private fun cameraAction() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //카메라 사용 가능한 앱 실행
        //if(cameraPermissionGranted()){//해당 기능 권한 설정 허락 여부 체크
        if(allPermissionGranted()){//모든 권한 수행 허락시
            startActivity(intent)
        }else{
            checkPermissions()
        }
    }

    private fun callAction() {
        //해당 앱은 권한이 다 승인되어야 수행되기 때문에 승인이 안되면 실행이 안되기에 별도의 작업 필요 없음
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)
        //전화를 걸 수 있는 앱 실행
        //val callIntent = Intent(Intent.ACTION_DIAL, number)
        //전화 걸기 전에 다이얼 앱 실행
        //묵시적인 Intent는 무슨 앱인지는 모르지만 해당 action은 설정을 하고
        //실행 가능한 앱을 알아서 찾아줄 것
        //하지만 전화 거는 것은 돈이 들어가기에 권한 설정을 필수적으로 해야함
        if(callphonePermissionGranted()){//권한 설정 허락 여부 체크
            startActivity(callIntent)
        }else{
            checkPermissions()
        }
    }
    /*

    private fun callAction() {
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)
        //전화를 걸 수 있는 앱 실행
        //val callIntent = Intent(Intent.ACTION_DIAL, number)
        //전화 걸기 전에 다이얼 앱 실행
        //묵시적인 Intent는 무슨 앱인지는 모르지만 해당 action은 설정을 하고
        //실행 가능한 앱을 알아서 찾아줄 것
        startActivity(callIntent)
        //하지만 전화 거는 것은 돈이 들어가기에 권한 설정을 필수적으로 해야함
        when{
            ActivityCompat.checkSelfPermission(this, //permission 허락된 경우
                android.Manifest.permission.CALL_PHONE)
                //checkSelfPermission() 현재 권한 정보 허용되었는지 체크하는 함수
                    == PackageManager.PERMISSION_GRANTED -> {
                        startActivity(callIntent)
                    }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                //명시적으로 permission 거부된 경우
                //참고로 두 번이나 명시적으로 거부한 경우 더이상 창이 뜨지 않고 앱 속성을 통해서 권한 설정해야함
                android.Manifest.permission.CALL_PHONE)
                    ->{
                        //거부된 경우 내용 작성
                        callAlertDlg()
                    }
            else ->{//이 부분은 아직 권한 설정에 해당하는 상황이 허용이나 거부되지 않은 상황일 때 즉, 처음 실행시
                //따라서 권한 허용을 요청해야 함
                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
                //permissionLauncher 객체를 통해서 권한 승인 여부를 받음
                //launch()로 해당 콜백함수 실행 매개변수로는 요청하는 permission 정보를 등록
            }
        }
    }
    */
}