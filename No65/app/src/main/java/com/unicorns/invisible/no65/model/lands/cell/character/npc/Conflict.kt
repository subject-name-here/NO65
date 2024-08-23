package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldConflict
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Conflict(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 6
    override val nameId: Int
        get() = R.string.lands_conflict_name

    override val centerSymbol: String
        get() = "\uD83D\uDC4A"
    override val centerSymbolColor
        get() = R.color.red
    override val faceCell: String
        get() = "\uD83E\uDD2C"

    var state = State.UNFRIENDLY
    override fun chillCheck() = state == State.CHILL

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.FRIENDLINESS else Emotion.BRAVERY

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldConflict(),
        onAfterVictoryEvent = EventUnlockBattle(37)
    )

    private var linesSaid = 0
    override val chillEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_conflict_hub_0_1
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_conflict_hub_1_1,
                    R.string.lands_conflict_hub_1_2,
                    R.string.lands_conflict_hub_1_3,
                )
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_conflict_hub_2_1
            }
        }

    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_conflict_0_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_conflict_else_1
            }
        }

    enum class State {
        CHILL,
        UNFRIENDLY
    }
}