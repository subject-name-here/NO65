package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.CellTheCreature
import com.unicorns.invisible.no65.model.lands.event.Event


class EventYou : Event({ manager ->
    manager.wrapCutsceneSkippable {
        val isCreatureExists = gameState.currentMap
            .getTopCells()
            .filterIsInstance<CellTheCreature>()
            .isNotEmpty()
        val isCheating = STARTED_GAME !in gameState.flagsMaster
        val protagonist = gameState.protagonist as CellProtagonist
        val youState = protagonist.youState
        val killed = protagonist.killed
        val messages = when {
            isCheating -> {
                listOf(
                    R.string.lands_event_you_cheating_1,
                    R.string.lands_event_you_cheating_2,
                )
            }
            isCreatureExists -> {
                listOf(R.string.lands_event_you_creature_1)
            }
            youState == CellProtagonist.YouState.UNKNOWN -> {
                listOf(
                    R.string.lands_event_you_unknown_1,
                    R.string.lands_event_you_unknown_2,
                )
            }
            youState == CellProtagonist.YouState.BC -> {
                listOf(
                    R.string.lands_event_you_bc_1,
                    R.string.lands_event_you_bc_2,
                )
            }
            youState == CellProtagonist.YouState.NOT_BC -> {
                listOf(
                    R.string.lands_event_you_not_bc_1,
                    R.string.lands_event_you_not_bc_2,
                )
            }
            youState == CellProtagonist.YouState.BANNERMAN -> {
                listOf(
                    R.string.lands_event_you_bannerman_1,
                    R.string.lands_event_you_bannerman_2,
                )
            }
            youState == CellProtagonist.YouState.CREEP -> {
                listOf(
                    R.string.lands_event_you_creep_1,
                    R.string.lands_event_you_creep_2,
                )
            }
            youState == CellProtagonist.YouState.INTRUDER -> {
                listOf(
                    R.string.lands_event_you_intruder_1,
                    R.string.lands_event_you_intruder_2,
                )
            }
            youState == CellProtagonist.YouState.CREEP_AGAIN -> {
                listOf(
                    R.string.lands_event_you_creep_again_1,
                    R.string.lands_event_you_creep_again_2,
                )
            }
            youState == CellProtagonist.YouState.ONLY_YOU -> {
                when (killed) {
                    in (0..32) -> {
                        listOf(
                            R.string.lands_event_you_only_less_33_1,
                        )
                    }
                    in (33..47) -> {
                        listOf(
                            R.string.lands_event_you_only_less_48_1,
                        )
                    }
                    63 -> {
                        listOf(
                            R.string.lands_event_you_only_63_1,
                        )
                    }
                    else -> {
                        listOf(
                            R.string.lands_event_you_only_else_1,
                        )
                    }
                }
            }
            else -> {
                listOf(R.string.lands_event_you_else)
            }
        }

        drawer.showMessages(messages, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    }
})