package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MET_WITH_CF
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ClingingFire
import com.unicorns.invisible.no65.model.lands.event.Event


class EventClingingFireMeet(
    private val characterCell: ClingingFire,
): Event({ manager ->
    manager.wrapCutscene {
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_clinging_fire_meet_1,
            R.string.lands_clinging_fire_meet_2,
            R.string.lands_clinging_fire_meet_3,
            R.string.lands_clinging_fire_meet_4,
            R.string.lands_clinging_fire_meet_5,
            R.string.lands_clinging_fire_meet_6,
            R.string.lands_clinging_fire_meet_7,
            R.string.lands_clinging_fire_meet_8,
            R.string.lands_clinging_fire_meet_9,
        ))
        gameState.flagsMaster.add(MET_WITH_CF)
    }
})