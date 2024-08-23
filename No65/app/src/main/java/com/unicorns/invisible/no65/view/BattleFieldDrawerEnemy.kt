package com.unicorns.invisible.no65.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.lb.auto_fit_textview.AutoResizeTextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityBattleBinding
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlinx.coroutines.Job


class BattleFieldDrawerEnemy(
    activity: MainActivity,
    fieldWidth: Int,
    fieldHeight: Int,
    private val binding: ActivityBattleBinding,
) : BattleFieldDrawerStandard(activity, fieldWidth, fieldHeight, binding) {
    private val enemyIcon
        get() = binding.enemyStandardInclude.enemyIcon

    private val enemyCells: List<TextView> by lazy {
        binding.enemyStandardInclude.enemyCells.characterCellsHolder.children
            .filterIsInstance<TextView>()
            .toList()
    }

    override val enemyHealthDecreaseField: TextView
        get() = binding.enemyStandardInclude.healthDecreaseField
    override val enemyCellsLayout: ConstraintLayout
        get() = binding.enemyStandardInclude.enemyCellsWrapper
    override val enemySpeechBubbleLayout: LinearLayout
        get() = binding.enemyStandardInclude.speechBubbleLayout
    override val enemySpeechBubble: AutoResizeTextView
        get() = binding.enemyStandardInclude.speechBubble
    override val enemyFaceCell: TextView
        get() = enemyCells[ENEMY_FACE_CELL_NUMBER]
    override val enemyCentreCell: TextView
        get() = enemyCells[ENEMY_CENTER_CELL_NUMBER]

    private val enemyStandardIncludeLayout
        get() = binding.enemyStandardIncludeLayout

    override fun init(): Job {
        return launchCoroutineOnMain {
            super.init().join()
            enemyStandardIncludeLayout.visibility = View.VISIBLE
            enemySpeechBubble.setMinTextSize(2f)
        }
    }

    override fun initEnemyPanel(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        enemyIcon.text = enemy.hexagramSymbol

        enemyFaceCell.text = enemy.defaultFace
        enemyCells[ENEMY_CENTER_CELL_NUMBER].text = enemy.centerSymbol
        enemyCells[ENEMY_CENTER_CELL_NUMBER].setTextColor(activity.getColorById(enemy.centerSymbolColorId))

        enemyCells[0].text = (enemy.number / 10).toString()
        enemyCells[2].text = (enemy.number % 10).toString()

        enemyCells[ENEMY_LEGS_CELL_NUMBER].text = enemy.legsSymbol

        enemyCells[3].text = enemy.handsSymbol
        enemyCells[5].text = enemy.handsSymbol
    }

    override fun updateEnemyPanel(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        val face = enemy.defaultFace
        if (enemyFaceCell.text != face) {
            enemyFaceCell.text = face
        }

        val centerSymbol = enemy.centerSymbol
        if (enemyCells[ENEMY_CENTER_CELL_NUMBER].text != centerSymbol) {
            enemyCells[ENEMY_CENTER_CELL_NUMBER].text = centerSymbol
        }
    }

    companion object {
        private const val ENEMY_FACE_CELL_NUMBER = 1
        private const val ENEMY_CENTER_CELL_NUMBER = 4
        private const val ENEMY_LEGS_CELL_NUMBER = 7
    }
}
