package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.CellTheCreature
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.cell.service.SaveDestroyedCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_KILLED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventTheCreatureBattleResult(val result: BattleResult) : Event({ manager ->
    launchCoroutineOnMain lambda@ {
        if (result == BattleResult.BATTLE_DEFEAT) {
            manager.activity.returnToMenu()
            return@lambda
        }

        val newManager = LandsManager(manager.activity, manager.gameState)
        val gameState = newManager.gameState as GameState65
        gameState.battleMode = BattleMode.PEACE
        newManager.init()
        newManager.processMap()

        val cheated = STARTED_GAME !in gameState.flagsMaster
        if (cheated) {
            val creature = gameState.currentMap.getTopCells()
                .filterIsInstance<CellTheCreature>()
                .firstOrNull()
            if (creature != null) {
                newManager.wrapCutsceneSkippable {
                    drawer.showCharacterMessages(creature, listOf(
                        R.string.lands_the_creature_after_battle_cheated_1,
                        R.string.lands_the_creature_after_battle_cheated_2,
                        R.string.lands_the_creature_after_battle_cheated_3,
                        R.string.lands_the_creature_after_battle_cheated_4,
                    ))
                }
            }
        } else {
            GlobalState.putBoolean(manager.activity, THE_CREATURE_KILLED, true)
            gameState.protagonist.killed++
            gameState.currentMap.getTopCells()
                .filterIsInstance<CellTheCreature>()
                .forEach {
                    gameState.currentMap.removeCellFromTop(it)
                }

            newManager.wrapCutscene {
                delay(2500L)
                val floor = gameState.currentMap.getTopCells().filterIsInstance<CellFloor>()
                val save = gameState.currentMap.getTopCells().filterIsInstance<SaveDestroyedCell>()
                val protagonist = gameState.currentMap.getTopCells().filterIsInstance<CellProtagonist>()

                activity.musicPlayer.playMusic(
                    R.raw.sfx_creature_bit,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
                floor.forEach { gameState.currentMap.clearCell(it.coordinates) }
                delay(2500L)
                activity.musicPlayer.playMusic(
                    R.raw.sfx_creature_bit,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
                save.forEach { gameState.currentMap.clearCell(it.coordinates) }
                delay(2500L)

                drawer.makeMessageBackgroundBlack()
                drawer.showCharacterMessages(gameState.protagonist, listOf(R.string.lands_protagonist_1))
                delay(500L)
                activity.musicPlayer.playMusic(
                    R.raw.sfx_creature_bit,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )
                protagonist.forEach { gameState.currentMap.clearCell(it.coordinates) }
                delay(500L)
                drawer.showCharacterMessages(gameState.protagonist, listOf(R.string.lands_protagonist_2))
                drawer.makeMessageBackgroundWhite()

                delay(2500L)
                drawer.showMessage(
                    R.string.lands_game_over,
                    color = R.color.black,
                    tapSoundId = R.raw.sfx_tap,
                    delayAfterMessage = 22229L
                )
                activity.returnToMenu()
            }
        }
    }
})