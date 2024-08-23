package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Explosion(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "Ӏ"
    override fun getStringColor(): Int = R.color.red

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        battleField.removeProjectile(this)
    }
}