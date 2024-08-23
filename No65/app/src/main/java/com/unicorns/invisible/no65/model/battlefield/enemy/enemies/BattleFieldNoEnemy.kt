package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.NoEnemyEmptyProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.NoEnemyNumber
import com.unicorns.invisible.no65.model.elements.trigram.AttackTrigram
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.util.*


class BattleFieldNoEnemy : BattleFieldEnemy() {
    override val maxHealth: Int = 404
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 404
    override val defenceTimeSec: Int = 404

    override val number: Int = 404
    override val nameId: Int = R.string.battlefield_no_enemy_name

    override val centerSymbolColorId: Int = R.color.no_color
    override val centerSymbol: String = "404"

    override val hexagramSymbol: String = "404"
    override val outerSkinTrigram = Heaven
    override val innerHeartTrigram = Heaven

    override val defaultFace: String = ""
    override val damageReceivedFace: String = ""
    override val noDamageReceivedFace: String = ""
    override val legsSymbol: String = ""
    override val handsSymbol: String = ""

    override val musicThemeId: Int
        get() = R.raw.battle_no_enemy

    override val beatId: Int
        get() = R.raw.sfx_no_enemy_beat

    override val goNumbersToDelays = listOf(
        0 to tickTime * 3 / 2,
        404 to tickTime * 3 / 2,
    )
    override val goText: String = "ERROR"
    override val afterGoTextDelay: Long
        get() = tickTime * 3 / 2

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = R.string.empty_line
        override fun getDefeatedLine(): Int = R.string.empty_line
        override fun getVictoryLine(): Int = R.string.empty_line
    }

    override fun onMoveStart(battleField: BattleField) {
        if (battleField.moveNumber >= 2 && health > 0) {
            health = 0
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            rowsAttack3.clear()
            deceivingTrail = 0
        }

        when (tickNumber) {
            in 0..30 -> {
                attack1(battleField, tickNumber)
            }
            in 31..60 -> {
                attack2(battleField, tickNumber - 31)
            }
            in 61..100 -> {
                attack3(battleField, tickNumber - 61)
            }
            in 101..150 -> {
                attack9(battleField)
            }
            in 151..200 -> {
                attack4(battleField, tickNumber - 151)
            }
            in 201..250 -> {
                attack5(battleField, tickNumber - 201)
            }
            in 251..300 -> {
                attack6(battleField, tickNumber - 251)
            }
            in 301..352 -> {
                attack7(battleField, tickNumber)
            }
            else -> {
                attack8(battleField, tickNumber - 353)
            }
        }
    }

    private fun attack1(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 0) {
            repeat(battleField.width) { col ->
                battleField.addProjectile(NoEnemyEmptyProjectile(
                    Coordinates(-1, col),
                    BattleFieldProjectile.Direction.DOWN
                ))
            }
        }
    }
    private fun attack2(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 0) {
            repeat(battleField.width) { col ->
                battleField.addProjectile(NoEnemyNumber(
                    Coordinates(-1, col),
                    BattleFieldProjectile.Direction.DOWN,
                    battleField.width,
                    battleField.height
                ))
            }
        }
    }
    private val rowsAttack3 = mutableListOf<Int>()
    private fun attack3(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                rowsAttack3.clear()
                repeat(battleField.height) { row ->
                    battleField.addProjectile(NoEnemyEmptyProjectile(
                        Coordinates(row, battleField.width),
                        BattleFieldProjectile.Direction.LEFT
                    ))
                }
            }
            1 -> {
                rowsAttack3.addAll(
                    (0 until battleField.height)
                        .takeRand(battleField.height * 2 / 3)
                )
                addNumbersAttack3(battleField)
            }
            2 -> {
                addNumbersAttack3(battleField)
            }
        }
    }
    private fun addNumbersAttack3(battleField: BattleField65) {
        rowsAttack3.forEach { row ->
            battleField.addProjectile(NoEnemyNumber(
                Coordinates(row, battleField.width),
                BattleFieldProjectile.Direction.LEFT,
                battleField.width,
                battleField.height,
            ))
        }
    }
    private fun attack4(battleField: BattleField65, tickNumber: Int) {
        repeat(2) {
            val col = randInt(battleField.width)
            battleField.addProjectile(NoEnemyEmptyProjectile(
                Coordinates(battleField.height, col),
                BattleFieldProjectile.Direction.UP
            ))
        }

        val row = if (tickNumber % 5 == 0 && randBoolean())
            battleField.protagonist.position.row
        else
            randInt(battleField.height)
        battleField.addProjectile(NoEnemyNumber(
            Coordinates(row, battleField.width),
            BattleFieldProjectile.Direction.LEFT,
            battleField.width,
            battleField.height,
        ))
    }
    private var deceivingTrail = 0
    private fun attack5(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                repeat(battleField.width) { col ->
                    battleField.addProjectile(NoEnemyEmptyProjectile(
                        Coordinates(battleField.height, col),
                        BattleFieldProjectile.Direction.UP
                    ))
                }
            }
            1 -> {
                if (randBoolean() || deceivingTrail + 1 >= battleField.height / 3) {
                    deceivingTrail = 0
                } else {
                    deceivingTrail++
                }
                if (deceivingTrail != 0) {
                    spawnNumbersRowUp(battleField)
                } else {
                    spawnNumberUp(battleField, randInt(battleField.width))
                }
            }
            2 -> {
                if (deceivingTrail != 0) {
                    spawnNumbersRowUp(battleField)
                } else {
                    spawnNumberUp(battleField, randInt(battleField.width))
                }
            }
        }
    }
    private fun spawnNumbersRowUp(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            spawnNumberUp(battleField, col)
        }
    }
    private fun spawnNumberUp(battleField: BattleField65, col: Int) {
        battleField.addProjectile(NoEnemyNumber(
            Coordinates(battleField.height, col),
            BattleFieldProjectile.Direction.UP,
            battleField.width,
            battleField.height
        ))
    }

    private fun attack6(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                val col = randInt(battleField.width)
                battleField.addProjectile(NoEnemyNumber(
                    Coordinates(battleField.height, col),
                    BattleFieldProjectile.Direction.UP,
                    battleField.width,
                    battleField.height,
                ))
            }
            1 -> {
                val col = randInt(battleField.width)
                battleField.addProjectile(NoEnemyNumber(
                    Coordinates(-1, col),
                    BattleFieldProjectile.Direction.DOWN,
                    battleField.width,
                    battleField.height,
                ))
            }
            2 -> {
                val emptyProjectiles = battleField.getAllObjects().filterIsInstance<NoEnemyEmptyProjectile>()
                if (emptyProjectiles.isEmpty() && randBoolean()) {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(NoEnemyEmptyProjectile(
                            Coordinates(-1, col),
                            BattleFieldProjectile.Direction.DOWN
                        ))
                        battleField.addProjectile(NoEnemyEmptyProjectile(
                            Coordinates(battleField.height, col),
                            BattleFieldProjectile.Direction.UP
                        ))
                    }
                }
            }
        }
    }

    private fun attack7(battleField: BattleField65, tickNumber: Int) {
        val numOfEmptyProjectiles = choose(1, 1, 2)
        (0 until battleField.width).takeRand(numOfEmptyProjectiles).forEach { col ->
            battleField.addProjectile(NoEnemyEmptyProjectile(
                Coordinates(-1, col),
                BattleFieldProjectile.Direction.DOWN
            ))
        }

        if (tickNumber % 2 == 0) {
            battleField.addProjectile(NoEnemyNumber(
                Coordinates(battleField.protagonist.position.row + randD(), -1),
                BattleFieldProjectile.Direction.RIGHT,
                battleField.width,
                battleField.height,
            ))
        }
    }

    private fun attack8(battleField: BattleField65, tickNumber: Int) {
        val numOfProjectiles = battleField.height / 2 - 1
        val list = (0 until battleField.height).takeRand(numOfProjectiles)
        when (tickNumber % 3) {
            0 -> {}
            1 -> {
                list.forEach { row ->
                    battleField.addProjectile(NoEnemyEmptyProjectile(
                        Coordinates(row, -1),
                        BattleFieldProjectile.Direction.RIGHT
                    ))
                }
            }
            2 -> {
                list.forEach { row ->
                    battleField.addProjectile(NoEnemyNumber(
                        Coordinates(row, -1),
                        BattleFieldProjectile.Direction.RIGHT,
                        battleField.width,
                        battleField.height
                    ))
                }
            }
        }
    }

    private fun attack9(battleField: BattleField65) {
        // CHAOS-CHAOS-CHAOS!!!
        val direction = BattleFieldProjectile.Direction.values().toList().random()
        val isOntoProtagonist = randBoolean()
        val pPos = battleField.protagonist.position
        val coordinates = when (direction) {
            BattleFieldProjectile.Direction.UP -> {
                val col = if (!isOntoProtagonist) randInt(battleField.width) else pPos.col
                Coordinates(battleField.height, col)
            }
            BattleFieldProjectile.Direction.DOWN -> {
                val col = if (!isOntoProtagonist) randInt(battleField.width) else pPos.col
                Coordinates(-1, col)
            }
            BattleFieldProjectile.Direction.LEFT -> {
                val row = if (!isOntoProtagonist) randInt(battleField.height) else pPos.row
                Coordinates(row, -1)
            }
            BattleFieldProjectile.Direction.RIGHT -> {
                val row = if (!isOntoProtagonist) randInt(battleField.height) else pPos.row
                Coordinates(row, battleField.width)
            }
            BattleFieldProjectile.Direction.NO_MOVEMENT -> {
                // Lucky you!
                return
            }
        }

        val projectile = if (randBoolean()) {
            NoEnemyEmptyProjectile(
                coordinates,
                direction
            )
        } else {
            NoEnemyNumber(
                coordinates,
                direction,
                battleField.width,
                battleField.height
            )
        }
        battleField.addProjectile(projectile)
    }

    override fun receiveTrigramDamage(trigram: AttackTrigram, multiplier: Int): Int {
        health = 0
        return 0
    }
}