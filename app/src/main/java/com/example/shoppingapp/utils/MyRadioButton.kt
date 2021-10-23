package com.example.shoppingapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MyRadioButton(context : Context, attrs : AttributeSet): AppCompatRadioButton(context, attrs) {

    init {
        applyFont()
    }

    private fun applyFont() {

        val bold_type_face = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = bold_type_face
    }


}