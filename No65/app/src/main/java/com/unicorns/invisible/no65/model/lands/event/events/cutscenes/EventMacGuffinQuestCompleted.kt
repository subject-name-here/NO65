package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.MACGUFFIN_QUEST_COMPLETED
import com.unicorns.invisible.no65.saveload.GlobalState


class EventMacGuffinQuestCompleted : Event(lamnda@ { manager ->
    if (GlobalState.getBoolean(manager.activity, MACGUFFIN_QUEST_COMPLETED)) return@lamnda
    manager.wrapCutscene {
        drawer.showMessages(
            listOf(
                R.string.lands_macguffin_quest_3_1,
                R.string.lands_macguffin_quest_3_2,
                R.string.lands_macguffin_quest_3_3,
                R.string.lands_macguffin_quest_3_4,
                R.string.lands_macguffin_quest_3_5,
                R.string.lands_macguffin_quest_3_6,
                R.string.lands_macguffin_quest_3_7,
            ),
            color = R.color.black,
            tapSoundId = R.raw.sfx_tap
        )
    }
    GlobalState.putBoolean(manager.activity, MACGUFFIN_QUEST_COMPLETED, true)
})