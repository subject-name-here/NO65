package com.unicorns.invisible.no65.view

import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityLandsBinding
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.MoveMode
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class Lands65Drawer(
    activity: MainActivity,
    binding: ActivityLandsBinding,
    landsWidth: Int,
    landsHeight: Int,
): LandsDrawer(activity, binding, landsWidth, landsHeight) {
    private val changeInteractionModeButton
        get() = binding.changeInteractionMode
    private val changeMoveModeButton
        get() = binding.changeMoveMode
    private val moneysTextView
        get() = binding.moneys
    private val timePanelTextView
        get() = binding.timePanel

    fun drawStats(protagonist: CellProtagonist) = launchCoroutineOnMain {
        moneysTextView.text = activity.getString(R.string.moneys_placeholder).format(protagonist.moneys)
    }
    fun drawTicks(ticks: Int) = launchCoroutineOnMain {
        timePanelTextView.text = activity.getString(R.string.time_panel_placeholder).format(ticks)
    }

    fun setBattleMode(mode: BattleMode) = launchCoroutineOnMain {
        changeInteractionModeButton.text = when (mode) {
            BattleMode.ATTACK ->
                activity.resources.getString(R.string.action_attack_symbol)
            BattleMode.PEACE, BattleMode.FIXED_PEACE ->
                activity.resources.getString(R.string.action_peace_symbol)
        }
    }
    fun setMoveMode(mode: MoveMode) = launchCoroutineOnMain {
        changeMoveModeButton.text = when (mode) {
            MoveMode.RUN ->
                activity.resources.getString(R.string.action_run_symbol)
            MoveMode.WALK, MoveMode.FIXED_WALK ->
                activity.resources.getString(R.string.action_walk_symbol)
        }
    }
}