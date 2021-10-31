package com.example.shoppingapp.utils.customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MyButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont()
    {
        val boldTypeFace = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeFace
    }
}