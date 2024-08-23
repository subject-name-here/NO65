package com.unicorns.invisible.no65.model.lands.cell.interactive

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
class RedBull(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'њ'
    override val symbolColor
        get() = R.color.blue

    private val useEvent
        get() = EventFactory.create { manager ->
            if (manager.gameState !is GameState65) return@create

            manager.gameState.currentMap.removeCellFromTop(this@RedBull)
            manager.gameState.protagonist.redBullState = CellProtagonist.RedBullState.USED
            manager.wrapCutsceneSkippable {
                drawer.showMessage(
                    R.string.lands_red_bull,
                    color = R.color.blue,
                    tapSoundId = R.raw.sfx_ze
                )
            }
        }

    override fun use(): Event {
        return useEvent
    }
}