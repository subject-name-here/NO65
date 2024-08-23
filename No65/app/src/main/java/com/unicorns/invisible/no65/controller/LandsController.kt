package com.unicorns.invisible.no65.controller


import android.annotation.SuppressLint
import android.widget.TableLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityLandsBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.dpad.CircleDPadMk2


class LandsController(
    activity: MainActivity,
    private val binding: ActivityLandsBinding
): LandsFieldController(activity) {
    override val dPadMk2: CircleDPadMk2
        get() = binding.cdp
    override val landsTable: TableLayout
        get() = binding.landsTable

    val onChangeMoveModeListeners: MutableList<() -> Unit> = ArrayList()
    val onChangeInteractionModeListeners: MutableList<() -> Unit> = ArrayList()
    val onRewindListeners: MutableList<() -> Unit> = ArrayList()

    private fun initListener(button: TextView, listenersList: MutableList<() -> Unit>) {
        setListener(button) {
            listenersList.forEach {
                it.invoke()
            }
        }
    }

    fun initListeners(goBack: () -> Unit) {
        setListener(binding.goBack, goBack)

        initListener(binding.changeMoveMode, onChangeMoveModeListeners)
        initListener(binding.changeInteractionMode, onChangeInteractionModeListeners)
        initListener(binding.rewind, onRewindListeners)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setSpeechTextViewSkip(onSwipeListener: () -> Unit) {
        val speechTextViewLayout = binding.speechTextViewLayout
        launchCoroutineOnMain {
            val swipeListener = object : OnSwipeTouchListener(activity) {
                override fun onSwipeHorizontal() {
                    onSwipeListener()
                }
            }
            speechTextViewLayout.setOnTouchListener { v, e ->
                swipeListener.onTouch(v, e)
                true
            }
        }
    }
}