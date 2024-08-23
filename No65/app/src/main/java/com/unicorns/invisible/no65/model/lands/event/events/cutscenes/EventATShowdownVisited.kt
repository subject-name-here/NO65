package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ArousingThunder
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.AT_EVADED_LIGHTNING
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.AT_LIGHTNING_STRIKE_REACHED
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.coroutines.delay


class EventATShowdownVisited(thunderCell: ArousingThunder) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        val reachedLightning = GlobalState.getInt(activity, AT_LIGHTNING_STRIKE_REACHED)
        val lightningEvaded = GlobalState.getBoolean(activity, AT_EVADED_LIGHTNING)
        if (reachedLightning > 0 && !lightningEvaded) {
            when (reachedLightning) {
                1, 2 -> {}
                3 -> {
                    drawer.showMessagesPhoneWithUnknownHead(listOf(
                        R.string.lands_arousing_thunder_showdown_again_3_1,
                        R.string.lands_arousing_thunder_showdown_again_3_2,
                        R.string.lands_arousing_thunder_showdown_again_3_3,
                        R.string.lands_arousing_thunder_showdown_again_3_4,
                        R.string.lands_arousing_thunder_showdown_again_3_5,
                        R.string.lands_arousing_thunder_showdown_again_3_6,
                    ))
                    delay(1000L)
                }
                4 -> {
                    drawer.showMessagesPhoneWithUnknownHead(listOf(
                        R.string.lands_arousing_thunder_showdown_again_4_1,
                        R.string.lands_arousing_thunder_showdown_again_4_2,
                        R.string.lands_arousing_thunder_showdown_again_4_3,
                        R.string.lands_arousing_thunder_showdown_again_4_4,
                        R.string.lands_arousing_thunder_showdown_again_4_5,
                    ))
                    delay(1000L)
                }
                5 -> {
                    drawer.showMessagesPhoneWithUnknownHead(listOf(
                        R.string.lands_arousing_thunder_showdown_again_5_1,
                        R.string.lands_arousing_thunder_showdown_again_5_2,
                        R.string.lands_arousing_thunder_showdown_again_5_3,
                        R.string.lands_arousing_thunder_showdown_again_5_4,
                        R.string.lands_arousing_thunder_showdown_again_5_5,
                        R.string.lands_arousing_thunder_showdown_again_5_6,
                        R.string.lands_arousing_thunder_showdown_again_5_7,
                        R.string.lands_arousing_thunder_showdown_again_5_8,
                        R.string.lands_arousing_thunder_showdown_again_5_9,
                    ))
                    delay(1000L)
                }
                else -> {
                    drawer.showMessagesPhoneWithUnknownHead(listOf(
                        R.string.lands_arousing_thunder_showdown_again_else_1
                    ))
                    delay(1000L)
                }
            }
        }
        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown_again_at_1,
                R.string.lands_arousing_thunder_showdown_again_at_2,
            )
        )
    }
})