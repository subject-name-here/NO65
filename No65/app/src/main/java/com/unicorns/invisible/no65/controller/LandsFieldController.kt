package com.unicorns.invisible.no65.controller

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.dpad.CircleDPadMk2
import com.unicorns.invisible.no65.view.dpad.DPadButton


abstract class LandsFieldController(protected val activity: MainActivity) {
    abstract val landsTable: TableLayout
    abstract val dPadMk2 : CircleDPadMk2

    @SuppressLint("ClickableViewAccessibility")
    fun addCellsListeners(
        landsWidth: Int,
        landsHeight: Int,
        singleTapCallback: (Coordinates) -> Unit = {},
        doubleTapCallback: (Coordinates) -> Unit
    ) {
        repeat(landsHeight) { r ->
            val row = landsTable.getChildAt(r) as TableRow
            repeat(landsWidth) { c ->
                val view = row.getChildAt(c) as ConstraintLayout
                launchCoroutineOnMain {
                    val tapListener = object : MultipleTapListener(activity) {
                        override fun onMultipleTap(event: MotionEvent, numberOfTaps: Int) {
                            if (numberOfTaps == 1) {
                                singleTapCallback(Coordinates(r, c))
                            } else if (numberOfTaps == 2) {
                                doubleTapCallback(Coordinates(r, c))
                            }
                        }
                    }
                    view.setOnTouchListener(tapListener)
                }
            }
        }
    }

    fun addMoveListener(deltaCallback: (Coordinates) -> Unit) = launchCoroutineOnMain {
        dPadMk2.listener = object : CircleDPadMk2.OnClickCircleDPadListener {
            override fun onClickButton(button: DPadButton) {
                val delta = when (button) {
                    DPadButton.BOTTOM_BUTTON -> Coordinates(1, 0)
                    DPadButton.LEFT_BUTTON -> Coordinates(0, -1)
                    DPadButton.TOP_BUTTON -> Coordinates(-1, 0)
                    DPadButton.RIGHT_BUTTON -> Coordinates(0, 1)
                }
                deltaCallback(delta)
            }
        }
    }

    protected fun setListener(button: View, listener: () -> Unit) = launchCoroutineOnMain {
        button.setOnClickListener { listener() }
    }
}