package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable

@Serializable
class CellEmpty(override val coordinates: Coordinates): Cell() {
    override val symbol
        get() = '+'
    override val symbolColor
        get() = R.color.black
    override val backgroundColor
        get() = R.color.black
}