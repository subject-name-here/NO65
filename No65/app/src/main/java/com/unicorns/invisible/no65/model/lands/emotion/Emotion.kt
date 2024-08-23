package com.unicorns.invisible.no65.model.lands.emotion

import com.unicorns.invisible.no65.R

enum class Emotion {
    ANGER,
    HOSTILITY,
    ARROGANCE,
    CALMNESS,
    INDIFFERENCE,
    SMALL_INTEREST,
    FEAR,
    FRIENDLINESS,
    JOY,
    ENERGIZED,
    HYPOCRISY,
    BRAVERY,
    LAW,
    WHISPER,
    SILENCE,
    ;

    fun getColor(): Int {
        return when (this) {
            ANGER -> R.color.red
            HOSTILITY -> R.color.dark_red
            ARROGANCE -> R.color.purple
            CALMNESS -> R.color.pink
            INDIFFERENCE -> R.color.dark_grey
            SMALL_INTEREST -> R.color.brown
            FEAR -> R.color.orange
            FRIENDLINESS -> R.color.green
            JOY -> R.color.yellow
            ENERGIZED -> R.color.energized_yellow
            HYPOCRISY -> R.color.true_green
            BRAVERY -> R.color.blue
            LAW -> R.color.dark_blue
            WHISPER -> R.color.light_grey
            SILENCE -> R.color.white
        }
    }
}