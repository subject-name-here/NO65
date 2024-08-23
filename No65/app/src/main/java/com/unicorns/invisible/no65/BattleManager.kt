package com.unicorns.invisible.no65

import androidx.viewbinding.ViewBinding
import com.quickbirdstudios.nonEmptyCollection.list.NonEmptyList
import com.quickbirdstudios.nonEmptyCollection.toNonEmptyListOrNull
import com.unicorns.invisible.no65.controller.BattleFieldController
import com.unicorns.invisible.no65.controller.BattleFieldControllerStandard
import com.unicorns.invisible.no65.databinding.ActivityBattleBinding
import com.unicorns.invisible.no65.databinding.ActivityBattleEqualBinding
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.BattleFieldEqual
import com.unicorns.invisible.no65.model.battlefield.elements.TrigramAggregator
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacterEqual
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
import kotlin.properties.Delegates


class BattleManager(
    val activity: MainActivity,
    private val protagonist: BattleFieldFighter,
    private val enemiesPool: NonEmptyList<BattleFieldCharacter>,
    private val managerMode: Mode = Mode.STANDARD,
    private val isEnemyAttackingFirst: Boolean = true,
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
            Mode.EQUAL -> {
                val equalBinding = binding as ActivityBattleEqualBinding
                BattleFieldDrawerEqual(activity, fieldWidth, fieldHeight, equalBinding)
            }
        }
    }

    val controller: BattleFieldController by lazy {
        if (managerMode.isStandard()) {
            val standardDrawer = drawer as BattleFieldDrawerStandard
            BattleFieldControllerStandard(
                activity,
                drawer.field,
                drawer.giveUpButton,
                standardDrawer.elementsLinearLayout,
                drawer.enemyCentreCell
            )
        } else {
            BattleFieldController(
                activity,
                drawer.field,
                drawer.giveUpButton,
                drawer.enemyCentreCell
            )
        }
    }

    private val enemy = enemiesPool.first()

    private val aggregator = TrigramAggregator()

    private val battleFieldLock = ReentrantLock()
    val battleField =
        if (managerMode.isStandard()) {
            BattleField65(
                fieldWidth,
                fieldHeight,
                protagonist as BattleFieldProtagonist,
                enemy as BattleFieldEnemy
            )
        } else {
            BattleFieldEqual(
                fieldWidth,
                fieldHeight,
                protagonist,
                enemy
            )
        }

    private var fieldJob: Job? = null
    private fun cancelFieldJob() {
        fieldJob?.cancel()
        fieldJob = null
    }
    private var elementsJob: Job? = null
    private fun cancelElementsJob() {
        elementsJob?.cancel()
        elementsJob = null
    }

    private var state: BattleState by Delegates.observable(BattleState.NONE) { _, _, new ->
        val isSwapped = battleField.areAttacksSwapped()
        fun getDefaultAttacker() = if (!isSwapped) enemy else protagonist
        fun getDefaultDefender() = if (!isSwapped) protagonist else enemy

        when (new) {
            BattleState.BATTLEFIELD -> {
                attacker = getDefaultAttacker()
                defender = getDefaultDefender()
            }
            BattleState.ELEMENTS, BattleState.BATTLEFIELD_REVERSED -> {
                attacker = getDefaultDefender()
                defender = getDefaultAttacker()
            }
            BattleState.NONE -> {
                attacker = getDefaultAttacker()
                defender = getDefaultDefender()

                cancelFieldJob()
                cancelElementsJob()
            }
        }
    }
    private var attacker: BattleFieldFighter = enemy
    private var defender: BattleFieldFighter = protagonist

    private val binding: ViewBinding = if (managerMode.isStandard()) {
        ActivityBattleBinding.inflate(activity.layoutInflater)
    } else {
        ActivityBattleEqualBinding.inflate(activity.layoutInflater)
    }

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

    suspend fun launchBattle() {
        init().join()

        drawer.hideAll()
        drawer.initEnemy(enemy)
        drawer.initProtagonist(protagonist)
        drawer.updateEnemy(enemy)
        drawer.updateProtagonist(protagonist)
        drawer.clearBattleOverCell()

        drawer.hideLoadingLayout().join()

        coroutineScope { launch {
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
            beginGame()
        } }
    }

    private suspend fun beginGame() {
        activity.musicPlayer.playMusic(enemy.musicThemeId, isLooping = true)
        drawer.animateEnemy(enemy.animation)
        if (drawer is BattleFieldDrawerEqual) {
            val drawerEqual = drawer as BattleFieldDrawerEqual
            drawerEqual.animateProtagonist((protagonist as BattleFieldCharacterEqual).animation)
        }

        addProtagonistMovementListener()
        if (managerMode.isStandard()) {
            addElementsListener()
        }

        if (managerMode.isStandard() || isEnemyAttackingFirst) {
            proceedToField()
        } else {
            proceedToFieldReversed()
        }
    }

    private fun addProtagonistMovementListener() {
        controller.addListenersForButtons { coordinates, numberOfTaps ->
            val shuffledCoordinates = Coordinates(battleField.rowsOrder.indexOf(coordinates.row), coordinates.col)
            when (state) {
                BattleState.BATTLEFIELD -> {
                    if (numberOfTaps == enemy.numberOfTaps) {
                        battleFieldLock.withLock {
                            battleField.changeProtagonistCoordinates(shuffledCoordinates)
                            onFieldChange()
                        }
                    }
                }
                BattleState.BATTLEFIELD_REVERSED -> {
                    if (numberOfTaps == enemy.numberOfTaps) {
                        battleFieldLock.withLock {
                            val attacker = attacker as BattleFieldCharacterEqual
                            attacker.onTapAttack(shuffledCoordinates, battleField)
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
            if (state != BattleState.ELEMENTS) return@addListenersForElementsLayout

            val monogram = MonogramGestureBijection.getMonogram(type)
            aggregator.take(monogram)

            val drawerStandard = drawer as BattleFieldDrawerStandard
            drawerStandard.setElementText(
                aggregator.getLastMonogramNumber(),
                monogram.getSymbol()
            )

            if (aggregator.hasTrigram()) {
                launchCoroutine {
                    onElementsEnd(
                        drawerStandard,
                        protagonist as BattleFieldProtagonist,
                        enemy as BattleFieldEnemy,
                        battleField as BattleField65,
                    )
                }
            }
        }
    }

    private var lineId: Int = 0
    private suspend fun proceedToField() { coroutineScope { launch {
        battleField.onEnemyMoveStart()

        val isTakingFromJournal = battleField is BattleField65 && battleField.takeFromJournal
        if (!isTakingFromJournal) {
            if (!battleField.areAttacksSwapped()) {
                enemy.onMoveStart(battleField)
            } else {
                if (protagonist is BattleFieldCharacter) {
                    protagonist.onMoveStart(battleField)
                }
            }
        }

        if (checkBattleOver()) {
            onBattleOver()
            return@launch
        }

        drawer.stopSpeechBubble()
        if (!isTakingFromJournal || enemy.lineGeneratorJournalOverride) {
            lineId = getEnemyLineGenerator().getLine(battleField.moveNumber)
        }
        drawer.showTextInEnemySpeechBubble(lineId)

        if (enemy.attackTimeMvs == 0) {
            onFieldEnd()
            return@launch
        }

        battleField.sendProtagonistToCenter()

        drawer.setEnemyProgressBarPercentage(0, 0)
        drawer.showField()
        drawer.updateEnemyPanel(enemy)

        startFieldJob()
    } } }
    private fun startFieldJob() {
        state = BattleState.BATTLEFIELD
        fieldJob = launchCoroutine {
            repeat(enemy.attackTimeMvs) {
                onTickField(it)
                delay(enemy.tickTime)
            }
            onFieldEnd()
        }
    }
    private fun onTickField(numberOfInvocation: Int) {
        val progress = getShiftedProgressPercentage(numberOfInvocation, enemy.attackTimeMvs)
        drawer.setEnemyProgressBarPercentage(progress, enemy.tickTime)

        battleFieldLock.withLock {
            if (battleField is BattleField65) {
                battleField.preTickField()
            }
            val isTakingFromJournal = battleField is BattleField65 && battleField.takeFromJournal

            val currentAttacker = attacker
            if (!isTakingFromJournal) {
                currentAttacker.onTick(numberOfInvocation, battleField)
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

            if (!isTakingFromJournal && battleField is BattleField65) {
                battleField.saveField()
            }

            onFieldChange()
        }
    }
    private fun onFieldChange() {
        drawer.drawField(battleField)

        if (checkBattleOver()) {
            onBattleOver()
        }
    }
    private suspend fun onFieldEnd() {
        state = BattleState.NONE

        battleField.onEnemyMoveEnd()

        battleField.clear()
        drawer.drawField(battleField)
        drawer.hideAll()

        enemy.onAttackEnd(this@BattleManager)

        if (managerMode.isStandard()) {
            proceedToElements()
        } else {
            proceedToFieldReversed()
        }
    }


    // NEXT ARE ELEMENTS FUNCTIONS

    private suspend fun proceedToElements() {
        val drawerStandard = drawer as BattleFieldDrawerStandard
        val protagonist65 = protagonist as BattleFieldProtagonist
        val enemy65 = enemy as BattleFieldEnemy
        val battleField65 = battleField as BattleField65

        if (enemy65.defenceTimeSec == 0) {
            onElementsEnd(drawerStandard, protagonist65, enemy65, battleField65)
            return
        }
        drawerStandard.showElements()
        startElementsJob(drawerStandard, protagonist65, enemy65, battleField65)
    }
    private fun startElementsJob(
        drawerStandard: BattleFieldDrawerStandard,
        protagonist65: BattleFieldProtagonist,
        enemy65: BattleFieldEnemy,
        battleField65: BattleField65
    ) {
        state = BattleState.ELEMENTS
        val elementsCalls = (enemy65.defenceTimeSec * 1000L / ELEMENTS_PROGRESS_BAR_TICK_TIME).toInt()
        elementsJob = launchCoroutine {
            repeat(elementsCalls) {
                updateElementsProgressBar(it, elementsCalls)
                delay(ELEMENTS_PROGRESS_BAR_TICK_TIME)
            }
            onElementsEnd(drawerStandard, protagonist65, enemy65, battleField65)
        }
    }
    private val updateElementsProgressBar: (Int, Int) -> Unit = { numberOfInvocation, elementsCalls ->
        drawer.setEnemyProgressBarPercentage(
            100 - getShiftedProgressPercentage(numberOfInvocation, elementsCalls),
            ELEMENTS_PROGRESS_BAR_TICK_TIME
        )
    }

    private suspend fun onElementsEnd(
        drawerStandard: BattleFieldDrawerStandard,
        protagonist65: BattleFieldProtagonist,
        enemy65: BattleFieldEnemy,
        battleField65: BattleField65
    ) {
        state = BattleState.NONE
        processTrigram(drawerStandard, protagonist65, enemy65, battleField65)
        aggregator.clear()
        drawerStandard.clearElements()
        if (checkBattleOver()) {
            onBattleOver()
            return
        }
        proceedToField()
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

    // ELEMENTS ARE OVER

    // FIELD REVERSED FUNCTIONS

    private suspend fun proceedToFieldReversed() {
        val protagonistEqual = protagonist as BattleFieldCharacterEqual
        val drawerEqual = drawer as BattleFieldDrawerEqual

        drawerEqual.stopSpeechBubble()
        drawerEqual.showTextInProtagonistSpeechBubble(
            getProtagonistLineGenerator().getLine(battleField.moveNumber)
        )

        if (protagonist.attackTimeMvs == 0) {
            onFieldReversedEnd()
            return
        }

        delay(1000L)

        drawerEqual.apply {
            setProtagonistProgressBarPercentage(0, 0)
            showFieldReversed()
        }

        battleField.addFighter(enemy)
        battleField.sendEnemyToCenter()

        if (battleField.areAttacksSwapped()) {
            (enemy as BattleFieldCharacterEqual).onMoveReversedStart(battleField)
        } else {
            protagonistEqual.onMoveReversedStart(battleField)
        }

        startFieldReversedJob(protagonistEqual)
    }

    private fun startFieldReversedJob(protagonistEqual: BattleFieldCharacterEqual) {
        state = BattleState.BATTLEFIELD_REVERSED
        val defenderEqual = defender as BattleFieldCharacterEqual

        fieldJob = launchCoroutineOnDefault {
            defenderEqual.defend(battleField)
            repeat(protagonistEqual.attackTimeMvs) {
                onTickFieldReversed(it, protagonistEqual)
                delay(protagonistEqual.tickTime)
            }
            defenderEqual.stopDefending()
            onFieldReversedEnd()
        }
    }
    private fun onTickFieldReversed(numberOfInvocation: Int, protagonistEqual: BattleFieldCharacterEqual) {
        val progress = getShiftedProgressPercentage(numberOfInvocation, protagonistEqual.attackTimeMvs)
        (drawer as BattleFieldDrawerEqual).setProtagonistProgressBarPercentage(progress, protagonistEqual.tickTime)

        battleFieldLock.withLock {
            protagonistEqual.onTick(this, numberOfInvocation)

            for (i in 0 until protagonistEqual.projectilePassesCellsPerMove) {
                battleField.getAllObjects()
                    .filterIsInstance<BattleFieldProjectile>()
                    .forEach { battleFieldObject ->
                        battleFieldObject.onTick(numberOfInvocation, battleField)
                    }
            }

            battleField.setFieldPlayable()

            onFieldChange()
        }
    }

    private suspend fun onFieldReversedEnd() {
        state = BattleState.NONE

        battleField.clear()
        drawer.drawField(battleField)
        drawer.hideAll()

        delay(1000L)

        (protagonist as BattleFieldCharacterEqual).onAttackEnd(this@BattleManager)

        proceedToField()
    }

    // FIELD REVERSED IS OVER

    private fun checkBattleOver(): Boolean {
        return protagonist.health <= 0 || enemy.health <= 0
    }
    private fun onBattleOver() {
        state = BattleState.NONE
        if (managerMode == Mode.EQUAL) {
            (enemy as BattleFieldCharacterEqual).stopDefending()
            (protagonist as BattleFieldCharacterEqual).stopDefending()
        }

        if (protagonist is BattleFieldProtagonist) {
            if (protagonist.health <= 0 && protagonist.hasAnkh) {
                ankhResetStage()
                return
            }
        }

        if (enemy.health <= 0 && enemy.secondChanceState == BattleFieldCharacter.SecondChanceState.HAS_SECOND_CHANCE) {
            secondChanceResetStage()
            return
        }

        drawer.hideAll()
        drawer.stopSpeechBubble()
        drawer.stopEnemyAnimation()
        if (drawer is BattleFieldDrawerEqual) {
            val drawerStandard = drawer as BattleFieldDrawerEqual
            drawerStandard.stopProtagonistAnimation()
        }
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
            enemy.onFieryAnkhUsage(this@BattleManager)

            drawer.showBlackScreen()
            val battleField65 = battleField as BattleField65
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
            proceedToField()
        }
    }

    private fun secondChanceResetStage() {
        battleField.clear()
        drawer.pauseEnemyAnimation()
        activity.musicPlayer.stopAllMusic()
        drawer.setEnemyProgressBarPercentage(0, 0)
        if (drawer is BattleFieldDrawerEqual) {
            val equalDrawer = drawer as BattleFieldDrawerEqual
            equalDrawer.setProtagonistProgressBarPercentage(0, 0)
        }
        drawer.hideAll()

        launchCoroutine {
            enemy.onSecondChanceUsage(this@BattleManager)

            enemy.secondChanceState = BattleFieldCharacter.SecondChanceState.USED_SECOND_CHANCE
            enemy.health = enemy.maxHealth
            drawer.updateEnemy(enemy)
            drawer.updateProtagonist(protagonist)

            activity.musicPlayer.playMusicSuspendTillEnd(
                R.raw.sfx_second_chance,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = false
            )

            drawer.resumeEnemyAnimation()
            activity.musicPlayer.playMusic(
                enemy.musicThemeId,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = true
            )
            proceedToField()
        }
    }

    private fun getEnemyLineGenerator(): BattleFieldLineGenerator {
        return if (enemy is BattleFieldEnemy) {
            enemy.lineGenerator
        } else {
            (enemy as BattleFieldCharacterEqual).getLineGenerator(protagonist as BattleFieldCharacterEqual)
        }
    }
    private fun getProtagonistLineGenerator(): BattleFieldLineGenerator {
        return if (protagonist is BattleFieldCharacterEqual) {
            protagonist.getLineGenerator(enemy as BattleFieldCharacterEqual)
        } else {
            BattleFieldLineGenerator.EMPTY
        }
    }

    private enum class BattleState {
        ELEMENTS,
        BATTLEFIELD,
        BATTLEFIELD_REVERSED,
        NONE
    }

    enum class Mode {
        STANDARD,
        STANDARD_TUTORIAL,
        STANDARD_D99,
        STANDARD_THE_CREATURE,
        EQUAL;

        fun isStandard(): Boolean = this != EQUAL
    }

    companion object {
        private const val FIELD_WIDTH = 5
        private const val FIELD_HEIGHT = 13

        const val ELEMENTS_PROGRESS_BAR_TICK_TIME = 100L
    }
}