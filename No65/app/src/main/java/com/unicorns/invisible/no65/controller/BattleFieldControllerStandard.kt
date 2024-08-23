package com.unicorns.invisible.no65.controller

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class BattleFieldControllerStandard(
    activity: MainActivity,
    fieldTable: LinearLayout,
    giveUpButton: TextView,
    private val elementsLayout: LinearLayout,
    enemyCentreCell: TextView
) : BattleFieldController(activity, fieldTable, giveUpButton, enemyCentreCell) {
    @SuppressLint("ClickableViewAccessibility")
    fun addListenersForElementsLayout(
        listener: (ElementsGestureType) -> Unit
    ) {
        launchCoroutineOnMain {
            val swipeListener = object : OnSwipeTouchListener(activity) {
                override fun onSwipeHorizontal() {
                    listener(ElementsGestureType.SWIPE)
                }
            }
            val doubleTapListener = object : MultipleTapListener(activity) {
                override fun onMultipleTap(event: MotionEvent, numberOfTaps: Int) {
                    if (numberOfTaps == 2) {
                        listener(ElementsGestureType.DOUBLE_TAP)
                    }
                }
            }

            elementsLayout.setOnTouchListener { v, e ->
                swipeListener.onTouch(v, e)
                doubleTapListener.onTouch(v, e)
                true
            }
        }
    }
}