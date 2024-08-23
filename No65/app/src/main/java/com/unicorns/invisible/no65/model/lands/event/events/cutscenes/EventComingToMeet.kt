package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ComingToMeet
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay

class EventComingToMeet(
    private val characterCell: ComingToMeet,
): Event({ manager ->
    val color = characterCell.speechColor
    val sound = characterCell.speechSound
    val messages1 = listOf(
        R.string.lands_coming_to_meet_eow_1
    )
    val messages2 = listOf(
        R.string.lands_coming_to_meet_eow_2
    )
    val messages3 = listOf(
        R.string.lands_coming_to_meet_eow_3
    )
    val messages4 = listOf(
        R.string.lands_coming_to_meet_eow_4
    )
    val messages5 = listOf(
        R.string.lands_coming_to_meet_eow_5,
        R.string.lands_coming_to_meet_eow_6
    )
    val messages6 = listOf(
        R.string.lands_coming_to_meet_eow_7
    )

    manager.wrapCutscene {
        drawer.showTalkingHead(characterCell)
        drawer.showMessages(messages1, color, sound)

        characterCell.state = ComingToMeet.State.GLAD
        drawer.updateTalkingHeadFace(characterCell)
        drawer.showMessages(messages2, color, sound)

        characterCell.state = ComingToMeet.State.HAPPY
        drawer.updateTalkingHeadFace(characterCell)
        drawer.showMessages(messages3, color, sound)

        characterCell.state = ComingToMeet.State.NOT_VERY_GLAD
        drawer.updateTalkingHeadFace(characterCell)
        drawer.showMessages(messages4, color, sound)

        characterCell.state = ComingToMeet.State.HAPPY
        drawer.updateTalkingHeadFace(characterCell)
        drawer.showMessages(messages5, color, sound)
        drawer.hideTalkingHead()

        gameState.currentMap.removeCellFromTop(characterCell)
        val hubMap = gameState.mapGraph.getMap("map_hub")
        hubMap.addCellOnTop(characterCell, Coordinates(7, 2))
        hubMap.visited = true

        delay(1000L)

        drawer.showTalkingHead(characterCell)
        drawer.showMessages(messages6, color, sound)
        drawer.hideTalkingHead()

        (gameState.protagonist as CellProtagonist).youState = CellProtagonist.YouState.BC
    }
})