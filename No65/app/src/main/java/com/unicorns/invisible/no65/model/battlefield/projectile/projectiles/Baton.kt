package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Baton(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override fun getString(): String = "ҙ"
    override fun getStringColor(): Int = R.color.almost_black

    override val priority: Int
        get() = if (isLoaded) 3 else 2

    private var isLoaded = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        if (!isLoaded) {
            isLoaded = true
        } else {
            battleField.removeProjectile(this)
            (1 until battleField.height).forEach { row ->
                battleField.addProjectile(SpaceDistortion(Coordinates(row, position.col), Direction.DOWN))
            }
        }
    }
}