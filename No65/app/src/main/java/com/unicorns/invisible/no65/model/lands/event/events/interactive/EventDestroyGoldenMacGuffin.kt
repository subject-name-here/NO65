package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.interactive.ReceptiveEarthMacGuffin
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.util.EventMacGuffinQuest
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventDestroyGoldenMacGuffin(cell: ReceptiveEarthMacGuffin) : Event({ manager ->
    manager.activity.musicPlayer.playMusic(
        R.raw.sfx_break_glass_big,
        behaviour = MusicPlayer.MusicBehaviour.IGNORE,
        isLooping = false
    )
    manager.gameState.currentMap.removeCellFromTop(cell)

    EventMacGuffinQuest().fireEventChain(manager)
})