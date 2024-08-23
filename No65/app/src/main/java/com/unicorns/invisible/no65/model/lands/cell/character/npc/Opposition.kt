package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldOpposition
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.OPPOSITION_CONTRACT_IN_PLACE
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventOppositionDeadFlag
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class Opposition(override var cellBelow: Cell): CellNPC() {
    override val nameId: Int
        get() = R.string.lands_opposition_name
    override val id: Int
        get() = 47

    var emotionState = EmotionState.SMILING
    override val faceCell: String
        get() = when (emotionState) {
            EmotionState.SMILING -> "\uD83D\uDE08"
            EmotionState.ANGRY -> "\uD83D\uDC7F"
        }
    override val centerSymbol: String
        get() = "\uD83D\uDC54"
    override val centerSymbolColor: Int
        get() = R.color.dark_red
    override val emotion: Emotion
        get() = Emotion.HYPOCRISY

    override val legsSymbol: String
        get() = "ӯ"

    override var rotation: Float
        get() = 0.666f
        set(_) {}

    var place = Place.JAIL

    private var preAttackEventFired = false
    private var contractInPlace = false
    private var disturbEventFired = false

    private val preAttackEvent
        get() = EventNPCSpeakCutscene(this, isSkippable = true) {
            listOf(
                R.string.lands_opposition_jail_pre_attack_1,
                R.string.lands_opposition_jail_pre_attack_2,
                R.string.lands_opposition_jail_pre_attack_3,
                R.string.lands_opposition_jail_pre_attack_4,
                R.string.lands_opposition_jail_pre_attack_5,
                R.string.lands_opposition_jail_pre_attack_6,
            )
        }

    private val afterPreAttackPeaceEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_opposition_jail_after_pre_attack_1
        }
    private val attackEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_opposition_jail_attack_1
        }.then(EventFactory.createWithNext { manager ->
            val isModestyDead = isModestyDefeated(manager)
            EventAttack(this@Opposition, BattleFieldOpposition(
                if (isModestyDead) {
                    BattleFieldOpposition.State.JAIL_MODESTY_DEAD
                } else {
                    BattleFieldOpposition.State.JAIL_MODESTY_ALIVE
                }
            ), onAfterVictoryEvent = EventOppositionDeadFlag().then(unlockBattlesEvent))
        })
    private val unlockBattlesEvent
        get() = EventUnlockBattle(7, 8, 18)


    private val adEvent
        get() = EventNPCSpeak(this) { R.string.lands_opposition_jail_pre_intro_1 }

    private val introduceModestyDefeatedEvent
        get() = EventNPCSpeakCutscene(this@Opposition, isSkippable = true) {
            listOf(
                R.string.lands_opposition_jail_intro_1,
                R.string.lands_opposition_jail_intro_2,
                R.string.lands_opposition_jail_intro_3,
                R.string.lands_opposition_jail_intro_modesty_dead_1,
                R.string.lands_opposition_jail_intro_modesty_dead_1_2,
                R.string.lands_opposition_jail_intro_modesty_dead_2,
                R.string.lands_opposition_jail_intro_modesty_dead_3,
                R.string.lands_opposition_jail_intro_modesty_dead_4,
                R.string.lands_opposition_jail_intro_modesty_dead_5,
                R.string.lands_opposition_jail_intro_modesty_dead_6,
            )
        }

    private val introduceModestyAliveEvent
        get() = EventNPCSpeakCutscene(this@Opposition, isSkippable = true) {
            listOf(
                R.string.lands_opposition_jail_intro_1,
                R.string.lands_opposition_jail_intro_2,
                R.string.lands_opposition_jail_intro_3,
                R.string.lands_opposition_jail_intro_modesty_alive_1,
                R.string.lands_opposition_jail_intro_modesty_alive_1_2,
                R.string.lands_opposition_jail_intro_modesty_alive_2,
                R.string.lands_opposition_jail_intro_modesty_alive_3,
                R.string.lands_opposition_jail_intro_modesty_alive_4,
                R.string.lands_opposition_jail_intro_modesty_alive_5,
                R.string.lands_opposition_jail_intro_modesty_alive_6,
                R.string.lands_opposition_jail_intro_modesty_alive_9,
                R.string.lands_opposition_jail_intro_modesty_alive_10,
            )
        }
    private val defeatModestyEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_opposition_jail_intro_modesty_alive_else_1
        }
    private val modestyDefeatedEvent
        get() = EventNPCSpeakCutscene(this, isSkippable = true) {
            listOf(
                R.string.lands_opposition_jail_modesty_defeated_1,
                R.string.lands_opposition_jail_modesty_defeated_2,
                R.string.lands_opposition_jail_modesty_defeated_3,
                R.string.lands_opposition_jail_modesty_defeated_4,
                R.string.lands_opposition_jail_modesty_defeated_5,
                R.string.lands_opposition_jail_modesty_defeated_6,
            )
        }

    private val disturbEvent
        get() = EventNPCSpeakCutscene(this, isSkippable = true) {
            listOf(
                R.string.lands_opposition_jail_modesty_defeated_disturb_1,
                R.string.lands_opposition_jail_modesty_defeated_disturb_2,
                R.string.lands_opposition_jail_modesty_defeated_disturb_3,
                R.string.lands_opposition_jail_modesty_defeated_disturb_4,
            )
        }

    private var linesSaid = 0
    override fun use(): Event = EventFactory.createWithNext { manager ->
        if (manager.gameState !is GameState65) return@createWithNext Event.Null

        if (place == Place.STR_50) {
            return@createWithNext getStrEvent(manager)
        }

        when (manager.gameState.battleMode) {
            BattleMode.ATTACK -> {
                manager.gameState.flagsMaster.remove(OPPOSITION_CONTRACT_IN_PLACE)
                contractInPlace = false
                if (preAttackEventFired) {
                    attackEvent
                } else {
                    preAttackEventFired = true
                    preAttackEvent
                }
            }
            BattleMode.PEACE -> {
                when {
                    preAttackEventFired -> {
                        afterPreAttackPeaceEvent
                    }
                    linesSaid == 0 -> {
                        adEvent
                    }
                    linesSaid == 1 -> {
                        if (isModestyDefeated(manager)) {
                            contractInPlace = true
                            manager.gameState.flagsMaster.add(OPPOSITION_CONTRACT_IN_PLACE)
                            introduceModestyDefeatedEvent
                        } else {
                            introduceModestyAliveEvent
                        }
                    }
                    contractInPlace -> {
                        if (disturbEventFired) {
                            Event.Null
                        } else {
                            disturbEventFired = true
                            disturbEvent
                        }
                    }
                    !isModestyDefeated(manager) -> defeatModestyEvent
                    else -> {
                        contractInPlace = true
                        manager.gameState.flagsMaster.add(OPPOSITION_CONTRACT_IN_PLACE)
                        modestyDefeatedEvent
                    }
                }.also { linesSaid++ }
            }
            else -> Event.Null
        }
    }

    private fun isModestyDefeated(manager: LandsManager): Boolean {
        return manager.gameState.currentMap
            .getTopCells()
            .filterIsInstance<Modesty>()
            .isEmpty()
    }

    val strAttackEvent
        get() = EventAttack(
            this,
            BattleFieldOpposition(BattleFieldOpposition.State.STR_50),
            onAfterVictoryEvent = unlockBattlesEvent
        )

    private var busyLineSaid = false
    private var warmWelcomeSaid = false
    private var jailLineSaid = false
    private var guitarHeroLineSaid = false
    private var aboutModestyLineSaid = false
    private fun getStrEvent(manager: LandsManager): Event {
        if (manager.gameState !is GameState65) return Event.Null
        if (manager.gameState.battleMode == BattleMode.ATTACK) {
            return EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_opposition_city_attack_1,
                    R.string.lands_opposition_city_attack_2,
                    R.string.lands_opposition_city_attack_3,
                    R.string.lands_opposition_city_attack_4,
                    R.string.lands_opposition_city_attack_5,
                )
            }.then(strAttackEvent)
        }

        val hasContract = OPPOSITION_CONTRACT_IN_PLACE in manager.gameState.flagsMaster
        return if (!hasContract) {
            if (!busyLineSaid) {
                busyLineSaid = true
                EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_opposition_city_0_1,
                        R.string.lands_opposition_city_0_2,
                        R.string.lands_opposition_city_0_3,
                        R.string.lands_opposition_city_0_4,
                    )
                }
            } else {
                EventNPCSpeak(this) { R.string.lands_opposition_city_else_1 }
            }
        } else {
            when {
                !warmWelcomeSaid -> {
                    warmWelcomeSaid = true
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_opposition_city_contracted_1,
                            R.string.lands_opposition_city_contracted_2,
                            R.string.lands_opposition_city_contracted_3,
                            R.string.lands_opposition_city_contracted_4,
                        )
                    }
                }
                !jailLineSaid -> {
                    jailLineSaid = true
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_opposition_city_contracted_5,
                            R.string.lands_opposition_city_contracted_6,
                            R.string.lands_opposition_city_contracted_7,
                        )
                    }
                }
                !guitarHeroLineSaid -> {
                    guitarHeroLineSaid = true
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_opposition_city_guitar_hero_1,
                            R.string.lands_opposition_city_guitar_hero_2,
                            R.string.lands_opposition_city_guitar_hero_3,
                        )
                    }
                }
                !aboutModestyLineSaid -> {
                    aboutModestyLineSaid = true
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_opposition_city_about_modesty_1,
                            R.string.lands_opposition_city_about_modesty_2,
                            R.string.lands_opposition_city_about_modesty_3,
                            R.string.lands_opposition_city_about_modesty_4,
                            R.string.lands_opposition_city_about_modesty_5,
                        )
                    }
                }
                else -> {
                    EventNPCSpeak(this) { R.string.lands_opposition_city_after_contract_else_1 }
                }
            }
        }
    }

    enum class Place {
        JAIL,
        STR_50
    }

    enum class EmotionState {
        SMILING,
        ANGRY
    }
}