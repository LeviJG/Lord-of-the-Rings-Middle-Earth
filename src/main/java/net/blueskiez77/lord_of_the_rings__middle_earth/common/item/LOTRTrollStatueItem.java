package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRStoneTrollEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * LOTRItemTrollStatue: places a {@link LOTRStoneTrollEntity}.
 *
 * <p>The original was one item with three damage values for the outfit and a
 * {@code TwoHeads} boolean in NBT, giving six creative entries. Damage values
 * are gone, so both now live in the stack's {@code minecraft:custom_data} under
 * the same two names -- the closest thing 26.2 has to the original's NBT, and
 * enough to keep a two-headed statue from stacking with a one-headed one (the
 * stack size is 1 in any case, as it was).
 */
public class LOTRTrollStatueItem extends Item {

    /** Matches the original's item damage: three outfits, 0 through 2. */
    public static final int OUTFIT_COUNT = 3;

    private static final String TAG_OUTFIT = "TrollOutfit";
    private static final String TAG_TWO_HEADS = "TwoHeads";

    public LOTRTrollStatueItem(Properties properties) {
        super(properties);
    }

    public static int getOutfit(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return 0;
        }
        int outfit = data.copyTag().getByteOr(TAG_OUTFIT, (byte) 0);
        return outfit < 0 || outfit >= OUTFIT_COUNT ? 0 : outfit;
    }

    public static boolean hasTwoHeads(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBooleanOr(TAG_TWO_HEADS, false);
    }

    public static void setOutfit(ItemStack stack, int outfit) {
        edit(stack, tag -> tag.putByte(TAG_OUTFIT, (byte) outfit));
    }

    /**
     * The flag, and with it the tooltip line the original's addInformation
     * added for two-headed statues.
     *
     * <p>That line used to be an appendHoverText override. Item.appendHoverText
     * is deprecated in 26.2 and no vanilla item overrides it any more -- per-stack
     * tooltip text comes off components now -- so the line is written into LORE
     * here instead, at the one place a statue is ever marked two-headed.
     */
    public static void setTwoHeads(ItemStack stack, boolean twoHeads) {
        edit(stack, tag -> tag.putBoolean(TAG_TWO_HEADS, twoHeads));
        if (twoHeads) {
            stack.set(DataComponents.LORE, new ItemLore(
                    List.of(Component.translatable("item.lotr.troll_statue.two_heads"))));
        } else {
            stack.remove(DataComponents.LORE);
        }
    }

    private static void edit(ItemStack stack, Consumer<CompoundTag> edit) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA, data.update(edit));
    }

    /** One stack per outfit, each in one-headed and two-headed form: getSubItems. */
    public static ItemStack stack(int outfit, boolean twoHeads) {
        ItemStack stack = new ItemStack(LOTRItems.TROLL_STATUE);
        setOutfit(stack, outfit);
        if (twoHeads) {
            setTwoHeads(stack, true);
        }
        return stack;
    }

    /**
     * getUnlocalizedName(ItemStack) appended the damage value, so the three
     * outfits read as "Troll Statue #1" through "#3" rather than sharing a name.
     */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId() + "." + getOutfit(stack));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        Direction face = context.getClickedFace();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        // onItemUse: a snow layer is stood on rather than replaced, anything
        // else that is not replaceable pushes the placement one block along the
        // face that was hit.
        BlockState clickedState = level.getBlockState(clicked);
        BlockPos pos = clicked;
        if (clickedState.is(Blocks.SNOW)) {
            face = Direction.UP;
        } else if (!clickedState.canBeReplaced()) {
            pos = clicked.relative(face);
        }

        if (player == null || !player.mayUseItemAt(pos, face, stack)) {
            return InteractionResult.FAIL;
        }

        // It needs something solid under it to stand on.
        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        LOTRStoneTrollEntity statue = new LOTRStoneTrollEntity(LOTREntities.STONE_TROLL, level);
        // The click's position within the block, so a statue lands where it was
        // aimed; facing the player, as 180 - rotationYaw did.
        statue.snapTo(pos.getX() + context.getClickLocation().x - clicked.getX(),
                pos.getY(),
                pos.getZ() + context.getClickLocation().z - clicked.getZ(),
                180.0f - player.getYRot() % 360.0f, 0.0f);

        if (!isClearFor(level, statue)) {
            statue.discard();
            return InteractionResult.FAIL;
        }

        statue.setTrollOutfit(getOutfit(stack));
        statue.setHasTwoHeads(hasTwoHeads(stack));
        statue.setPlacedByPlayer(true);
        level.addFreshEntity(statue);
        level.playSound(null, statue.getX(), statue.getY(), statue.getZ(),
                Blocks.STONE.defaultBlockState().getSoundType().getBreakSound(),
                SoundSource.BLOCKS,
                (Blocks.STONE.defaultBlockState().getSoundType().getVolume() + 1.0f) / 2.0f,
                Blocks.STONE.defaultBlockState().getSoundType().getPitch() * 0.8f);
        stack.shrink(1);
        return InteractionResult.SUCCESS;
    }

    /**
     * checkNoEntityCollision, getCollidingBoundingBoxes and isAnyLiquid: room
     * to stand, and not in water or lava.
     *
     * <p>noCollision covers the first two on its own -- blocks in the way and
     * entities that are actually solid. Testing every entity in the box instead
     * would be stricter than the original was: 1.7.10's checkNoEntityCollision
     * only cared about entities with preventEntitySpawning set, which a player
     * or a dropped item does not have, so standing where you are placing was
     * always allowed.
     */
    private static boolean isClearFor(Level level, LOTRStoneTrollEntity statue) {
        AABB box = statue.getBoundingBox();
        return level.noCollision(statue, box) && !level.containsAnyLiquid(box);
    }
}
