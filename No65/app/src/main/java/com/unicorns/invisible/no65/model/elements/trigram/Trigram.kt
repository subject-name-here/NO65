package com.unicorns.invisible.no65.model.elements.trigram

import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65


sealed interface Trigram {
    fun applyAttack(battleField: BattleField65): AttackResult
    fun applyAttackWithRequiem(battleField: BattleField65) = applyAttack(battleField)

    fun getMonograms() = TrigramMonogramsBijection.getMonograms(this)
}