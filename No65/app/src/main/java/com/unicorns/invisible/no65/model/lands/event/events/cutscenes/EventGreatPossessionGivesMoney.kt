package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GREAT_POSSESSION_GAVE_MONEY
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GreatPossession
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMoneyGP
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import kotlinx.coroutines.delay


class EventGreatPossessionGivesMoney(gp: GreatPossession) : Event({ manager ->
    manager.wrapCutscene {
        drawer.showCharacterMessages(gp, listOf(
            R.string.lands_great_possession_before_d99_1,
            R.string.lands_great_possession_before_d99_2,
            R.string.lands_great_possession_before_d99_3,
            R.string.lands_great_possession_before_d99_4,
            R.string.lands_great_possession_before_d99_5,
            R.string.lands_great_possession_before_d99_6,
            R.string.lands_great_possession_before_d99_7,
            R.string.lands_great_possession_before_d99_8,
            R.string.lands_great_possession_before_d99_9,
            R.string.lands_great_possession_before_d99_10,
            R.string.lands_great_possession_before_d99_11,
            R.string.lands_great_possession_before_d99_12,
            R.string.lands_great_possession_before_d99_13,
            R.string.lands_great_possession_before_d99_14,
            R.string.lands_great_possession_before_d99_15,
            R.string.lands_great_possession_before_d99_16,
        ))

        delay(250L)
        val floor = CellUtils.findCurrentMapClosestFloor(manager, manager.gameState.protagonist)!!
        gameState.currentMap.createCellOnTop(floor, CellMoneyGP::class)
        gameState.flagsMaster.add(GREAT_POSSESSION_GAVE_MONEY)
        delay(250L)
        drawer.showCharacterMessages(gp, listOf(
            R.string.lands_great_possession_before_d99_17,
            R.string.lands_great_possession_before_d99_18,
            R.string.lands_great_possession_before_d99_19,
        ))
    }
})