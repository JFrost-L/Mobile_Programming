package com.frost.ex02dbpath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.frost.ex02dbpath.databinding.ActivityMainBinding
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDBHelper: MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDB()
        init()
        getAllRecord()
    }

    private fun initDB() {
        //dbFile 배포하기
        val dbFile = getDatabasePath("mydb.db")//db경로
        if(!dbFile.parentFile.exists()){//부모 파일이 없으면
            dbFile.parentFile.mkdir()//생성
        }
        if(!dbFile.exists()){//db파일이 없으면? -> 생성
            val file = resources.openRawResource(R.raw.mydb)//파일 객체
            val fileSize = file.available()//파일 크기
            val buffer = ByteArray(fileSize)//파일 크기만큼 버퍼 생성
            file.read(buffer)//버퍼에 데이터 넣기
            file.close()//파일 닫기
            dbFile.createNewFile()//dbFile에 새 파일 만들어
            val output = FileOutputStream(dbFile)
            output.write(buffer)//해당 객체에 버퍼 데이터 쓰기
            output.close()
        }
    }

    fun getAllRecord(){
        myDBHelper.getAllRecord()
    }
    fun clearEditText(){
        binding.apply {
            pIdEdit.text.clear()
            pNameEdit.text.clear()
            pQuantityEdit.text.clear()
        }
    }
    private fun init(){
        myDBHelper = MyDBHelper(this)
        binding.apply {
            testSql.addTextChangedListener {//textWatcher 이용
                val pName = it.toString()
                val result = myDBHelper.findProduct2(pName)
            }

            insertBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(0, name, quantity)
                //객체 생성
                val result = myDBHelper.insertProduct(product)
                if(result){
                    getAllRecord()
                    Toast.makeText(this@MainActivity, "Data INSERT SUCCESS", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data INSERT FAILED", Toast.LENGTH_SHORT).show()
                }
                clearEditText()
            }
            findBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val result = myDBHelper.findProduct(name)
                if(result){
                    Toast.makeText(this@MainActivity, "RECORD FOUND", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "NO MATCH FOUND", Toast.LENGTH_SHORT).show()
                }
            }
            deleteBtn.setOnClickListener {
                val pId = pIdEdit.text.toString()
                val result = myDBHelper.deleteProduct(pId)
                if(result){
                    Toast.makeText(this@MainActivity, "Data DELETE SUCCESS", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data DELETE FAILED", Toast.LENGTH_SHORT).show()
                }
                getAllRecord()
                clearEditText()
            }
            updateBtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid, name, quantity)
                //객체 생성
                val result = myDBHelper.updateProduct(product)
                if(result){
                    getAllRecord()
                    Toast.makeText(this@MainActivity, "Data UPDATE SUCCESS", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data UPDATE FAILED", Toast.LENGTH_SHORT).show()
                }
                clearEditText()
            }
        }
    }
}