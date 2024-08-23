package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class TeslaSphere(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = BattleFieldProtagonist.BASIC_HEALTH / 2 - 1
    override val priority: Int = 5
    override fun getString(): String {
        return if (charging) {
            "Ѳ"
        } else {
            "ѱ"
        }
    }
    override fun getStringColor(): Int {
        return if (charging) {
            R.color.energized_yellow
        } else {
            R.color.grey
        }
    }

    var inited = false
    var charging = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        when {
            !inited -> {
                inited = true
            }
            !charging -> {
                val opposingCol = if (position.col == 0) {
                    battleField.width - 1
                } else {
                    0
                }
                val opposingCell = Coordinates(position.row, opposingCol)
                if (battleField.getMap()[opposingCell] is TeslaSphere) {
                    charging = true
                }
            }
            else -> {
                if (position.col == 0) {
                    for (col in (1 until (battleField.width - 1))) {
                        battleField.addProjectile(Lightning(Coordinates(position.row, col)))
                    }
                }
            }
        }

        super.onTick(tickNumber, battleField)
    }
}