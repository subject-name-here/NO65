package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Stone
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.FOUGHT_WITH_ITT
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBooleanPercent


class BattleFieldInnerTruthTainted : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 25
    override val defenceTimeSec: Int = 5

    override val number: Int = 66
    override val nameId: Int
        get() = R.string.battlefield_inner_truth_tainted_name

    override val goText: String = "0"

    override val centerSymbolColorId: Int = R.color.black
    override val centerSymbol: String = "\uD83D\uDC41"
    override val legsSymbol: String = "~"

    override val hexagramSymbol: String = "?"
    override val outerSkinTrigram = Wind
    override val innerHeartTrigram = Lake

    override val animation: CharacterAnimation = CharacterAnimation.FLOAT

    override val musicThemeId: Int = R.raw.battle_inner_truth_tainted
    override val beatId: Int = R.raw.sfx_itt

    override val defaultFace: String = "\uD83C\uDF1A"
    override val damageReceivedFace: String = "\uD83C\uDF1A"
    override val noDamageReceivedFace: String = "\uD83C\uDF1A"

    private var threatLineGenerated = false
    private var damageReceived = false
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int {
            if (!threatLineGenerated && damageReceived) {
                threatLineGenerated = true
                return R.string.battlefield_inner_truth_tainted_on_damage
            }

            return when (moveNumber) {
                1 -> R.string.battlefield_inner_truth_tainted_1
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_inner_truth_tainted_d
        override fun getVictoryLine(): Int = R.string.empty_line
    }

    override suspend fun onBattleBegins(manager: BattleManager) {
        GlobalState.putBoolean(manager.activity, FOUGHT_WITH_ITT, true)
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val direction = BattleFieldProjectile.Direction.RIGHT
        repeat(battleField.height) {
            if (randBooleanPercent(42)) {
                battleField.addProjectile(Stone(Coordinates(it, -1), direction))
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