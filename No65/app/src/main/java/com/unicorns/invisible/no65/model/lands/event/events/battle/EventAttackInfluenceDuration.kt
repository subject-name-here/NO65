package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDuration
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldInfluence
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Duration
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Influence
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle


class EventAttackInfluenceDuration : Event(lambda@ { manager ->
    val influence = manager.gameState.currentMap.getTopCells().filterIsInstance<Influence>().firstOrNull()
    val duration = manager.gameState.currentMap.getTopCells().filterIsInstance<Duration>().firstOrNull()
    if (influence == null || duration == null) {
        return@lambda
    }

    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(influence, listOf(
            R.string.lands_influence_duration_attack_influence_1,
            R.string.lands_influence_duration_attack_influence_2,
            R.string.lands_influence_duration_attack_influence_3,
        ))
        drawer.showCharacterMessages(duration, listOf(
            R.string.lands_influence_duration_attack_duration_1
        ))
    }

    EventAttackDouble(
        influence,
        duration,
        nonEmptyListOf(BattleFieldDuration(), BattleFieldInfluence()),
        onAfterVictoryEvent = EventUnlockBattle(39)
    ).fireEventChain(manager)
})