package com.unicorns.invisible.no65.model.lands

enum class MoveMode {
    WALK,
    RUN,
    FIXED_WALK;

    fun next(): MoveMode {
        return when (this) {
            WALK -> RUN
            RUN -> WALK
            FIXED_WALK -> FIXED_WALK
        }
    }
}