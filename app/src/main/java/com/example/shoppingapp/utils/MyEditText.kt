package com.example.shoppingapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText(context: Context, attributeSet: AttributeSet) : AppCompatEditText(context, attributeSet) {
    init {
        apply_font()
    }

    private fun apply_font()
    {
        val bold_type_face = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = bold_type_face
    }

}