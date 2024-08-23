package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.CrazyDiamond
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.RustyChain
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldGrace : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 22
    override val hexagramSymbol: String = "䷕"
    override val nameId: Int
        get() = R.string.battlefield_grace_name

    override val centerSymbolColorId: Int = R.color.light_blue
    override val centerSymbol: String = "\uD83D\uDC8E"

    override val outerSkinTrigram = Mountain
    override val innerHeartTrigram = Fire

    override val defaultFace: String = "\uD83E\uDD34"
    override val damageReceivedFace: String = "\uD83E\uDD34"
    override val noDamageReceivedFace: String = "\uD83E\uDD34"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_grace_1
                2 -> R.string.battlefield_grace_2
                3 -> R.string.battlefield_grace_3
                4 -> R.string.battlefield_grace_4
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_grace_d
        override fun getVictoryLine(): Int = R.string.battlefield_grace_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 3 == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(RustyChain(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
                    }
                }
            }
            2 -> {
                if (tickNumber % 4 == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(RustyChain(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
                        battleField.addProjectile(RustyChain(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
                    }
                }
            }
            else -> {
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
        }
    }
}