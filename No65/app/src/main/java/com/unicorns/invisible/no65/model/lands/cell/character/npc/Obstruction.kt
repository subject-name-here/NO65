package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldObstruction
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventObstructionIntro
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Obstruction(override var cellBelow: Cell) : CellNPCStandard(), CellControl {
    override val id: Int
        get() = 39
    override val nameId: Int
        get() = R.string.lands_obstruction_name

    override val centerSymbol: String
        get() = "⏻"
    override val centerSymbolColor
        get() = R.color.red
    override val faceCell: String
        get() = "\uD83E\uDD16"

    var state = State.PSEUDO_HOSTILE
    override val emotion: Emotion
        get() = when (state) {
            State.PSEUDO_HOSTILE -> Emotion.HOSTILITY
            State.NORMAL -> Emotion.CALMNESS
        }

    override val speechSound: Int
        get() = R.raw.sfx_speech_robot

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldObstruction(),
            onAfterVictoryEvent = EventUnlockBattle(23)
        )

    private var linesSaid = 0
    private var isSpeaking = false
    override val speakEvent: Event
        get() {
            if (isSpeaking) {
                return Event.Null
            }
            isSpeaking = true
            return when (linesSaid++) {
                0 -> EventObstructionIntro(this)
                1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_obstruction_1_1,
                        R.string.lands_obstruction_1_2,
                        R.string.lands_obstruction_1_3,
                        R.string.lands_obstruction_1_4,
                    )
                }
                2 -> EventNPCSpeak(this) {
                    R.string.lands_obstruction_2_1
                }
                3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_obstruction_3_1,
                        R.string.lands_obstruction_3_2,
                        R.string.lands_obstruction_3_3,
                    )
                }
                4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_obstruction_4_1,
                        R.string.lands_obstruction_4_2,
                        R.string.lands_obstruction_4_3,
                    )
                }
                5 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_obstruction_5_1,
                        R.string.lands_obstruction_5_2,
                    )
                }
                else -> EventNPCSpeak(this) {
                    R.string.lands_obstruction_else_1
                }
            }.then(EventFactory.create { isSpeaking = false })
        }

    private val getOutOfMyWayEvent: Event
        get() {
            isSpeaking = true
            return EventNPCSpeak(this) {
                R.string.lands_obstruction_get_out_1
            }.then(EventFactory.create { isSpeaking = false })
        }

    override fun onTickWithEvent(tick: Int): Event {
        return if (tick % (LandsManager.TICKS_PER_SECOND * 5) == 0) {
            eventMove()
        } else {
            Event.Null
        }
    }

    @Transient
    private val trajectory = listOf(
        Coordinates(-5, 1),
        Coordinates(-4, 1),
        Coordinates(-3, 1),
        Coordinates(-2, 1),
        Coordinates(-1, 1),
        Coordinates(0, 1),
        Coordinates(0, 2),
        Coordinates(0, 3),
        Coordinates(-1, 3),
        Coordinates(-2, 3),
        Coordinates(-3, 3),
        Coordinates(-4, 3),
        Coordinates(-5, 3),
        Coordinates(-5, 2),
    )
    private fun eventMove() = EventFactory.createWithNext { manager ->
        if (isSpeaking) {
            return@createWithNext Event.Null
        }

        val index = trajectory.indexOf(coordinates)
        if (index == -1) {
            return@createWithNext Event.Null
        }

        val newIndex = (index + 1) % trajectory.size
        val moveResult = manager.gameState.currentMap.moveTo(this@Obstruction, trajectory[newIndex])

        if (
            !moveResult &&
            manager.gameState.currentMap.getTopCell(trajectory[newIndex]) is CellProtagonist
        ) {
            return@createWithNext if (linesSaid == 0) speakEvent else getOutOfMyWayEvent
        }

        Event.Null
    }

    enum class State {
        PSEUDO_HOSTILE,
        NORMAL
    }
}