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
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMiscBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks.*;

/**
 * The LOTR blocks of tabFood: the placeable foods and kebab stands.
 * Split out of LOTRBlocks by creative tab; LOTRBlocks keeps the shared
 * builders and family lists and loads this class from its init.
 */
public final class LOTRFoodBlocks {

    public static final Block KEBAB_BLOCK = registerSoftBlock("kebab_block", 0.5f, SoundType.WOOD);

    /** LOTRBlockBarrel: Material.wood, hardness 3, resistance 5. */
    public static final Block BARREL = register("barrel", LOTRBarrelBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(3.0f, 3.0f) // 1.7.10 blast resistance = setResistance x 3 / 5 (or the hardness, if higher).
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .ignitedByLava(),
            true, UnaryOperator.identity(), LOTRBarrelItem::new);

    /** marzipanBlock: 0.4375 wide, 0.375 high, setFoodStats(3, 0.3), and a plain ItemBlock. */
    public static final Block MARCHPANE_BLOCK = registerPlaceableFood("marchpane_block", 0.4375f, 0.375f, 3, 0.3f, false);
    // The LOTRItemPlaceableFood cakes: one to a stack, the default 0.4375 by
    // 0.5 unless noted, and the default setFoodStats(2, 0.1).
    public static final Block APPLE_CRUMBLE = registerPlaceableFood("apple_crumble", 0.4375f, 0.5f, 2, 0.1f, true);
    public static final Block CHERRY_PIE = registerPlaceableFood("cherry_pie", 0.4375f, 0.5f, 2, 0.1f, true);
    public static final Block BANANA_CAKE = registerPlaceableFood("banana_cake", 0.4375f, 0.5f, 2, 0.1f, true);
    /** dalishPastry: LOTRBlockPlaceableFood(0.3125, 0.375). */
    public static final Block DALISH_PASTRY = registerPlaceableFood("dalish_pastry", 0.3125f, 0.375f, 2, 0.1f, true);
    public static final Block BERRY_PIE = registerPlaceableFood("berry_pie", 0.4375f, 0.5f, 2, 0.1f, true);
    public static final Block LEMON_CAKE = registerPlaceableFood("lemon_cake", 0.4375f, 0.5f, 2, 0.1f, true);

    /** bananaBlock and dateBlock: hardness 0, resistance 1, wood sound, no item of their own. */
    public static final Block BANANA_BLOCK = registerHangingFruit("banana_block", 3.0, 15.0, () -> LOTRFoodItems.BANANA);
    public static final Block DATE_BLOCK = registerHangingFruit("date_block", 5.0, 11.0, () -> LOTRFoodItems.DATE);

    public static final Block FINE_PLATE = registerPlate("fine_plate", PLATE_SOUND);
    public static final Block WOODEN_PLATE = registerPlate("wooden_plate", SoundType.WOOD);
    public static final Block STONEWARE_PLATE = registerPlate("stoneware_plate", PLATE_SOUND);

    // LOTRBlockMug and its subclasses: the width and height they passed to
    // LOTRBlockMug(f, f1), and each one's step sound.
    public static final Block MUG_BLOCK = registerMug("mug_block", LOTRVessel.MUG, 3.0f, 8.0f, SoundType.WOOD);
    public static final Block CERAMIC_MUG_BLOCK = registerMug("ceramic_mug_block", LOTRVessel.MUG_CLAY, 3.0f, 8.0f, SoundType.STONE);
    public static final Block GOLDEN_GOBLET_BLOCK = registerMug("golden_goblet_block", LOTRVessel.GOBLET_GOLD, 2.5f, 9.0f, SoundType.METAL);
    public static final Block SILVER_GOBLET_BLOCK = registerMug("silver_goblet_block", LOTRVessel.GOBLET_SILVER, 2.5f, 9.0f, SoundType.METAL);
    public static final Block COPPER_GOBLET_BLOCK = registerMug("copper_goblet_block", LOTRVessel.GOBLET_COPPER, 2.5f, 9.0f, SoundType.METAL);
    public static final Block WOODEN_CUP_BLOCK = registerMug("wooden_cup_block", LOTRVessel.GOBLET_WOOD, 2.5f, 9.0f, SoundType.WOOD);
    public static final Block SKULL_CUP_BLOCK = registerMug("skull_cup_block", LOTRVessel.SKULL, 4.0f, 10.0f, SoundType.STONE);
    public static final Block WINE_GLASS_BLOCK = registerMug("wine_glass_block", LOTRVessel.GLASS, 2.5f, 10.0f, SoundType.GLASS);
    public static final Block BOTTLE_BLOCK = registerMug("bottle_block", LOTRVessel.BOTTLE, 3.0f, 10.0f, SoundType.GLASS);
    public static final Block ALE_HORN_BLOCK = registerMug("ale_horn_block", LOTRVessel.HORN, 5.0f, 12.0f, SoundType.STONE);
    public static final Block GOLDEN_ALE_HORN_BLOCK = registerMug("golden_ale_horn_block", LOTRVessel.HORN_GOLD, 5.0f, 12.0f, SoundType.STONE);
    // LOTRBlockMorgulShroom: a Mordor flower that ticks randomly, to spread by water.
    public static final Block MORGUL_SHROOM = track(ALL_FLOWERS, register("morgul_shroom", LOTRMorgulShroomBlock::new,
            plantProperties().offsetType(BlockBehaviour.OffsetType.XZ).randomTicks(), true));

    private LOTRFoodBlocks() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRBlocks.init. */
    static void init() {
    }
}
