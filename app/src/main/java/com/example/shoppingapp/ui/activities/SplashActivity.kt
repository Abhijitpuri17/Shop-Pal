package com.example.shoppingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.shoppingapp.R
import com.example.shoppingapp.utils.Constants
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /**
         * Remove status bar
         */
        removeStatusBar()

        /**
         * Add top or bottom animations to views
         */
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        tv_app_name.animation = bottomAnim
        tv_creator_name.animation = bottomAnim
        iv_app_icon.animation = topAnim

        /**
         * Move to main activity after splash time
         */
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            this.finish()
        }, Constants.SPLASH_SCREEN_TIME)

    }
}