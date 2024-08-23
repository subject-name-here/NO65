package com.unicorns.invisible.no65.model.battlefield

data class AttackResult(val damage: Int, val type: DamageType) {
    enum class DamageType {
        DEFENSIVE_ATTACK,
        HIT_SUCCESSFUL,
        HIT_UNSUCCESSFUL,
        EVADED,
        WIND_REQUIEM,
        WIND_REQUIEM_DEFLECTED,
        HIT_INFINITY,
        TIMEBACK_DENIED,
        CASTED_UNKNOWN_TRIGRAM,
        NOT_CASTED;

        fun isAttack(): Boolean {
            return this != DEFENSIVE_ATTACK &&
                    this != NOT_CASTED &&
                    this != TIMEBACK_DENIED &&
                    this != CASTED_UNKNOWN_TRIGRAM
        }
    }
}