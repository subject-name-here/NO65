package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Stick
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Stone
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldInnerTruth : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 20
    override val defenceTimeSec: Int = 6

    override val number: Int = 61
    override val nameId: Int
        get() = R.string.battlefield_inner_truth_name

    override val centerSymbolColorId: Int = R.color.blue
    override val centerSymbol: String = "\uD83D\uDC41"

    override val hexagramSymbol: String = "䷼"
    override val outerSkinTrigram = Wind
    override val innerHeartTrigram = Lake

    override val defaultFace: String = "\uD83D\uDE14"
    override val damageReceivedFace: String = "\uD83D\uDE2D"
    override val noDamageReceivedFace: String = "\uD83D\uDE03"

    override val beatId: Int
        get() = R.raw.sfx_heartbeat

    private var attackLinesSaid = 0
    private var damageReceived = false
    private var damageReceivedLineShown = false
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int {
            if (damageReceived && !damageReceivedLineShown) {
                damageReceivedLineShown = true
                return R.string.battlefield_inner_truth_on_damage
            }

            return when (attackLinesSaid++) {
                0 -> R.string.battlefield_inner_truth_1
                1 -> R.string.battlefield_inner_truth_2
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_inner_truth_d
        override fun getVictoryLine(): Int = R.string.battlefield_inner_truth_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val direction = BattleFieldProjectile.Direction.DOWN
        val gap = if (battleField.moveNumber == 1) 5 else 4
        if (tickNumber % gap == 0) {
            repeat(battleField.width) {
                if (randBoolean()) {
                    battleField.addProjectile(Stick(Coordinates(-1, it), direction))
                } else {
                    battleField.addProjectile(Stone(Coordinates(-1, it), direction))
                }
            }
        }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        damageReceived = result.damage > 0
    }
}