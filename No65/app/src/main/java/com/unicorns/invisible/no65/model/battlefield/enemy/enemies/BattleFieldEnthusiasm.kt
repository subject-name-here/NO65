package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Bang
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Battery
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldEnthusiasm : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * (ENEMY_MAYBE_HEALTH_MULTIPLIER + 1)
    override var health: Int = maxHealth

    override val number: Int = 16
    override val nameId: Int
        get() = R.string.battlefield_enthusiasm_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "⛳"

    override val hexagramSymbol: String = "䷏"
    override val outerSkinTrigram = Thunder
    override val innerHeartTrigram = Earth

    override var defaultFace: String = "⍡"
    override val damageReceivedFace: String = "⍣"
    override val noDamageReceivedFace: String = "⍢"

    override val animation: CharacterAnimation = CharacterAnimation.ROTATE_FULL

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_enthusiasm_1
                2 -> R.string.battlefield_enthusiasm_2
                3 -> R.string.battlefield_enthusiasm_3
                4 -> R.string.battlefield_enthusiasm_4
                5 -> R.string.battlefield_enthusiasm_5
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.empty_line
        override fun getVictoryLine(): Int = R.string.battlefield_enthusiasm_v
    }

    override fun onMoveStart(battleField: BattleField) {
        if (battleField.moveNumber > 5) {
            health = 0
            defaultFace = "x_x"
        }
    }

    private val queue = ArrayDeque<Int>()
    private var cols = mutableListOf<Int>()
    private var safeRow = -1

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            queue.clear()
            cols = mutableListOf()
            safeRow = -1
        }

        when (battleField.moveNumber) {
            1 -> {
                val col = battleField.protagonist.position.col
                queue.addLast(col)

                for (c in queue) {
                    battleField.addProjectile(Bang(Coordinates(battleField.height, c), BattleFieldProjectile.Direction.UP))
                    battleField.addProjectile(Bang(Coordinates(-1, c), BattleFieldProjectile.Direction.DOWN))
                }

                if (queue.size > battleField.height / 2) {
                    queue.removeFirst()
                }
            }
            2 -> {
                if (tickNumber % 2 == 0) {
                    cols.clear()
                    generateOneBang(battleField)
                } else {
                    generateLineBang(battleField)
                }
            }
            3 -> {
                val safeLineWidth = battleField.width
                if (tickNumber % safeLineWidth == 0) {
                    safeRow = randInt(battleField.height)
                }

                repeat(battleField.height) {
                    if (it != safeRow) {
                        if (it % 2 == 0) {
                            battleField.addProjectile(Battery(Coordinates(it, -1), BattleFieldProjectile.Direction.RIGHT))
                        } else {
                            battleField.addProjectile(Battery(Coordinates(it, battleField.width), BattleFieldProjectile.Direction.LEFT))
                        }
                    }
                }
            }
            4, 5 -> {
                val col = battleField.protagonist.position.col
                queue.addLast(col)

                for (c in queue) {
                    battleField.addProjectile(Bang(Coordinates(battleField.height, c), BattleFieldProjectile.Direction.UP))
                }

                if (queue.size > battleField.width - 1) {
                    queue.removeFirst()
                }

                if (tickNumber % battleField.width == 0) {
                    repeat(battleField.height) {
                        if (it % 2 == 0) {
                            battleField.addProjectile(Battery(Coordinates(it, -1), BattleFieldProjectile.Direction.RIGHT))
                        } else {
                            battleField.addProjectile(Battery(Coordinates(it, battleField.width), BattleFieldProjectile.Direction.LEFT))
                        }
                    }
                }
            }
            else -> {}
        }
    }

    private fun generateOneBang(battleField: BattleField65) {
        battleField.removeProjectiles()
        cols = arrayListOf(
            battleField.protagonist.position.col,
            battleField.protagonist.position.col,
            randInt(battleField.width)
        ).shuffled().toMutableList()

        battleField.addProjectile(Bang(Coordinates(-1, cols[0]), BattleFieldProjectile.Direction.DOWN))
        battleField.addProjectile(Bang(Coordinates(battleField.height, cols[1]), BattleFieldProjectile.Direction.UP))
    }

    private fun generateLineBang(battleField: BattleField65) {
        repeat(battleField.height / 2 + 1) {
            battleField.addProjectile(Bang(Coordinates(it, cols[0]), BattleFieldProjectile.Direction.NO_MOVEMENT))
        }
        repeat(battleField.height / 2 + 1) {
            val row = battleField.height - it - 1
            battleField.addProjectile(Bang(Coordinates(row, cols[1]), BattleFieldProjectile.Direction.NO_MOVEMENT))
        }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        health = maxHealth
    }
}