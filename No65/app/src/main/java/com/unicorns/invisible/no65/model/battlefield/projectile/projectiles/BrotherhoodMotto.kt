package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class BrotherhoodMotto(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override fun getString(): String = "ӌ"
    override fun getStringColor(): Int = R.color.dark_red
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * "TOGETHER".length
    override val priority: Int = 5
}