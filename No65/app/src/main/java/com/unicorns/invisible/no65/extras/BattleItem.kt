package com.unicorns.invisible.no65.extras

import com.quickbirdstudios.nonEmptyCollection.list.NonEmptyList
import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.knowledge.Knowledge

class BattleItem(
    val nameId: Int,
    val enemiesPool: NonEmptyList<BattleFieldEnemy>,
    val knowledge: Knowledge,
    val managerMode: BattleManager.Mode = BattleManager.Mode.STANDARD,
) {
    constructor(
        nameId: Int,
        enemy: BattleFieldEnemy,
        knowledge: Knowledge,
        managerMode: BattleManager.Mode = BattleManager.Mode.STANDARD,
    ) : this(nameId, nonEmptyListOf(enemy), knowledge, managerMode)
}