package com.unicorns.invisible.no65.util

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.sign


@Serializable
data class Coordinates(var row: Int, var col: Int) {
    operator fun plus(coordinates: Coordinates): Coordinates {
        return Coordinates(this.row + coordinates.row, this.col + coordinates.col)
    }

    operator fun minus(coordinates: Coordinates): Coordinates {
        return Coordinates(this.row - coordinates.row, this.col - coordinates.col)
    }

    operator fun unaryMinus(): Coordinates {
        return Coordinates(-this.row, -this.col)
    }

    operator fun times(m: Int): Coordinates {
        return Coordinates(row * m, col * m)
    }

    operator fun div(q: Int): Coordinates {
        return Coordinates(row / q, col / q)
    }

    fun sign(): Coordinates {
        return Coordinates(row.sign, col.sign)
    }

    fun abs(): Int {
        return abs(row) + abs(col)
    }

    companion object {
        val ZERO = Coordinates(0, 0)
    }
}
