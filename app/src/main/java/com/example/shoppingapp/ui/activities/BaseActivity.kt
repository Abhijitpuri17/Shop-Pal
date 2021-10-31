package com.example.shoppingapp.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.example.shoppingapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity()
{

    var doubleBackToExitPressedOnce = false
    lateinit var mProgressDialog : Dialog

    /**
     * Method to show snackBar in case of error or success
     */
    fun showErrorSnackBar(message: String, errorMessage: Boolean)
    {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this,
                R.color.color_snackbar_error
            ))
        }
        else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this,
                R.color.color_snackbar_success
            ))
        }
        snackBar.show()
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

    /**
     * function to get the extension of a file
     */
    fun getFileExtension(activity : Activity, uri: Uri?) : String?
    {
       return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    fun doubleBackToExit()
    {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true

        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000) ;

    }


}