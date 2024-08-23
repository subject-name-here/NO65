package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Opposition
import com.unicorns.invisible.no65.model.lands.event.Event


class EventOppositionMoneyStolen : Event(lambda@ { manager ->
    val opposition = manager.gameState.currentMap.getTopCells()
        .filterIsInstance<Opposition>()
        .firstOrNull() ?: return@lambda

    manager.wrapCutscene {
        opposition.emotionState = Opposition.EmotionState.ANGRY
        drawer.showCharacterMessages(opposition,
            listOf(
                R.string.lands_opposition_money_stolen_1,
                R.string.lands_opposition_money_stolen_2,
                R.string.lands_opposition_money_stolen_3,
                R.string.lands_opposition_money_stolen_4,
            )
        )
    }
    opposition.strAttackEvent.fireEventChain(manager)
})