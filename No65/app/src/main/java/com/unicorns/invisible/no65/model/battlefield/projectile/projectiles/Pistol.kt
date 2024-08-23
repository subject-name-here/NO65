package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Pistol(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = BattleFieldProtagonist.BASIC_HEALTH / 2 + 1
    override val priority: Int
        get() = if (isLoaded) 3 else 2
    override fun getString(): String = "Җ"
    override fun getStringColor(): Int = R.color.almost_black

    private var isLoaded = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        if (!isLoaded) {
            isLoaded = true
        } else {
            battleField.removeProjectile(this)
            repeat(battleField.width - 1) { col ->
                battleField.addProjectile(Bullet(Coordinates(position.row, col)))
            }
        }
    }
}