package com.example.shoppingapp.view.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import com.example.shoppingapp.utils.GlideLoader
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.lang.Exception

class UserProfile : BaseActivity()
{

    lateinit var mUserDetails : User
    lateinit var userImageUri : Uri
    private var userImageURL : String? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // get userDetails from intent as a parcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
            if (mUserDetails.user_profile_image.isNotEmpty()){
                userImageURL = mUserDetails.user_profile_image
                GlideLoader(this).loadPicture(userImageURL!!, iv_user_image)
            }

            if (mUserDetails.mobile != 0L){
                et_mobile_number.setText(mUserDetails.mobile.toString())
                setUpActionBar()
            }

            if (mUserDetails.gender.isNotEmpty()) {
                if (mUserDetails.gender == Constants.MALE)
                    radio_btn_male.isChecked = true
                else radio_btn_female.isChecked = true
            }

        }

        // set first , last name and email previously added and make these editText disabled
        et_first_name.setText(mUserDetails.firstName)
        et_last_name.setText(mUserDetails.lastName)
        et_email.setText(mUserDetails.email)
        et_email.isEnabled = false

        // when user clicks on imageView
        iv_user_image.setOnClickListener {
            onUserImageClick()
        }

        // when user clicks on save
        btn_save_user_info.setOnClickListener {
            saveUserInfo()
        }

    }

    private fun setUpActionBar()
    {
        setSupportActionBar(toolbar_user_profile)
        val actionBar = supportActionBar
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
        }
        toolbar_user_profile.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun onUserImageClick()
    {
        // check for storage permission
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                     //showErrorSnackBar("Storage permission is already granted", false)
                    showImageChooser()
                }
        else
        // if app doesn't have storage permission, ask for the permission
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE)
        }
    }

    private fun saveUserInfo()
    {
        if (validateDetails()) {

            //showErrorSnackBar("Info saved successfully", false)

            val mobileNumber = et_mobile_number.text.toString()
            val gender = if (radio_btn_male.isChecked) Constants.MALE else Constants.FEMALE

            // make a hashmap to modify info saved in users collection of firebase
            val userHashMap = HashMap<String, Any>()

            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
            userHashMap[Constants.GENDER] = gender
            userHashMap[Constants.FIRST_NAME] = et_first_name.text.toString()
            userHashMap[Constants.LAST_NAME] = et_last_name.text.toString()
            userHashMap[Constants.PROFILE_COMPLETED] = 1
            if (userImageURL != null && userImageURL!!.isNotEmpty()) {
                userHashMap[Constants.USER_IMAGE] = userImageURL!!
            }

            showProgressDialog("Please wait ...")
            FirestoreClass().updateUserProfileData(this, userHashMap)

        }
    }

    private fun saveUserImageInFirebaseStorage()
    {
        showProgressDialog("Please wait..")
        val imageExtension = getFileExtension(this, userImageUri) //MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(userImageUri))

        val ref = FirebaseStorage.getInstance()
            .reference.child(
                Constants.USER_PROFILE_IMAGE +
                    System.currentTimeMillis() + "." +
                    imageExtension
            )

        ref.putFile(userImageUri)
            .addOnSuccessListener {
            it.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { url->
                    hideProgressDialog()
                    userImageURL = url!!.toString()
                }
        }
    }


    fun userProfileUpdateSuccess()
    {
        hideProgressDialog()
        startActivity(Intent(this, DashboardActivity::class.java))
        this.finish()
    }


    private fun validateDetails() : Boolean
    {
        if (TextUtils.isEmpty(et_mobile_number.text.toString())) {
            showSnackBar("Please Enter your Mobile Number", true)
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showSnackBar("Storage permission is granted", false)
                showImageChooser()
            }
            else {
                showSnackBar("Storage permission denied", true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null)
                {
                    try {
                        val selectedImageFileUri = data.data!!
                        //iv_user_image.setImageURI(selectedImageFileUri)
                        userImageUri = selectedImageFileUri
                        saveUserImageInFirebaseStorage()
                        GlideLoader(this).loadPicture(selectedImageFileUri, iv_user_image)
                    } catch (e : Exception) {
                        showSnackBar(e.localizedMessage!!.toString(), true)
                    }
                }
            }
        }
    }










}