package com.unicorns.invisible.no65.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


open class BattleFieldController(
    protected val activity: Activity,
    private val fieldTable: LinearLayout,
    private val giveUpButton: TextView,
    private val enemyCentreCell: TextView
) {
    private var isListeningButtons = true

    fun stopButtonsListener() { isListeningButtons = false }
    fun resumeButtonsListener() { isListeningButtons = true }

    @SuppressLint("ClickableViewAccessibility")
    fun addListenersForButtons(listener: (Coordinates, Int) -> Unit) {
        launchCoroutineOnMain {
            val cellsInCol = fieldTable.children.count()
            val row = fieldTable.children.first() as LinearLayout
            val cellsInRow = row.children.count()
            val fieldWidth = fieldTable.right
            val fieldHeight = fieldTable.bottom - fieldTable.top
            val cellWidth = fieldWidth / cellsInRow
            val cellHeight = fieldHeight / cellsInCol
            fieldTable.setOnTouchListener(object : MultipleTapListener(activity) {
                override fun onMultipleTap(event: MotionEvent, numberOfTaps: Int) {
                    val (x, y) = event.x to event.y
                    val (xOnField, yOnField) = x to y - fieldTable.y
                    val (j, i) = (xOnField / cellWidth).toInt() to (yOnField / cellHeight).toInt()
                    if (isListeningButtons) {
                        listener(Coordinates(i, j), numberOfTaps)
                    }
                }
            })
        }
    }

    fun setupGiveUpButton(listener: () -> Unit) = textViewSetListener(giveUpButton, listener)
    fun addListenerOnEnemyCentreCell(listener: () -> Unit) = textViewSetListener(enemyCentreCell, listener)
    private fun textViewSetListener(textView: TextView, listener: () -> Unit) {
        launchCoroutineOnMain {
            textView.setOnClickListener { listener() }
        }
    }
}