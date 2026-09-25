package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREntityTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRAnimalJarItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTreasurePileItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBarrelItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPlateItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRKebabStandItem;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMiscBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks.*;

/**
 * The LOTR blocks of tabDeco: plants, leaves, fences, panes, torches, chandeliers, carpets, banners, treasure.
 * Split out of LOTRBlocks by creative tab; LOTRBlocks keeps the shared
 * builders and family lists and loads this class from its init.
 */
public final class LOTRDecorationBlocks {

    public static final Block ORC_TORCH = registerDoubleTorch("orc_torch");

    // LOTRBlockOrcBomb, metadata 0/1/2 of one block in 1.7.10. The strength is
    // the blast radius and the fuse length; see LOTROrcBombBlock.
    // Registration order is getSubBlocks' order -- the three plain strengths,
    // then the three fire ones -- because the creative tab walks this list.
    // LOTRBlockRhunFire, the flame a broken jar leaves. No item: it is placed by
    // the jar and by nothing else.
    public static final Block KHAMULS_FIRE = register("khamuls_fire",
            LOTRKhamulsFireBlock::new, LOTRKhamulsFireBlock.fireProperties(), false);
    // lotr:tauredainDoubleTorch, the "Taurethrim Torch". Registered exactly as
    // the orc torch is -- both were a bare new LOTRBlockDoubleTorch() in
    // LOTRMod, differing only in their sprites.
    public static final Block TAUREDAIN_DOUBLE_TORCH = registerDoubleTorch("tauredain_double_torch");

    public static final Block BRONZE_BIRD_CAGE = registerBirdCage("bronze_bird_cage", SoundType.METAL);
    public static final Block IRON_BIRD_CAGE = registerBirdCage("iron_bird_cage", SoundType.METAL);
    public static final Block SILVER_BIRD_CAGE = registerBirdCage("silver_bird_cage", SoundType.METAL);
    public static final Block GOLD_BIRD_CAGE = registerBirdCage("gold_bird_cage", SoundType.METAL);
    public static final Block WOODEN_BIRD_CAGE = registerBirdCage("wooden_bird_cage", SoundType.WOOD);
    public static final Block BUTTERFLY_JAR = registerButterflyJar("butterfly_jar");

    // LOTRMod: new LOTRBlockWeaponRack(). Material.circuits, hardness 0.5,
    // resistance 1.0, wooden footsteps.

    // LOTRItemBanner.BannerType, one standing and one wall block each. Vanilla's
    // banner blocks do the placing and the rotating; see LOTRBannerBlock.
    public static final Block GONDOR_WALL_BANNER = registerWallBanner("gondor_wall_banner", LOTRBannerType.GONDOR);
    public static final Block GONDOR_BANNER = registerBanner("gondor_banner", LOTRBannerType.GONDOR, GONDOR_WALL_BANNER);
    public static final Block ROHAN_WALL_BANNER = registerWallBanner("rohan_wall_banner", LOTRBannerType.ROHAN);
    public static final Block ROHAN_BANNER = registerBanner("rohan_banner", LOTRBannerType.ROHAN, ROHAN_WALL_BANNER);
    public static final Block MORDOR_WALL_BANNER = registerWallBanner("mordor_wall_banner", LOTRBannerType.MORDOR);
    public static final Block MORDOR_BANNER = registerBanner("mordor_banner", LOTRBannerType.MORDOR, MORDOR_WALL_BANNER);
    public static final Block LOTHLORIEN_WALL_BANNER = registerWallBanner("lothlorien_wall_banner", LOTRBannerType.LOTHLORIEN);
    public static final Block LOTHLORIEN_BANNER = registerBanner("lothlorien_banner", LOTRBannerType.LOTHLORIEN, LOTHLORIEN_WALL_BANNER);
    public static final Block MIRKWOOD_WALL_BANNER = registerWallBanner("mirkwood_wall_banner", LOTRBannerType.MIRKWOOD);
    public static final Block MIRKWOOD_BANNER = registerBanner("mirkwood_banner", LOTRBannerType.MIRKWOOD, MIRKWOOD_WALL_BANNER);
    public static final Block DUNLAND_WALL_BANNER = registerWallBanner("dunland_wall_banner", LOTRBannerType.DUNLAND);
    public static final Block DUNLAND_BANNER = registerBanner("dunland_banner", LOTRBannerType.DUNLAND, DUNLAND_WALL_BANNER);
    public static final Block ISENGARD_WALL_BANNER = registerWallBanner("isengard_wall_banner", LOTRBannerType.ISENGARD);
    public static final Block ISENGARD_BANNER = registerBanner("isengard_banner", LOTRBannerType.ISENGARD, ISENGARD_WALL_BANNER);
    public static final Block DURIN_WALL_BANNER = registerWallBanner("durin_wall_banner", LOTRBannerType.DURIN);
    public static final Block DURIN_BANNER = registerBanner("durin_banner", LOTRBannerType.DURIN, DURIN_WALL_BANNER);
    public static final Block ANGMAR_WALL_BANNER = registerWallBanner("angmar_wall_banner", LOTRBannerType.ANGMAR);
    public static final Block ANGMAR_BANNER = registerBanner("angmar_banner", LOTRBannerType.ANGMAR, ANGMAR_WALL_BANNER);
    public static final Block NEAR_HARAD_WALL_BANNER = registerWallBanner("near_harad_wall_banner", LOTRBannerType.NEAR_HARAD);
    public static final Block NEAR_HARAD_BANNER = registerBanner("near_harad_banner", LOTRBannerType.NEAR_HARAD, NEAR_HARAD_WALL_BANNER);
    public static final Block HIGH_ELF_WALL_BANNER = registerWallBanner("high_elf_wall_banner", LOTRBannerType.HIGH_ELF);
    public static final Block HIGH_ELF_BANNER = registerBanner("high_elf_banner", LOTRBannerType.HIGH_ELF, HIGH_ELF_WALL_BANNER);
    public static final Block BLUE_MOUNTAINS_WALL_BANNER = registerWallBanner("blue_mountains_wall_banner", LOTRBannerType.BLUE_MOUNTAINS);
    public static final Block BLUE_MOUNTAINS_BANNER = registerBanner("blue_mountains_banner", LOTRBannerType.BLUE_MOUNTAINS, BLUE_MOUNTAINS_WALL_BANNER);
    public static final Block RANGER_WALL_BANNER = registerWallBanner("ranger_wall_banner", LOTRBannerType.RANGER);
    public static final Block RANGER_BANNER = registerBanner("ranger_banner", LOTRBannerType.RANGER, RANGER_WALL_BANNER);
    public static final Block DOL_GULDUR_WALL_BANNER = registerWallBanner("dol_guldur_wall_banner", LOTRBannerType.DOL_GULDUR);
    public static final Block DOL_GULDUR_BANNER = registerBanner("dol_guldur_banner", LOTRBannerType.DOL_GULDUR, DOL_GULDUR_WALL_BANNER);
    public static final Block GUNDABAD_WALL_BANNER = registerWallBanner("gundabad_wall_banner", LOTRBannerType.GUNDABAD);
    public static final Block GUNDABAD_BANNER = registerBanner("gundabad_banner", LOTRBannerType.GUNDABAD, GUNDABAD_WALL_BANNER);
    public static final Block HALF_TROLL_WALL_BANNER = registerWallBanner("half_troll_wall_banner", LOTRBannerType.HALF_TROLL);
    public static final Block HALF_TROLL_BANNER = registerBanner("half_troll_banner", LOTRBannerType.HALF_TROLL, HALF_TROLL_WALL_BANNER);
    public static final Block DOL_AMROTH_WALL_BANNER = registerWallBanner("dol_amroth_wall_banner", LOTRBannerType.DOL_AMROTH);
    public static final Block DOL_AMROTH_BANNER = registerBanner("dol_amroth_banner", LOTRBannerType.DOL_AMROTH, DOL_AMROTH_WALL_BANNER);
    public static final Block MOREDAIN_WALL_BANNER = registerWallBanner("moredain_wall_banner", LOTRBannerType.MOREDAIN);
    public static final Block MOREDAIN_BANNER = registerBanner("moredain_banner", LOTRBannerType.MOREDAIN, MOREDAIN_WALL_BANNER);
    public static final Block TAUREDAIN_WALL_BANNER = registerWallBanner("tauredain_wall_banner", LOTRBannerType.TAUREDAIN);
    public static final Block TAUREDAIN_BANNER = registerBanner("tauredain_banner", LOTRBannerType.TAUREDAIN, TAUREDAIN_WALL_BANNER);
    public static final Block DALE_WALL_BANNER = registerWallBanner("dale_wall_banner", LOTRBannerType.DALE);
    public static final Block DALE_BANNER = registerBanner("dale_banner", LOTRBannerType.DALE, DALE_WALL_BANNER);
    public static final Block DORWINION_WALL_BANNER = registerWallBanner("dorwinion_wall_banner", LOTRBannerType.DORWINION);
    public static final Block DORWINION_BANNER = registerBanner("dorwinion_banner", LOTRBannerType.DORWINION, DORWINION_WALL_BANNER);
    public static final Block HOBBIT_WALL_BANNER = registerWallBanner("hobbit_wall_banner", LOTRBannerType.HOBBIT);
    public static final Block HOBBIT_BANNER = registerBanner("hobbit_banner", LOTRBannerType.HOBBIT, HOBBIT_WALL_BANNER);
    public static final Block ANORIEN_WALL_BANNER = registerWallBanner("anorien_wall_banner", LOTRBannerType.ANORIEN);
    public static final Block ANORIEN_BANNER = registerBanner("anorien_banner", LOTRBannerType.ANORIEN, ANORIEN_WALL_BANNER);
    public static final Block ITHILIEN_WALL_BANNER = registerWallBanner("ithilien_wall_banner", LOTRBannerType.ITHILIEN);
    public static final Block ITHILIEN_BANNER = registerBanner("ithilien_banner", LOTRBannerType.ITHILIEN, ITHILIEN_WALL_BANNER);
    public static final Block LOSSARNACH_WALL_BANNER = registerWallBanner("lossarnach_wall_banner", LOTRBannerType.LOSSARNACH);
    public static final Block LOSSARNACH_BANNER = registerBanner("lossarnach_banner", LOTRBannerType.LOSSARNACH, LOSSARNACH_WALL_BANNER);
    public static final Block LEBENNIN_WALL_BANNER = registerWallBanner("lebennin_wall_banner", LOTRBannerType.LEBENNIN);
    public static final Block LEBENNIN_BANNER = registerBanner("lebennin_banner", LOTRBannerType.LEBENNIN, LEBENNIN_WALL_BANNER);
    public static final Block PELARGIR_WALL_BANNER = registerWallBanner("pelargir_wall_banner", LOTRBannerType.PELARGIR);
    public static final Block PELARGIR_BANNER = registerBanner("pelargir_banner", LOTRBannerType.PELARGIR, PELARGIR_WALL_BANNER);
    public static final Block BLACKROOT_VALE_WALL_BANNER = registerWallBanner("blackroot_vale_wall_banner", LOTRBannerType.BLACKROOT_VALE);
    public static final Block BLACKROOT_VALE_BANNER = registerBanner("blackroot_vale_banner", LOTRBannerType.BLACKROOT_VALE, BLACKROOT_VALE_WALL_BANNER);
    public static final Block PINNATH_GELIN_WALL_BANNER = registerWallBanner("pinnath_gelin_wall_banner", LOTRBannerType.PINNATH_GELIN);
    public static final Block PINNATH_GELIN_BANNER = registerBanner("pinnath_gelin_banner", LOTRBannerType.PINNATH_GELIN, PINNATH_GELIN_WALL_BANNER);
    public static final Block MINAS_MORGUL_WALL_BANNER = registerWallBanner("minas_morgul_wall_banner", LOTRBannerType.MINAS_MORGUL);
    public static final Block MINAS_MORGUL_BANNER = registerBanner("minas_morgul_banner", LOTRBannerType.MINAS_MORGUL, MINAS_MORGUL_WALL_BANNER);
    public static final Block BLACK_URUK_WALL_BANNER = registerWallBanner("black_uruk_wall_banner", LOTRBannerType.BLACK_URUK);
    public static final Block BLACK_URUK_BANNER = registerBanner("black_uruk_banner", LOTRBannerType.BLACK_URUK, BLACK_URUK_WALL_BANNER);
    public static final Block GONDOR_STEWARD_WALL_BANNER = registerWallBanner("gondor_steward_wall_banner", LOTRBannerType.GONDOR_STEWARD);
    public static final Block GONDOR_STEWARD_BANNER = registerBanner("gondor_steward_banner", LOTRBannerType.GONDOR_STEWARD, GONDOR_STEWARD_WALL_BANNER);
    public static final Block NAN_UNGOL_WALL_BANNER = registerWallBanner("nan_ungol_wall_banner", LOTRBannerType.NAN_UNGOL);
    public static final Block NAN_UNGOL_BANNER = registerBanner("nan_ungol_banner", LOTRBannerType.NAN_UNGOL, NAN_UNGOL_WALL_BANNER);
    public static final Block RHUDAUR_WALL_BANNER = registerWallBanner("rhudaur_wall_banner", LOTRBannerType.RHUDAUR);
    public static final Block RHUDAUR_BANNER = registerBanner("rhudaur_banner", LOTRBannerType.RHUDAUR, RHUDAUR_WALL_BANNER);
    public static final Block LAMEDON_WALL_BANNER = registerWallBanner("lamedon_wall_banner", LOTRBannerType.LAMEDON);
    public static final Block LAMEDON_BANNER = registerBanner("lamedon_banner", LOTRBannerType.LAMEDON, LAMEDON_WALL_BANNER);
    public static final Block RHUN_WALL_BANNER = registerWallBanner("rhun_wall_banner", LOTRBannerType.RHUN);
    public static final Block RHUN_BANNER = registerBanner("rhun_banner", LOTRBannerType.RHUN, RHUN_WALL_BANNER);
    public static final Block RIVENDELL_WALL_BANNER = registerWallBanner("rivendell_wall_banner", LOTRBannerType.RIVENDELL);
    public static final Block RIVENDELL_BANNER = registerBanner("rivendell_banner", LOTRBannerType.RIVENDELL, RIVENDELL_WALL_BANNER);
    public static final Block ESGAROTH_WALL_BANNER = registerWallBanner("esgaroth_wall_banner", LOTRBannerType.ESGAROTH);
    public static final Block ESGAROTH_BANNER = registerBanner("esgaroth_banner", LOTRBannerType.ESGAROTH, ESGAROTH_WALL_BANNER);
    public static final Block UMBAR_WALL_BANNER = registerWallBanner("umbar_wall_banner", LOTRBannerType.UMBAR);
    public static final Block UMBAR_BANNER = registerBanner("umbar_banner", LOTRBannerType.UMBAR, UMBAR_WALL_BANNER);
    public static final Block HARAD_NOMAD_WALL_BANNER = registerWallBanner("harad_nomad_wall_banner", LOTRBannerType.HARAD_NOMAD);
    public static final Block HARAD_NOMAD_BANNER = registerBanner("harad_nomad_banner", LOTRBannerType.HARAD_NOMAD, HARAD_NOMAD_WALL_BANNER);
    public static final Block HARAD_GULF_WALL_BANNER = registerWallBanner("harad_gulf_wall_banner", LOTRBannerType.HARAD_GULF);
    public static final Block HARAD_GULF_BANNER = registerBanner("harad_gulf_banner", LOTRBannerType.HARAD_GULF, HARAD_GULF_WALL_BANNER);
    public static final Block BREE_WALL_BANNER = registerWallBanner("bree_wall_banner", LOTRBannerType.BREE);
    public static final Block BREE_BANNER = registerBanner("bree_banner", LOTRBannerType.BREE, BREE_WALL_BANNER);

    public static final Block WEAPON_RACK = register("weapon_rack", LOTRWeaponRackBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.5f, 0.6f) // setHardness(0.5).setResistance(1): blast 0.6
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY),
            // Stack of one. LOTRItemBlockWeaponRack did NOT limit its stack --
            // racks stacked to 64 in 1.7.10 -- so this is a deliberate change.
            item -> item.stacksTo(1));

    public static final Block CLOVER = registerClover("clover", true);
    public static final Block FOUR_LEAF_CLOVER = registerClover("four_leaf_clover", false);

    public static final Block HIGH_ELVEN_WALL_TORCH = registerWallTorch("high_elven_wall_torch", LOTRGlowStyle.ELVEN_GLOW);
    public static final Block HIGH_ELVEN_TORCH = registerTorch("high_elven_torch", HIGH_ELVEN_WALL_TORCH, LOTRGlowStyle.ELVEN_GLOW);
    public static final Block WOOD_ELVEN_WALL_TORCH = registerWallTorch("wood_elven_wall_torch", LOTRGlowStyle.WOOD_ELVEN_TORCH);
    public static final Block WOOD_ELVEN_TORCH = registerTorch("wood_elven_torch", WOOD_ELVEN_WALL_TORCH, LOTRGlowStyle.WOOD_ELVEN_TORCH);
    public static final Block MORGUL_WALL_TORCH = registerWallTorch("morgul_wall_torch", LOTRGlowStyle.MORGUL);
    public static final Block MORGUL_TORCH = registerTorch("morgul_torch", MORGUL_WALL_TORCH, LOTRGlowStyle.MORGUL);
    public static final Block MALLORN_WALL_TORCH = registerWallTorch("mallorn_wall_torch", LOTRGlowStyle.MALLORN_SILVER);
    public static final Block MALLORN_TORCH = registerTorch("mallorn_torch", MALLORN_WALL_TORCH, LOTRGlowStyle.MALLORN_SILVER);
    public static final Block MALLORN_BLUE_WALL_TORCH = registerWallTorch("mallorn_blue_wall_torch", LOTRGlowStyle.MALLORN_BLUE);
    public static final Block BLUE_MALLORN_TORCH = registerTorch("blue_mallorn_torch", MALLORN_BLUE_WALL_TORCH, LOTRGlowStyle.MALLORN_BLUE);
    public static final Block MALLORN_GOLD_WALL_TORCH = registerWallTorch("mallorn_gold_wall_torch", LOTRGlowStyle.MALLORN_GOLD);
    public static final Block MALLORN_GOLD_TORCH = registerTorch("mallorn_gold_torch", MALLORN_GOLD_WALL_TORCH, LOTRGlowStyle.MALLORN_GOLD);
    public static final Block MALLORN_GREEN_WALL_TORCH = registerWallTorch("mallorn_green_wall_torch", LOTRGlowStyle.MALLORN_GREEN);
    public static final Block GREEN_MALLORN_TORCH = registerTorch("green_mallorn_torch", MALLORN_GREEN_WALL_TORCH, LOTRGlowStyle.MALLORN_GREEN);

    public static final Block TALL_GRASS_FERNSPROUT = registerGrass("tall_grass_fernsprout", LOTRPlantBlock.Ground.SOIL);
    public static final Block TALL_GRASS_FLOWER = registerGrass("tall_grass_flower", LOTRPlantBlock.Ground.SOIL);
    public static final Block TALL_GRASS_NETTLE = registerGrass("tall_grass_nettle", LOTRPlantBlock.Ground.SOIL, LOTRPlantBlock.Sting.NETTLE);
    public static final Block TALL_GRASS_SHORT = registerGrass("tall_grass_short", LOTRPlantBlock.Ground.SOIL);
    public static final Block TALL_GRASS_THISTLE = registerGrass("tall_grass_thistle", LOTRPlantBlock.Ground.SOIL, LOTRPlantBlock.Sting.THISTLE);
    public static final Block TALL_GRASS_WHEAT = registerGrass("tall_grass_wheat", LOTRPlantBlock.Ground.SOIL);

    public static final Block IVY = registerVine("ivy");
    public static final Block RED_IVY = registerVine("red_ivy");
    public static final Block MIRK_VINES = registerVine("mirk_vines");
    public static final Block WILLOW_VINES = registerVine("willow_vines");

    public static final Block HITHLAIN_LADDER = registerRope("hithlain_ladder", true, true);
    public static final Block MALLORN_LADDER = registerLadder("mallorn_ladder");
    // LOTRBlockTreasurePile, NOT a plain cube: eight layer heights, the
    // shallowest of them carpet-thin. They were registered as full blocks here
    // by mistake.
    public static final Block TREASURE_COPPER = registerTreasurePile("treasure_copper");
    public static final Block TREASURE_GOLD = registerTreasurePile("treasure_gold");
    public static final Block TREASURE_SILVER = registerTreasurePile("treasure_silver");

    public static final Block BERRY_BUSH_BLACKBERRY = registerBush("berry_bush_blackberry");
    public static final Block BERRY_BUSH_BLUEBERRY = registerBush("berry_bush_blueberry");
    public static final Block BERRY_BUSH_CRANBERRY = registerBush("berry_bush_cranberry");
    public static final Block BERRY_BUSH_ELDERBERRY = registerBush("berry_bush_elderberry");
    public static final Block BERRY_BUSH_RASPBERRY = registerBush("berry_bush_raspberry");
    public static final Block BERRY_BUSH_WILDBERRY = registerBush("berry_bush_wildberry");

    public static final Block CORN_STALK = registerCorn("corn_stalk");
    // The bare post: LOTRBlockGrapevine(false) was hardness 2, resistance 5
    // (blast 3), soundTypeWood, and solid to walk into -- a fence post, not a plant.
    public static final Block GRAPEVINE = track(ALL_FLOWERS, register("grapevine",
            props -> new LOTRPlantBlock(LOTRPlantBlock.Shape.POST, LOTRPlantBlock.Ground.STURDY_OR_SELF, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0f, 3.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY),
            true));
    // LOTRBlockGrapevineRed and LOTRBlockGrapevineWhite: the bearing vines,
    // planted from grape seeds. setCreativeTab(null) in the original -- you get
    // them by planting, never out of the menu -- so no item and no tab entry.
    public static final Block RED_GRAPEVINE = registerGrapevine("red_grapevine");
    public static final Block GREEN_GRAPEVINE = registerGrapevine("green_grapevine");
    public static final Block REEDS = registerReed("reeds", true);
    public static final Block DRIED_REEDS = registerReed("dried_reeds", false);
    public static final Block FANGORN_RIVERWEED = registerRiverweed("fangorn_riverweed");
    public static final Block WEB_UNGOLIANT = registerWeb("web_ungoliant");

    // LOTRBlockMarshLights: Material.circuits, never drawn, no item (it only
    // ever came from world generation and had no icon).
    public static final Block MARSH_LIGHTS = register("marsh_lights", LOTRMarshLightsBlock::new,
            BlockBehaviour.Properties.of().noCollision().noOcclusion().noLootTable()
                    .pushReaction(PushReaction.DESTROY), false);

    // LOTRBlockGoran: hardness 0, stone footsteps, Material.rock -- so a
    // pickaxe to get it back. Metadata 1 (goranNames "rock") is Cargoran.
    public static final Block GORAN = register("goran", LOTRGoranBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak().sound(SoundType.STONE)
                    .requiresCorrectToolForDrops(), true);
    public static final Block GORAN_ROCK = register("goran_rock", LOTRGoranBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak().sound(SoundType.STONE)
                    .requiresCorrectToolForDrops(), true);

    // LOTRBlockStalactite, one block per metadata now: 0 hung from a ceiling,
    // 1 stood on a floor. stalactite on stone, stalactiteIce on packed ice,
    // stalactiteObsidian on obsidian.
    public static final Block STALACTITE = registerStalactite("stalactite", Blocks.STONE, true);
    public static final Block STALAGMITE = registerStalactite("stalagmite", Blocks.STONE, false);
    public static final Block ICE_STALACTITE = registerStalactite("ice_stalactite", Blocks.PACKED_ICE, true);
    public static final Block ICE_STALAGMITE = registerStalactite("ice_stalagmite", Blocks.PACKED_ICE, false);
    public static final Block OBSIDIAN_STALACTITE = registerStalactite("obsidian_stalactite", Blocks.OBSIDIAN, true);
    public static final Block OBSIDIAN_STALAGMITE = registerStalactite("obsidian_stalagmite", Blocks.OBSIDIAN, false);
    public static final Block ROPE = registerRope("rope", false, false);

    public static final Block MORDOR_MOSS = registerCarpet("mordor_moss", 0.2f);
    public static final Block THATCH_FLOOR = registerCarpet("thatch_floor", 0.2f);

    public static final Block CHERRY_LEAVES = registerLeaves("cherry_leaves");
    public static final Block MANGROVE_LEAVES = registerLeaves("mangrove_leaves");
    public static final Block ALMOND_LEAVES = registerLeaves("almond_leaves");
    public static final Block APPLE_LEAVES = registerLeaves("apple_leaves");
    public static final Block ASPEN_LEAVES = registerLeaves("aspen_leaves");
    public static final Block BANANA_LEAVES = registerLeaves("banana_leaves");
    public static final Block BAOBAB_LEAVES = registerLeaves("baobab_leaves");
    public static final Block BEECH_LEAVES = registerLeaves("beech_leaves");
    public static final Block CEDAR_LEAVES = registerLeaves("cedar_leaves");
    public static final Block CHESTNUT_LEAVES = registerLeaves("chestnut_leaves");
    public static final Block CYPRESS_LEAVES = registerLeaves("cypress_leaves");
    public static final Block DATE_PALM_LEAVES = registerLeaves("date_palm_leaves");
    public static final Block DRAGON_LEAVES = registerLeaves("dragon_leaves");
    public static final Block FIR_LEAVES = registerLeaves("fir_leaves");
    public static final Block GREEN_OAK_LEAVES = registerLeaves("green_oak_leaves");
    public static final Block HOLLY_LEAVES = registerLeaves("holly_leaves");
    public static final Block KANUKA_LEAVES = registerLeaves("kanuka_leaves");
    public static final Block LAIRELOSSE_LEAVES = registerLeaves("lairelosse_leaves");
    public static final Block LARCH_LEAVES = registerLeaves("larch_leaves");
    public static final Block LEBETHRON_LEAVES = registerLeaves("lebethron_leaves");
    public static final Block LEMON_LEAVES = registerLeaves("lemon_leaves");
    public static final Block LIME_LEAVES = registerLeaves("lime_leaves");
    public static final Block MAHOGANY_LEAVES = registerLeaves("mahogany_leaves");
    public static final Block MALLORN_LEAVES = registerLeaves("mallorn_leaves");
    public static final Block MANGO_LEAVES = registerLeaves("mango_leaves");
    public static final Block MAPLE_LEAVES = registerLeaves("maple_leaves");
    public static final Block MIRK_OAK_LEAVES = registerLeaves("mirk_oak_leaves");
    public static final Block MIRK_OAK_RED_LEAVES = registerLeaves("mirk_oak_red_leaves");
    public static final Block OLIVE_LEAVES = registerLeaves("olive_leaves");
    public static final Block ORANGE_LEAVES = registerLeaves("orange_leaves");
    public static final Block PALM_LEAVES = registerLeaves("palm_leaves");
    public static final Block PEAR_LEAVES = registerLeaves("pear_leaves");
    public static final Block PINE_LEAVES = registerLeaves("pine_leaves");
    public static final Block PLUM_LEAVES = registerLeaves("plum_leaves");
    public static final Block POMEGRANATE_LEAVES = registerLeaves("pomegranate_leaves");
    public static final Block REDWOOD_LEAVES = registerLeaves("redwood_leaves");
    public static final Block SHIRE_PINE_LEAVES = registerLeaves("shire_pine_leaves");
    public static final Block WILLOW_LEAVES = registerLeaves("willow_leaves");

    public static final Block CHERRY_SAPLING = registerSapling("cherry_sapling");
    public static final Block ALMOND_SAPLING = registerSapling("almond_sapling");
    public static final Block APPLE_SAPLING = registerSapling("apple_sapling");
    public static final Block ASPEN_SAPLING = registerSapling("aspen_sapling");
    public static final Block BANANA_SAPLING = registerSapling("banana_sapling");
    public static final Block BAOBAB_SAPLING = registerSapling("baobab_sapling");
    public static final Block BEECH_SAPLING = registerSapling("beech_sapling");
    public static final Block CEDAR_SAPLING = registerSapling("cedar_sapling");
    public static final Block CHESTNUT_SAPLING = registerSapling("chestnut_sapling");
    public static final Block CYPRESS_SAPLING = registerSapling("cypress_sapling");
    public static final Block DATE_PALM_SAPLING = registerSapling("date_palm_sapling");
    public static final Block DRAGON_SAPLING = registerSapling("dragon_sapling");
    public static final Block FIR_SAPLING = registerSapling("fir_sapling");
    public static final Block GREEN_OAK_SAPLING = registerSapling("green_oak_sapling");
    public static final Block HOLLY_SAPLING = registerSapling("holly_sapling");
    public static final Block KANUKA_SAPLING = registerSapling("kanuka_sapling");
    public static final Block LAIRELOSSE_SAPLING = registerSapling("lairelosse_sapling");
    public static final Block LARCH_SAPLING = registerSapling("larch_sapling");
    public static final Block LEBETHRON_SAPLING = registerSapling("lebethron_sapling");
    public static final Block LEMON_SAPLING = registerSapling("lemon_sapling");
    public static final Block LIME_SAPLING = registerSapling("lime_sapling");
    public static final Block MAHOGANY_SAPLING = registerSapling("mahogany_sapling");
    public static final Block MALLORN_SAPLING = registerSapling("mallorn_sapling");
    public static final Block MANGO_SAPLING = registerSapling("mango_sapling");
    public static final Block MANGROVE_SAPLING = registerSapling("mangrove_sapling");
    public static final Block MAPLE_SAPLING = registerSapling("maple_sapling");
    public static final Block MIRK_OAK_RED_SAPLING = registerSapling("mirk_oak_red_sapling");
    public static final Block MIRK_OAK_SAPLING = registerSapling("mirk_oak_sapling");
    public static final Block OLIVE_SAPLING = registerSapling("olive_sapling");
    public static final Block ORANGE_SAPLING = registerSapling("orange_sapling");
    public static final Block PALM_SAPLING = registerSapling("palm_sapling");
    public static final Block PEAR_SAPLING = registerSapling("pear_sapling");
    public static final Block PINE_SAPLING = registerSapling("pine_sapling");
    public static final Block PLUM_SAPLING = registerSapling("plum_sapling");
    public static final Block POMEGRANATE_SAPLING = registerSapling("pomegranate_sapling");
    public static final Block REDWOOD_SAPLING = registerSapling("redwood_sapling");
    public static final Block SHIRE_PINE_SAPLING = registerSapling("shire_pine_sapling");
    public static final Block WILLOW_SAPLING = registerSapling("willow_sapling");

    public static final Block ASPHODEL = registerFlower("asphodel");
    public static final Block ATHELAS = registerFlower("athelas", LOTRPlantBlock.Shape.BROAD);
    public static final Block BLUEBELL = registerFlower("bluebell");
    public static final Block DWARF_HERB = registerFlower("dwarf_herb");
    public static final Block ELANOR = registerFlower("elanor");
    public static final Block FLAX_PLANT = registerFlower("flax_plant", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block LAVENDER = registerFlower("lavender", LOTRPlantBlock.Shape.BROAD);
    public static final Block MARIGOLD = registerFlower("marigold");
    public static final Block MORGUL_FLOWER = registerFlower("morgul_flower", LOTRPlantBlock.Shape.WIDE, LOTRPlantBlock.Ground.SOIL_OR_MORDOR);
    public static final Block NIPHREDIL = registerFlower("niphredil");
    public static final Block SHIRE_HEATHER = registerFlower("shire_heather", LOTRPlantBlock.Shape.BROAD);
    public static final Block SIMBELMYNE = registerFlower("simbelmyne");
    public static final Block HARAD_FLOWER_DAISY = registerFlower("harad_flower_daisy");
    public static final Block PINK_HARAD_FLOWER = registerFlower("pink_harad_flower");
    public static final Block RED_HARAD_FLOWER = registerFlower("red_harad_flower");
    public static final Block YELLOW_HARAD_FLOWER = registerFlower("yellow_harad_flower");
    public static final Block RHUN_FLOWER_CHRYS_BLUE = registerFlower("rhun_flower_chrys_blue");
    public static final Block RHUN_FLOWER_CHRYS_ORANGE = registerFlower("rhun_flower_chrys_orange");
    public static final Block RHUN_FLOWER_CHRYS_PINK = registerFlower("rhun_flower_chrys_pink");
    public static final Block RHUN_FLOWER_CHRYS_WHITE = registerFlower("rhun_flower_chrys_white");
    public static final Block RHUN_FLOWER_CHRYS_YELLOW = registerFlower("rhun_flower_chrys_yellow");

    public static final Block ARID_GRASS = registerGrass("arid_grass", LOTRPlantBlock.Ground.SOIL_OR_SAND);
    public static final Block BLACKROOT = registerFlower("blackroot", LOTRPlantBlock.Shape.WIDE);
    // LOTRBlockCorruptMallorn: setLightLevel(0.625f), light 9.
    public static final Block CORRUPT_MALLORN = track(ALL_FLOWERS, register("corrupt_mallorn",
            props -> new LOTRPlantBlock(LOTRPlantBlock.Shape.GRASS, LOTRPlantBlock.Ground.SOIL, props),
            plantProperties().offsetType(BlockBehaviour.OffsetType.XZ).lightLevel(state -> 9), true));
    public static final Block DEAD_MARSH_PLANT = registerFlower("dead_marsh_plant", LOTRPlantBlock.Shape.GRASS);
    public static final Block FANGORN_PLANT_BROWN = registerFlower("fangorn_plant_brown", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_GOLD = registerFlower("fangorn_plant_gold", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_GREEN = registerFlower("fangorn_plant_green", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_RED = registerFlower("fangorn_plant_red", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_SILVER = registerFlower("fangorn_plant_silver", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_YELLOW = registerFlower("fangorn_plant_yellow", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block MORDOR_GRASS = registerGrass("mordor_grass", LOTRPlantBlock.Ground.MORDOR);
    public static final Block MORDOR_THORN = registerGrass("mordor_thorn", LOTRPlantBlock.Ground.MORDOR, LOTRPlantBlock.Sting.THORN);
    public static final Block PIPEWEED_PLANT = registerFlower("pipeweed_plant", LOTRPlantBlock.Shape.GRASS);

    // LOTRBlockDoubleFlower's four metas, one block each. It extended
    // BlockDoublePlant, and everything it changed is what vanilla's
    // TallFlowerBlock already does: the top half drops nothing, breaking either
    // half takes the other (without a drop in creative), and bone meal pops a
    // copy of the flower rather than growing anything.
    public static final Block BLACK_IRIS = registerDoubleFlower("black_iris");
    public static final Block YELLOW_IRIS = registerDoubleFlower("yellow_iris");
    public static final Block HIBISCUS = registerDoubleFlower("hibiscus");
    public static final Block FLAME_OF_HARAD = registerDoubleFlower("flame_of_harad");

    public static final Block BLUE_DWARF_BARS = registerBars("blue_dwarf_bars");
    public static final Block BRONZE_BARS = registerBars("bronze_bars");
    public static final Block DWARF_BARS = registerBars("dwarf_bars");
    public static final Block GALADHRIM_BARS = registerBars("galadhrim_bars");
    public static final Block GALADHRIM_WOOD_BARS = registerWoodBars("galadhrim_wood_bars");
    public static final Block GOLD_BARS = registerBars("gold_bars");
    public static final Block HIGH_ELF_BARS = registerBars("high_elf_bars");
    public static final Block HIGH_ELF_WOOD_BARS = registerWoodBars("high_elf_wood_bars");
    public static final Block MITHRIL_BARS = registerBars("mithril_bars");
    public static final Block ORC_STEEL_BARS = registerBars("orc_steel_bars");
    public static final Block REED_BARS = registerReedBars("reed_bars");
    public static final Block SILVER_BARS = registerBars("silver_bars");
    public static final Block URUK_BARS = registerBars("uruk_bars");
    public static final Block WOOD_ELF_BARS = registerBars("wood_elf_bars");
    public static final Block WOOD_ELF_WOOD_BARS = registerWoodBars("wood_elf_wood_bars");

    public static final Block BLUE_DWARVEN_CHANDELIER = registerChandelier("blue_dwarven_chandelier", LOTRGlowStyle.FLAME);
    public static final Block BRONZE_CHANDELIER = registerChandelier("bronze_chandelier", LOTRGlowStyle.FLAME);
    public static final Block DWARVEN_CHANDELIER = registerChandelier("dwarven_chandelier", LOTRGlowStyle.FLAME);
    public static final Block GOLD_CHANDELIER = registerChandelier("gold_chandelier", LOTRGlowStyle.FLAME);
    public static final Block HIGH_ELVEN_CHANDELIER = registerChandelier("high_elven_chandelier", LOTRGlowStyle.ELVEN_GLOW_STEADY);
    public static final Block IRON_CHANDELIER = registerChandelier("iron_chandelier", LOTRGlowStyle.FLAME);
    public static final Block BLUE_MALLORN_CHANDELIER = registerChandelier("blue_mallorn_chandelier", LOTRGlowStyle.MALLORN_BLUE);
    public static final Block MALLORN_GOLD_CHANDELIER = registerChandelier("mallorn_gold_chandelier", LOTRGlowStyle.MALLORN_GOLD);
    public static final Block GREEN_MALLORN_CHANDELIER = registerChandelier("green_mallorn_chandelier", LOTRGlowStyle.MALLORN_GREEN);
    public static final Block SILVER_MALLORN_CHANDELIER = registerChandelier("silver_mallorn_chandelier", LOTRGlowStyle.MALLORN_SILVER);
    public static final Block MITHRIL_CHANDELIER = registerChandelier("mithril_chandelier", LOTRGlowStyle.FLAME);
    public static final Block MORGUL_CHANDELIER = registerChandelier("morgul_chandelier", LOTRGlowStyle.MORGUL);
    public static final Block ORC_CHANDELIER = registerChandelier("orc_chandelier", LOTRGlowStyle.FLAME);
    public static final Block SILVER_CHANDELIER = registerChandelier("silver_chandelier", LOTRGlowStyle.FLAME);
    public static final Block URUK_CHANDELIER = registerChandelier("uruk_chandelier", LOTRGlowStyle.FLAME);
    public static final Block WOOD_ELVEN_CHANDELIER = registerChandelier("wood_elven_chandelier", LOTRGlowStyle.WOOD_ELVEN_CHANDELIER);

    public static final Block BLACK_STAINED_GLASS_PANE = registerGlassPane("black_stained_glass_pane", BLACK_STAINED_GLASS);
    public static final Block BLUE_STAINED_GLASS_PANE = registerGlassPane("blue_stained_glass_pane", BLUE_STAINED_GLASS);
    public static final Block BROWN_STAINED_GLASS_PANE = registerGlassPane("brown_stained_glass_pane", BROWN_STAINED_GLASS);
    public static final Block CYAN_STAINED_GLASS_PANE = registerGlassPane("cyan_stained_glass_pane", CYAN_STAINED_GLASS);
    public static final Block GRAY_STAINED_GLASS_PANE = registerGlassPane("gray_stained_glass_pane", GRAY_STAINED_GLASS);
    public static final Block GREEN_STAINED_GLASS_PANE = registerGlassPane("green_stained_glass_pane", GREEN_STAINED_GLASS);
    public static final Block LIGHT_BLUE_STAINED_GLASS_PANE = registerGlassPane("light_blue_stained_glass_pane", LIGHT_BLUE_STAINED_GLASS);
    public static final Block LIME_STAINED_GLASS_PANE = registerGlassPane("lime_stained_glass_pane", LIME_STAINED_GLASS);
    public static final Block MAGENTA_STAINED_GLASS_PANE = registerGlassPane("magenta_stained_glass_pane", MAGENTA_STAINED_GLASS);
    public static final Block ORANGE_STAINED_GLASS_PANE = registerGlassPane("orange_stained_glass_pane", ORANGE_STAINED_GLASS);
    public static final Block PINK_STAINED_GLASS_PANE = registerGlassPane("pink_stained_glass_pane", PINK_STAINED_GLASS);
    public static final Block PURPLE_STAINED_GLASS_PANE = registerGlassPane("purple_stained_glass_pane", PURPLE_STAINED_GLASS);
    public static final Block RED_STAINED_GLASS_PANE = registerGlassPane("red_stained_glass_pane", RED_STAINED_GLASS);
    public static final Block SILVER_STAINED_GLASS_PANE = registerGlassPane("silver_stained_glass_pane", SILVER_STAINED_GLASS);
    public static final Block WHITE_STAINED_GLASS_PANE = registerGlassPane("white_stained_glass_pane", WHITE_STAINED_GLASS);
    public static final Block YELLOW_STAINED_GLASS_PANE = registerGlassPane("yellow_stained_glass_pane", YELLOW_STAINED_GLASS);
    public static final Block GLASS_PANE = registerGlassPane("glass_pane", GLASS);

    public static final Block SHIRE_PINE_FENCE = registerFence("shire_pine_fence", SHIRE_PINE_PLANKS);
    public static final Block MALLORN_FENCE = registerFence("mallorn_fence", MALLORN_PLANKS);
    public static final Block MIRK_OAK_FENCE = registerFence("mirk_oak_fence", MIRK_OAK_PLANKS);
    public static final Block CHARRED_FENCE = registerFence("charred_fence", CHARRED_PLANKS);
    public static final Block APPLE_FENCE = registerFence("apple_fence", APPLE_PLANKS);
    public static final Block PEAR_FENCE = registerFence("pear_fence", PEAR_PLANKS);
    public static final Block CHERRY_FENCE = registerFence("cherry_fence", CHERRY_PLANKS);
    public static final Block MANGO_FENCE = registerFence("mango_fence", MANGO_PLANKS);
    public static final Block LEBETHRON_FENCE = registerFence("lebethron_fence", LEBETHRON_PLANKS);
    public static final Block BEECH_FENCE = registerFence("beech_fence", BEECH_PLANKS);
    public static final Block HOLLY_FENCE = registerFence("holly_fence", HOLLY_PLANKS);
    public static final Block BANANA_FENCE = registerFence("banana_fence", BANANA_PLANKS);
    public static final Block MAPLE_FENCE = registerFence("maple_fence", MAPLE_PLANKS);
    public static final Block LARCH_FENCE = registerFence("larch_fence", LARCH_PLANKS);
    public static final Block DATE_PALM_FENCE = registerFence("date_palm_fence", DATE_PALM_PLANKS);
    public static final Block MANGROVE_FENCE = registerFence("mangrove_fence", MANGROVE_PLANKS);
    public static final Block CHESTNUT_FENCE = registerFence("chestnut_fence", CHESTNUT_PLANKS);
    public static final Block BAOBAB_FENCE = registerFence("baobab_fence", BAOBAB_PLANKS);
    public static final Block CEDAR_FENCE = registerFence("cedar_fence", CEDAR_PLANKS);
    public static final Block FIR_FENCE = registerFence("fir_fence", FIR_PLANKS);
    public static final Block PINE_FENCE = registerFence("pine_fence", PINE_PLANKS);
    public static final Block LEMON_FENCE = registerFence("lemon_fence", LEMON_PLANKS);
    public static final Block ORANGE_FENCE = registerFence("orange_fence", ORANGE_PLANKS);
    public static final Block LIME_FENCE = registerFence("lime_fence", LIME_PLANKS);
    public static final Block MAHOGANY_FENCE = registerFence("mahogany_fence", MAHOGANY_PLANKS);
    public static final Block WILLOW_FENCE = registerFence("willow_fence", WILLOW_PLANKS);
    public static final Block CYPRESS_FENCE = registerFence("cypress_fence", CYPRESS_PLANKS);
    public static final Block OLIVE_FENCE = registerFence("olive_fence", OLIVE_PLANKS);
    public static final Block ASPEN_FENCE = registerFence("aspen_fence", ASPEN_PLANKS);
    public static final Block GREEN_OAK_FENCE = registerFence("green_oak_fence", GREEN_OAK_PLANKS);
    public static final Block LAIRELOSSE_FENCE = registerFence("lairelosse_fence", LAIRELOSSE_PLANKS);
    public static final Block ALMOND_FENCE = registerFence("almond_fence", ALMOND_PLANKS);
    public static final Block ROTTEN_FENCE = registerFence("rotten_fence", ROTTEN_PLANKS);
    public static final Block PLUM_FENCE = registerFence("plum_fence", PLUM_PLANKS);
    public static final Block REDWOOD_FENCE = registerFence("redwood_fence", REDWOOD_PLANKS);
    public static final Block POMEGRANATE_FENCE = registerFence("pomegranate_fence", POMEGRANATE_PLANKS);
    public static final Block PALM_FENCE = registerFence("palm_fence", PALM_PLANKS);
    public static final Block DRAGON_FENCE = registerFence("dragon_fence", DRAGON_PLANKS);
    public static final Block KANUKA_FENCE = registerFence("kanuka_fence", KANUKA_PLANKS);

    static {
        // LOTRBlockClover.colorMultiplier returned getBiomeGrassColor too.
        GRASS_TINTED.addAll(ALL_CLOVERS);
        GRASS_TINTED.addAll(List.of(TALL_GRASS_SHORT, TALL_GRASS_FLOWER, TALL_GRASS_WHEAT,
                TALL_GRASS_THISTLE, TALL_GRASS_NETTLE, TALL_GRASS_FERNSPROUT));
        GRASS_TINTED_WITH_OVERLAY.addAll(List.of(TALL_GRASS_FLOWER, TALL_GRASS_WHEAT, TALL_GRASS_THISTLE));
    }

    // LOTRBlockFallenLeaves.assignLeaves: fallenLeaves took vanilla's leaves
    // and leaves2, the three fallenLeavesLOTR blocks every leaf of the mod.
    // Registered after the leaves so each can name its own.
    static {
        for (Block leaves : List.of(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES,
                Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES)) {
            registerFallenLeaves(leaves);
        }
        for (Block leaves : List.copyOf(LOTRBlocks.ALL_LEAVES)) {
            registerFallenLeaves(leaves);
        }
    }

    private LOTRDecorationBlocks() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRBlocks.init. */
    static void init() {
    }
}
