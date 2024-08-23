package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.CREATIVE_HEAVEN_DEAD
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.CellTheCreature
import com.unicorns.invisible.no65.model.lands.cell.character.npc.AfterCompletion
import com.unicorns.invisible.no65.model.lands.cell.service.SaveCellDecoy
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventSave
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.ATTRIBUTIONS_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_KILLED
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_MET_COUNTER
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.takeRand
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventAfterCompletion(private val ac: AfterCompletion) : Event({manager ->
    manager.activity.musicPlayer.stopAllMusic()
    manager.wrapCutscene {
        delay(2500L)
        drawer.showCharacterMessages(ac, listOf(
            R.string.lands_after_completion_1,
            R.string.lands_after_completion_2,
            R.string.lands_after_completion_3,
            R.string.lands_after_completion_4,
            R.string.lands_after_completion_5,
            R.string.lands_after_completion_6,
            R.string.lands_after_completion_7,
            R.string.lands_after_completion_8,
            R.string.lands_after_completion_9,
            R.string.lands_after_completion_10,
        ))
        activity.musicPlayer.playMusic(
            R.raw.sfx_creature_appear,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        drawer.fadeInWhite(6000L)
        drawer.hideIncludeLayout()
        gameState.currentMap.removeCellFromTop(ac)
        val creature = gameState.currentMap.createCellOnTop(ac.coordinates, CellTheCreature::class)
        delay(2500L)

        fun removeCells(isTotal: Boolean) {
            activity.musicPlayer.playMusic(
                R.raw.sfx_creature_bit,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = false
            )
            val cells = CellUtils.getCellsInSight(this).filterIsInstance<CellNonEmpty>()
            val counter = if (isTotal) cells.size else cells.size / 2
            cells.takeRand(counter).forEach {
                if (it !is CellTheCreature && it !is CellProtagonist && it !is SaveCellDecoy) {
                    gameState.currentMap.clearCell(it.coordinates)
                }
            }
        }

        when {
            GlobalState.getBoolean(activity, THE_CREATURE_KILLED) -> {
                drawer.showCharacterMessages(creature, listOf(
                    R.string.lands_the_creature_intro_1,
                    R.string.lands_the_creature_intro_2,
                    R.string.lands_the_creature_intro_3,
                    R.string.lands_the_creature_after_death_0_1,
                    R.string.lands_the_creature_after_death_0_2,
                    R.string.lands_the_creature_after_death_0_3,
                ))

                delay(1000L)
                removeCells(isTotal = false)
                delay(1000L)

                drawer.showCharacterMessages(creature, listOf(
                    R.string.lands_the_creature_after_death_1_1,
                    R.string.lands_the_creature_after_death_1_2,
                    R.string.lands_the_creature_after_death_1_3,
                    R.string.lands_the_creature_after_death_1_4,
                    R.string.lands_the_creature_after_death_1_5,
                ))

                delay(1000L)
                removeCells(isTotal = false)
                delay(1000L)

                drawer.showCharacterMessages(creature, listOf(
                    R.string.lands_the_creature_after_death_2_1,
                    R.string.lands_the_creature_after_death_2_2,
                    R.string.lands_the_creature_after_death_2_3,
                    R.string.lands_the_creature_after_death_2_4,
                    R.string.lands_the_creature_after_death_2_5,
                    R.string.lands_the_creature_after_death_2_6,
                ))
            }
            STARTED_GAME !in gameState.flagsMaster -> {
                drawer.showCharacterMessages(creature, listOf(
                    R.string.lands_the_creature_intro_1,
                    R.string.lands_the_creature_intro_2,
                    R.string.lands_the_creature_intro_3,
                    R.string.lands_the_creature_cheater_0_1,
                    R.string.lands_the_creature_cheater_0_2,
                ))

                delay(1000L)
                removeCells(isTotal = false)
                delay(1000L)

                if (!gameState.flagsMaster.contains(CREATIVE_HEAVEN_DEAD)) {
                    drawer.showCharacterMessages(creature, listOf(
                        R.string.lands_the_creature_cheater_1_living_heaven_1,
                        R.string.lands_the_creature_cheater_1_living_heaven_2,
                        R.string.lands_the_creature_cheater_1_living_heaven_3,
                    ))
                } else {
                    drawer.showCharacterMessages(creature, listOf(
                        R.string.lands_the_creature_cheater_1_dead_heaven_1,
                        R.string.lands_the_creature_cheater_1_dead_heaven_2,
                        R.string.lands_the_creature_cheater_1_dead_heaven_3,
                    ))
                }

                delay(1000L)
                removeCells(isTotal = false)
                delay(1000L)

                drawer.showCharacterMessages(creature, listOf(
                    R.string.lands_the_creature_cheater_2_1,
                    R.string.lands_the_creature_cheater_2_2,
                    R.string.lands_the_creature_cheater_2_3,
                    R.string.lands_the_creature_cheater_2_4,
                    R.string.lands_the_creature_cheater_2_5,
                ))
            }
            else -> {
                val enc = GlobalState.getInt(activity, THE_CREATURE_MET_COUNTER)
                GlobalState.putInt(activity, THE_CREATURE_MET_COUNTER, enc + 1)
                when (enc) {
                    0 -> {
                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_intro_1,
                            R.string.lands_the_creature_intro_2,
                            R.string.lands_the_creature_intro_3,
                            R.string.lands_the_creature_0_0_1,
                            R.string.lands_the_creature_0_0_2,
                            R.string.lands_the_creature_0_0_3,
                            R.string.lands_the_creature_0_0_4,
                            R.string.lands_the_creature_0_0_5,
                            R.string.lands_the_creature_0_0_6,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_0_1_1,
                            R.string.lands_the_creature_0_1_2,
                            R.string.lands_the_creature_0_1_3,
                            R.string.lands_the_creature_0_1_4,
                            R.string.lands_the_creature_0_1_5,
                            R.string.lands_the_creature_0_1_6,
                            R.string.lands_the_creature_0_1_7,
                            R.string.lands_the_creature_0_1_8,
                            R.string.lands_the_creature_0_1_9,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_0_3_1,
                            R.string.lands_the_creature_0_3_2,
                            R.string.lands_the_creature_0_3_3,
                            R.string.lands_the_creature_0_3_4,
                            R.string.lands_the_creature_0_3_5,
                            R.string.lands_the_creature_0_3_6,
                            R.string.lands_the_creature_0_3_7,
                            R.string.lands_the_creature_0_3_8,
                            R.string.lands_the_creature_0_3_9,
                            R.string.lands_the_creature_0_3_10,
                            R.string.lands_the_creature_0_3_11,
                            R.string.lands_the_creature_0_3_12,
                            R.string.lands_the_creature_0_3_13,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_0_4_1,
                            R.string.lands_the_creature_0_4_2,
                            R.string.lands_the_creature_0_4_3,
                            R.string.lands_the_creature_0_4_4,
                            R.string.lands_the_creature_0_4_6,
                            R.string.lands_the_creature_0_4_7,
                            R.string.lands_the_creature_0_4_8,
                            R.string.lands_the_creature_0_4_9,
                            R.string.lands_the_creature_0_4_10,
                        ))
                    }
                    1 -> {
                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_intro_1,
                            R.string.lands_the_creature_intro_2,
                            R.string.lands_the_creature_intro_3,
                            R.string.lands_the_creature_1_0_1,
                            R.string.lands_the_creature_1_0_2,
                            R.string.lands_the_creature_1_0_4,
                            R.string.lands_the_creature_1_0_5,
                            R.string.lands_the_creature_1_0_6,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_1_1_1,
                            R.string.lands_the_creature_1_1_2,
                            R.string.lands_the_creature_1_1_3,
                            R.string.lands_the_creature_1_1_4,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_1_2_1,
                            R.string.lands_the_creature_1_2_3,
                            R.string.lands_the_creature_1_2_5,
                            R.string.lands_the_creature_1_2_6,
                            R.string.lands_the_creature_1_2_7,
                        ))
                    }
                    2 -> {
                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_intro_1,
                            R.string.lands_the_creature_intro_2,
                            R.string.lands_the_creature_intro_3,
                            R.string.lands_the_creature_2_0_1,
                            R.string.lands_the_creature_2_0_2,
                            R.string.lands_the_creature_2_0_3,
                            R.string.lands_the_creature_2_0_4,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_2_1_1,
                            R.string.lands_the_creature_2_1_2,
                            R.string.lands_the_creature_2_1_3,
                            R.string.lands_the_creature_2_1_4,
                            R.string.lands_the_creature_2_1_5,
                            R.string.lands_the_creature_2_1_6,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_2_2_1,
                            R.string.lands_the_creature_2_2_2,
                            R.string.lands_the_creature_2_2_3,
                            R.string.lands_the_creature_2_2_4,
                            R.string.lands_the_creature_2_2_5,
                            R.string.lands_the_creature_2_2_6,
                        ))
                    }
                    3 -> {
                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_intro_1,
                            R.string.lands_the_creature_intro_2,
                            R.string.lands_the_creature_intro_3,
                            R.string.lands_the_creature_3_0_1,
                            R.string.lands_the_creature_3_0_2,
                            R.string.lands_the_creature_3_0_3,
                            R.string.lands_the_creature_3_0_4,
                            R.string.lands_the_creature_3_0_5,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_3_1_1,
                            R.string.lands_the_creature_3_1_2,
                            R.string.lands_the_creature_3_1_3,
                            R.string.lands_the_creature_3_1_4,
                            R.string.lands_the_creature_3_1_5,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_3_2_1,
                            R.string.lands_the_creature_3_2_2,
                        ))
                    }
                    4 -> {
                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_intro_1,
                            R.string.lands_the_creature_intro_2,
                            R.string.lands_the_creature_intro_3,
                            R.string.lands_the_creature_4_0_1,
                            R.string.lands_the_creature_4_0_2,
                            R.string.lands_the_creature_4_0_3,
                            R.string.lands_the_creature_4_0_4,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_4_1_1,
                            R.string.lands_the_creature_4_1_2,
                            R.string.lands_the_creature_4_1_3,
                            R.string.lands_the_creature_4_1_4,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_4_2_1,
                            R.string.lands_the_creature_4_2_3,
                            R.string.lands_the_creature_4_2_4,
                            R.string.lands_the_creature_4_2_5,
                            R.string.lands_the_creature_4_2_6,
                        ))
                    }
                    else -> {
                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_intro_1,
                            R.string.lands_the_creature_intro_2,
                            R.string.lands_the_creature_intro_3,
                            R.string.lands_the_creature_else_0_1,
                            R.string.lands_the_creature_else_0_2,
                            R.string.lands_the_creature_else_0_3,
                            R.string.lands_the_creature_else_0_4,
                        ))

                        delay(1000L)
                        removeCells(isTotal = false)
                        delay(1000L)

                        drawer.showCharacterMessages(creature, listOf(
                            R.string.lands_the_creature_else_1_1,
                            R.string.lands_the_creature_else_1_2,
                            R.string.lands_the_creature_else_1_3,
                            R.string.lands_the_creature_else_1_4,
                        ))
                    }
                }
            }
        }


        delay(1000L)
        removeCells(isTotal = true)
        delay(1000L)

        EventSave(silent = true).fireEventChain(this)
        GlobalState.putBoolean(manager.activity, ATTRIBUTIONS_AVAILABLE, true)
    }
})