package com.unicorns.invisible.no65.model.lands.cell.control.sash

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.CellStatic

abstract class Sash: CellStatic() {
    override val symbolColor: Int
        get() = R.color.black
    override val backgroundColor: Int
        get() = R.color.white
}