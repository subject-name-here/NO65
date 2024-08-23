package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.CocktailGlass
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldTheCauldron : BattleFieldEnemy() {
    override val number: Int = 50
    override val hexagramSymbol: String = "䷱"
    override val nameId: Int
        get() = R.string.battlefield_the_cauldron_name

    override val centerSymbolColorId: Int = R.color.almost_black
    override val centerSymbol: String = "\uD83E\uDD43"

    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Wind

    private var state = State.DRUNK
    override val defaultFace: String
        get() = when (state) {
            State.DRUNK -> "\uD83E\uDD24"
            State.SICK -> "\uD83E\uDD22"
            State.VOMITING -> "\uD83E\uDD2E"
        }
    override val damageReceivedFace: String
        get() = "\uD83D\uDE25"
    override val noDamageReceivedFace: String
        get() = defaultFace

    override val animation: CharacterAnimation = CharacterAnimation.DRUNK_SWING

    private var sickLineSaid = false
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_the_cauldron_1
            else -> when {
                state == State.DRUNK -> R.string.battlefield_the_cauldron_drunk
                state == State.SICK && !sickLineSaid -> {
                    sickLineSaid = true
                    R.string.battlefield_the_cauldron_sick
                }
                state == State.VOMITING -> {
                    R.string.battlefield_the_cauldron_vomiting
                }
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_the_cauldron_d
        override fun getVictoryLine(): Int = R.string.battlefield_the_cauldron_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        repeat(battleField.width / 2) {
            val col = randInt(battleField.width)
            battleField.addProjectile(CocktailGlass(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
        }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        when {
            state == State.DRUNK &&
                    health <= BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER -> {
                state = State.SICK
            }
            state == State.SICK &&
                    health <= BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER -> {
                state = State.VOMITING
            }
            state == State.VOMITING -> {
                state = State.SICK
            }
        }
    }

    enum class State {
        DRUNK,
        SICK,
        VOMITING
    }
}