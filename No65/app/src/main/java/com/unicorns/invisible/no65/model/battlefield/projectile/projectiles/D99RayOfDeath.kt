package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class D99RayOfDeath(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    private var fired = false
    override var damage: Int = 2 * DEFAULT_PROJECTILE_DAMAGE
    override val priority: Int
        get() = if (fired) 0 else 100
    override fun getString(): String = "Ұ"
    override fun getStringColor(): Int = R.color.red

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        fired = true
    }
}