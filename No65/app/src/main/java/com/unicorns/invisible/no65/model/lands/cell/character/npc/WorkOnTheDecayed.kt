package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldWorkOnTheDecayed
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class WorkOnTheDecayed(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 18
    override val nameId: Int
        get() = R.string.lands_work_on_the_decayed_name

    override val centerSymbol: String
        get() = "☝"
    override val centerSymbolColor
        get() = R.color.true_green
    override val faceCell: String
        get() = "\uD83E\uDD13"

    override val emotion: Emotion
        get() = Emotion.HYPOCRISY

    private val state: State = State.DOOMED
    override fun chillCheck() = state == State.CHILL

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldWorkOnTheDecayed(),
            onAfterVictoryEvent = EventUnlockBattle(27)
        )

    private var linesSaid = 0
    override val chillEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_work_on_the_decayed_chill_0_1
            }
            1 -> EventNPCSpeak(this) {
                R.string.lands_work_on_the_decayed_chill_1_1
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_work_on_the_decayed_chill_2_1,
                    R.string.lands_work_on_the_decayed_chill_2_2,
                    R.string.lands_work_on_the_decayed_chill_2_3,
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_work_on_the_decayed_chill_3_1,
                    R.string.lands_work_on_the_decayed_chill_3_2,
                    R.string.lands_work_on_the_decayed_chill_3_3,
                    R.string.lands_work_on_the_decayed_chill_3_4,
                )
            }
            4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_work_on_the_decayed_chill_4_1,
                    R.string.lands_work_on_the_decayed_chill_4_2,
                )
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_work_on_the_decayed_chill_else_1
            }
        }

    private var speakLinesSaid = 0
    override val speakEvent: Event
        get() = when (speakLinesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_work_on_the_decayed_0_1,
                    R.string.lands_work_on_the_decayed_0_2,
                    R.string.lands_work_on_the_decayed_0_3,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_work_on_the_decayed_1_1,
                    R.string.lands_work_on_the_decayed_1_2,
                    R.string.lands_work_on_the_decayed_1_3,
                    R.string.lands_work_on_the_decayed_1_4,
                    R.string.lands_work_on_the_decayed_1_5,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_work_on_the_decayed_2_1,
                    R.string.lands_work_on_the_decayed_2_2,
                    R.string.lands_work_on_the_decayed_2_3,
                    R.string.lands_work_on_the_decayed_2_4,
                    R.string.lands_work_on_the_decayed_2_5,
                )
            }
            5 -> EventNPCSpeak(this) {
                R.string.lands_work_on_the_decayed_5_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_work_on_the_decayed_else_1
            }
        }

    enum class State {
        CHILL,
        DOOMED,
    }
}