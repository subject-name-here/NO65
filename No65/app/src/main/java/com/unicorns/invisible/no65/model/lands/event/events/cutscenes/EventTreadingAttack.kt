package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTreading
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.TREADING_WARNING_ACTIVATED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Treading
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.TREADING_CUTSCENE_FIRED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.CellUtils


class EventTreadingAttack : Event({ manager ->
    val floor = CellUtils.findCurrentMapClosestFloor(manager, manager.gameState.protagonist)!!
    val treading = manager.gameState.currentMap.createCellOnTop(floor, Treading::class)

    val wasWarned = TREADING_WARNING_ACTIVATED in manager.gameState.flagsMaster
    val wasFired = GlobalState.getBoolean(manager.activity, TREADING_CUTSCENE_FIRED)

    val lines = when {
        wasFired -> {
            listOf(
                R.string.lands_treading_attack_again_1,
                R.string.lands_treading_attack_again_2,
                R.string.lands_treading_attack_again_3,
            )
        }
        wasWarned -> {
            listOf(
                R.string.lands_treading_attack_1,
                R.string.lands_treading_attack_warned_1,
                R.string.lands_treading_attack_warned_2,
                R.string.lands_treading_attack_warned_3,
                R.string.lands_treading_attack_warned_4,
                R.string.lands_treading_attack_warned_5,
                R.string.lands_treading_attack_2,
                R.string.lands_treading_attack_3,
                R.string.lands_treading_attack_4,
                R.string.lands_treading_attack_5,
                R.string.lands_treading_attack_6,
                R.string.lands_treading_attack_7,
                R.string.lands_treading_attack_8,
            )
        }
        else -> {
            listOf(
                R.string.lands_treading_attack_1,
                R.string.lands_treading_attack_not_warned_1,
                R.string.lands_treading_attack_not_warned_2,
                R.string.lands_treading_attack_not_warned_3,
                R.string.lands_treading_attack_not_warned_4,
                R.string.lands_treading_attack_2,
                R.string.lands_treading_attack_3,
                R.string.lands_treading_attack_4,
                R.string.lands_treading_attack_5,
                R.string.lands_treading_attack_6,
                R.string.lands_treading_attack_7,
                R.string.lands_treading_attack_8,
            )
        }
    }

    manager.wrapCutscene {
        drawer.showCharacterMessages(treading, lines)
    }

    GlobalState.putBoolean(manager.activity, TREADING_CUTSCENE_FIRED, true)
    EventAttack(
        treading,
        BattleFieldTreading(wasFired),
        onAfterVictoryEvent = EventUnlockBattle(51, 64)
    ).fireEventChain(manager)
})