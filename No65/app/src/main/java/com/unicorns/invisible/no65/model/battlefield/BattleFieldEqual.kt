package com.unicorns.invisible.no65.model.battlefield

import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldFighter


class BattleFieldEqual(
    width: Int,
    height: Int,
    protagonist: BattleFieldFighter,
    enemy: BattleFieldCharacter
) : BattleField(width, height, protagonist, enemy)