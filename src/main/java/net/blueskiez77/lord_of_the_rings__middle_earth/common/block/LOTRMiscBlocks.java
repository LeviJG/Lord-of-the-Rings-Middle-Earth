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
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks.*;

/**
 * The LOTR blocks of tabMisc: buttons, pressure plates and the other small odds and ends.
 * Split out of LOTRBlocks by creative tab; LOTRBlocks keeps the shared
 * builders and family lists and loads this class from its init.
 */
public final class LOTRMiscBlocks {

    public static final Block MORDOR_ROCK_BUTTON = registerButton("mordor_rock_button", MORDOR_ROCK);
    public static final Block MORDOR_ROCK_PRESSURE_PLATE = registerPressurePlate("mordor_rock_pressure_plate", MORDOR_ROCK);
    public static final Block GONDOR_ROCK_BUTTON = registerButton("gondor_rock_button", GONDOR_ROCK);
    public static final Block GONDOR_ROCK_PRESSURE_PLATE = registerPressurePlate("gondor_rock_pressure_plate", GONDOR_ROCK);
    public static final Block ROHAN_ROCK_BUTTON = registerButton("rohan_rock_button", ROHAN_ROCK);
    public static final Block ROHAN_ROCK_PRESSURE_PLATE = registerPressurePlate("rohan_rock_pressure_plate", ROHAN_ROCK);
    public static final Block BLUE_ROCK_BUTTON = registerButton("blue_rock_button", BLUE_ROCK);
    public static final Block BLUE_ROCK_PRESSURE_PLATE = registerPressurePlate("blue_rock_pressure_plate", BLUE_ROCK);
    public static final Block RED_ROCK_BUTTON = registerButton("red_rock_button", RED_ROCK);
    public static final Block RED_ROCK_PRESSURE_PLATE = registerPressurePlate("red_rock_pressure_plate", RED_ROCK);
    public static final Block CHALK_BUTTON = registerButton("chalk_button", CHALK);
    public static final Block CHALK_PRESSURE_PLATE = registerPressurePlate("chalk_pressure_plate", CHALK);

    private LOTRMiscBlocks() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRBlocks.init. */
    static void init() {
    }
}
