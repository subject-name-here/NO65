package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellSemiStatic
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.serialization.Serializable


@Serializable
class GenocideLock(override var cellBelow: Cell): CellSemiStatic() {
    override val symbol: Char
        get() = 'ҋ'
    override val symbolColor
        get() = R.color.dark_grey
    override val backgroundColor
        get() = R.color.almost_white

    override fun isPassable(): Boolean = true

    private var isLaunched = false
    override fun onStep(): Event {
        return EventFactory.create { manager ->
            if (!isLaunched) {
                isLaunched = true
                manager.activity.musicPlayer.playMusic(
                    R.raw.genocider,
                    MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
            }
        }
    }
}