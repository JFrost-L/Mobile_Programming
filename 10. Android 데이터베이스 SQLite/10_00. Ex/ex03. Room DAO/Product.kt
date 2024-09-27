package com.frost.ex03room_dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="products")//테이블 이름
data class Product(
    @PrimaryKey(autoGenerate = true) var pId:Int,//primary 설정에 자동 증가
    @ColumnInfo(name="pname") var pName:String,//attribute 설정
    @ColumnInfo(name = "pquantity") var pQuantity:Int)
