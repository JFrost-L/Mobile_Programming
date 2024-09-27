package com.frost.ex03room_dao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.ex03room_dao.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var db:ProductDatabase
    var recordset = ArrayList<Product>()
    var adapter = MyDataAdapter(ArrayList<Product>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initLayout()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        adapter.itemClickListener = object :MyDataAdapter.OnItemClickListener{
            //adapter 객체의 item 클릭시 리스너 초기화... 익명 객체로(object)
            override fun OnItemClick(position: Int) {
                binding.pIdEdit.setText(adapter.items[position].pId.toString())
                binding.pNameEdit.setText(adapter.items[position].pName)
                binding.pQuantityEdit.setText(adapter.items[position].pQuantity.toString())
            }
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }
    fun initLayout() {
        db = ProductDatabase.getDatabase(this)//싱글톤 객체 얻기
        
        CoroutineScope(Dispatchers.IO).launch{
            //일단 record 다 가져오기 메인에서 수행할 수 없어서 코루틴 이용
            getAllRecord()
        }

        binding.apply{
            insertBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(0, name, quantity)
                CoroutineScope(Dispatchers.IO).launch{
                    insert(product)
                }
            }
            updateBtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid, name, quantity)
                CoroutineScope(Dispatchers.IO).launch{
                    update(product)
                }
            }
            deleteBtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid, name, quantity)
                CoroutineScope(Dispatchers.IO).launch{
                    delete(product)
                }
            }

            findBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                CoroutineScope(Dispatchers.IO).launch{
                    findProduct(name)
                }
            }
            findBtn2.setOnClickListener {
                var name = pNameEdit.text.toString()
                name = name+"%"
                CoroutineScope(Dispatchers.IO).launch{
                    findProduct2(name)
                }
            }
        }
    }
    fun insert(product: Product){
        db.productDAO().insertProduct(product)
        getAllRecord()
    }

    fun update(product: Product){
        db.productDAO().updateProduct(product)
        getAllRecord()
    }

    fun delete(product: Product){
        db.productDAO().deleteProduct(product)
        getAllRecord()
    }

    fun findProduct(pname: String){
        recordset = db.productDAO().findProduct(pname) as ArrayList<Product>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }
    fun findProduct2(pname: String){
        recordset = db.productDAO().findProduct2(pname) as ArrayList<Product>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }
    fun getAllRecord() {
        recordset = db.productDAO().getAllRecord() as ArrayList<Product>
        //DAO의 getAllRecord 가져오기
        adapter.items = recordset
        //어댑터에 반영
        CoroutineScope(Dispatchers.Main).launch {
            //IO 코루틴에서 Main으로 잠시 switching
            adapter.notifyDataSetChanged()
        }
    }
}