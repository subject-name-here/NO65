package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Obstruction
import com.unicorns.invisible.no65.model.lands.event.Event


class EventObstructionIntro(obstruction: Obstruction) : Event({ manager ->
    val lines1 = listOf(
        R.string.lands_obstruction_0_1,
        R.string.lands_obstruction_0_2,
        R.string.lands_obstruction_0_3,
        R.string.lands_obstruction_0_4,
    )
    val lines2 = listOf(
        R.string.lands_obstruction_0_5,
        R.string.lands_obstruction_0_6,
    )

    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(obstruction, lines1)
        obstruction.state = Obstruction.State.NORMAL
        drawer.showCharacterMessages(obstruction, lines2)
    }
})