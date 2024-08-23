package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class BattleFieldJournalEntryProjectile(
    position: Coordinates,
    direction: Direction,
    override var damage: Int,
    private val icon: String,
    private val color: Int
) : BattleFieldProjectile(position, direction) {
    constructor(battleFieldObject: BattleFieldProjectile) : this(
        battleFieldObject.position,
        battleFieldObject.direction,
        battleFieldObject.damage,
        battleFieldObject.getString(),
        battleFieldObject.getStringColor()
    )

    override fun getString(): String = icon
    override fun getStringColor(): Int = color
}