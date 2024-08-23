package com.unicorns.invisible.no65.model.elements.trigram

import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import kotlinx.serialization.Serializable

@Serializable
object Lake : Trigram {
    override fun applyAttack(battleField: BattleField65): AttackResult {
        battleField.protagonist.heal()
        return AttackResult(0, AttackResult.DamageType.DEFENSIVE_ATTACK)
    }
    override fun applyAttackWithRequiem(battleField: BattleField65): AttackResult {
        battleField.protagonist.healRequiem()
        return AttackResult(0, AttackResult.DamageType.DEFENSIVE_ATTACK)
    }
}