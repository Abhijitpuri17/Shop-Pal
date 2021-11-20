package com.example.shoppingapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.models.User
import com.example.shoppingapp.ui.activities.*
import com.example.shoppingapp.ui.fragments.ProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
                activity.showErrorSnackBar("Something went wrong while uploading product", true)
            }
    }

    fun registerUser(activity: SignUpActivity, user : User)
    {
        mFirestore.collection(Constants.USERS).document(user.id)
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
                activity.showErrorSnackBar("Something went wrong", true)
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
                            activity.showErrorSnackBar("Success", false)
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
                        activity.showErrorSnackBar("failed", true)
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
            document(getCurrentUserID()).
            update(userHashMap).addOnCompleteListener {
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

    fun getProducts(fragment: Fragment)
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

}











