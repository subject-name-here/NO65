package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Arrow15(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = 15
    override val priority: Int
        get() = if (isLoaded) 3 else 2
    override fun getString(): String = "Ҕ"
    override fun getStringColor(): Int = R.color.green

    var isLoaded = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        if (!isLoaded) {
            isLoaded = true
        } else {
            battleField.removeProjectile(this)
            repeat(battleField.height) { row ->
                battleField.addProjectile(Arrow1(Coordinates(row, position.col)))
            }
        }
    }
}