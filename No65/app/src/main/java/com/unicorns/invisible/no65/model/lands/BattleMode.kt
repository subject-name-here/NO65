package com.unicorns.invisible.no65.model.lands

enum class BattleMode {
    FIXED_PEACE,
    PEACE,
    ATTACK;

    fun next(): BattleMode {
        return when(this) {
            FIXED_PEACE -> FIXED_PEACE
            PEACE -> ATTACK
            ATTACK -> PEACE
        }
    }
}