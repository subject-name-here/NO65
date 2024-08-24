package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class CrazyDiamond(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 3
    override fun getString(): String = "Ј"
    override fun getStringColor(): Int = R.color.light_blue
    override val priority: Int = 2

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        direction = Direction.entries.random()
    }
}