package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGatheringTogether
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class GatheringTogether(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 45
    override val nameId: Int
        get() = R.string.lands_gathering_together_name

    override val centerSymbol: String
        get() = "\uD83C\uDF8A"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDE00"

    override val emotion: Emotion
        get() = Emotion.JOY

    private val place: Place = Place.SEC_4_HOME
    override fun chillCheck() = place == Place.HUB

    private var linesSaid = 0
    override val chillEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_gathering_together_chill_0_1,
                    R.string.lands_gathering_together_chill_0_2,
                    R.string.lands_gathering_together_chill_0_3,
                )
            }
            1 -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_chill_1_1
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_chill_2_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_chill_else_1
            }
        }

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldGatheringTogether(),
            onAfterVictoryEvent = EventUnlockBattle(20)
        )

    private var speakLinesSaid = 0
    override val speakEvent: Event
        get() = when (speakLinesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_0_1
            }
            1 -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_1_1
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_2_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_gathering_together_else_1
            }
        }

    enum class Place {
        HUB,
        SEC_4_HOME,
    }
}