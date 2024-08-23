package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.ZProjectile
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldWaiting : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 31
    override val defenceTimeSec: Int = 10

    override val number: Int = 5
    override val nameId: Int
        get() = R.string.battlefield_waiting_name

    override val centerSymbolColorId: Int = R.color.light_blue
    override val centerSymbol: String = "\uD83D\uDCA4"

    override val hexagramSymbol: String = "䷄"
    override val outerSkinTrigram = Water
    override val innerHeartTrigram = Heaven

    override val defaultFace: String = "\uD83D\uDE34"
    override val damageReceivedFace: String = "\uD83D\uDE34"
    override val noDamageReceivedFace: String = "\uD83D\uDE34"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_waiting_1
                2 -> R.string.battlefield_waiting_2
                3 -> R.string.battlefield_waiting_3
                else -> R.string.battlefield_waiting_0
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_waiting_dv
        override fun getVictoryLine(): Int = R.string.battlefield_waiting_dv
    }

    private var lastTripletCell = 0
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val width = battleField.width
        val direction = BattleFieldProjectile.Direction.LEFT

        repeat(3) {
            battleField.addProjectile(ZProjectile(Coordinates(lastTripletCell, width), direction))
            lastTripletCell = (lastTripletCell + 1) % battleField.height
        }
    }
}