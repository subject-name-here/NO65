package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.cell.character.CellTheCreature
import com.unicorns.invisible.no65.model.lands.cell.service.SaveCellDecoy
import com.unicorns.invisible.no65.model.lands.cell.service.SaveDestroyedCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_ENSURES_VICTORY
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventPreFinalBoss(creature: CellTheCreature) : Event(lambda@ {manager ->
    val cheated = !manager.gameState.flagsMaster.contains(STARTED_GAME)
    if (cheated) {
        manager.wrapCutsceneSkippable {
            drawer.showCharacterMessages(creature, listOf(
                R.string.lands_the_creature_fight_cheated_1,
                R.string.lands_the_creature_fight_cheated_2,
            ))
        }
        return@lambda
    }

    manager.wrapCutscene {
        activity.musicPlayer.playMusic(
            R.raw.sfx_creature_bit,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )

        CellUtils.getCellsInSight(this).filterIsInstance<SaveCellDecoy>().forEach {
            gameState.currentMap.clearCell(it.coordinates)
            gameState.currentMap.createCellOnTop(it.coordinates, SaveDestroyedCell::class)
        }

        delay(2500L)
        if (!GlobalState.getBoolean(activity, THE_CREATURE_ENSURES_VICTORY)) {
            GlobalState.putBoolean(activity, THE_CREATURE_ENSURES_VICTORY, true)
            drawer.showCharacterMessages(creature, listOf(
                R.string.lands_the_creature_fight_intro_1,
                R.string.lands_the_creature_fight_new_0_1,
                R.string.lands_the_creature_fight_new_0_2,
                R.string.lands_the_creature_fight_new_0_3,
                R.string.lands_the_creature_fight_new_0_4,
                R.string.lands_the_creature_fight_new_0_5,
                R.string.lands_the_creature_fight_new_0_6,
                R.string.lands_the_creature_fight_new_0_7,
            ))

            delay(10000L)

            drawer.showCharacterMessages(creature, listOf(
                R.string.lands_the_creature_fight_new_1_1,
                R.string.lands_the_creature_fight_new_1_2,
                R.string.lands_the_creature_fight_new_1_3,
                R.string.lands_the_creature_fight_new_1_4,
                R.string.lands_the_creature_fight_new_1_5,
                R.string.lands_the_creature_fight_new_1_6,
            ))
        } else {
            drawer.showCharacterMessages(creature, listOf(
                R.string.lands_the_creature_fight_intro_1,
                R.string.lands_the_creature_fight_again_1
            ))
        }
    }
})