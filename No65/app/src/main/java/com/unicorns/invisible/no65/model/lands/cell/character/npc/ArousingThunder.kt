package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldArousingThunder
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.AT_GHOST_ATTACKED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.AT_STARTED_SHOWDOWN
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.EMPTY_STREET_ENTERED
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.battle.victory.EventArousingThunderAfterVictory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventATGhostAttacked
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventATShowdownFinale1
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventATShowdownFinale2
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventATShowdownVisited
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.AT_BATTLE_EVENT_REACHED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.randBoolean
import kotlinx.serialization.Serializable


@Serializable
class ArousingThunder(override var cellBelow: Cell) : CellNPC() {
    override val nameId: Int
        get() = R.string.lands_arousing_thunder_name
    override val id: Int
        get() = 51
    override val faceCell: String
        get() = "\uD83E\uDD10"
    override val centerSymbol: String
        get() = "∅"
    override val centerSymbolColor: Int
        get() = R.color.purple
    override val emotion: Emotion
        get() = Emotion.WHISPER

    override val speechSound: Int
        get() = R.raw.sfx_whisper

    private val firstAttackEvent
        get() = EventATGhostAttacked(this)

    private val speakEvent
        get() = EventNPCSpeak(this) {
            if (randBoolean()) {
                R.string.lands_arousing_thunder_speak_1
            } else {
                R.string.lands_arousing_thunder_speak_2
            }
        }

    private val attackEvent
        get() = EventAttack(
            this@ArousingThunder,
            BattleFieldArousingThunder(),
            onAfterVictoryEvent = EventArousingThunderAfterVictory().then(EventUnlockBattle(13))
        )

    private var firstCutsceneFired = false
    override fun use(): Event {
        return EventFactory.createWithNext lambda@ { manager ->
            if (manager.gameState !is GameState65) return@lambda Event.Null

            val flagsMaster = manager.gameState.flagsMaster
            if (AT_STARTED_SHOWDOWN in flagsMaster && EMPTY_STREET_ENTERED !in flagsMaster) {
                val alreadyFought = GlobalState.getBoolean(manager.activity, AT_BATTLE_EVENT_REACHED, false)
                return@lambda when {
                    alreadyFought -> EventATShowdownVisited(this@ArousingThunder).then(attackEvent)
                    !firstCutsceneFired -> {
                        firstCutsceneFired = true
                        EventATShowdownFinale1(this@ArousingThunder)
                    }
                    else -> EventATShowdownFinale2(this@ArousingThunder).then(attackEvent)
                }
            }

            if (manager.gameState.battleMode == BattleMode.ATTACK) {
                when {
                    EMPTY_STREET_ENTERED in flagsMaster -> speakEvent
                    AT_GHOST_ATTACKED in flagsMaster -> speakEvent
                    else -> {
                        flagsMaster.add(AT_GHOST_ATTACKED)
                        firstAttackEvent
                    }
                }
            } else {
                speakEvent
            }
        }
    }
}