package com.unicorns.invisible.no65.model.battlefield

import com.unicorns.invisible.no65.util.Coordinates


abstract class BattleFieldObject(var position: Coordinates) {
    abstract fun getString(): String
    abstract fun getStringColor(): Int

    abstract fun onTick(tickNumber: Int, battleField: BattleField)
}