package com.unicorns.invisible.no65.model.battlefield.enemy

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.elements.trigram.*


// This class fights exclusively against BattleFieldProtagonist.
abstract class BattleFieldEnemy : BattleFieldCharacter() {
    open val defenceTimeSec: Int = 5

    abstract val outerSkinTrigram: Trigram
    abstract val innerHeartTrigram: Trigram

    abstract val lineGenerator: BattleFieldLineGenerator

    final override fun getString(): String = ""
    final override fun getStringColor(): Int = R.color.no_color

    final override fun onTick(tickNumber: Int, battleField: BattleField) {
        onTick(battleField as BattleField65, tickNumber)
    }
    abstract fun onTick(battleField: BattleField65, tickNumber: Int)

    fun receiveTrigramDamage(trigram: AttackTrigram, multiplier: Int, isWindRequiem: Boolean): AttackResult {
        if (evadesSimpleAttacks && multiplier == 1) {
            return AttackResult(0, AttackResult.DamageType.EVADED)
        }

        return when {
            isWindRequiem && isRequiemProof -> AttackResult(0, AttackResult.DamageType.WIND_REQUIEM_DEFLECTED)
            isWindRequiem -> {
                val damage = health
                health = 0
                AttackResult(damage, AttackResult.DamageType.WIND_REQUIEM)
            }
            isHealthInfinite -> AttackResult(0, AttackResult.DamageType.HIT_INFINITY)
            else -> {
                val dmg = receiveTrigramDamage(trigram, multiplier)
                if (dmg > 0) {
                    AttackResult(dmg, AttackResult.DamageType.HIT_SUCCESSFUL)
                } else {
                    AttackResult(dmg, AttackResult.DamageType.HIT_UNSUCCESSFUL)
                }.also { health -= dmg }
            }
        }
    }

    protected open fun receiveTrigramDamage(trigram: AttackTrigram, multiplier: Int): Int {
        return when (trigram) {
            innerHeartTrigram -> 0
            outerSkinTrigram -> BASE_DAMAGE_TO_ENEMY * 3
            else -> when (trigram) {
                is Earth -> BASE_DAMAGE_TO_ENEMY * 2
                is Water, is Mountain, is Thunder -> BASE_DAMAGE_TO_ENEMY
            }
        } * multiplier
    }

    open suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {}
}