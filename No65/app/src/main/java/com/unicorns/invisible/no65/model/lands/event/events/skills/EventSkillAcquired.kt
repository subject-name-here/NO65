package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.controller.ElementsGestureType
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.elements.monogram.MonogramGestureBijection
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.lands.StringResourceForFormatWrapper

abstract class EventSkillAcquired(private val bookCell: SkillBook): Event() {
    protected abstract val skillNameId: Int
    protected abstract val effectDescriptionId: Int
    protected abstract val trigram: Trigram
    protected open val isRequiem = false

    final override val thisEventLambda: suspend Event.(LandsManager) -> Unit
        get() = { manager ->
            if (isRequiem) {
                acquireSkillRequiem(manager)
            } else {
                acquireSkill(manager)
            }
        }

    private suspend fun acquireSkill(
        manager: LandsManager
    ) {
        val gameState = manager.gameState
        if (gameState !is GameState65) return

        gameState.currentMap.removeCellFromTop(bookCell)

        if (gameState.knowledge.knowsTrigram(trigram)) {
            manager.wrapCutsceneSkippable {
                showYouAlreadyKnowSkill(manager)
            }
            return
        }

        gameState.knowledge.learnTrigram(trigram)

        manager.wrapCutscene {
            drawer.showMessageFormatted(
                R.string.skills_acquired,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap,
                formatArgs = arrayOf(StringResourceForFormatWrapper(skillNameId))
            )
            showDescription(manager)
        }
    }

    private suspend fun acquireSkillRequiem(
        manager: LandsManager
    ) {
        val gameState = manager.gameState
        if (gameState !is GameState65) return

        gameState.currentMap.removeCellFromTop(bookCell)

        when {
            !gameState.knowledge.knowsTrigram(trigram) -> {
                manager.wrapCutsceneSkippable {
                    drawer.showMessage(
                        R.string.skills_requiem_without_basic,
                        color = R.color.black,
                        tapSoundId = R.raw.sfx_tap,
                    )
                }
                return
            }
            gameState.knowledge.knowsRequiem(trigram) -> {
                manager.wrapCutsceneSkippable {
                    showYouAlreadyKnowSkill(manager)
                }
                return
            }
        }

        gameState.knowledge.learnRequiem(trigram)

        manager.wrapCutscene {
            drawer.showMessageFormatted(
                R.string.skills_acquired_requiem,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap,
                formatArgs = arrayOf(StringResourceForFormatWrapper(skillNameId))
            )
            showDescription(manager)
        }
    }

    private suspend fun showYouAlreadyKnowSkill(manager: LandsManager) {
        manager.drawer.showMessageFormatted(
            R.string.skills_you_already_know_skill,
            color = R.color.black,
            tapSoundId = R.raw.sfx_tap,
            formatArgs = arrayOf(StringResourceForFormatWrapper(skillNameId)),
        )
    }

    private suspend fun showDescription(manager: LandsManager) {
        val monograms = trigram.getMonograms()
        with(manager) {
            drawer.showMessage(
                effectDescriptionId,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap
            )
            drawer.showMessageFormatted(
                R.string.skills_has_trigram,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap,
                formatArgs = arrayOf(
                    StringResourceForFormatWrapper(skillNameId),
                    StringResourceForFormatWrapper(monograms.first.getNameId()),
                    StringResourceForFormatWrapper(monograms.second.getNameId()),
                    StringResourceForFormatWrapper(monograms.third.getNameId()),
                )
            )
            drawer.showMessageFormatted(
                R.string.skills_how_to_cast,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap,
                formatArgs = arrayOf(
                    StringResourceForFormatWrapper(skillNameId),
                    StringResourceForFormatWrapper(MonogramGestureBijection.getGestureType(monograms.first).getNameId()),
                    StringResourceForFormatWrapper(MonogramGestureBijection.getGestureType(monograms.second).getNameId()),
                    StringResourceForFormatWrapper(MonogramGestureBijection.getGestureType(monograms.third).getNameId()),
                )
            )
        }
    }

    private fun ElementsGestureType.getNameId(): Int = when (this) {
        ElementsGestureType.SWIPE -> R.string.skills_swipe
        ElementsGestureType.DOUBLE_TAP -> R.string.skills_double_tap
    }
}