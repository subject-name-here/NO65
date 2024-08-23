package com.unicorns.invisible.no65.util

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import com.unicorns.invisible.no65.view.LandsFieldDrawer

class ScreenDimensions {
    companion object {
        fun getCutoutHeight(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.window.decorView.rootWindowInsets.displayCutout?.safeInsetTop ?: 0
            } else 0
        }

        private fun getScreenDimensionsPx(): Dimensions {
            val metrics = Resources.getSystem().displayMetrics
            return Dimensions(metrics.widthPixels, metrics.heightPixels)
        }

        data class Dimensions(val width: Int, val height: Int)

        fun getLandsHeight(landsWidth: Int, landsWeight: Double): Int {
            val (screenWidthPx, screenHeightPx) = getScreenDimensionsPx()

            var landsHeight = (screenHeightPx * landsWeight).toInt() * landsWidth / screenWidthPx
            if (landsHeight % 2 == 0) {
                landsHeight++
            }
            return landsHeight
        }
    }
}