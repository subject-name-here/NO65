package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventMacGuffinQuestCompleted
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.SOUND_TEST_NO_ENEMY_DEAD
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventNoEnemyVictory: Event({ manager ->
    GlobalState.putBoolean(manager.activity, SOUND_TEST_NO_ENEMY_DEAD, true)
    launchCoroutineOnMain {
        val newManager = LandsManager(manager.activity, manager.gameState)

        if (newManager.gameState is GameState65) {
            newManager.gameState.battleMode = BattleMode.PEACE
        }
        newManager.init()
        newManager.processMap()

        EventMacGuffinQuestCompleted().fireEventChain(newManager)
    }
})