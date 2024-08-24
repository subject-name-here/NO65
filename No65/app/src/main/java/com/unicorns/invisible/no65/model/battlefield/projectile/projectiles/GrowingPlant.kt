package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class GrowingPlant(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2 - 1
    override fun getString(): String = "Ҥ"
    override fun getStringColor(): Int = R.color.green

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        position.row %= battleField.height
        position.col %= battleField.width

        when {
            position.row == -1 -> position.row = battleField.height - 1
            position.row == battleField.height -> position.row = 0
            position.col == -1 -> position.col = battleField.width - 1
            position.col == battleField.width -> position.col = 0
        }
    }
}