package com.unicorns.invisible.no65.model

import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.model.lands.RegisteredCounters
import com.unicorns.invisible.no65.model.lands.RegisteredEvents
import com.unicorns.invisible.no65.model.lands.RegisteredFlags
import com.unicorns.invisible.no65.model.lands.cell.CellCompanion
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.character.CellNonStaticCharacter
import com.unicorns.invisible.no65.model.lands.cell.moveable.CellNonStaticMoveable
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.model.lands.map.MapGraph
import com.unicorns.invisible.no65.saveload.SaveManager
import kotlinx.serialization.Serializable
import java.util.ArrayDeque


@Serializable
abstract class GameState {
    var tick = 0

    abstract val mapGraph: MapGraph
    abstract var currentMapIndex: String
        protected set

    val currentMap: LandsMap
        get() = mapGraph.getMap(currentMapIndex)

    abstract val protagonist: CellNonStaticCharacter

    val companions
        get() = currentMap.getTopCells()
            .filterIsInstance<CellNonEmpty>()
            .filterIsInstance<CellCompanion>()
            .filter { it.isCompanion() }

    val eventMaster = RegisteredEvents()
    val countersMaster = RegisteredCounters()
    val flagsMaster = RegisteredFlags()

    fun setCurrentMapIndex(newVertexRelativeIndex: Int, activity: MainActivity) {
        // 1: change values
        val newVertex = mapGraph.getVertexRelative(currentMapIndex, newVertexRelativeIndex)
        currentMapIndex = newVertex.index

        // 2: mark new map as visited
        currentMap.visited = true

        // 3: load neighbors
        val neighborsIndices = mapGraph.getNeighbors(currentMapIndex)
        for (neighbor in neighborsIndices) {
            if (!mapGraph.has(neighbor)) {
                val map = SaveManager.loadMapFromRaw(activity, neighbor)
                mapGraph.addVertex(neighbor, map)
            }
        }

        // 4: get reachable maps from current map
        val reachableMapsIndices = mutableListOf(currentMapIndex)
        val queue = ArrayDeque(listOf(currentMapIndex))

        while (!queue.isEmpty()) {
            val index = queue.pop()
            mapGraph.getNeighbors(index)
                .filterNot { it in reachableMapsIndices }
                .forEach {
                    queue.add(it)
                    reachableMapsIndices.add(it)
                }
        }

        // 5: remove all unreachable vertices
        mapGraph.vertices.removeAll { it.index !in reachableMapsIndices }
    }
}