package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class TheMMBrokenHeart(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    var orderToShoot = false
    private var preparedToShoot = false
    override fun getString() = "Ҭ"
    override fun getStringColor(): Int = if (orderToShoot || preparedToShoot) R.color.red else R.color.dark_grey

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        when {
            preparedToShoot -> {
                battleField.addProjectile(TheMMArrow(Coordinates(position.row + 1, position.col)))
                orderToShoot = false
                preparedToShoot = false
            }
            orderToShoot -> {
                preparedToShoot = true
            }
        }
    }
}