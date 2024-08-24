package com.unicorns.invisible.no65.model.battlefield.fighter

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation


abstract class BattleFieldCharacter : BattleFieldFighter() {
    abstract val nameId: Int
    abstract val number: Int

    open val attackTimeMvs: Int = 25

    abstract val defaultFace: String
    abstract val damageReceivedFace: String
    abstract val noDamageReceivedFace: String

    abstract val centerSymbol: String
    abstract val centerSymbolColorId: Int

    abstract val hexagramSymbol: String

    open val beatId = R.raw.sfx_retrobeat
    open val beforeCountdownDelay
        get() = tickTime / 2
    open val goNumbersToDelays: List<Pair<Int, Long>>
        get() = listOf(
            3 to tickTime / 2,
            2 to tickTime / 2,
            1 to tickTime / 2,
        )
    open val goText = "GO!"
    open val afterGoTextDelay
        get() = tickTime / 2

    open val legsSymbol = "Ӯ"
    open val handsSymbol = "+"
    open val animation = CharacterAnimation.NONE
    open val musicThemeId = R.raw.battle_enemy

    // BOSS MODIFIERS
    open val evadesSimpleAttacks: Boolean = false
    open val areAttacksMirrored = false
    open val numberOfTaps = 2
    open val projectilePassesCellsPerMove = 1
    open val tickTime: Long = PROJECTILE_SPEED_MILLISECONDS
    open val isHealthInfinite = false
    open val isRequiemProof = false
    open val timebackDenied = false

    open val lineGeneratorJournalOverride = false

    open val rowViewsShuffleSeed: Int? = null

    open suspend fun onBattleBegins(manager: BattleManager) {}
    open fun onMoveStart(battleField: BattleField) {}
    open fun onTick(manager: BattleManager, tickNumber: Int) {}
    open fun onMoveRepeat(battleField: BattleField) {}
}