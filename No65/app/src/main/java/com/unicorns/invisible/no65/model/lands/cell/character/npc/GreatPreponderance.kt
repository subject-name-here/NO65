package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGreatPreponderance
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class GreatPreponderance(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 28
    override val nameId: Int
        get() = R.string.lands_great_preponderance_name

    override val centerSymbol: String
        get() = "\uD83E\uDD47"
    override val centerSymbolColor
        get() = R.color.true_yellow

    var state = State.HOSTILE
    override fun chillCheck() = state == State.CHILL

    override val faceCell: String
        get() = if (chillCheck()) "\uD83D\uDE0E" else "\uD83D\uDE20"

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.FRIENDLINESS else Emotion.ANGER

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldGreatPreponderance(),
            onAfterVictoryEvent = EventUnlockBattle(61)
        )

    private var linesSaid = 0
    override val chillEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this) {
                listOf(
                    R.string.lands_great_preponderance_chill_0_1,
                    R.string.lands_great_preponderance_chill_0_2,
                    R.string.lands_great_preponderance_chill_0_3,
                    R.string.lands_great_preponderance_chill_0_4,
                    R.string.lands_great_preponderance_chill_0_5,
                )
            }
            1 -> EventNPCSpeak(this) {
                R.string.lands_great_preponderance_chill_1_1
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_great_preponderance_chill_2_1
            }
            3 -> EventNPCSpeak(this) {
                R.string.lands_great_preponderance_chill_3_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_great_preponderance_chill_else_1
            }
        }

    override val speakEvent: Event
        get() = EventNPCSpeakCutscene(this) {
            listOf(R.string.lands_great_preponderance_0_1)
        }.then(attackEvent)

    enum class State {
        CHILL,
        HOSTILE
    }
}