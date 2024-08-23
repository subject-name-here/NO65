package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose
import kotlin.math.absoluteValue


class NoEnemyNumber(
    position: Coordinates,
    private var realDirection: Direction,
    private val width: Int,
    private val height: Int,
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = 1
    override val priority: Int = 2
    override fun getString(): String {
        val trueNumber = position.row * width + position.col
        val delta = choose(-3, -2, -2, -1, -1, -1, 1, 1, 1, 2, 2, 3)
        var fakeNumber = (trueNumber + delta).coerceIn(0, width * height - 1)
        if (fakeNumber == trueNumber) {
            fakeNumber = if (fakeNumber < 5) {
                trueNumber + delta.absoluteValue
            } else {
                trueNumber - delta.absoluteValue
            }
        }
        return fakeNumber.toString()
    }
    override fun getStringColor(): Int = R.color.light_grey

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        position += realDirection.getDelta()
    }
}