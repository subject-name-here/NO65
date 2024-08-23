package com.unicorns.invisible.no65.model.battlefield.fighter.equal

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacterEqual
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.AbysmalWaterDrop
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.AbysmalWaterDropAuto
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BCSpaceDistorter
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.util.*
import com.unicorns.invisible.no65.view.BattleFieldDrawerEqual
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


class BattleFieldEqualAbysmalWater(
    private val isWatereverCutsceneless: Boolean = false
) : BattleFieldCharacterEqual() {
    override fun getString(): String = "Ӻ"
    override fun getStringColor(): Int = R.color.dark_grey

    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 40

    override val nameId: Int
        get() = R.string.battlefield_abysmal_water_name
    override val number: Int
        get() = 29

    override val defaultFace: String
        get() = "\uD83D\uDC19"
    override val damageReceivedFace: String
        get() = "\uD83D\uDFBF"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDC19"

    override val centerSymbol: String
        get() = "⛲"
    override val centerSymbolColorId: Int
        get() = R.color.light_blue

    override val hexagramSymbol: String
        get() = "䷜"

    override val musicThemeId: Int = R.raw.battle_abysmal_water
    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT

    override var secondChanceState = SecondChanceState.HAS_SECOND_CHANCE

    override fun getLineGenerator(enemy: BattleFieldCharacterEqual): BattleFieldLineGenerator {
        return when (enemy) {
            is BattleFieldEqualBeforeCompletion ->
                if (isWatereverCutsceneless) {
                    bcLineGeneratorCutsceneless
                } else {
                    bcLineGenerator
                }
            else -> BattleFieldLineGenerator.EMPTY
        }
    }

    private var secondChanceMoves = 0
    private val bcLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (secondChanceState == SecondChanceState.USED_SECOND_CHANCE) {
            when (secondChanceMoves) {
                0 -> R.string.battlefield_abysmal_water_after_second_chance
                else -> R.string.empty_line
            }
        } else {
            when (moveNumber) {
                1 -> R.string.battlefield_abysmal_water_1
                2 -> R.string.battlefield_abysmal_water_2
                3 -> R.string.battlefield_abysmal_water_3
                4 -> R.string.battlefield_abysmal_water_4
                5 -> {
                    if (health <= maxHealth / 5)
                        R.string.battlefield_abysmal_water_5_low_health
                    else
                        R.string.battlefield_abysmal_water_5
                }
                else -> R.string.battlefield_abysmal_water_else
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_abysmal_water_d
        override fun getVictoryLine(): Int = R.string.battlefield_abysmal_water_v
    }
    private val bcLineGeneratorCutsceneless = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_abysmal_water_1_alternative
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_abysmal_water_d_alternative
        override fun getVictoryLine(): Int = R.string.battlefield_abysmal_water_v_alternative
    }

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        if (tickNumber == 0) {
            createWalls(battleField)
            chargingCols.clear()
            fullCols.clear()
            currentCol = -1
            targetedRow = -1
            return
        }

        when (battleField.moveNumber) {
            1 -> simpleAttack(battleField)
            2 -> crossAttack(battleField, tickNumber)
            3, 6, 10 -> multiCrossAttack(battleField, tickNumber)
            else -> multiCrossQuickAttack(battleField, tickNumber)
        }
    }

    private val chargingCols = mutableListOf<Int>()
    private val fullCols = mutableListOf<Int>()
    private fun simpleAttack(battleField: BattleField) {
        fullCols.forEach { col ->
            repeat(battleField.height) { row ->
                val proj = battleField.getMap()[Coordinates(row, col)]
                if (proj is BattleFieldProjectile) {
                    battleField.removeProjectile(proj)
                }
            }
        }
        fullCols.clear()

        chargingCols.forEach { col ->
            repeat(battleField.height) { row ->
                battleField.addProjectile(AbysmalWaterDrop(Coordinates(row, col)))
            }
        }
        fullCols.addAll(chargingCols)
        chargingCols.clear()

        val allCols = getAllCols(battleField)
        val freeCols = (allCols - fullCols.toSet()).toMutableSet()

        if (freeCols.size > 1) {
            if (randBoolean()) {
                val pCol = battleField.protagonist.position.col
                if (pCol in freeCols) {
                    chargingCols.add(pCol)
                    freeCols.remove(pCol)
                }
            }

            val newChargingColsSize = when (freeCols.size) {
                0, 1 -> 0
                2 -> choose(0, 1, 1)
                else -> choose(0, 1, 1, 2, 2)
            }
            chargingCols.addAll(freeCols.takeRand(newChargingColsSize))
            chargingCols.forEach { col ->
                addGeneratorDrop(battleField, col)
            }
        }
    }

    private var currentCol: Int = -1
    private var targetedRow: Int = -1
    private fun crossAttack(battleField: BattleField, tickNumber: Int) {
        when (tickNumber % 3) {
            1 -> {
                clearAllDrops(battleField)

                currentCol = getAllCols(battleField).random()
                addGeneratorDrop(battleField, currentCol)
            }
            2 -> {
                repeat(battleField.height) { row ->
                    battleField.addProjectile(AbysmalWaterDrop(Coordinates(row, currentCol)))
                }
                val pRow = battleField.protagonist.position.row
                val pCol = battleField.protagonist.position.col
                val proj = battleField.getMap()[Coordinates(pRow, currentCol)]
                if (proj is AbysmalWaterDrop) {
                    val direction = if (pCol > currentCol)
                        BattleFieldProjectile.Direction.RIGHT
                    else
                        BattleFieldProjectile.Direction.LEFT
                    proj.changeDirection(direction)
                }
                targetedRow = pRow
            }
            0 -> {
                val proj = battleField.getMap()[Coordinates(targetedRow, currentCol)]
                if (proj is AbysmalWaterDrop) {
                    val condition: (Int) -> Boolean = when (proj.direction) {
                        BattleFieldProjectile.Direction.LEFT -> { it -> it <= currentCol }
                        BattleFieldProjectile.Direction.RIGHT -> { it -> it >= currentCol }
                        else -> { _ -> false }
                    }
                    getAllCols(battleField).filter(condition).forEach { col ->
                        battleField.addProjectile(AbysmalWaterDrop(Coordinates(targetedRow, col)))
                    }
                }
            }
        }
    }

    private fun multiCrossAttack(battleField: BattleField, tickNumber: Int) {
        when (tickNumber % 3) {
            1 -> {
                clearAllDrops(battleField)

                currentCol = choose(getAllCols(battleField).random(), battleField.protagonist.position.col)
                addGeneratorDrop(battleField, currentCol)
            }
            2 -> {
                repeat(battleField.height) { row ->
                    battleField.addProjectile(AbysmalWaterDrop(Coordinates(row, currentCol)))
                }
                multiCrossGenerateDirections(battleField)
            }
            0 -> {
                multiCrossShootFromSides(battleField)
            }
        }
    }

    private fun multiCrossQuickAttack(battleField: BattleField, tickNumber: Int) {
        when (tickNumber % 2) {
            1 -> {
                multiCrossShootFromSides(battleField)

                currentCol = choose(getAllCols(battleField).random(), battleField.protagonist.position.col)
                addGeneratorDrop(battleField, currentCol)
            }
            0 -> {
                clearAllDrops(battleField)
                repeat(battleField.height) { row ->
                    battleField.addProjectile(AbysmalWaterDrop(Coordinates(row, currentCol)))
                }
                multiCrossGenerateDirections(battleField)
            }
        }
    }

    private fun getAllCols(battleField: BattleField): List<Int> {
        return (0..battleField.width / 2).map { it * 2 }
    }

    private fun createWalls(battleField: BattleField) {
        repeat(battleField.width / 2) {
            val col = 2 * it + 1
            repeat(battleField.height) { row ->
                battleField.addProjectile(WallStreet(Coordinates(row, col)).apply { damage = 100 })
            }
        }
    }

    private fun clearAllDrops(battleField: BattleField) {
        battleField.getAllObjects()
            .filterIsInstance<AbysmalWaterDrop>()
            .forEach { battleField.removeProjectile(it) }
    }

    private fun addGeneratorDrop(battleField: BattleField, col: Int) {
        val drop = AbysmalWaterDrop(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN)
        drop.changeColor(R.color.dark_red)
        battleField.addProjectile(drop)
    }

    private val windowSize
        get() = choose(2, 3, 4)
    private val directions = mutableListOf<BattleFieldProjectile.Direction>()
    private fun multiCrossGenerateDirections(battleField: BattleField) {
        val pRow = battleField.protagonist.position.row
        val pCol = battleField.protagonist.position.col
        val projMap = battleField.getMap()
        repeat(battleField.height) { row ->
            val proj = projMap[Coordinates(row, currentCol)]
            if (proj is AbysmalWaterDrop) {
                val direction = if (row == pRow) {
                    if (pCol > currentCol)
                        BattleFieldProjectile.Direction.RIGHT
                    else
                        BattleFieldProjectile.Direction.LEFT
                } else {
                    val window = directions.takeLast(windowSize)
                    when (window.distinct().size) {
                        0 -> SIDES_LIST.random()
                        1 -> {
                            val remainingDirection = SIDES_LIST - window.toSet()
                            remainingDirection.firstOrNull() ?: BattleFieldProjectile.Direction.NO_MOVEMENT
                        }
                        else -> {
                            SIDES_LIST.random()
                        }
                    }
                }
                directions.add(direction)
                proj.changeDirection(direction)
            }
        }
        directions.clear()
    }

    private fun multiCrossShootFromSides(battleField: BattleField) {
        repeat(battleField.height) { row ->
            val proj = battleField.getMap()[Coordinates(row, currentCol)]
            if (proj is AbysmalWaterDrop) {
                val allCols = getAllCols(battleField)
                val sideCols = when (proj.direction) {
                    BattleFieldProjectile.Direction.LEFT -> allCols.filter { it <= currentCol }
                    BattleFieldProjectile.Direction.RIGHT -> allCols.filter { it >= currentCol }
                    else -> listOf()
                }
                sideCols.forEach { col ->
                    battleField.addProjectile(AbysmalWaterDrop(Coordinates(row, col)))
                }
            }
        }
    }

    private var defensiveJob: Job? = null
    override fun defend(battleField: BattleField) {
        defensiveJob = when (battleField.protagonist) {
            is BattleFieldEqualBeforeCompletion -> defensiveJobBeforeCompletion(battleField)
            else -> Job()
        }
    }

    private fun defensiveJobBeforeCompletion(battleField: BattleField) = launchCoroutine {
        while (isActive) {
            val delayTime = when (battleField.moveNumber) {
                1 -> tickTime * 4
                2 -> tickTime * 7 / 2
                else -> tickTime * 3
            }
            delay(delayTime)

            val allCoordinates = mutableListOf<Coordinates>()
            (0 until battleField.width).forEach { col ->
                (1 until battleField.height - 1).forEach { row ->
                    allCoordinates.add(Coordinates(row, col))
                }
            }
            val occupiedCoordinates = battleField.getAllObjects().map { it.position }
            val remainingCoordinates = allCoordinates - occupiedCoordinates.toSet()
            if (remainingCoordinates.isNotEmpty()) {
                battleField.changeEnemyCoordinates(remainingCoordinates.random())
            }
        }
    }

    override fun stopDefending() {
        defensiveJob?.cancel()
        defensiveJob = null
    }

    override fun onMoveReversedStart(battleField: BattleField) {
        createWalls(battleField)
    }

    override fun onTapAttack(coordinates: Coordinates, battleField: BattleField) {
        val sourcesPossibleCoordinates = listOf(
            Coordinates(0, 0),
            Coordinates(0, 2),
            Coordinates(0, 4),
        )
        val projectilesMap = battleField.getMap()
        if (coordinates in sourcesPossibleCoordinates) {
            val occupiedSources = sourcesPossibleCoordinates
                .map { sourceCoordinates -> projectilesMap[sourceCoordinates] }
                .count { it is BattleFieldProjectile && it.direction != BattleFieldProjectile.Direction.NO_MOVEMENT }
            if (occupiedSources == 0) {
                battleField.addProjectile(AbysmalWaterDropAuto(coordinates, BattleFieldProjectile.Direction.DOWN))
            }
        } else {
            val occupant = battleField.getMap()[coordinates]
            if (occupant is AbysmalWaterDropAuto && occupant.direction in SIDES_LIST) {
                val newDirection = (SIDES_LIST - occupant.direction).firstOrNull() ?: BattleFieldProjectile.Direction.NO_MOVEMENT
                occupant.changeDirectionInstantly(newDirection)
            }
        }
    }

    override suspend fun onSecondChanceUsage(manager: BattleManager) {
        if (isWatereverCutsceneless) {
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_cutsceneless_1)
            return
        }

        val drawerEqual = manager.drawer as BattleFieldDrawerEqual
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_1)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_2)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_3)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_4)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_5)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_6)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_7)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_8)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_9)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_bc_1)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_10)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_bc_2)
        delay(1000L)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_11)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_12)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_bc_3)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_second_chance_bc_4)
    }

    override suspend fun onAttackEnd(manager: BattleManager) {
        val battleField = manager.battleField
        if (battleField.protagonist is BattleFieldEqualBeforeCompletion) {
            if (battleField.moveNumber == 1 && !isWatereverCutsceneless) {
                bcAttackIntroduction(manager)
            } else if (secondChanceState == SecondChanceState.USED_SECOND_CHANCE) {
                when (secondChanceMoves++) {
                    1 -> if (health == maxHealth) { attackHints(manager) }
                }
            }
        }
    }
    private suspend fun bcAttackIntroduction(manager: BattleManager) {
        val drawerEqual = manager.drawer as BattleFieldDrawerEqual
        drawerEqual.hideGiveUpButtonSoft()
        manager.activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_scratch,
            behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
            isLooping = false
        )
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_1)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_2)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_3)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_4)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_5)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_6)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_7)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_8)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_9)

        manager.activity.musicPlayer.playMusic(
            R.raw.battle_abysmal_water_bc_cutscene,
            behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
            isLooping = true
        )

        delay(1000L)
        drawerEqual.showFieldReversed()
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_10)
        delay(1500L)
        manager.battleField.addProjectile(BCSpaceDistorter(Coordinates(0, 2)))
        drawerEqual.drawField(manager.battleField)
        delay(1000L)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_11)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_12)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_13)

        delay(1500L)
        manager.battleField.addProjectile(BCSpaceDistorter(Coordinates(manager.battleField.height - 1, 2)))
        drawerEqual.drawField(manager.battleField)
        delay(1000L)

        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_14)

        delay(500L)
        manager.battleField.getAllObjects()
            .filterIsInstance<BCSpaceDistorter>()
            .forEach { it.onTick(0, manager.battleField) }
        drawerEqual.drawField(manager.battleField)
        delay(500L)

        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_15)

        drawerEqual.hideAll()
        drawerEqual.drawField(manager.battleField)
        manager.battleField.clear()
        delay(1000L)

        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_16)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_17)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_18)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_19)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_20)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_21)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_22)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_bc_attack_tutorial_bc_23)
        manager.activity.musicPlayer.stopMusicByResourceId(R.raw.battle_abysmal_water_bc_cutscene)

        delay(1000L)
        manager.activity.musicPlayer.resumeAllMusic()
        drawerEqual.showGiveUpButton()
    }

    private suspend fun attackHints(manager: BattleManager) {
        val drawerEqual = manager.drawer as BattleFieldDrawerEqual
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_1)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_2)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_3)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_4)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_5)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_6)
        delay(2000L)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_7)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_8)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_9)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_10)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_11)
        delay(3000L)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_12)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_13)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_14)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_15)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_16)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_17)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_18)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_19)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_20)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_21)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_22)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_23)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_24, withDelay = false)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_25)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_26)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_27)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_28)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_29)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_30)
        delay(2000L)
        drawerEqual.showTextInProtagonistSpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_bc_31)
        drawerEqual.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_abysmal_water_hints_32)
    }

    companion object {
        val SIDES_LIST = listOf(BattleFieldProjectile.Direction.RIGHT, BattleFieldProjectile.Direction.LEFT)
    }
}