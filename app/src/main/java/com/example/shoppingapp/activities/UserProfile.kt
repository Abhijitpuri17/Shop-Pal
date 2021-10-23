package com.example.shoppingapp.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.FirestoreClass
import com.example.shoppingapp.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.lang.Exception
import java.util.jar.Manifest

class UserProfile : BaseActivity()
{

    lateinit var mUserDetails : User
    lateinit var userImageUri : Uri
    lateinit var userImageURL : String
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // get userDetails from intent as a parcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.setText(mUserDetails.firstName)
        et_last_name.setText(mUserDetails.lastName)
        et_email.setText(mUserDetails.email)
        et_first_name.isEnabled = false
        et_last_name.isEnabled = false
        et_email.isEnabled = false

        iv_user_image.setOnClickListener {
            onUserImageClick()
        }


        btn_submit_user_info.setOnClickListener {
            saveUserInfo()
        }

    }

    private fun onUserImageClick()
    {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                     //showErrorSnackbar("Storage permission is already granted", false)
                    showImageChooser()
                }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE)
        }
    }

    private fun saveUserInfo()
    {
        if (validateDetails()) {
            showErrorSnackbar("Info saved successfully", false)

            val mobileNumber = et_mobile_number.text.toString()
            val gender = if (radio_btn_male.isChecked) Constants.MALE else Constants.FEMALE

            val userHashMap = HashMap<String, Any>()

            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
            userHashMap[Constants.GENDER] = gender
            userHashMap[Constants.PROFILE_COMPLETED] = 1
            if (userImageURL.isNotEmpty()) {
                userHashMap[Constants.USER_IMAGE] = userImageURL
            }

            showProgressDialog("Please wait ...")
            FirestoreClass().updateUserProfileData(this, userHashMap)

        }
    }

    private fun saveUserImageInFirebaseStorage()
    {

        showProgressDialog("Please wait..")
        val imageExtension =   getFileExtension(this, userImageUri) //MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(userImageUri))

        val ref = FirebaseStorage.getInstance().reference.child("Image " + System.currentTimeMillis() + "." + imageExtension)

        ref.putFile(userImageUri).addOnSuccessListener {
            it.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener {url->
                    hideProgressDialog()
                    userImageURL = url!!.toString()
                }
        }
    }


    fun userProfileUpdateSuccess()
    {
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }


    private fun validateDetails() : Boolean
    {
        if (TextUtils.isEmpty(et_mobile_number.text.toString())) {
            showErrorSnackbar("Please Enter your Mobile Number", true)
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
                showErrorSnackbar("Storage permission is granted", false)
                showImageChooser()
            }
            else {
                showErrorSnackbar("Storage permission denied", true)
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
                        GlideLoader(this).loadUserPicture(selectedImageFileUri, iv_user_image)
                    } catch (e : Exception) {
                        showErrorSnackbar(e.localizedMessage!!.toString(), true)
                    }
                }
            }
        }
    }










}