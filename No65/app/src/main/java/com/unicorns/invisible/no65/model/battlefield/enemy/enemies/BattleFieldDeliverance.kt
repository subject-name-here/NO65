package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DeliveranceBagOfMoney
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DeliveranceTwodent
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldDeliverance(hadADeal: Boolean) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_deliverance_name
    override val number: Int
        get() = 40

    override val defaultFace: String
        get() = "\uD83E\uDD11"
    override val damageReceivedFace: String
        get() = "\uD83E\uDD11"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDD11"

    override val centerSymbol: String
        get() = "\uD83D\uDCB8"
    override val centerSymbolColorId: Int
        get() = R.color.green

    override val hexagramSymbol: String
        get() = "䷧"
    override val outerSkinTrigram: Trigram
        get() = Thunder
    override val innerHeartTrigram: Trigram
        get() = Water

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_deliverance_1
            2 -> if (hadADeal) R.string.battlefield_deliverance_deal_1 else R.string.empty_line
            3 -> R.string.battlefield_deliverance_3
            else -> R.string.empty_line
        }

        override fun getDefeatedLine(): Int = R.string.battlefield_deliverance_d
        override fun getVictoryLine(): Int = R.string.battlefield_deliverance_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 0) {
            repeat(battleField.width) { col ->
                battleField.addProjectile(DeliveranceBagOfMoney(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
            }
        } else {
            battleField.addProjectile(DeliveranceBagOfMoney(Coordinates(-1, randInt(battleField.width)), BattleFieldProjectile.Direction.DOWN))
            when (battleField.moveNumber) {
                1 -> {
                    battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, battleField.protagonist.position.col)))
                }
                2 -> {
                    battleField.addProjectile(DeliveranceTwodent(Coordinates(battleField.height, randInt(battleField.width))))
                }
                else -> {}
            }
        }
    }
}