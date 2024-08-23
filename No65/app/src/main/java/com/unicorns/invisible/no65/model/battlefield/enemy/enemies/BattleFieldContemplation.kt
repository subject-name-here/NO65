package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Baton
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Handcuffs
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Pistol
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldContemplation : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_contemplation_name
    override val number: Int = 20

    override val defaultFace: String
        get() = "\uD83D\uDD75"
    override val damageReceivedFace: String
        get() = "\uD83D\uDD75"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDD75"

    override val centerSymbol: String
        get() = "\uD83D\uDD2B"
    override val centerSymbolColorId: Int
        get() = R.color.almost_black

    override val hexagramSymbol: String
        get() = "䷓"
    override val outerSkinTrigram: Trigram
        get() = Wind
    override val innerHeartTrigram: Trigram
        get() = Earth

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_contemplation_1
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_contemplation_d
        override fun getVictoryLine(): Int = R.string.battlefield_contemplation_v
    }

    private var attackType = -1
    private val moveRange = (1..6)
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            attackType = if (battleField.moveNumber in moveRange) {
                battleField.moveNumber
            } else {
                moveRange.random()
            }
        }

        when (attackType) {
            1 -> {
                if (tickNumber % 2 == 1) {
                    spawnPistol(battleField)
                }
            }
            2 -> {
                if (tickNumber % 3 == 0) {
                    spawnHandcuffs(battleField)
                } else {
                    spawnHandcuffsOnProtagonist(battleField)
                }
            }
            3 -> {
                if (tickNumber % 2 == 1) {
                    spawnPistol(battleField)
                }
                if (tickNumber % 3 == 0) {
                    spawnHandcuffs(battleField)
                }
            }
            4 -> {
                if (tickNumber % 2 == 1) {
                    spawnPistol(battleField)
                    spawnBaton(battleField)
                }
            }
            5 -> {
                if (tickNumber % 3 == 0) {
                    spawnHandcuffs(battleField)
                } else {
                    spawnBaton(battleField)
                }
            }
            6 -> {
                when (tickNumber % 6) {
                    0, 3 -> spawnHandcuffs(battleField)
                    2 -> spawnBaton(battleField)
                    4 -> spawnPistol(battleField)
                }
            }
        }
    }

    private fun spawnPistol(battleField: BattleField65) {
        battleField.addProjectile(Pistol(Coordinates(battleField.protagonist.position.row, battleField.width - 1)))
    }

    private fun spawnHandcuffs(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            battleField.addProjectile(Handcuffs(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
        }
    }
    private fun spawnHandcuffsOnProtagonist(battleField: BattleField65) {
        battleField.addProjectile(Handcuffs(Coordinates(-1, battleField.protagonist.position.col), BattleFieldProjectile.Direction.DOWN))
    }
    private fun spawnBaton(battleField: BattleField65) {
        battleField.addProjectile(Baton(Coordinates(0, battleField.protagonist.position.col)))
    }
}