package com.unicorns.invisible.no65.model.lands.event

import com.unicorns.invisible.no65.LandsManager


class EventFactory {
    companion object {
        fun create(lambda: suspend Event.(LandsManager) -> Unit): Event {
            return object : Event(lambda) {}
        }

        fun createWithNext(lambda: suspend Event.(LandsManager) -> Event): Event {
            return object : Event({ manager ->
                lambda(manager).fireEventChain(manager)
            }) {}
        }
    }
}