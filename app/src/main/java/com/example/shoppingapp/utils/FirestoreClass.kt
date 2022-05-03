package com.example.shoppingapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingapp.models.CartItem
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.models.User
import com.example.shoppingapp.view.activities.*
import com.example.shoppingapp.view.fragments.DashboardFragment
import com.example.shoppingapp.view.fragments.ProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun uploadProductDetails(activity: AddProductActivity, product : Product)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showSnackBar("Something went wrong while uploading product", true)
            }
    }

    fun registerUser(activity: SignUpActivity, user : User)
    {
        mFirestore.collection(Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    Toast.makeText(activity.applicationContext, "User Information stored successfully", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(activity.applicationContext, "Storing user information failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showSnackBar("Something went wrong", true)
            }
    }

    fun getCurrentUserID() : String
    {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""

        if (currentUser != null) currentUserID = currentUser.uid

        return currentUserID
    }

    fun getUserDetails(activity: Activity)
    {
        mFirestore.collection(Constants.USERS).
                document(getCurrentUserID()).
                get().
                addOnSuccessListener { document->
                    Log.i(activity.javaClass.simpleName, document.toString())

                   val user = document.toObject(User::class.java)!!

                    // store username in device storage
                    val sharedPreferences = activity.getSharedPreferences(Constants.SHOP_PAL_PREFERENCES, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()

                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.firstName} ${user.lastName}"
                    )

                    editor.apply()

                    when (activity)
                    {
                        is LogInActivity -> {
                            activity.showSnackBar("Success", false)
                            activity.logInSuccess(user)
                        }

                        is SettingsActivity->{
                             activity.userDetailsSuccess(user)
                        }
                    }
                }
            .addOnFailureListener {
                when(activity)
                {
                    is LogInActivity->{
                        activity.showSnackBar("failed", true)
                        activity.hideProgressDialog()
                    }

                    is SettingsActivity->{
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap : HashMap<String, Any>)
    {
        mFirestore.collection(Constants.USERS).
            document(getCurrentUserID())
            .update(userHashMap)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    if (activity is UserProfile) {
                        activity.userProfileUpdateSuccess()
                    }

                } else {

                    if (activity is UserProfile) {
                        activity.hideProgressDialog()
                    }

                }
        }
    }

    fun getProductsList(fragment: Fragment)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener {document->
                Log.e("Product List", document.documents.toString())

                val productsList : ArrayList<Product> = ArrayList()

                for (doc in document.documents)
                {
                    val product = doc.toObject(Product::class.java)

                    product!!.product_id = doc.id

                    productsList.add(product)
                }

                when(fragment){
                    is ProductsFragment->{
                        fragment.getProductsSuccess(productsList)
                    }
                }

            }
    }

    fun getProductDetails(activity: Activity, product_details_id: String){

        mFirestore.collection(Constants.PRODUCTS)
            .document(product_details_id)
            .get()
            .addOnSuccessListener {
                val productDetails = it.toObject(Product::class.java)
                if (activity is ProductDetailsActivity){
                 activity.getProductDetailsSuccess(productDetails!!)
                }
            }
            .addOnFailureListener {
                if (activity is ProductDetailsActivity){
                    activity.hideProgressDialog()
                    Toast.makeText(activity.applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun getDashboardItems(fragment: DashboardFragment)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { documents ->

                val productList = ArrayList<Product>()

                for (doc in documents)
                {
                    val currProduct =  doc.toObject(Product::class.java)
                    currProduct.product_id = doc.id
                    productList.add(currProduct)
                }

                fragment.successDashboardItemsList(productList)

            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
                Toast.makeText(fragment.requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

    }


    fun deleteProduct(fragment: ProductsFragment, productId: String)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.onProductDeleteSuccess()
            }
            .addOnFailureListener {

                fragment.hideProgressDialog()

                Toast.makeText(fragment.requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()

            }
    }


    fun addCartItems(activity: Activity, cartItem: CartItem){
        mFirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(cartItem, SetOptions.merge())
            .addOnSuccessListener {
               if (activity is ProductDetailsActivity){
                   activity.addToCartSuccess()
               }
            }
            .addOnFailureListener {
                if (activity is ProductDetailsActivity){
                   activity.showSnackBar(it.message.toString(), true)
                }
            }
    }

    fun checkIfItemExistsInCart(activity: Activity, productID : String){
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productID)
            .get()
            .addOnSuccessListener {
                if (it.documents.size > 0){
                    if (activity is ProductDetailsActivity){
                        activity.productExistsInCartSuccess()
                    }
                } else {
                    if (activity is ProductDetailsActivity) {
                        activity.hideProgressDialog()
                    }
                }
            }
            .addOnFailureListener {
                if (activity is ProductDetailsActivity){
                    activity.hideProgressDialog()
                    Toast.makeText(activity.applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun getCartList(activity: Activity)
    {
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.d("TAG", document.documents.toString())

                val cartList = ArrayList<CartItem>()

                for (doc in document.documents){
                    val cartItem = doc.toObject(CartItem::class.java)!!
                    cartItem.id = doc.id
                    cartList.add(cartItem)
                }
                if (activity is CartListActivity){
                    activity.successCartItemsList(cartList)
                }

            }.addOnFailureListener {
                if (activity is CartListActivity){
                    activity.hideProgressDialog()
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }

            }

    }


}











