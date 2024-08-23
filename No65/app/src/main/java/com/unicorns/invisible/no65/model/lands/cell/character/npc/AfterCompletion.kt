package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ENTERED_FINALE
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventAfterCompletion
import kotlinx.serialization.Serializable

@Serializable
class AfterCompletion(override var cellBelow: Cell): CellNPC() {
    override val nameId: Int
        get() = R.string.lands_after_completion_name
    override val id: Int
        get() = 63
    override val faceCell: String
        get() = "\uD83D\uDC74"
    override val centerSymbol: String
        get() = "\uD83D\uDD1A"
    override val centerSymbolColor: Int
        get() = R.color.black
    override val emotion: Emotion
        get() = Emotion.LAW

    var state = State.SAVE_MODE

    override fun use(): Event {
        return Event.Null
    }

    private var cutsceneStarted = false
    override fun onSight(distanceToProtagonist: Int): Event {
        return EventFactory.createWithNext { manager ->
            if (
                manager.gameState.flagsMaster.contains(ENTERED_FINALE)
                && state == State.READY_FOR_CUTSCENE
                && !cutsceneStarted
            ) {
                cutsceneStarted = true
                EventAfterCompletion(this@AfterCompletion)
            } else {
                Event.Null
            }
        }
    }

    enum class State {
        SAVE_MODE,
        READY_FOR_CUTSCENE
    }
}