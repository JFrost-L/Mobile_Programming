package com.frost.ex02dbpath
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

class MyDBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSiON) {
    companion object{
        val DB_NAME = "mydb.db"
        val DB_VERSiON = 1
        val TABLE_NAME="products"
        //DB attribute명
        val PID = "pId"
        val PNAME = "pName"
        val PQUANTITY = "pQuantity"
    }
    fun getAllRecord(){
        //table 모든 내용 뿌리기
        val strSql = "SELECT * FROM ${TABLE_NAME};"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)
        showRecord(cursor)
        cursor.close()
        db.close()
    }

    private fun showRecord(cursor: Cursor) {
        cursor.moveToFirst()//처음으로 커서 이동
        val attrCount = cursor.columnCount//attribute 개수
        val activity = context as MainActivity
        activity.binding.tableLayout.removeAllViewsInLayout()//일단 모두 지우고 다시 만들기

        //타이틀 만들기
        val tableRow = TableRow(activity)
        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        //row를 위한 Parameter 크기 동적 설정
        tableRow.layoutParams = rowParam
        val viewParam = TableRow.LayoutParams(0, 100, 1f)
        //tuple들 크기 설정
        
        for(i in 0 until attrCount){
            val textView = TextView(activity)//textView 생성
            textView.layoutParams = viewParam
            textView.text = cursor.getColumnName(i)
            textView.setBackgroundColor(Color.LTGRAY)
            textView.textSize = 15.0f
            textView.gravity = Gravity.CENTER
            tableRow.addView(textView)
            //설정한 textView를 tableRow에 넣기
        }
        activity.binding.tableLayout.addView(tableRow)
        //한 줄 넣기
        if(cursor.count==0){//레코드가 없는 경우
            return
        }
        //레코드 추가하기
        do {
            val row = TableRow(activity)
            row.layoutParams = rowParam
            row.setOnClickListener {//row 선택시 클릭 이벤트처리
                for(i in 0 until attrCount){
                    val textView = row.getChildAt(i) as TextView
                    //i번째 해당하는 TextView 가져오기
                    when(textView.tag){//tag로 식별하기!
                        0->activity.binding.pIdEdit.setText(textView.text)
                        1->activity.binding.pNameEdit.setText(textView.text)
                        2->activity.binding.pQuantityEdit.setText(textView.text)
                    }
                }
            }
            for(i in 0 until attrCount){
                val textView = TextView(activity)//textView 생성
                textView.tag = i//tag로 식별하기
                textView.layoutParams = viewParam
                textView.text = cursor.getString(i)
                textView.textSize = 13.0f
                textView.gravity = Gravity.CENTER
                row.addView(textView)
                //설정한 textView를 tableRow에 넣기
            }
            activity.binding.tableLayout.addView(row)
        }while(cursor.moveToNext())

    }

    fun insertProduct(product: Product):Boolean{
        val values = ContentValues()
        values.put(PNAME, product.pName)
        values.put(PQUANTITY, product.pQuantity)
        //insert 할 value들 할당
        val db = writableDatabase
        //삽입은 db에 쓰기
        val flag = db.insert(TABLE_NAME, null, values)>0
        //insert 성공하면 0보다 큰 값으로 true가 flag에 저장됨
        db.close()
        return flag
    }
    fun findProduct(name:String):Boolean{
        val strSql = "SELECT * FROM $TABLE_NAME WHERE $PNAME = '$name';"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count!=0//찾은 데이터 존재 유무
        showRecord(cursor)
        cursor.close()
        db.close()
        return flag
    }

    fun findProduct2(name: String): Boolean {//like절 옵션 추가
        val strSql = "SELECT * FROM $TABLE_NAME WHERE $PNAME LIKE '$name%';"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count!=0//찾은 데이터 존재 유무
        showRecord(cursor)
        cursor.close()
        db.close()
        return flag
    }

    fun deleteProduct(pid:String):Boolean{
        val strSql = "SELECT * FROM $TABLE_NAME WHERE $PID = '$pid';"
        val db = writableDatabase//삭제
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count!=0//찾은 데이터 존재 유무
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_NAME, "$PID=?", arrayOf(pid))//arg로 접근
        }
        cursor.close()
        db.close()
        return flag
    }

    fun updateProduct(product: Product): Boolean {
        val pid = product.pId
        val strSql = "SELECT * FROM $TABLE_NAME WHERE $PID = '$pid';"
        val db = writableDatabase//수정
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count!=0//찾은 데이터 존재 유무
        if(flag){
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(PNAME, product.pName)
            values.put(PQUANTITY, product.pQuantity)
            db.update(TABLE_NAME, values, "$PID=?", arrayOf(pid.toString()))//arg로 접근
        }
        cursor.close()
        db.close()
        return flag
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                "$PID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PNAME TEXT, " +
                "$PQUANTITY INTEGER);"
        //sql 구문 작성
        db!!.execSQL(create_table)//명령어 실행
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists ${TABLE_NAME}"
        //존재하면 삭제
        db!!.execSQL(drop_table)
        onCreate(db)
    }


}