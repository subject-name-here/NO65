package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.MONEYS_STOLEN_BY_THE_WANDERER
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.COMING_TO_MEET_DEAD
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.DELIVERANCE_BARGAIN_STARTED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GREAT_POSSESSION_MONEY_ABSORBED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MODESTY_DEAD
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventDeliveranceEncourages
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventDeliveranceHimIntro
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventDeliveranceShowsAd
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventDeliveranceTakesAllYourMoneys
import com.unicorns.invisible.no65.model.lands.event.events.d99.EventD99Short
import com.unicorns.invisible.no65.model.lands.event.events.d99.EventPreD99
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventRemoveCell
import com.unicorns.invisible.no65.model.lands.event.events.util.EventSetPeaceMode
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.D_NINETY_NINE_SAVE
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


@Serializable
class Deliverance(override var cellBelow: Cell): CellNPC() {
    override val id: Int
        get() = 40
    override val nameId: Int
        get() = R.string.lands_deliverance_name

    override val centerSymbol: String
        get() = "\uD83D\uDCB8"
    override val centerSymbolColor
        get() = R.color.green
    override val faceCell: String
        get() = "\uD83E\uDD11"

    override val emotion: Emotion
        get() = Emotion.FRIENDLINESS

    override val speechSound: Int
        get() = R.raw.sfx_coin

    // a b d e g h i k l m n o p r s t u v z
    // ch -> tsh (tshois)
    // minus: c, f, j, q, w, x, y

    override fun use(): Event {
        return if (awaitsMoneys()) {
            resumeIfIsAwaiting(true)
            Event.Null
        } else {
            EventFactory.createWithNext { manager ->
                if (manager.gameState !is GameState65) return@createWithNext Event.Null
                when {
                    manager.gameState.battleMode == BattleMode.ATTACK -> {
                        attackEvent
                    }
                    place == Place.HUB -> {
                        chillEvent
                    }
                    else -> {
                        speakEvent
                    }
                }
            }
        }
    }

    private var adShown = false
    private val attackEvent: Event
        get() = when (place) {
            Place.HUB -> Event.Null
            Place.SEC4 -> {
                if (!adShown) {
                    adShown = true
                    EventDeliveranceShowsAd(this).then(EventSetPeaceMode())
                } else {
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_deliverance_sec4_attacked_1,
                            R.string.lands_deliverance_sec4_attacked_2,
                            R.string.lands_deliverance_sec4_attacked_3,
                            R.string.lands_deliverance_sec4_attacked_4,
                            R.string.lands_deliverance_sec4_attacked_5,
                            R.string.lands_deliverance_sec4_attacked_6,
                            R.string.lands_deliverance_sec4_attacked_7,
                            R.string.lands_deliverance_sec4_attacked_8,
                        )
                    }.then(EventRemoveCell(this)).then(EventSetPeaceMode())
                }
            }
            Place.GOODS_STREET -> EventNPCSpeakCutscene(this) {
                listOf(
                    R.string.lands_deliverance_attacked_1,
                    R.string.lands_deliverance_attacked_2,
                    R.string.lands_deliverance_attacked_3,
                    R.string.lands_deliverance_attacked_4,
                    R.string.lands_deliverance_attacked_5,
                    R.string.lands_deliverance_attacked_6,
                    R.string.lands_deliverance_attacked_7,
                )
            }.then(EventRemoveCell(this)).then(EventSetPeaceMode())
        }

    private val addFlagEvent
        get() = EventFactory.create { manager ->
            manager.gameState.flagsMaster.add(DELIVERANCE_BARGAIN_STARTED)
        }

    private val speakEvent: Event
        get() = when (place) {
            Place.HUB -> Event.Null
            Place.SEC4 -> speakSec4Event
            Place.GOODS_STREET -> speakGoodsStreetEvent
        }

    private var linesSec4Said = 0
    private val speakSec4Event
        get() = EventFactory.createWithNext { manager ->
            if (manager.gameState !is GameState65) return@createWithNext Event.Null

            when (linesSec4Said++) {
                0 -> EventNPCSpeak(this@Deliverance) {
                    R.string.lands_deliverance_sec4_0_1
                }
                1 -> {
                    val hasAffair = DELIVERANCE_BARGAIN_STARTED in manager.gameState.flagsMaster
                    if (hasAffair) {
                        val moneys = manager.gameState.protagonist.moneys
                        if (moneys > 0) {
                            EventNPCSpeakCutscene(this@Deliverance) {
                                listOf(
                                    R.string.lands_deliverance_sec4_1_met_1,
                                    R.string.lands_deliverance_sec4_1_met_2,
                                    R.string.lands_deliverance_sec4_1_met_rich_3,
                                    R.string.lands_deliverance_sec4_1_met_rich_4,
                                    R.string.lands_deliverance_sec4_1_met_rich_5,
                                    R.string.lands_deliverance_sec4_1_met_rich_6,
                                    R.string.lands_deliverance_sec4_1_met_rich_7,
                                )
                            }
                        } else {
                            EventNPCSpeakCutscene(this@Deliverance) {
                                listOf(
                                    R.string.lands_deliverance_sec4_1_met_1,
                                    R.string.lands_deliverance_sec4_1_met_2,
                                    R.string.lands_deliverance_sec4_1_met_poor_3,
                                    R.string.lands_deliverance_sec4_1_met_poor_4,
                                    R.string.lands_deliverance_sec4_1_met_poor_5,
                                    R.string.lands_deliverance_sec4_1_met_poor_6,
                                    R.string.lands_deliverance_sec4_1_met_poor_7,
                                    R.string.lands_deliverance_sec4_1_met_poor_8,
                                )
                            }
                        }
                    } else {
                        EventNPCSpeakCutscene(this@Deliverance) {
                            listOf(
                                R.string.lands_deliverance_sec4_1_new_1,
                                R.string.lands_deliverance_sec4_1_new_2,
                                R.string.lands_deliverance_sec4_1_new_3,
                                R.string.lands_deliverance_sec4_1_new_4,
                                R.string.lands_deliverance_sec4_1_new_5,
                                R.string.lands_deliverance_sec4_1_new_6,
                                R.string.lands_deliverance_sec4_1_new_7,
                                R.string.lands_deliverance_sec4_1_new_8,
                            )
                        }.then(addFlagEvent)
                    }
                }
                2 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_2_1,
                        R.string.lands_deliverance_sec4_2_2,
                        R.string.lands_deliverance_sec4_2_3,
                        R.string.lands_deliverance_sec4_2_4,
                        R.string.lands_deliverance_sec4_2_5,
                    )
                }
                3 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_3_1,
                        R.string.lands_deliverance_sec4_3_2,
                    )
                }
                4 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_4_1,
                        R.string.lands_deliverance_sec4_4_2,
                        R.string.lands_deliverance_sec4_4_3,
                    )
                }
                5 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_5_1,
                        R.string.lands_deliverance_sec4_5_2,
                        R.string.lands_deliverance_sec4_5_3,
                        R.string.lands_deliverance_sec4_5_4,
                    )
                }
                6 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_6_1,
                        R.string.lands_deliverance_sec4_6_2,
                    )
                }
                7 -> {
                    EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                        listOf(
                            R.string.lands_deliverance_sec4_7_1,
                            R.string.lands_deliverance_sec4_7_2,
                            R.string.lands_deliverance_sec4_7_3,
                            R.string.lands_deliverance_sec4_7_4,
                            R.string.lands_deliverance_sec4_7_5,
                            R.string.lands_deliverance_sec4_7_6,
                        )
                    }
                }
                8 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_8_1,
                        R.string.lands_deliverance_sec4_8_2,
                        R.string.lands_deliverance_sec4_8_3,
                    )
                }
                9 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_9_1,
                        R.string.lands_deliverance_sec4_9_2,
                        R.string.lands_deliverance_sec4_9_3,
                    )
                }
                10 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                    listOf(
                        R.string.lands_deliverance_sec4_10_1,
                        R.string.lands_deliverance_sec4_10_2,
                        R.string.lands_deliverance_sec4_10_3,
                    )
                }
                else -> EventNPCSpeak(this@Deliverance) {
                    R.string.lands_deliverance_sec4_else_1
                }
            }
        }

    private var linesSaid = 0
    private var wandererLinesSaid = 0
    @Transient
    private var cutsceneStarted = false
    private val speakGoodsStreetEvent: Event
        get() = EventFactory.createWithNext { manager ->
            if (manager.gameState !is GameState65) return@createWithNext Event.Null
            val moneys = manager.gameState.protagonist.moneys
            val flags = manager.gameState.flagsMaster
            val gpMoneyAcquired = GREAT_POSSESSION_MONEY_ABSORBED in flags
            val friendsAlive = COMING_TO_MEET_DEAD !in flags && MODESTY_DEAD !in flags
            val hasAffair = DELIVERANCE_BARGAIN_STARTED in manager.gameState.flagsMaster
            val wandererStoleMoneys = manager.gameState.countersMaster[MONEYS_STOLEN_BY_THE_WANDERER] > 0
            when {
                moneys >= 5 && friendsAlive -> {
                    cutsceneStarted = true
                    EventPreD99(this@Deliverance)
                }
                moneys >= 5 -> EventDeliveranceTakesAllYourMoneys(this@Deliverance, is5Moneys = true)
                moneys == 4 && !gpMoneyAcquired -> when (linesSaid++) {
                    0 -> EventDeliveranceEncourages(this@Deliverance)
                    else -> EventNPCSpeak(this@Deliverance) { R.string.lands_deliverance_encourage_else }
                }
                !gpMoneyAcquired && hasAffair -> when (linesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                        listOf(
                            R.string.lands_deliverance_less_money_has_affair_1,
                            if (moneys == 0) R.string.lands_deliverance_less_money_no_money else R.string.lands_deliverance_less_money_not_enough_money,
                            R.string.lands_deliverance_less_money_has_affair_3,
                            R.string.lands_deliverance_less_money_has_affair_4,
                            R.string.lands_deliverance_less_money_has_affair_5,
                            R.string.lands_deliverance_less_money_has_affair_6,
                        )
                    }
                    else -> EventNPCSpeak(this@Deliverance) { R.string.lands_deliverance_less_money_has_affair_else }
                }
                !gpMoneyAcquired && !hasAffair -> when (linesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                        listOf(
                            R.string.lands_deliverance_less_money_no_affair_1,
                            if (moneys == 0) R.string.lands_deliverance_less_money_no_money else R.string.lands_deliverance_less_money_not_enough_money,
                            R.string.lands_deliverance_less_money_no_affair_3,
                            R.string.lands_deliverance_less_money_no_affair_4,
                        )
                    }
                    else -> EventNPCSpeak(this@Deliverance) { R.string.lands_deliverance_less_money_no_affair_else }
                }
                wandererStoleMoneys -> when (wandererLinesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@Deliverance, isSkippable = true) {
                        listOf(
                            R.string.lands_deliverance_stolen_money_1,
                            R.string.lands_deliverance_stolen_money_2,
                            R.string.lands_deliverance_stolen_money_3,
                            R.string.lands_deliverance_stolen_money_4,
                            R.string.lands_deliverance_stolen_money_5,
                        )
                    }
                    else -> EventNPCSpeak(this@Deliverance) { R.string.lands_deliverance_stolen_money_else }
                }
                else -> EventDeliveranceTakesAllYourMoneys(this@Deliverance, is5Moneys = false)
            }
        }

    private var chillEventFired = false
    private var talkLaterLineSaid = false
    private var himEventFired = false
    private val chillEvent
        get() = if (!chillEventFired) {
            chillEventFired = true
            EventNPCSpeakCutscene(this@Deliverance) {
                listOf(
                    R.string.lands_deliverance_chill_meet_1,
                    R.string.lands_deliverance_chill_meet_2,
                    R.string.lands_deliverance_chill_meet_3,
                    R.string.lands_deliverance_chill_meet_4,
                    R.string.lands_deliverance_chill_meet_5,
                    R.string.lands_deliverance_chill_meet_6,
                    R.string.lands_deliverance_chill_meet_8,
                    R.string.lands_deliverance_chill_meet_9,
                    R.string.lands_deliverance_chill_meet_10,
                    R.string.lands_deliverance_chill_meet_11,
                )
            }.then(addFlagEvent)
        } else {
            EventFactory.createWithNext { manager ->
                val master = getMaster(manager)
                when {
                    master != null && master.toldAboutRobot -> {
                        EventNPCSpeak(master) {
                            R.string.lands_great_possession_touching_robot_1
                        }
                    }
                    master != null && master.himLineSaid && !himEventFired && !talkLaterLineSaid -> {
                        himEventFired = true
                        EventDeliveranceHimIntro(this@Deliverance, master)
                    }
                    !talkLaterLineSaid -> {
                        talkLaterLineSaid = true
                        EventNPCSpeak(this@Deliverance) {
                            R.string.lands_deliverance_chill_on_duty_1
                        }
                    }
                    else -> {
                        EventNPCSpeak(this@Deliverance) {
                            R.string.lands_deliverance_chill_else_1
                        }
                    }
                }
            }
        }
    private fun getMaster(manager: LandsManager): GreatPossession? {
        val cells = manager.gameState.currentMap.getTopCells()
        return cells.filterIsInstance<GreatPossession>().firstOrNull()
    }

    override fun onSight(distanceToProtagonist: Int): Event {
        val placeReq = place == Place.GOODS_STREET
        return if (placeReq && !cutsceneStarted) {
            EventFactory.createWithNext { manager ->
                if (manager.gameState !is GameState65) return@createWithNext Event.Null

                val hasSave = GlobalState.getBoolean(manager.activity, D_NINETY_NINE_SAVE)
                val moneyReq = manager.gameState.protagonist.moneys == 5
                if (hasSave && moneyReq && !cutsceneStarted) {
                    cutsceneStarted = true
                    EventD99Short()
                } else {
                    Event.Null
                }
            }
        } else {
            Event.Null
        }
    }

    @Transient
    var awaitingUseContinuation: Continuation<Boolean>? = null
    @Transient
    private val lock = ReentrantLock()
    override fun isUsableDuringCutscene() = awaitsMoneys()
    private fun awaitsMoneys(): Boolean {
        return awaitingUseContinuation != null
    }
    fun resumeIfIsAwaiting(value: Boolean) {
        lock.withLock {
            awaitingUseContinuation?.resume(value)
            awaitingUseContinuation = null
        }
    }

    var place = Place.SEC4
    enum class Place {
        HUB,
        SEC4,
        GOODS_STREET
    }
}