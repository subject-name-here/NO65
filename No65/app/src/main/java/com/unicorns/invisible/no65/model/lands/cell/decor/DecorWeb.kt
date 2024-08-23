package com.unicorns.invisible.no65.model.lands.cell.decor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class DecorWeb(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'Ѻ'
    override val symbolColor
        get() = R.color.light_grey

    @Transient
    private var isUsed = false
    override fun use(): Event {
        if (isUsed) {
            return Event.Null
        }

        isUsed = true
        return EventFactory.create { manager ->
            manager.activity.musicPlayer.playMusicSuspendTillEnd(
                R.raw.sfx_web_through,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = false
            )
            manager.gameState.currentMap.removeCellFromTop(this@DecorWeb)
        }
    }
}