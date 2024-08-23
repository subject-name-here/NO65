package com.unicorns.invisible.no65.view

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lb.auto_fit_textview.AutoResizeTextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityBattleBinding
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


class BattleFieldDrawerD99(
    activity: MainActivity,
    fieldWidth: Int,
    fieldHeight: Int,
    private val binding: ActivityBattleBinding,
) : BattleFieldDrawerStandard(activity, fieldWidth, fieldHeight, binding) {
    private val centerLeftCell: TextView
        get() = binding.enemyD99Include.enemyCellsD99.cell3D99
    private val centerCenterCell: TextView
        get() = binding.enemyD99Include.enemyCellsD99.cell4D99
    private val centerRightCell: TextView
        get() = binding.enemyD99Include.enemyCellsD99.cell5D99

    private val hand1: TextView
        get() = binding.enemyD99Include.hand1
    private val hand2: TextView
        get() = binding.enemyD99Include.hand2
    private val hand3: TextView
        get() = binding.enemyD99Include.hand3
    private val hand4: TextView
        get() = binding.enemyD99Include.hand4
    private val hand5: TextView
        get() = binding.enemyD99Include.hand5
    private val hand6: TextView
        get() = binding.enemyD99Include.hand6

    override val enemySpeechBubbleLayout: LinearLayout
        get() = binding.enemyD99Include.speechBubbleLayout
    override val enemySpeechBubble: TextView
        get() = binding.enemyD99Include.speechBubble
    override val enemyFaceCell: TextView
        get() = binding.enemyD99Include.enemyCellsD99.cell1D99
    override val enemyHealthDecreaseField: TextView
        get() = binding.enemyD99Include.healthDecreaseField
    override val enemyCellsLayout: ConstraintLayout
        get() = binding.enemyD99Include.enemyCellsD99.root
    override val enemyCentreCell: TextView
        get() = binding.enemyD99Include.enemyCellsD99.cell4D99

    private val enemyD99IncludeLayout
        get() = binding.enemyD99IncludeLayout

    override fun init(): Job {
        return launchCoroutineOnMain {
            super.init().join()
            enemyD99IncludeLayout.visibility = View.VISIBLE
            (enemySpeechBubble as AutoResizeTextView).setMinTextSize(2f)
        }
    }

    override fun initEnemyPanel(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        updateEnemyFace(enemy)
        centerLeftCell.text = activity.getString(R.string.d99_coming_to_meet_symbol)
        centerCenterCell.text = activity.getString(R.string.d99_deliverance_symbol)
        centerRightCell.text = activity.getString(R.string.d99_modesty_symbol)
        centerRightCell.setTextColor(activity.getColorById(R.color.red))
    }

    override fun updateEnemyPanel(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        val face = enemy.defaultFace
        if (enemyFaceCell.text != face) {
            enemyFaceCell.text = face
        }
    }

    fun prepareBlocksToFire() = launchCoroutineOnMain {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            1.0f,
        ).apply {
            duration = 250L
            fillAfter = true
        }
        hand1.startAnimation(animation)
        hand2.startAnimation(animation)
        hand3.startAnimation(animation)
        hand4.startAnimation(animation)
        hand5.startAnimation(animation)
        hand6.startAnimation(animation)
    }
    fun returnBlocks() = launchCoroutineOnMain {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
        ).apply {
            duration = 500L
        }
        hand1.startAnimation(animation)
        hand2.startAnimation(animation)
        hand3.startAnimation(animation)
        hand4.startAnimation(animation)
        hand5.startAnimation(animation)
        hand6.startAnimation(animation)

        launchCoroutine {
            delay(750L)
            launchHandsAnimation()
        }
    }

    private fun launchHandsAnimation() = launchCoroutineOnMain {
        val animation = AnimationUtils.loadAnimation(activity, R.anim.floating_d99)
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        hand1.startAnimation(animation)
        delay(150L)
        hand2.startAnimation(animation)
        delay(150L)
        hand3.startAnimation(animation)
        delay(150L)
        hand4.startAnimation(animation)
        delay(150L)
        hand5.startAnimation(animation)
        delay(150L)
        hand6.startAnimation(animation)
    }

    override fun animateEnemy(animation: CharacterAnimation) {
        super.animateEnemy(animation)
        launchHandsAnimation()
    }
}