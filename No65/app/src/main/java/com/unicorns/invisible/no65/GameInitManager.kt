package com.unicorns.invisible.no65

import android.widget.TextView
import com.unicorns.invisible.no65.controller.MenuItemController
import com.unicorns.invisible.no65.databinding.ActivityInitBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.GameInitDrawer
import kotlinx.coroutines.delay
import kotlin.properties.Delegates


class GameInitManager(
    override val activity: MainActivity
) : MenuItemManager {
    override val binding = ActivityInitBinding.inflate(activity.layoutInflater)
    override val controller = object : MenuItemController() {
        override val activity: MainActivity = this@GameInitManager.activity
        override val buttonsBlockingToListeners: Map<TextView, () -> Unit>
            get() = mapOf(
                binding.initResultButton to ::startNewGame,
                binding.goBack to ::exit,
            )
        override val buttonsFreeToListeners: Map<TextView, () -> Unit>
            get() = mapOf(
                binding.magicButton to ::magicButtonListener,
            )
    }
    override val drawer = GameInitDrawer(activity, binding)

    override fun setupContent() {
        drawer.showControls()
        drawer.setStatusText(R.string.game_init_loading)
    }
    override suspend fun launch(playMusic: Boolean) {
        super.launch(playMusic)
        delay(5000L)
        onInitSuccess()
    }

    private var areButtonsShown by Delegates.observable(true) { _, _, new ->
        if (new) {
            drawer.showControls()
        } else {
            drawer.showControlsExplained()
        }
    }
    private fun magicButtonListener() {
        areButtonsShown = !areButtonsShown
    }

    private fun onInitSuccess() {
        drawer.setStatusText(R.string.game_init_success)
        drawer.setInitInfo()
        drawer.showStartGameButton()
    }

    private fun startNewGame() {
        launchCoroutineOnMain {
            drawer.stop()
            drawer.fadeToBlack().join()
            activity.goToNewGame()
        }
    }
}