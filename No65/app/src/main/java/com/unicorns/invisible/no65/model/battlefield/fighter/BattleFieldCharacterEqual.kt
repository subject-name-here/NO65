package com.unicorns.invisible.no65.model.battlefield.fighter

import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.util.Coordinates


abstract class BattleFieldCharacterEqual : BattleFieldCharacter() {
    // The next two functions are for playable characters.
    open fun onMoveReversedStart(battleField: BattleField) {}
    open fun onTapAttack(coordinates: Coordinates, battleField: BattleField) {}
    // The next two functions are for NPCs.
    open fun defend(battleField: BattleField) {}
    open fun stopDefending() {}
    // No one stops you from being both playable and NPC.

    abstract fun getLineGenerator(enemy: BattleFieldCharacterEqual): BattleFieldLineGenerator
}