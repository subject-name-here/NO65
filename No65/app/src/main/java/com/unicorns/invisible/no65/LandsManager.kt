package com.unicorns.invisible.no65

import com.unicorns.invisible.no65.controller.LandsController
import com.unicorns.invisible.no65.databinding.ActivityLandsBinding
import com.unicorns.invisible.no65.model.GameState
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.GameStateBC
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.MoveMode
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.SAVE_COUNTER
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.JAIL_ENTERED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.RegisteredMusic
import com.unicorns.invisible.no65.model.lands.cell.*
import com.unicorns.invisible.no65.model.lands.cell.character.npc.CellNPC
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.*
import com.unicorns.invisible.no65.view.Lands65Drawer
import com.unicorns.invisible.no65.view.LandsBCDrawer
import com.unicorns.invisible.no65.view.LandsDrawer
import com.unicorns.invisible.no65.view.LandsFieldDrawer.Companion.LANDS_WEIGHT
import com.unicorns.invisible.no65.view.lands.LandsMessageStoppedException
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.properties.Delegates


class LandsManager(
    val activity: MainActivity,
    val gameState: GameState
) {
    private val binding = ActivityLandsBinding.inflate(activity.layoutInflater)
    val landsWidth = LANDS_WIDTH
    val landsHeight = ScreenDimensions.getLandsHeight(landsWidth, LANDS_WEIGHT)

    val drawer: LandsDrawer by lazy {
        if (gameState is GameState65) {
            Lands65Drawer(activity, binding, landsWidth, landsHeight)
        } else {
            LandsBCDrawer(activity, binding, landsWidth, landsHeight)
        }
    }

    val controller: LandsController by lazy {
        LandsController(activity, binding)
    }

    private val mainCycleJob: Job = launchCoroutineOnDefaultLazy {
        while (isActive) {
            mapLock.withLock {
                onTick()
                drawer.drawMap(gameState.currentMap, gameState.protagonist.coordinates)
            }
            if (gameState.tick++ % TICKS_PER_SECOND == 0) {
                val landsDrawer = this@LandsManager.drawer
                if (landsDrawer is Lands65Drawer) {
                    landsDrawer.drawTicks(gameState.tick / TICKS_PER_SECOND)
                }
            }
            delay(TICK_TIME_MILLISECONDS)
        }
    }
    private val mapLock = ReentrantLock()

    var stopped = false

    private var cutsceneState by Delegates.observable(CutsceneState.NO_CUTSCENE) { _, _, new ->
        when (new) {
            CutsceneState.CUTSCENE_UNSKIPPABLE -> {
                drawer.speechLayoutShowOverride = true
                drawer.setSkipMessageVisibility(false)
            }
            CutsceneState.CUTSCENE_SKIPPABLE -> {
                drawer.speechLayoutShowOverride = true
                drawer.setSkipMessageVisibility(true)
            }
            CutsceneState.NO_CUTSCENE -> {
                drawer.speechLayoutShowOverride = false
                drawer.setSkipMessageVisibility(false)
            }
        }
    }

    private fun isNotCutscene(): Boolean = cutsceneState == CutsceneState.NO_CUTSCENE
    suspend fun wrapCutscene(cutscene: suspend LandsManager.() -> Unit) {
        cutsceneState = CutsceneState.CUTSCENE_UNSKIPPABLE
        this.cutscene()
        cutsceneState = CutsceneState.NO_CUTSCENE
    }
    suspend fun wrapCutsceneSkippable(cutscene: suspend LandsManager.() -> Unit) {
        cutsceneState = CutsceneState.CUTSCENE_SKIPPABLE
        this.cutscene()
        cutsceneState = CutsceneState.NO_CUTSCENE
    }

    var rewindActiveOverride = false

    suspend fun init() {
        activity.setContentView(binding.root)
        drawer.initMain().join()
        initController()
    }

    private fun initController() {
        controller.addMoveListener(::processMove)

        if (gameState is GameState65) {
            val drawer65 = drawer as Lands65Drawer
            controller.onChangeInteractionModeListeners.add {
                if (gameState.battleMode != BattleMode.FIXED_PEACE) {
                    activity.playToggleSwitchSound()
                }
            }
            controller.onChangeInteractionModeListeners.add {
                gameState.battleMode = gameState.battleMode.next()
                drawer65.setBattleMode(gameState.battleMode)
            }

            controller.onChangeMoveModeListeners.add {
                if (gameState.moveMode != MoveMode.FIXED_WALK) {
                    activity.playToggleSwitchSound()
                }
            }
            controller.onChangeMoveModeListeners.add {
                gameState.moveMode = gameState.moveMode.next()
                drawer65.setMoveMode(gameState.moveMode)
            }

            controller.onRewindListeners.add {
                if (gameState.rewindAvailable || rewindActiveOverride) {
                    activity.playToggleSwitchSound()
                }
            }
            controller.onRewindListeners.add {
                if (isNotCutscene() && gameState.rewindAvailable || rewindActiveOverride) {
                    gameState.restartMoveables()
                }
            }
        } else if (gameState is GameStateBC) {
            (drawer as LandsBCDrawer).hideControls()
        }

        controller.addCellsListeners(landsWidth, landsHeight) { screenCoordinates ->
            val mapCoordinates = getMapCoordinatesByScreenCoordinates(
                landsWidth,
                landsHeight,
                screenCoordinates,
                gameState.protagonist.coordinates
            )
            processUse(mapCoordinates)
        }

        controller.initListeners(::goBack)

        controller.setSpeechTextViewSkip {
            if (cutsceneState == CutsceneState.CUTSCENE_SKIPPABLE || activity.debug) {
                drawer.stopPrintingMessage(LandsMessageStoppedException.Reason.SKIPPED)
            }
        }
    }

    fun processMap() {
        if (gameState is GameState65) {
            val drawer65 = drawer as Lands65Drawer
            drawer65.setBattleMode(gameState.battleMode)
            drawer65.setMoveMode(gameState.moveMode)
            drawer65.drawStats(gameState.protagonist)
        }

        drawer.drawMap(gameState.currentMap, gameState.protagonist.coordinates)

        drawer.hideLoadingLayout()

        activity.musicPlayer.playMusic(
            getCurrentMapMusicThemeId(),
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = true
        )

        mainCycleJob.start()
    }

    private fun onTick() {
        val cells = gameState.currentMap.getTopCells()
        for (cell in cells) {
            cell.onTick(gameState.tick)

            if (cell is CellControl) {
                launchCoroutine {
                    cell.onTickWithEvent(gameState.tick).fireEventChain(this@LandsManager)
                }
            }

            if (cell is CellNonEmpty) {
                val cellBelow = cell.cellBelow
                if (cellBelow is CellPassable) {
                    launchCoroutine {
                        getCellBelowStepEvent(cellBelow, cell).fireEventChain(this@LandsManager)
                    }
                }
                if (cellBelow is CellSemiStatic && cellBelow.isPassable()) {
                    launchCoroutine {
                        cellBelow.onStep().fireEventChain(this@LandsManager)
                    }
                }
            }
        }

        for (cell in CellUtils.getCellsInSight(this)) {
            if (cell is CellNPC) {
                val dist = CellUtils.distanceToProtagonist(this, cell.coordinates)
                launchCoroutine {
                    cell.onSight(dist).fireEventChain(this@LandsManager)
                }
            }
        }
    }
    private fun getCellBelowStepEvent(it: CellPassable, firingCell: CellNonEmpty): Event {
        val registeredEvent = gameState.eventMaster.getOnStepEvent(gameState.currentMapIndex, it.coordinates)
        registeredEvent.attachFiringCell(firingCell)

        return it.onStep().then(registeredEvent)
    }

    private fun processMove(delta: Coordinates) {
        if (isNotCutscene()) {
            val isRunning = gameState is GameState65 && gameState.moveMode == MoveMode.RUN
            val totalDelta = if (isRunning) delta * 2 else delta
            mapLock.withLock {
                gameState.currentMap.moveOnDelta(gameState.protagonist, totalDelta)
                gameState.companions
                    .forEach {
                        launchCoroutine {
                            it.onProtagonistMoveOnDelta(totalDelta).fireEventChain(this@LandsManager)
                        }
                    }
            }
        }
    }

    private fun processUse(mapCoordinates: Coordinates) {
        val cell = gameState.currentMap.getTopCell(mapCoordinates)
        if (cell is CellUsable) {
            if (isNotCutscene() || cell.isUsableDuringCutscene()) {
                val onUseEvent = gameState.eventMaster.getOnUseEvent(gameState.currentMapIndex, mapCoordinates)
                launchCoroutine {
                    cell.use().then(onUseEvent).fireEventChain(this@LandsManager)
                }
            }
        }
    }

    fun changeCurrentMapIndex(newRelativeIndex: Int) {
        val previousMusicThemeId = getCurrentMapMusicThemeId()
        val prevIndex = gameState.currentMapIndex
        val companions = gameState.companions

        gameState.setCurrentMapIndex(newRelativeIndex, activity)

        val companionsCoroutines = companions.map {
            launchCoroutine {
                it.onCurrentMapChange(prevIndex).fireEventChain(this@LandsManager)
            }
        }

        launchCoroutine {
            companionsCoroutines.joinAll()
            val entranceEvent = gameState.eventMaster.getOnMapEnterEvent(gameState.currentMapIndex)
            entranceEvent.fireEventChain(this@LandsManager)
        }

        val newMusicThemeId = getCurrentMapMusicThemeId()
        if (previousMusicThemeId != newMusicThemeId) {
            activity.musicPlayer.stopMusicByResourceId(previousMusicThemeId)
            activity.musicPlayer.playMusic(
                newMusicThemeId,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = true
            )
        }
    }

    fun stop() {
        stopped = true
        activity.musicPlayer.stopAllMusic()
        if (mainCycleJob.isActive) {
            mainCycleJob.cancel()
        }
        drawer.stop()
    }

    fun getCurrentMapMusicThemeId(): Int {
        return (RegisteredMusic.locationToMusic[gameState.currentMapIndex] ?: { 0 })(this)
    }

    private var isExiting = false
    private fun goBack() {
        if (isNotCutscene()) {
            activity.playToggleSwitchSound()
            val saveCounter = gameState.countersMaster[SAVE_COUNTER]
            val isCheating = STARTED_GAME !in gameState.flagsMaster
            val enteredJail = JAIL_ENTERED in gameState.flagsMaster
            if (isExiting || isCheating || enteredJail && saveCounter >= 2) {
                stop()
                activity.returnToMenu()
            } else {
                isExiting = true
                launchCoroutine {
                    wrapCutsceneSkippable {
                        drawer.showMessage(
                            R.string.on_exit,
                            color = R.color.black,
                            tapSoundId = R.raw.sfx_tap
                        )
                    }

                    delay(10000L)
                    if (!stopped) {
                        isExiting = false
                    }
                }
            }
        }
    }

    enum class CutsceneState {
        CUTSCENE_UNSKIPPABLE,
        CUTSCENE_SKIPPABLE,
        NO_CUTSCENE
    }

    companion object {
        private const val LANDS_WIDTH = 7
        const val TICK_TIME_MILLISECONDS = 40L
        const val TICKS_PER_SECOND = (1000L / TICK_TIME_MILLISECONDS).toInt()
    }
}