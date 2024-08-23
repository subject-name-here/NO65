package com.unicorns.invisible.no65.model.lands.event.events.waterever

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameStateBC
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.character.npc.AbysmalWater
import com.unicorns.invisible.no65.model.lands.cell.decor.DoubleTapCell
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.HAS_SEEN_WATER_CUTSCENE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.LandsBCDrawer
import com.unicorns.invisible.no65.view.music.MusicPlayer
import com.unicorns.invisible.no65.view.speech.SpeechProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll


class EventAbysmalWaterAndBC : Event(lambda@ { manager ->
    val map = manager.gameState.currentMap
    val hasSeenCutscene = GlobalState.getBoolean(manager.activity, HAS_SEEN_WATER_CUTSCENE)
    val cutsceneWrapper = if (hasSeenCutscene) manager::wrapCutsceneSkippable else manager::wrapCutscene
    val doubleTapCell = map.createCellOnTop(manager.gameState.protagonist.coordinates, DoubleTapCell::class)

    cutsceneWrapper {
        delay(5000L)

        val aw = map.createCellOnTop(Coordinates(0, 19), AbysmalWater::class)
        val bc = (manager.gameState as GameStateBC).protagonist

        drawer.showUnknownCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_0_1
        ))

        delay(1000L)

        CellUtils.moveOnTrajectory(
            listOf(
                Coordinates(0, 20),
                Coordinates(0, 21),
                Coordinates(0, 22),
            ),
            aw,
            map,
            LandsManager.TICK_TIME_MILLISECONDS * 5
        )

        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_1_1
        ))
        drawer.showUnknownCharacterMessages(
            bc,
            listOf(
                R.string.lands_abysmal_water_bc_2_1
            ),
        )
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_3_1,
            R.string.lands_abysmal_water_aw_3_2,
        ))
        drawer.showUnknownCharacterMessages(
            bc,
            listOf(
                R.string.lands_abysmal_water_bc_4_1,
                R.string.lands_abysmal_water_bc_4_2,
            ),
        )
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_5_1,
        ))
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_5_2,
        ), delayAfterMessage = SpeechProperties.DELAY_AFTER_MESSAGE / 2)
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_5_3,
        ), delayAfterMessage = 0L)
        drawer.showUnknownCharacterMessages(
            bc,
            listOf(
                R.string.lands_abysmal_water_bc_6_1,
                R.string.lands_abysmal_water_bc_6_2,
            ),
        )
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_7_1,
        ))
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_7_2,
            R.string.lands_abysmal_water_aw_7_3,
            R.string.lands_abysmal_water_aw_7_4,
            R.string.lands_abysmal_water_aw_7_5,
        ), delayAfterMessage = SpeechProperties.DELAY_AFTER_MESSAGE / 2)

        delay(1000L)
        manager.activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_chair_move,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        map.moveOnDelta(doubleTapCell, Coordinates(0, 1))
        delay(1000L)

        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_8_1
        ))
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_9_1
        ))
        delay(1000L)

        (-1..1).forEach { x ->
            (-1..1).forEach { y ->
                val coordinates = manager.gameState.protagonist.coordinates + Coordinates(x, y)
                val cell = map.getTopCell(coordinates)
                if (cell is CellFloor) {
                    cell.litUp = true
                } else if (cell is CellNonEmpty) {
                    val cellBelow = cell.cellBelow
                    if (cellBelow is CellFloor) {
                        cellBelow.litUp = true
                    }
                }
            }
        }

        delay(3000L)

        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_10_1,
        ))
        delay(3000L)
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_11_1,
        ))
        delay(3000L)

        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_12_1,
            R.string.lands_abysmal_water_aw_12_2,
        ))
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_13_1,
        ))
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_14_1,
            R.string.lands_abysmal_water_aw_14_2,
        ))

        delay(1000L)
        val spinningBCJob = launchCoroutine {
            while (isActive) {
                bc.rotation += 3f
                delay(LandsManager.TICK_TIME_MILLISECONDS)
            }
        }
        delay(500L)
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_15_1,
            R.string.lands_abysmal_water_bc_15_2,
            R.string.lands_abysmal_water_bc_15_3,
            R.string.lands_abysmal_water_bc_15_4,
        ))
        spinningBCJob.cancel()
        bc.rotation = 0f
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_15_5,
        ), delayAfterMessage = 0L)

        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_16_1,
        ))
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_17_1,
        ))
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_17_2,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_18_1,
            R.string.lands_abysmal_water_aw_18_2,
            R.string.lands_abysmal_water_aw_18_3,
            R.string.lands_abysmal_water_aw_18_4,
        ))
        val jobMoving = launchCoroutine {
            CellUtils.moveOnTrajectory(
                listOf(
                    Coordinates(0, 22),
                    Coordinates(0, 21),
                    Coordinates(0, 20),
                    Coordinates(0, 19),
                ),
                aw,
                map,
                LandsManager.TICK_TIME_MILLISECONDS * 5
            )
        }
        val jobTalking = launchCoroutine {
            drawer.showCharacterMessages(bc, listOf(
                R.string.lands_abysmal_water_bc_19_1,
            ), delayAfterMessage = SpeechProperties.DELAY_AFTER_MESSAGE / 3)
        }
        joinAll(jobMoving, jobTalking)

        manager.activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.sfx_teleport_bc,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        val landsDrawer = drawer
        if (landsDrawer is LandsBCDrawer) {
            landsDrawer.flash().join()
        }
        map.moveTo(aw, Coordinates(0, 22))

        delay(1000L)
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_20_1,
            R.string.lands_abysmal_water_aw_20_2,
            R.string.lands_abysmal_water_aw_20_3,
            R.string.lands_abysmal_water_aw_20_4,
            R.string.lands_abysmal_water_aw_20_5,
        ))
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_21_1,
            R.string.lands_abysmal_water_bc_21_2,
        ))
        bc.emotion = Emotion.HOSTILITY
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_21_3,
        ))
        bc.emotion = Emotion.FRIENDLINESS
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_21_4,
        ))

        delay(1000L)
        aw.state = AbysmalWater.State.BRAVE
        activity.musicPlayer.playMusic(
            R.raw.sfx_huh,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_22_0,
            R.string.lands_abysmal_water_aw_22_1,
            R.string.lands_abysmal_water_aw_22_2,
            R.string.lands_abysmal_water_aw_22_3,
            R.string.lands_abysmal_water_aw_22_4,
            R.string.lands_abysmal_water_aw_22_5,
            R.string.lands_abysmal_water_aw_22_6,
            R.string.lands_abysmal_water_aw_22_7,
            R.string.lands_abysmal_water_aw_22_8,
            R.string.lands_abysmal_water_aw_22_9,
        ))
        drawer.showCharacterMessages(bc, listOf(
            R.string.lands_abysmal_water_bc_23_1,
            R.string.lands_abysmal_water_bc_23_2,
            R.string.lands_abysmal_water_bc_23_3,
        ))
        drawer.showCharacterMessages(aw, listOf(
            R.string.lands_abysmal_water_aw_24_1,
            R.string.lands_abysmal_water_aw_24_2,
            R.string.lands_abysmal_water_aw_24_3,
            R.string.lands_abysmal_water_aw_24_4,
        ))
        GlobalState.putBoolean(manager.activity, HAS_SEEN_WATER_CUTSCENE, true)

        launchCoroutine {
            EventAttackAbysmalWater().fireEventChain(manager)
        }
    }
})