package com.example.shoppingapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapp.R
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(Constants.SHOP_PAL_PREFERENCES, Context.MODE_PRIVATE)

        val userName = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "Abhijit")

        tv_main.text = "Hello ${userName}"

    }
}