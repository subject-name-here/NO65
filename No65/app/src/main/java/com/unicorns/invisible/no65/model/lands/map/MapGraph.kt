package com.unicorns.invisible.no65.model.lands.map

import com.unicorns.invisible.no65.view.lands.NumberToStringId.Companion.ERROR_MAP_NAME_NUMBER
import kotlinx.serialization.Serializable


@Serializable
class MapGraph {
    val vertices: ArrayList<Vertex> = ArrayList()
    private val edges: Map<String, ArrayList<String>>

    constructor(map: LandsMap) {
        vertices.add(Vertex(map.name, map))
        edges = mapOf(map.name to ArrayList())
    }

    fun getMap(mapIndex: String): LandsMap {
        val vertex = vertices.find { it.index == mapIndex }
        return vertex?.map ?: DEFAULT_MAP
    }

    fun getMapRelative(currentIndex: String, neighborIndex: Int): LandsMap {
        return getVertexRelative(currentIndex, neighborIndex).map
    }

    fun getVertexRelative(currentIndex: String, neighborIndex: Int): Vertex {
        if (neighborIndex < 0) {
            return vertices.find { it.index == currentIndex } ?: DEFAULT_VERTEX
        }
        val index = edges[currentIndex]?.getOrNull(neighborIndex) ?: DEFAULT_VERTEX
        return vertices.find { it.index == index } ?: DEFAULT_VERTEX
    }

    fun getNeighbors(index: String): List<String> {
        return edges[index]?.toList() ?: emptyList()
    }

    fun addVertex(index: String, map: LandsMap) {
        if (!has(index)) {
            vertices.add(Vertex(index, map))
        }
    }

    fun rawConnectWith(parent: String, child: String) {
        edges[parent]?.clear()
        edges[parent]?.add(child)
    }

    fun has(mapIndex: String): Boolean {
        return mapIndex in vertices.map { it.index }
    }

    @Serializable
    data class Vertex(val index: String, val map: LandsMap)

    companion object {
        private const val ERROR_MAP_INDEX = "ERROR_MAP"
        val DEFAULT_MAP = LandsMap().also { it.nameId = ERROR_MAP_NAME_NUMBER }
        val DEFAULT_VERTEX = Vertex(ERROR_MAP_INDEX, DEFAULT_MAP)
    }
}