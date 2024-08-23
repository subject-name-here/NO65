package com.unicorns.invisible.no65

import android.widget.TextView
import com.unicorns.invisible.no65.controller.MenuItemController
import com.unicorns.invisible.no65.databinding.ActivityTrigramsBinding
import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.view.TrigramsSheetDrawer


class TrigramsSheetManager(
    override val activity: MainActivity,
    val knowledge: Knowledge
) : MenuItemManager {
    override val binding = ActivityTrigramsBinding.inflate(activity.layoutInflater)
    override val drawer = TrigramsSheetDrawer(activity, binding)
    override val controller = object : MenuItemController() {
        override val activity: MainActivity = this@TrigramsSheetManager.activity
        override val buttonsBlockingToListeners: Map<TextView, () -> Unit>
            get() = mapOf(
                binding.goBack to ::exit
            )
    }

    override fun setupContent() {
        drawer.updateFromKnowledge(knowledge)
    }
}