package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.SplittingApart
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventSplittingApartBreakdown(sa: SplittingApart) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(
            sa, listOf(
                R.string.lands_splitting_apart_event_1,
                R.string.lands_splitting_apart_event_2,
                R.string.lands_splitting_apart_event_3,
                R.string.lands_splitting_apart_event_4,
            )
        )

        delay(1000L)
        sa.state = SplittingApart.State.PANICKING
        delay(500L)

        drawer.showCharacterMessages(
            sa, listOf(
                R.string.lands_splitting_apart_event_5,
                R.string.lands_splitting_apart_event_6,
                R.string.lands_splitting_apart_event_7,
                R.string.lands_splitting_apart_event_8,
                R.string.lands_splitting_apart_event_9,
                R.string.lands_splitting_apart_event_10,
                R.string.lands_splitting_apart_event_11,
                R.string.lands_splitting_apart_event_12,
                R.string.lands_splitting_apart_event_13,
                R.string.lands_splitting_apart_event_14,
                R.string.lands_splitting_apart_event_15,
                R.string.lands_splitting_apart_event_16,
                R.string.lands_splitting_apart_event_17,
            )
        )
        sa.state = SplittingApart.State.DONE
        delay(500L)
        drawer.showCharacterMessages(
            sa, listOf(
                R.string.lands_splitting_apart_event_18,
                R.string.lands_splitting_apart_event_19,
            )
        )
    }
})