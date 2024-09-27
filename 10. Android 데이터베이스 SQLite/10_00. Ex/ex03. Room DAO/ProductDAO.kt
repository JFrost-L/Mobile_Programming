package com.frost.ex03room_dao

import androidx.room.*


@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)//동일한 id 들어가 충돌 발생시 무시하기로 함
    fun insertProduct(product:Product)

    @Delete
    fun deleteProduct(product:Product)

    @Update
    fun updateProduct(product:Product)

    @Query("Select * from products")
    fun getAllRecord():List<Product>

    @Query("Select * from products where pName = :name")
    fun findProduct(name:String):List<Product>

    @Query("Select * from products where pName LIKE :name")
    fun findProduct2(name:String):List<Product>

}
