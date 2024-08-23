package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassable
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.serialization.Serializable


@Serializable
abstract class SkillBook: CellStaticOnPassable(), CellUsable {
    protected abstract val acquiredEvent: Event

    final override fun use(): Event {
        return acquiredEvent
    }
}