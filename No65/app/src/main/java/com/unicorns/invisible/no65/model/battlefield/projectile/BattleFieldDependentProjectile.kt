package com.unicorns.invisible.no65.model.battlefield.projectile

import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.util.Coordinates


abstract class BattleFieldDependentProjectile(
    initPosition: Coordinates,
    startDirection: Direction
) : BattleFieldProjectile(initPosition, startDirection) {
    final override fun onTick(tickNumber: Int, battleField: BattleField) {}
}