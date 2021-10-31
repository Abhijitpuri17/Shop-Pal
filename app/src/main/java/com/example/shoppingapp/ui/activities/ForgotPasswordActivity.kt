package com.example.shoppingapp.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.shoppingapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.et_email

class ForgotPasswordActivity : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn_submit_forgot_pass.setOnClickListener {
            resetPassword()
        }

       setupActionBar()
    }

    private fun resetPassword()
    {
        if (et_email.text.toString().isEmpty()) {
            showErrorSnackBar("Please enter you email address", true)
        }
        else {
            showProgressDialog("Please wait...")
            FirebaseAuth.getInstance().sendPasswordResetEmail(et_email.text.toString()).addOnCompleteListener {
                hideProgressDialog()
                if (it.isSuccessful)
                {
                    showErrorSnackBar("An email is sent to your email id to reset the password", false)
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 2500)
                }
                else
                {
                    showErrorSnackBar(it.exception!!.message.toString(), true)
                }
            }
        }
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_forgot_password)
        val actionbar = supportActionBar
        if (actionbar != null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_btn)
            actionbar.setDisplayShowTitleEnabled(false)
        }

        toolbar_forgot_password.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}