package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.CellNonStaticCharacter
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.serialization.Serializable


@Serializable
abstract class CellNPC : CellNonStaticCharacter(), CellUsable {
    abstract val nameId: Int

    open fun onSight(distanceToProtagonist: Int): Event { return Event.Null }
}