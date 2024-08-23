package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.UpcomingFire
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.UpcomingFireYellowOverdrive
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CF_BATTLE_KNOWS_ABOUT_EVASIONS
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CF_BATTLE_KNOWS_HOW_TO_DAMAGE
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.FOUGHT_WITH_CF
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.view.music.MusicPlayer


class BattleFieldClingingFire(private val isFromExtras: Boolean = false) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 30
    override val hexagramSymbol: String = "䷝"
    override val nameId: Int
        get() = R.string.battlefield_clinging_fire_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "\uD83D\uDD25"

    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Fire

    override val defaultFace: String = "\uD83D\uDC6E"
    override val damageReceivedFace: String = "\uD83D\uDE23"
    override val noDamageReceivedFace: String = "\uD83D\uDC6E"

    override val evadesSimpleAttacks: Boolean = true
    override var musicThemeId: Int = R.raw.battle_clinging_fire1

    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT_HORIZONTAL

    private var knowsAboutEvasion = false
    private var knowsHowToDamage = false
    override suspend fun onBattleBegins(manager: BattleManager) {
        if (isFromExtras) {
            knowsAboutEvasion = true
            knowsHowToDamage = true
        } else {
            GlobalState.putBoolean(manager.activity, FOUGHT_WITH_CF, true)
            knowsAboutEvasion = GlobalState.getBoolean(manager.activity, CF_BATTLE_KNOWS_ABOUT_EVASIONS)
            knowsHowToDamage = GlobalState.getBoolean(manager.activity, CF_BATTLE_KNOWS_HOW_TO_DAMAGE)
        }

        if (knowsHowToDamage) {
            musicThemeId = R.raw.battle_clinging_fire2
        }
    }

    private var evasionsNumber = 0
    private var evasionLine1Said = false
    private var evasionLine2Said = false
    private var evasionLine3Said = false
    private var evasionLine4Said = false
    private var threeMoreTimesLineFire = false
    private var threeMoreTimesLineSaid = false
    private var extrasLineSaid = false
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int {
            return when {
                isFromExtras && !extrasLineSaid -> {
                    extrasLineSaid = true
                    R.string.battlefield_clinging_fire_extras
                }

                !knowsHowToDamage && evasionsNumber == 1 && !evasionLine1Said -> {
                    evasionLine1Said = true
                    R.string.battlefield_clinging_fire_evasion_1
                }
                !knowsHowToDamage && evasionsNumber == 2 && !evasionLine2Said -> {
                    evasionLine2Said = true
                    R.string.battlefield_clinging_fire_evasion_2
                }
                !knowsHowToDamage && evasionsNumber == 3 && !evasionLine3Said -> {
                    evasionLine3Said = true
                    R.string.battlefield_clinging_fire_evasion_3
                }
                !knowsHowToDamage && evasionsNumber == 4 && !evasionLine4Said -> {
                    evasionLine4Said = true
                    R.string.battlefield_clinging_fire_evasion_4
                }

                threeMoreTimesLineFire && !threeMoreTimesLineSaid -> {
                    threeMoreTimesLineSaid = true
                    R.string.battlefield_clinging_fire_three_more_times
                }

                else -> R.string.empty_line
            }
        }

        override fun getDefeatedLine(): Int = R.string.battlefield_clinging_fire_d
        override fun getVictoryLine(): Int = R.string.battlefield_clinging_fire_v
    }

    private var movesInSecondPhase = 0
    private var direction = BattleFieldProjectile.Direction.NO_MOVEMENT
    private var col = -1
    override fun onMoveStart(battleField: BattleField) {
        if (knowsHowToDamage) {
            movesInSecondPhase++
        }
        val directionToCol = if (randBoolean()) {
            BattleFieldProjectile.Direction.LEFT to battleField.width
        } else {
            BattleFieldProjectile.Direction.RIGHT to -1
        }
        direction = directionToCol.first
        col = directionToCol.second
    }

    private var safeLine = -1
    private var safeLineWidth = 0
    private var safeLineDragged = 0
    private var isOnTop = false
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0 || safeLineDragged >= safeLineWidth) {
            safeLineDragged = 0

            safeLine = if (battleField.moveNumber % 2 == 1) {
                ((0 until battleField.height) - safeLine).random()
            } else {
                val rowD = randInt(3)
                val result = (if (isOnTop) battleField.height - 1 - rowD else rowD)
                isOnTop = result < 3
                result
            }

            safeLineWidth = if (knowsHowToDamage) {
                when (movesInSecondPhase) {
                    in (1..2) -> battleField.width - 1
                    in (3..4) -> randInt(3, battleField.width)
                    else -> randInt(2, battleField.width - 1)
                }
            } else {
                battleField.width
            }
        }

        
        repeat(battleField.height) {
            if (it != safeLine) {
                battleField.addProjectile(UpcomingFire(Coordinates(it, col), direction))
            }
        }
        safeLineDragged++

        if (knowsHowToDamage) {
            spawnMiddleLine(battleField, tickNumber)
        }
    }

    private fun spawnMiddleLine(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber) {
            battleField.width / 2 -> {
                val parity = battleField.protagonist.position.row % 2
                repeat(battleField.height) { row ->
                    if (row % 2 != parity) {
                        battleField.addProjectile(
                            UpcomingFireYellowOverdrive(
                                Coordinates(row, battleField.width / 2)
                            )
                        )
                    }
                }
            }
            battleField.width / 2 + 1 -> {
                repeat(battleField.height) { row ->
                    battleField.addProjectile(
                        UpcomingFireYellowOverdrive(
                            Coordinates(row, battleField.width / 2)
                        )
                    )
                }
            }
        }

        battleField
            .getAllObjects()
            .filterIsInstance<UpcomingFire>()
            .filter {
                if (direction == BattleFieldProjectile.Direction.LEFT) {
                    it.position.col == battleField.width / 2 + 1
                } else {
                    it.position.col == battleField.width / 2 - 1
                }
            }
            .forEach {
                if (direction == BattleFieldProjectile.Direction.LEFT) {
                    it.position.col--
                } else {
                    it.position.col++
                }
            }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (isFromExtras) {
            return
        }

        when (result.type) {
            AttackResult.DamageType.EVADED -> {
                evasionsNumber++
                if (!knowsAboutEvasion) {
                    if (knowsHowToDamage) {
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_clinging_fire_evasion_surprise)
                    }
                    GlobalState.putBoolean(manager.activity, CF_BATTLE_KNOWS_ABOUT_EVASIONS, true)
                    knowsAboutEvasion = true
                }
            }
            AttackResult.DamageType.HIT_SUCCESSFUL -> {
                if (!knowsHowToDamage) {
                    knowsHowToDamage = true
                    GlobalState.putBoolean(manager.activity, CF_BATTLE_KNOWS_HOW_TO_DAMAGE, true)

                    manager.activity.musicPlayer.stopAllMusic()
                    if (knowsAboutEvasion) {
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_clinging_fire_first_hit)
                        threeMoreTimesLineFire = true
                    } else {
                        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_clinging_fire_had_a_surprise)
                    }

                    manager.activity.musicPlayer.playMusic(
                        R.raw.battle_clinging_fire2,
                        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                        isLooping = true
                    )
                }
            }
            else -> {}
        }
    }
}