package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.MACGUFFINS_DESTROYED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MACGUFFIN_QUEST_STARTED
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.MACGUFFIN_QUEST_COMPLETED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventMacGuffinQuest : Event(lambda@ { manager ->
    val state = manager.gameState
    val counter = state.countersMaster.preInc(MACGUFFINS_DESTROYED)
    val questCompleted = GlobalState.getBoolean(manager.activity, MACGUFFIN_QUEST_COMPLETED)

    if (counter > MACGUFFINS_NUMBER) return@lambda

    suspend fun showMacGuffinProgress() {
        manager.drawer.showMessageFormatted(
            R.string.lands_macguffin_quest_counter,
            color = R.color.black,
            tapSoundId = R.raw.sfx_tap,
            formatArgs = arrayOf(counter, MACGUFFINS_NUMBER)
        )
    }

    when (counter) {
        1 -> {
            if (state is GameState65 && state.battleMode == BattleMode.FIXED_PEACE) {
                state.flagsMaster.add(MACGUFFIN_QUEST_STARTED)
                if (!questCompleted) {
                    manager.wrapCutscene {
                        drawer.showMessage(
                            R.string.lands_macguffin_quest_1_1,
                            color = R.color.black,
                            tapSoundId = R.raw.sfx_tap
                        )
                        drawer.showMessageFormatted(
                            R.string.lands_macguffin_quest_1_2,
                            color = R.color.black,
                            tapSoundId = R.raw.sfx_tap,
                            formatArgs = arrayOf(MACGUFFINS_NUMBER)
                        )
                        drawer.showMessages(
                            listOf(
                                R.string.lands_macguffin_quest_1_3,
                                R.string.lands_macguffin_quest_1_4,
                            ),
                            color = R.color.black,
                            tapSoundId = R.raw.sfx_tap
                        )
                    }
                } else {
                    manager.wrapCutsceneSkippable {
                        showMacGuffinProgress()
                    }
                }
            }
        }
        MACGUFFINS_NUMBER -> {
            if (MACGUFFIN_QUEST_STARTED in state.flagsMaster) {
                manager.wrapCutscene {
                    if (!questCompleted) {
                        drawer.showMessages(
                            listOf(
                                R.string.lands_macguffin_quest_2_1,
                                R.string.lands_macguffin_quest_2_2,
                                R.string.lands_macguffin_quest_2_3,
                                R.string.lands_macguffin_quest_2_4,
                            ),
                            color = R.color.black,
                            tapSoundId = R.raw.sfx_tap
                        )
                    }
                    activity.musicPlayer.playMusic(
                        R.raw.sfx_macguffin_teleport,
                        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                        isLooping = false
                    )
                    drawer.fadeInWhite(time = 4500L)
                    EventTeleport(1, Coordinates.ZERO, manager.gameState.protagonist).fireEventChain(this)
                    drawer.hideIncludeLayout()
                }
            }
        }
        else -> {
            if (MACGUFFIN_QUEST_STARTED in state.flagsMaster) {
                manager.wrapCutsceneSkippable {
                    showMacGuffinProgress()
                }
            }
        }
    }
}) {
    companion object {
        const val MACGUFFINS_NUMBER = 31
    }
}