package com.unicorns.invisible.no65.model.lands.event.events.d99

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ComingToMeet
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Modesty
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventPreD99(deliverance: Deliverance) : Event(lambda@ { manager ->
    val gameState = manager.gameState
    if (gameState !is GameState65) return@lambda
    gameState.protagonist.moneys = 0

    manager.activity.musicPlayer.playMusicSuspendTillStart(
        R.raw.cutscene_pre_d99_1,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    manager.wrapCutscene {
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_pre_d99_1,
            R.string.lands_deliverance_pre_d99_2,
            R.string.lands_deliverance_pre_d99_3,
            R.string.lands_deliverance_pre_d99_4,
            R.string.lands_deliverance_pre_d99_5,
            R.string.lands_deliverance_pre_d99_6,
            R.string.lands_deliverance_pre_d99_7,
            R.string.lands_deliverance_pre_d99_8,
            R.string.lands_deliverance_pre_d99_9,
            R.string.lands_deliverance_pre_d99_10,
            R.string.lands_deliverance_pre_d99_11,
            R.string.lands_deliverance_pre_d99_12,
            R.string.lands_deliverance_pre_d99_13,
        ))
        manager.activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.cutscene_pre_d99_2,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_pre_d99_14,
            R.string.lands_deliverance_pre_d99_15,
            R.string.lands_deliverance_pre_d99_16,
            R.string.lands_deliverance_pre_d99_17,
            R.string.lands_deliverance_pre_d99_18,
            R.string.lands_deliverance_pre_d99_19,
        ))
        val protagonistFloor = deliverance.coordinates + Coordinates(2, 0)
        gameState.currentMap.moveTo(gameState.protagonist, protagonistFloor)
        val floor1 = deliverance.coordinates + Coordinates(0, 1)
        val floor2 = deliverance.coordinates + Coordinates(0, -1)
        delay(500L)
        val modesty = gameState.currentMap.createCellOnTop(floor1, Modesty::class)
        delay(500L)
        val comingToMeet = gameState.currentMap.createCellOnTop(floor2, ComingToMeet::class)
        delay(500L)
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_pre_d99_20,
        ))
        drawer.showCharacterMessages(comingToMeet, listOf(R.string.lands_deliverance_pre_d99_coming_to_meet_line))
        drawer.showCharacterMessages(modesty, listOf(R.string.lands_deliverance_pre_d99_modesty_line))
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_pre_d99_21,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_pre_d99_22,
            R.string.lands_deliverance_pre_d99_23,
        ))

        EventD99().fireEventChain(manager)
    }
})