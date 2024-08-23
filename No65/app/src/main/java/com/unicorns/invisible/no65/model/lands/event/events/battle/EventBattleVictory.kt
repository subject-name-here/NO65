package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.books.*
import com.unicorns.invisible.no65.model.lands.cell.character.npc.*
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventBattleVictory(characterCell: CellNPC, onAfterVictoryEvent: Event): Event({ manager ->
    launchCoroutineOnMain {
        val newManager = LandsManager(manager.activity, manager.gameState)
        newManager.gameState.currentMap.apply {
            val coordinates = characterCell.coordinates
            removeCellFromTop(characterCell)

            when (characterCell) {
                is CreativeHeaven -> {
                    createCellOnTop(coordinates, SkillBookHeaven::class)
                }
                is ReceptiveEarth -> {
                    createCellOnTop(coordinates, SkillBookEarth::class)
                }
                is KeepingStillMountain -> {
                    createCellOnTop(coordinates, SkillBookMountain::class)
                }
                is BeforeCompletion -> {
                    createCellOnTop(coordinates, SkillBookWater::class)
                }
                is InnerTruth -> {
                    createCellOnTop(coordinates, SkillBookWind::class)
                    val otherBookCoordinates = newManager.gameState.protagonist.coordinates + Coordinates(0, -1)
                    createCellOnTop(otherBookCoordinates, SkillBookLake::class)
                }
                is JoyousLake -> {
                    createCellOnTop(coordinates, SkillBookLakeRequiem::class)
                }
                is ClingingFire -> {
                    createCellOnTop(coordinates, SkillBookFire::class)
                }
                is ArousingThunder -> {
                    createCellOnTop(coordinates, SkillBookThunder::class)
                }
                is GentleWind -> {
                    val gwCoordinates = Coordinates(-1, 11)
                    createCellOnTop(gwCoordinates, SkillBookWindRequiem::class)
                }
                else -> {}
            }
        }
        if (newManager.gameState is GameState65) {
            newManager.gameState.battleMode = BattleMode.PEACE
            newManager.gameState.protagonist.killed++
        }
        newManager.init()
        newManager.processMap()
        onAfterVictoryEvent.fireEventChain(newManager)
        EventTreadingCheckAfterBattle().fireEventChain(newManager)
    }
})