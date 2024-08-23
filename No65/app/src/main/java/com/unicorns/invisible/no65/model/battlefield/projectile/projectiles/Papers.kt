package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Papers(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.DOWN
) {
    override fun getString(): String = "џ"
    override fun getStringColor(): Int = R.color.light_grey
    override val priority: Int
        get() = 4

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        val projectilesUnderUs = battleField
            .getAllObjects()
            .filterIsInstance<BattleFieldProjectile>()
            .count { it.position.col == position.col && it.position.row > position.row }
        if (position.row == battleField.height - 1 || projectilesUnderUs == battleField.height - position.row - 1) {
            direction = Direction.NO_MOVEMENT
        }
        super.onTick(tickNumber, battleField)
    }
}