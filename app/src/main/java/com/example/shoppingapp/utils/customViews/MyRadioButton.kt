package com.example.shoppingapp.utils.customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MyRadioButton(context : Context, attrs : AttributeSet): AppCompatRadioButton(context, attrs) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeFace = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeFace
    }


}