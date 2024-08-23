package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.cell.CellPassable
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.randInt
import kotlinx.serialization.Serializable

@Serializable
class NightSpawner(override var cellBelow: Cell) : CellStatic(), CellControl {
    override val symbol: Char
        get() = 'ѯ'
    override val symbolColor: Int
        get() = R.color.black
    override val backgroundColor: Int
        get() = R.color.white

    // initialDelta must be chosen by hand! If all initialDelta are the same, it's dead end.
    private val initialDelta = randInt(ticksPerLaunch)

    override fun onTickWithEvent(tick: Int): Event {
        if (tick % ticksPerLaunch == initialDelta) {
            return EventFactory.create { manager ->
                launchProjectile(manager)
            }
        }

        return Event.Null
    }

    private fun launchProjectile(manager: LandsManager) {
        if (CellUtils.distanceToProtagonist(manager, coordinates) <= TOO_CLOSE_TO_PROTAGONIST_DISTANCE) {
            return
        }

        val protagonist = manager.gameState.protagonist
        val deltaR = (coordinates - protagonist.coordinates).row
        val deltaC = (coordinates - protagonist.coordinates).col

        when {
            (deltaR > deltaC) && (deltaR > 0) -> {
                launchProjectileWhere(manager, NightComesDown.Direction.UP)
            }
            (deltaR > deltaC) && (deltaR < 0) -> {
                launchProjectileWhere(manager, NightComesDown.Direction.DOWN)
            }
            (deltaR < deltaC) && (deltaC > 0) -> {
                launchProjectileWhere(manager, NightComesDown.Direction.LEFT)
            }
            (deltaR < deltaC) && (deltaC < 0) -> {
                launchProjectileWhere(manager, NightComesDown.Direction.RIGHT)
            }
        }
    }

    private fun launchProjectileWhere(manager: LandsManager, direction: NightComesDown.Direction) {
        val projectileCoordinates = coordinates + direction.getDelta()
        val occupier = manager.gameState.currentMap.getTopCell(projectileCoordinates)
        if (occupier is CellPassable) {
            val projectile = manager.gameState.currentMap.createCellOnTop(projectileCoordinates, NightComesDown::class)
            projectile.direction = direction
        }
    }

    companion object {
        const val ticksPerLaunch = LandsManager.TICKS_PER_SECOND * 2
        const val TOO_CLOSE_TO_PROTAGONIST_DISTANCE = 2
    }
}