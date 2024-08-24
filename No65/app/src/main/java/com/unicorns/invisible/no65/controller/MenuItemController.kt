package com.unicorns.invisible.no65.controller

import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


abstract class MenuItemController {
    abstract val activity: MainActivity
    open val buttonsBlockingToListeners: Map<TextView, () -> Unit> = mapOf()
    open val buttonsNonBlockingToListeners: Map<TextView, () -> Unit> = mapOf()
    open val buttonsFreeToListeners: Map<TextView, () -> Unit> = mapOf()

    private var itemSelected: Boolean = false
    fun reload() { itemSelected = false }

    fun setupButtons() = launchCoroutineOnMain {
        buttonsBlockingToListeners.forEach { pair ->
            setBlockingListener(pair.key, pair.value)
        }

        buttonsNonBlockingToListeners.forEach { pair ->
            setNonBlockingListener(pair.key, pair.value)
        }

        buttonsFreeToListeners.forEach { pair ->
            setFreeListener(pair.key, pair.value)
        }
    }

    fun setBlockingListener(button: TextView, listener: () -> Unit) {
        launchCoroutineOnMain {
            button.setOnClickListener {
                if (!itemSelected) {
                    itemSelected = true
                    activity.playToggleSwitchSound()
                    listener()
                }
            }
        }
    }
    fun setNonBlockingListener(button: TextView, listener: () -> Unit) {
        launchCoroutineOnMain {
            button.setOnClickListener {
                if (!itemSelected) {
                    activity.playToggleSwitchSound()
                    listener()
                }
            }
        }
    }
    private fun setFreeListener(button: TextView, listener: () -> Unit) {
        launchCoroutineOnMain {
            button.setOnClickListener {
                activity.playToggleSwitchSound()
                listener()
            }
        }
    }
}