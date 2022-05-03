package com.example.shoppingapp.view.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import com.example.shoppingapp.utils.GlideLoader
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_product.*
import java.lang.Exception

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



    private fun saveProductImageToFirebaseStorage()
    {
        showProgressDialog("Please wait...")

        val imgExtension = getFileExtension(this, mProductImageURI)

        val ref = FirebaseStorage.getInstance().reference.child("${Constants.PRODUCT_IMAGE} ${System.currentTimeMillis()}.$imgExtension")

        ref.putFile(mProductImageURI!!).addOnSuccessListener {

            it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                hideProgressDialog()
                mProductImageURL = url!!.toString()
            }
        }
    }



   private fun validateProductDetails() : Boolean
    {

        return when{

            mProductImageURL == null -> {
                showSnackBar("Please select the product image", true)
                false
            }

            TextUtils.isEmpty(et_product_title.text.toString()) -> {
                showSnackBar("Please enter Product Title", true)
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString()) ->{
                showSnackBar("Please enter Product Description", true)
                false
            }

            TextUtils.isEmpty(et_product_price.text.toString()) ->{
                showSnackBar("Please enter Product Price", true)
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString()) ->{
                showSnackBar("Please enter Product Quantity", true)
                false
            }

            else -> true
        }

    }


    private fun submitAddProduct()
    {
        if (validateProductDetails())
        {

            showProgressDialog("Please wait")

            val userName = this.getSharedPreferences(Constants.SHOP_PAL_PREFERENCES, MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

            val product = Product(FirestoreClass().getCurrentUserID(),
                                    userName,
                                    et_product_title.text.toString(),
                                    et_product_price.text.toString(),
                                    et_product_description.text.toString(),
                                    et_product_quantity.text.toString(),
                                    mProductImageURL!!
                                )

            FirestoreClass().uploadProductDetails(this, product)

        }
    }

    fun productUploadSuccess()
    {
        hideProgressDialog()
        Toast.makeText(this, "Product uploaded successfully", Toast.LENGTH_SHORT).show()
        finish()
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
                showSnackBar("Storage Permission granted.", false)
                showImageChooser()
            }
            else {
                showSnackBar("Storage Permission denied.", true)
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

                        saveProductImageToFirebaseStorage()

                        iv_btn_choose_image_add_product.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_edit_orange))

                        GlideLoader(this).loadPicture(mProductImageURI!!, iv_product_image)
                    }
                    catch (e : Exception){
                        showSnackBar("Something went wrong!", true)
                    }
                }
            }
        }
    }




}