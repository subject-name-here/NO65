package com.unicorns.invisible.no65.view

import android.view.View
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityInitBinding
import com.unicorns.invisible.no65.init.InitData
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class GameInitDrawer(
    override val activity: MainActivity,
    private val binding: ActivityInitBinding
) : FadeDrawer {
    private val goBackButton
        get() = binding.goBack
    private val cdp
        get() = binding.cdp
    private val cdpUses
        get() = binding.cdpUses
    private val changeMoveMode
        get() = binding.changeMoveMode
    private val changeInteractionMode
        get() = binding.changeInteractionMode
    private val moneys
        get() = binding.moneys
    private val timePanel
        get() = binding.timePanel

    private val statusView
        get() = binding.status
    private val initInfo
        get() = binding.initInfo
    private val initResultButton
        get() = binding.initResultButton

    override val screen: View
        get() = binding.initScreen

    fun showControls() = launchCoroutineOnMain {
        goBackButton.text = activity.getString(R.string.go_back_symbol)
        cdp.visibility = View.VISIBLE
        cdpUses.visibility = View.INVISIBLE
        changeMoveMode.text = activity.getString(R.string.action_walk_symbol)
        changeInteractionMode.text = activity.getString(R.string.action_peace_symbol)
        moneys.text = activity.getString(R.string.moneys_placeholder).format(InitData.INIT_MONEYS)
        timePanel.text = activity.getString(R.string.time_panel_placeholder).format(InitData.INIT_TIME)
    }
    
    fun showControlsExplained() = launchCoroutineOnMain {
        goBackButton.text = activity.getString(R.string.go_back_button_explained)
        cdp.visibility = View.INVISIBLE
        cdpUses.visibility = View.VISIBLE
        changeMoveMode.text = activity.getString(R.string.change_move_mode_button_explained)
        changeInteractionMode.text = activity.getString(R.string.change_interaction_mode_button_explained)
        moneys.text = activity.getString(R.string.moneys_explained)
        timePanel.text = activity.getString(R.string.time_panel_explained)
    }

    fun setStatusText(statusId: Int) = launchCoroutineOnMain {
        val status = activity.getString(statusId)
        statusView.text = activity.getString(R.string.init_status).format(status)
    }
    fun setInitInfo() = launchCoroutineOnMain {
        initInfo.text = activity.getString(R.string.success_dive_into)
    }
    fun showStartGameButton() = launchCoroutineOnMain {
        initResultButton.text = activity.getString(R.string.dive_into)
    }

    fun stop() {
        cdp.stop()
    }
}