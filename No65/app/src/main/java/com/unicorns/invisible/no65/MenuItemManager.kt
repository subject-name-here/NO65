package com.unicorns.invisible.no65

import androidx.viewbinding.ViewBinding
import com.unicorns.invisible.no65.controller.MenuItemController
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.FadeDrawer
import com.unicorns.invisible.no65.view.music.MusicPlayer


interface MenuItemManager {
    val activity: MainActivity
    val binding: ViewBinding
    val drawer: FadeDrawer
    val controller: MenuItemController

    suspend fun launch(playMusic: Boolean = false) {
        activity.setContentView(binding.root)
        controller.setupButtons()
        setupContent()

        if (playMusic) {
            activity.musicPlayer.playMusic(
                R.raw.main_menu,
                isLooping = true,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL
            )
        }

        drawer.fadeFromWhite().join()
    }

    fun setupContent() {}

    fun exit() {
        launchCoroutine {
            drawer.fadeToWhite().join()
            returnBack()
        }
    }
    fun returnBack() {
        activity.returnToMenu(playMusic = false)
    }
}