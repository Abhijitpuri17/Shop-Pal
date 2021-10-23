package com.example.shoppingapp.activities

import android.app.Activity
import android.app.Dialog
import android.app.Notification
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity()
{

    lateinit var mProgressDialog : Dialog

    /**
     * Method to show snackbar in case of error or success
     */
    fun showErrorSnackbar(message: String, errorMessage: Boolean)
    {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

        val snackbarView = snackbar.view
        if (errorMessage){
            snackbarView.setBackgroundColor(ContextCompat.getColor(this,
                R.color.color_snackbar_error
            ))
        }
        else {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this,
                R.color.color_snackbar_success
            ))
        }
        snackbar.show()

    }


    /**
     * Show progress dialog
     */

    fun showProgressDialog(text: String)
    {

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()

    }

    /**
     * hide progress dialog
     */

    fun hideProgressDialog()
    {
        mProgressDialog.dismiss()
    }

    /**
     * Remove status bar
     */
    fun removeStatusBar()
    {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    /**
     * function to show image chooser
     */
    fun showImageChooser()
    {
      val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity : Activity, uri: Uri?) : String?
    {
       return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


}