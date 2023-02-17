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

/**
 * The  data class for storing
 * @param constructorName  the name of item
 */
data class Item(val constructorName : String){
    var name = constructorName
}


/**
 * The adapter for the recyclerview
 */

class RecyclerAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    /**
     * ViewHolder for the recyclerView
     * @param  itemView  item
     */

    class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val item:LinearLayout = itemView.findViewById(R.id.productLayout)
    }

    /**
     * Overrides function executing on view holder creation, makes list items clickable
     * @param parent  parent ViewGroup
     * @param viewType view type (not used)
     * @return result RecyclerViewHolder
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        itemView.isClickable = true
        return RecyclerViewHolder(itemView)
    }

    /**
     * Override for function executing on view holder binding, starts activity which is used for editing/removing elements
     * @param holder RecyclerView holder
     * @param position item position in list
     */

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

    /**
     * Override for function which returns the length of the list
     */

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

        recyclerView = findViewById(R.id.productList)
        recyclerView.layoutManager = LinearLayoutManager(this)  //setting up the RecyclerView
        recyclerView.adapter = RecyclerAdapter(items)

        val addButton:Button = findViewById(R.id.add)

        addButton.setOnClickListener{
            val intent = Intent(this@MainActivity, ProductRecognizeActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    /**
     * Changes the list of products (adds an element if the request code is 0 and edits/removes an existing element if the request code is 1)
     * @param requestCode request code which specifies what to do with the list 0 for adding an item to the list, 1 for editing/removing one
     * @param resultCode shows if the activity worked properly
     * @param data an Intent with data returned from activity
     */

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