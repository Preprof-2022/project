package com.example.shoppingcheck_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText

/**
 *
 */

class ProductAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_add)

        /**
         * This script sends the name of product which user is trying to add, and sends it to MainActivity
         */

        val buttonAdd : Button = findViewById(R.id.save)
        val textField : EditText = findViewById(R.id.editProductName)

        buttonAdd.setOnClickListener {
            val intent = Intent()
            intent.putExtra("name", textField.text.toString())
            setResult(RESULT_OK, intent)
            println(textField.text)
            finish()
        }
    }
}