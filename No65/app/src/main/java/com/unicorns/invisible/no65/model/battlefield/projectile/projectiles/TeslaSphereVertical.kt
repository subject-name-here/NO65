package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class TeslaSphereVertical(
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

    var charging = false
    private var charged = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        when {
            charging && !charged -> {
                charged = true
            }
            charging && charged -> {
                (1 until battleField.height).forEach {
                    battleField.addProjectile(Lightning(Coordinates(it, position.col)))
                }
                charged = false
                charging = false
            }
        }
    }
}