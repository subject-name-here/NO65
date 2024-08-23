package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class MagnetBot(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.DOWN
) {
    override var damage: Int = super.damage * 2
    private var directionChanged = false
    override val priority: Int
        get() = if (directionChanged) 0 else 1
    override fun getString(): String = "ў"
    override fun getStringColor(): Int = R.color.dark_red

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        val protagonistPos = battleField.protagonist.position
        if (protagonistPos.col == position.col) {
            if (protagonistPos.row < position.row && !directionChanged) {
                directionChanged = true
                changeDirection(Direction.UP)
            }
        }

        super.onTick(tickNumber, battleField)
    }
}