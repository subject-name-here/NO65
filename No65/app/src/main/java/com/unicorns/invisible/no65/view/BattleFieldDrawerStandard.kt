package com.unicorns.invisible.no65.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityBattleBinding
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldFighter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.elements.trigram.*
import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.util.ScreenDimensions
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


abstract class BattleFieldDrawerStandard(
    activity: MainActivity,
    fieldWidth: Int,
    fieldHeight: Int,
    private val binding: ActivityBattleBinding
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

    val elements: ConstraintLayout
        get() = binding.elements
    val elementsLinearLayout: LinearLayout
        get() = binding.elementsLinearLayout
    private val trigramElements by lazy {
        listOf(
            binding.trigramElement0,
            binding.trigramElement1,
            binding.trigramElement2,
        )
    }

    override val loadingLayout: ConstraintLayout
        get() = binding.loadingLayout

    final override val protagonistHealthBar
        get() = binding.protagonistHealthBar
    private val protagonistImageCell
        get() = binding.protagonistIcon
    private val protagonistModifiersLeftGuideline
        get() = binding.modifiersLeftGuideline
    private val protagonistHealthBarGuideline
        get() = binding.protagonistHealthBarGuideline
    private val multiplierPlace
        get() = binding.multiplierPlace
    val ankhPlace: TextView
        get() = binding.ankhPlace

    private val lowerBar
        get() = binding.lowerBar

    override fun init(): Job {
        return launchCoroutineOnMain {
            super.init().join()

            val cutoutHeight = ScreenDimensions.getCutoutHeight(activity)
            lowerBar.height = cutoutHeight
            lowerBar.text = lowerBar.text.repeat(65)

            protagonistHealthBar.max = PROGRESS_BAR_MAX_VALUE
        }
    }

    override fun initProtagonistPanel(protagonist: BattleFieldFighter): Job = launchCoroutineOnMain {
        if (protagonist is BattleFieldProtagonist) {
            changeModifiersVisibilityFromKnowledge(protagonist.knowledge)
        }
    }
    private fun changeModifiersVisibilityFromKnowledge(knowledge: Knowledge) {
        var guidelinePercentage = 0.8f

        if (!knowledge.knowsTrigram(Wind)) {
            multiplierPlace.visibility = View.GONE
            guidelinePercentage += 0.1f
        }
        if (!knowledge.knowsTrigram(Fire)) {
            ankhPlace.visibility = View.GONE
            guidelinePercentage += 0.1f
        }

        protagonistModifiersLeftGuideline.setGuidelinePercent(guidelinePercentage)
        protagonistHealthBarGuideline.setGuidelinePercent(guidelinePercentage)
    }

    fun updateProtagonistHealthBarLimiter(protagonist: BattleFieldProtagonist) = launchCoroutineOnMain {
        val left = 0.1f
        val right = (protagonistHealthBarGuideline.layoutParams as ConstraintLayout.LayoutParams).guidePercent

        val guidelinePercentage = left + (right - left) * (protagonist.maxHealth.toFloat() / BattleFieldProtagonist.BASIC_HEALTH.toFloat())
        protagonistHealthBarGuideline.setGuidelinePercent(guidelinePercentage)
    }

    override fun drawProtagonistData(protagonist: BattleFieldFighter) = launchCoroutineOnMain {
        (protagonist as BattleFieldProtagonist).apply {
            multiplierPlace.text = if (hasWindRequiem) {
                activity.getString(R.string.infinity_placeholder)
            } else {
                activity.getString(R.string.multiplier_placeholder).format(multiplier)
            }
            val ankhColor = if (hasAnkh) {
                R.color.orange
            } else {
                R.color.light_grey
            }
            ankhPlace.setTextColor(activity.getColorById(ankhColor))
        }
    }

    override fun dissolveProtagonist(protagonist: BattleFieldFighter): Job = launchCoroutineOnMain {
        dissolveCell(protagonistImageCell)
    }

    fun showElements() = launchCoroutineOnMain {
        field.visibility = View.INVISIBLE
        elements.visibility = View.VISIBLE
    }
    override fun showField() = launchCoroutineOnMain {
        super.showField()
        elements.visibility = View.INVISIBLE
    }
    override fun hideAll() = launchCoroutineOnMain {
        super.hideAll()
        elements.visibility = View.INVISIBLE
    }

    fun setElementText(elementsCounter: Int, symbol: String) {
        if (elementsCounter >= trigramElements.size) return
        launchCoroutineOnMain {
            trigramElements[elementsCounter].text = symbol
        }
    }

    suspend fun showTrigramTriggered(
        result: AttackResult,
        enemy: BattleFieldEnemy,
        element: Trigram
    ) {
        when {
            element is AttackTrigram -> {
                val healthDecreaseText = when (result.type) {
                    AttackResult.DamageType.EVADED -> {
                        activity.getString(R.string.evaded)
                    }
                    AttackResult.DamageType.CASTED_UNKNOWN_TRIGRAM -> {
                        activity.getString(R.string.failed)
                    }
                    AttackResult.DamageType.WIND_REQUIEM_DEFLECTED -> {
                        activity.getString(R.string.blocked)
                    }
                    AttackResult.DamageType.HIT_INFINITY -> {
                        activity.getString(R.string.ignored)
                    }
                    AttackResult.DamageType.WIND_REQUIEM -> {
                        activity.getString(R.string.damage_infinity)
                    }
                    else -> activity.getString(R.string.damage_placeholder).format(result.damage)
                }
                showEnemyHealthDecrease(healthDecreaseText)

                if (result.type == AttackResult.DamageType.EVADED) {
                    animateEnemyEvasion()
                }

                if (result.damage == 0) {
                    triggeredAttackNoDamage(enemy)
                } else {
                    triggeredAttackWithDamage(enemy)
                }
                hideEnemyHealthDecrease()
            }
            result.type == AttackResult.DamageType.CASTED_UNKNOWN_TRIGRAM -> {
                val healthDecreaseText = activity.getString(R.string.failed)
                showEnemyHealthDecrease(healthDecreaseText)
                delay(OFFENSIVE_ATTACK_DURATION)
                hideEnemyHealthDecrease()
            }
            result.type == AttackResult.DamageType.TIMEBACK_DENIED -> {
                val healthDecreaseText = activity.getString(R.string.denied)
                showEnemyHealthDecrease(healthDecreaseText)
                delay(OFFENSIVE_ATTACK_DURATION)
                hideEnemyHealthDecrease()
            }
            else -> {
                triggeredDefensiveAttack(element)
            }
        }
    }
    private suspend fun triggeredDefensiveAttack(element: Trigram) = suspendCoroutine { cont ->
        val protagonistRequestedPanel = when (element) {
            is Fire -> {
                activity.musicPlayer.playMusic(
                    R.raw.sfx_lighter,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
                ankhPlace
            }
            is Wind -> {
                activity.musicPlayer.playMusic(
                    R.raw.sfx_wind,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
                multiplierPlace
            }
            is Lake -> {
                activity.musicPlayer.playMusic(
                    R.raw.sfx_heal,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
                protagonistHealthBar
            }
            Heaven -> {
                launchCoroutine {
                    activity.musicPlayer.playMusicSuspendTillEnd(
                        R.raw.sfx_knocks_reversed,
                        behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                        isLooping = false
                    )
                    cont.resume(Unit)
                }
                return@suspendCoroutine
            }
            else -> {
                cont.resume(Unit)
                return@suspendCoroutine
            }
        }

        animator.animateBlinkWithCallback(protagonistRequestedPanel) {
            cont.resume(Unit)
        }
    }
    private suspend fun triggeredAttackNoDamage(enemy: BattleFieldEnemy) {
        launchCoroutineOnMain {
            enemyFaceCell.text = enemy.noDamageReceivedFace
            delay(OFFENSIVE_ATTACK_DURATION)
            enemyFaceCell.text = enemy.defaultFace
        }.join()
    }
    private suspend fun triggeredAttackWithDamage(enemy: BattleFieldEnemy) = suspendCoroutine { cont ->
        activity.musicPlayer.playMusic(
            R.raw.sfx_damage_enemy_2,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )

        launchCoroutineOnMain {
            enemyFaceCell.text = enemy.damageReceivedFace
        }

        animator.animateBlinkWithCallback(enemyFaceCell) {
            launchCoroutineOnMain {
                enemyFaceCell.text = enemy.defaultFace
            }
            cont.resume(Unit)
        }
    }

    fun clearElements() = launchCoroutineOnMain {
        trigramElements.forEach { trigramElement ->
            trigramElement.text = ""
            trigramElement.scaleX = 3f
        }
    }
    fun scrambleTrigram() = launchCoroutineOnMain {
        trigramElements.forEach { trigramElement ->
            trigramElement.text = activity.getString(R.string.scrambled_element)
            trigramElement.scaleX = 1f
        }
    }
}