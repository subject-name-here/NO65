package com.unicorns.invisible.no65.model

import com.unicorns.invisible.no65.model.lands.cell.character.npc.BeforeCompletion
import com.unicorns.invisible.no65.model.lands.map.MapGraph
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Transient


class GameStateBC(
    override val mapGraph: MapGraph,
    override var currentMapIndex: String
) : GameState() {
    @Transient
    override val protagonist: BeforeCompletion = findProtagonistBC()
    private fun findProtagonistBC(): BeforeCompletion = currentMap
        .getTopCells()
        .filterIsInstance<BeforeCompletion>()
        .getOrElse(0) {
            currentMap.createCellOnTop(Coordinates.ZERO, BeforeCompletion::class)
        }.apply { state = BeforeCompletion.State.PROTAGONIST }
}