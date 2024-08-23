package com.unicorns.invisible.no65.controller

import android.view.inputmethod.InputMethodManager
import android.widget.TableLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.databinding.ActivityCreatorBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.dpad.CircleDPadMk2


class CreatorController(
    activity: MainActivity,
    private val binding: ActivityCreatorBinding
): LandsFieldController(activity) {
    override val landsTable: TableLayout
        get() = binding.landsTable
    override val dPadMk2: CircleDPadMk2
        get() = binding.cdp

    fun addListItemListeners(listener: (Int) -> Unit) {
        binding.allCellList.children.forEachIndexed { index, view ->
            setListener(view) { listener(index) }
        }
    }

    fun addDrawEraseButtonListener(listener: () -> Unit) = setListener(binding.drawEraseButton, listener)
    fun addSaveButtonListener(listener: () -> Unit) = setListener(binding.saveButton, listener)

    fun hideKeyboard() = launchCoroutineOnMain {
        val inputMethodManager = ContextCompat.getSystemService(activity, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}