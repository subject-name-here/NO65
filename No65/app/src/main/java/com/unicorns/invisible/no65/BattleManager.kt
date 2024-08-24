package com.unicorns.invisible.no65

import androidx.viewbinding.ViewBinding
import com.quickbirdstudios.nonEmptyCollection.list.NonEmptyList
import com.quickbirdstudios.nonEmptyCollection.toNonEmptyListOrNull
import com.unicorns.invisible.no65.controller.BattleFieldController
import com.unicorns.invisible.no65.controller.BattleFieldControllerStandard
import com.unicorns.invisible.no65.databinding.ActivityBattleBinding
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.elements.TrigramAggregator
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldFighter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.elements.monogram.MonogramGestureBijection
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.util.*
import com.unicorns.invisible.no65.view.*
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class BattleManager(
    val activity: MainActivity,
    private val protagonist: BattleFieldFighter,
    private val enemiesPool: NonEmptyList<BattleFieldCharacter>,
    private val managerMode: Mode = Mode.STANDARD,
    private val fieldWidth: Int = FIELD_WIDTH,
    private val fieldHeight: Int = FIELD_HEIGHT,
    private val afterBattleCallback: (BattleResult) -> Unit,
) {
    val drawer: BattleFieldDrawer by lazy {
        when (managerMode) {
            Mode.STANDARD, Mode.STANDARD_TUTORIAL -> {
                val standardBinding = binding as ActivityBattleBinding
                BattleFieldDrawerEnemy(activity, fieldWidth, fieldHeight, standardBinding)
            }
            Mode.STANDARD_D99 -> {
                val standardBinding = binding as ActivityBattleBinding
                BattleFieldDrawerD99(activity, fieldWidth, fieldHeight, standardBinding)
            }
            Mode.STANDARD_THE_CREATURE -> {
                val standardBinding = binding as ActivityBattleBinding
                BattleFieldDrawerTheCreature(activity, fieldWidth, fieldHeight, standardBinding)
            }
        }
    }

    val controller: BattleFieldController by lazy {
        val standardDrawer = drawer as BattleFieldDrawerStandard
        BattleFieldControllerStandard(
            activity,
            drawer.field,
            drawer.giveUpButton,
            standardDrawer.elementsLinearLayout,
            drawer.enemyCentreCell
        )
    }

    private val enemy = enemiesPool.first()

    private val aggregator = TrigramAggregator()

    private val battleFieldLock = ReentrantLock()
    val battleField = BattleField65(
        fieldWidth,
        fieldHeight,
        protagonist as BattleFieldProtagonist,
        enemy as BattleFieldEnemy
    )

    private val binding: ViewBinding = ActivityBattleBinding.inflate(activity.layoutInflater)

    private suspend fun init() = launchCoroutineOnMain {
        activity.setContentView(binding.root)

        drawer.init().join() // Initialize drawer before controller!!!
        if (managerMode == Mode.STANDARD_TUTORIAL) {
            drawer.hideGiveUpButton()
        } else {
            controller.setupGiveUpButton {
                protagonist.health = 0
                drawer.updateProtagonist(protagonist)
            }
        }
    }

    private enum class BattleFieldState {
        BATTLEFIELD,
        ELEMENTS,
        OTHER,
    }
    private var state = BattleFieldState.OTHER

    private var mainJob: Job? = null
    suspend fun launchBattle() {
        init().join()

        drawer.hideAll()
        drawer.initEnemy(enemy)
        drawer.initProtagonist(protagonist)
        drawer.updateEnemy(enemy)
        drawer.updateProtagonist(protagonist)
        drawer.clearBattleOverCell()

        drawer.hideLoadingLayout().join()

        delay(enemy.beforeCountdownDelay)

        enemy.goNumbersToDelays.forEach { numberToDelay ->
            activity.musicPlayer.playMusicSuspendTillStart(enemy.beatId)
            drawer.writeInBattleOverCell(numberToDelay.first.toString())
            delay(numberToDelay.second)
        }

        if (enemy.goText != "") {
            activity.musicPlayer.playMusicSuspendTillStart(enemy.beatId)
            drawer.writeInBattleOverCell(enemy.goText)
            delay(enemy.afterGoTextDelay)
        }
        drawer.clearBattleOverCell()

        enemy.onBattleBegins(this@BattleManager)

        activity.musicPlayer.playMusic(enemy.musicThemeId, isLooping = true)
        drawer.animateEnemy(enemy.animation)

        addProtagonistMovementListener()
        addElementsListener()
        launchMainJob()
    }
    private fun launchMainJob() {
        mainJob = launchCoroutineOnDefault {
            while (isActive) {
                proceedToField()
                onFieldEnd()
                proceedToElements()
                onElementsEnd()
            }
        }
    }

    private fun addProtagonistMovementListener() {
        controller.addListenersForButtons { coordinates, numberOfTaps ->
            val shuffledCoordinates = Coordinates(battleField.rowsOrder.indexOf(coordinates.row), coordinates.col)
            when (state) {
                BattleFieldState.BATTLEFIELD -> {
                    if (numberOfTaps == enemy.numberOfTaps) {
                        battleFieldLock.withLock {
                            battleField.changeProtagonistCoordinates(shuffledCoordinates)
                            onFieldChange()
                        }
                    }
                }
                else -> {}
            }
        }
    }
    private fun addElementsListener() {
        val controllerStandard = controller
        if (controllerStandard !is BattleFieldControllerStandard) return
        controllerStandard.addListenersForElementsLayout { type ->
            if (state != BattleFieldState.ELEMENTS) return@addListenersForElementsLayout
            val monogram = MonogramGestureBijection.getMonogram(type)
            aggregator.take(monogram)

            val drawerStandard = drawer as BattleFieldDrawerStandard
            drawerStandard.setElementText(
                aggregator.getLastMonogramNumber(),
                monogram.getSymbol()
            )
        }
    }

    private var lineId: Int = 0
    private suspend fun proceedToField() {
        battleField.onEnemyMoveStart()

        val isTakingFromJournal = battleField.takeFromJournal
        if (!isTakingFromJournal) {
            enemy.onMoveStart(battleField)
        }

        checkpoint()

        drawer.stopSpeechBubble()
        if (!isTakingFromJournal || enemy.lineGeneratorJournalOverride) {
            lineId = getEnemyLineGenerator().getLine(battleField.moveNumber)
        }
        drawer.showTextInEnemySpeechBubble(lineId)

        if (enemy.attackTimeMvs == 0) {
            return
        }

        battleField.sendProtagonistToCenter()

        drawer.setEnemyProgressBarPercentage(0, 0)
        drawer.showField()
        drawer.updateEnemyPanel(enemy)

        state = BattleFieldState.BATTLEFIELD
        repeat(enemy.attackTimeMvs) {
            onTickField(it)
            delay(enemy.tickTime)
        }
        state = BattleFieldState.OTHER
    }

    private fun onTickField(numberOfInvocation: Int) {
        val progress = getShiftedProgressPercentage(numberOfInvocation, enemy.attackTimeMvs)
        drawer.setEnemyProgressBarPercentage(progress, enemy.tickTime)

        battleFieldLock.withLock {
            battleField.preTickField()
            val isTakingFromJournal = battleField.takeFromJournal

            if (!isTakingFromJournal) {
                enemy.onTick(numberOfInvocation, battleField)
            }
            enemy.onTick(this, numberOfInvocation)

            if (!isTakingFromJournal) {
                repeat(enemy.projectilePassesCellsPerMove) {
                    battleField.getAllObjects()
                        .filterIsInstance<BattleFieldProjectile>()
                        .forEach { battleFieldObject ->
                            battleFieldObject.onTick(numberOfInvocation, battleField)
                        }
                }
            }

            battleField.setFieldPlayable()

            if (!isTakingFromJournal) {
                battleField.saveField()
            }

            onFieldChange()
        }
    }
    private fun onFieldChange() {
        drawer.drawField(battleField)
        checkpoint()
    }
    private suspend fun onFieldEnd() {
        battleField.onEnemyMoveEnd()

        battleField.clear()
        drawer.drawField(battleField)
        drawer.hideAll()
    }


    // NEXT ARE ELEMENTS FUNCTIONS

    private suspend fun proceedToElements() {
        val drawerStandard = drawer as BattleFieldDrawerStandard
        val protagonist65 = protagonist as BattleFieldProtagonist
        val enemy65 = enemy as BattleFieldEnemy
        val battleField65 = battleField as BattleField65

        if (enemy65.defenceTimeSec == 0) {
            return
        }
        drawerStandard.showElements()

        state = BattleFieldState.ELEMENTS
        val elementsCalls = (enemy65.defenceTimeSec * 1000L / ELEMENTS_PROGRESS_BAR_TICK_TIME).toInt()
        repeat(elementsCalls) {
            updateElementsProgressBar(it, elementsCalls)
            delay(ELEMENTS_PROGRESS_BAR_TICK_TIME)
            if (aggregator.hasTrigram()) {
                state = BattleFieldState.OTHER
                processTrigram(drawerStandard, protagonist65, enemy65, battleField65)
                return
            }
        }
        state = BattleFieldState.OTHER
        processTrigram(drawerStandard, protagonist65, enemy65, battleField65)
    }
    private val updateElementsProgressBar: (Int, Int) -> Unit = { numberOfInvocation, elementsCalls ->
        drawer.setEnemyProgressBarPercentage(
            100 - getShiftedProgressPercentage(numberOfInvocation, elementsCalls),
            ELEMENTS_PROGRESS_BAR_TICK_TIME
        )
    }

    private suspend fun processTrigram(
        drawerStandard: BattleFieldDrawerStandard,
        protagonist65: BattleFieldProtagonist,
        enemy65: BattleFieldEnemy,
        battleField65: BattleField65
    ) {
        val knowledge = protagonist65.knowledge
        val trigram = aggregator.getTrigram()
        val result = when {
            trigram == null || managerMode == Mode.STANDARD_TUTORIAL -> {
                AttackResult(0, AttackResult.DamageType.NOT_CASTED)
            }
            !knowledge.knowsTrigram(trigram) -> {
                drawerStandard.scrambleTrigram()
                AttackResult(0, AttackResult.DamageType.CASTED_UNKNOWN_TRIGRAM)
            }
            !knowledge.knowsRequiem(trigram) -> {
                trigram.applyAttack(battleField65)
            }
            else -> {
                trigram.applyAttackWithRequiem(battleField65)
            }
        }

        val isTimeback = trigram is Heaven &&
                knowledge.knowsTrigram(trigram) &&
                result.type != AttackResult.DamageType.TIMEBACK_DENIED
        if (isTimeback) {
            drawer.showBlackScreen()
        }

        drawer.updateEnemyHealthBar(enemy)
        if (isTimeback) {
            drawer.updateProtagonistInstantly(protagonist)
        } else {
            drawer.updateProtagonist(protagonist)
        }

        val wasAttacked = result.type.isAttack()
        when {
            managerMode == Mode.STANDARD_TUTORIAL -> {
                delay(BattleFieldDrawer.OFFENSIVE_ATTACK_DURATION)
            }
            trigram != null && wasAttacked -> {
                drawer.pauseEnemyAnimation()
                drawerStandard.showTrigramTriggered(result, enemy65, trigram)
            }
            trigram != null -> {
                drawerStandard.showTrigramTriggered(result, enemy65, trigram)
            }
        }
        enemy65.afterElements(result, battleField65, this, trigram)
        if (managerMode != Mode.STANDARD_TUTORIAL && trigram != null && wasAttacked) {
            drawer.resumeEnemyAnimation()
        }
        if (isTimeback) {
            drawer.stopBlackScreen()
        }
    }

    private fun onElementsEnd() {
        checkpoint()
        aggregator.clear()
        (drawer as BattleFieldDrawerStandard).clearElements()
    }

    // ELEMENTS ARE OVER

    private fun checkpoint() {
        if (protagonist.health <= 0 || enemy.health <= 0) {
            onBattleOver()
        }
    }

    private fun onBattleOver() {
        mainJob?.cancel()

        if (protagonist is BattleFieldProtagonist) {
            if (protagonist.health <= 0 && protagonist.hasAnkh) {
                ankhResetStage()
                return
            }
        }

        drawer.hideAll()
        drawer.stopSpeechBubble()
        drawer.stopEnemyAnimation()
        drawer.updateEnemy(enemy)
        drawer.updateProtagonist(protagonist)

        val result = if (protagonist.health <= 0) {
            BattleResult.BATTLE_DEFEAT
        } else {
            BattleResult.BATTLE_VICTORY
        }

        activity.musicPlayer.playMusic(
            R.raw.battle_outro,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )

        launchCoroutine {
            val outroMusic = launch { delay(4500L) }

            val enemyLineGenerator = getEnemyLineGenerator()
            if (result == BattleResult.BATTLE_DEFEAT) {
                val res1 = drawer.showTextInEnemySpeechBubble(enemyLineGenerator.getVictoryLine())
                val res2 = drawer.dissolveProtagonist(protagonist)
                joinAll(res1, res2, outroMusic)
            } else {
                val res1 = drawer.showTextInEnemySpeechBubble(enemyLineGenerator.getDefeatedLine())
                val res2 = drawer.dissolveEnemy(enemy)
                joinAll(res1, res2, outroMusic)
            }
            onFinished(result)
        }
    }

    private fun onFinished(result: BattleResult) {
        activity.musicPlayer.stopAllMusic()

        val newEnemiesPoolSize = enemiesPool.size - 1
        if (newEnemiesPoolSize == 0 || result == BattleResult.BATTLE_DEFEAT) {
            afterBattleCallback(result)
        } else {
            activity.musicPlayer.playMusic(R.raw.battle_intro_restart)
            val newPool = enemiesPool.takeLast(newEnemiesPoolSize).toNonEmptyListOrNull()!!

            launchCoroutineOnMain {
                drawer.undissolveEnemy(enemiesPool[1])
                BattleManager(
                    activity,
                    protagonist,
                    newPool,
                    managerMode,
                    afterBattleCallback = afterBattleCallback
                ).apply {
                    battleField.protagonist.health = this@BattleManager.battleField.protagonist.health
                    launchBattle()
                }
            }
        }
    }

    private fun ankhResetStage() {
        launchCoroutine {
            drawer.showBlackScreen()
            val battleField65 = battleField
            battleField65.ankhRestart()
            battleField65.protagonist.hasAnkh = false
            drawer.updateEnemy(enemy)
            drawer.updateProtagonistInstantly(protagonist)

            activity.musicPlayer.playMusicSuspendTillEnd(
                R.raw.sfx_knocks_reversed,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = false
            )
            drawer.stopBlackScreen()
            launchMainJob()
        }
    }

    private fun getEnemyLineGenerator(): BattleFieldLineGenerator {
        return if (enemy is BattleFieldEnemy) {
            enemy.lineGenerator
        } else {
            BattleFieldLineGenerator.EMPTY
        }
    }

    enum class Mode {
        STANDARD,
        STANDARD_TUTORIAL,
        STANDARD_D99,
        STANDARD_THE_CREATURE;
    }

    companion object {
        private const val FIELD_WIDTH = 5
        private const val FIELD_HEIGHT = 13

        const val ELEMENTS_PROGRESS_BAR_TICK_TIME = 100L
    }
}