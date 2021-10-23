package com.example.shoppingapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        /**
         * Remove status bar
         */
        removeStatusBar()

        /**
         * Go to sign up activity if user is new
         */
        tv_go_to_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        /**
         * Try to Log in User on clicking log in button
         */
        btn_log_in.setOnClickListener {
            logInUser()
        }

        /**
         * If forgot password go to forgotPasswordActivity
         */
        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }


    /**
     * Checking if user entered email and password or not
     */
    private fun validate_login_details() : Boolean
    {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim{it <= ' '}) -> {
                showErrorSnackbar("Please enter your email address", true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackbar("Please enter your password to continue", true)
                false
            }

            else -> true
        }
    }

    private fun logInUser()
    {
        /**
         * Try to log in only if user entered email and password
         */
        if (validate_login_details())
        {
            /**
             * while logging in using firebase, show progressbar
             */
            showProgressDialog("Please wait...")

            val email = et_email.text.toString()
            val password = et_password.text.toString()

            /**
             * Login with email and password using firebase
             */
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {

                /**
                 * If user logged in successfully
                 */
                if (it.isSuccessful)
                {
                     val user = it.result!!.user

                    // if email is verified go to main activity
                    if (user!!.isEmailVerified) {
                        showErrorSnackbar("Successfully logged in", false)
                        FirestoreClass().getUserDetails(this)
                    }

                    // if user didn't verify email show error on screen
                    else {
                        hideProgressDialog()
                        showErrorSnackbar(
                            "Please verify your email id by visiting email sent to your id",
                            true
                        )
                        if (FirebaseAuth.getInstance().currentUser != null)
                        FirebaseAuth.getInstance().signOut()
                    }
                }

                /**
                 * If some error occurred or something went wrong while logging in, show what went wrong in snackbar
                 */
                else {
                    hideProgressDialog()
                    showErrorSnackbar(it.exception!!.message.toString(), true)
                }
            }


        }
    }



    fun logInSuccess(user: User) {
        /**
         * hide the progress bar when logged in
         */
        hideProgressDialog()

        if (user.profileCompleted == 0) {
            val intent = Intent(this, UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }
        else
        startActivity(Intent(this, MainActivity::class.java))
    }

}