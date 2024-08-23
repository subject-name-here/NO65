package com.unicorns.invisible.no65.util

import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R


fun MainActivity.getColorById(colorId: Int): Int {
    return try {
        ContextCompat.getColor(this, colorId)
    } catch (e: Exception) {
        Log.e("ViewUtils", e.toString())
        R.color.no_color
    }
}

fun setTextAppearance(element: TextView, styleId: Int) {
    TextViewCompat.setTextAppearance(element, styleId)
}