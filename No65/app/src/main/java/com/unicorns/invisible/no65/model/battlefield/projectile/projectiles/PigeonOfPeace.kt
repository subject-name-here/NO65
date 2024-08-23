package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class PigeonOfPeace(
    position: Coordinates,
    direction: Direction,
    private val isDeadly: Boolean = false
) : BattleFieldProjectile(
    position, direction
) {
    override val priority: Int = 3
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "ҍ"
    override fun getStringColor(): Int = R.color.light_grey

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        bomb(battleField)
    }

    private fun bomb(battleField: BattleField) {
        if (position.col == battleField.protagonist.position.col) {
            for (r in 0 until battleField.height) {
                val projectile = if (isDeadly) {
                    PeaceSymbolDeadly(Coordinates(r, position.col), Direction.UP)
                } else {
                    PeaceSymbol(Coordinates(r, position.col), Direction.UP)
                }
                battleField.addProjectile(projectile)
            }
        }
    }
}