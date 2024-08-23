package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldStandstill
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Standstill(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 12
    override val nameId: Int
        get() = R.string.lands_standstill_name

    override val centerSymbol: String
        get() = "\uD83C\uDF39"
    override val centerSymbolColor
        get() = R.color.red

    var state = State.HOSTILE
    override fun chillCheck() = state == State.CHILL

    override val faceCell: String
        get() = if (chillCheck()) "\uD83D\uDE0A" else "\uD83D\uDE11"

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.JOY else Emotion.BRAVERY

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldStandstill(),
        onAfterVictoryEvent = EventUnlockBattle(33)
    )

    private var linesSaid = 0
    override val speakEvent: Event
        get() = EventFactory.createWithNext { manager ->
            val returnCells = manager.gameState.currentMap.getTopCells().filterIsInstance<Return>()
            if (returnCells.isEmpty()) {
                EventNPCSpeakCutscene(this@Standstill, isSkippable = true) {
                    listOf(
                        R.string.lands_standstill_on_return_death_1,
                        R.string.lands_standstill_on_return_death_2,
                        R.string.lands_standstill_on_return_death_3,
                        R.string.lands_standstill_on_return_death_4,
                    )
                }.then(attackEvent)
            } else {
                when (linesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@Standstill, isSkippable = true) {
                        listOf(
                            R.string.lands_standstill_0_1,
                            R.string.lands_standstill_0_2,
                            R.string.lands_standstill_0_3,
                        )
                    }
                    1 -> EventNPCSpeakCutscene(this@Standstill, isSkippable = true) {
                        listOf(
                            R.string.lands_standstill_1_1,
                            R.string.lands_standstill_1_2,
                            R.string.lands_standstill_1_3,
                            R.string.lands_standstill_1_4,
                        )
                    }
                    else -> EventNPCSpeak(this@Standstill) {
                        R.string.lands_standstill_else_1
                    }
                }
            }
        }

    override val chillEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_standstill_chill_0_1
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_standstill_chill_1_1,
                    R.string.lands_standstill_chill_1_2,
                )
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_standstill_chill_2_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_standstill_chill_else_1
            }
        }

    enum class State {
        CHILL,
        HOSTILE
    }
}