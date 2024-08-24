package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.D99Kikiriki
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.D99RayOfDeath
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DeliveranceBagOfMoney
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DeliveranceTwodent
import com.unicorns.invisible.no65.model.elements.trigram.*
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.D_NINETY_NINE_RULES_EXPLAINED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.view.BattleFieldDrawerD99
import com.unicorns.invisible.no65.view.BattleFieldDrawerStandard
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class BattleFieldD99 : BattleFieldEnemy() {
    override val maxHealth: Int = 99
    override var health: Int = maxHealth

    override val nameId: Int
        get() = R.string.battlefield_d99_name
    override val number: Int
        get() = 99

    override var defaultFace: String = "\uD83E\uDD11"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE23"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDD11"

    override val centerSymbol: String = ""
    override val centerSymbolColorId: Int = R.color.no_color
    override val hexagramSymbol: String = ""
    override val outerSkinTrigram: Trigram = Heaven
    override val innerHeartTrigram: Trigram = Heaven

    override val attackTimeMvs: Int
        get() = 99
    override val defenceTimeSec: Int
        get() = 3
    override val tickTime: Long
        get() = FAST_PROJECTILE_SPEED_MILLISECONDS
    override val musicThemeId: Int
        get() = R.raw.battle_d99
    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT_D99
    override val beatId: Int
        get() = R.raw.sfx_retrobeat_distorted

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_d99_1
            3 -> R.string.battlefield_d99_3
            5 -> R.string.battlefield_d99_5
            else -> R.string.battlefield_d99_0
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_d99_d
        override fun getVictoryLine(): Int = R.string.battlefield_d99_v
    }

    override suspend fun onBattleBegins(manager: BattleManager) {
        val rulesExplained = GlobalState.getBoolean(manager.activity, D_NINETY_NINE_RULES_EXPLAINED)
        if (!rulesExplained) {
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_4)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_5)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_6)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_long_7)
            GlobalState.putBoolean(manager.activity, D_NINETY_NINE_RULES_EXPLAINED, true)
        } else {
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_short_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_short_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_short_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_rules_short_4)
        }
    }

    private var returnBlocks = {}
    private fun playSnapSound(manager: BattleManager) {
        manager.activity.musicPlayer.playMusic(
            R.raw.sfx_snap_2,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
    }
    override fun onTick(manager: BattleManager, tickNumber: Int) {
        when (tickNumber % 20) {
            13 -> {
                val drawerD99 = manager.drawer as BattleFieldDrawerD99
                drawerD99.prepareBlocksToFire()
                playSnapSound(manager)
                returnBlocks = {
                    drawerD99.returnBlocks()
                    returnBlocks = {}
                }
            }
            15 -> {}
            16 -> {
                returnBlocks()
            }
        }
    }

    override fun onMoveRepeat(battleField: BattleField) {
        returnBlocks()
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 20 == 15) {
            val width = battleField.width
            val cols = (0 until width) - (width / 2 - 1..width / 2 + 1)
            cols.forEach { col ->
                repeat(battleField.height) { row ->
                    battleField.addProjectile(D99RayOfDeath(Coordinates(row, col)))
                }
            }
        }

        @Suppress("MoveVariableDeclarationIntoWhen")
        val attackNumber = (battleField.moveNumber - 1) % 4 + 1
        when (attackNumber) {
            1 -> move1(battleField, tickNumber)
            2 -> move2(battleField, tickNumber)
            3 -> move3(battleField, tickNumber)
            4 -> move4(battleField, tickNumber)
        }
    }

    private var lastCell = 0
    private fun move1(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            lastCell = 0
        }

        when (tickNumber) {
            in (0..23) -> {
                if (tickNumber % 4 == 0) {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, col)))
                    }
                }
            }
            in (24..46) -> {
                repeat(2) {
                    battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, lastCell)))
                    lastCell = (lastCell + 1) % battleField.width
                }
                val col = randInt(battleField.width)
                battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, col)))
            }
            in (47..74) -> {
                val blockSize = battleField.height / 2 + 1
                if (tickNumber % blockSize < blockSize - 3) {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, col)))
                    }
                }
            }
            else -> {
                if (tickNumber % 4 == 0) {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, col)))
                    }
                } else {
                    val col = randInt(battleField.width)
                    battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, col)))
                }
            }
        }
    }

    private fun move2(battleField: BattleField65, tickNumber: Int) {
        val gap = when (tickNumber) {
            in (0..20) -> 9
            in (21..40) -> 7
            in (41..60) -> 5
            in (61..80) -> 4
            else -> 3
        }
        if (tickNumber % gap == 0) {
            val protagonistRow = battleField.protagonist.position.row
            battleField.addProjectile(D99Kikiriki(Coordinates(protagonistRow, -1), BattleFieldProjectile.Direction.RIGHT))
            val protagonistCol = battleField.protagonist.position.col
            battleField.addProjectile(D99Kikiriki(Coordinates(-1, protagonistCol), BattleFieldProjectile.Direction.DOWN))
        } else {
            val row = randInt(battleField.height)
            battleField.addProjectile(D99Kikiriki(Coordinates(row, -1), BattleFieldProjectile.Direction.RIGHT))
            val col = randInt(battleField.width)
            battleField.addProjectile(D99Kikiriki(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))

        }
    }

    private var colsList = mutableListOf<Int>()
    private fun move3(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            colsList = mutableListOf()
        }

        val protagonistCol = battleField.protagonist.position.col
        if (tickNumber % battleField.height == battleField.height - 1) {
            battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, protagonistCol)))
        }

        val condition = when (tickNumber) {
            in (0..49) -> {
                { tickNumber % 4 == 0 }
            }
            else -> {
                { tickNumber % 6 >= 3 }
            }
        }
        if (condition()) {
            repeat(battleField.width) { col ->
                addBagDown(battleField, col)
            }
            colsList = (0 until battleField.width).toMutableList()
            colsList.shuffle()
        } else {
            var numberOfAdditionalBags = battleField.width / 3 - 1
            if (protagonistCol in colsList) {
                addBagDown(battleField, protagonistCol)
                colsList.remove(protagonistCol)
                numberOfAdditionalBags--
            }

            repeat(numberOfAdditionalBags) {
                val col = colsList.removeAt(0)
                addBagDown(battleField, col)
            }
        }
    }
    private fun addBagDown(battleField: BattleField65, col: Int) {
        battleField.addProjectile(DeliveranceBagOfMoney(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
    }

    private fun move4(battleField: BattleField65, tickNumber: Int) {
        val protagonistCol = battleField.protagonist.position.col
        val protagonistRow = battleField.protagonist.position.row
        when {
            tickNumber % battleField.height == battleField.height - 1 -> {
                if (randBoolean()) {
                    battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, protagonistCol)))
                } else {
                    battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, randInt(battleField.width))))
                }
            }
            tickNumber % 4 == 0 -> {
                repeat(battleField.height) { row ->
                    addBagLeft(battleField, row)
                }
            }
            tickNumber % 4 == 2 -> {
                repeat(battleField.height) { row ->
                    addBagRight(battleField, row)
                }
            }
            tickNumber % 4 == 3 -> {
                battleField.addProjectile(D99Kikiriki(Coordinates(protagonistRow, -1), BattleFieldProjectile.Direction.RIGHT))
            }
        }
    }
    private fun addBagLeft(battleField: BattleField65, row: Int) {
        battleField.addProjectile(DeliveranceBagOfMoney(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
    }
    private fun addBagRight(battleField: BattleField65, row: Int) {
        battleField.addProjectile(DeliveranceBagOfMoney(Coordinates(row, -1), BattleFieldProjectile.Direction.RIGHT))
    }

    override fun receiveTrigramDamage(trigram: AttackTrigram, multiplier: Int): Int {
        return 30
    }

    private var healingIsForbiddenLineSaid = false
    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        when (element) {
            Lake -> {
                if (!healingIsForbiddenLineSaid) {
                    healingIsForbiddenLineSaid = true
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_on_healing_long_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_on_healing_long_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_on_healing_long_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_on_healing_long_4)
                } else {
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_on_healing_short_1)
                }
                val damage = BattleFieldProtagonist.HEAL_POINTS + randInt(-9, 10)
                battleField.protagonist.health = (battleField.protagonist.health - damage).coerceIn(1, battleField.protagonist.maxHealth)
                manager.drawer.updateProtagonist(battleField.protagonist)
            }
            Wind -> {
                battleField.protagonist.dropMultiplier()
                manager.drawer.updateProtagonist(battleField.protagonist)
                delay(200L)
                (manager.drawer as BattleFieldDrawerStandard).showTrigramTriggered(result, this, element)
            }
            else -> {}
        }

        if (health <= 0) {
            manager.activity.musicPlayer.stopAllMusic()
            defaultFace = "\uD83D\uDE28"
            manager.drawer.updateEnemyFace(this)
            (manager.drawer as BattleFieldDrawerD99).prepareBlocksToFire()
            delay(1000L)

            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_1)

            defaultFace = "\uD83D\uDE02"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_2)

            defaultFace = "\uD83E\uDD11"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_4)

            defaultFace = "\uD83C\uDF1D"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_99_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_99_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_99_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_99_4)

            defaultFace = "\uD83E\uDD11"
            manager.drawer.updateEnemyFace(this)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_last_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_last_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_last_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_d99_dying_last_4)
        }
    }
}