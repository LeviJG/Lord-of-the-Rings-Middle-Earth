package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRTrollTotemBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockTrollTotem. A three-block stone idol -- base, body, head -- that
 * mountain trolls raise in the hills, and that will summon a Mountain Troll
 * Chieftain if you feed the head a bone at night.
 *
 * <p>In 1.7.10 this was ONE block holding all three parts in metadata: the low
 * two bits chose base/body/head and the next two held the rotation. The port
 * splits the parts into three blocks, which is how every other metadata family
 * here has been handled, and moves the rotation onto a normal FACING property.
 * The parts still behave as one object: placing any of them re-aligns the whole
 * column to the direction you were facing, exactly as onBlockPlacedBy did.
 *
 * <p>The block itself draws nothing -- getRenderType returned a custom id and
 * renderAsNormalBlock was false. All three parts are drawn by
 * LOTRTrollTotemRenderer out of the one entity model, which is why they share a
 * block entity even though only the head has anything to think about.
 */
public class LOTRTrollTotemBlock extends Block implements EntityBlock {

    public static final MapCodec<LOTRTrollTotemBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Part.CODEC.fieldOf("part").forGetter(LOTRTrollTotemBlock::part),
                    propertiesCodec()
            ).apply(instance, LOTRTrollTotemBlock::new));

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final Part part;

    public LOTRTrollTotemBlock(Part part, Properties properties) {
        super(properties);
        this.part = part;
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    public Part part() {
        return part;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRTrollTotemBlockEntity(pos, state);
    }

    /**
     * Only the head has anything to tick: its jaw yawns open when the totem is
     * ready to summon, and that is a client-side animation.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (part != Part.HEAD || type != LOTRBlockEntities.TROLL_TOTEM) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRTrollTotemBlockEntity>)
                LOTRTrollTotemBlockEntity::tick;
    }

    /** The model lives entirely in the block entity renderer. */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /**
     * The totem does not block sky light. In 1.7.10 that came for free:
     * Block's constructor set {@code lightOpacity = isOpaqueCube() ? 255 : 0}
     * and LOTRBlockTrollTotem overrode isOpaqueCube() to false, so the idol was
     * invisible to the light engine. The default here would instead derive it
     * from the collision shape, which is a full cube, and a totem standing in
     * the open would cast a solid shadow column and darken its own head.
     */
    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    // ------------------------------------------------------------ interaction

    /**
     * LOTRBlockTrollTotem.onBlockActivated. Feeding the HEAD a bone summons a
     * Mountain Troll Chieftain, but only when all of this holds: the totem is
     * complete and can see the night sky (canSummon), and the player is an
     * ENEMY of Angmar -- alignment strictly below zero. Trolls do not answer
     * their own kind's friends.
     *
     * <p>The original tested the ore dictionary name "bone", which covers the
     * vanilla bone and any modded equivalent. The port has no ore dictionary,
     * so this uses the vanilla item plus the {@code c:bones} convention tag.
     *
     * <p>PARTIAL: the summon itself cannot spawn anything yet -- there are no
     * entities in the port. The totem is consumed and nothing rises. See
     * docs/TODO-troll-totem.md.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (part != Part.HEAD || !isBone(stack)) {
            return InteractionResult.PASS;
        }
        if (!(level.getBlockEntity(pos) instanceof LOTRTrollTotemBlockEntity totem) || !totem.canSummon()) {
            return InteractionResult.PASS;
        }
        if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.ANGMAR) >= 0.0f) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            totem.summon();
        }
        return InteractionResult.SUCCESS;
    }

    /** LOTRMod.isOreNameEqual(itemstack, "bone"). */
    private static boolean isBone(ItemStack stack) {
        return stack.is(Items.BONE) || stack.is(BONES_TAG);
    }

    private static final TagKey<Item> BONES_TAG = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath("c", "bones"));

    // ------------------------------------------------------------- placement

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    /**
     * LOTRBlockTrollTotem.onBlockPlacedBy. Whichever part you place takes the
     * direction you were facing, and then drags the rest of the column round to
     * match -- so a totem always ends up consistently oriented no matter which
     * order its three blocks went down in, and no matter which way you were
     * standing for the earlier ones.
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide()) {
            return;
        }
        Direction facing = state.getValue(FACING);

        switch (part) {
            case HEAD -> {
                if (align(level, pos.below(), Part.BODY, facing)) {
                    align(level, pos.below(2), Part.BASE, facing);
                }
            }
            case BODY -> {
                align(level, pos.below(), Part.BASE, facing);
                align(level, pos.above(), Part.HEAD, facing);
            }
            case BASE -> {
                if (align(level, pos.above(), Part.BODY, facing)) {
                    align(level, pos.above(2), Part.HEAD, facing);
                }
            }
        }
    }

    /** Turns the totem part at this position, if that is what is there. */
    private static boolean align(Level level, BlockPos pos, Part expected, Direction facing) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LOTRTrollTotemBlock other && other.part() == expected) {
            level.setBlock(pos, state.setValue(FACING, facing), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    /** Which of the three stacked blocks this is. Metadata bits 0-1 in 1.7.10. */
    public enum Part implements StringRepresentable {
        HEAD("head"),
        BODY("body"),
        BASE("base");

        public static final StringRepresentable.EnumCodec<Part> CODEC =
                StringRepresentable.fromEnum(Part::values);

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
