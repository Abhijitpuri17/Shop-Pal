package com.example.shoppingapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapp.R
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        btn_back_add_products.setOnClickListener {
            onBackPressed()
        }

    }
}