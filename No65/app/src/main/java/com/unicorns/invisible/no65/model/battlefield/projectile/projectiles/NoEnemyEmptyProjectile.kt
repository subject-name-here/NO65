package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class NoEnemyEmptyProjectile(
    position: Coordinates,
    private var realDirection: Direction
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = 2
    override fun getString(): String = " "
    override fun getStringColor(): Int = R.color.no_color

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        position += realDirection.getDelta()
    }
}