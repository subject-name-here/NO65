package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Banana
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldSmallTaming : BattleFieldEnemy() {
    override val maxHealth: Int
        get() = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 9
    override val nameId: Int
        get() = R.string.battlefield_small_taming_name

    override val centerSymbolColorId: Int = R.color.yellow
    override val centerSymbol: String = "\uD83C\uDF4C"

    override val hexagramSymbol: String = "䷈"
    override val outerSkinTrigram = Wind
    override val innerHeartTrigram = Heaven

    override var defaultFace: String = "\uD83D\uDE49"
    override val damageReceivedFace: String = "\uD83D\uDE48"
    override val noDamageReceivedFace: String = "\uD83D\uDE4A"

    override val animation: CharacterAnimation = CharacterAnimation.ROTATE_FULL

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_small_taming_1
            2 -> R.string.battlefield_small_taming_2
            3 -> R.string.battlefield_small_taming_3
            4 -> R.string.battlefield_small_taming_4
            else -> R.string.battlefield_small_taming_0
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_small_taming_d
        override fun getVictoryLine(): Int = R.string.battlefield_small_taming_v
    }

    private val shifts = arrayListOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % battleField.width == 0) {
            shifts.clear()
            shifts.addAll(0 until battleField.width)
            shifts.shuffle()
        }
        battleField.addProjectile(Banana(Coordinates(-1, shifts[tickNumber % battleField.width]), BattleFieldProjectile.Direction.DOWN))

        if (tickNumber % 3 == 0) {
            repeat(2) {
                val row = randInt(battleField.height)
                battleField.addProjectile(Banana(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
        }
    }
}