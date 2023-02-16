package com.example.shoppingcheck_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Item(val constructorName : String){
    var name = constructorName
}

class RecyclerAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val item:LinearLayout = itemView.findViewById(R.id.productLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        itemView.isClickable = true
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item = holder.item
        val itemText = item.findViewById<TextView>(R.id.productName)
        itemText.text = items[position].name
        item.setOnClickListener{
            val intent = Intent(item.context, ProductEditActivity::class.java)
            intent.putExtra("item name", itemText.text)
            intent.putExtra("position", position)
            (item.context as MainActivity).startActivityForResult(intent, 1)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class MainActivity : AppCompatActivity() {
    lateinit var items: MutableList<Item>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        items = mutableListOf<Item>()

        for (i in 10..30){
            items.add(Item("БАНан"))
        }

        recyclerView = findViewById(R.id.productList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(items)

        val addButton:Button = findViewById(R.id.add)

        addButton.setOnClickListener{
            val intent = Intent(this@MainActivity, ProductAddActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == RESULT_OK) {
                items.add(Item(data?.getStringExtra("name").toString()))
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        else {
            if(resultCode == RESULT_OK){
                val action = data?.getStringExtra("action")
                val position = data?.getIntExtra("position", 0)
                if (action == "rename"){
                    items[position!!].name = data?.getStringExtra("name").toString()
                }
                else if (action == "delete"){
                    items.removeAt(position!!)
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }
}