package com.unicorns.invisible.no65.model.battlefield.projectile

import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleFieldObject
import com.unicorns.invisible.no65.util.Coordinates


abstract class BattleFieldProjectile(
    initPosition: Coordinates,
    startDirection: Direction
) : BattleFieldObject(initPosition) {

    open var damage = DEFAULT_PROJECTILE_DAMAGE
    open val priority = 1

    var direction = startDirection
        protected set

    private var newDirection: Direction? = null
    fun changeDirection(newDirection: Direction) {
        this.newDirection = newDirection
    }

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        val delta = direction.getDelta()
        position += delta

        direction = newDirection ?: direction
        newDirection = null
    }

    enum class Direction {
        UP {
            override fun getString() = "^"
        },
        DOWN {
            override fun getString() = "|"
        },
        LEFT {
            override fun getString() = "<"
        },
        RIGHT {
            override fun getString() = ">"
        },
        NO_MOVEMENT {
            override fun getString() = ""
        };

        abstract fun getString(): String

        fun getDelta(): Coordinates {
            return when (this) {
                UP -> Coordinates(-1, 0)
                DOWN -> Coordinates(1, 0)
                LEFT -> Coordinates(0, -1)
                RIGHT -> Coordinates(0, 1)
                NO_MOVEMENT -> Coordinates(0, 0)
            }
        }
    }

    companion object {
        const val DEFAULT_PROJECTILE_DAMAGE = 5
    }
}