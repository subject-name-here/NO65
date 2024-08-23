package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Dollar
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DollarWorthless
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay
import kotlin.reflect.full.primaryConstructor


class BattleFieldGreatPossession(hasAttackedFirst: Boolean, private val beforeD99: Boolean) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 14
    override val hexagramSymbol: String = "䷍"
    override val nameId: Int
        get() = R.string.battlefield_great_possession_name

    override val centerSymbolColorId: Int = R.color.green
    override val centerSymbol: String = "\uD83D\uDCB8"

    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Heaven

    override var defaultFace: String = "\uD83D\uDE11"
    override val damageReceivedFace: String = "\uD83D\uDE2C"
    override val noDamageReceivedFace: String = "\uD83D\uDE0F"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            if (hasAttackedFirst) {
                when (moveNumber) {
                    1 -> R.string.battlefield_great_possession_after_d99_1
                    2 -> R.string.battlefield_great_possession_after_d99_2
                    else -> R.string.battlefield_great_possession_after_d99_0
                }
            } else {
                when (moveNumber) {
                    1 -> R.string.battlefield_great_possession_1
                    2 -> R.string.battlefield_great_possession_2
                    3 -> R.string.battlefield_great_possession_3
                    4 -> R.string.battlefield_great_possession_4
                    else -> R.string.battlefield_great_possession_0
                }
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_great_possession_d
        override fun getVictoryLine(): Int = R.string.battlefield_great_possession_v
    }

    private var selectedSpaces = listOf<Int>()
    private val shiftedElements = (0..4).toMutableList()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            selectedSpaces = emptyList()
            shiftedElements.shuffle()
            drawWalls(battleField)
            return
        }

        battleField.getAllObjects().filterIsInstance<Dollar>().forEach { battleField.removeProjectile(it) }

        for (spaceNum in selectedSpaces) {
            fillSpace(battleField, spaceNum, harmless = false)
        }

        selectedSpaces = when (battleField.moveNumber) {
            1, 2 -> {
                listOf(shiftedElements[tickNumber % 5])
            }
            3, 4 -> {
                val firstSpaceIndex = tickNumber * 2 % 5
                val secondSpaceIndex = (tickNumber * 2 + 1) % 5
                listOf(shiftedElements[firstSpaceIndex], shiftedElements[secondSpaceIndex])
            }
            5 -> {
                val firstSpaceIndex = tickNumber * 2 % 5
                if (firstSpaceIndex == 0) {
                    shiftedElements.shuffle()
                }
                val firstElement = shiftedElements[firstSpaceIndex]

                val secondSpaceIndex = (tickNumber * 2 + 1) % 5
                if (secondSpaceIndex == 0) {
                    shiftedElements.shuffle()
                }
                val secondElement = shiftedElements[secondSpaceIndex]

                listOf(firstElement, secondElement)
            }
            else -> {
                val protagonistSpace = getSpaceByCoordinates(battleField.protagonist.position, battleField)
                listOf(protagonistSpace, ((0..4) - protagonistSpace - selectedSpaces.toSet()).random())
            }
        }

        for (spaceNum in selectedSpaces) {
            fillSpace(battleField, spaceNum, harmless = true)
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

    private fun fillSpace(battleField: BattleField65, spaceNumber: Int, harmless: Boolean) {
        val middleTopWallRow = battleField.height / 2 - 2
        val middleBottomWallRow = battleField.height / 2 + 2
        val wallCol = battleField.width / 2

        val projectile = if (harmless) DollarWorthless::class.primaryConstructor else Dollar::class.primaryConstructor
        val defaultProjectile = WallStreet(Coordinates(-1, -1))

        fun addProjectile(row: Int, col: Int) {
            battleField.addProjectile(projectile?.call(Coordinates(row, col)) ?: defaultProjectile)
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

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (health <= 0) {
            manager.activity.musicPlayer.stopAllMusic()
            defaultFace = "\uD83D\uDE0F"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_4)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_5)
            if (beforeD99) {
                delay(500L)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_d99_1)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_d99_2)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_d99_3)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_d99_4)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_great_possession_on_death_d99_5)
            }
        }
    }
}