package com.example.shoppingapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.lang.Exception
import java.net.URI
import java.util.jar.Manifest

class AddProductActivity : BaseActivity()
{

    var mProductImageURI : Uri? = null
    var mProductImageURL : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)


        btn_back_add_products.setOnClickListener {
            onBackPressed()
        }

        iv_btn_choose_image_add_product.setOnClickListener {
            chooseProductImage()
        }

        btn_submit_add_product.setOnClickListener {
            submitAddProduct()
        }
    }

   private fun validateProductDetails() : Boolean
    {

        return when{
            TextUtils.isEmpty(et_product_title.text.toString()) -> {
                showErrorSnackBar("Please enter Product Title", true)
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString()) ->{
                showErrorSnackBar("Please enter Product Description", true)
                false
            }

            TextUtils.isEmpty(et_product_price.text.toString()) ->{
                showErrorSnackBar("Please enter Product Price", true)
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString()) ->{
                showErrorSnackBar("Please enter Product Quantity", true)
                false
            }

            else -> true
        }

    }


    private fun submitAddProduct()
    {
        if (validateProductDetails())
        {
            showErrorSnackBar("Product added successfully", false)
        }
    }

    private fun chooseProductImage()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            showImageChooser()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showErrorSnackBar("Storage Permission granted.", false)
                showImageChooser()
            }
            else {
                showErrorSnackBar("Storage Permission denied.", true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE)
            {
                if (data != null)
                {
                    try{
                        mProductImageURI = data.data!!

                        GlideLoader(this).loadUserPicture(mProductImageURI!!, iv_product_image)
                    }
                    catch (e : Exception){
                        showErrorSnackBar("Something went wrong!", true)
                    }
                }
            }
        }
    }




}