package com.unicorns.invisible.no65.view

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.children
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.*
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.music.MusicPlayer
import com.unicorns.invisible.no65.view.speech.SpeechProperties
import kotlinx.coroutines.*


class IntroDrawer(
    override val activity: MainActivity,
    private val binding: ActivityIntroBinding
) : FadeDrawer {
    private val speechTextViewLayout
        get() = binding.introTextViewLayout
    private val speechTextView
        get() = binding.introTextView
    override val screen
        get() = binding.introScreen

    private val slide1Binding: IntroSlide1Binding
        get() = binding.includeSlide1
    private val slide2Binding: IntroSlide2Binding
        get() = binding.includeSlide2
    private val slide3Binding: IntroSlide3Binding
        get() = binding.includeSlide3
    private val slide4Binding: IntroSlide4Binding
        get() = binding.includeSlide4

    private val slide1Content: ConstraintLayout
        get() = slide1Binding.slide1Content
    private val slide2Content: ConstraintLayout
        get() = slide2Binding.slide2Content
    private val slide2Slide1Content: ConstraintLayout
        get() = slide2Binding.includeSlide1.slide1Content
    private val slide3Content: ConstraintLayout
        get() = slide3Binding.slide3Lands
    private val slide3Cells
        get() = listOf(
            slide3Binding.slide3Cell1.landsCell,
            slide3Binding.slide3Cell2.landsCell,
            slide3Binding.slide3Cell3.landsCell,
            slide3Binding.slide3Cell4.landsCell,
            slide3Binding.slide3Cell5.landsCell,
            slide3Binding.slide3Cell6.landsCell,
            slide3Binding.slide3Cell7.landsCell,
            slide3Binding.slide3Cell8.landsCell,
            slide3Binding.slide3Cell9.landsCell,
        )
    private val slideCellsWithBodiesBindings
        get() = listOf(
            slide3Binding.slide3Cell4,
            slide3Binding.slide3Cell6,
        )
    private val slideCellsWithBodiesMovementBindings
        get() = listOf(
            slide3Binding.slide3Cell6,
            slide3Binding.slide3Cell5,
            slide3Binding.slide3Cell8,
            slide3Binding.slide3Cell9,
        )
    private val slide3Dividers
        get() = listOf(
            slide3Binding.slide3Divider1,
            slide3Binding.slide3Divider2,
            slide3Binding.slide3Divider3,
            slide3Binding.slide3Divider4,
        )
    private val slide4Content: ConstraintLayout
        get() = slide4Binding.slide4Lands
    private val slide4CellBinding: LandsCellBinding
        get() = slide4Binding.slide4Cell
    private val slide4Cell: ConstraintLayout
        get() = slide4Binding.slide4Cell.landsCell

    private val yinSymbolSlide1
        get() = slide1Binding.yin
    private val yangSymbolSlide1
        get() = slide1Binding.yang

    private val yinSymbolSlide2
        get() = slide2Binding.includeSlide1.yin
    private val yangSymbolSlide2
        get() = slide2Binding.includeSlide1.yang

    fun slide1Appear() = launchCoroutineOnMain {
        slide1Content.visibility = View.VISIBLE
        val animation1 = AlphaAnimation(0.0f, 1.0f)
        animation1.duration = 2000
        slide1Content.startAnimation(animation1)
    }

    fun rotateSymbolsSlide1() = rotateSymbols(yinSymbolSlide1, yangSymbolSlide1)
    fun rotateSymbolsSlide2() = rotateSymbols(yinSymbolSlide2, yangSymbolSlide2)

    private var currentJob: Job? = null
    private fun rotateSymbols(yin: TextView, yang: TextView) {
        currentJob = launchCoroutineOnMain {
            val properties = listOf("rotation", "rotationX", "rotationY")
            while (isActive) {
                properties.forEach { property ->
                    val rotation1 = ObjectAnimator.ofFloat(yin, property, 0f, 360f)
                    rotation1.duration = 2500
                    rotation1.start()

                    val rotation2 = if (property == "rotation") {
                        ObjectAnimator.ofFloat(yang, property, 180f, 540f)
                    } else {
                        ObjectAnimator.ofFloat(yang, property, 0f, 360f)
                    }
                    rotation2.duration = 2500
                    rotation2.start()
                    delay(2500L)
                }
            }
        }
    }
    fun stopRotation() {
        currentJob?.cancel()
        currentJob = null
    }


    fun slide1ToSlide2Transition() = launchCoroutineOnMain {
        fadeToGrey()
    }
    fun hideScreen() = launchCoroutineOnMain {
        screen.visibility = View.GONE
        screen.setBackgroundColor(activity.getColorById(R.color.transparent))
    }

    fun slide2Appear() = launchCoroutineOnMain {
        slide2Content.visibility = View.VISIBLE
        slide2Slide1Content.visibility = View.VISIBLE
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.fillAfter = true
        animation.duration = 500
        slide2Slide1Content.startAnimation(animation)
    }

    fun slide2ShowTrigrams() = launchCoroutineOnMain {
        slide2Content.children.forEach {
            it.visibility = View.VISIBLE
            if (it is TextView) {
                val animation = AlphaAnimation(0.0f, 1.0f)
                animation.duration = 2500
                it.startAnimation(animation)
            }
        }
    }

    fun slide2ToSlide3Transition() = launchCoroutineOnMain {
        fadeToBlack()
    }

    fun slide3Appear() = launchCoroutineOnMain {
        slide3Content.visibility = View.VISIBLE
        val animation1 = AlphaAnimation(0.0f, 1.0f)
        animation1.duration = 1000
        slide3Content.startAnimation(animation1)
    }

    fun slide3CellsCreation() = launchCoroutineOnMain {
        slide3Dividers.forEach { divider ->
            divider.visibility = View.INVISIBLE
        }
        slide3Cells.forEach { view ->
            view.getChildAt(1).visibility = View.INVISIBLE
            view.getChildAt(0).setBackgroundColor(activity.getColorById(R.color.sand))
            delay(200L)
        }
    }

    fun slide3BodiesCreation() = launchCoroutineOnMain {
        slideCellsWithBodiesBindings.forEach { binding ->
            val cell = binding.cellSymbol
            cell.setBackgroundColor(activity.getColorById(R.color.sand))
            val bodyBinding = binding.cellCharacter
            bodyBinding.root.visibility = View.VISIBLE
            bodyBinding.cell1.text = ""
            bodyBinding.cell4.text = ""
            delay(200L)
        }
    }

    fun slide3BodiesLife() = launchCoroutineOnMain {
        slideCellsWithBodiesBindings.forEach { binding ->
            binding.cellCharacter.cell4.text = activity.getString(R.string.sample_character_center_symbol)
            delay(200L)
        }
    }

    fun slide3BodiesMind() = launchCoroutineOnMain {
        slideCellsWithBodiesBindings.forEach { binding ->
            binding.cellCharacter.cell1.text = activity.getString(R.string.sample_character_face)
            delay(200L)
        }
    }

    fun slide3BodiesBattle() = launchCoroutineOnMain {
        val binding = slideCellsWithBodiesBindings[0]
        binding.cellCharacter.root.visibility = View.INVISIBLE
        val cell = binding.cellSymbol
        cell.text = activity.getString(R.string.dead_body_symbol)
        cell.setTextColor(activity.getColorById(R.color.black))
    }

    fun slide3BodiesTime() = launchCoroutineOnMain {
        val binding = slideCellsWithBodiesBindings[1]
        repeat(10) {
            binding.cellCharacter.cell4.text = it.toString()
            delay(200L)
        }
        binding.cellCharacter.cell4.text = activity.getString(R.string.sample_character_center_symbol)
    }

    fun slide3BodiesMovement() = launchCoroutineOnMain {
        slideCellsWithBodiesMovementBindings.forEachIndexed { index, oldBinding ->
            oldBinding.cellCharacter.root.visibility = View.INVISIBLE

            val newIndex = (index + 1) % slideCellsWithBodiesMovementBindings.size
            val newBidning = slideCellsWithBodiesMovementBindings[newIndex]
            newBidning.cellCharacter.root.visibility = View.VISIBLE
            delay(400L)
        }
    }


    fun slide3ToSlide4Transition() = launchCoroutineOnMain {
        fadeToBlack()
    }

    fun slide4Appear() = launchCoroutineOnMain {
        slide4Content.visibility = View.VISIBLE
        slide4Content.children.forEach {
            it.visibility = View.INVISIBLE
        }
        val animation1 = AlphaAnimation(0.0f, 1.0f)
        animation1.duration = 1000
        slide4Content.startAnimation(animation1)
    }
    fun slide4BannermanAppear() = launchCoroutineOnMain {
        slide4Cell.visibility = View.VISIBLE
        val characterBinding = slide4CellBinding.cellCharacter
        characterBinding.characterCellsHolder.children.forEach {
            if (it is TextView) {
                it.setTextColor(activity.getColorById(R.color.white))
            }
        }
        characterBinding.cell0.text = activity.getString(R.string.bannerman_unknown_feature)
        characterBinding.cell1.text = activity.getString(R.string.bannerman_face_hidden)
        characterBinding.cell2.text = activity.getString(R.string.bannerman_unknown_feature)
        characterBinding.cell4.text = activity.getString(R.string.bannerman_unknown_feature)

        val animation1 = AlphaAnimation(0.0f, 1.0f)
        animation1.duration = 2000
        slide4Cell.startAnimation(animation1)
    }

    fun slide4CellBorderAppear() = launchCoroutineOnMain {
        slide4Content.children.forEach {
            if (it !is ConstraintLayout && it !is Guideline) {
                it.visibility = View.VISIBLE
                val animation1 = AlphaAnimation(0.0f, 1.0f)
                animation1.duration = 1000
                it.startAnimation(animation1)
            }
        }
    }

    fun slide4Outro() = launchCoroutineOnMain {
        fadeToBlack()
    }


    fun startShowingMessages(
        backgroundColor: Int
    ) = launchCoroutineOnMain {
        speechTextViewLayout.visibility = View.VISIBLE
        speechTextViewLayout.setBackgroundColor(activity.getColorById(backgroundColor))
    }
    fun showMessage(
        messageId: Int,
        textColor: Int,
        backgroundColor: Int
    ) = launchCoroutineOnMain {
        val message = activity.getString(messageId)
        activity.musicPlayer.playMusic(
            R.raw.sfx_tap,
            MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = true
        )
        speechTextView.setTextColor(activity.getColorById(textColor))
        speechTextViewLayout.setBackgroundColor(activity.getColorById(backgroundColor))

        repeat(message.length) {
            delay(SpeechProperties.MILLISECONDS_PER_SYMBOL)
            speechTextView.text = message.subSequence(0, it + 1).toString()
        }
        activity.musicPlayer.stopMusicByResourceId(R.raw.sfx_tap)
    }

    fun clearMessage() = launchCoroutineOnMain {
        speechTextView.text = ""
    }


    fun slide1Hide() = launchCoroutineOnMain {
        slide1Content.visibility = View.GONE
    }
    fun slide2Hide() = launchCoroutineOnMain {
        slide2Content.visibility = View.GONE
    }
    fun slide3Hide() = launchCoroutineOnMain {
        slide3Content.visibility = View.GONE
    }
    fun slide4Hide() = launchCoroutineOnMain {
        slide4Content.visibility = View.GONE
    }
}