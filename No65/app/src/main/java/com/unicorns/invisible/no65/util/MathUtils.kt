package com.unicorns.invisible.no65.util


fun getNthBit(num: Int, n: Int): Int {
    return (num shr n) and 1
}

fun getShiftedProgressPercentage(numberOfInvocation: Int, numOfCalls: Int): Int {
    return (100f * (numberOfInvocation + 1) / numOfCalls).toInt()
}

fun Int.isCloseTo(n: Int, delta: Int): Boolean {
    return n - delta <= this && this <= n + delta
}