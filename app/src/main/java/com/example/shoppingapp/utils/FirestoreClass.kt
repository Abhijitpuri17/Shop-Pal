package com.example.shoppingapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.shoppingapp.activities.LogInActivity
import com.example.shoppingapp.activities.MainActivity
import com.example.shoppingapp.activities.SignUpActivity
import com.example.shoppingapp.activities.UserProfile
import com.example.shoppingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()


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
    }

    private fun getCurrentUserID() : String
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
                addOnSuccessListener {document->
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

                    if (activity is LogInActivity) {
                        activity.logInSuccess(user)
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


}