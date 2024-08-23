package com.unicorns.invisible.no65

enum class Gradation {
    YES,
    GOOD,
    MAYBE,
    BAD,
    NO;

    fun getFloatValue(): Float {
        return when (this) {
            YES -> 1f
            GOOD -> 0.75f
            MAYBE -> 0.5f
            BAD -> 0.25f
            NO -> 0f
        }
    }

    fun next(): Gradation {
        return when (this) {
            YES -> GOOD
            GOOD -> MAYBE
            MAYBE -> BAD
            BAD -> NO
            NO -> YES
        }
    }
}