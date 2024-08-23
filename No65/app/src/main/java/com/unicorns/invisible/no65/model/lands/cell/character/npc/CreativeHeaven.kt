package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldCreativeHeaven
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventCreativeHeaven
import com.unicorns.invisible.no65.model.lands.event.events.util.EventCreativeHeavenDead
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class CreativeHeaven(override var cellBelow: Cell): CellNPC() {
    override val id: Int
        get() = 1
    override val nameId: Int
        get() = R.string.lands_creative_heaven_name

    override val centerSymbol: String
        get() = "⏳"
    override val centerSymbolColor
        get() = R.color.black
    override val faceCell: String
        get() = "\uD83D\uDE0F"

    override val emotion: Emotion = Emotion.ARROGANCE

    override fun use(): Event {
        return Event.Null
    }

    @Transient
    private var eventFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        return if (eventFired) {
            Event.Null
        } else {
            eventFired = true
            EventFactory.createWithNext { manager ->
                val wordsSaid = GlobalState.getBoolean(manager.activity, GlobalFlags.CREATIVE_HEAVEN_BATTLE_IN_PROGRESS)
                if (!wordsSaid) {
                    EventCreativeHeaven(this@CreativeHeaven)
                } else {
                    Event.Null
                }.then(
                    EventAttack(
                        this@CreativeHeaven,
                        BattleFieldCreativeHeaven(hasBeginning = !wordsSaid),
                        onAfterVictoryEvent = EventCreativeHeavenDead().then(EventUnlockBattle(53))
                    )
                )
            }
        }
    }
}