package com.unicorns.invisible.no65.model.elements.trigram

import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import kotlinx.serialization.Serializable

@Serializable
object Heaven : Trigram {
    override fun applyAttack(battleField: BattleField65): AttackResult {
        return if (battleField.enemy.timebackDenied) {
            AttackResult(0, AttackResult.DamageType.TIMEBACK_DENIED)
        } else {
            val health = battleField.protagonist.health
            battleField.timeback()
            AttackResult(health, AttackResult.DamageType.DEFENSIVE_ATTACK)
        }
    }
}