package com.unicorns.invisible.no65.model

import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.MoveMode
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.map.MapGraph
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class GameState65(
    override val mapGraph: MapGraph,
    override var currentMapIndex: String
) : GameState() {
    val knowledge = Knowledge()
    var battleMode = BattleMode.FIXED_PEACE
    var moveMode = MoveMode.FIXED_WALK
    var rewindAvailable = false

    @Transient
    override val protagonist: CellProtagonist = findProtagonist()
    private fun findProtagonist(): CellProtagonist = currentMap
        .getTopCells()
        .filterIsInstance<CellProtagonist>()
        .getOrElse(0) {
            currentMap.createCellOnTop(Coordinates.ZERO, CellProtagonist::class)
        }
}