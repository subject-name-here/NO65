package com.unicorns.invisible.no65.model.lands

import com.unicorns.invisible.no65.model.elements.trigram.*
import com.unicorns.invisible.no65.model.lands.cell.*
import com.unicorns.invisible.no65.model.lands.cell.books.*
import com.unicorns.invisible.no65.model.lands.cell.character.*
import com.unicorns.invisible.no65.model.lands.cell.character.npc.*
import com.unicorns.invisible.no65.model.lands.cell.control.*
import com.unicorns.invisible.no65.model.lands.cell.control.door.*
import com.unicorns.invisible.no65.model.lands.cell.control.sash.*
import com.unicorns.invisible.no65.model.lands.cell.decor.*
import com.unicorns.invisible.no65.model.lands.cell.floor.*
import com.unicorns.invisible.no65.model.lands.cell.interactive.*
import com.unicorns.invisible.no65.model.lands.cell.moveable.*
import com.unicorns.invisible.no65.model.lands.cell.service.*
import com.unicorns.invisible.no65.model.lands.cell.trigger.*
import com.unicorns.invisible.no65.model.lands.cell.wall.*
import com.unicorns.invisible.no65.model.lands.cell.wall.block.*
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.reflect.KClass


class RegisteredCells {
    companion object {
        // IMPORTANT: cells must be registered in two lists!
        val cells: List<KClass<out Cell>> = listOf(
            CellEmpty::class,
            // SERVICE
            SaveCell::class,
            TeleportCell::class,
            // FLOOR
            CellFloorWhite::class,
            CellFloorAlmostWhite::class,
            CellFloorLightGrey::class,
            CellFloorGrey::class,
            CellFloorRed::class,
            CellFloorDarkRed::class,
            CellFloorOrange::class,
            CellFloorSandy::class,
            CellFloorTrueYellow::class,
            CellFloorYellow::class,
            CellFloorTrueGreen::class,
            CellFloorGreen::class,
            CellFloorLightBlue::class,
            CellFloorBlue::class,
            CellFloorDarkBlue::class,
            CellFloorPurple::class,
            CellFloorPink::class,
            CellFloorPoisonPink::class,
            CellFloorBrown::class,
            CellFloorAlmostBlack::class,
            TeleportCellBroken::class,
            // CHARACTERS
            DifficultyAtTheBeginning::class,
            InnerTruth::class,
            Waiting::class,
            CreativeHeaven::class,
            ReceptiveEarth::class,
            YouthfulFolly::class,
            Conflict::class,
            SmallTaming::class,
            SmallPreponderance::class,
            KeepingStillMountain::class,
            GreatTaming::class,
            TheArmy::class,
            HoldingTogether::class,
            JoyousLake::class,
            Treading::class,
            Peace::class,
            ClingingFire::class,
            BeforeCompletion::class,
            Standstill::class,
            Fellowship::class,
            GreatPossession::class,
            ComingToMeet::class,
            Approach::class,
            Following::class,
            Modesty::class,
            Enthusiasm::class,
            WorkOnTheDecayed::class,
            TheCauldron::class,
            TheFamily::class,
            GatheringTogether::class,
            Grace::class,
            PushingUpward::class,
            GreatPreponderance::class,
            Progress::class,
            GreatPower::class,
            Deliverance::class,
            Limitation::class,
            Development::class,
            Innocence::class,
            BitingThrough::class,
            Opposition::class,
            ArousingThunder::class,
            TheWell::class,
            Breakthrough::class,
            Oppression::class,
            Abundance::class,
            GentleWind::class,
            Dispersion::class,
            Obstruction::class,
            Revolution::class,
            SplittingApart::class,
            Retreat::class,
            TheWanderer::class,
            Contemplation::class,
            Return::class,
            Increase::class,
            Decrease::class,
            DarkeningOfTheLight::class,
            MouthCorners::class,
            TheMarryingMaiden::class,
            Influence::class,
            Duration::class,
            AfterCompletion::class,
            // MOVEABLES
            Source::class,
            // BUTTON
            Socket::class,
            // DECOR
            StarsAndStripes::class,
            DecorBush::class,
            DecorBush2::class,
            DecorFlower1Red::class,
            DecorFlower1Yellow::class,
            DecorFlower2Pink::class,
            DecorFlower2Purple::class,
            DecorFlower3Blue::class,
            DecorFlower3Green::class,
            DecorLuckyFlower::class,
            DecorTools::class,
            DecorGreekTemple::class,
            DecorChessboard::class,
            DecorSpiral::class,
            DecorArtObject::class,
            DecorArtObject2::class,
            MacGuffinVanilla::class,
            MacGuffinSoundKeeper::class,
            FenceHorizontal::class,
            FenceVertical::class,
            DoubleTapCell::class,
            ExitToTheRight::class,
            BrotherhoodDecorStar::class,
            BrotherhoodDecorTogether::class,
            MarketDecorDiamond::class,
            MarketDecorGoldBars::class,
            MarketDecorCocktailGlass::class,
            MarketDecorCocktailGlassBroken::class,
            MarketDecorWaxBox::class,
            Guillotine::class,
            DecorEmptyAquarium::class,
            DecorFilledAquarium::class,
            DecorDarks::class,
            AceOfClubsTable::class,
            DecorMannequin::class,
            DecorMannequinDark::class,
            DecorMannequinSoldier::class,
            DecorMannequinSoldierDark::class,
            DecorTable::class,
            DecorREGift::class,
            DecorREThreeLeg::class,
            DecorDeadBody::class,
            DecorWeb::class,
            DecorDarkWeb::class,
            DecorBalloon::class,
            DecorTeddyBear::class,
            DecorBomb::class,
            DecorLeg::class,
            LetterCell::class,
            DecorDenied::class,
            DecorWeapon::class,
            DecorPapers::class,
            SavePointMarker::class,
            DecorEye::class,
            // INTERACTIVE
            CellInfo::class,
            CellGraffiti::class,
            CellGraffitiDestroyed::class,
            PointOfInterest::class,
            Note::class,
            EstonianRoom::class,
            CellMoney::class,
            RedBull::class,
            // DOORS
            SashBottom::class,
            SashLeft::class,
            SashRight::class,
            SashTop::class,
            HorizontalDoor::class,
            VerticalDoor::class,
            // WALLS
            BrickWall::class,
            StoneWall::class,
            HashWall::class,
            FistWall::class,
            TempleWall::class,
            TempleWallInner::class,
            BlockWallGrey::class,
            BlockWallOrange::class,
            BlockWallAlmostBlack::class,
            BlockWallBrown::class,
            BlockWallParty::class,
            BlockWallGoodsStreet::class,
            // SKILL BOOKS
            SkillBookEarth::class,
            SkillBookFire::class,
            SkillBookHeaven::class,
            SkillBookLake::class,
            SkillBookLakeRequiem::class,
            SkillBookMountain::class,
            SkillBookThunder::class,
            SkillBookWater::class,
            SkillBookWind::class,
            SkillBookWindRequiem::class,
            // SERVICE
            SaveDestroyedCell::class,
            SaveCellDecoy::class,
        )

        private fun PolymorphicModuleBuilder<CellNonEmpty>.registerSubclasses() {
            // SERVICE
            subclass(SaveCell::class)
            subclass(SaveDestroyedCell::class)
            subclass(SaveCellDecoy::class)
            subclass(TeleportCell::class)
            subclass(TeleportCellSecret::class)
            subclass(GenocideLock::class)
            subclass(NightSpawner::class)
            subclass(NightComesDown::class)
            subclass(ATActivator::class)
            subclass(MathClassLock::class)
            subclass(TreadingActivator::class)
            subclass(DeliveranceActivator::class)
            subclass(TheMMActivator::class)
            subclass(D99Activator::class)
            subclass(GentleWindTaintedActivator::class)
            subclass(NoEnemyActivator::class)
            // FLOOR
            subclass(CellFloorLightGrey::class)
            subclass(CellFloorOrange::class)
            subclass(CellFloorWhite::class)
            subclass(CellFloorAlmostBlack::class)
            subclass(CellFloorYellow::class)
            subclass(CellFloorTrueGreen::class)
            subclass(CellFloorDarkRed::class)
            subclass(CellFloorSandy::class)
            subclass(CellFloorRed::class)
            subclass(CellFloorBlue::class)
            subclass(CellFloorTrueYellow::class)
            subclass(CellFloorLightBlue::class)
            subclass(CellFloorPurple::class)
            subclass(CellFloorPink::class)
            subclass(CellFloorPoisonPink::class)
            subclass(CellFloorBrown::class)
            subclass(CellFloorAlmostWhite::class)
            subclass(CellFloorGreen::class)
            subclass(CellFloorGrey::class)
            subclass(CellFloorDarkBlue::class)
            subclass(TeleportCellBroken::class)
            subclass(CellFloorDAA::class)
            // TRIGGERS
            subclass(ReceptiveEarthTrigger::class)
            subclass(ReceptiveEarthMacGuffin::class)
            subclass(KSMTrapport::class)
            // CHARACTERS
            subclass(CellProtagonist::class)
            subclass(CellTheCreature::class)
            subclass(DifficultyAtTheBeginning::class)
            subclass(InnerTruth::class)
            subclass(Waiting::class)
            subclass(CreativeHeaven::class)
            subclass(ReceptiveEarth::class)
            subclass(YouthfulFolly::class)
            subclass(Conflict::class)
            subclass(SmallTaming::class)
            subclass(SmallPreponderance::class)
            subclass(KeepingStillMountain::class)
            subclass(GreatTaming::class)
            subclass(TheArmy::class)
            subclass(HoldingTogether::class)
            subclass(JoyousLake::class)
            subclass(Treading::class)
            subclass(Peace::class)
            subclass(ClingingFire::class)
            subclass(BeforeCompletion::class)
            subclass(Standstill::class)
            subclass(Fellowship::class)
            subclass(GreatPossession::class)
            subclass(ComingToMeet::class)
            subclass(Approach::class)
            subclass(Following::class)
            subclass(Modesty::class)
            subclass(Enthusiasm::class)
            subclass(WorkOnTheDecayed::class)
            subclass(TheCauldron::class)
            subclass(TheFamily::class)
            subclass(GatheringTogether::class)
            subclass(Grace::class)
            subclass(PushingUpward::class)
            subclass(GreatPreponderance::class)
            subclass(Progress::class)
            subclass(GreatPower::class)
            subclass(Deliverance::class)
            subclass(Limitation::class)
            subclass(Development::class)
            subclass(Innocence::class)
            subclass(BitingThrough::class)
            subclass(Opposition::class)
            subclass(ArousingThunder::class)
            subclass(TheWell::class)
            subclass(Breakthrough::class)
            subclass(Oppression::class)
            subclass(Abundance::class)
            subclass(GentleWind::class)
            subclass(Dispersion::class)
            subclass(Obstruction::class)
            subclass(Revolution::class)
            subclass(SplittingApart::class)
            subclass(Retreat::class)
            subclass(TheWanderer::class)
            subclass(Contemplation::class)
            subclass(Return::class)
            subclass(Increase::class)
            subclass(Decrease::class)
            subclass(DarkeningOfTheLight::class)
            subclass(MouthCorners::class)
            subclass(TheMarryingMaiden::class)
            subclass(Influence::class)
            subclass(Duration::class)
            subclass(AfterCompletion::class)
            // MOVEABLES
            subclass(Source::class)
            // BUTTONS
            subclass(Socket::class)
            // DECOR
            subclass(StarsAndStripes::class)
            subclass(DecorBush::class)
            subclass(DecorBush2::class)
            subclass(DecorFlower1Red::class)
            subclass(DecorFlower1Yellow::class)
            subclass(DecorFlower2Purple::class)
            subclass(DecorFlower2Pink::class)
            subclass(DecorFlower3Blue::class)
            subclass(DecorFlower3Green::class)
            subclass(DecorTools::class)
            subclass(DecorGreekTemple::class)
            subclass(DecorChessboard::class)
            subclass(DecorLuckyFlower::class)
            subclass(DecorSpiral::class)
            subclass(DecorArtObject::class)
            subclass(DecorArtObject2::class)
            subclass(MacGuffinVanilla::class)
            subclass(MacGuffinSoundKeeper::class)
            subclass(MacGuffinOpposition::class)
            subclass(FenceHorizontal::class)
            subclass(FenceVertical::class)
            subclass(DoubleTapCell::class)
            subclass(BrotherhoodDecorStar::class)
            subclass(BrotherhoodDecorTogether::class)
            subclass(MarketDecorDiamond::class)
            subclass(MarketDecorGoldBars::class)
            subclass(MarketDecorCocktailGlass::class)
            subclass(MarketDecorCocktailGlassBroken::class)
            subclass(DecorEmptyAquarium::class)
            subclass(DecorDarks::class)
            subclass(Guillotine::class)
            subclass(DecorFilledAquarium::class)
            subclass(AceOfClubsTable::class)
            subclass(ExitToTheRight::class)
            subclass(DecorMannequin::class)
            subclass(DecorMannequinDark::class)
            subclass(DecorTable::class)
            subclass(DecorREGift::class)
            subclass(DecorREThreeLeg::class)
            subclass(DecorDeadBody::class)
            subclass(DecorWeb::class)
            subclass(DecorDarkWeb::class)
            subclass(DecorBalloon::class)
            subclass(DecorTeddyBear::class)
            subclass(DecorBomb::class)
            subclass(DecorMannequinSoldier::class)
            subclass(DecorMannequinSoldierDark::class)
            subclass(MarketDecorWaxBox::class)
            subclass(DecorLeg::class)
            subclass(LetterCell::class)
            subclass(DecorDenied::class)
            subclass(DecorWeapon::class)
            subclass(DecorPapers::class)
            subclass(SavePointMarker::class)
            subclass(DecorEye::class)
            // INTERACTIVE
            subclass(AbysmalWaterRemains::class)
            subclass(CellInfo::class)
            subclass(CellGraffiti::class)
            subclass(CellGraffitiDestroyed::class)
            subclass(PointOfInterest::class)
            subclass(Note::class)
            subclass(EstonianRoom::class)
            subclass(CellMoney::class)
            subclass(CellMoneyGP::class)
            subclass(RedBull::class)
            subclass(TheMachine::class)
            subclass(AlienSpaceshipControls::class)
            subclass(TheMarryingMaidenHolder::class)
            // DOORS
            subclass(SashBottom::class)
            subclass(SashLeft::class)
            subclass(SashRight::class)
            subclass(SashTop::class)
            subclass(HorizontalDoor::class)
            subclass(VerticalDoor::class)
            // WALLS
            subclass(BrickWall::class)
            subclass(StoneWall::class)
            subclass(HashWall::class)
            subclass(FistWall::class)
            subclass(TempleWall::class)
            subclass(TempleWallInner::class)
            subclass(BlockWallGrey::class)
            subclass(BlockWallOrange::class)
            subclass(BlockWallAlmostBlack::class)
            subclass(BlockWallBrown::class)
            subclass(BlockWallParty::class)
            subclass(BlockWallGoodsStreet::class)
            // SKILL BOOKS
            subclass(SkillBookWindRequiem::class)
            subclass(SkillBookWind::class)
            subclass(SkillBookWater::class)
            subclass(SkillBookThunder::class)
            subclass(SkillBookMountain::class)
            subclass(SkillBookLakeRequiem::class)
            subclass(SkillBookLake::class)
            subclass(SkillBookHeaven::class)
            subclass(SkillBookFire::class)
            subclass(SkillBookEarth::class)
        }

        val serializersModule = SerializersModule {
            polymorphic(Cell::class) {
                subclass(CellEmpty::class)
                registerSubclasses()
            }
            polymorphic(CellNonEmpty::class) {
                registerSubclasses()
            }
            polymorphic(Trigram::class) {
                subclass(Earth::class)
                subclass(Fire::class)
                subclass(Heaven::class)
                subclass(Lake::class)
                subclass(Mountain::class)
                subclass(Thunder::class)
                subclass(Water::class)
                subclass(Wind::class)
            }
        }
    }
}