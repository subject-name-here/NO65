package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.D_NINETY_NINE_DEFEATED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ComingToMeet
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Modesty
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.SOUND_TEST_D99_DEAD
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventBattleD99Victory : Event({ manager ->
    GlobalState.putBoolean(manager.activity, SOUND_TEST_D99_DEAD, true)
    launchCoroutineOnMain {
        val newManager = LandsManager(manager.activity, manager.gameState)

        val deliverance = newManager.gameState.currentMap.getTopCells().filterIsInstance<Deliverance>().firstOrNull()
        if (deliverance != null) {
            newManager.gameState.currentMap.removeCellFromTop(deliverance)
        }
        val modesty = newManager.gameState.currentMap.getTopCells().filterIsInstance<Modesty>().firstOrNull()
        if (modesty != null) {
            newManager.gameState.currentMap.removeCellFromTop(modesty)
        }
        val comingToMeet = newManager.gameState.currentMap.getTopCells().filterIsInstance<ComingToMeet>().firstOrNull()
        if (comingToMeet != null) {
            newManager.gameState.currentMap.removeCellFromTop(comingToMeet)
        }

        newManager.gameState.flagsMaster.add(D_NINETY_NINE_DEFEATED)

        if (newManager.gameState is GameState65) {
            newManager.gameState.battleMode = BattleMode.PEACE
            newManager.gameState.protagonist.killed += 3
        }
        newManager.init()
        newManager.processMap()
        // EventTreadingCheckAfterBattle().fireEventChain(newManager)
    }
})