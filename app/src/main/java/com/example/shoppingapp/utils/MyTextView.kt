package com.example.shoppingapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MyTextView(context : Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        apply_font()
    }

    private fun apply_font()
    {
        val bold_type_face = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = bold_type_face
    }

}