package com.unicorns.invisible.no65.model.elements.trigram

import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65

sealed interface AttackTrigram : Trigram {
    override fun applyAttack(battleField: BattleField65): AttackResult {
        with(battleField) {
            val result = enemy.receiveTrigramDamage(
                this@AttackTrigram,
                protagonist.multiplier,
                protagonist.hasWindRequiem
            )
            protagonist.dropMultiplier()
            if (enemy.areAttacksMirrored) {
                protagonist.takeDamage(result.damage)
            }
            return result
        }
    }
}