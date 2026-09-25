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
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMiscBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks.*;

/**
 * The LOTR blocks of tabUtil: crafting tables, forges, ovens, chests, doors, gates, beds, traps and the like.
 * Split out of LOTRBlocks by creative tab; LOTRBlocks keeps the shared
 * builders and family lists and loads this class from its init.
 */
public final class LOTRUtilityBlocks {

    public static final Block TAUREDAIN_DART_TRAP = registerDartTrap("tauredain_dart_trap", TAUREDAIN_BRICK);
    public static final Block GOLD_TAUREDAIN_DART_TRAP = registerDartTrap("gold_tauredain_dart_trap", TAUREDAIN_GOLD_BRICK);
    public static final Block OBSIDIAN_TAUREDAIN_DART_TRAP = registerDartTrap("obsidian_tauredain_dart_trap", TAUREDAIN_OBSIDIAN_BRICK);

    // Forges. Four blocks, one block entity, thirteen slots -- see LOTRForgeBlockEntity.
    public static final Block DWARVEN_FORGE = registerForge("dwarven_forge");
    public static final Block ELVEN_FORGE = registerForge("elven_forge");
    public static final Block ORC_FORGE = registerForge("orc_forge");
    public static final Block ALLOY_FORGE = registerForge("alloy_forge");

    // Beacon of Gondor. Custom geometry, so it joins no family list: its
    // blockstate, models and item model are hand-written under
    // src/main/resources/assets/lotr/, not generated.
    public static final Block BEACON_OF_GONDOR = registerBeacon("beacon_of_gondor");

    // Hobbit oven. Nineteen slots, nine cook lanes -- see
    // LOTRHobbitOvenBlockEntity. Light 13 while lit, as in the original.
    public static final Block HOBBIT_OVEN = registerHobbitOven("hobbit_oven");

    public static final Block ANGMAR_CRAFTING_TABLE = registerCraftingTable("angmar_crafting_table");
    public static final Block BLUE_DWARVEN_CRAFTING_TABLE = registerCraftingTable("blue_dwarven_crafting_table");
    public static final Block BREE_CRAFTING_TABLE = registerCraftingTable("bree_crafting_table");
    public static final Block DALE_CRAFTING_TABLE = registerCraftingTable("dale_crafting_table");
    public static final Block DOL_AMROTH_CRAFTING_TABLE = registerCraftingTable("dol_amroth_crafting_table");
    public static final Block DOL_GULDUR_CRAFTING_TABLE = registerCraftingTable("dol_guldur_crafting_table");
    public static final Block DORWINION_CRAFTING_TABLE = registerCraftingTable("dorwinion_crafting_table");
    public static final Block DUNLENDING_CRAFTING_TABLE = registerCraftingTable("dunlending_crafting_table");
    public static final Block DWARVEN_CRAFTING_TABLE = registerCraftingTable("dwarven_crafting_table");
    public static final Block ELVEN_CRAFTING_TABLE = registerCraftingTable("elven_crafting_table");
    public static final Block GONDORIAN_CRAFTING_TABLE = registerCraftingTable("gondorian_crafting_table");
    public static final Block GULF_CRAFTING_TABLE = registerCraftingTable("gulf_crafting_table");
    public static final Block GUNDABAD_CRAFTING_TABLE = registerCraftingTable("gundabad_crafting_table");
    public static final Block HALF_TROLL_CRAFTING_TABLE = registerCraftingTable("half_troll_crafting_table");
    public static final Block HIGH_ELVEN_CRAFTING_TABLE = registerCraftingTable("high_elven_crafting_table");
    public static final Block HOBBIT_CRAFTING_TABLE = registerCraftingTable("hobbit_crafting_table");
    public static final Block MOREDAIN_CRAFTING_TABLE = registerCraftingTable("moredain_crafting_table");
    public static final Block MORGUL_CRAFTING_TABLE = registerCraftingTable("morgul_crafting_table");
    public static final Block NEAR_HARAD_CRAFTING_TABLE = registerCraftingTable("near_harad_crafting_table");
    public static final Block RANGER_CRAFTING_TABLE = registerCraftingTable("ranger_crafting_table");
    public static final Block RHUN_CRAFTING_TABLE = registerCraftingTable("rhun_crafting_table");
    public static final Block RIVENDELL_CRAFTING_TABLE = registerCraftingTable("rivendell_crafting_table");
    public static final Block ROHIRRIC_CRAFTING_TABLE = registerCraftingTable("rohirric_crafting_table");
    public static final Block TAUREDAIN_CRAFTING_TABLE = registerCraftingTable("tauredain_crafting_table");
    public static final Block UMBAR_CRAFTING_TABLE = registerCraftingTable("umbar_crafting_table");
    public static final Block URUK_CRAFTING_TABLE = registerCraftingTable("uruk_crafting_table");
    public static final Block WOOD_ELVEN_CRAFTING_TABLE = registerCraftingTable("wood_elven_crafting_table");

    public static final Block DOL_AMROTH_GATE = registerWoodenGate("dol_amroth_gate");
    public static final Block DWARVEN_GATE = registerStoneGate("dwarven_gate");
    public static final Block ELVEN_GATE = registerWoodenGate("elven_gate");
    public static final Block GOLD_GATE = registerMetalGate("gold_gate");
    public static final Block GONDOR_GATE = registerWoodenGate("gondor_gate");
    public static final Block HIGH_ELVEN_GATE = registerWoodenGate("high_elven_gate");
    public static final Block BLUE_HOBBIT_GATE = registerWoodenGate("blue_hobbit_gate");
    public static final Block GREEN_HOBBIT_GATE = registerWoodenGate("green_hobbit_gate");
    public static final Block RED_HOBBIT_GATE = registerWoodenGate("red_hobbit_gate");
    public static final Block YELLOW_HOBBIT_GATE = registerWoodenGate("yellow_hobbit_gate");
    public static final Block MITHRIL_GATE = registerMetalGate("mithril_gate");
    public static final Block NEAR_HARAD_GATE = registerWoodenGate("near_harad_gate");
    public static final Block ORC_GATE = registerMetalGate("orc_gate");
    public static final Block RHUN_GATE = registerWoodenGate("rhun_gate");
    public static final Block ROHAN_GATE = registerWoodenGate("rohan_gate");
    public static final Block SILVER_GATE = registerMetalGate("silver_gate");
    public static final Block TAUREDAIN_GATE = registerWoodenGate("tauredain_gate");
    public static final Block URUK_GATE = registerMetalGate("uruk_gate");
    public static final Block WOOD_ELVEN_GATE = registerWoodenGate("wood_elven_gate");
    public static final Block WOODEN_GATE = registerWoodenGate("wooden_gate");

    // The two dwarven doors. Both are full cubes of plain stone that vanish
    // into a wall until opened, so unlike the gates above they get no
    // .noOcclusion() -- a closed one really is a solid block.
    // LOTRBlockChest. Three of them, differing only in texture and material:
    // the casket and the box are wood at hardness 2.5, the basket is cloth at
    // 0.5. None of them pair into double chests.
    public static final Block LEBETHRON_CASKET = registerChest("lebethron_casket", "lebethron",
            MapColor.WOOD, 2.5f, SoundType.WOOD);
    public static final Block REED_BASKET = registerChest("reed_basket", "basket",
            MapColor.SAND, 0.5f, SoundType.WOOL);
    public static final Block MALLORN_BOX = registerChest("mallorn_box", "mallorn",
            MapColor.WOOD, 2.5f, SoundType.WOOD);
    // The odd one out: stone rather than wood, hardness 3.0.
    public static final Block ANCIENT_HARADRIC_CHEST = registerChest("ancient_haradric_chest",
            "ancient_harad", MapColor.STONE, 3.0f, SoundType.STONE);
    // chestStone: LOTRBlockChest(Material.rock, ...), hardness 3.0, stone step
    // sound -- the same rock build as the Haradric chest, in plain grey.
    public static final Block STONE_CHEST = registerChest("stone_chest", "stone",
            MapColor.STONE, 3.0f, SoundType.STONE);

    // LOTRBlockKebabStand: Material.circuits, hardness 0, resistance 1, wood
    // step sound -- it breaks instantly and blocks nothing. The variant string
    // picks the renderer's texture: "" wooden, "sand" for Harad.
    // LOTRBlockMillstone: Material.rock, hardness 4.0, stone step sound. It has
    // no facing -- the original registers only a side and a top icon.
    /** The LOTR anvil: vanilla's anvil block, opening LOTRContainerAnvil. */
    public static final Block ANVIL = register("anvil", LOTRAnvilBlock::new,
            BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.ANVIL),
            true);

    public static final Block MILLSTONE = register("millstone", LOTRMillstoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0f)
                    .sound(SoundType.STONE),
            true);

    // LOTRBlockBed. Eight beds, vanilla behaviour throughout; the only thing
    // that varies is the upholstery and the planks on the underside, which the
    // original took from bedBottomBlock/bedBottomMetadata. The 1.7.10 ids are
    // in brackets where they differ from the display name.
    public static final Block GALADHRIM_BED = registerBed("galadhrim_bed");   // elvenBed
    public static final Block WOOD_ELVEN_BED = registerBed("wood_elven_bed");
    public static final Block HIGH_ELVEN_BED = registerBed("high_elven_bed");
    public static final Block DWARVEN_BED = registerBed("dwarven_bed");
    public static final Block ORC_BED = registerBed("orc_bed");
    public static final Block LION_FUR_BED = registerBed("lion_fur_bed");     // lionBed
    public static final Block FUR_BED = registerBed("fur_bed");               // wargFurBed
    public static final Block STRAW_BED = registerBed("straw_bed");

    // LOTRBlockOrcChain: Material.circuits, hardness 1.0, metal step sound. It
    // hangs from the ceiling, it is climbable, and chandeliers hang off it.
    public static final Block ORC_CHAIN = register("orc_chain", LOTROrcChainBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(1.0f)
                    .sound(SoundType.METAL) // LOTRBlockOrcChain: soundTypeMetal.
                    // The chain keeps a hair-thin collision box of its own
                    // (getCollisionBoundingBoxFromPool), so this is noOcclusion
                    // rather than noCollision.
                    .noOcclusion(),
            true);

    public static final Block KEBAB_STAND = registerKebabStand("kebab_stand", "");
    public static final Block KEBAB_STAND_SAND = registerKebabStand("kebab_stand_sand", "sand");

    // LOTRBlockEntJar: Material.clay, hardness 1.0, glass step sound. It needs
    // a solid block underneath and drops when that goes.
    public static final Block ENT_JAR = register("ent_jar", LOTREntJarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CLAY)
                    .strength(1.0f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY),
            true);

    // The troll totem, one block per part. LOTRBlockTrollTotem: Material.rock
    // with default hardness -- it never called setHardness, so it keeps Block's
    // 0 hardness and breaks instantly, as in 1.7.10.
    public static final Block TROLL_TOTEM_HEAD = registerTrollTotem("troll_totem_head", LOTRTrollTotemBlock.Part.HEAD);
    public static final Block TROLL_TOTEM_BODY = registerTrollTotem("troll_totem_body", LOTRTrollTotemBlock.Part.BODY);
    public static final Block TROLL_TOTEM_BASE = registerTrollTotem("troll_totem_base", LOTRTrollTotemBlock.Part.BASE);

    // LOTRBlockCommandTable: Material.iron, hardness 2.5, metal step sound.
    // Its plank tabletop overhangs the block on every side, so it must not
    // occlude and its neighbours must keep drawing their faces.
    // LOTRBlockUnsmeltery extends LOTRBlockForgeBase: Material.rock, hardness
    // 4.0, stone sound -- the same numbers registerForge uses. Its cauldron is
    // drawn by a block entity renderer, so the block itself never occludes.
    public static final Block UNSMELTERY = track(CUBES_NO_TIER, register("unsmeltery", LOTRUnsmelteryBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0f) // LOTRBlockForgeBase: setHardness(4) only.
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(net.minecraft.world.level.block.AbstractFurnaceBlock.LIT) ? 13 : 0),
            true));

    public static final Block TABLE_OF_COMMAND = register("table_of_command", LOTRTableOfCommandBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(2.5f)
                    .sound(SoundType.METAL)
                    .noOcclusion(),
            true);

    public static final Block DWARVEN_DOOR = registerDwarvenDoor("dwarven_door", LOTRDwarvenDoorBlock::new);
    public static final Block ITHILDIN_DWARVEN_DOOR =
            registerDwarvenDoor("ithildin_dwarven_door", LOTRIthildinDwarvenDoorBlock::new);

    // LOTRBlockSignCarved, twice: cut into stone or wood by a chisel, or inlaid
    // with ithildin by a moon-chisel. BlockSign with soundTypeStone and hardness
    // 0.5; no collision, no item and no drop -- only a chisel makes one.
    public static final Block CARVED_SIGN = registerCarvedSign("carved_sign");
    public static final Block CARVED_ITHILDIN_SIGN = registerCarvedSign("carved_ithildin_sign");

    public static final Block FLAX_CROP = registerCrop("flax_crop", 4);
    public static final Block LEEK_CROP = registerCrop("leek_crop", 4);
    public static final Block LETTUCE_CROP = registerCrop("lettuce_crop", 4);
    public static final Block PIPEWEED_CROP = registerCrop("pipeweed_crop", 4);
    public static final Block TURNIP_CROP = registerCrop("turnip_crop", 4);
    public static final Block YAM_CROP = registerCrop("yam_crop", 4);

    // LOTRMod: createMetal(false), createMetal(false), createWooden(false).
    // These are gates you open, not decorative bars -- registering them as
    // IronBarsBlock left three of the mod's gates unopenable.
    public static final Block GATE_BRONZE_BARS = registerMetalGate("gate_bronze_bars", false);
    public static final Block GATE_IRON_BARS = registerMetalGate("gate_iron_bars", false);
    public static final Block GATE_WOODEN_CROSS = registerWoodenGate("gate_wooden_cross", false);

    // mechanisedRailOn and mechanisedRailOff. Only the on rail has an item --
    // the mechanism makes it -- and neither was in a creative tab.
    public static final Block MECHANISED_RAIL = registerRail("mechanised_rail", true, true);
    public static final Block MECHANISED_RAIL_OFF = registerRail("mechanised_rail_off", false, false);
    // LOTRBlockUtumnoReturnPortalBase: hardness -1 and resistance Float.MAX_VALUE, quantityDropped 0, not opaque.
    public static final Block UTUMNO_RETURN_PORTAL_BASE = register("utumno_return_portal_base",
            LOTRUtumnoReturnPortalBaseBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(-1.0f, 3600000.0f)
                    .sound(SoundType.STONE)
                    .lightLevel(LOTRUtumnoReturnPortalBaseBlock::lightLevel)
                    .noOcclusion()
                    .noLootTable()
                    .pushReaction(PushReaction.BLOCK),
            true);
    // LOTRBlockUtumnoReturnLight: Material.circuits, hardness left at 0, setLightLevel(1.0f), no collision box, isReplaceable, drops nothing.
    public static final Block UTUMNO_RETURN_LIGHT = register("utumno_return_light",
            LOTRUtumnoReturnLightBlock::new,
            BlockBehaviour.Properties.of()
                    .noCollision()
                    .instabreak()
                    .replaceable()
                    .lightLevel(state -> 15)
                    .noOcclusion()
                    .noLootTable()
                    .pushReaction(PushReaction.DESTROY),
            true);

    public static final Block CHERRY_TRAPDOOR = registerTrapdoor("cherry_trapdoor");
    public static final Block ALMOND_TRAPDOOR = registerTrapdoor("almond_trapdoor");
    public static final Block APPLE_TRAPDOOR = registerTrapdoor("apple_trapdoor");
    public static final Block ASPEN_TRAPDOOR = registerTrapdoor("aspen_trapdoor");
    public static final Block BANANA_TRAPDOOR = registerTrapdoor("banana_trapdoor");
    public static final Block BAOBAB_TRAPDOOR = registerTrapdoor("baobab_trapdoor");
    public static final Block BEECH_TRAPDOOR = registerTrapdoor("beech_trapdoor");
    public static final Block CEDAR_TRAPDOOR = registerTrapdoor("cedar_trapdoor");
    public static final Block CHARRED_TRAPDOOR = registerTrapdoor("charred_trapdoor");
    public static final Block CHESTNUT_TRAPDOOR = registerTrapdoor("chestnut_trapdoor");
    public static final Block CYPRESS_TRAPDOOR = registerTrapdoor("cypress_trapdoor");
    public static final Block DATE_PALM_TRAPDOOR = registerTrapdoor("date_palm_trapdoor");
    public static final Block DRAGON_TRAPDOOR = registerTrapdoor("dragon_trapdoor");
    public static final Block FIR_TRAPDOOR = registerTrapdoor("fir_trapdoor");
    public static final Block GREEN_OAK_TRAPDOOR = registerTrapdoor("green_oak_trapdoor");
    public static final Block HOLLY_TRAPDOOR = registerTrapdoor("holly_trapdoor");
    public static final Block KANUKA_TRAPDOOR = registerTrapdoor("kanuka_trapdoor");
    public static final Block LAIRELOSSE_TRAPDOOR = registerTrapdoor("lairelosse_trapdoor");
    public static final Block LARCH_TRAPDOOR = registerTrapdoor("larch_trapdoor");
    public static final Block LEBETHRON_TRAPDOOR = registerTrapdoor("lebethron_trapdoor");
    public static final Block LEMON_TRAPDOOR = registerTrapdoor("lemon_trapdoor");
    public static final Block LIME_TRAPDOOR = registerTrapdoor("lime_trapdoor");
    public static final Block MAHOGANY_TRAPDOOR = registerTrapdoor("mahogany_trapdoor");
    public static final Block MALLORN_TRAPDOOR = registerTrapdoor("mallorn_trapdoor");
    public static final Block MANGO_TRAPDOOR = registerTrapdoor("mango_trapdoor");
    public static final Block MANGROVE_TRAPDOOR = registerTrapdoor("mangrove_trapdoor");
    public static final Block MAPLE_TRAPDOOR = registerTrapdoor("maple_trapdoor");
    public static final Block MIRK_OAK_TRAPDOOR = registerTrapdoor("mirk_oak_trapdoor");
    public static final Block OLIVE_TRAPDOOR = registerTrapdoor("olive_trapdoor");
    public static final Block ORANGE_TRAPDOOR = registerTrapdoor("orange_trapdoor");
    public static final Block PALM_TRAPDOOR = registerTrapdoor("palm_trapdoor");
    public static final Block PEAR_TRAPDOOR = registerTrapdoor("pear_trapdoor");
    public static final Block PINE_TRAPDOOR = registerTrapdoor("pine_trapdoor");
    public static final Block PLUM_TRAPDOOR = registerTrapdoor("plum_trapdoor");
    public static final Block POMEGRANATE_TRAPDOOR = registerTrapdoor("pomegranate_trapdoor");
    public static final Block REDWOOD_TRAPDOOR = registerTrapdoor("redwood_trapdoor");
    public static final Block ROTTEN_TRAPDOOR = registerTrapdoor("rotten_trapdoor");
    public static final Block SHIRE_PINE_TRAPDOOR = registerTrapdoor("shire_pine_trapdoor");
    public static final Block WILLOW_TRAPDOOR = registerTrapdoor("willow_trapdoor");

    public static final Block CHERRY_DOOR = registerDoor("cherry_door");
    public static final Block ALMOND_DOOR = registerDoor("almond_door");
    public static final Block APPLE_DOOR = registerDoor("apple_door");
    public static final Block ASPEN_DOOR = registerDoor("aspen_door");
    public static final Block BANANA_DOOR = registerDoor("banana_door");
    public static final Block BAOBAB_DOOR = registerDoor("baobab_door");
    public static final Block BEECH_DOOR = registerDoor("beech_door");
    public static final Block CEDAR_DOOR = registerDoor("cedar_door");
    public static final Block CHARRED_DOOR = registerDoor("charred_door");
    public static final Block CHESTNUT_DOOR = registerDoor("chestnut_door");
    public static final Block CYPRESS_DOOR = registerDoor("cypress_door");
    public static final Block DATE_PALM_DOOR = registerDoor("date_palm_door");
    public static final Block DRAGON_DOOR = registerDoor("dragon_door");
    public static final Block FIR_DOOR = registerDoor("fir_door");
    public static final Block GREEN_OAK_DOOR = registerDoor("green_oak_door");
    public static final Block HOLLY_DOOR = registerDoor("holly_door");
    public static final Block KANUKA_DOOR = registerDoor("kanuka_door");
    public static final Block LAIRELOSSE_DOOR = registerDoor("lairelosse_door");
    public static final Block LARCH_DOOR = registerDoor("larch_door");
    public static final Block LEBETHRON_DOOR = registerDoor("lebethron_door");
    public static final Block LEMON_DOOR = registerDoor("lemon_door");
    public static final Block LIME_DOOR = registerDoor("lime_door");
    public static final Block MAHOGANY_DOOR = registerDoor("mahogany_door");
    public static final Block MALLORN_DOOR = registerDoor("mallorn_door");
    public static final Block MANGO_DOOR = registerDoor("mango_door");
    public static final Block MANGROVE_DOOR = registerDoor("mangrove_door");
    public static final Block MAPLE_DOOR = registerDoor("maple_door");
    public static final Block MIRK_OAK_DOOR = registerDoor("mirk_oak_door");
    public static final Block OLIVE_DOOR = registerDoor("olive_door");
    public static final Block ORANGE_DOOR = registerDoor("orange_door");
    public static final Block PALM_DOOR = registerDoor("palm_door");
    public static final Block PEAR_DOOR = registerDoor("pear_door");
    public static final Block PINE_DOOR = registerDoor("pine_door");
    public static final Block PLUM_DOOR = registerDoor("plum_door");
    public static final Block POMEGRANATE_DOOR = registerDoor("pomegranate_door");
    public static final Block REDWOOD_DOOR = registerDoor("redwood_door");
    public static final Block ROTTEN_DOOR = registerDoor("rotten_door");
    public static final Block SHIRE_PINE_DOOR = registerDoor("shire_pine_door");
    public static final Block WILLOW_DOOR = registerDoor("willow_door");

    public static final Block ALMOND_FENCE_GATE = registerFenceGate("almond_fence_gate", ALMOND_PLANKS);
    public static final Block APPLE_FENCE_GATE = registerFenceGate("apple_fence_gate", APPLE_PLANKS);
    public static final Block ASPEN_FENCE_GATE = registerFenceGate("aspen_fence_gate", ASPEN_PLANKS);
    public static final Block BANANA_FENCE_GATE = registerFenceGate("banana_fence_gate", BANANA_PLANKS);
    public static final Block BAOBAB_FENCE_GATE = registerFenceGate("baobab_fence_gate", BAOBAB_PLANKS);
    public static final Block BEECH_FENCE_GATE = registerFenceGate("beech_fence_gate", BEECH_PLANKS);
    public static final Block CEDAR_FENCE_GATE = registerFenceGate("cedar_fence_gate", CEDAR_PLANKS);
    public static final Block CHARRED_FENCE_GATE = registerFenceGate("charred_fence_gate", CHARRED_PLANKS);
    public static final Block CHERRY_FENCE_GATE = registerFenceGate("cherry_fence_gate", CHERRY_PLANKS);
    public static final Block CHESTNUT_FENCE_GATE = registerFenceGate("chestnut_fence_gate", CHESTNUT_PLANKS);
    public static final Block CYPRESS_FENCE_GATE = registerFenceGate("cypress_fence_gate", CYPRESS_PLANKS);
    public static final Block DATE_PALM_FENCE_GATE = registerFenceGate("date_palm_fence_gate", DATE_PALM_PLANKS);
    public static final Block DRAGON_FENCE_GATE = registerFenceGate("dragon_fence_gate", DRAGON_PLANKS);
    public static final Block FIR_FENCE_GATE = registerFenceGate("fir_fence_gate", FIR_PLANKS);
    public static final Block GREEN_OAK_FENCE_GATE = registerFenceGate("green_oak_fence_gate", GREEN_OAK_PLANKS);
    public static final Block HOLLY_FENCE_GATE = registerFenceGate("holly_fence_gate", HOLLY_PLANKS);
    public static final Block KANUKA_FENCE_GATE = registerFenceGate("kanuka_fence_gate", KANUKA_PLANKS);
    public static final Block LAIRELOSSE_FENCE_GATE = registerFenceGate("lairelosse_fence_gate", LAIRELOSSE_PLANKS);
    public static final Block LARCH_FENCE_GATE = registerFenceGate("larch_fence_gate", LARCH_PLANKS);
    public static final Block LEBETHRON_FENCE_GATE = registerFenceGate("lebethron_fence_gate", LEBETHRON_PLANKS);
    public static final Block LEMON_FENCE_GATE = registerFenceGate("lemon_fence_gate", LEMON_PLANKS);
    public static final Block LIME_FENCE_GATE = registerFenceGate("lime_fence_gate", LIME_PLANKS);
    public static final Block MAHOGANY_FENCE_GATE = registerFenceGate("mahogany_fence_gate", MAHOGANY_PLANKS);
    public static final Block MALLORN_FENCE_GATE = registerFenceGate("mallorn_fence_gate", MALLORN_PLANKS);
    public static final Block MANGO_FENCE_GATE = registerFenceGate("mango_fence_gate", MANGO_PLANKS);
    public static final Block MANGROVE_FENCE_GATE = registerFenceGate("mangrove_fence_gate", MANGROVE_PLANKS);
    public static final Block MAPLE_FENCE_GATE = registerFenceGate("maple_fence_gate", MAPLE_PLANKS);
    public static final Block MIRK_OAK_FENCE_GATE = registerFenceGate("mirk_oak_fence_gate", MIRK_OAK_PLANKS);
    public static final Block OLIVE_FENCE_GATE = registerFenceGate("olive_fence_gate", OLIVE_PLANKS);
    public static final Block ORANGE_FENCE_GATE = registerFenceGate("orange_fence_gate", ORANGE_PLANKS);
    public static final Block PALM_FENCE_GATE = registerFenceGate("palm_fence_gate", PALM_PLANKS);
    public static final Block PEAR_FENCE_GATE = registerFenceGate("pear_fence_gate", PEAR_PLANKS);
    public static final Block PINE_FENCE_GATE = registerFenceGate("pine_fence_gate", PINE_PLANKS);
    public static final Block PLUM_FENCE_GATE = registerFenceGate("plum_fence_gate", PLUM_PLANKS);
    public static final Block POMEGRANATE_FENCE_GATE = registerFenceGate("pomegranate_fence_gate", POMEGRANATE_PLANKS);
    public static final Block REDWOOD_FENCE_GATE = registerFenceGate("redwood_fence_gate", REDWOOD_PLANKS);
    public static final Block ROTTEN_FENCE_GATE = registerFenceGate("rotten_fence_gate", ROTTEN_PLANKS);
    public static final Block SHIRE_PINE_FENCE_GATE = registerFenceGate("shire_pine_fence_gate", SHIRE_PINE_PLANKS);
    public static final Block WILLOW_FENCE_GATE = registerFenceGate("willow_fence_gate", WILLOW_PLANKS);

    public static final List<Block> ALL_CARVED_SIGNS = List.of(CARVED_SIGN, CARVED_ITHILDIN_SIGN);

    private LOTRUtilityBlocks() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRBlocks.init. */
    static void init() {
    }
}
