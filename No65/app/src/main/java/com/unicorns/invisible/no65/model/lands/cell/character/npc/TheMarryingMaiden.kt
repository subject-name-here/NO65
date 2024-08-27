package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_MARRYING_MAIDEN_LEFT_THE_PARTY
import com.unicorns.invisible.no65.model.lands.cell.*
import com.unicorns.invisible.no65.model.lands.cell.decor.DecorWeb
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.*
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowMessages
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventTheMarryingMaidenIncreaseCounter
import com.unicorns.invisible.no65.model.lands.event.events.util.EventTheMarryingMaidenKilled
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_MM_ENCOUNTERED_COUNTER
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class TheMarryingMaiden(override var cellBelow: Cell): CellNPCStandard(), CellControl, CellCompanion {
    override val id: Int
        get() = 54
    override val nameId: Int
        get() = R.string.lands_the_marrying_maiden_name

    override var rotation: Float = 0f

    var companionState = CompanionState.BEFORE_COMPANIONSHIP
    var emotionState = EmotionState.DETERMINED
    var heartState = HeartState.NORMAL
    override val centerSymbol: String
        get() = when (heartState) {
            HeartState.NORMAL -> " <3 "
            HeartState.BROKEN -> " </3 "
        }
    override val centerSymbolColor
        get() = R.color.black
    override val faceCell: String
        get() = when (emotionState) {
            EmotionState.GLAD -> "\uD83D\uDE38"
            EmotionState.LAUGHING -> "\uD83D\uDE39"
            EmotionState.SMILING -> "\uD83D\uDE3A"
            EmotionState.LOVE -> "\uD83D\uDE3B"
            EmotionState.DETERMINED -> "\uD83D\uDE3C"
            EmotionState.KISS -> "\uD83D\uDE3D"
            EmotionState.SADNESS -> "\uD83D\uDE3E"
            EmotionState.CRYING -> "\uD83D\uDE3F"
            EmotionState.DISAPPOINTMENT -> "\uD83D\uDE40"
        }

    override val emotion: Emotion
        get() = when (emotionState) {
            EmotionState.GLAD -> Emotion.FRIENDLINESS
            EmotionState.LAUGHING -> Emotion.FRIENDLINESS
            EmotionState.SMILING -> Emotion.FRIENDLINESS
            EmotionState.LOVE -> Emotion.FRIENDLINESS
            EmotionState.DETERMINED -> Emotion.BRAVERY
            EmotionState.KISS -> Emotion.ENERGIZED
            EmotionState.SADNESS -> Emotion.FEAR
            EmotionState.CRYING -> Emotion.FEAR
            EmotionState.DISAPPOINTMENT -> Emotion.SMALL_INTEREST
        }

    var autoFollow = false

    private val swapEvent: Event
        get() = EventNPCSpeak(this) {
            R.string.lands_the_marrying_maiden_swap_1
        }.then(EventFactory.create { manager ->
            autoFollow = false
            val protagonistCoordinates = manager.gameState.protagonist.coordinates
            manager.gameState.currentMap.removeCellFromTop(this@TheMarryingMaiden)
            manager.gameState.currentMap.moveTo(manager.gameState.protagonist, coordinates)
            manager.gameState.currentMap.addCellOnTop(this@TheMarryingMaiden, protagonistCoordinates)
            autoFollow = true
        })

    override val speakEvent: Event
        get() = EventFactory.createWithNext { _ ->
            if (companionState == CompanionState.AFTER_COMPANIONSHIP) {
                EventNPCSpeak(this@TheMarryingMaiden) {
                    R.string.lands_the_marrying_maiden_speak_after_companionship_1
                }
            } else {
                swapEvent
            }
        }

    override val attackEvent: Event
        get() = when (companionState) {
            CompanionState.BEFORE_COMPANIONSHIP,
            CompanionState.DURING_COMPANIONSHIP -> EventShowMessages(listOf(
                R.string.lands_the_marrying_maiden_attack_1
            ))
            CompanionState.AFTER_COMPANIONSHIP -> EventTheMarryingMaidenBackstab(this).then(boutEvent)
        }

    private val boutEvent: Event
        get() = EventAttack(
            this@TheMarryingMaiden,
            BattleFieldTheMarryingMaiden(),
            onAfterVictoryEvent = EventTheMarryingMaidenKilled().then(EventUnlockBattle(52))
        )

    override fun onProtagonistMoveOnDelta(delta: Coordinates): Event {
        return EventFactory.create { manager ->
            if (!autoFollow) {
                return@create
            }
            val protagonistCoordinates = manager.gameState.protagonist.coordinates
            val newCoordinates = protagonistCoordinates - delta.sign()
            val opposingCell = manager.gameState.currentMap.getTopCell(newCoordinates)
            val isPassable = opposingCell is CellPassable || opposingCell is CellSemiStatic && opposingCell.isPassable()
            val isTeleport = opposingCell is TeleportCell
            if (isPassable && !isTeleport) {
                manager.gameState.currentMap.moveTo(this@TheMarryingMaiden, newCoordinates)
            } else {
                val freeFloorCoordinates = CellUtils.findCurrentMapClosestFloor(manager, manager.gameState.protagonist)
                if (freeFloorCoordinates != null) {
                    manager.gameState.currentMap.moveTo(this@TheMarryingMaiden, freeFloorCoordinates)
                }
            }
        }
    }

    override fun onCurrentMapChange(prevIndex: String): Event {
        return EventFactory.create lambda@ { manager ->
            with(manager.gameState) {
                val freeFloorCoordinates = CellUtils.findCurrentMapClosestFloor(manager, protagonist) ?: return@lambda
                mapGraph.getMap(prevIndex).removeCellFromTop(this@TheMarryingMaiden)
                currentMap.addCellOnTop(this@TheMarryingMaiden, freeFloorCoordinates)

                if (currentMapIndex == "map_sec1") {
                    currentMap.moveTo(this@TheMarryingMaiden, Coordinates(0, 1))
                }
            }
        }
    }

    private var visitedHome = false
    private var webClearedEventFired = false
    private var hadSeriousTalk = false
    override fun onTickWithEvent(tick: Int): Event {
        return EventFactory.createWithNext { manager ->
            when (manager.gameState.currentMapIndex) {
                "map_str_44" -> {
                    when {
                        !visitedHome && checkIfWebIsCleared(manager) -> {
                            visitedHome = true
                            webClearedEventFired = true
                            EventTheMarryingMaidenHome(
                                this@TheMarryingMaiden,
                                isVisit = true,
                                isClean = true
                            )
                        }
                        !visitedHome && !checkIfWebIsCleared(manager) -> {
                            visitedHome = true
                            EventTheMarryingMaidenHome(
                                this@TheMarryingMaiden,
                                isVisit = true,
                                isClean = false
                            )
                        }
                        !webClearedEventFired && checkIfWebIsCleared(manager) -> {
                            webClearedEventFired = true
                            EventTheMarryingMaidenHome(
                                this@TheMarryingMaiden,
                                isVisit = false,
                                isClean = true
                            )
                        }

                        else -> Event.Null
                    }
                }
                "map_mpt" -> {
                    if (!hadSeriousTalk) {
                        hadSeriousTalk = true
                        EventTheMarryingMaidenSeriousTalk(this@TheMarryingMaiden)
                    } else {
                        Event.Null
                    }
                }

                else -> Event.Null
            }
        }
    }
    private fun checkIfWebIsCleared(manager: LandsManager): Boolean {
        val webs = manager.gameState.currentMap.getTopCells().filterIsInstance<DecorWeb>()
        return webs.isEmpty()
    }

    override fun isCompanion(): Boolean = companionState == CompanionState.DURING_COMPANIONSHIP

    @Transient
    private var cutsceneStarted = false
    override fun onSight(distanceToProtagonist: Int): Event {
        return EventFactory.createWithNext { manager ->
            if (manager.gameState.currentMap.getTopCells().none { it is TheMarryingMaiden }) {
                return@createWithNext Event.Null
            }
            val brokeUp = THE_MARRYING_MAIDEN_LEFT_THE_PARTY in manager.gameState.flagsMaster
            if (brokeUp && !cutsceneStarted) {
                cutsceneStarted = true
                val encCounter = GlobalState.getInt(manager.activity, THE_MM_ENCOUNTERED_COUNTER)
                if (encCounter >= ENCOUNTERS_TO_LET_GO) {
                    EventTheMarryingMaidenLetsGo(this@TheMarryingMaiden)
                } else {
                    EventTheMarryingMaidenBout(this@TheMarryingMaiden)
                        .then(EventTheMarryingMaidenIncreaseCounter())
                        .then(boutEvent)
                }
            } else {
                Event.Null
            }
        }
    }

    enum class EmotionState {
        GLAD,
        LAUGHING,
        SMILING,
        LOVE,
        DETERMINED,
        KISS,
        SADNESS,
        CRYING,
        DISAPPOINTMENT
    }

    enum class HeartState {
        NORMAL, BROKEN
    }

    enum class CompanionState {
        BEFORE_COMPANIONSHIP,
        DURING_COMPANIONSHIP,
        AFTER_COMPANIONSHIP
    }

    companion object {
        const val ENCOUNTERS_TO_LET_GO = 4
    }
}