package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Question
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldGatheringTogether : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 45
    override val hexagramSymbol: String = "䷬"
    override val nameId: Int
        get() = R.string.battlefield_gathering_together_name

    override val centerSymbolColorId: Int = R.color.blue
    override val centerSymbol: String = "\uD83C\uDF8A"

    override val outerSkinTrigram = Lake
    override val innerHeartTrigram = Earth

    override var defaultFace: String = "\uD83D\uDE00"
    override val damageReceivedFace: String = "\uD83D\uDE2A"
    override val noDamageReceivedFace: String = "\uD83D\uDE02"

    override val animation: CharacterAnimation = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_gathering_together_1
            2 -> R.string.battlefield_gathering_together_2
            3 -> R.string.battlefield_gathering_together_3
            4 -> R.string.battlefield_gathering_together_4
            else -> R.string.battlefield_gathering_together_0
        }

        override fun getDefeatedLine(): Int = R.string.battlefield_gathering_together_d
        override fun getVictoryLine(): Int = R.string.battlefield_gathering_together_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (battleField.moveNumber % 2 == 0) {
            repeat(battleField.width) { col ->
                battleField.addProjectile(Question(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
            }
        } else {
            repeat(battleField.width) { col ->
                battleField.addProjectile(Question(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
            }
        }
    }
}