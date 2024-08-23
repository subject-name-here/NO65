package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowPOIMessageMultiline
import com.unicorns.invisible.no65.view.lands.NumberToStringId
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
class PointOfInterest(override var cellBelow: Cell): CellMessage() {
    override val symbol
        get() = '!'

    override val symbolColor
        get() = when (state) {
            State.NOT_CLICKED -> R.color.true_yellow
            State.CLICKED -> R.color.blurple
        }

    override fun use(): Event {
        state = State.CLICKED
        return EventShowPOIMessageMultiline(NumberToStringId.getMessage(messageNumber))
    }

    override var state = State.NOT_CLICKED

    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override var messageNumber: Int = -1
}