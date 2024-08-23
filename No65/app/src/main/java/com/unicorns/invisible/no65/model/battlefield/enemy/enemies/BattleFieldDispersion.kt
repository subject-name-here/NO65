package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Flower
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates

class BattleFieldDispersion(val isIndifferent: Boolean) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_dispersion_name
    override val number: Int
        get() = 59

    override val attackTimeMvs: Int
        get() = if (isIndifferent) 16 else 30
    override val tickTime: Long = if (isIndifferent) {
        SLOW_PROJECTILE_SPEED_MILLISECONDS
    } else {
        super.tickTime
    }
    override var musicThemeId: Int = if (isIndifferent) {
        R.raw.battle_enemy_slow
    } else {
        super.musicThemeId
    }

    override val defaultFace: String
        get() = "\uD83C\uDF83"
    override val damageReceivedFace: String
        get() = "\uD83E\uDD67"
    override val noDamageReceivedFace: String
        get() = "\uD83C\uDF83"

    override val centerSymbol: String
        get() = "\uD83C\uDFF6"
    override val centerSymbolColorId: Int
        get() = R.color.true_yellow

    override val hexagramSymbol: String
        get() = "䷺"
    override val outerSkinTrigram: Trigram
        get() = Wind
    override val innerHeartTrigram: Trigram
        get() = Water

    override val lineGenerator: BattleFieldLineGenerator
        get() = object : BattleFieldLineGenerator {
            override fun getLine(moveNumber: Int): Int = if (isIndifferent) {
                R.string.battlefield_dispersion_ice_cream
            } else {
                when (moveNumber) {
                    1 -> R.string.battlefield_dispersion_1
                    2 -> R.string.battlefield_dispersion_2
                    3 -> R.string.battlefield_dispersion_3
                    4 -> R.string.battlefield_dispersion_4
                    else -> R.string.empty_line
                }
            }
            override fun getDefeatedLine(): Int = if (isIndifferent) {
                R.string.battlefield_dispersion_ice_cream
            } else {
                R.string.battlefield_dispersion_d
            }
            override fun getVictoryLine(): Int = if (isIndifferent) {
                R.string.battlefield_dispersion_ice_cream
            } else {
                R.string.battlefield_dispersion_v
            }
        }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
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
}