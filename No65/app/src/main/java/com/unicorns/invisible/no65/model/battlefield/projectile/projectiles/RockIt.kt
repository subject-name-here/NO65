package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt

class RockIt(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.UP
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 4
    override fun getString(): String = "ҿ"
    override fun getStringColor(): Int = R.color.almost_black

    private var timeToExplode = 0
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        if (timeToExplode == 0) {
            timeToExplode = randInt(battleField.height - 1)
        } else {
            timeToExplode--
            if (timeToExplode == 0) {
                val row = position.row
                repeat(battleField.width) {
                    if (it != position.col) {
                        battleField.addProjectile(Explosion(Coordinates(row, it)))
                    }
                }
            }
        }
    }
}