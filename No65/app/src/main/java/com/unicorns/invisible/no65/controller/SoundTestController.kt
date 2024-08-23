package com.unicorns.invisible.no65.controller

import android.widget.LinearLayout
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.databinding.ActivitySoundTestBinding
import com.unicorns.invisible.no65.databinding.SoundTestItemBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class SoundTestController(
    override val activity: MainActivity,
    private val binding: ActivitySoundTestBinding,
    private val exitFunction: () -> Unit
) : MenuItemController() {
    override val buttonsBlockingToListeners: Map<TextView, () -> Unit>
        get() = mapOf(
            binding.goBack to exitFunction
        )

    fun setSoundNames(names: List<String>, listener: (Int) -> Unit) = launchCoroutineOnMain {
        val list = binding.soundTestList
        list.removeAllViews()

        names.forEachIndexed { index, name ->
            addSoundItem(index, name, list, listener)
        }
    }

    private fun addSoundItem(index: Int, name: String, viewList: LinearLayout, listener: (Int) -> Unit) {
        val itemBinding = SoundTestItemBinding.inflate(activity.layoutInflater, viewList, true)
        val newItem = itemBinding.root
        newItem.text = name
        setSilentListener(newItem) { listener(index) }
    }
}