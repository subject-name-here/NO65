package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BenzeneRing
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldSplittingApart : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_splitting_apart_name
    override val number: Int
        get() = 23

    override val defaultFace: String
        get() = "\uD83D\uDE41"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE2B"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE41"

    override val centerSymbol: String
        get() = "\uD83D\uDCBE"
    override val centerSymbolColorId: Int
        get() = R.color.blue

    override val hexagramSymbol: String
        get() = "䷖"
    override val outerSkinTrigram: Trigram
        get() = Mountain
    override val innerHeartTrigram: Trigram
        get() = Earth

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_splitting_apart_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_splitting_apart_d
        override fun getVictoryLine(): Int = R.string.battlefield_splitting_apart_v
    }

    private var protagonistCol = -1
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val mid = battleField.width / 2
        if (tickNumber * 2 <= battleField.height) {
            for (i in 0..tickNumber) {
                battleField.addProjectile(WallStreet(Coordinates(i, mid)))
                battleField.addProjectile(WallStreet(Coordinates(battleField.height - 1 - i, mid)))
            }
        }

        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 3 == 0) {
                    addRings(battleField)
                }
            }
            2 -> {
                if (tickNumber % 4 == 0) {
                    addRings(battleField)
                }
                when (tickNumber % 10) {
                    8 -> {
                        protagonistCol = battleField.protagonist.position.col
                        removeRings(battleField, mid)
                    }
                    9 -> {
                        benzeneExplosion(battleField, mid)
                    }
                }
            }
            else -> {
                if (tickNumber % 4 == 0) {
                    addRings(battleField)
                }
                when (tickNumber % 6) {
                    4 -> {
                        protagonistCol = battleField.protagonist.position.col
                        removeRings(battleField, mid)
                    }
                    5 -> {
                        benzeneExplosion(battleField, mid)
                    }
                }
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
}