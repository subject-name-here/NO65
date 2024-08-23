package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.model.lands.cell.interactive.MacGuffinSoundKeeper
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventDestroyMacGuffinSoundKeeper(cell: MacGuffinSoundKeeper, soundId: Int) : Event({ manager ->
    manager.activity.musicPlayer.playMusic(
        soundId,
        behaviour = MusicPlayer.MusicBehaviour.IGNORE,
        isLooping = false
    )
    manager.gameState.currentMap.removeCellFromTop(cell)
})