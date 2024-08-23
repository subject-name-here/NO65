package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGentleWind
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGentleWindTainted
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventGentleWindWar
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class GentleWind(override var cellBelow: Cell) : CellNPC() {
    override val nameId: Int
        get() = R.string.lands_gentle_wind_name
    override val id: Int
        get() = 57

    var state: State = State.STANDARD
    override val faceCell: String
        get() = when (state) {
            State.STANDARD, State.DECLARING_WAR -> "\uD83E\uDD29"
            State.OFFERING -> "\uD83D\uDE23"
            State.THINKING -> "\uD83E\uDD14"
            State.HAPPY -> "\uD83D\uDE02"
            State.NEUTRAL -> "\uD83D\uDE10"
        }
    override val centerSymbol: String
        get() = "⧓"
    override val centerSymbolColor: Int
        get() = R.color.red
    override val emotion: Emotion
        get() = when (state) {
            State.STANDARD, State.DECLARING_WAR -> Emotion.ARROGANCE
            State.OFFERING -> Emotion.BRAVERY
            State.THINKING -> Emotion.WHISPER
            State.HAPPY -> Emotion.JOY
            State.NEUTRAL -> Emotion.LAW
        }

    override val speechSound: Int
        get() = when (state) {
            State.THINKING -> R.raw.sfx_whisper
            else -> R.raw.sfx_speech
        }

    override fun use(): Event {
        return Event.Null
    }

    enum class State {
        STANDARD,
        OFFERING,
        THINKING,
        HAPPY,
        NEUTRAL,
        DECLARING_WAR
    }

    @Transient
    private var eventFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        if (state == State.DECLARING_WAR && !eventFired) {
            eventFired = true
            return EventGentleWindWar(this)
                .then(EventAttack(
                    this,
                    nonEmptyListOf(
                        BattleFieldGentleWind(),
                        BattleFieldGentleWindTainted()
                    ),
                    onAfterVictoryEvent = EventUnlockBattle(62)
                ))
        }
        return Event.Null
    }
}