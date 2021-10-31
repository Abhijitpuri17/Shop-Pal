package com.example.shoppingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.utils.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity()
{


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()

        /**
         * go to log in activity if user already has an account
         */
        tv_go_to_log_in.setOnClickListener{
            startActivity(Intent(this, LogInActivity::class.java))
            this.finish()
        }

        /**
         * call registerUssr function when sign up button is clicked
         */
        btn_sign_up.setOnClickListener{
            registerUser()
        }

    }

    /**
     * Setting up the action bar to get back button on action bar
     */
    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_sign_up_activity)
        val actionbar = supportActionBar
        if (actionbar != null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_btn)
            actionbar.setDisplayShowTitleEnabled(false)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    /**
     * Checking if details entered by user are valid like name
     */
    private fun validateRegisterDetails() : Boolean
    {
        when{

            /**
             * if first name field is empty
             */
             TextUtils.isEmpty(et_first_name.text.toString().trim{it <= ' '}) -> {
                 showErrorSnackBar("Please enter first name", true)
                return false
             }

            /**
             * if last name field is empty
             */
            TextUtils.isEmpty(et_last_name.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Please enter last name", true)
                return false
            }

            /**
             * if email field is empty
             */
            TextUtils.isEmpty(et_email.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Please enter your email address", true)
               return false
            }

            /**
             * if password field is empty
             */
            TextUtils.isEmpty(et_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Please enter password", true)
               return false
            }

            /**
             * if confirm password field is empty
             */
            TextUtils.isEmpty(et_confirm_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar("Please enter password in confirm password field", true)
               return false
            }

            /**
             * if password and confirm password do not match
             */

            et_password.text.toString() != et_confirm_password.text.toString() -> {
                showErrorSnackBar("Passwords not matched", true)
               return false
            }

            /**
             * if terms and conditions are not accepted
             */
            !checkbox_terms_and_conditions.isChecked -> {
                showErrorSnackBar("Please accept the terms and conditions to continue", true)
               return false
            }

            else -> {
                // if all details are valid
              return true
            }
        }
    }

    /**
     * registering user with email and password using firebase
     */
    private fun registerUser()
    {
        /**
         * if details entered are valid only then register the user
         */
        if (validateRegisterDetails())
        {
            /**
             * show progress Dialog while registration is process is being done in background
             */
            showProgressDialog("Please wait...")

            val email = et_email.text.toString()
            val password = et_password.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener {
                        /**
                         * if registering user task is successfully completed
                         */
                        if (it.isSuccessful)
                              {
                                  val firebaseUser = it.result!!.user!!

                                  /**
                                   * Store user info in firebase fire-store
                                   */
                                  val user = User(firebaseUser.uid, et_first_name.text.toString(),et_last_name.text.toString(), email)
                                  FirestoreClass().registerUser(this, user)

                                      firebaseUser.sendEmailVerification()
                                          .addOnCompleteListener { task ->
                                              hideProgressDialog()
                                              if (task.isSuccessful) {
                                                  showErrorSnackBar(
                                                      "Verification email has been sent to your email id. Please verify to continue",
                                                      false
                                                  )
                                                  Handler(Looper.getMainLooper()).postDelayed({
                                                      /**
                                                       * Sign out user and send them to log In Screen
                                                       */
                                                      FirebaseAuth.getInstance().signOut()
                                                      startActivity(
                                                          Intent(
                                                              this,
                                                              LogInActivity::class.java
                                                          )
                                                      )
                                                      this.finish()
                                                  }, 3500)
                                              } else {
                                                  FirebaseAuth.getInstance().signOut()
                                                  showErrorSnackBar(
                                                      "Please enter a valid email id",
                                                      true
                                                  )
                                              }
                                          }
                              }

                        /**
                         * if some error occurs while registering
                         */
                        else
                        {
                            hideProgressDialog()
                            showErrorSnackBar(it.exception!!.message.toString(), true)
                        }
                    }
        }
    }



}