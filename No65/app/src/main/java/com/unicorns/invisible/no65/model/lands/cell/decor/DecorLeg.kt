package com.unicorns.invisible.no65.model.lands.cell.decor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import kotlinx.serialization.Serializable

@Serializable
class DecorLeg(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = ';'
    override val symbolColor
        get() = R.color.black

    override fun use(): Event {
        return EventFactory.create { manager ->
            if (manager.gameState is GameState65) {
                manager.gameState.currentMap.removeCellFromTop(this@DecorLeg)
                manager.gameState.protagonist.legState = CellProtagonist.LegState.ADDED
                manager.wrapCutsceneSkippable {
                    drawer.showMessage(
                        R.string.lands_leg_applied,
                        color = R.color.black,
                        tapSoundId = R.raw.sfx_tap
                    )
                }
            }
        }
    }
}