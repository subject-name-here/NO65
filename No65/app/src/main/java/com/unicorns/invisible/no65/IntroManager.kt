package com.unicorns.invisible.no65

import com.unicorns.invisible.no65.databinding.ActivityIntroBinding
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.IntroDrawer
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class IntroManager(val activity: MainActivity) {
    private val drawer: IntroDrawer

    init {
        val binding = ActivityIntroBinding.inflate(activity.layoutInflater)
        activity.setContentView(binding.root)
        drawer = IntroDrawer(activity, binding)
    }

    suspend fun start() {
        delay(5000L)
        drawer.startShowingMessages(R.color.black)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide1_1,
                textColor = R.color.white,
                backgroundColor = R.color.black
            ).join()
            delay(2500L)
            drawer.clearMessage()
        }
        delay(7000L)
        activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.cutscene_intro,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        delay(500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide1_2,
                textColor = R.color.white,
                backgroundColor = R.color.black
            ).join()
            delay(2500L)
            drawer.clearMessage()
        }

        drawer.slide1Appear()
        delay(2500L)
        drawer.rotateSymbolsSlide1()
        delay(5000L)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide1_3,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(5000L)
        drawer.slide1ToSlide2Transition()
        delay(2500L)
        drawer.clearMessage()
        drawer.stopRotation()
        drawer.slide1Hide()

        drawer.slide2Appear()
        drawer.startShowingMessages(R.color.grey)
        drawer.hideScreen()
        drawer.rotateSymbolsSlide2()
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide2_1,
                textColor = R.color.black,
                backgroundColor = R.color.grey
            )
        }
        delay(2500L)
        drawer.slide2ShowTrigrams()
        delay(5000L)
        drawer.clearMessage()
        delay(2500L)

        drawer.slide2ToSlide3Transition()
        delay(2500L)
        drawer.stopRotation()
        drawer.slide2Hide()

        drawer.startShowingMessages(R.color.black)
        drawer.hideScreen()

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_1,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3Appear()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_2,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3CellsCreation()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_3,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3BodiesCreation()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_4,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3BodiesLife()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_5,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3BodiesMind()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_6,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3BodiesBattle()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_7,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3BodiesTime()
        delay(2500L)

        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_8,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide3BodiesMovement()
        delay(2500L)
        drawer.clearMessage()

        delay(2500L)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide3_9,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }

        delay(5000L)
        drawer.clearMessage()
        delay(2500L)

        drawer.slide3ToSlide4Transition()
        delay(2500L)
        drawer.slide3Hide()

        drawer.startShowingMessages(R.color.black)
        drawer.hideScreen()
        drawer.slide4Appear()

        delay(2500L)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide4_1,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(7500L)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide4_2,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide4BannermanAppear()
        delay(5000L)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide4_3,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(7500L)
        launchCoroutine {
            drawer.showMessage(
                R.string.intro_slide4_4,
                textColor = R.color.white,
                backgroundColor = R.color.black
            )
        }
        delay(2500L)
        drawer.slide4CellBorderAppear()
        delay(7500L)
        drawer.slide4Outro()
        delay(2500L)
        drawer.slide4Hide()
        delay(2500L)
    }
}