package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.TheMMBrokenHeart
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.TheMMBrokenHeartSource
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_MM_POWER_EMBRACED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.BattleFieldDrawer
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class BattleFieldTheMarryingMaiden(private val isFromExtras: Boolean = false) : BattleFieldEnemy() {
    override val maxHealth: Int
        get() = if (hasReloaded) BASE_DAMAGE_TO_ENEMY * TOUGH_BOSS_HEALTH_MULTIPLIER else BASE_DAMAGE_TO_ENEMY * ENEMY_MAYBE_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val nameId: Int
        get() = R.string.battlefield_the_marrying_maiden_name
    override val number: Int
        get() = 54

    override var defaultFace: String = "\uD83D\uDE3F"
    override val damageReceivedFace: String
        get() = if (hasReloaded) defaultFace else "\uD83D\uDE40"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE3A"

    override val centerSymbol: String
        get() = " </3 "
    override val centerSymbolColorId: Int
        get() = R.color.black

    override val animation: CharacterAnimation
        get() = if (hasReloaded) CharacterAnimation.FLOAT else CharacterAnimation.LITTLE_SWING
    override var musicThemeId: Int = super.musicThemeId

    override val hexagramSymbol: String
        get() = "䷵"
    override val outerSkinTrigram: Trigram
        get() = Thunder
    override val innerHeartTrigram: Trigram
        get() = Lake

    override suspend fun onBattleBegins(manager: BattleManager) {
        val startWithPower = if (isFromExtras) {
            false
        } else {
            GlobalState.getBoolean(manager.activity, THE_MM_POWER_EMBRACED)
        }

        if (!startWithPower) {
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_begin_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_begin_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_begin_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_begin_4)
            defaultFace = "\uD83D\uDE3C"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_begin_5)
        } else {
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_begin_again_1)
            defaultFace = "\uD83D\uDE3C"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_reload)
            musicThemeId = R.raw.battle_tmm_2
        }

        hasReloaded = startWithPower
        health = maxHealth
    }

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (!hasReloaded) {
            when (moveNumber) {
                1 -> R.string.battlefield_the_marrying_maiden_1
                else -> R.string.empty_line
            }
        } else {
            R.string.empty_line
        }
        override fun getDefeatedLine(): Int = if (hasReloaded)
            R.string.battlefield_the_marrying_maiden_d_reloaded
        else
            R.string.battlefield_the_marrying_maiden_d

        override fun getVictoryLine(): Int = if (hasReloaded)
            R.string.battlefield_the_marrying_maiden_v_reloaded
        else
            R.string.battlefield_the_marrying_maiden_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (!hasReloaded) {
            if (battleField.protagonist.health <= BattleFieldProjectile.DEFAULT_PROJECTILE_DAMAGE * 2) {
                defaultFace = "\uD83D\uDE40"
                removeProjectilesNearProtagonist(battleField)
            } else {
                defaultFace = "\uD83D\uDE3C"
            }
        }

        if (!hasReloaded) {
            when (tickNumber % 3) {
                0 -> {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(
                            TheMMBrokenHeart(
                                Coordinates(-1, col),
                                BattleFieldProjectile.Direction.DOWN
                            )
                        )
                    }
                }
            }

            when (battleField.moveNumber) {
                1 -> {
                    spawnArrowAboveProtagonist(battleField)
                }
                else -> {
                    spawnThreeArrowsAboveProtagonist(battleField)
                }
            }
        } else {
            powerfulAttack(battleField, tickNumber)
        }

        if (!hasReloaded && battleField.protagonist.health <= BattleFieldProjectile.DEFAULT_PROJECTILE_DAMAGE * 2) {
            removeProjectilesNearProtagonist(battleField)
        }
    }

    private fun spawnArrowAboveProtagonist(battleField: BattleField65) {
        val col = battleField.protagonist.position.col
        battleField.getAllObjects()
            .filterIsInstance<TheMMBrokenHeart>()
            .filter { it.direction == BattleFieldProjectile.Direction.DOWN }
            .filter { it.position.col == col }
            .filter { battleField.protagonist.position.row - it.position.row > 0 }
            .maxByOrNull { it.position.row }?.orderToShoot = true
    }

    private fun spawnThreeArrowsAboveProtagonist(battleField: BattleField65) {
        val col = battleField.protagonist.position.col
        val heartsAbove = battleField.getAllObjects()
            .filterIsInstance<TheMMBrokenHeart>()
            .filter { it.position.col in (col - 1..col + 1) }
            .filter { battleField.protagonist.position.row - it.position.row > 0 }
        val row = heartsAbove.maxOfOrNull { it.position.row }
        heartsAbove
            .filter { it.position.row == row }
            .forEach { it.orderToShoot = true }
    }

    private fun removeProjectilesNearProtagonist(battleField: BattleField65) {
        (-3..3).forEach { rowDelta ->
            (-1..1).forEach { colDelta ->
                val inhabitant = battleField.getMap()[battleField.protagonist.position + Coordinates(rowDelta, colDelta)]
                if (inhabitant is BattleFieldProjectile) {
                    battleField.removeProjectile(inhabitant)
                }
            }
        }
    }

    private fun powerfulAttack(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                repeat(battleField.width) { col ->
                    battleField.addProjectile(TheMMBrokenHeart(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                }
            }
            1 -> {
                spawnArrowAboveProtagonist(battleField)
            }
            2 -> {
                val col = ((0 until battleField.width) - battleField.protagonist.position.col).random()

                battleField.addProjectile(
                    TheMMBrokenHeartSource(
                        Coordinates(battleField.protagonist.position.row, col)
                    )
                )
            }
        }
    }

    private var hasReloaded = false
    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (health <= 0 && !hasReloaded) {
            manager.activity.musicPlayer.stopAllMusic()
            defaultFace = "\uD83D\uDE3F"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_4)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_5)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_6)

            defaultFace = "\uD83D\uDE3C"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_7)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_8)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_before_reload_9)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_reload)

            musicThemeId = R.raw.battle_tmm_2

            hasReloaded = true
            health = maxHealth
            manager.drawer.updateEnemy(this)
            delay(BattleFieldDrawer.HEALTH_BAR_ANIMATION_DURATION)

            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_the_marrying_maiden_after_reload_1)
            if (!isFromExtras) {
                GlobalState.putBoolean(manager.activity, THE_MM_POWER_EMBRACED, true)
            }

            manager.activity.musicPlayer.playMusic(
                musicThemeId,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = true
            )
        }
    }
}