package net.blueskiez77.lord_of_the_rings__middle_earth.common.banner;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBarrelBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCraftingTableBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTREntJarBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRKebabStandBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPlaceableFoodBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPlateBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTableOfCommandBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRWeaponRackBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBannerProtectable;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREventHandler's banner protection, on the server: in protected land, a
 * player the banner turns away may not break blocks (onBlockBreak), use them
 * -- each kind asking its own permission: doors, tables, containers, personal
 * containers, food, beds, switches, and anything else (or anything at all
 * while sneaking with an item) the full permission (onBlockInteract) -- put
 * out fire (the left click), attack or use hanging things and the protectable
 * entities (onEntityAttackedByPlayer, onEntityInteract), or fill or empty a
 * bucket there (onFillBucket).
 *
 * <p>A standing banner is judged as LOTREntityBanner.attackEntityFrom judged
 * a blow: a banner in someone's land stands unless it protects that land
 * itself with self-protection off, and a self-protecting one falls only to
 * whoever may edit it.
 */
public final class LOTRBannerEvents {

    private LOTRBannerEvents() {
    }

    public static void init() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            if (blockEntity instanceof LOTRBannerBlockEntity banner && state.getBlock() instanceof LOTRBannerBlock) {
                return canBreakBanner(level, player, pos, banner);
            }
            return !LOTRBannerProtection.isProtected(level, pos,
                    LOTRBannerProtection.forPlayer(player, LOTRBannerProtection.Permission.FULL), true);
        });

        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }
            BlockPos pos = hit.getBlockPos();
            BlockState state = level.getBlockState(pos);
            LOTRBannerProtection.Permission perm = permissionFor(player, level, pos, state);
            if (LOTRBannerProtection.isProtected(level, pos, LOTRBannerProtection.forPlayer(player, perm), true)) {
                if (state.getBlock() instanceof DoorBlock && level instanceof ServerLevel serverLevel) {
                    serverLevel.sendBlockUpdated(pos.below(), level.getBlockState(pos.below()), level.getBlockState(pos.below()), 3);
                    serverLevel.sendBlockUpdated(pos.above(), level.getBlockState(pos.above()), level.getBlockState(pos.above()), 3);
                }
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });

        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }
            BlockPos firePos = pos.relative(direction);
            if (level.getBlockState(firePos).getBlock() instanceof BaseFireBlock
                    && LOTRBannerProtection.isProtected(level, firePos,
                    LOTRBannerProtection.forPlayer(player, LOTRBannerProtection.Permission.FULL), true)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, level, hand, entity, hit) ->
                protectedEntity(player, level, entity) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) ->
                protectedEntity(player, level, entity) ? InteractionResult.FAIL : InteractionResult.PASS);

        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (level.isClientSide() || !(stack.getItem() instanceof BucketItem)) {
                return InteractionResult.PASS;
            }
            BlockHitResult hit = bucketTarget(level, player, stack.is(Items.BUCKET));
            if (hit.getType() == HitResult.Type.BLOCK && LOTRBannerProtection.isProtected(level, hit.getBlockPos(),
                    LOTRBannerProtection.forPlayer(player, LOTRBannerProtection.Permission.FULL), true)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
    }

    private static boolean canBreakBanner(Level level, Player player, BlockPos pos, LOTRBannerBlockEntity banner) {
        boolean isProtectionBanner = banner.isProtectingTerritory();
        if (LOTRBannerProtection.isProtected(level, pos,
                LOTRBannerProtection.forPlayer(player, LOTRBannerProtection.Permission.FULL), true)
                && (!isProtectionBanner || banner.selfProtectionSetting())) {
            return false;
        }
        return !(isProtectionBanner && banner.selfProtectionSetting() && !banner.canPlayerEditBanner(player));
    }

    /** onBlockInteract's choice of permission for right-clicking this block. */
    private static LOTRBannerProtection.Permission permissionFor(Player player, Level level, BlockPos pos, BlockState state) {
        boolean mightBeAbleToAlterWorld = player.isShiftKeyDown() && !player.getMainHandItem().isEmpty();
        if (mightBeAbleToAlterWorld) {
            return LOTRBannerProtection.Permission.FULL;
        }
        Block block = state.getBlock();
        if (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock
                || block instanceof LOTRGateBlock) {
            return LOTRBannerProtection.Permission.DOORS;
        }
        if (block instanceof CraftingTableBlock || block instanceof LOTRCraftingTableBlock || block instanceof AnvilBlock
                || block instanceof LOTRTableOfCommandBlock) {
            return LOTRBannerProtection.Permission.TABLES;
        }
        if (level.getBlockEntity(pos) instanceof Container) {
            return block instanceof LOTRBarrelBlock || block instanceof LOTRKebabStandBlock
                    ? LOTRBannerProtection.Permission.FOOD : LOTRBannerProtection.Permission.CONTAINERS;
        }
        if (block instanceof LOTRWeaponRackBlock || block == Blocks.BOOKSHELF || block instanceof JukeboxBlock) {
            return LOTRBannerProtection.Permission.CONTAINERS;
        }
        if (block instanceof EnderChestBlock) {
            return LOTRBannerProtection.Permission.PERSONAL_CONTAINERS;
        }
        if (block instanceof LOTRPlateBlock || block instanceof CakeBlock || block instanceof LOTRPlaceableFoodBlock
                || block instanceof LOTRMugBlock || block instanceof LOTREntJarBlock) {
            return LOTRBannerProtection.Permission.FOOD;
        }
        if (block instanceof BedBlock) {
            return LOTRBannerProtection.Permission.BEDS;
        }
        if (block instanceof ButtonBlock || block instanceof LeverBlock) {
            return LOTRBannerProtection.Permission.SWITCHES;
        }
        return LOTRBannerProtection.Permission.FULL;
    }

    private static boolean protectedEntity(Player player, Level level, Entity entity) {
        return !level.isClientSide() && (entity instanceof HangingEntity || entity instanceof LOTRBannerProtectable)
                && LOTRBannerProtection.isProtected(level, entity,
                LOTRBannerProtection.forPlayer(player, LOTRBannerProtection.Permission.FULL), true);
    }

    /** The block a bucket in this player's hand would act on, as BucketItem finds it: an empty one sees water. */
    private static BlockHitResult bucketTarget(Level level, Player player, boolean empty) {
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.calculateViewVector(player.getXRot(), player.getYRot())
                .scale(player.blockInteractionRange()));
        return level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE,
                empty ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE, player));
    }
}
