package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDarkeningOfTheLight
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_WANDERER_STOLE_MONEYS
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTMMAndDOTL
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTMMAndDOTL2
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class DarkeningOfTheLight(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 36
    override val nameId: Int
        get() = R.string.lands_darkening_of_the_light_name

    override val centerSymbol: String
        get() = "\uD83E\uDD46"
    override val centerSymbolColor
        get() = R.color.brown
    override val faceCell: String
        get() = "\uD83E\uDD20"

    override val emotion: Emotion = Emotion.LAW

    override val attackEvent: Event
        get() = EventAttack(
            this,
            BattleFieldDarkeningOfTheLight(),
            onAfterVictoryEvent = EventUnlockBattle(35)
        )

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) { R.string.lands_darkening_of_the_light_0_1 }
            1 -> EventNPCSpeak(this) { R.string.lands_darkening_of_the_light_1_1 }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_darkening_of_the_light_2_1,
                    R.string.lands_darkening_of_the_light_2_2,
                    R.string.lands_darkening_of_the_light_2_3,
                )
            }
            3 -> EventNPCSpeak(this) { R.string.lands_darkening_of_the_light_3_1 }
            4 -> EventFactory.createWithNext { manager ->
                val mms = manager.gameState.companions.filterIsInstance<TheMarryingMaiden>()
                if (mms.isNotEmpty()) {
                    EventTMMAndDOTL(mms.first(), this@DarkeningOfTheLight)
                } else {
                    speakEvent
                }
            }
            5 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_darkening_of_the_light_5_1,
                    R.string.lands_darkening_of_the_light_5_2,
                )
            }
            6 -> EventFactory.createWithNext { manager ->
                val mms = manager.gameState.companions.filterIsInstance<TheMarryingMaiden>()
                if (mms.isNotEmpty() && THE_WANDERER_STOLE_MONEYS in manager.gameState.flagsMaster) {
                    EventTMMAndDOTL2(mms.first(), this@DarkeningOfTheLight)
                } else {
                    speakEvent
                }
            }
            7 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_darkening_of_the_light_7_1,
                    R.string.lands_darkening_of_the_light_7_2,
                )
            }
            8 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_darkening_of_the_light_8_1,
                    R.string.lands_darkening_of_the_light_8_2,
                )
            }
            9 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_darkening_of_the_light_9_1,
                    R.string.lands_darkening_of_the_light_9_2,
                    R.string.lands_darkening_of_the_light_9_3,
                    R.string.lands_darkening_of_the_light_9_4,
                    R.string.lands_darkening_of_the_light_9_5,
                )
            }
            10 -> EventNPCSpeak(this) { R.string.lands_darkening_of_the_light_10_1 }
            else -> EventNPCSpeak(this) { R.string.lands_darkening_of_the_light_else_1 }
        }
}