package com.unicorns.invisible.no65.view

import android.view.View
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityExtrasBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class ExtrasDrawer(
    override val activity: MainActivity,
    private val binding: ActivityExtrasBinding
) : FadeDrawer {
    override val screen: View
        get() = binding.screen

    private val waterAddonButton: TextView
        get() = binding.waterAddonButton
    private val soundTestButton: TextView
        get() = binding.soundTestButton

    fun setWaterAddonButton(isAvailable: Boolean) = launchCoroutineOnMain {
        waterAddonButton.text = if (isAvailable) {
            activity.getString(R.string.water_addon)
        } else {
            activity.getString(R.string.extras_unknown)
        }
    }

    fun showSoundTestButton() = launchCoroutineOnMain {
        soundTestButton.visibility = View.VISIBLE
    }
}