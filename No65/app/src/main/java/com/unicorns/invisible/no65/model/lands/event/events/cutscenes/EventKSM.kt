package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.KeepingStillMountain
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.KSM_BOSS_REVEALED
import com.unicorns.invisible.no65.saveload.GlobalState


class EventKSM(
    private val characterCell: KeepingStillMountain,
): Event(lambda@ { manager ->
    if (GlobalState.getBoolean(manager.activity, KSM_BOSS_REVEALED)) {
        manager.wrapCutscene {
            drawer.showCharacterMessages(characterCell, listOf(
                R.string.lands_keeping_still_mountain_event_again_1,
                R.string.lands_keeping_still_mountain_event_again_2,
            ))
        }
        return@lambda
    }

    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_keeping_still_mountain_event_1,
            R.string.lands_keeping_still_mountain_event_2,
            R.string.lands_keeping_still_mountain_event_3,
            R.string.lands_keeping_still_mountain_event_4,
            R.string.lands_keeping_still_mountain_event_5,
            R.string.lands_keeping_still_mountain_event_6,
            R.string.lands_keeping_still_mountain_event_7,
            R.string.lands_keeping_still_mountain_event_8,
            R.string.lands_keeping_still_mountain_event_9,
            R.string.lands_keeping_still_mountain_event_10,
            R.string.lands_keeping_still_mountain_event_11,
            R.string.lands_keeping_still_mountain_event_12,
            R.string.lands_keeping_still_mountain_event_13,
            R.string.lands_keeping_still_mountain_event_14,
            R.string.lands_keeping_still_mountain_event_15,
            R.string.lands_keeping_still_mountain_event_16,
            R.string.lands_keeping_still_mountain_event_17,
            R.string.lands_keeping_still_mountain_event_18,
            R.string.lands_keeping_still_mountain_event_19,
            R.string.lands_keeping_still_mountain_event_20,
            R.string.lands_keeping_still_mountain_event_21,
        ))
    }
})