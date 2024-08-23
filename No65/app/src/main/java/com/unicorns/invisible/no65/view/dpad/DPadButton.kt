package com.unicorns.invisible.no65.view.dpad

enum class DPadButton {
    BOTTOM_BUTTON,
    LEFT_BUTTON,
    TOP_BUTTON,
    RIGHT_BUTTON
    ;

    fun number(): Int {
        return when (this) {
            BOTTOM_BUTTON -> 0
            LEFT_BUTTON -> 1
            TOP_BUTTON -> 2
            RIGHT_BUTTON -> 3
        }
    }

    companion object {
        fun getByNumber(index: Int): DPadButton {
            return when (index) {
                1 -> LEFT_BUTTON
                2 -> TOP_BUTTON
                3 -> RIGHT_BUTTON
                else -> BOTTOM_BUTTON
            }
        }
    }
}