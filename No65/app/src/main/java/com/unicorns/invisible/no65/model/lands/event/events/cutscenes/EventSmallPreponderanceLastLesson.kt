package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.SmallPreponderance
import com.unicorns.invisible.no65.model.lands.event.Event


class EventSmallPreponderanceLastLesson(characterCell: SmallPreponderance): Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_small_preponderance_last_lesson_1,
            R.string.lands_small_preponderance_last_lesson_2,
            R.string.lands_small_preponderance_last_lesson_3,
            R.string.lands_small_preponderance_last_lesson_4,
            R.string.lands_small_preponderance_last_lesson_5,
            R.string.lands_small_preponderance_last_lesson_6,
            R.string.lands_small_preponderance_last_lesson_7,
            R.string.lands_small_preponderance_last_lesson_8,
        ))

        (gameState.protagonist as CellProtagonist).youState = CellProtagonist.YouState.ONLY_YOU
    }
})