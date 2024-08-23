package com.unicorns.invisible.no65.view

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.databinding.ActivityLandsBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class LandsBCDrawer(
    activity: MainActivity,
    binding: ActivityLandsBinding,
    landsWidth: Int,
    landsHeight: Int,
): LandsDrawer(activity, binding, landsWidth, landsHeight) {
    private val controlsScreen: ConstraintLayout
        get() = binding.controlsScreen

    fun flash(time: Long = 250L) = launchCoroutineOnMain {
        fadeToWhite(time).join()
        hideIncludeLayout()
    }

    fun hideControls() = launchCoroutineOnMain {
        controlsScreen.visibility = View.VISIBLE
    }
}