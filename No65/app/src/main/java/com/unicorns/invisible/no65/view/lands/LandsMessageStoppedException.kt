package com.unicorns.invisible.no65.view.lands

import java.util.concurrent.CancellationException

class LandsMessageStoppedException(val reason: Reason) : CancellationException() {
    enum class Reason {
        OVERRIDDEN_BY_ANOTHER_MESSAGE,
        SKIPPED,
        DRAWER_HALTED
    }
}