package com.frost.ex03room_dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class],
    version = 1
)

abstract class ProductDatabase :RoomDatabase(){
    abstract fun productDAO():ProductDAO

    companion object{//database 객체 생성 -> 싱글톤
        private var INSTANCE:ProductDatabase? = null

        fun getDatabase(context: Context):ProductDatabase{
            //context 정보 기반으로 객체 생성
            val tempInstance = INSTANCE
            //임시 객체로 만들어두기
            
            if(tempInstance!=null){
                return tempInstance
                //이미 만들어진 경우
            }
            
            val instance = Room.databaseBuilder(//DB객체 생성
                context,
                ProductDatabase::class.java,
                "productdb"
            ).build()
            
            INSTANCE = instance
            return instance
            //처음 생성한 경우
        }
    }
}