package com.unicorns.invisible.no65.util

import kotlin.random.Random


fun <T> choose(vararg args: T): T {
    return args.toList().random()
}

fun <T> Iterable<T>.takeRand(n: Int): List<T> {
    return this.shuffled().take(n)
}

fun randBoolean(): Boolean = Random.nextBoolean()
fun randBooleanPercent(percent: Int): Boolean {
    return Random.nextInt(100) < percent
}

fun randInt(until: Int): Int {
    return Random.nextInt(until)
}
fun randInt(from: Int, until: Int): Int {
    return Random.nextInt(from, until)
}

fun randD(): Int = (-1..1).random()

fun randCoordinatesWithExclusion(width: Int, height: Int, exclusion: Coordinates): Coordinates {
    val numOfExclusion = exclusion.row * width + exclusion.col
    val num = ((0 until width * height) - numOfExclusion).random()
    return Coordinates(num / width, num % width)
}