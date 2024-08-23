package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
class Note(override var cellBelow: Cell): CellMessage() {
    override val symbol
        get() = 'ѷ'

    override val symbolColor
        get() = when (state) {
            State.NOT_CLICKED -> R.color.dark_grey
            State.CLICKED -> R.color.blurple
        }

    override var state = State.NOT_CLICKED

    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override var messageNumber: Int = -1
}