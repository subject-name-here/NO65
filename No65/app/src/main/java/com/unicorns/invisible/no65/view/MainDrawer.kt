package com.unicorns.invisible.no65.view

import android.view.View
import com.unicorns.invisible.no65.Gradation
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityMainBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class MainDrawer(override val activity: MainActivity) : FadeDrawer {
    private val binding: ActivityMainBinding
        get() = activity.binding as ActivityMainBinding

    override val screen: View
        get() = binding.mainScreen

    private val gameNameView
        get() = binding.gameName

    private val continueButton
        get() = binding.continueButton
    private val newGameButton
        get() = binding.newGameButton
    private val redactMapButton
        get() = binding.redactMap
    private val playMapButton
        get() = binding.playMap
    private val volumeModeButton
        get() = binding.volumeModeButton
    private val helpButton
        get() = binding.helpTrigrams
    private val attrButton
        get() = binding.attributions


    fun hideContinueButton() = launchCoroutineOnMain {
        continueButton.visibility = View.GONE
    }

    fun hideNewGameButton() = launchCoroutineOnMain {
        newGameButton.visibility = View.GONE
    }

    fun hideCreatedMapsButtons() = launchCoroutineOnMain {
        redactMapButton.visibility = View.GONE
        playMapButton.visibility = View.GONE
    }

    fun updateVolumeMode(value: Gradation) = launchCoroutineOnMain {
        volumeModeButton.text = activity.getString(R.string.volume).format(value.name)
    }

    fun hideHelpButton() = launchCoroutineOnMain {
        helpButton.visibility = View.GONE
    }

    fun hideAttributionsButton() = launchCoroutineOnMain {
        attrButton.visibility = View.GONE
    }

    fun creativeHeavenTouch() = launchCoroutineOnMain {
        gameNameView.text = activity.getString(R.string.game_name_ch)
        continueButton.text = activity.getString(R.string.continue_game_ch)
    }
}