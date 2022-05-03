package com.example.shoppingapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppingapp.R
import com.example.shoppingapp.models.CartItem
import com.example.shoppingapp.utils.FirestoreClass
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_product_details.*

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setUpActionBar()


    }

    private fun setUpActionBar()
    {
        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)

            actionBar.title = ""
        }

        toolbar_cart_list_activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun getCartItemsList(){
        showProgressDialog("Please wait...")
        FirestoreClass().getCartList(this)
    }

    override fun onResume() {
        super.onResume()
        getCartItemsList()
    }


    fun successCartItemsList(cartList : ArrayList<CartItem>){
        hideProgressDialog()
        for (item in cartList){
            Toast.makeText(this, item.title, Toast.LENGTH_LONG).show()
        }
    }

}