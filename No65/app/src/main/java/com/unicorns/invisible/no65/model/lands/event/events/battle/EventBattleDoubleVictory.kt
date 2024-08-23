package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.character.npc.CellNPC
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventBattleDoubleVictory(
    characterCell1: CellNPC,
    characterCell2: CellNPC,
    onAfterVictoryEvent: Event
): Event({ manager ->
    launchCoroutineOnMain {
        val newManager = LandsManager(manager.activity, manager.gameState)
        newManager.gameState.currentMap.apply {
            removeCellFromTop(characterCell1)
            removeCellFromTop(characterCell2)
        }
        if (newManager.gameState is GameState65) {
            newManager.gameState.battleMode = BattleMode.PEACE
            newManager.gameState.protagonist.killed += 2
        }
        newManager.init()
        newManager.processMap()

        onAfterVictoryEvent.fireEventChain(newManager)
        EventTreadingCheckAfterBattle().fireEventChain(newManager)
    }
})