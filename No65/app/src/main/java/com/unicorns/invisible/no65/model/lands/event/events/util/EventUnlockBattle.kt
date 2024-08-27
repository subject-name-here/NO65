package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.SaveManager


class EventUnlockBattle(indices: List<Int>) : Event({ manager ->
    for (index in indices) {
        SaveManager.unlockBattle(manager.activity, index)
    }
}) {
    constructor(index: Int) : this(listOf(index))
    constructor(vararg indices: Int): this(indices.toList())
}