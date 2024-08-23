package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class TheMMBrokenHeartSource(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override fun getString() = "Ҭ"
    override fun getStringColor(): Int = R.color.red
    override val priority: Int = 10

    private var ready = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        if (!ready) {
            ready = true
            return
        }

        battleField.removeProjectile(this)

        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(-1, 0), Direction.UP))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(-1 ,1), Direction.UP))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(1, 0), Direction.DOWN))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(1, -1), Direction.DOWN))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(-1, -1), Direction.LEFT))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(0, -1), Direction.LEFT))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(0, 1), Direction.RIGHT))
        battleField.addProjectile(TheMMBrokenHeart(position + Coordinates(1, 1), Direction.RIGHT))
    }
}