package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class RevolutionExplodingBomb(
    position: Coordinates,
    direction: Direction = Direction.NO_MOVEMENT
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 7
    override val priority: Int = 7
    override fun getString(): String = "Ҁ"

    private var state = State.CALM
    override fun getStringColor(): Int = when (state) {
        State.CALM -> R.color.almost_black
        State.BOMBY_2, State.BOMBY_1 -> R.color.dark_red
    }

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        when (state) {
            State.CALM -> {}
            State.BOMBY_2 -> {
                state = State.BOMBY_1
            }
            State.BOMBY_1 -> {
                (-1..1).forEach { rowDelta ->
                    (-1..1).forEach { colDelta ->
                        battleField.addProjectile(Explosion(position + Coordinates(rowDelta, colDelta)))
                    }
                }
                state = State.CALM
            }
        }
    }

    fun setToExplode() {
        if (state == State.CALM) {
            state = State.BOMBY_2
        }
    }
    fun isCalm() = state == State.CALM

    enum class State {
        CALM,
        BOMBY_2,
        BOMBY_1,
    }
}