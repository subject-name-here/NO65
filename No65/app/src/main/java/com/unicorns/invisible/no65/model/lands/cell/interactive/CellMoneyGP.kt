package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.util.EventOnAbsorbGPMoney
import kotlinx.serialization.Serializable

@Serializable
class CellMoneyGP(override var cellBelow: Cell): CellMoneyAbstract() {
    override fun use(): Event = super.use().then(EventOnAbsorbGPMoney())
}