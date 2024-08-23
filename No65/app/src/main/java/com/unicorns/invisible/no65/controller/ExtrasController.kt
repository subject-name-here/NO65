package com.unicorns.invisible.no65.controller

import android.widget.LinearLayout
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.databinding.ActivityExtrasBinding
import com.unicorns.invisible.no65.databinding.ExtrasItemBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class ExtrasController(
    override val activity: MainActivity,
    private val binding: ActivityExtrasBinding,
    goBackListener: () -> Unit,
    private val idleListener: () -> Unit,
    soundTestListener: () -> Unit,
    replayButtonsListener: () -> Unit,
    waterAddonButtonListener: () -> Unit,
    isWaterAddonButtonBlocking: Boolean,
) : MenuItemController() {
    override val buttonsBlockingToListeners: Map<TextView, () -> Unit> = mutableMapOf(
        binding.goBack to goBackListener,
        binding.soundTestButton to soundTestListener,
    ).also {
        if (isWaterAddonButtonBlocking) {
            it[binding.waterAddonButton] = waterAddonButtonListener
        }
    }
    override val buttonsNonBlockingToListeners: Map<TextView, () -> Unit> = mutableMapOf(
        binding.extrasButton1 to idleListener,
        binding.extrasButton3 to idleListener,
        binding.replayBattlesButton to replayButtonsListener,
    ).also {
        if (!isWaterAddonButtonBlocking) {
            it[binding.waterAddonButton] = waterAddonButtonListener
        }
    }

    fun showBattlesList(
        battleNamesToIsBlocking: List<Pair<String, Boolean>>,
        listener: (Int) -> Unit
    ) = launchCoroutineOnMain {
        val viewList = binding.extrasList
        viewList.removeAllViews()

        battleNamesToIsBlocking.forEachIndexed { index, pair ->
            val battleName = pair.first
            val isBlocking = pair.second
            addItemListener(viewList, index, battleName, isBlocking, isIdle = !isBlocking, listener)
        }
    }

    fun showWaterAddonButtons(
        textId1: Int, listener1: () -> Unit,
        textId2: Int, listener2: () -> Unit
    ) = launchCoroutineOnMain {
        val viewList = binding.extrasList
        viewList.removeAllViews()

        addItemListener(viewList, 0, "") {}
        addItemListener(viewList, 1, activity.getString(textId1)) { listener1() }
        addItemListener(viewList, 2, "") {}
        addItemListener(viewList, 3, activity.getString(textId2)) { listener2() }
    }

    private fun addItemListener(
        viewList: LinearLayout,
        index: Int,
        text: String,
        isBlocking: Boolean = true,
        isIdle: Boolean = false,
        listener: (Int) -> Unit,
    ) {
        val extrasItemBinding = ExtrasItemBinding.inflate(activity.layoutInflater, viewList, true)
        val newItem = extrasItemBinding.root
        newItem.text = text
        when {
            isBlocking -> {
                setBlockingListener(newItem) { listener(index) }
            }
            !isIdle -> {
                setNonBlockingListener(newItem) { listener(index) }
            }
            else -> {
                setNonBlockingListener(newItem, idleListener)
            }
        }
    }
}