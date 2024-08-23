package com.unicorns.invisible.no65

import android.widget.TextView
import com.unicorns.invisible.no65.controller.MenuItemController
import com.unicorns.invisible.no65.databinding.ActivityAboutBinding
import com.unicorns.invisible.no65.view.AboutDrawer


class AboutManager(
    override val activity: MainActivity
) : MenuItemManager {
    override val binding = ActivityAboutBinding.inflate(activity.layoutInflater)
    override val drawer = AboutDrawer(activity, binding)
    override val controller = object : MenuItemController() {
        override val activity: MainActivity = this@AboutManager.activity
        override val buttonsBlockingToListeners: Map<TextView, () -> Unit>
            get() = mapOf(
                binding.goBack to ::exit
            )
    }

    override fun setupContent() {
        drawer.setEmailLink()
        drawer.setTwitterLink()
    }
}