package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheWanderer
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.battle.victory.EventTheWandererAfterVictory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTheWandererStealsMoneys
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.util.CellUtils
import kotlinx.serialization.Serializable

@Serializable
class TheWanderer(override var cellBelow: Cell): CellNPCStandard(), CellControl {
    override val nameId: Int
        get() = R.string.lands_the_wanderer_name
    override val id: Int
        get() = 56

    override val faceCell: String
        get() = "\uD83D\uDE1C"
    override val centerSymbol: String
        get() = "\uD83D\uDD13"
    override val centerSymbolColor: Int
        get() = R.color.almost_black
    override val emotion: Emotion
        get() = Emotion.HYPOCRISY

    var state: State = State.SEC_2
    private var sec2EventFired = false
    private val sec2Event: Event
        get() = if (sec2EventFired) {
            Event.Null
        } else {
            sec2EventFired = true
            EventTheWandererStealsMoneys(this)
        }

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldTheWanderer(state),
            onAfterVictoryEvent = EventTheWandererAfterVictory(coordinates)
                .then(EventUnlockBattle(36))
        )

    private var linesSaid = 0
    override val speakEvent: Event
        get() = EventFactory.createWithNext { manager ->
            when (state) {
                State.SEC_2 -> {
                    if (manager.gameState is GameState65 && manager.gameState.protagonist.moneys > 0) {
                        EventNPCSpeak(this@TheWanderer) { R.string.lands_the_wanderer_sec2_come_closer_1 }
                    } else {
                        EventNPCSpeak(this@TheWanderer) { R.string.lands_the_wanderer_sec2_no_moneys_1 }
                    }
                }
                State.KING_OF_CLUBS -> when (linesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@TheWanderer, isSkippable = true) {
                        listOf(
                            R.string.lands_the_wanderer_koc_0_1,
                            R.string.lands_the_wanderer_koc_0_2,
                            R.string.lands_the_wanderer_koc_0_3,
                            R.string.lands_the_wanderer_koc_0_4,
                            R.string.lands_the_wanderer_koc_0_5,
                            R.string.lands_the_wanderer_koc_0_6,
                            R.string.lands_the_wanderer_koc_0_7,
                            R.string.lands_the_wanderer_koc_0_8,
                            R.string.lands_the_wanderer_koc_0_9,
                        )
                    }
                    else -> EventNPCSpeak(this@TheWanderer) { R.string.lands_the_wanderer_koc_else_1 }
                }
            }
        }

    override fun onTickWithEvent(tick: Int): Event {
        return EventFactory.createWithNext { manager ->
            val stateReq = state == State.SEC_2
            val distReq = CellUtils.distanceToProtagonist(manager, coordinates) == 1
            val moneysReq = manager.gameState is GameState65 && manager.gameState.protagonist.moneys > 0
            if (stateReq && distReq && moneysReq) {
                sec2Event
            } else {
                Event.Null
            }
        }
    }

    enum class State {
        SEC_2,
        KING_OF_CLUBS
    }
}