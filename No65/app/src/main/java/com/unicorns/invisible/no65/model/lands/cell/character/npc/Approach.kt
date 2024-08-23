package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldApproach
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable

@Serializable
class Approach(override var cellBelow: Cell) : CellNPC() {
    override val id: Int
        get() = 19
    override val nameId: Int
        get() = R.string.lands_approach_name
    override val centerSymbol: String
        get() = "\uD83D\uDD78"
    override val centerSymbolColor
        get() =  R.color.almost_black
    override val faceCell: String
        get() = when (state) {
            State.SLEEPING -> "\uD83D\uDE34"
            State.AWAKEN -> "\uD83D\uDE20"
        }
    override val emotion: Emotion
        get() = Emotion.HOSTILITY

    private var state: State = State.SLEEPING

    private val attackEvent
        get() = EventAttack(
            this,
            BattleFieldApproach(),
            onAfterVictoryEvent = EventUnlockBattle(3)
        )

    private val preAttackEvent
        get() = EventNPCSpeakCutscene(this) {
            listOf(R.string.lands_approach_pre_attack)
        }

    private val speakEvent
        get() = EventNPCSpeakCutscene(this) {
            listOf(R.string.lands_approach_speak)
        }
    private val teleportEvent
        get() = EventFactory.createWithNext { manager ->
            EventTeleport(-1, Coordinates.ZERO, manager.gameState.protagonist)
        }

    private var attackEventStarted = false
    private var speakEventStarted = false
    private val clearFlagsEvent
        get() = EventFactory.create {
            attackEventStarted = false
            speakEventStarted = false
        }

    private val useEvent
        get() = EventFactory.createWithNext { manager ->
            if (manager.gameState !is GameState65) return@createWithNext Event.Null

            state = State.AWAKEN
            if (manager.gameState.battleMode == BattleMode.ATTACK) {
                attackEventStarted = true
                preAttackEvent.then(attackEvent)
            } else {
                speakEventStarted = true
                speakEvent.then(teleportEvent)
            }.then(clearFlagsEvent)
        }
    override fun use(): Event {
        return useEvent
    }

    override fun onSight(distanceToProtagonist: Int): Event {
        if (!speakEventStarted && !attackEventStarted) {
            if (distanceToProtagonist < 2) {
                speakEventStarted = true
                state = State.AWAKEN
                return speakEvent.then(teleportEvent).then(clearFlagsEvent)
            } else {
                state = State.SLEEPING
            }
        }
        return Event.Null
    }

    enum class State {
        SLEEPING,
        AWAKEN
    }
}