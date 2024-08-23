package com.unicorns.invisible.no65.view

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.lb.auto_fit_textview.AutoResizeTextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityBattleEqualBinding
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacterEqual
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldFighter
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


class BattleFieldDrawerEqual(
    activity: MainActivity,
    fieldWidth: Int,
    fieldHeight: Int,
    private val binding: ActivityBattleEqualBinding
) : BattleFieldDrawer(activity, fieldWidth, fieldHeight) {
    override val mainBattleLayout
        get() = binding.mainBattleLayout
    override val battleScreen
        get() = binding.battleScreen

    override val field
        get() = binding.field

    override val upperBar
        get() = binding.upperBar

    override val enemyName
        get() = binding.enemyName
    override val enemyInnerPanel
        get() = binding.enemyInnerPanel
    override val enemyProgressBar
        get() = binding.enemyProgressBar
    override val enemyHealthBar
        get() = binding.enemyHealthBar

    override val battleOverCell
        get() = binding.battleOverCell

    override val giveUpButton
        get() = binding.giveUpButton

    override val loadingLayout: ConstraintLayout
        get() = binding.loadingLayout

    private val enemyCells: List<TextView> by lazy {
        binding.enemyInclude.enemyCells.characterCellsHolder.children
            .filterIsInstance<TextView>()
            .toList()
    }
    private val protagonistCellsWrapper: ConstraintLayout
        get() = binding.protagonistInclude.enemyCellsWrapper
    private val protagonistCells: List<TextView> by lazy {
        binding.protagonistInclude.enemyCells.characterCellsHolder.children
            .filterIsInstance<TextView>()
            .toList()
    }

    override val enemyCellsLayout: ConstraintLayout
        get() = binding.enemyInclude.enemyCellsWrapper
    override val enemyHealthDecreaseField: TextView
        get() = binding.enemyInclude.healthDecreaseField
    override val enemySpeechBubble: TextView
        get() = binding.enemyInclude.speechBubble
    override val enemySpeechBubbleLayout: LinearLayout
        get() = binding.enemyInclude.speechBubbleLayout
    override val enemyFaceCell: TextView
        get() = enemyCells[ENEMY_FACE_CELL_NUMBER]
    override val enemyCentreCell: TextView
        get() = enemyCells[ENEMY_CENTER_CELL_NUMBER]
    private val enemyIcon: TextView
        get() = binding.enemyInclude.enemyIcon

    private val protagonistCellsLayout: ConstraintLayout
        get() = binding.protagonistInclude.enemyCells.root
    override val protagonistHealthBar: ProgressBar
        get() = binding.protagonistHealthBar
    private val protagonistProgressBar: ProgressBar
        get() = binding.protagonistProgressBar
    private val protagonistFaceCell: TextView
        get() = protagonistCells[ENEMY_FACE_CELL_NUMBER]
    private val protagonistIcon: TextView
        get() = binding.protagonistInclude.enemyIcon
    private val protagonistName: TextView
        get() = binding.protagonistName
    private val protagonistHealthDecreaseField: TextView
        get() = binding.protagonistInclude.healthDecreaseField

    private val protagonistSpeechBubbleLayout: LinearLayout
        get() = binding.protagonistInclude.speechBubbleLayout
    private val protagonistSpeechBubble: TextView
        get() = binding.protagonistInclude.speechBubble

    override fun init(): Job {
        return launchCoroutineOnMain {
            super.init().join()

            protagonistProgressBar.max = PROGRESS_BAR_MAX_VALUE
            protagonistHealthBar.max = PROGRESS_BAR_MAX_VALUE

            protagonistName.rotation = 180f
            protagonistCellsWrapper.rotation = 180f
            protagonistIcon.rotation = 180f
            protagonistHealthDecreaseField.rotation = 180f
            protagonistSpeechBubbleLayout.rotationX = 180f
            protagonistSpeechBubble.rotation = 180f
            protagonistSpeechBubble.rotationX = 180f
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

    private var enemyPrevHealth = 0
    private var enemyHealthDecreaseQueueSize = 0
    private var currentEnemyDamage = 0
    override fun updateEnemyHealthBar(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        super.updateEnemyHealthBar(enemy)

        if (enemy.health in 1 until enemyPrevHealth) {
            activity.musicPlayer.playMusic(
                R.raw.sfx_damage_enemy_2,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = false
            )

            enemyHealthDecreaseQueueSize++
            currentEnemyDamage += enemyPrevHealth - enemy.health
            showEnemyHealthDecrease(activity.getString(R.string.damage_placeholder).format(currentEnemyDamage))
            launchCoroutineOnMain {
                enemyFaceCell.text = enemy.damageReceivedFace
                animator.animateFastBlinkWithCallback(enemyFaceCell) {
                    launchCoroutineOnMain {
                        enemyFaceCell.text = enemy.defaultFace
                    }
                }
            }
            launchCoroutine {
                delay(2000L)
                if (--enemyHealthDecreaseQueueSize == 0) {
                    hideEnemyHealthDecrease()
                    currentEnemyDamage = 0
                }
            }
        }
        enemyPrevHealth = enemy.health
    }

    override fun initProtagonistPanel(protagonist: BattleFieldFighter) = launchCoroutineOnMain {
        val protagonistEqual = protagonist as BattleFieldCharacterEqual
        protagonistIcon.text = protagonistEqual.hexagramSymbol

        protagonistName.text = activity.getString(protagonistEqual.nameId)

        protagonistFaceCell.text = protagonistEqual.defaultFace
        protagonistCells[ENEMY_CENTER_CELL_NUMBER].text = protagonistEqual.centerSymbol
        protagonistCells[ENEMY_CENTER_CELL_NUMBER].setTextColor(activity.getColorById(protagonistEqual.centerSymbolColorId))

        protagonistCells[0].text = (protagonistEqual.number / 10).toString()
        protagonistCells[2].text = (protagonistEqual.number % 10).toString()

        protagonistCells[ENEMY_LEGS_CELL_NUMBER].text = protagonistEqual.legsSymbol

        protagonistCells[3].text = protagonistEqual.handsSymbol
        protagonistCells[5].text = protagonistEqual.handsSymbol

        (protagonistSpeechBubble as AutoResizeTextView).setMinTextSize(2f)

    }

    override fun drawProtagonistData(protagonist: BattleFieldFighter) = launchCoroutineOnMain {}
    private var currentProtagonistDamage = 0
    override fun showProtagonistHealthDecrease(delta: Int) {
        currentProtagonistDamage += delta
        launchCoroutineOnMain {
            protagonistHealthDecreaseField.text = activity.getString(R.string.damage_placeholder).format(currentProtagonistDamage)
            protagonistHealthDecreaseField.visibility = View.VISIBLE
            val appearAnimation = AnimationUtils.loadAnimation(activity, R.anim.appear)
            protagonistHealthDecreaseField.startAnimation(appearAnimation)
        }
    }
    override fun hideProtagonistHealthDecrease() {
        currentProtagonistDamage = 0
        launchCoroutineOnMain {
            protagonistHealthDecreaseField.visibility = View.INVISIBLE
        }
    }

    override fun animateProtagonistFaceOnDamage(protagonist: BattleFieldFighter) {
        val protagonistEqual = protagonist as BattleFieldCharacterEqual
        launchCoroutineOnMain {
            protagonistFaceCell.text = protagonistEqual.damageReceivedFace
            animator.animateFastBlinkWithCallback(protagonistFaceCell) {
                launchCoroutineOnMain {
                    protagonistFaceCell.text = protagonistEqual.defaultFace
                }
            }
        }
    }

    fun setProtagonistProgressBarPercentage(progress: Int, durationMSec: Long) = launchCoroutineOnMain {
        setProgressBarPercentage(protagonistProgressBar, progress, durationMSec)
    }

    fun animateProtagonist(animation: CharacterAnimation) = animator.animateProtagonist(animation, protagonistCellsLayout)
    fun stopProtagonistAnimation() = animator.stopProtagonistAnimation(protagonistCellsLayout)
    override fun dissolveProtagonist(protagonist: BattleFieldFighter) = launchCoroutineOnMain {
        protagonistFaceCell.text = (protagonist as BattleFieldCharacterEqual).defaultFace
        dissolveCell(protagonistFaceCell)
    }

    suspend fun showTextInProtagonistSpeechBubbleSuspend(
        textId: Int,
        withDelay: Boolean = true
    ) = showTextInSpeechBubbleSuspend(activity.getString(textId), protagonistSpeechBubbleLayout, protagonistSpeechBubble, withDelay = withDelay)
    fun showTextInProtagonistSpeechBubble(textId: Int) = launchCoroutine {
        showTextInProtagonistSpeechBubbleSuspend(textId)
    }


    override fun showField() = launchCoroutineOnMain {
        super.showField()
        field.rotation = 0f
    }
    fun showFieldReversed() = launchCoroutineOnMain {
        field.visibility = View.VISIBLE
        field.rotation = 180f
    }

    companion object {
        private const val ENEMY_FACE_CELL_NUMBER = 1
        private const val ENEMY_CENTER_CELL_NUMBER = 4
        private const val ENEMY_LEGS_CELL_NUMBER = 7
    }
}