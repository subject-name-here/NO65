package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldMouthCorners
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_MARRYING_MAIDEN_LEFT_THE_PARTY
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class MouthCorners(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 27
    override val nameId: Int
        get() = R.string.lands_mouth_corners_name

    var state = State.NORMAL
    override val centerSymbol: String
        get() = "\uD83D\uDC8C"
    override val centerSymbolColor
        get() = R.color.dark_red
    override val faceCell: String
        get() = when (state) {
            State.NORMAL -> "\uD83D\uDE18"
            State.KISSING_CLOSED_EYES -> "\uD83D\uDE19"
            State.KISSING_OPEN_EYES -> "\uD83D\uDE17"
        }

    override val emotion: Emotion
        get() = when (state) {
            State.NORMAL -> Emotion.HYPOCRISY
            State.KISSING_CLOSED_EYES -> Emotion.CALMNESS
            State.KISSING_OPEN_EYES -> Emotion.CALMNESS
        }

    override val attackEvent: Event
        get() = EventFactory.createWithNext { manager ->
            val hasBrokeHeart = THE_MARRYING_MAIDEN_LEFT_THE_PARTY in manager.gameState.flagsMaster
            EventAttack(
                this@MouthCorners,
                BattleFieldMouthCorners(hasBrokeHeart),
                onAfterVictoryEvent = EventUnlockBattle(41)
            )
        }

    private var linesSaid = 0
    override val speakEvent: Event
        get() = EventFactory.createWithNext { manager ->
            val hasBrokeHeart = THE_MARRYING_MAIDEN_LEFT_THE_PARTY in manager.gameState.flagsMaster
            if (!hasBrokeHeart) {
                when (linesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@MouthCorners, isSkippable = true) {
                        listOf(
                            R.string.lands_mouth_corners_0_1,
                            R.string.lands_mouth_corners_0_2,
                            R.string.lands_mouth_corners_0_3,
                            R.string.lands_mouth_corners_0_4,
                        )
                    }
                    1 -> EventNPCSpeakCutscene(this@MouthCorners, isSkippable = true) {
                        listOf(
                            R.string.lands_mouth_corners_1_1,
                            R.string.lands_mouth_corners_1_2,
                            R.string.lands_mouth_corners_1_3,
                        )
                    }
                    else -> EventNPCSpeak(this@MouthCorners) { R.string.lands_mouth_corners_else_1 }
                }
            } else {
                when (linesSaid++) {
                    0 -> EventNPCSpeakCutscene(this@MouthCorners, isSkippable = true) {
                        listOf(
                            R.string.lands_mouth_corners_broke_heart_0_1,
                            R.string.lands_mouth_corners_broke_heart_0_2,
                            R.string.lands_mouth_corners_broke_heart_0_3,
                        )
                    }
                    1 -> EventNPCSpeakCutscene(this@MouthCorners, isSkippable = true) {
                        listOf(
                            R.string.lands_mouth_corners_broke_heart_1_1,
                            R.string.lands_mouth_corners_broke_heart_1_2,
                            R.string.lands_mouth_corners_broke_heart_1_3,
                        )
                    }
                    2 -> EventNPCSpeakCutscene(this@MouthCorners, isSkippable = true) {
                        listOf(
                            R.string.lands_mouth_corners_broke_heart_2_1,
                            R.string.lands_mouth_corners_broke_heart_2_2,
                        )
                    }
                    else -> EventNPCSpeak(this@MouthCorners) { R.string.lands_mouth_corners_broke_heart_else_1 }
                }
            }
        }

    enum class State {
        NORMAL,
        KISSING_CLOSED_EYES,
        KISSING_OPEN_EYES,
    }
}