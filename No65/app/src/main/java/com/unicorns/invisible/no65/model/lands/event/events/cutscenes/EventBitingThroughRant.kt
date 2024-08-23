package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.BITING_THROUGH_FIRED_MADELINE
import com.unicorns.invisible.no65.model.lands.cell.character.npc.BitingThrough
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event


class EventBitingThroughRant(bitingThrough: BitingThrough) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        bitingThrough.emotion = Emotion.HOSTILITY
        drawer.showCharacterMessages(bitingThrough, listOf(R.string.lands_biting_through_jail_rant_1))
        drawer.showCharacterMessages(bitingThrough, listOf(R.string.lands_biting_through_jail_rant_2), delayAfterMessage = 0L)
        bitingThrough.emotion = Emotion.ANGER
        drawer.showCharacterMessages(bitingThrough, listOf(
            R.string.lands_biting_through_jail_rant_3,
            R.string.lands_biting_through_jail_rant_4,
            R.string.lands_biting_through_jail_rant_5,
            R.string.lands_biting_through_jail_rant_6
        ))
        gameState.flagsMaster.add(BITING_THROUGH_FIRED_MADELINE)
    }
})