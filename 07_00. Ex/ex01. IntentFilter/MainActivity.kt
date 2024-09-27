package com.frost.ex01intentfilter

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.ex01intentfilter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var adapter:MyDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }
    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        adapter = MyDataAdapter(ArrayList<MyData>())
        //명시적으로 adapter 객체 생성 후 사용(빈 객체를 넣기)
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        //api 버전별 처리
        val appList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent, PackageManager.ResolveInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")//어노테이션 설정으로 deprecate 처리
            packageManager.queryIntentActivities(intent, 0)
        }
        if(appList.size>0){
            for(appInfo in appList){
                var appLabel = appInfo.loadLabel(packageManager).toString()
                var appClass = appInfo.activityInfo.name
                var appPackage= appInfo.activityInfo.packageName
                var appIcon= appInfo.loadIcon(packageManager)
                adapter.items.add(MyData(appLabel, appClass, appPackage, appIcon))
            }
        }
        adapter.itemClickListener = object :MyDataAdapter.OnItemClickListener{
            //adapter 객체의 item 클릭시 리스너 초기화... 익명 객체로(object)
            override fun OnItemClick(data: MyData) {
                val intent = packageManager.getLaunchIntentForPackage(data.appPackage)
                //패키지 정보로 인텐트 생성
                startActivity(intent)
            }
        }
        binding.recyclerView.adapter = adapter
    }
}