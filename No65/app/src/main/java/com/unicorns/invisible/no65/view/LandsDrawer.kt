package com.unicorns.invisible.no65.view

import android.view.View
import android.widget.TableLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityLandsBinding
import com.unicorns.invisible.no65.databinding.D99LayoutBinding
import com.unicorns.invisible.no65.databinding.DeliveranceAdLayoutBinding
import com.unicorns.invisible.no65.model.lands.cell.character.CellNonStaticCharacter
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.CellTheCreature
import com.unicorns.invisible.no65.model.lands.cell.character.npc.CellNPC
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.SpeechUtils
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.lands.DeliveranceAdAnimator
import com.unicorns.invisible.no65.view.lands.LandsMessageStoppedException
import com.unicorns.invisible.no65.view.lands.NumberToStringId
import com.unicorns.invisible.no65.view.lands.NumberToStringId.Companion.MISSING_NAME_ID
import com.unicorns.invisible.no65.view.music.MusicPlayer
import com.unicorns.invisible.no65.view.speech.SpeechProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates


abstract class LandsDrawer(
    activity: MainActivity,
    protected val binding: ActivityLandsBinding,
    landsWidth: Int,
    landsHeight: Int,
): LandsFieldDrawer(activity, landsWidth, landsHeight) {
    private val facePanel
        get() = binding.speechLineFaceLayout
    private val characterNamePanel
        get() = binding.characterName
    private val locationName
        get() = binding.locationName
    private val locationNameLayout
        get() = binding.locationNameLayout
    private val controlsPanel
        get() = binding.controls
    private val speechTextView
        get() = binding.speechTextView
    private val speechTextViewLayout
        get() = binding.speechTextViewLayout
    private val skipMessage
        get() = binding.skipMessage

    private val facePanelHead
        get() = binding.speechTalkingHead
    private val facePanelDiv
        get() = binding.speechTalkingHeadDiv
    private val facePanelMod
        get() = binding.speechTalkingHeadMod

    private val includeLayout
        get() = binding.includeLayout

    private val cdp
        get() = binding.cdp

    private var stopped = false

    override val landsTable: TableLayout
        get() = binding.landsTable

    override val loadingLayout: ConstraintLayout
        get() = binding.loadingLayout

    override val screen: View
        get() = binding.includeLayout

    var speechLayoutShowOverride by Delegates.observable(false) { _, _, new ->
        if (new) {
            clearText()
            preShowMessage()
        } else {
            postShowMessage()
            clearText()
        }
    }

    override fun initSecondary() {
        speechTextView.setMinTextSize(2f)
    }

    override fun drawMap(map: LandsMap, centerCell: Coordinates) {
        super.drawMap(map, centerCell)
        launchCoroutineOnMain {
            val mapName = if (map.nameId != MISSING_NAME_ID) {
                activity.getString(NumberToStringId.getMapName(map.nameId))
            } else {
                map.name
            }
            if (locationName.text != mapName) {
                locationName.text = mapName
            }
        }
    }

    private var currentJob: Job? = null
    private suspend fun showMessageRaw(
        message: String,
        color: Int,
        tapSoundId: Int,
        delayAfterMessage: Long
    ) = suspendCoroutine { cont ->
        if (message == "") {
            cont.resume(null)
            return@suspendCoroutine
        }

        currentJob = launchCoroutineOnMain {
            activity.musicPlayer.playMusic(
                tapSoundId,
                MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = true
            )
            speechTextView.setTextColor(activity.getColorById(color))

            repeat(message.length) {
                delay(SpeechProperties.MILLISECONDS_PER_SYMBOL)
                speechTextView.text = message.subSequence(0, it + 1)
            }
            activity.musicPlayer.stopMusicByResourceId(tapSoundId)
            delay(delayAfterMessage)
        }.apply {
            invokeOnCompletion {
                activity.musicPlayer.stopMusicByResourceId(tapSoundId)
                cont.resume(it)
            }
        }
    }

    private fun beforeMessageBatch() {
        stopPrintingMessage(LandsMessageStoppedException.Reason.OVERRIDDEN_BY_ANOTHER_MESSAGE)
        clearText()
        if (!speechLayoutShowOverride) {
            preShowMessage()
        }
    }
    private fun afterMessageBatch() {
        if (!speechLayoutShowOverride) {
            postShowMessage()
        }
        clearText()
    }
    private suspend fun showStringMessages(
        messages: List<String>,
        color: Int,
        tapSoundId: Int,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE,
    ) {
        beforeMessageBatch()
        for (message in messages) {
            val jobResult = showMessageRaw(message, color, tapSoundId, delayAfterMessage)
            if (
                jobResult is LandsMessageStoppedException &&
                jobResult.reason != LandsMessageStoppedException.Reason.SKIPPED
            ) {
                return
            }
        }
        afterMessageBatch()
    }
    suspend fun showMessages(
        messageIds: List<Int>,
        color: Int,
        tapSoundId: Int,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE,
    ) {
        val messages = messageIds.map { activity.getString(it) }
        showStringMessages(messages, color, tapSoundId, delayAfterMessage)
    }
    suspend fun showMultilineMessage(
        messageId: Int,
        color: Int,
        tapSoundId: Int,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE
    ) {
        val messages = activity.getString(messageId).split("\n")
        showStringMessages(messages, color, tapSoundId, delayAfterMessage)
    }
    suspend fun showMessage(
        messageId: Int,
        color: Int = R.color.black,
        tapSoundId: Int,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE
    ) {
        val message = activity.getString(messageId)
        showStringMessages(listOf(message), color, tapSoundId, delayAfterMessage)
    }
    suspend fun showMessageFormatted(
        messageId: Int,
        color: Int = R.color.black,
        tapSoundId: Int,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE,
        vararg formatArgs: Any
    ) {
        val argsList = formatArgs.asList()
        val processedArgsList = SpeechUtils.processFormatArgs(argsList, activity)
        val message = activity.getString(messageId, *processedArgsList.toTypedArray())
        showStringMessages(listOf(message), color, tapSoundId, delayAfterMessage)
    }

    fun preShowMessage() = launchCoroutineOnMain {
        locationNameLayout.visibility = View.INVISIBLE
        controlsPanel.visibility = View.INVISIBLE
        speechTextViewLayout.visibility = View.VISIBLE
    }
    fun postShowMessage() = launchCoroutineOnMain {
        locationNameLayout.visibility = View.VISIBLE
        controlsPanel.visibility = View.VISIBLE
        speechTextViewLayout.visibility = View.INVISIBLE
    }
    private fun clearText() = launchCoroutineOnMain {
        speechTextView.text = ""
    }

    fun stopPrintingMessage(reason: LandsMessageStoppedException.Reason) {
        if (currentJob?.isCompleted == false) {
            currentJob?.cancel(LandsMessageStoppedException(reason))
        }
    }

    fun setSkipMessageVisibility(isVisible: Boolean) = launchCoroutineOnMain {
        skipMessage.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private var talkingHeadsShown: Int = 0
    fun showTalkingHead(cell: CellNonStaticCharacter) = launchCoroutineOnMain {
        characterNamePanel.visibility = View.VISIBLE
        facePanel.visibility = View.VISIBLE
        facePanelHead.text = cell.faceCell
        if (cell is CellTheCreature) {
            facePanelDiv.text = ""
            facePanelMod.text = ""
        } else {
            facePanelDiv.text = (cell.id / 10).toString()
            facePanelMod.text = (cell.id % 10).toString()
        }

        characterNamePanel.text = when (cell) {
            is CellNPC -> {
                val name = activity.getString(cell.nameId)
                activity.getString(R.string.message_character_name).format(name)
            }
            is CellProtagonist -> {
                activity.getString(R.string.you)
            }
            else -> {
                activity.getString(R.string.message_character_name).format(activity.getString(R.string.name_unknown))
            }
        }

        talkingHeadsShown++
    }
    fun updateTalkingHeadFace(cell: CellNonStaticCharacter) = launchCoroutineOnMain {
        facePanelHead.text = cell.faceCell
    }
    fun showUnknownTalkingHead() = launchCoroutineOnMain {
        characterNamePanel.visibility = View.VISIBLE
        facePanel.visibility = View.VISIBLE
        facePanelHead.text = activity.getString(R.string.unknown_face)
        facePanelDiv.text = activity.getString(R.string.unknown_number)
        facePanelMod.text = activity.getString(R.string.unknown_number)

        characterNamePanel.text = activity.getString(R.string.message_character_name).format(activity.getString(R.string.name_unknown))

        talkingHeadsShown++
    }
    fun hideTalkingHead() = launchCoroutineOnMain {
        if (--talkingHeadsShown == 0) {
            hideTalkingHeadPanel()
        }
    }
    private fun hideTalkingHeadPanel() {
        facePanel.visibility = View.GONE
        characterNamePanel.visibility = View.GONE
        characterNamePanel.text = ""
    }

    suspend fun showMessagesPhoneWithUnknownHead(
        messages: List<Int>,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE
    ) {
        showUnknownTalkingHead()
        showMessages(
            messages,
            color = R.color.black,
            tapSoundId = R.raw.sfx_phone,
            delayAfterMessage = delayAfterMessage
        )
        hideTalkingHead()
    }

    suspend fun showUnknownCharacterMessages(
        character: CellNonStaticCharacter,
        messagesIds: List<Int>,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE,
    ) {
        showUnknownTalkingHead()
        showMessages(messagesIds, character.speechColor, character.speechSound, delayAfterMessage)
        hideTalkingHead()
    }

    suspend fun showCharacterMessages(
        character: CellNonStaticCharacter,
        messageIds: List<Int>,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE,
    ) {
        val messages = messageIds.map { activity.getString(it) }
        if (messages.isEmpty() || messages.size == 1 && messages[0] == "") {
            return
        }
        showTalkingHead(character)
        showStringMessages(messages, character.speechColor, character.speechSound, delayAfterMessage)
        hideTalkingHead()
    }


    suspend fun showDeliveranceAd() {
        val binding = withContext(Dispatchers.Main) {
            includeLayout.visibility = View.VISIBLE
            activity.musicPlayer.playMusicSuspendTillStart(
                R.raw.cutscene_the_advert,
                behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
                isLooping = false
            )
            DeliveranceAdLayoutBinding.inflate(activity.layoutInflater, includeLayout, true)
        }
        DeliveranceAdAnimator(activity, binding).animate()
        withContext(Dispatchers.Main) {
            includeLayout.visibility = View.GONE
            includeLayout.removeAllViews()
            activity.musicPlayer.resumeAllMusic()
        }
    }

    suspend fun showD99Layout(withFadeIn: Boolean): D99Drawer {
        val binding = withContext(Dispatchers.Main) {
            includeLayout.visibility = View.VISIBLE
            if (withFadeIn) {
                fadeInWhite()
            }
            D99LayoutBinding.inflate(activity.layoutInflater, includeLayout, true)
        }
        return D99Drawer(activity, binding)
    }

    suspend fun fadeInWhite(time: Long = 2500L) = fadeToWhite(time).join()
    suspend fun fadeInBlack(time: Long = 2500L) = fadeToBlack(time).join()

    fun hideIncludeLayout() = launchCoroutineOnMain {
        includeLayout.visibility = View.INVISIBLE
    }

    fun makeMessageBackgroundBlack() = launchCoroutineOnMain {
        speechTextView.setBackgroundColor(activity.getColorById(R.color.black))
    }
    fun makeMessageBackgroundWhite() = launchCoroutineOnMain {
        speechTextView.setBackgroundColor(activity.getColorById(R.color.white))
    }

    fun stop() = launchCoroutineOnMain {
        stopped = true
        cdp.stop()
        stopPrintingMessage(LandsMessageStoppedException.Reason.DRAWER_HALTED)
    }
}