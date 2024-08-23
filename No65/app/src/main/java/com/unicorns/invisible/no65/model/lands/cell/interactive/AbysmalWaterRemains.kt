package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import kotlinx.serialization.Serializable

@Serializable
class AbysmalWaterRemains(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'Ӧ'
    override val symbolColor
        get() = R.color.black

    private val event
        get() = EventFactory.create lambda@ { manager ->
            if (manager.gameState !is GameState65) return@lambda
            with (manager.gameState) {
                if (battleMode == BattleMode.ATTACK) {
                    currentMap.removeCellFromTop(this@AbysmalWaterRemains)
                    protagonist.killed++
                }
            }

            val message = if (manager.gameState.battleMode != BattleMode.ATTACK) {
                R.string.lands_abysmal_water_remains_fail
            } else {
                R.string.lands_abysmal_water_remains_destroyed
            }
            manager.wrapCutsceneSkippable {
                drawer.showMessage(message, color = R.color.black, tapSoundId = R.raw.sfx_tap)
            }
        }

    override fun use(): Event {
        return event
    }
}