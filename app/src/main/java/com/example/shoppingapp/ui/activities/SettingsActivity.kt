package com.example.shoppingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import com.example.shoppingapp.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity()
{

    private lateinit var userDetails : User
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btn_back_settings.setOnClickListener {
            onBackPressed()
        }

        btn_logout_settings.setOnClickListener {
            logout()
        }

        btn_edit_profile_settings.setOnClickListener {
            val intent = Intent(Intent(this, UserProfile::class.java))
            intent.putExtra(Constants.EXTRA_USER_DETAILS, userDetails)
            startActivity(intent)
        }

    }

    private fun logout()
    {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun getUserDetails()
    {
        showProgressDialog("Please wait ...")

        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user : User)
    {
        userDetails = user
        hideProgressDialog()
        if (user.user_profile_image.isNotEmpty())
        GlideLoader(this).loadPicture(user.user_profile_image, iv_user_image_settings)
        et_user_name.setText("${user.firstName} ${user.lastName}")
        et_user_email.setText(user.email)
        et_user_mobile_number.setText("${user.mobile}")

    }

}