package com.unicorns.invisible.no65.model.lands.cell.control.sash

import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.Serializable

@Serializable
class SashLeft(override var cellBelow: Cell): Sash() {
    override val symbol: Char
        get() = '{'
}