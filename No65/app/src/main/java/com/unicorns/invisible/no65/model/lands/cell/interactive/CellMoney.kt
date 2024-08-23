package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.Serializable

@Serializable
class CellMoney(override var cellBelow: Cell): CellMoneyAbstract()