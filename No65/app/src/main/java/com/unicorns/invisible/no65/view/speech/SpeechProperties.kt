package com.unicorns.invisible.no65.view.speech

class SpeechProperties {
    companion object {
        private const val TAP_SOUND_LENGTH = 220L // actually, that's not always true
        private const val SYMBOLS_PER_TAP = 3
        const val MILLISECONDS_PER_SYMBOL = TAP_SOUND_LENGTH / SYMBOLS_PER_TAP
        const val DELAY_AFTER_MESSAGE = 1600L
    }
}