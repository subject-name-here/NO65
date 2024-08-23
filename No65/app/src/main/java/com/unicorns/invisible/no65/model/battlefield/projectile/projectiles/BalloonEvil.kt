package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt

class BalloonEvil(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.UP
) {
    override val priority: Int
        get() = 3
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "ќ"
    override fun getStringColor(): Int = R.color.almost_black

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        if (position.row <= battleField.height / 2 + 1) {
            when (randInt(5)) {
                0, 3, 4 -> direction = Direction.UP
                1 -> direction = Direction.RIGHT
                2 -> direction = Direction.LEFT
            }
        }
    }
}