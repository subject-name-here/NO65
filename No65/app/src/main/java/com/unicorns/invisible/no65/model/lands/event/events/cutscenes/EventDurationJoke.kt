package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Duration
import com.unicorns.invisible.no65.model.lands.event.Event


class EventDurationJoke(duration: Duration) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_duration_joke_1,
            R.string.lands_duration_joke_2,
            R.string.lands_duration_joke_3,
            R.string.lands_duration_joke_4,
            R.string.lands_duration_joke_5,
            R.string.lands_duration_joke_6,
            R.string.lands_duration_joke_7,
        ))
        duration.state = Duration.State.LAUGHING
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_duration_joke_8,
            R.string.lands_duration_joke_9,
            R.string.lands_duration_joke_10,
            R.string.lands_duration_joke_11,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_duration_joke_12,
            R.string.lands_duration_joke_13,
        ))
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_duration_joke_14,
            R.string.lands_duration_joke_15,
            R.string.lands_duration_joke_16,
            R.string.lands_duration_joke_17,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_duration_joke_18,
            R.string.lands_duration_joke_19,
            R.string.lands_duration_joke_20,
        ))
        duration.state = Duration.State.NORMAL
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_duration_joke_21,
            R.string.lands_duration_joke_22,
        ))
    }
})