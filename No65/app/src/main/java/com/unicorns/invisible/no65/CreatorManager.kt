package com.unicorns.invisible.no65

import com.unicorns.invisible.no65.controller.CreatorController
import com.unicorns.invisible.no65.databinding.ActivityCreatorBinding
import com.unicorns.invisible.no65.model.lands.RegisteredCells
import com.unicorns.invisible.no65.model.lands.cell.*
import com.unicorns.invisible.no65.model.lands.cell.control.Socket
import com.unicorns.invisible.no65.model.lands.cell.control.door.Door
import com.unicorns.invisible.no65.model.lands.cell.decor.LetterCell
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMessage
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.saveload.SaveManager
import com.unicorns.invisible.no65.util.*
import com.unicorns.invisible.no65.view.CreatorDrawer
import com.unicorns.invisible.no65.view.LandsFieldDrawer.Companion.LANDS_WEIGHT
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


class CreatorManager(
    private val activity: MainActivity,
    private val currentMap: LandsMap
) {
    private val landsWidth = LANDS_WIDTH
    private val landsHeight: Int = ScreenDimensions.getLandsHeight(landsWidth, LANDS_WEIGHT)

    private val binding = ActivityCreatorBinding.inflate(activity.layoutInflater)

    private val drawer: CreatorDrawer by lazy {
        CreatorDrawer(activity, landsWidth, landsHeight, binding)
    }
    private val controller: CreatorController by lazy {
        CreatorController(activity, binding)
    }

    private var centerCoordinates = Coordinates.ZERO

    private var currentMode: Mode by Delegates.observable(Mode.ERASE) { _, _, new ->
        drawer.setMode(new)
    }
    private var currentCell: KClass<out Cell> = CellEmpty::class

    fun start() {
        activity.setContentView(binding.root)
        launchCoroutineOnMain {
            drawer.initMain().join()
            initController()

            drawer.drawMap(currentMap, centerCoordinates)
            drawer.setLocationName(currentMap.name)
            drawer.setMode(currentMode)

            drawer.hideLoadingLayout()
        }
    }


    private fun initController() {
        controller.addMoveListener { delta ->
            centerCoordinates += delta
            drawer.drawMap(currentMap, centerCoordinates)
        }

        controller.addListItemListeners { cellListChildIndex ->
            currentMode = Mode.DRAW

            currentCell = RegisteredCells.cells[cellListChildIndex]
            drawer.highlightedItemIndex = cellListChildIndex
        }

        controller.addDrawEraseButtonListener {
            currentMode = currentMode.next()
        }

        controller.addCellsListeners(landsWidth, landsHeight, ::onSingleTapCell, ::onDoubleTapCell)

        controller.addSaveButtonListener {
            currentMap.name = drawer.getLocationName()
            SaveManager.saveMap(activity, currentMap)
            stop()
            activity.returnToMenu(playMusic = true)
        }
    }



    private fun onSingleTapCell(screenFieldCoordinates: Coordinates) {
        val cellCoordinates = getMapCoordinatesByScreenCoordinates(
            landsWidth,
            landsHeight,
            screenFieldCoordinates,
            centerCoordinates
        )

        when (currentMode) {
            Mode.ERASE -> {
                val cell = currentMap.getTopCell(cellCoordinates)
                if (cell is CellNonEmpty) {
                    currentMap.removeCellFromTop(cell)
                }
            }
            Mode.DRAW -> {
                if (currentCell == CellEmpty::class) {
                    currentMap.clearCell(cellCoordinates)
                } else {
                    val cellBelow = currentMap.getTopCell(cellCoordinates)
                    val cell = currentCell.primaryConstructor!!.call(cellBelow)

                    if (checkCellOnCellBelow(cell, cellBelow)) {
                        currentMap.addCellOnTop(cell, cellCoordinates)
                    }
                }
            }
        }

        drawer.drawMap(currentMap, centerCoordinates)
    }

    private fun onDoubleTapCell(screenFieldCoordinates: Coordinates) {
        val cellCoordinates = getMapCoordinatesByScreenCoordinates(
            landsWidth,
            landsHeight,
            screenFieldCoordinates,
            centerCoordinates
        )

        when (val cell = currentMap.getTopCell(cellCoordinates)) {
            is TeleportCell -> {
                drawer.showTeleportRedactor(cell) { mapIndex, coordinates ->
                    cell.toMapIndex = mapIndex
                    cell.toCoordinates = coordinates
                    controller.hideKeyboard()
                }
            }
            is Socket -> {
                drawer.showControlRedactor(cell.id) { id ->
                    cell.id = id
                    controller.hideKeyboard()
                }
            }
            is Door -> {
                drawer.showControlRedactor(cell.id) { id ->
                    cell.id = id
                    controller.hideKeyboard()
                }
            }
            is CellMessage -> {
                drawer.showMessageRedactor(cell) { messageNumber ->
                    cell.messageNumber = messageNumber
                    controller.hideKeyboard()
                }
            }
            is LetterCell -> {
                drawer.showLetterRedactor(cell) { letter ->
                    cell.symbol = letter
                    controller.hideKeyboard()
                    drawer.drawMap(currentMap, centerCoordinates)
                }
            }
            else -> {}
        }
    }

    private fun checkCellOnCellBelow(cell: Cell, cellBelow: Cell): Boolean {
         return cellBelow is CellEmpty && cell is CellPassable ||
                 cellBelow is CellEmpty && cell is CellStatic ||
                 cellBelow is CellEmpty && cell is CellSemiStatic ||
                 cellBelow is CellPassable && cell is CellNonStatic ||
                 cellBelow is CellPassable && cell is CellStaticOnPassable
    }

    fun stop() {
        drawer.stop()
    }

    enum class Mode {
        DRAW,
        ERASE;

        fun next() = when (this) {
            DRAW -> ERASE
            ERASE -> DRAW
        }
    }

    companion object {
        const val LANDS_WIDTH = 7
    }
}