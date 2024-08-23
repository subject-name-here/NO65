package com.unicorns.invisible.no65.model.lands.event

import com.unicorns.invisible.no65.LandsManager


abstract class Event(protected open val thisEventLambda: suspend Event.(LandsManager) -> Unit) {
    protected constructor(): this({})

    private val nextEventList: MutableList<Event> = mutableListOf()

    fun then(event: Event?): Event {
        if (event != null) {
            nextEventList.add(event)
        }
        return this
    }

    suspend fun fireEventChain(manager: LandsManager) {
        thisEventLambda(manager)

        for (event in nextEventList) {
            event.fireEventChain(manager)
        }
    }

    companion object {
        val Null
            get() = EventFactory.create {}
    }
}
