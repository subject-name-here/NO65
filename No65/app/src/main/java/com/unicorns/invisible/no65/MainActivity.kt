package com.unicorns.invisible.no65

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.unicorns.invisible.no65.controller.MainController
import com.unicorns.invisible.no65.databinding.ActivityMainBinding
import com.unicorns.invisible.no65.databinding.ActivityTurnOnBinding
import com.unicorns.invisible.no65.init.InitData
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.MoveMode
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.WORLD_TRACE_NUMBER
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.model.lands.map.MapGraph
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.ATTRIBUTIONS_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CREATIVE_HEAVEN_BATTLE_IN_PROGRESS
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.SOUND_TEST_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.TRIGRAMS_HELP_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.saveload.SaveManager
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.view.MainDrawer
import com.unicorns.invisible.no65.view.TurnOnDrawer
import com.unicorns.invisible.no65.view.lands.NumberToStringId
import com.unicorns.invisible.no65.view.lands.NumberToStringId.Companion.CREATOR_DEFAULT_MAP_NAME_NUMBER
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlin.system.exitProcess


class MainActivity : RootActivity(), MenuItemManager {
    override val activity: MainActivity = this
    override val controller = MainController(
        this,
        ::continueGame,
        ::newGame,
        ::redactMapListener,
        ::playMapListener,
        ::volumeListener,
        ::showHelp,
        ::showAttributions,
        ::showAbout,
    )
    override val drawer = MainDrawer(this)
    val musicPlayer = MusicPlayer(this)
    var debug = false

    // This abomination of init value is just to initialize with something
    // It is guaranteed that binding will be properly initialized when used.
    override var binding = ViewBinding { View(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launchCoroutineOnMain {
            val turnOnBinding = ActivityTurnOnBinding.inflate(layoutInflater)
            setContentView(turnOnBinding.root)

            setMusicPlayerVolume()
            TurnOnDrawer(this@MainActivity, turnOnBinding).start()

            launch(playMusic = true)
        }
    }

    override fun onPause() {
        super.onPause()
        musicPlayer.pauseAllMusic()
    }

    override fun onResume() {
        super.onResume()
        musicPlayer.resumeAllMusic()
    }

    override suspend fun launch(playMusic: Boolean) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        controller.reload()
        super.launch(playMusic)
    }

    override fun setupContent() {
        debug = GlobalState.getBoolean(this@MainActivity, DEBUG_KEY, default = false)
        checkCreativeHeavenTweak()

        val saveExists = SaveManager.saveExists(this@MainActivity)
        if (!saveExists) {
            drawer.hideContinueButton()
        }

        val attributionsAvailable = GlobalState.getBoolean(this@MainActivity, ATTRIBUTIONS_AVAILABLE)
        if (saveExists && !attributionsAvailable) {
            drawer.hideNewGameButton()
        }

        if (!debug) {
            drawer.hideCreatedMapsButtons()
        }

        drawer.updateVolumeMode(musicPlayer.currentVolume)

        val knowAboutTrigrams = GlobalState.getBoolean(this@MainActivity, TRIGRAMS_HELP_AVAILABLE)
        if (!knowAboutTrigrams) {
            drawer.hideHelpButton()
        }

        if (!attributionsAvailable) {
            drawer.hideAttributionsButton()
        }
    }

    private fun setMusicPlayerVolume() {
        val savedVolumeValue = GlobalState.get(this, VOLUME_KEY, default = Gradation.YES.toString())
        musicPlayer.currentVolume = Gradation.valueOf(savedVolumeValue)
    }
    private fun volumeListener() {
        musicPlayer.nextVolumeMode()
        drawer.updateVolumeMode(musicPlayer.currentVolume)
        GlobalState.put(this, VOLUME_KEY, musicPlayer.currentVolume.toString())
    }

    private fun getCreatedMapsFiles() = SaveManager.loadMapFiles(this)
    private fun getCreatedMapsNames() = getCreatedMapsFiles().map { it.name }
    private fun getRawMapsNames(): List<String> {
        val rawResources = R.raw::class.members
        return rawResources.asSequence()
            .map { it.name }
            .filter { it.startsWith("map") }
            .filter { !it.contains("daa") }
            .filter { !it.contains("mac_guf") }
            .filter { !it.contains("cht_rum") }
            .filter { !it.contains("wat_abh") }
            .toList()
    }
    private fun getTotalNames() = getCreatedMapsNames() + getRawMapsNames()

    private fun redactMapListener() {
        controller.createdMapsListListener(CreatedMapsMode.REDACT, getTotalNames(), ::onMapSelected)
    }
    private fun playMapListener() {
        controller.createdMapsListListener(CreatedMapsMode.PLAY, getTotalNames(), ::onMapSelected)
    }
    private fun onMapSelected(index: Int, mode: CreatedMapsMode) {
        if (index == -1 && mode == CreatedMapsMode.REDACT) {
            startCreatorManager(LandsMap(
                getString(
                    NumberToStringId.getMapName(CREATOR_DEFAULT_MAP_NAME_NUMBER)
                )
            ))
            return
        }

        val rawMapsNames = getRawMapsNames()
        val rawIndex = index - getCreatedMapsNames().size
        val isMapRaw = rawIndex >= 0
        val map = if (isMapRaw) {
            SaveManager.loadMapFromRaw(this, rawMapsNames[rawIndex])
        } else {
            SaveManager.loadMap(getCreatedMapsFiles()[index])
        }

        if (mode == CreatedMapsMode.REDACT) {
            startCreatorManager(map)
        } else {
            val state = if (isMapRaw) {
                val mapName = rawMapsNames[rawIndex]
                SaveManager.loadInitState(this).apply {
                    mapGraph.rawConnectWith(InitData.SUB_INIT_MAP_INDEX, mapName)
                }
            } else {
                GameState65(MapGraph(map), map.name)
            }

            state.battleMode = BattleMode.PEACE
            state.moveMode = MoveMode.WALK
            state.rewindAvailable = true
            state.knowledge.setAllBasics()
            if (isMapRaw && rawMapsNames[rawIndex] == "map_tpl_rm8") {
                state.knowledge.setWindRequiem()
            }
            if (isMapRaw && rawMapsNames[rawIndex] == "map_fin") {
                state.knowledge.setWindRequiem()
                state.knowledge.setLakeRequiem()
                state.protagonist.killed = 63
            }
            launchCoroutine {
                drawer.fadeToBlack().join()
                startGameWithState(state)
            }
        }
    }

    private fun continueGame() {
        val state = SaveManager.loadState(this)
        launchCoroutine {
            drawer.fadeToBlack().join()
            startGameWithState(state)
        }
    }
    private fun newGame() = launchCoroutineOnMain {
        val activity = this@MainActivity
        drawer.fadeToWhite().join()
        val initManager = GameInitManager(activity)
        initManager.launch()
    }
    fun goToNewGame() = launchCoroutineOnMain {
        val activity = this@MainActivity
        GlobalState.clearWalkthroughFlags(activity)
        playIntro()
        val state = SaveManager.loadInitState(activity)
        state.flagsMaster.add(STARTED_GAME)
        if (randBoolean()) {
            state.countersMaster[WORLD_TRACE_NUMBER] = randInt(1000)
        }
        SaveManager.saveState(state, activity)
        startGameWithState(state)
    }

    private fun startGameWithState(state: GameState65) {
        launchCoroutineOnMain {
            musicPlayer.stopAllMusic()
            LandsManager(this@MainActivity, state).apply {
                init()
                processMap()
            }
        }
    }

    private fun startCreatorManager(map: LandsMap) {
        launchCoroutineOnMain {
            drawer.fadeToBlack(duration = 500L).join()
            musicPlayer.stopAllMusic()
            CreatorManager(this@MainActivity, map).start()
        }
    }

    private fun showHelp() {
        val knowledge = SaveManager.loadKnowledgeFromState(this)
        launchCoroutineOnMain {
            drawer.fadeToWhite().join()
            TrigramsSheetManager(this@MainActivity, knowledge).launch()
        }
    }

    private var attributionsEnterCounter = 0
    private fun showAttributions() {
        attributionsEnterCounter++
        if (attributionsEnterCounter % 5 == 0) {
            debug = !debug
            GlobalState.putBoolean(this, DEBUG_KEY, debug)
        }

        GlobalState.putBoolean(this, SOUND_TEST_AVAILABLE, attributionsEnterCounter == 13)

        launchCoroutineOnMain {
            drawer.fadeToWhite().join()
            AttributionsManager(this@MainActivity).launch()
        }
    }

    private fun showAbout() {
        launchCoroutineOnMain {
            drawer.fadeToWhite().join()
            AboutManager(this@MainActivity).launch()
        }
    }

    private suspend fun playIntro() {
        musicPlayer.stopAllMusic()
        launchCoroutineOnMain {
            IntroManager(this@MainActivity).start()
        }.join()
    }

    fun playToggleSwitchSound() {
        musicPlayer.playMusic(
            R.raw.sfx_menu2,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
    }

    private fun checkCreativeHeavenTweak() {
        val creativeHeavenBattleInProgress = GlobalState.getBoolean(this, CREATIVE_HEAVEN_BATTLE_IN_PROGRESS)
        if (creativeHeavenBattleInProgress) {
            drawer.creativeHeavenTouch()
        }
    }

    fun returnToMenu(playMusic: Boolean = true) = launchCoroutineOnMain {
        launch(playMusic)
    }

    fun exitGame() {
        finishAffinity()
        exitProcess(0)
    }

    enum class CreatedMapsMode {
        REDACT,
        PLAY
    }

    companion object {
        const val DEBUG_KEY = "debug"
        const val VOLUME_KEY = "volume"
    }
}