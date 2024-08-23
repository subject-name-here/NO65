package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Alien(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = 2

    override fun getString(): String = "ҏ"
    override fun getStringColor(): Int = R.color.red

    private var initTick = -1
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        when {
            initTick == -1 -> {
                initTick = tickNumber
            }
            tickNumber == initTick + 1 -> {
                (-1..1).forEach { delta ->
                    (0 until battleField.width - 1).forEach { col ->
                        battleField.addProjectile(
                            RetreatSpaceDistortion(
                                Coordinates(position.row + delta, col),
                                Direction.LEFT
                            )
                        )
                    }
                }
            }
            tickNumber == initTick + 2 -> {
                battleField.removeProjectile(this)
            }
        }
    }
}