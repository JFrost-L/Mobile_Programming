package com.frost.ex01firebase

data class Product(var pId:Int, var pName:String, var pQuantity:Int){
    constructor():this(0, "noinfo", 0)
    //꼭 디폴트 생성자 추가!
}
