package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGreatPossession
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.COMING_TO_MEET_DEAD
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.DELIVERANCE_BARGAIN_STARTED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.D_NINETY_NINE_DEFEATED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GREAT_POSSESSION_GAVE_MONEY
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MODESTY_DEAD
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.battle.victory.EventGreatPossessionAfterVictory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventGreatPossessionGivesMoney
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventGreatPossessionWelcome
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randBooleanPercent
import kotlinx.serialization.Serializable

@Serializable
class GreatPossession(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 14
    override val nameId: Int
        get() = R.string.lands_great_possession_name

    override val centerSymbol: String
        get() = "\uD83D\uDCB8"
    override val centerSymbolColor
        get() = R.color.green

    var state = State.CITY_AWAITING_CLIENT
    override fun chillCheck() = state == State.HUB_IGNORES

    override val faceCell: String
        get() = when (state) {
            State.CITY_AWAITING_CLIENT -> "\uD83D\uDE0F"
            else -> "\uD83D\uDE11"
        }

    override val emotion: Emotion
        get() = when (state) {
            State.CITY_AWAITING_CLIENT -> Emotion.FRIENDLINESS
            else -> Emotion.INDIFFERENCE
        }

    private fun getAttackEvent(hasAttackedFirst: Boolean): Event {
        return EventFactory.createWithNext { manager ->
            if (manager.gameState !is GameState65) return@createWithNext Event.Null
            val moneys = manager.gameState.protagonist.moneys
            val flags = manager.gameState.flagsMaster
            val beforeNinetyNine = D_NINETY_NINE_DEFEATED !in flags &&
                    COMING_TO_MEET_DEAD !in flags &&
                    MODESTY_DEAD !in flags &&
                    (moneys == 5 || moneys == 4 && GREAT_POSSESSION_GAVE_MONEY in flags)
            EventAttack(
                this@GreatPossession,
                BattleFieldGreatPossession(hasAttackedFirst, beforeNinetyNine),
                onAfterVictoryEvent = EventGreatPossessionAfterVictory(coordinates)
                    .then(EventUnlockBattle(48))
            )
        }
    }
    override val attackEvent
        get() = getAttackEvent(false)

    private var linesSaid = 0
    private var beforeNinetyNineLineSaid = false
    override val speakEvent: Event
        get() = EventFactory.createWithNext { manager ->
            if (manager.gameState !is GameState65) return@createWithNext Event.Null
            val moneys = manager.gameState.protagonist.moneys
            val beforeNinetyNine = D_NINETY_NINE_DEFEATED !in manager.gameState.flagsMaster &&
                    COMING_TO_MEET_DEAD !in manager.gameState.flagsMaster &&
                    MODESTY_DEAD !in manager.gameState.flagsMaster &&
                    moneys == 4
            val afterNinetyNine = D_NINETY_NINE_DEFEATED in manager.gameState.flagsMaster
            when {
                afterNinetyNine -> {
                    EventNPCSpeakCutscene(this@GreatPossession, isSkippable = true) {
                        listOf(
                            R.string.lands_great_possession_after_d99_1,
                            R.string.lands_great_possession_after_d99_2,
                            R.string.lands_great_possession_after_d99_3,
                            R.string.lands_great_possession_after_d99_4,
                            R.string.lands_great_possession_after_d99_5,
                            R.string.lands_great_possession_after_d99_6,
                            R.string.lands_great_possession_after_d99_7,
                            R.string.lands_great_possession_after_d99_8,
                            R.string.lands_great_possession_after_d99_9,
                            R.string.lands_great_possession_after_d99_10,
                        )
                    }.then(getAttackEvent(hasAttackedFirst = true))
                }
                moneys == 0 -> {
                    EventNPCSpeak(this@GreatPossession) {
                        R.string.lands_great_possession_no_money_1
                    }
                }
                beforeNinetyNine && !beforeNinetyNineLineSaid && linesSaid > 0 -> {
                    beforeNinetyNineLineSaid = true
                    EventGreatPossessionGivesMoney(this@GreatPossession)
                }
                else -> when (linesSaid++) {
                    0 -> EventGreatPossessionWelcome(this@GreatPossession)
                    else -> EventNPCSpeak(this@GreatPossession) { R.string.lands_great_possession_after_welcome_1 }
                }
            }
        }

    var toldAboutRobot = false
    var himLineSaid = false
    override val chillEvent: Event
        get() = EventFactory.createWithNext { manager ->
            val spokeWithRobot = DELIVERANCE_BARGAIN_STARTED in manager.gameState.flagsMaster
            val message = if (spokeWithRobot && !toldAboutRobot) {
                toldAboutRobot = true
                R.string.lands_great_possession_chill_about_robot_1
            } else if (!himLineSaid && randBoolean()) {
                himLineSaid = true
                R.string.lands_great_possession_chill_about_him_1
            } else {
                R.string.lands_great_possession_chill_else_1
            }
            EventNPCSpeak(this@GreatPossession) { message }
        }

    enum class State {
        HUB_IGNORES,
        CITY_AWAITING_CLIENT,
        CITY_KNOWS_BANNERMAN
    }
}