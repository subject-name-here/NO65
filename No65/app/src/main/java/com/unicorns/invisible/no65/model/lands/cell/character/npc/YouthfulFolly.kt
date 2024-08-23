package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldYouthfulFolly
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class YouthfulFolly(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 4
    override val nameId: Int
        get() = R.string.lands_youthful_folly_name

    override val centerSymbol: String
        get() = "\uD83C\uDFC4"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDE00"

    override val emotion: Emotion
        get() = Emotion.ENERGIZED

    private val place = Place.SEC_2_BAR
    override fun chillCheck() = place == Place.HUB

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldYouthfulFolly(),
        onAfterVictoryEvent = EventUnlockBattle(34)
    )

    private var linesSaid = 0
    override val chillEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_youthful_folly_chill_0_1,
                    R.string.lands_youthful_folly_chill_0_2,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_youthful_folly_chill_1_1,
                    R.string.lands_youthful_folly_chill_1_2,
                )
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_youthful_folly_chill_2_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_youthful_folly_chill_else_1
            }
        }

    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_youthful_folly_0_1,
                    R.string.lands_youthful_folly_0_2,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_youthful_folly_1_1,
                    R.string.lands_youthful_folly_1_2,
                )
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_youthful_folly_2_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_youthful_folly_else_1
            }
        }

    enum class Place {
        HUB,
        SEC_2_BAR,
    }
}