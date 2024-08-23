package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class RustyChain(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "Ӝ"
    override fun getStringColor(): Int = R.color.rust

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        val protagonistRow = battleField.protagonist.position.row
        direction = when {
            protagonistRow > position.row -> Direction.DOWN
            protagonistRow == position.row -> Direction.NO_MOVEMENT
            else -> Direction.UP
        }
    }
}