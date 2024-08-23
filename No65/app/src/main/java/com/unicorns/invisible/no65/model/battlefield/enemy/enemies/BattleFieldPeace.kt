package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.PeaceSymbol
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.PigeonOfPeace
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldPeace : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val nameId: Int
        get() = R.string.battlefield_peace_name
    override val number: Int
        get() = 11

    override val attackTimeMvs: Int
        get() = 31

    override val defaultFace: String
        get() = "\uD83D\uDC3C"
    override val damageReceivedFace: String
        get() = "\uD83D\uDC79"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDC3C"

    override val centerSymbol: String
        get() = "☮"
    override val centerSymbolColorId: Int
        get() = R.color.blue

    override val hexagramSymbol: String
        get() = "䷊"
    override val outerSkinTrigram: Trigram
        get() = Earth
    override val innerHeartTrigram: Trigram
        get() = Heaven

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_peace_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_peace_d
        override fun getVictoryLine(): Int = R.string.battlefield_peace_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % battleField.width == 0) {
                    launchPigeon(battleField)
                }
            }
            2 -> {
                launchTwoPigeons(battleField)
            }
            3 -> {
                if (tickNumber == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(WallStreet(Coordinates(2, it)))
                    }
                }

                launchTwoPigeons(battleField)

                if (tickNumber % 4 == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(PeaceSymbol(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
                    }
                }
            }
            else -> {
                if (tickNumber == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(WallStreet(Coordinates(2, it)))
                    }
                }

                launchTwoPigeons(battleField)

                if (tickNumber % 3 == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(PeaceSymbol(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
                    }
                }
            }
        }
    }

    private fun launchPigeon(battleField: BattleField65) {
        battleField.addProjectile(PigeonOfPeace(Coordinates(0, -1), BattleFieldProjectile.Direction.RIGHT))
    }

    private fun launchTwoPigeons(battleField: BattleField65) {
        val pigeons = battleField.getAllObjects().filterIsInstance<PigeonOfPeace>()
        if (pigeons.size < 2 && randBoolean()) {
            if (randBoolean()) {
                battleField.addProjectile(PigeonOfPeace(Coordinates(0, -1), BattleFieldProjectile.Direction.RIGHT))
            } else {
                battleField.addProjectile(PigeonOfPeace(Coordinates(1, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
        }
    }
}