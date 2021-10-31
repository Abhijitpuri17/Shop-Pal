package com.example.shoppingapp.utils.customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MyTextView(context : Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont()
    {
        val boldTypeFace = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = boldTypeFace
    }

}