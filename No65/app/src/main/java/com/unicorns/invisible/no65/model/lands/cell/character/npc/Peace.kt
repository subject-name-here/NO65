package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldPeace
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventPeace
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Peace(override var cellBelow: Cell) : CellNPC() {
    override val id: Int
        get() = 11
    override val nameId: Int
        get() = R.string.lands_peace_name

    override val centerSymbol: String
        get() = "☮"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDC3C"

    override val emotion: Emotion
        get() = Emotion.LAW

    private val attackEvent
        get() = EventAttack(
            this,
            BattleFieldPeace(),
            onAfterVictoryEvent = EventUnlockBattle(24)
        )

    private var linesSaid = 0
    private val speakEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) { R.string.lands_peace_0_1 }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_peace_1_1,
                    R.string.lands_peace_1_2,
                    R.string.lands_peace_1_3,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_peace_2_1,
                    R.string.lands_peace_2_2,
                    R.string.lands_peace_2_3,
                    R.string.lands_peace_2_4,
                    R.string.lands_peace_2_5,
                    R.string.lands_peace_2_6,
                    R.string.lands_peace_2_7,
                    R.string.lands_peace_2_8,
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_peace_3_1,
                    R.string.lands_peace_3_2,
                    R.string.lands_peace_3_3,
                    R.string.lands_peace_3_4,
                )
            }
            4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_peace_4_1,
                    R.string.lands_peace_4_2,
                    R.string.lands_peace_4_3,
                )
            }
            else -> EventNPCSpeak(this) { R.string.lands_peace_else_1 }
        }

    private var onSightEventLaunched = false
    override fun onSight(distanceToProtagonist: Int): Event {
        if (state != State.AGGRESSIVE_ON_SIGHT) {
            return Event.Null
        }
        if (distanceToProtagonist <= 2 && !onSightEventLaunched) {
            onSightEventLaunched = true
            return EventPeace(this).then(attackEvent)
        }

        return Event.Null
    }

    override fun use(): Event {
        return when (state) {
            State.AGGRESSIVE_ON_SIGHT -> EventPeace(this).then(attackEvent)
            State.NORMAL -> EventFactory.createWithNext { manager ->
                if (manager.gameState !is GameState65) return@createWithNext Event.Null
                if (manager.gameState.battleMode == BattleMode.ATTACK) {
                    attackEvent
                } else {
                    speakEvent
                }
            }
        }
    }

    var state = State.NORMAL
    enum class State {
        AGGRESSIVE_ON_SIGHT,
        NORMAL
    }
}