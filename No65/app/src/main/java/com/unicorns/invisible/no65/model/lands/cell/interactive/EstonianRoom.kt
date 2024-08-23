package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import kotlinx.serialization.Serializable

@Serializable
class EstonianRoom(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'ѕ'
    override val symbolColor
        get() = R.color.blue

    private val showMessageEvent
        get() = EventFactory.create { manager ->
            manager.wrapCutsceneSkippable {
                drawer.showMessage(R.string.lands_estonian_room, tapSoundId = R.raw.sfx_ze)
            }
        }

    override fun use(): Event {
        return showMessageEvent
    }
}