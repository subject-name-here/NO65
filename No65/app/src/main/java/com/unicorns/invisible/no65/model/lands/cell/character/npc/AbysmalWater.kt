package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event


class AbysmalWater(override var cellBelow: Cell) : CellNPCStandard() {
    override val nameId: Int
        get() = R.string.lands_abysmal_water_name
    override val id: Int
        get() = 29

    override val faceCell: String
        get() = "\uD83D\uDC19"
    override val centerSymbol: String
        get() = "⛲"
    override val centerSymbolColor: Int
        get() = R.color.light_blue

    var state = State.HONEST
    override val emotion: Emotion
        get() = when (state) {
            State.HONEST -> Emotion.LAW
            State.BRAVE -> Emotion.BRAVERY
        }

    override val attackEvent: Event
        get() = Event.Null
    override val speakEvent: Event
        get() = Event.Null

    enum class State {
        HONEST,
        BRAVE
    }
}