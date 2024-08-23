package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LetterReverseProjectile
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class BattleFieldPushingUpward : BattleFieldEnemy() {
    override val number: Int = 46
    override val hexagramSymbol: String = "䷭"
    override val nameId: Int
        get() = R.string.battlefield_pushing_upward_name

    override val centerSymbolColorId: Int = R.color.black
    override val centerSymbol: String = "\uD83D\uDD1D"

    override val outerSkinTrigram = Earth
    override val innerHeartTrigram = Wind

    override val defaultFace: String = "\uD83D\uDE43"
    override val damageReceivedFace: String = "\uD83D\uDE35"
    override val noDamageReceivedFace: String = "\uD83D\uDE43"

    override val animation = CharacterAnimation.ROTATE_FULL

    override val goNumbersToDelays = listOf(
        3 to tickTime,
        2 to tickTime,
        3 to tickTime * 2 / 5,
        2 to tickTime * 2 / 5,
        1 to tickTime * 2 / 5,
    )
    override val beatId: Int
        get() = R.raw.sfx_snap

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_pushing_upward_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_pushing_upward_d
        override fun getVictoryLine(): Int = R.string.battlefield_pushing_upward_v
    }

    private fun playSnapSound(activity: MainActivity) {
        activity.musicPlayer.playMusic(
            R.raw.sfx_snap_2,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
    }

    private var isRotated = false
    private val rotationJournal = mutableListOf<Boolean>()
    private fun rotate180(manager: BattleManager, isInstantly: Boolean = false) {
        isRotated = true
        launchCoroutineOnMain {
            if (!isInstantly) {
                playSnapSound(manager.activity)
                delay(tickTime)
            }
            manager.drawer.rotateScreen180()
        }
    }
    private fun rotate0(manager: BattleManager, isInstantly: Boolean = false) {
        isRotated = false
        launchCoroutineOnMain {
            if (!isInstantly) {
                playSnapSound(manager.activity)
                delay(tickTime)
            }
            manager.drawer.rotateScreen0()
        }
    }

    override fun onMoveStart(battleField: BattleField) {
        rotationJournal.clear()
    }

    override fun onTick(manager: BattleManager, tickNumber: Int) {
        if (tickNumber == 0) {
            if (rotationJournal.isNotEmpty()) {
                val isRotatedOnStart = rotationJournal[0]
                if (isRotatedOnStart) {
                    rotate180(manager, isInstantly = true)
                } else {
                    rotate0(manager, isInstantly = true)
                }
            } else {
                rotationJournal.add(isRotated)
            }
        }

        if (tickNumber % 4 == 2) {
            val numberInRotationJournal = tickNumber / 4 + 1
            while (numberInRotationJournal >= rotationJournal.size) {
                rotationJournal.add(randBoolean())
            }

            val isCurrentlyRotated = rotationJournal[numberInRotationJournal]
            if (isCurrentlyRotated) {
                if (!isRotated) {
                    rotate180(manager)
                }
            } else {
                if (isRotated) {
                    rotate0(manager)
                }
            }
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val protagonistCoordinates = battleField.protagonist.position
        battleField.addProjectile(
            LetterReverseProjectile(
                Coordinates(battleField.height, protagonistCoordinates.col),
                BattleFieldProjectile.Direction.DOWN,
                LETTERS.random(),
                COLORS.random()
            )
        )
        battleField.addProjectile(
            LetterReverseProjectile(
                Coordinates(-1, protagonistCoordinates.col),
                BattleFieldProjectile.Direction.UP,
                LETTERS.random(),
                COLORS.random()
            )
        )
        battleField.addProjectile(
            LetterReverseProjectile(
                Coordinates(protagonistCoordinates.row, -1),
                BattleFieldProjectile.Direction.LEFT,
                LETTERS.random(),
                COLORS.random()
            )
        )
        battleField.addProjectile(
            LetterReverseProjectile(
                Coordinates(protagonistCoordinates.row, battleField.width),
                BattleFieldProjectile.Direction.RIGHT,
                LETTERS.random(),
                COLORS.random()
            )
        )
    }

    companion object {
        val LETTERS = arrayListOf("D", "A", "N", "C", "E", "!")
        val COLORS = arrayListOf(R.color.red, R.color.blue, R.color.true_green, R.color.true_yellow, R.color.orange)
    }
}