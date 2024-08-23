package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class NuclearRocket(
    position: Coordinates,
    private val radius: Int
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = BattleFieldProtagonist.BASIC_HEALTH - 1
    override fun getString(): String = "ӟ"
    override fun getStringColor(): Int = R.color.black

    private var landed = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        if (!landed) {
            landed = true
        } else {
            battleField.removeProjectile(this)
            for (row in ((position.row - radius)..(position.row + radius))) {
                for (col in ((position.col - radius)..(position.col + radius))) {
                    battleField.addProjectile(NuclearExplosion(Coordinates(row, col)))
                }
            }
        }
    }
}