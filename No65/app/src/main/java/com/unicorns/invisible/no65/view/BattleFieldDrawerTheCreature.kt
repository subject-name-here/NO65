package com.unicorns.invisible.no65.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.databinding.ActivityBattleBinding
import com.unicorns.invisible.no65.databinding.TheCreatureBlackScreenBinding
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlinx.coroutines.Job


class BattleFieldDrawerTheCreature(
    activity: MainActivity,
    fieldWidth: Int,
    fieldHeight: Int,
    private val binding: ActivityBattleBinding
) : BattleFieldDrawerStandard(activity, fieldWidth, fieldHeight, binding) {

    override val enemyCellsLayout: ConstraintLayout
        get() = binding.enemyTheCreatureInclude.creatureCells.root
    override val enemyHealthDecreaseField
        get() = binding.enemyTheCreatureInclude.healthDecreaseField
    override val enemySpeechBubble
        get() = binding.enemyTheCreatureInclude.speechBubble
    override val enemySpeechBubbleLayout: LinearLayout
        get() = binding.enemyTheCreatureInclude.speechBubbleLayout
    override val enemyFaceCell: TextView
        get() = binding.enemyTheCreatureInclude.creatureCells.cell1Creature
    override val enemyCentreCell: TextView
        get() = binding.enemyTheCreatureInclude.creatureCells.cell4Creature

    private val enemyTheCreatureIncludeLayout
        get() = binding.enemyTheCreatureIncludeLayout

    override fun init(): Job {
        return launchCoroutineOnMain {
            super.init().join()
            enemyTheCreatureIncludeLayout.visibility = View.VISIBLE
            enemySpeechBubble.setMinTextSize(2f)
        }
    }

    override fun initEnemyPanel(enemy: BattleFieldCharacter) = launchCoroutineOnMain {}
    override fun updateEnemyPanel(enemy: BattleFieldCharacter) = launchCoroutineOnMain {}

    fun writeWordsOnBlackScreen(words: String) = launchCoroutineOnMain {
        val screenBinding = TheCreatureBlackScreenBinding.inflate(activity.layoutInflater, battleScreen, true)
        val textView = screenBinding.theCreatureMidtro
        textView.setMinTextSize(2f)
        textView.text = words
    }
    fun clearBlackScreen() = launchCoroutineOnMain {
        battleScreen.removeAllViews()
    }
}