package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldContemplation
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventContemplationKilled
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CONTEMPLATION_ENCOUNTERED_COUNTER
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CONTEMPLATION_WAS_KILLED
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.serialization.Serializable

@Serializable
class Contemplation(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 20
    override val nameId: Int
        get() = R.string.lands_contemplation_name

    override val centerSymbol: String
        get() = "\uD83D\uDD2B"
    override val centerSymbolColor
        get() = R.color.almost_black
    override val faceCell: String
        get() = "\uD83D\uDD75"

    override val emotion: Emotion = Emotion.LAW

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldContemplation(),
            onAfterVictoryEvent = EventContemplationKilled().then(EventUnlockBattle(31))
        )

    override val speakEvent: Event
        get() = EventNPCSpeak(this) { R.string.lands_contemplation_speak }

    private var eventFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        return if (eventFired) {
            Event.Null
        } else {
            eventFired = true
            EventFactory.createWithNext lambda@ { manager ->
                if (GlobalState.getBoolean(manager.activity, CONTEMPLATION_WAS_KILLED)) {
                    return@lambda EventNPCSpeakCutscene(this@Contemplation) {
                        listOf(
                            R.string.lands_contemplation_meta_dead_1,
                            R.string.lands_contemplation_meta_dead_2,
                            R.string.lands_contemplation_meta_dead_3,
                        )
                    }
                }
                val timesFired = GlobalState.getInt(manager.activity, CONTEMPLATION_ENCOUNTERED_COUNTER, 0)
                GlobalState.putInt(manager.activity, CONTEMPLATION_ENCOUNTERED_COUNTER, timesFired + 1)
                when (timesFired) {
                    0 -> {
                        EventNPCSpeakCutscene(this@Contemplation) {
                            listOf(
                                R.string.lands_contemplation_meta_0_1,
                                R.string.lands_contemplation_meta_0_2,
                                R.string.lands_contemplation_meta_0_3,
                                R.string.lands_contemplation_meta_0_4,
                                R.string.lands_contemplation_meta_0_5,
                                R.string.lands_contemplation_meta_0_6,
                                R.string.lands_contemplation_meta_0_7,
                                R.string.lands_contemplation_meta_0_8,
                                R.string.lands_contemplation_meta_0_9,
                                R.string.lands_contemplation_meta_0_10,
                            )
                        }.then(attackEvent)
                    }
                    1 -> {
                        EventNPCSpeakCutscene(this@Contemplation) {
                            listOf(
                                R.string.lands_contemplation_meta_1_1,
                                R.string.lands_contemplation_meta_1_2,
                                R.string.lands_contemplation_meta_1_3,
                            )
                        }.then(attackEvent)
                    }
                    2 -> {
                        EventNPCSpeakCutscene(this@Contemplation) {
                            listOf(
                                R.string.lands_contemplation_meta_2_1,
                                R.string.lands_contemplation_meta_2_2,
                            )
                        }.then(attackEvent)
                    }
                    3 -> {
                        EventNPCSpeakCutscene(this@Contemplation) {
                            listOf(
                                R.string.lands_contemplation_meta_3_1,
                                R.string.lands_contemplation_meta_3_2,
                                R.string.lands_contemplation_meta_3_3,
                                R.string.lands_contemplation_meta_3_4,
                                R.string.lands_contemplation_meta_3_5,
                            )
                        }.then(attackEvent)
                    }
                    4 -> {
                        EventNPCSpeakCutscene(this@Contemplation) {
                            listOf(
                                R.string.lands_contemplation_meta_4_1,
                                R.string.lands_contemplation_meta_4_2,
                                R.string.lands_contemplation_meta_4_3,
                                R.string.lands_contemplation_meta_4_4,
                                R.string.lands_contemplation_meta_4_5,
                                R.string.lands_contemplation_meta_4_6,
                                R.string.lands_contemplation_meta_4_7,
                                R.string.lands_contemplation_meta_4_8,
                                R.string.lands_contemplation_meta_4_9,
                                R.string.lands_contemplation_meta_4_10,
                                R.string.lands_contemplation_meta_4_11,
                                R.string.lands_contemplation_meta_4_12,
                                R.string.lands_contemplation_meta_4_13,
                            )
                        }
                    }
                    else -> Event.Null
                }
            }
        }
    }
}