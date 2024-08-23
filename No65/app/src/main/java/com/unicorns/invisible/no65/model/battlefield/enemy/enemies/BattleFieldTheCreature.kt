package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile.Companion.DEFAULT_PROJECTILE_DAMAGE
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.*
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_EXPLAINED_FIRST_ATTACK
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_LAST_MOVE_NUMBER
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_TIMEBACK_LIMIT_EXPLAINED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.*
import com.unicorns.invisible.no65.view.BattleFieldDrawerStandard
import com.unicorns.invisible.no65.view.BattleFieldDrawerTheCreature
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay
import kotlin.reflect.full.primaryConstructor


// Make no mistake: it can be even harder.
class BattleFieldTheCreature(
    private val protagonistKilled: Int,
    private val wasKilled: Boolean,
    private val isCheating: Boolean,
) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * TOUGH_BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val isRequiemProof: Boolean = !isCheating
    override val timebackDenied: Boolean
        get() = timebackCount >= TIMEBACK_LIMIT

    override val lineGeneratorJournalOverride = true

    override val nameId: Int
        get() = R.string.battlefield_the_creature_name
    override val number: Int
        get() = 63

    override val attackTimeMvs: Int
        get() = 100
    override val defenceTimeSec: Int
        get() = if (afterNextAttackDeath) 2 else 3
    override val tickTime: Long
        get() = SUPER_FAST_PROJECTILE_SPEED_MILLISECONDS

    override val goNumbersToDelays: List<Pair<Int, Long>>
        get() = emptyList()
    override val goText: String
        get() = ""

    override var musicThemeId: Int = 0

    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT

    override val defaultFace: String
        get() = "∞"
    override val damageReceivedFace: String
        get() = "∞"
    override val noDamageReceivedFace: String
        get() = "∞"

    override val centerSymbol: String
        get() = "\uD83D\uDD1B"
    override val centerSymbolColorId: Int
        get() = R.color.black

    override val hexagramSymbol: String
        get() = ""
    override val outerSkinTrigram: Trigram
        get() = Heaven
    override val innerHeartTrigram: Trigram
        get() = Heaven

    private fun limitProtagonistHealth(manager: BattleManager) {
        val protagonist65 = manager.battleField.protagonist as BattleFieldProtagonist
        protagonist65.health = protagonistKilled
        protagonist65.maxHealth = protagonistKilled
        (manager.drawer as BattleFieldDrawerStandard).updateProtagonistHealthBarLimiter(protagonist65)
        manager.drawer.updateProtagonist(protagonist65)
    }
    private fun invertColors(manager: BattleManager) {
        playCreatureBit(manager)
        manager.drawer.invertColors()
    }
    private suspend fun writeWordsInBattleOverCell(words: List<Int>, manager: BattleManager) {
        words.forEach {
            manager.activity.musicPlayer.playMusicSuspendTillStart(beatId)
            manager.drawer.writeInBattleOverCell(it)
            delay(1000L)
        }
        manager.drawer.clearBattleOverCell()
    }
    private suspend fun writeName(manager: BattleManager) {
        val name = manager.activity.getString(R.string.battlefield_the_creature_intro)

        val text0 = '-' + "_".repeat(name.length) + '-'
        manager.drawer.writeInBattleOverCell(text0)
        delay(450L)

        repeat(name.length) { index ->
            val text = '-' + name.take(index + 1).padEnd(name.length, '_') + '-'
            manager.activity.musicPlayer.playMusicSuspendTillStart(R.raw.sfx_doom)
            manager.drawer.writeInBattleOverCell(text)
            delay(450L)
        }
    }

    private var timebackLimitExplained = false
    override suspend fun onBattleBegins(manager: BattleManager) {
        delay(1000L)
        val isFirstTime = !GlobalState.getBoolean(manager.activity, THE_CREATURE_EXPLAINED_FIRST_ATTACK)
        when {
            isCheating -> {
                limitProtagonistHealth(manager)
                delay(2000L)

                invertColors(manager)
                delay(1000L)

                writeWordsInBattleOverCell(listOf(R.string.battlefield_the_creature_cheat_center_words_1), manager)
            }
            isFirstTime -> {
                GlobalState.putBoolean(manager.activity, THE_CREATURE_EXPLAINED_FIRST_ATTACK, true)
                writeName(manager)
                delay(1000L)
                manager.drawer.clearBattleOverCell()
                delay(1000L)

                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_begin_1)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_begin_2)

                limitProtagonistHealth(manager)
                delay(1000L)

                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_begin_3)

                delay(500L)
                invertColors(manager)
                delay(500L)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_begin_4)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_begin_5)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_begin_6)

                writeWordsInBattleOverCell(listOf(
                    R.string.battlefield_the_creature_begin_center_words_1,
                    R.string.battlefield_the_creature_begin_center_words_2,
                    R.string.battlefield_the_creature_begin_center_words_3
                ), manager)
            }
            else -> {
                writeName(manager)
                delay(1000L)
                manager.drawer.clearBattleOverCell()
                delay(1000L)

                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_again_1)

                limitProtagonistHealth(manager)
                delay(2000L)

                invertColors(manager)
                delay(1000L)

                writeWordsInBattleOverCell(listOf(
                    R.string.battlefield_the_creature_again_center_words_1,
                    R.string.battlefield_the_creature_again_center_words_2,
                    R.string.battlefield_the_creature_again_center_words_3
                ), manager)
            }
        }

        if (isCheating) {
            offset = GlobalState.getInt(manager.activity, THE_CREATURE_LAST_MOVE_NUMBER)
            musicThemeId = R.raw.battle_the_creature_cheated
        }
        timebackLimitExplained = GlobalState.getBoolean(manager.activity, THE_CREATURE_TIMEBACK_LIMIT_EXPLAINED)
    }

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = R.string.empty_line
        override fun getDefeatedLine(): Int = when {
            isCheating -> R.string.empty_line
            wasKilled -> R.string.battlefield_the_creature_d_killed_again
            else -> R.string.battlefield_the_creature_d
        }
        override fun getVictoryLine(): Int = when {
            isCheating -> R.string.empty_line
            else -> R.string.battlefield_the_creature_v
        }
    }

    private fun writePhraseOnBlackScreen(manager: BattleManager, tickNumber: Int) {
        val breakNumberToPhraseId = mapOf(
            1 to R.string.battlefield_the_creature_midtro_1,
            2 to R.string.battlefield_the_creature_midtro_2,
            3 to R.string.battlefield_the_creature_midtro_3,
            4 to R.string.battlefield_the_creature_midtro_4,
            5 to R.string.battlefield_the_creature_midtro_5,
            6 to R.string.battlefield_the_creature_midtro_6,
            7 to R.string.battlefield_the_creature_midtro_7,
            8 to R.string.battlefield_the_creature_midtro_8,
            9 to R.string.battlefield_the_creature_midtro_9,
            10 to R.string.battlefield_the_creature_midtro_10,
            11 to R.string.battlefield_the_creature_midtro_11,
            12 to R.string.battlefield_the_creature_midtro_12,
            13 to R.string.battlefield_the_creature_midtro_13,
            14 to R.string.battlefield_the_creature_midtro_14,
            15 to R.string.battlefield_the_creature_midtro_15,
            16 to R.string.battlefield_the_creature_midtro_16,
            17 to R.string.battlefield_the_creature_midtro_17,
            18 to R.string.battlefield_the_creature_midtro_18,
            19 to R.string.battlefield_the_creature_midtro_19,
            20 to R.string.battlefield_the_creature_midtro_20,
            21 to R.string.battlefield_the_creature_midtro_21,
            22 to R.string.battlefield_the_creature_midtro_22,
            23 to R.string.battlefield_the_creature_midtro_23,
            24 to R.string.battlefield_the_creature_midtro_24,
        )
        val elsePhraseIds = listOf(
            R.string.battlefield_the_creature_midtro_else_1,
            R.string.battlefield_the_creature_midtro_else_2,
            R.string.battlefield_the_creature_midtro_else_3,
            R.string.battlefield_the_creature_midtro_else_4,
            R.string.battlefield_the_creature_midtro_else_5,
            R.string.battlefield_the_creature_midtro_else_6,
            R.string.battlefield_the_creature_midtro_else_7,
            R.string.battlefield_the_creature_midtro_1,
            R.string.battlefield_the_creature_midtro_2,
            R.string.battlefield_the_creature_midtro_3,
            R.string.battlefield_the_creature_midtro_6,
            R.string.battlefield_the_creature_midtro_20,
            R.string.battlefield_the_creature_midtro_24,
        )
        val breakNumber = (manager.battleField.moveNumber - 1) * 3 + tickNumber / 25
        val phraseId = breakNumberToPhraseId[breakNumber] ?: elsePhraseIds.random()
        val phrase = manager.activity.getString(phraseId, protagonistKilled)
        (manager.drawer as BattleFieldDrawerTheCreature).writeWordsOnBlackScreen(phrase)
    }
    private fun clearBlackScreen(manager: BattleManager) {
        (manager.drawer as BattleFieldDrawerTheCreature).clearBlackScreen()
    }

    private var attackNumber = -1
    private var offset = 0
    override fun onTick(manager: BattleManager, tickNumber: Int) {
        if (!isCheating && tickNumber == 0) {
            val themeId = if (afterNextAttackDeath) {
                R.raw.battle_the_creature_kq_4
            } else if (manager.battleField.moveNumber == 1) {
                R.raw.battle_the_creature_kq_1
            } else {
                when ((manager.battleField.moveNumber - 2) % 4) {
                    0 -> R.raw.battle_the_creature_1
                    1 -> R.raw.battle_the_creature_2
                    2 -> R.raw.battle_the_creature_kq_2
                    else -> R.raw.battle_the_creature_kq_3
                }
            }
            manager.activity.musicPlayer.playMusic(
                themeId,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = false
            )
        }

        if (tickNumber != 0 && tickNumber % 25 == 0) {
            playCreatureBit(manager)
            manager.drawer.showBlackScreen()
            if (!isCheating) {
                writePhraseOnBlackScreen(manager, tickNumber)
            }
            manager.controller.stopButtonsListener()
            manager.battleField.clear()
            manager.battleField.sendProtagonistToCenter()
        }
        if (tickNumber != 1 && tickNumber % 25 == 1) {
            manager.drawer.stopBlackScreen()
            if (!isCheating) {
                clearBlackScreen(manager)
            }
            manager.controller.resumeButtonsListener()
        }

        if (isCheating && tickNumber == 0) {
            GlobalState.putInt(
                manager.activity,
                THE_CREATURE_LAST_MOVE_NUMBER,
                (offset + manager.battleField.moveNumber - 1) % 8
            )
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (health <= 20 && tickNumber == 0) {
            afterNextAttackDeath = true
        }

        if (tickNumber % 25 == 0) {
            val innerMoveNumber = tickNumber / 25
            attackNumber = (offset + battleField.moveNumber - 1) * 4 + innerMoveNumber
            attackNumber %= 32
        }

        val innerTickNumber = tickNumber % 25 - if (tickNumber >= 25) 1 else 0
        if (afterNextAttackDeath) {
            when (attackNumber % 4) {
                0 -> abundanceAttack(battleField, isStrong = true)
                1 -> peaceAttack(battleField, innerTickNumber, isStrong = true)
                2 -> greatTamingAttack(battleField, innerTickNumber, isStrong = true)
                3 -> multiAttack(battleField, innerTickNumber)
            }
            return
        }

        when (attackNumber) {
            0 -> theWandererAttack(battleField, innerTickNumber)
            1 -> limitationAttack(battleField, innerTickNumber)
            2 -> arousingThunderAttack(battleField, innerTickNumber)
            3 -> clingingFireAttack(battleField, innerTickNumber)
            4 -> comingToMeetAttack(battleField, innerTickNumber)
            5 -> contemplationAttack(battleField, innerTickNumber)
            6 -> darkeningOfTheLightAttack(battleField, innerTickNumber)
            7 -> difficultyAtTheBeginningAttack(battleField, innerTickNumber)
            8 -> treadingAttack(battleField, innerTickNumber)
            9 -> durationAttack(battleField, innerTickNumber)
            10 -> enthusiasmAttack(battleField, innerTickNumber)
            11 -> fellowshipAttack(battleField)
            12 -> gatheringTogetherAttack(battleField, tickNumber)
            13 -> gentleWindAttack(battleField, innerTickNumber)
            14 -> graceAttack(battleField, innerTickNumber)
            15 -> abundanceAttack(battleField)
            16 -> greatPreponderanceAttack(battleField, innerTickNumber)
            17 -> greatTamingAttack(battleField, innerTickNumber)
            18 -> innerTruthAttack(battleField)
            19 -> ksmAttack(battleField, innerTickNumber)
            20 -> bitingThroughAttack(battleField, innerTickNumber)
            21 -> modestyAttack(battleField, innerTickNumber)
            22 -> mouthCornersAttack(battleField, innerTickNumber)
            23 -> oppressionAttack(battleField, innerTickNumber)
            24 -> theWellAttack(battleField, innerTickNumber)
            25 -> retreatAttack(battleField, innerTickNumber)
            26 -> returnAttack(battleField, innerTickNumber)
            27 -> theMarryingMaidenAttack(battleField, innerTickNumber)
            28 -> splittingApartAttack(battleField, innerTickNumber)
            29 -> greatPossessionAttack(battleField, innerTickNumber)
            30 -> dispersionAttack(battleField, innerTickNumber)
            31 -> peaceAttack(battleField, innerTickNumber)
        }
    }

    // ABUNDANCE
    private fun abundanceAttack(battleField: BattleField65, isStrong: Boolean = false) {
        spawnJoke(battleField)
        spawnJoke(battleField)
        if (isStrong || randBoolean()) {
            spawnJoke(battleField)
        }
        spawnCensorship(battleField)
    }
    private fun spawnJoke(battleField: BattleField65) {
        val row = randInt(battleField.height)
        if (randBoolean()) {
            battleField.addProjectile(Joke(Coordinates(row, -1), BattleFieldProjectile.Direction.RIGHT))
        } else {
            battleField.addProjectile(Joke(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
        }
    }
    private fun spawnCensorship(battleField: BattleField65) {
        val col = randInt(battleField.width)
        if (randBoolean()) {
            battleField.addProjectile(Censorship(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
        } else {
            battleField.addProjectile(Censorship(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
        }
    }

    // AROUSING THUNDER
    private fun arousingThunderAttack(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                battleField.removeProjectiles()
                generateSpheres(battleField)
            }
        }
    }
    private fun generateSpheres(battleField: BattleField65) {
        var numOfSpheresOnSide = battleField.height - 1
        val row = battleField.protagonist.position.row
        (row - 1..row + 1).intersect(0 until battleField.height).forEach {
            battleField.addProjectile(TeslaSphere(Coordinates(it, 0)))
            battleField.addProjectile(TeslaSphere(Coordinates(it, battleField.width - 1)))
            numOfSpheresOnSide--
        }

        val rowsLeft = (0 until battleField.height).takeRand(numOfSpheresOnSide)
        val rowsRight = (0 until battleField.height).takeRand(numOfSpheresOnSide)
        for (rowLeft in rowsLeft) {
            battleField.addProjectile(TeslaSphere(Coordinates(rowLeft, 0)))
        }
        for (rowRight in rowsRight) {
            battleField.addProjectile(TeslaSphere(Coordinates(rowRight, battleField.width - 1)))
        }
    }

    // BITING THROUGH
    private fun bitingThroughAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 0) {
            spawnPlaneWave(battleField)
        } else {
            spawnPlaneOnProtagonist(battleField)
            spawnRandomPlanes(battleField, (battleField.height - 1) / 2 - 1)
        }
    }
    private fun spawnPlaneWave(battleField: BattleField65) {
        repeat(battleField.height) { row ->
            battleField.addProjectile(PaperPlane(Coordinates(row, battleField.width)))
        }
    }
    private fun spawnPlaneOnProtagonist(battleField: BattleField65) {
        battleField.addProjectile(
            PaperPlane(
                Coordinates(
                    battleField.protagonist.position.row,
                    battleField.width
                )
            )
        )
    }
    private fun spawnRandomPlanes(battleField: BattleField65, times: Int) {
        repeat(times) {
            battleField.addProjectile(
                PaperPlane(
                    Coordinates(
                        randInt(battleField.height),
                        battleField.width
                    )
                )
            )
        }
    }

    // CLINGING FIRE
    private var direction: BattleFieldProjectile.Direction = BattleFieldProjectile.Direction.NO_MOVEMENT
    private var col: Int = -1
    private var safeLine = -1
    private val safeLineWidth = 2
    private var safeLineDragged = safeLineWidth
    private var isOnTop = false
    private fun clingingFireAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            val directionToCol = if (randBoolean()) {
                BattleFieldProjectile.Direction.LEFT to battleField.width
            } else {
                BattleFieldProjectile.Direction.RIGHT to -1
            }
            direction = directionToCol.first
            col = directionToCol.second
            safeLine = -1
            safeLineDragged = safeLineWidth
            isOnTop = randBoolean()
        }

        if (safeLineDragged >= safeLineWidth) {
            safeLineDragged = -2

            val rowD = randInt(3)
            safeLine = (if (isOnTop) battleField.height - 1 - rowD else rowD)
            isOnTop = safeLine < 3
        }

        repeat(battleField.height) {
            if (it != safeLine || safeLineDragged < 0) {
                battleField.addProjectile(UpcomingFire(Coordinates(it, col), direction))
            }
        }
        safeLineDragged++
    }

    // COMING TO MEET
    private val currentCoordinates = arrayListOf<Coordinates>()
    private fun comingToMeetAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentCoordinates.clear()
        }
        battleField.removeProjectiles()
        generateHighFives(battleField)
        when (tickNumber % 2) {
            0 -> {
                generateLowFives(battleField)
            }
            1 -> {
                currentCoordinates.clear()
                generateLowerFive(battleField)
            }
        }
    }
    private fun generateHighFives(battleField: BattleField65) {
        repeat(battleField.height) { r ->
            repeat(battleField.width) { c ->
                battleField.addProjectile(HighFive(Coordinates(r, c)))
            }
        }
    }
    private fun generateLowerFive(battleField: BattleField65) {
        val coordinates = generateCoordinatesForLowerFive(battleField)
        currentCoordinates.add(coordinates)
        battleField.addProjectile(LowerFive(coordinates))
    }
    private fun generateCoordinatesForLowerFive(battleField: BattleField65): Coordinates {
        return if (randBooleanPercent(50)) {
            val pRow = battleField.protagonist.position.row
            val pCol = battleField.protagonist.position.col
            val coordinates = if (randBoolean()) {
                Coordinates(pRow, randInt(battleField.width))
            } else {
                Coordinates(randInt(battleField.height), pCol)
            }
            if (coordinates == battleField.protagonist.position) {
                randCoordinatesWithExclusion(
                    battleField.width,
                    battleField.height,
                    battleField.protagonist.position
                )
            } else {
                coordinates
            }
        } else {
            randCoordinatesWithExclusion(
                battleField.width,
                battleField.height,
                battleField.protagonist.position
            )
        }
    }
    private fun generateLowFives(battleField: BattleField65) {
        for (coordinates in currentCoordinates) {
            repeat(battleField.height) { r ->
                battleField.addProjectile(LowFive(Coordinates(r, coordinates.col)))
            }
            repeat(battleField.width) { c ->
                battleField.addProjectile(LowFive(Coordinates(coordinates.row, c)))
            }
        }
    }

    // CONTEMPLATION
    private fun contemplationAttack(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 6) {
            0, 3 -> spawnHandcuffs(battleField)
            4 -> spawnPistols(battleField)
        }
    }
    private fun spawnPistols(battleField: BattleField65) {
        val row = battleField.protagonist.position.row
        battleField.addProjectile(Pistol(Coordinates(row, battleField.width - 1)))
        battleField.addProjectile(Pistol(Coordinates(row + 1, battleField.width - 1)))
    }
    private fun spawnHandcuffs(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            battleField.addProjectile(Handcuffs(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
        }
    }

    // DARKENING OF THE LIGHT
    private val centers1 = mutableSetOf<Coordinates>()
    private val centers2 = mutableSetOf<Coordinates>()
    private fun darkeningOfTheLightAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            centers1.clear()
            centers2.clear()
        }

        when (tickNumber % 3) {
            0 -> {
                battleField.removeProjectiles()
                spawnCrabPersons(battleField, 2)

                centers1.clear()
                centers2.clear()

                fillCenters(battleField, 1)
                addCenter1NearProtagonist(battleField)
                spawnBars(battleField, 1)
            }
            1 -> {
                removeBars(battleField)
                removeCrabs(battleField)
                fillCenters(battleField, 2)
                spawnBars(battleField, 2)
            }
            2 -> {
                removeBars(battleField)
                spawnCrabPersons(battleField, 1)
            }
        }
    }
    private fun fillCenters(battleField: BattleField65, numberOfBatch: Int) {
        val numberOfCenters = 4
        val centers = if (numberOfBatch == 1) centers1 else centers2
        repeat(numberOfCenters) {
            val centerCoordinates = randCoordinatesWithExclusion(
                battleField.width,
                battleField.height,
                battleField.protagonist.position
            )
            centers.add(centerCoordinates)
        }
    }
    private fun addCenter1NearProtagonist(battleField: BattleField65) {
        (-1..1).shuffled().forEach { dx ->
            (-1..1).shuffled().forEach { dy ->
                if (dx != 0 && dy != 0) {
                    val pos = battleField.protagonist.position
                    val barPosition = Coordinates(pos.row + dx, pos.col + dy)
                    if (
                        barPosition.row in (0 until battleField.height) &&
                        barPosition.col in (0 until battleField.width)
                    ) {
                        centers1.add(barPosition)
                        return
                    }
                }
            }
        }
    }
    private fun spawnBars(battleField: BattleField65, numberOfBatch: Int) {
        val centers = if (numberOfBatch == 1) centers1 else centers2
        for (center in centers) {
            battleField.addProjectile(Bar(center))
        }
    }
    private fun spawnCrabPersons(battleField: BattleField65, numberOfBatch: Int) {
        val centers = if (numberOfBatch == 1) centers1 else centers2
        for (center in centers) {
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    battleField.addProjectile(CrabPerson(center + Coordinates(dx, dy)))
                }
            }
        }
    }
    private fun removeBars(battleField: BattleField65) {
        battleField.getAllObjects()
            .filterIsInstance<Bar>()
            .forEach { battleField.removeProjectile(it) }
    }
    private fun removeCrabs(battleField: BattleField65) {
        battleField.getAllObjects()
            .filterIsInstance<CrabPerson>()
            .forEach { battleField.removeProjectile(it) }
    }

    // DIFFICULTY AT THE BEGINNING
    private fun difficultyAtTheBeginningAttack(battleField: BattleField65, tickNumber: Int) {
        val width = battleField.width
        when (tickNumber % 3) {
            0 -> {
                repeat(width) {
                    addSteelRainDeadly(battleField, it)
                }
            }
            1 -> {
                val currentCells = (0 until width).takeRand(width - 1)
                currentCells.forEach {
                    addSteelRainDeadly(battleField, it)
                }
            }
        }
    }
    private fun addSteelRainDeadly(battleField: BattleField65, col: Int) {
        battleField.addProjectile(SteelRainDeadly(Coordinates(-1, col)))
    }

    // DISPERSION
    private fun dispersionAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            val mid = battleField.height / 2
            battleField.addProjectile(Flower(Coordinates(mid, -1), BattleFieldProjectile.Direction.RIGHT).also { it.loaded = true })
            battleField.addProjectile(Flower(Coordinates(mid, battleField.width), BattleFieldProjectile.Direction.LEFT).also { it.loaded = true })
        } else if (tickNumber == battleField.width / 2) {
            battleField.getAllObjects()
                .filterIsInstance<Flower>()
                .forEach { it.charged = true }
        }
    }

    // DURATION
    private var freeCell = -1
    private fun durationAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            freeCell = -1
        }
        if (freeCell == -1) {
            val center = battleField.protagonist.position
            freeCell = center.row * battleField.width + center.col
        }
        when (tickNumber % 3) {
            0 -> {
                val newFreeCell = randInt(battleField.width * battleField.height)
                repeat(battleField.height) { row ->
                    repeat(battleField.width) { col ->
                        if (row * battleField.width + col != freeCell) {
                            battleField.addProjectile(
                                LetterProjectile(
                                    Coordinates(row, col),
                                    BattleFieldProjectile.Direction.NO_MOVEMENT,
                                    newFreeCell.toString()
                                )
                            )
                        }
                    }
                }
                freeCell = newFreeCell
            }
            1 -> {
                battleField.removeProjectiles()
            }
        }
    }

    // ENTHUSIASM
    private var enthusiasmCols = mutableListOf<Int>()
    private fun enthusiasmAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber >= 2) {
            if (tickNumber % 2 == 0) {
                enthusiasmCols.clear()
                removeBangs(battleField)
                generateOneBang(battleField)
            } else {
                generateLineBang(battleField)
            }
        }

        if (tickNumber % battleField.width == 0) {
            (1 until battleField.height - 1).forEach {
                if (it % 2 == 0) {
                    battleField.addProjectile(Battery(Coordinates(it, -1), BattleFieldProjectile.Direction.RIGHT))
                } else {
                    battleField.addProjectile(Battery(Coordinates(it, battleField.width), BattleFieldProjectile.Direction.LEFT))
                }
            }
        }
    }
    private fun generateOneBang(battleField: BattleField65) {
        enthusiasmCols = listOf(
            battleField.protagonist.position.col,
            battleField.protagonist.position.col,
            randInt(battleField.width)
        ).shuffled().toMutableList()

        battleField.addProjectile(Bang(Coordinates(-1, enthusiasmCols[0]), BattleFieldProjectile.Direction.DOWN))
        battleField.addProjectile(Bang(Coordinates(battleField.height, enthusiasmCols[1]), BattleFieldProjectile.Direction.UP))
    }
    private fun generateLineBang(battleField: BattleField65) {
        repeat(battleField.height / 2 + 1) {
            battleField.addProjectile(Bang(Coordinates(it, enthusiasmCols[0]), BattleFieldProjectile.Direction.NO_MOVEMENT))
            val row = battleField.height - it - 1
            battleField.addProjectile(Bang(Coordinates(row, enthusiasmCols[1]), BattleFieldProjectile.Direction.NO_MOVEMENT))
        }
    }
    private fun removeBangs(battleField: BattleField65) {
        battleField.getAllObjects().filterIsInstance<Bang>().forEach { battleField.removeProjectile(it) }
    }

    // FELLOWSHIP
    private fun fellowshipAttack(battleField: BattleField65) {
        val protagonistCoordinates = battleField.protagonist.position
        battleField.addProjectile(
            Star52(
                Coordinates(
                    -1,
                    protagonistCoordinates.col
                ),
                BattleFieldProjectile.Direction.DOWN
            )
        )
        battleField.addProjectile(
            Star52(
                Coordinates(
                    battleField.height,
                    protagonistCoordinates.col
                ),
                BattleFieldProjectile.Direction.UP
            )
        )
        battleField.addProjectile(
            Star52(
                Coordinates(
                    protagonistCoordinates.row,
                    battleField.width
                ),
                BattleFieldProjectile.Direction.LEFT
            )
        )
        battleField.addProjectile(
            Star52(
                Coordinates(
                    protagonistCoordinates.row,
                    -1
                ),
                BattleFieldProjectile.Direction.RIGHT
            )
        )
    }

    // GATHERING TOGETHER
    private var qDirectionToRow = BattleFieldProjectile.Direction.DOWN to -1
    private fun gatheringTogetherAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            qDirectionToRow = if (randBoolean()) {
                BattleFieldProjectile.Direction.UP to battleField.height
            } else {
                BattleFieldProjectile.Direction.DOWN to -1
            }
        }
        repeat(battleField.width) { col ->
            battleField.addProjectile(Question(
                Coordinates(qDirectionToRow.second, col),
                qDirectionToRow.first
            ))
        }
    }

    // GENTLE WIND
    private fun gentleWindAttack(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                repeat(battleField.width) { col ->
                    battleField.addProjectile(King(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                }
            }
            1 -> {
                val row = randInt(battleField.height)
                battleField.addProjectile(House(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                battleField.addProjectile(House(Coordinates(battleField.protagonist.position.row, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
            2 -> {
                val row = randInt(battleField.height)
                battleField.addProjectile(House(Coordinates(row, -1), BattleFieldProjectile.Direction.RIGHT))
                battleField.addProjectile(House(Coordinates(battleField.protagonist.position.row, -1), BattleFieldProjectile.Direction.RIGHT))
            }
        }
    }

    // GRACE
    private fun graceAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            repeat(battleField.height) {
                battleField.addProjectile(CrazyDiamond(Coordinates(it, 0), BattleFieldProjectile.Direction.NO_MOVEMENT))
                battleField.addProjectile(CrazyDiamond(Coordinates(it, battleField.width - 1), BattleFieldProjectile.Direction.NO_MOVEMENT))
            }
        }
        if (tickNumber % 4 == 0) {
            repeat(battleField.width) {
                battleField.addProjectile(RustyChain(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
                battleField.addProjectile(RustyChain(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
            }
        }
    }

    // GREAT POSSESSION
    private var selectedSpaces = setOf<Int>()
    private var prevSelectedSpaces = setOf<Int>()
    private fun greatPossessionAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            selectedSpaces = emptySet()
            drawWalls(battleField)
            return
        }

        if (tickNumber % 2 == 0) {
            battleField.getAllObjects().filterIsInstance<Dollar>().forEach { battleField.removeProjectile(it) }
            for (spaceNum in selectedSpaces) {
                fillSpace(battleField, spaceNum, harmless = false)
            }

            val protagonistSpace = getSpaceByCoordinates(battleField.protagonist.position, battleField)
            val randomSpace1 = ((0..4) - protagonistSpace - selectedSpaces.toSet()).random()
            val randomSpace2 = ((0..4) - protagonistSpace - randomSpace1 - selectedSpaces.toSet()).randomOrNull() ?: protagonistSpace
            prevSelectedSpaces = HashSet(selectedSpaces)
            selectedSpaces = if (protagonistSpace in selectedSpaces) {
                setOf(randomSpace1, randomSpace2)
            } else {
                setOf(protagonistSpace, randomSpace1)
            }
            val remainder = ((0..4).toSet() - selectedSpaces - prevSelectedSpaces)
            if (randBooleanPercent(44) && remainder.isNotEmpty() && prevSelectedSpaces.size != 3) {
                selectedSpaces += remainder.take(1)
            }

            for (spaceNum in selectedSpaces) {
                fillSpace(battleField, spaceNum, harmless = true)
            }
        } else {
            battleField.getAllObjects().filterIsInstance<DollarWorthless>().forEach { battleField.removeProjectile(it) }
            if (prevSelectedSpaces.size + selectedSpaces.size > 4) {
                battleField.getAllObjects().filterIsInstance<Dollar>().forEach { battleField.removeProjectile(it) }
            }
        }
    }
    private fun drawWalls(battleField: BattleField65) {
        val middleTopWallRow = battleField.height / 2 - 2
        val middleBottomWallRow = battleField.height / 2 + 2
        val wallCol = battleField.width / 2

        repeat(battleField.height) {
            if (it !in (middleTopWallRow..middleBottomWallRow)) {
                battleField.addProjectile(WallStreet(Coordinates(it, wallCol)))
            }
        }
        repeat(battleField.width) {
            battleField.addProjectile(WallStreet(Coordinates(middleTopWallRow, it)))
            battleField.addProjectile(WallStreet(Coordinates(middleBottomWallRow, it)))
        }
    }
    private fun fillSpace(battleField: BattleField65, spaceNumber: Int, harmless: Boolean, isClearing: Boolean = false) {
        val middleTopWallRow = battleField.height / 2 - 2
        val middleBottomWallRow = battleField.height / 2 + 2
        val wallCol = battleField.width / 2

        val projectile = if (harmless) DollarWorthless::class.primaryConstructor else Dollar::class.primaryConstructor
        val defaultProjectile = WallStreet(Coordinates(-1, -1))

        fun addProjectile(row: Int, col: Int) {
            if (isClearing) {
                battleField.removeProjectileAt(Coordinates(row, col))
            } else {
                battleField.addProjectile(projectile?.call(Coordinates(row, col)) ?: defaultProjectile)
            }
        }

        when (spaceNumber) {
            0 -> {
                for (row in 0 until middleTopWallRow) {
                    for (col in 0 until wallCol) {
                        addProjectile(row, col)
                    }
                }
            }
            1 -> {
                for (row in 0 until middleTopWallRow) {
                    for (col in (wallCol + 1) until battleField.width) {
                        addProjectile(row, col)
                    }
                }
            }
            2 -> {
                for (row in (middleBottomWallRow + 1) until battleField.height) {
                    for (col in 0 until wallCol) {
                        addProjectile(row, col)
                    }
                }
            }
            3 -> {
                for (row in (middleBottomWallRow + 1) until battleField.height) {
                    for (col in (wallCol + 1) until battleField.width) {
                        addProjectile(row, col)
                    }
                }
            }
            else -> {
                for (row in (middleTopWallRow + 1) until middleBottomWallRow) {
                    repeat(battleField.width) { col ->
                        addProjectile(row, col)
                    }
                }
            }
        }
    }
    private fun getSpaceByCoordinates(coordinates: Coordinates, battleField: BattleField65): Int {
        val middleTopWallRow = battleField.height / 2 - 2
        val middleBottomWallRow = battleField.height / 2 + 2
        val wallCol = battleField.width / 2

        return when {
            coordinates.col in (middleTopWallRow..middleBottomWallRow) -> 4
            coordinates.row < middleTopWallRow && coordinates.col < wallCol -> 0
            coordinates.row < middleTopWallRow && coordinates.col > wallCol -> 1
            coordinates.row > middleBottomWallRow && coordinates.col < wallCol -> 2
            coordinates.row > middleBottomWallRow && coordinates.col > wallCol -> 3
            else -> 4
        }
    }

    // GREAT PREPONDERANCE
    private fun greatPreponderanceAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0 || tickNumber % 2 == 1) {
            return
        }

        battleField.getAllObjects().filterIsInstance<NuclearExplosion>().forEach {
            battleField.removeProjectile(it)
        }

        setUpRockets(battleField, battleField.height / 2 - 1)
    }
    private fun setUpRockets(battleField: BattleField65, radius: Int) {
        val protagonistCoordinates = battleField.protagonist.position
        val randomCoordinates = randCoordinatesWithExclusion(battleField.width, battleField.height, protagonistCoordinates)

        battleField.addProjectile(NuclearRocket(randomCoordinates, radius))
    }

    // GREAT TAMING
    private var greatTamingCols = mutableListOf<Int>()
    private fun greatTamingAttack(battleField: BattleField65, tickNumber: Int, isStrong: Boolean = false) {
        when (tickNumber % 4) {
            0 -> {
                greatTamingCols.clear()
                battleField.removeProjectiles()
            }
            1 -> {
                generateOnePaw(battleField, isStrong)
            }
            2 -> {
                generateLinePaws(battleField)
                greatTamingCols.clear()
                generateOnePaw(battleField, isStrong)
            }
            3 -> {
                generateLinePaws(battleField)
            }
        }
    }
    private fun generateOnePaw(battleField: BattleField65, isStrong: Boolean) {
        greatTamingCols.add(battleField.protagonist.position.col)
        if (isStrong) {
            val available = ((0 until battleField.width) - greatTamingCols.toSet()).toMutableList()
            repeat(battleField.width) {
                val occupant = battleField.getMap()[Coordinates(0, it)]
                if (occupant is Paw) {
                    available.remove(it)
                }
            }
            greatTamingCols.add(available.random())
        } else {
            greatTamingCols.add(randInt(battleField.width))
        }
        greatTamingCols = greatTamingCols.distinct().toMutableList()
        for (col in greatTamingCols) {
            battleField.addProjectile(Paw(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
        }
    }
    private fun generateLinePaws(battleField: BattleField65) {
        for (col in greatTamingCols) {
            repeat(battleField.height) {
                battleField.addProjectile(Paw(Coordinates(it, col), BattleFieldProjectile.Direction.NO_MOVEMENT))
            }
        }
    }

    // INNER TRUTH
    private fun innerTruthAttack(battleField: BattleField65) {
        val direction = BattleFieldProjectile.Direction.RIGHT
        repeat(battleField.height) {
            if (randBooleanPercent(65)) {
                battleField.addProjectile(Stone(Coordinates(it, -1), direction))
            }
        }
    }

    // KEEPING STILL MOUNTAIN
    private fun ksmAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 6 < 3) {
            generateSnow(battleField)
        }
        if (tickNumber % 6 == 5) {
            generateAvalanche(battleField)
        }
    }
    private fun generateSnow(battleField: BattleField65) {
        repeat(battleField.width) {
            if (randBooleanPercent(66)) {
                battleField.addProjectile(Snow(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
            }
            if (randBooleanPercent(66)) {
                battleField.addProjectile(Snow(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
            }
        }
    }
    private fun generateAvalanche(battleField: BattleField65) {
        if (randBoolean()) {
            repeat(battleField.height) {
                battleField.addProjectile(Avalanche(Coordinates(it, -1), BattleFieldProjectile.Direction.RIGHT))
            }
        } else {
            repeat(battleField.height) {
                battleField.addProjectile(Avalanche(Coordinates(it, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
        }
    }

    // LIMITATION
    private var currentDirection = BattleFieldProjectile.Direction.UP
    private var freeCol = -1
    private var turnsSinceFreeCol = 0
    private fun limitationAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentDirection = BattleFieldProjectile.Direction.UP
            freeCol = -1
            turnsSinceFreeCol = 0
        }
        when (tickNumber % battleField.height) {
            0 -> {
                changeProjectilesDirection(battleField)
            }
            else -> {
                standardMove(battleField, tickNumber)
            }
        }
    }
    private fun changeProjectilesDirection(battleField: BattleField65) {
        val newDirection = if (currentDirection == BattleFieldProjectile.Direction.UP) {
            BattleFieldProjectile.Direction.DOWN
        } else {
            BattleFieldProjectile.Direction.UP
        }

        battleField.getAllObjects()
            .filterIsInstance<BattleFieldProjectile>()
            .forEach {
                it.changeDirection(newDirection)
            }

        currentDirection = newDirection
    }
    private fun standardMove(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                launchSqrts(battleField)
                defineFreeCol(battleField)
            }
            else -> {
                launchMiddleProjectiles(battleField)
            }
        }
    }
    private fun defineFreeCol(battleField: BattleField65) {
        freeCol = randInt(-1, battleField.width)
        if (freeCol == -1) {
            turnsSinceFreeCol++
        }
        if (turnsSinceFreeCol >= battleField.height / 3 - 2) {
            freeCol = randInt(battleField.width)
        }
    }
    private fun launchSqrts(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(Sqrt(Coordinates(getRowFromDirection(battleField), it), currentDirection))
        }
    }
    private fun launchMiddleProjectiles(battleField: BattleField65) {
        repeat(battleField.width) {
            if (it != freeCol) {
                battleField.addProjectile(Sqrt(Coordinates(getRowFromDirection(battleField), it), currentDirection))
            }
        }
    }
    private fun getRowFromDirection(battleField: BattleField65): Int {
        return if (currentDirection == BattleFieldProjectile.Direction.UP) {
            battleField.height
        } else {
            -1
        }
    }

    // MODESTY
    private fun modestyAttack(battleField: BattleField65, tickNumber: Int) {
        val cols = if (tickNumber % 3 == 0) {
            (0 until battleField.width)
        } else {
            (0 until battleField.width).takeRand(2)
        }
        cols.forEach { col ->
            battleField.addProjectile(BalloonEvil(Coordinates(battleField.height, col)))
        }
    }

    // MOUTH CORNERS
    // Invariant: currentRowsN.size <= rowsMax / 2
    private val currentRows1 = mutableSetOf<Int>()
    private val currentRows2 = mutableSetOf<Int>()
    private fun mouthCornersAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentRows1.clear()
            currentRows2.clear()
            spawnWalls(battleField)
        }

        val availableRows = (0..battleField.height / 2).map { it * 2 }
        val pRow = battleField.protagonist.position.row
        val rowsMax = battleField.height / 2
        when (tickNumber % 3) {
            0 -> {
                removeKissKissKiss(battleField)
                spawnKissKissKiss(battleField, 2)

                currentRows1.clear()

                val freeRows = availableRows - currentRows2
                if (pRow in currentRows2) {
                    currentRows1.addAll(freeRows.takeRand(rowsMax / 2))
                } else {
                    currentRows1.add(pRow)
                    currentRows1.addAll((freeRows - pRow).takeRand(rowsMax / 2 - 1))
                }

                currentRows1.forEach { row ->
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(Kiss(Coordinates(row, col)))
                    }
                }
            }
            1 -> {
                removeKissKissKiss(battleField)
                currentRows2.clear()

                val freeRows = availableRows - currentRows1
                if (pRow in currentRows1) {
                    currentRows2.addAll(freeRows.takeRand(rowsMax / 2))
                } else {
                    currentRows2.add(pRow)
                    currentRows2.addAll((freeRows - pRow).takeRand(rowsMax / 2 - 1))
                }
                currentRows2.forEach { row ->
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(Kiss(Coordinates(row, col)))
                    }
                }
            }
            2 -> {
                spawnKissKissKiss(battleField, 1)
            }
        }
    }
    private fun spawnWalls(battleField: BattleField65) {
        repeat(battleField.height / 2) {
            val row = it * 2 + 1
            repeat(battleField.width) { col ->
                battleField.addProjectile(WallStreet(Coordinates(row, col)))
            }
        }
    }
    private fun spawnKissKissKiss(battleField: BattleField65, batchNumber: Int) {
        val rows = if (batchNumber == 1) currentRows1 else currentRows2
        rows.forEach { row ->
            repeat(battleField.width) { col ->
                battleField.addProjectile(KissKissKiss(Coordinates(row, col)))
            }
        }
    }
    private fun removeKissKissKiss(battleField: BattleField65) {
        battleField.getAllObjects()
            .filterIsInstance<KissKissKiss>()
            .forEach { battleField.removeProjectile(it) }
    }

    // OPPRESSION
    private fun oppressionAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 0) {
            spawnLine(battleField)
        } else {
            spawnBat(battleField)
        }
    }
    private fun spawnBat(battleField: BattleField65) {
        val (col, dir) = getRandomColDir(battleField)
        battleField.addProjectile(Bat(Coordinates(-1, col), dir))
    }
    private fun spawnLine(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            battleField.addProjectile(BatFlipped(Coordinates(-1, col)))
        }
    }
    private fun getRandomColDir(battleField: BattleField65): Pair<Int, BattleFieldProjectile.Direction> {
        return Pair(
            randInt(1, battleField.width - 1),
            choose(BattleFieldProjectile.Direction.LEFT, BattleFieldProjectile.Direction.RIGHT)
        )
    }

    // PEACE
    private fun peaceAttack(battleField: BattleField65, tickNumber: Int, isStrong: Boolean = false) {
        if (tickNumber == 0) {
            repeat(battleField.width) {
                battleField.addProjectile(WallStreet(Coordinates(2, it)))
            }
        }

        launchTwoPigeons(battleField)

        if (tickNumber % 4 == 0) {
            repeat(battleField.width) {
                val projectile = PeaceSymbolDeadly(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP)
                if (isStrong) {
                    projectile.damage = 10
                }
                battleField.addProjectile(projectile)
            }
        }
    }
    private fun launchTwoPigeons(battleField: BattleField65) {
        val pigeons = battleField.getAllObjects().filterIsInstance<PigeonOfPeace>()
        if (pigeons.size < 2 && randBoolean()) {
            if (randBoolean()) {
                battleField.addProjectile(PigeonOfPeace(Coordinates(0, -1), BattleFieldProjectile.Direction.RIGHT, isDeadly = true))
            } else {
                battleField.addProjectile(PigeonOfPeace(Coordinates(1, battleField.width), BattleFieldProjectile.Direction.LEFT, isDeadly = true))
            }
        }
    }

    // RETREAT
    private fun retreatAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 1) {
            val rows = (0 until battleField.height).takeRand(3)
            rows.forEach { row ->
                battleField.addProjectile(Alien(Coordinates(row, battleField.width - 1)))
            }
        }
    }

    // RETURN
    private var upperLeftCorner = Coordinates(-1, -1)
    private fun returnAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            upperLeftCorner = Coordinates(-1, -1)
        }
        if (upperLeftCorner.row == -1) {
            upperLeftCorner = battleField.protagonist.position
            fillTheField(battleField)
            return
        }
        battleField.getAllObjects()
            .filterIsInstance<GrowingPlant>()
            .forEach { it.changeDirection(BattleFieldProjectile.Direction.NO_MOVEMENT) }

        if (tickNumber % 2 == 1) {
            val direction = if (randBooleanPercent(65)) {
                val rowDiff = battleField.protagonist.position.row - upperLeftCorner.row
                val colDiff = battleField.protagonist.position.col - upperLeftCorner.col
                val yAxisDirection = if (rowDiff == 0) {
                    BattleFieldProjectile.Direction.DOWN
                } else {
                    BattleFieldProjectile.Direction.UP
                }
                val xAxisDirection = if (colDiff == 0) {
                    BattleFieldProjectile.Direction.RIGHT
                } else {
                    BattleFieldProjectile.Direction.LEFT
                }
                choose(xAxisDirection, yAxisDirection)
            } else {
                listOf(
                    BattleFieldProjectile.Direction.UP, BattleFieldProjectile.Direction.DOWN,
                    BattleFieldProjectile.Direction.LEFT, BattleFieldProjectile.Direction.RIGHT
                ).random()
            }

            when (direction) {
                BattleFieldProjectile.Direction.UP, BattleFieldProjectile.Direction.DOWN -> {
                    battleField.getAllObjects()
                        .filterIsInstance<GrowingPlant>()
                        .filter {
                            val diff = it.position.col - upperLeftCorner.col
                            diff in 0..1 || diff == -(battleField.width - 1)
                        }
                        .forEach { it.changeDirection(direction) }
                }
                BattleFieldProjectile.Direction.LEFT, BattleFieldProjectile.Direction.RIGHT -> {
                    battleField.getAllObjects()
                        .filterIsInstance<GrowingPlant>()
                        .filter {
                            val diff = it.position.row - upperLeftCorner.row
                            diff in 0..1 || diff == -(battleField.height - 1)
                        }
                        .forEach { it.changeDirection(direction) }
                }
                BattleFieldProjectile.Direction.NO_MOVEMENT -> {}
            }

            upperLeftCorner += direction.getDelta()
            when {
                upperLeftCorner.row == -1 -> upperLeftCorner.row = battleField.height - 1
                upperLeftCorner.row == battleField.height -> upperLeftCorner.row = 0
                upperLeftCorner.col == -1 -> upperLeftCorner.col = battleField.width - 1
                upperLeftCorner.col == battleField.width -> upperLeftCorner.col = 0
            }
        }
    }
    private fun fillTheField(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            repeat(battleField.height) { row ->
                val rowDiff = row - upperLeftCorner.row
                val colDiff = col - upperLeftCorner.col
                if (!(rowDiff in 0..1 && colDiff in 0..1)) {
                    battleField.addProjectile(GrowingPlant(Coordinates(row, col)))
                }
            }
        }
    }

    // SPLITTING APART
    private var protagonistCol = -1
    private fun splittingApartAttack(battleField: BattleField65, tickNumber: Int) {
        val mid = battleField.width / 2
        if (tickNumber * 2 <= battleField.height) {
            for (i in 0..tickNumber) {
                battleField.addProjectile(WallStreet(Coordinates(i, mid)))
                battleField.addProjectile(WallStreet(Coordinates(battleField.height - 1 - i, mid)))
            }
        }

        if (tickNumber % 3 == 0) {
            addRings(battleField)
        }
        when (tickNumber % 6) {
            3 -> {
                protagonistCol = battleField.protagonist.position.col
                removeRings(battleField, mid)
            }
            5 -> {
                benzeneExplosion(battleField, mid)
            }
        }
    }
    private fun addRings(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(BenzeneRing(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
        }
    }
    private fun removeRings(battleField: BattleField65, mid: Int) {
        battleField.getAllObjects()
            .filter {
                if (protagonistCol < mid) {
                    it.position.col < mid
                } else {
                    it.position.col > mid
                }
            }
            .filterIsInstance<BattleFieldProjectile>()
            .forEach { battleField.removeProjectile(it) }
    }
    private fun benzeneExplosion(battleField: BattleField65, mid: Int) {
        val cols = if (protagonistCol < mid) {
            (0 until mid)
        } else {
            (mid + 1 until battleField.width)
        }
        for (col in cols) {
            repeat(battleField.height) {
                battleField.addProjectile(BenzeneRing(Coordinates(it + 1, col), BattleFieldProjectile.Direction.UP))
            }
        }
    }

    // THE MARRYING MAIDEN
    private fun theMarryingMaidenAttack(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                repeat(battleField.width) { col ->
                    battleField.addProjectile(TheMMBrokenHeart(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                }
            }
            1 -> {
                spawnArrowAboveProtagonist(battleField)
            }
            2 -> {
                val protagonistCol = battleField.protagonist.position.col
                val col = ((0 until battleField.width).minus(protagonistCol - 1..protagonistCol + 1)).random()
                battleField.addProjectile(
                    TheMMBrokenHeartSource(
                        Coordinates(battleField.protagonist.position.row, col)
                    )
                )
            }
        }
    }
    private fun spawnArrowAboveProtagonist(battleField: BattleField65) {
        val col = battleField.protagonist.position.col
        battleField.getAllObjects()
            .filterIsInstance<TheMMBrokenHeart>()
            .filter { it.direction == BattleFieldProjectile.Direction.DOWN }
            .filter { it.position.col == col }
            .filter { battleField.protagonist.position.row - it.position.row > 0 }
            .maxByOrNull { it.position.row }?.orderToShoot = true
    }

    // THE WANDERER
    private val rows = mutableListOf<Int>()
    private val cols = mutableListOf<Int>()
    private fun theWandererAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            rows.clear()
            cols.clear()
            repeat(battleField.width * battleField.height) {
                if (it % 2 == 1) {
                    val row = it / battleField.width
                    val col = it % battleField.width
                    battleField.addProjectile(WallStreet(Coordinates(row, col)))
                }
            }
        }
        when {
            tickNumber % 3 == 0 -> { rows.add(-1) }
            tickNumber % 4 == 1 -> { cols.add(battleField.width) }
        }

        battleField.getAllObjects().filterIsInstance<ThreeWaySharp>().forEach {
            battleField.removeProjectile(it)
        }

        rows.forEachIndexed { index, row ->
            repeat(battleField.width) { col ->
                battleField.addProjectile(ThreeWaySharp(Coordinates(row, col), BattleFieldProjectile.Direction.DOWN))
            }
            rows[index]++
        }
        rows.removeAll { it >= battleField.height }

        cols.forEachIndexed { index, col ->
            repeat(battleField.height) { row ->
                battleField.addProjectile(ThreeWaySharp(Coordinates(row, col), BattleFieldProjectile.Direction.LEFT))
            }
            cols[index]--
        }
        cols.removeAll { it < 0 }
    }

    // THE WELL
    private var currentMRows = mutableSetOf<Int>()
    private fun theWellAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentMRows.clear()
        }

        when (tickNumber % 4) {
            0 -> {
                currentMRows.clear()
                battleField.getAllObjects()
                    .filterIsInstance<LetterProjectile>()
                    .forEach { battleField.removeProjectile(it) }

                currentMRows = (0 until battleField.height)
                    .takeRand(battleField.height - 1)
                    .toMutableSet()
                currentMRows.take(battleField.height / 2).forEach { currentMRow ->
                    addM(battleField, currentMRow, BattleFieldProjectile.Direction.RIGHT)
                }
            }
            1 -> {
                currentMRows.reversed().take(battleField.height / 2).forEach { currentMRow ->
                    addM(battleField, currentMRow, BattleFieldProjectile.Direction.LEFT)
                }
            }
            2 -> {
                removeMs(battleField)
            }
            3 -> {
                currentMRows.forEach { currentMRow ->
                    (0 until battleField.width).forEach { col ->
                        battleField.addProjectile(
                            LetterProjectile(
                                Coordinates(currentMRow, col),
                                BattleFieldProjectile.Direction.NO_MOVEMENT,
                                "O",
                                color = R.color.green
                            )
                        )
                    }
                }
            }
        }
    }

    private fun addM(battleField: BattleField65, currentMRow: Int, direction: BattleFieldProjectile.Direction) {
        val coordinates = if (direction == BattleFieldProjectile.Direction.LEFT) {
            Coordinates(currentMRow, 0)
        } else {
            Coordinates(currentMRow, battleField.width - 1)
        }
        val projectile = LetterProjectile(
            coordinates,
            BattleFieldProjectile.Direction.NO_MOVEMENT,
            "M",
            color = R.color.dark_red
        ).apply {
            damage = DEFAULT_PROJECTILE_DAMAGE * 4
            priority = 2
        }
        battleField.addProjectile(projectile)
    }
    private fun removeMs(battleField: BattleField65) {
        battleField.getAllObjects()
            .filterIsInstance<LetterProjectile>()
            .filter { it.symbol == "M" }
            .forEach { battleField.removeProjectile(it) }
    }

    // TREADING
    private fun treadingAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            repeat(battleField.width) {
                battleField.addProjectile(TeslaSphereVertical(Coordinates(0, it)))
            }
        }

        battleField.getAllObjects().filterIsInstance<Lightning>().forEach {
            battleField.removeProjectile(it)
        }

        val direction = BattleFieldProjectile.Direction.LEFT
        (1 until battleField.height).forEach {
            if (randBooleanPercent(55)) {
                battleField.addProjectile(Stone(Coordinates(it, battleField.width), direction))
            }
        }

        if (tickNumber % 3 == 1) {
            battleField
                .getAllObjects()
                .filterIsInstance<TeslaSphereVertical>()
                .filter { it.position.col == battleField.protagonist.position.col }
                .forEach { it.charging = true }
        }
    }

    // Elements of Grace, Standstill, Splitting Apart,
    // DATB, Great Preponderance and Oppression.
    private fun multiAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            repeat(battleField.height) {
                battleField.addProjectile(CrazyDiamond(Coordinates(it, 0), BattleFieldProjectile.Direction.NO_MOVEMENT))
                battleField.addProjectile(CrazyDiamond(Coordinates(it, battleField.width - 1), BattleFieldProjectile.Direction.NO_MOVEMENT))
            }
        }

        val mid = battleField.width / 2
        if (tickNumber * 2 <= battleField.height) {
            for (i in 0..tickNumber) {
                battleField.addProjectile(WallStreet(Coordinates(i, mid)))
                battleField.addProjectile(WallStreet(Coordinates(battleField.height - 1 - i, mid)))
            }
        }
        battleField.getAllObjects().filterIsInstance<StandstillEightOfClubs>().forEach {
            it.position += it.direction.getDelta()
        }

        when (tickNumber % 4) {
            0 -> {
                battleField.getAllObjects().filterIsInstance<NuclearExplosion>()
                    .forEach {
                        battleField.removeProjectile(it)
                    }
                repeat(battleField.width) {
                    battleField.addProjectile(SteelRain(Coordinates(-1, it)))
                }
            }
            1 -> {
                battleField.addProjectile(StandstillEightOfClubs(Coordinates(battleField.height - 1, battleField.protagonist.position.col)))
            }
            2 -> {
                spawnBat(battleField)
            }
            3 -> {
                spawnBat(battleField)
            }
        }
        when (tickNumber) {
            8, 20 -> setUpRockets(battleField, 1)
            15 -> {
                battleField.getAllObjects()
                    .filterIsInstance<SteelRain>()
                    .forEach {
                        it.changeDirection(BattleFieldProjectile.Direction.UP)
                    }
            }
            19 -> {
                battleField.getAllObjects()
                    .filterIsInstance<SteelRain>()
                    .forEach {
                        it.changeDirection(BattleFieldProjectile.Direction.DOWN)
                    }
            }
        }
    }

    private var windRequiemLineSaid = false
    private var timebackCount = 0
    private var timebackLimitWarningIssued = false
    private var afterNextAttackDeath = false
    private var healthRegeneratedLineSaid = false
    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (afterNextAttackDeath && health > 0) {
            if (element is Heaven) {
                delay(tickTime)
                val phraseTime1 = manager.activity.getString(R.string.battlefield_the_creature_after_next_turn_death_0)
                val phraseTime2 = manager.activity.getString(R.string.battlefield_the_creature_after_next_turn_death_1)
                listOf(phraseTime1, phraseTime2).forEach {
                    playCreatureBit(manager)
                    (manager.drawer as BattleFieldDrawerTheCreature).writeWordsOnBlackScreen(it)
                    delay(tickTime * 3)
                }
                clearBlackScreen(manager)
            } else {
                playCreatureBit(manager)
                manager.drawer.showBlackScreen()
            }
            delay(tickTime)
            val phrasesList = mutableListOf<String>()
            phrasesList.add(manager.activity.getString(R.string.battlefield_the_creature_after_next_turn_death_2))
            if ((manager.battleField.protagonist as BattleFieldProtagonist).hasAnkh) {
                (manager.battleField.protagonist as BattleFieldProtagonist).hasAnkh = false
                phrasesList.add(manager.activity.getString(R.string.battlefield_the_creature_after_next_turn_death_f))
            }
            phrasesList.add(manager.activity.getString(R.string.battlefield_the_creature_after_next_turn_death_3))
            phrasesList.add(manager.activity.getString(R.string.battlefield_the_creature_after_next_turn_death_4))
            phrasesList.forEach {
                playCreatureBit(manager)
                (manager.drawer as BattleFieldDrawerTheCreature).writeWordsOnBlackScreen(it)
                delay(tickTime * 3)
            }
            clearBlackScreen(manager)
            manager.drawer.stopBlackScreen()
            delay(tickTime)
            manager.battleField.protagonist.health = 0
            playCreatureBit(manager)
            manager.drawer.updateProtagonistInstantly(manager.battleField.protagonist)
            delay(2000L)
            return
        }

        if (!isCheating && element == Heaven && battleField.moveNumber == 1) {
            playCreatureBit(manager)
            manager.battleField.protagonist.health = result.damage
            manager.drawer.updateProtagonistInstantly(manager.battleField.protagonist)
            delay(1000L)
            manager.drawer.stopBlackScreen()
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_first_move_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_first_move_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_first_move_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_first_move_4)
            (manager.battleField as BattleField65).takeFromJournal = false
            return
        }

        if (!isCheating && element == Heaven && result.type != AttackResult.DamageType.TIMEBACK_DENIED) {
            timebackCount++
            if (timebackCount == TIMEBACK_LIMIT && !timebackLimitExplained && !timebackLimitWarningIssued) {
                timebackLimitWarningIssued = true
                delay(tickTime)
                playCreatureBit(manager)
                val phrase = manager.activity.getString(R.string.battlefield_the_creature_close_to_timeback_limit_stop)
                (manager.drawer as BattleFieldDrawerTheCreature).writeWordsOnBlackScreen(phrase)
                delay(tickTime)
                clearBlackScreen(manager)
                delay(tickTime)
            }
        } else {
            timebackCount = 0
        }

        if (!isCheating && result.type == AttackResult.DamageType.TIMEBACK_DENIED) {
            if (!timebackLimitExplained) {
                timebackLimitExplained = true
                GlobalState.putBoolean(manager.activity, THE_CREATURE_TIMEBACK_LIMIT_EXPLAINED, true)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_limit_1)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_limit_2)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_limit_3)
                manager.drawer.showTextInEnemySpeechBubbleFormattedSuspend(
                    R.string.battlefield_the_creature_hit_timeback_limit_4,
                    formatArgs = arrayOf(TIMEBACK_LIMIT)
                )
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_limit_5)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_hit_timeback_limit_6)
            }
        }

        if (!isCheating && result.type == AttackResult.DamageType.WIND_REQUIEM_DEFLECTED) {
            if (!windRequiemLineSaid) {
                windRequiemLineSaid = true
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_requiem_deflected_1)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_requiem_deflected_2)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_requiem_deflected_3)
            } else {
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_requiem_deflected_repeat_1)
            }
            return
        }

        if (health <= 0) {
            when {
                !isCheating && result.damage > 20 -> {
                    if (healthRegeneratedLineSaid) {
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_again_1)
                    } else {
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_1)
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_2)
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_3)
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_4)
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_5)
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_too_fast_6)
                        healthRegeneratedLineSaid = true
                    }
                    health = when (manager.battleField.moveNumber) {
                        1, 2, 3, 4 -> maxHealth
                        5 -> 60
                        6 -> 40
                        7 -> 20
                        else -> 10
                    }
                    manager.drawer.updateEnemyHealthBar(this)
                }
                isCheating -> {
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_cheat_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_cheat_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_cheat_3)
                }
                wasKilled -> {
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_killed_again_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_killed_again_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_killed_again_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_killed_again_4)
                }
                else -> {
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_creature_on_death_3)
                }
            }
        }
    }

    private fun playCreatureBit(manager: BattleManager) {
        manager.activity.musicPlayer.playMusic(
            R.raw.sfx_creature_bit,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
    }

    companion object {
        const val TIMEBACK_LIMIT = 3
    }
}