package com.example.shoppingapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.shoppingapp.R
import com.example.shoppingapp.models.CartItem
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import com.example.shoppingapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {


    private lateinit var productDetails : Product
    private var productDetailsID : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setUpActionBar()

        productDetailsID = intent.getStringExtra(Constants.PRODUCT_DETAILS_ID)!!

        showProgressDialog("Please wait...")
        FirestoreClass().getProductDetails(this, productDetailsID)

        btn_add_to_cart.setOnClickListener {
            addToCart()
        }

        btn_go_to_cart.setOnClickListener {
           val intent = Intent(this, CartListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProductDetailsOnScreen(){
        GlideLoader(this).loadPicture(productDetails.image, iv_product_image)
        tv_product_title.text = productDetails.title
        tv_product_price.text = "Rs.${productDetails.price}"

        tv_product_description.text = productDetails.description

        tv_stock_quantity.text = productDetails.stock_quantity

        if (productDetails.user_id == FirestoreClass().getCurrentUserID()){
            btn_add_to_cart.visibility = View.GONE
            btn_go_to_cart.visibility = View.GONE
        } else {
            btn_add_to_cart.visibility = View.VISIBLE
            showProgressDialog("Please wait..")
            FirestoreClass().checkIfItemExistsInCart(this, productDetailsID)
        }
    }

    private fun addToCart()
    {
        val item = CartItem(
            FirestoreClass().getCurrentUserID(),
            product_id = productDetailsID,
            title = productDetails.title,
            price= productDetails.price,
            image = productDetails.image,
            cart_quantity = Constants.DEFAULT_CART_QUANTITY,
        )

        FirestoreClass().addCartItems(this, item)
    }

    fun addToCartSuccess(){
      //  Toast.makeText(this, "Added To cart", Toast.LENGTH_SHORT).show()
        showSnackBar("Added To Cart", false)
        showProgressDialog("Please wait...")
        FirestoreClass().checkIfItemExistsInCart(this, productDetailsID)
    }


    fun getProductDetailsSuccess(product_details: Product){
        hideProgressDialog()
        productDetails = product_details
        loadProductDetailsOnScreen()
    }

    fun productExistsInCartSuccess(){
        hideProgressDialog()
        btn_go_to_cart.visibility = View.VISIBLE
        btn_add_to_cart.visibility = View.GONE
    }

    private fun setUpActionBar()
    {
        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)

            actionBar.title = ""
        }

        toolbar_product_details_activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

}