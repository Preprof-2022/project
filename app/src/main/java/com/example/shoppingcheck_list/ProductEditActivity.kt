package com.example.shoppingcheck_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ProductEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_edit)

        val buttonSave : Button = findViewById(R.id.SaveEdited)
        val buttonDelete: Button = findViewById(R.id.remove)
        val textField : EditText = findViewById(R.id.editProductName2)

        val position = intent.extras?.getInt("position")

        textField.setText(intent.extras?.getString("item name"))

        buttonSave.setOnClickListener {
            val intent = Intent()
            intent.putExtra("action", "rename")
            intent.putExtra("name", textField.text.toString())
            intent.putExtra("position", position)
            setResult(RESULT_OK, intent)
            println(textField.text)
            finish()
        }

        buttonDelete.setOnClickListener {
            val intent = Intent()
            intent.putExtra("action", "delete")
            intent.putExtra("position", position)
            setResult(RESULT_OK, intent)
            println(position)
            finish()
        }
    }
}