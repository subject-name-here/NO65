package com.unicorns.invisible.no65.model.battlefield.fighter

import com.unicorns.invisible.no65.model.battlefield.BattleFieldObject
import com.unicorns.invisible.no65.util.Coordinates


abstract class BattleFieldFighter : BattleFieldObject(Coordinates.ZERO) {
    open val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_MAYBE_HEALTH_MULTIPLIER
    open var health: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_MAYBE_HEALTH_MULTIPLIER

    open fun takeDamage(dp: Int) {
        health -= dp
    }

    companion object {
        const val SLOW_PROJECTILE_SPEED_MILLISECONDS = 1500L
        const val PROJECTILE_SPEED_MILLISECONDS = 900L
        const val FAST_PROJECTILE_SPEED_MILLISECONDS = 750L
        const val SUPER_FAST_PROJECTILE_SPEED_MILLISECONDS = 600L

        const val BASE_DAMAGE_TO_ENEMY = 10

        const val ENEMY_NO_HEALTH_MULTIPLIER = 3
        const val ENEMY_BAD_HEALTH_MULTIPLIER = 5
        const val ENEMY_MAYBE_HEALTH_MULTIPLIER = 9
        const val BOSS_HEALTH_MULTIPLIER = 12
        const val TOUGH_BOSS_HEALTH_MULTIPLIER = 15
    }
}