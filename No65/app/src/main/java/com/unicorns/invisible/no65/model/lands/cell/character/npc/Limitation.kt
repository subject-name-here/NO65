package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldLimitation
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Limitation(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 60
    override val nameId: Int
        get() = R.string.lands_limitation_name

    override val centerSymbol: String
        get() = "∰"
    override val centerSymbolColor
        get() = R.color.black
    override val faceCell: String
        get() = "\uD83E\uDDD0"

    override val emotion: Emotion
        get() = Emotion.SMALL_INTEREST

    private val place: Place = Place.TEMPLE
    override fun chillCheck() = place == Place.HUB

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldLimitation(),
            onAfterVictoryEvent = EventUnlockBattle(57)
        )

    override val speakEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) { R.string.lands_limitation_0_1 }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_limitation_1_1,
                    R.string.lands_limitation_1_2,
                    R.string.lands_limitation_1_3,
                    R.string.lands_limitation_1_4,
                    R.string.lands_limitation_1_5,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_limitation_2_1,
                    R.string.lands_limitation_2_2,
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_limitation_3_1,
                    R.string.lands_limitation_3_2,
                )
            }
            else -> EventNPCSpeak(this) { R.string.lands_limitation_else_1 }
        }

    private var linesSaid = 0
    override val chillEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_limitation_chill_0_1
                1 -> R.string.lands_limitation_chill_1_1
                2 -> R.string.lands_limitation_chill_2_1
                3 -> R.string.lands_limitation_chill_3_1
                4 -> R.string.lands_limitation_chill_4_1
                else -> R.string.lands_limitation_chill_else_1
            }
        }

    enum class Place {
        HUB,
        TEMPLE
    }
}