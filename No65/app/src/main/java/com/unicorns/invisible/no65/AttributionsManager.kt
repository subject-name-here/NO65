package com.unicorns.invisible.no65

import android.widget.TextView
import com.unicorns.invisible.no65.controller.MenuItemController
import com.unicorns.invisible.no65.databinding.ActivityAttributionsBinding
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnDefault
import com.unicorns.invisible.no65.view.AttributionsDrawer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


class AttributionsManager(
    override val activity: MainActivity
) : MenuItemManager {
    override val binding = ActivityAttributionsBinding.inflate(activity.layoutInflater)
    override val drawer = AttributionsDrawer(activity, binding)
    override val controller = object : MenuItemController() {
        override val activity: MainActivity = this@AttributionsManager.activity
        override val buttonsBlockingToListeners: Map<TextView, () -> Unit>
            get() = mapOf(
                binding.goBack to ::exit
            )
    }

    override suspend fun launch(playMusic: Boolean) {
        super.launch(playMusic)
        startMessages()
    }

    private var currentJob: Job? = null
    private fun startMessages() {
        currentJob = launchCoroutineOnDefault {
            delay(1000L)
            drawer.showMessage(R.string.attributions_game_name)
            delay(1000L)
            drawer.showMessage(R.string.attributions_me_1)
            delay(1000L)
            drawer.showMessage(R.string.attributions_me_2)
            delay(1000L)
            drawer.showMessage(R.string.attributions_me_3)
            delay(1000L)
            drawer.showMessage(R.string.attributions_header_2)
            delay(1000L)
            drawer.showMessage(R.string.attributions_stack_overflow)
            delay(1000L)
            drawer.showMessage(R.string.attributions_autofit_textview)
            delay(1000L)
            drawer.showMessage(R.string.attributions_dpad)
            delay(1000L)
            drawer.showMessage(R.string.attributions_symbola)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_generated)
            delay(1000L)
            drawer.showMessage(R.string.attributions_header_3)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_1)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_2)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_3)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_4)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_5)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_6)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_7)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_8)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_9)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_10)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_11)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_12)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_13)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_14)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_15)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_16)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_17)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_18)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_19)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_20)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_21)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_22)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_23)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_24)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_25)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_26)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_27)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_28)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_29)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_30)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_31)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_1)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_2)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_3)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_4)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_5)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_6)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_old_7)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_1)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_2)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_3)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_4)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_5)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_6)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_7)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_8)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_9)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_10)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_11)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_12)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_13)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_14)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_15)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_16)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_17)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_from_st_18)
            delay(1000L)
            drawer.showMessage(R.string.attributions_uncredited_sounds_1)
            delay(1000L)
            drawer.showMessage(R.string.attributions_uncredited_sounds_2)
            delay(1000L)
            drawer.showMessage(R.string.attributions_sound_thank_you_1)
            delay(1000L)
            drawer.showMessage(R.string.empty_line)
            delay(1000L)
            drawer.showMessage(R.string.attributions_special_thanks)
            delay(1000L)
            drawer.showMessage(R.string.attributions_thank_you)
            delay(1000L)
            drawer.showMessage(R.string.attributions_take_care)
            delay(1000L)
            drawer.showMessage(R.string.attributions_bye)
        }
    }

    override fun exit() {
        launchCoroutine {
            currentJob?.cancel()
            drawer.stop()
            super.exit()
        }
    }
}