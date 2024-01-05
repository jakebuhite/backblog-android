package com.tabka.backblog.utilities

import android.content.Context
import android.util.TypedValue

class DesignUtility {
    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt()
    }
}