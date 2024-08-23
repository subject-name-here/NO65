package com.unicorns.invisible.no65.view

import android.graphics.ColorMatrixColorFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import com.lb.auto_fit_textview.AutoResizeTextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleFieldObject
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldFighter
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.*
import com.unicorns.invisible.no65.view.battlefield.BattleFieldAnimator
import com.unicorns.invisible.no65.view.music.MusicPlayer
import com.unicorns.invisible.no65.view.speech.SpeechProperties
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates


abstract class BattleFieldDrawer(
    protected val activity: MainActivity,
    private val fieldWidth: Int,
    private val fieldHeight: Int
) {
    protected val animator = BattleFieldAnimator(activity)

    abstract val mainBattleLayout: ConstraintLayout
    abstract val battleScreen: ConstraintLayout

    abstract val upperBar: TextView

    abstract val enemyName: TextView
    abstract val enemyInnerPanel: ConstraintLayout
    abstract val enemyProgressBar: ProgressBar
    abstract val enemyHealthBar: ProgressBar

    abstract val field: LinearLayout
    abstract val battleOverCell: TextView

    abstract val giveUpButton: TextView

    abstract val enemyCellsLayout: ConstraintLayout
    abstract val enemyHealthDecreaseField: TextView
    abstract val enemySpeechBubble: TextView
    abstract val enemySpeechBubbleLayout: LinearLayout
    abstract val enemyFaceCell: TextView
    abstract val enemyCentreCell: TextView

    abstract val protagonistHealthBar: ProgressBar

    abstract val loadingLayout: ConstraintLayout

    private var areColorsInverted: Boolean = false
    private val buttonNumberColorId: Int
        get() = if (areColorsInverted) R.color.anti_light_grey else R.color.light_grey
    private val projectileArrowColorId: Int
        get() = if (areColorsInverted) R.color.white else R.color.black

    open fun init() = launchCoroutineOnMain {
        field.removeAllViews()

        val cutoutHeight = ScreenDimensions.getCutoutHeight(activity)
        upperBar.height = cutoutHeight
        upperBar.text = upperBar.text.repeat(65)

        val rowParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f / fieldHeight
        )
        val buttonParam = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f / fieldWidth
        )

        for (i in 0 until fieldHeight) {
            val row = LinearLayout(activity).apply { orientation = LinearLayout.HORIZONTAL }
            row.layoutParams = rowParam
            field.addView(row)
            for (j in 0 until fieldWidth) {
                val button = AutoResizeTextView(activity)
                setTextAppearance(button, R.style.battlefield_button_style)
                button.layoutParams = buttonParam
                button.setPadding(8, 12, 8, 12)
                button.gravity = Gravity.CENTER
                button.maxLines = 1
                button.setMinTextSize(2f)
                button.textSize = 200f
                button.background = ResourcesCompat.getDrawable(activity.resources, R.drawable.button_back, activity.theme)
                row.addView(button)
            }
        }

        enemyProgressBar.max = PROGRESS_BAR_MAX_VALUE
        enemyHealthBar.max = PROGRESS_BAR_MAX_VALUE
    }

    fun initEnemy(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        enemyName.text = activity.getString(enemy.nameId)
        (enemySpeechBubble as AutoResizeTextView).setMinTextSize(2f)
        initEnemyPanel(enemy)
    }
    abstract fun initEnemyPanel(enemy: BattleFieldCharacter): Job
    fun initProtagonist(protagonist: BattleFieldFighter) = launchCoroutineOnMain {
        initProtagonistPanel(protagonist)
    }
    abstract fun initProtagonistPanel(protagonist: BattleFieldFighter): Job

    fun hideLoadingLayout() = launchCoroutineOnMain {
        loadingLayout.visibility = View.GONE
    }

    fun drawField(battleField: BattleField) = launchCoroutineOnMain {
        val coordinatesToObjects = battleField.getMap()
        for (i in 0 until fieldHeight) {
            val row = field.getChildAt(battleField.rowsOrder[i]) as LinearLayout
            for (j in 0 until fieldWidth) {
                val button = row.getChildAt(j) as TextView
                val coordinates = Coordinates(i, j)
                val battleFieldObject = coordinatesToObjects[coordinates]
                writeBattleFieldObjectOnView(battleFieldObject, button, coordinates)
            }
        }

        updateEnemy(battleField.enemy)
        updateProtagonist(battleField.protagonist)
    }
    private fun writeBattleFieldObjectOnView(
        battleFieldObject: BattleFieldObject?,
        view: TextView,
        coordinates: Coordinates
    ) {
        if (battleFieldObject == null) {
            val cellNumber = coordinates.row * fieldWidth + coordinates.col
            view.text = cellNumber.toString()
            view.setTextColor(activity.getColorById(buttonNumberColorId))
        } else {
            val objectString = battleFieldObject.getString()
            var buttonText = objectString
            if (battleFieldObject is BattleFieldProjectile) {
                buttonText += battleFieldObject.direction.getString()
            }
            val spanText: Spannable = SpannableString(buttonText)

            var objectColor = battleFieldObject.getStringColor()
            if (areColorsInverted && (objectColor == R.color.black || objectColor == R.color.almost_black)) {
                objectColor = R.color.white
            }
            spanText.setSpan(
                ForegroundColorSpan(activity.getColorById(objectColor)),
                0,
                objectString.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            if (buttonText.length > objectString.length) {
                spanText.setSpan(
                    ForegroundColorSpan(activity.getColorById(projectileArrowColorId)),
                    objectString.length,
                    objectString.length + 1,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }

            view.text = spanText
        }
    }

    fun updateEnemy(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        updateEnemyHealthBar(enemy)
        updateEnemyPanel(enemy)
    }
    open fun updateEnemyHealthBar(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        setEnemyHealthBarPercentage((100f * enemy.health / enemy.maxHealth).toInt())
    }
    abstract fun updateEnemyPanel(enemy: BattleFieldCharacter): Job
    fun updateEnemyFace(enemy: BattleFieldCharacter) = launchCoroutineOnMain {
        enemyFaceCell.text = enemy.defaultFace
    }

    private var prevHealth = 0
    private var protagonistHealthDecreaseQueueSize: Int by Delegates.observable(0) { _, _, new ->
        if (new == 0) {
            hideProtagonistHealthDecrease()
        }
    }
    fun updateProtagonist(protagonist: BattleFieldFighter) {
        if (protagonist.health in 1 until prevHealth) {
            activity.musicPlayer.playMusic(
                R.raw.sfx_damage,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = false
            )
            animator.animateBlink(protagonistHealthBar)

            animateProtagonistFaceOnDamage(protagonist)

            protagonistHealthDecreaseQueueSize++
            showProtagonistHealthDecrease(prevHealth - protagonist.health)
            launchCoroutine {
                delay(2000L)
                --protagonistHealthDecreaseQueueSize
            }
        }

        prevHealth = protagonist.health
        launchCoroutineOnMain {
            setProtagonistHealthBarPercentage(
                (100f * protagonist.health / protagonist.maxHealth).toInt()
            )
            drawProtagonistData(protagonist)
        }
    }
    fun updateProtagonistInstantly(protagonist: BattleFieldFighter) {
        prevHealth = protagonist.health
        launchCoroutineOnMain {
            setProtagonistHealthBarPercentage(
                (100f * protagonist.health / protagonist.maxHealth).toInt(),
                animationDuration = 0L
            )
            drawProtagonistData(protagonist)
        }
    }
    abstract fun drawProtagonistData(protagonist: BattleFieldFighter): Job
    open fun showProtagonistHealthDecrease(delta: Int) {}
    open fun hideProtagonistHealthDecrease() {}
    open fun animateProtagonistFaceOnDamage(protagonist: BattleFieldFighter) {}

    open fun showField() = launchCoroutineOnMain {
        field.visibility = View.VISIBLE
    }
    open fun hideAll() = launchCoroutineOnMain {
        field.visibility = View.INVISIBLE
    }

    fun setEnemyProgressBarPercentage(progress: Int, durationMSec: Long) = launchCoroutineOnMain {
        setProgressBarPercentage(enemyProgressBar, progress, durationMSec)
    }
    private fun setProtagonistHealthBarPercentage(
        progress: Int,
        animationDuration: Long = HEALTH_BAR_ANIMATION_DURATION
    ) {
        setProgressBarPercentage(protagonistHealthBar, progress, animationDuration)
    }
    private fun setEnemyHealthBarPercentage(progress: Int) {
        setProgressBarPercentage(enemyHealthBar, progress, HEALTH_BAR_ANIMATION_DURATION)
    }
    protected fun setProgressBarPercentage(progressBar: ProgressBar, progress: Int, durationMSec: Long) {
        val progressInProgressBar = progress * PROGRESS_BAR_MAX_VALUE / 100
        if (durationMSec == 0L) {
            progressBar.progress = progressInProgressBar
            return
        }
        animator.animateProgressBar(progressBar, progressInProgressBar, durationMSec)
    }

    protected fun showEnemyHealthDecrease(healthDecreaseText: String) = launchCoroutineOnMain {
        enemyHealthDecreaseField.text = healthDecreaseText
        enemyHealthDecreaseField.visibility = View.VISIBLE
        val appearAnimation = AnimationUtils.loadAnimation(activity, R.anim.appear)
        enemyHealthDecreaseField.startAnimation(appearAnimation)
    }
    protected fun hideEnemyHealthDecrease() = launchCoroutineOnMain {
        enemyHealthDecreaseField.visibility = View.INVISIBLE
    }


    fun dissolveEnemy(enemy: BattleFieldCharacter) = launchCoroutine {
        launchCoroutineOnMain {
            enemyFaceCell.text = enemy.defaultFace
        }
        dissolveCell(enemyFaceCell)
    }
    abstract fun dissolveProtagonist(protagonist: BattleFieldFighter): Job

    protected suspend fun dissolveCell(faceCell: TextView) = suspendCoroutine { cont ->
        launchCoroutineOnMain {
            faceCell.visibility = View.INVISIBLE

            battleOverCell.text = faceCell.text
            setTextAppearance(battleOverCell, R.style.dissolve_style)
            if (areColorsInverted) {
                battleOverCell.setTextColor(activity.getColorById(R.color.white))
            }

            animator.animateView(battleOverCell, R.anim.dissolve) {
                faceCell.visibility = View.INVISIBLE
                cont.resume(Unit)
            }
        }
    }

    suspend fun undissolveEnemy(enemy: BattleFieldCharacter) = suspendCoroutine { cont ->
        launchCoroutineOnMain {
            battleOverCell.text = enemy.defaultFace
            setTextAppearance(battleOverCell, R.style.dissolve_style)
            if (areColorsInverted) {
                battleOverCell.setTextColor(activity.getColorById(R.color.white))
            }

            animator.animateView(battleOverCell, R.anim.undissolve) {
                cont.resume(Unit)
            }
        }
    }

    fun clearBattleOverCell() = launchCoroutineOnMain {
        battleOverCell.text = ""
    }
    fun writeInBattleOverCell(text: String) = launchCoroutineOnMain {
        battleOverCell.text = text
    }
    fun writeInBattleOverCell(textId: Int) = launchCoroutineOnMain {
        battleOverCell.text = activity.getString(textId)
    }


    private var currentJob: Job? = null
    protected suspend fun showTextInSpeechBubbleSuspend(
        text: String,
        bubbleLayout: LinearLayout,
        bubble: TextView,
        withDelay: Boolean = true
    ) {
        if (text == "") {
            return
        }

        if (currentJob != null) {
            stopSpeechBubble()
        }

        val speechSoundId = R.raw.sfx_tap
        suspendCoroutine { cont ->
            currentJob = launchCoroutineOnMain {
                showSpeechBubble(bubbleLayout)
                activity.musicPlayer.playMusic(
                    speechSoundId,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = true
                )

                repeat(text.length) {
                    delay(SpeechProperties.MILLISECONDS_PER_SYMBOL)
                    writeInSpeechBubble(text.subSequence(0, it + 1).toString(), bubble)
                }
                activity.musicPlayer.stopMusicByResourceId(speechSoundId)
                if (withDelay) {
                    delay(SpeechProperties.DELAY_AFTER_MESSAGE)
                }
            }.apply {
                invokeOnCompletion {
                    hideSpeechBubble(bubbleLayout)
                    writeInSpeechBubble("", bubble)
                    activity.musicPlayer.stopMusicByResourceId(speechSoundId)
                    cont.resume(Unit)
                }
            }
        }
    }
    suspend fun showTextInEnemySpeechBubbleSuspend(
        textId: Int,
        withDelay: Boolean = true
    ) = showTextInSpeechBubbleSuspend(activity.getString(textId), enemySpeechBubbleLayout, enemySpeechBubble, withDelay)
    fun showTextInEnemySpeechBubble(
        textId: Int
    ) = launchCoroutine {
        showTextInEnemySpeechBubbleSuspend(textId)
    }
    suspend fun showTextInEnemySpeechBubbleFormattedSuspend(
        textId: Int,
        withDelay: Boolean = true,
        vararg formatArgs: Any
    ) {
        val argsList = formatArgs.asList()
        val processedArgsList = SpeechUtils.processFormatArgs(argsList, activity)
        val text = activity.getString(textId, *processedArgsList.toTypedArray())
        showTextInSpeechBubbleSuspend(text, enemySpeechBubbleLayout, enemySpeechBubble, withDelay)
    }


    fun stopSpeechBubble() {
        if (currentJob?.isCompleted == false) {
            currentJob?.cancel()
        }
    }
    private fun showSpeechBubble(bubbleLayout: LinearLayout) = launchCoroutineOnMain {
        bubbleLayout.visibility = View.VISIBLE
    }
    private fun hideSpeechBubble(bubbleLayout: LinearLayout) = launchCoroutineOnMain {
        bubbleLayout.visibility = View.INVISIBLE
    }
    private fun writeInSpeechBubble(text: String, bubble: TextView) = launchCoroutineOnMain {
        bubble.text = text
    }

    open fun animateEnemy(animation: CharacterAnimation) = animator.animateEnemy(animation, enemyCellsLayout)
    protected fun animateEnemyEvasion() = animator.animateEvasion(enemyCellsLayout)
    fun stopEnemyAnimation() = animator.stopEnemyAnimation(enemyCellsLayout)
    fun pauseEnemyAnimation() = animator.pauseEnemyAnimation(enemyCellsLayout)
    fun resumeEnemyAnimation() = animator.resumeEnemyAnimation(enemyCellsLayout)

    suspend fun enemyLeaveTheScene() = animator.characterLeavesScene(enemyCellsLayout)
    suspend fun enemyReturnToTheScene() = animator.characterReturnsOnScene(enemyCellsLayout)

    fun showGiveUpButton() = launchCoroutineOnMain {
        giveUpButton.visibility = View.VISIBLE
    }
    fun hideGiveUpButtonSoft() = launchCoroutineOnMain {
        giveUpButton.visibility = View.INVISIBLE
    }
    fun hideGiveUpButton() = launchCoroutineOnMain {
        giveUpButton.visibility = View.GONE
    }
    fun hideEnemyHealthBarWithAnimation() = launchCoroutineOnMain {
        setProgressBarPercentage(enemyHealthBar, 0, HEALTH_BAR_ANIMATION_DURATION)
        delay(HEALTH_BAR_ANIMATION_DURATION)
        enemyHealthBar.visibility = View.INVISIBLE
    }

    fun invertColors() = launchCoroutineOnMain {
        areColorsInverted = true
        DrawableCompat.setTint(enemyProgressBar.progressDrawable, activity.getColorById(R.color.white))
        mainBattleLayout.setBackgroundColor(activity.getColorById(R.color.black))

        val q = ArrayDeque<View>()
        q.add(mainBattleLayout)

        while (q.isNotEmpty()) {
            when (val view = q.removeFirst()) {
                is TextView -> {
                    view.setTextColor(activity.getColorById(R.color.white))
                    view.background?.colorFilter = ColorMatrixColorFilter(NEGATIVE)
                }
                is ConstraintLayout -> {
                    q.addAll(view.children)
                    if (view != mainBattleLayout) {
                        view.background?.colorFilter = ColorMatrixColorFilter(NEGATIVE)
                    }
                }
                is LinearLayout -> {
                    q.addAll(view.children)
                    view.background?.colorFilter = ColorMatrixColorFilter(NEGATIVE)
                }
            }
        }

        enemyHealthDecreaseField.setTextColor(activity.getColorById(R.color.red))
        if (this is BattleFieldDrawerStandard) {
            ankhPlace.setTextColor(activity.getColorById(R.color.light_grey))
        }
    }

    fun showBlackScreen() = launchCoroutineOnMain {
        battleScreen.visibility = View.VISIBLE
    }
    fun stopBlackScreen() = launchCoroutineOnMain {
        battleScreen.visibility = View.GONE
    }

    fun rotateScreen180() = launchCoroutineOnMain {
        mainBattleLayout.rotation = 180f
        battleOverCell.rotation = 180f
    }
    fun rotateScreen0() = launchCoroutineOnMain {
        mainBattleLayout.rotation = 0f
        battleOverCell.rotation = 0f
    }

    companion object {
        const val HEALTH_BAR_ANIMATION_DURATION = 1000L
        const val PROGRESS_BAR_MAX_VALUE = 10000

        const val OFFENSIVE_ATTACK_DURATION = 1000L

        private val NEGATIVE = floatArrayOf(
            -1.0f,     .0f,     .0f,    .0f,  255.0f,
            .0f,   -1.0f,     .0f,    .0f,  255.0f,
            .0f,     .0f,   -1.0f,    .0f,  255.0f,
            .0f,     .0f,     .0f,   1.0f,     .0f
        )
    }
}
