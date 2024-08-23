package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ReceptiveEarth
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventDestroyGoldenMacGuffin
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import kotlinx.serialization.Serializable


@Serializable
class ReceptiveEarthMacGuffin(override var cellBelow: Cell): MacGuffin() {
    override val symbolColor
        get() = R.color.true_yellow

    override val symbol: Char
        get() = when (triggerState) {
            MacGuffinState.WHOLE, MacGuffinState.TOUCHED, MacGuffinState.ALMOST_BROKEN -> 'һ'
            MacGuffinState.BROKEN -> 'ӛ'
            MacGuffinState.NON_EXISTENT -> ' '
        }

    override fun use(): Event = EventFactory.createWithNext lambda@ { manager ->
        triggerState = triggerState.next()

        val reCells = manager.gameState.currentMap.getTopCells().filterIsInstance<ReceptiveEarth>()
        processEvent(reCells.getOrNull(0))
    }

    override val destroyEvent: Event
        get() = EventDestroyGoldenMacGuffin(this)

    private fun processEvent(cell: ReceptiveEarth?): Event {
        return when (triggerState) {
            MacGuffinState.WHOLE -> { Event.Null }
            MacGuffinState.TOUCHED -> {
                if (cell != null) {
                    EventNPCSpeakCutscene(cell) {
                        listOf(R.string.lands_receptive_earth_macguffin_touched_1)
                    }
                } else {
                    Event.Null
                }
            }
            MacGuffinState.ALMOST_BROKEN -> {
                if (cell != null) {
                    EventNPCSpeakCutscene(cell) {
                        listOf(R.string.lands_receptive_earth_macguffin_almost_broken_1)
                    }
                } else {
                    Event.Null
                }
            }
            MacGuffinState.BROKEN -> {
                cell?.attackSequence ?: Event.Null
            }
            MacGuffinState.NON_EXISTENT -> {
                destroyEvent
            }
        }
    }
}