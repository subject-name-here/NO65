package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldKeepingStillMountain
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.battle.victory.EventKSMAfterVictory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventKSM
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class KeepingStillMountain(override var cellBelow: Cell): CellNPC() {
    override val id: Int
        get() = 52
    override val nameId: Int
        get() = R.string.lands_keeping_still_mountain_name

    override val centerSymbol: String
        get() = "\uD83D\uDDFB"
    override val centerSymbolColor
        get() = R.color.grey
    override val faceCell: String
        get() = "\uD83D\uDDFF"

    override val emotion: Emotion
        get() = Emotion.INDIFFERENCE
    override val speechSound: Int
        get() = R.raw.sfx_low_speech

    private var attackEventFired = false
    private val attackEvent : Event
        get() = EventKSM(this)
            .then(
                EventAttack(
                    this,
                    BattleFieldKeepingStillMountain(),
                    onAfterVictoryEvent = EventKSMAfterVictory().then(EventUnlockBattle(9))
                )
            )

    override fun use(): Event {
        return Event.Null
    }

    override fun onSight(distanceToProtagonist: Int): Event {
        return if (attackEventFired) {
            Event.Null
        } else {
            attackEventFired = true
            attackEvent
        }
    }
}