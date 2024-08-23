package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldRetreat
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.battle.victory.EventRetreatAfterVictory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventRetreatCheckShipIsReady
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Retreat(override var cellBelow: Cell): CellNPCStandard(), CellControl {
    override val nameId: Int
        get() = R.string.lands_retreat_name
    override val id: Int
        get() = 33

    override val faceCell: String
        get() = "\uD83D\uDC7E"
    override val centerSymbol: String
        get() = "\uD835\uDEBA"
    override val centerSymbolColor: Int
        get() = R.color.dark_blue
    override val emotion: Emotion
        get() = Emotion.HOSTILITY

    private var linesSaid = 0
    override val speakEvent
        get() = if (leaveEventFired) {
            EventNPCSpeak(this) {
                R.string.lands_retreat_after_ship_ready_1
            }
        } else {
            when (linesSaid++) {
                0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_retreat_0_1,
                        R.string.lands_retreat_0_2,
                        R.string.lands_retreat_0_3,
                    )
                }
                1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_retreat_1_1,
                        R.string.lands_retreat_1_2,
                        R.string.lands_retreat_1_3,
                    )
                }
                2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_retreat_2_1
                    )
                }
                else -> EventNPCSpeak(this) {
                    R.string.lands_retreat_else_1
                }
            }
        }

    var leaveEventFired = false
    override fun onTickWithEvent(tick: Int): Event = EventRetreatCheckShipIsReady(this)

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldRetreat(),
            onAfterVictoryEvent = EventRetreatAfterVictory().then(EventUnlockBattle(25))
        )
}