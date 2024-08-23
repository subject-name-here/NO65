package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class NuclearExplosion(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = BattleFieldProtagonist.BASIC_HEALTH / 3 + 1
    override val priority: Int = 100
    override fun getString(): String = "ӝ"
    override fun getStringColor(): Int = R.color.dark_red
}