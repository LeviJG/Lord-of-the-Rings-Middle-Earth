package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTROrcBombEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

/**
 * The dispenser behaviours for the mod's blocks. The projectile ones (pebbles,
 * darts, fire pots, thrown misc items, throwing axes) are ProjectileItems and
 * are registered with registerProjectileBehavior in LOTRMod.
 */
public final class LOTRDispenserBehaviours {

    private LOTRDispenserBehaviours() {
    }

    public static void init() {
        LOTRBlocks.ALL_ORC_BOMBS.forEach(bomb -> DispenserBlock.registerBehavior(bomb.asItem(), ORC_BOMB));
        DispenserBlock.registerBehavior(LOTRCombatBlocks.KHAMULS_FIRE_JAR.asItem(), FIRE_JAR);
    }

    /**
     * LOTRDispenseOrcBomb: a lit bomb in the block in front, burning the usual
     * {@code 40 + strength * 20}. The original also added {@code damage * 10}
     * to EntityTNTPrimed.fuse, but the bomb counts down its own orcBombFuse, so
     * that never changed anything. droppedByPlayer: it breaks terrain, which
     * every bomb in the port does.
     */
    private static final DefaultDispenseItemBehavior ORC_BOMB = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            ServerLevel level = source.level();
            Direction facing = source.state().getValue(DispenserBlock.FACING);
            BlockPos pos = source.pos().relative(facing);
            if (stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                LOTROrcBombEntity bomb = new LOTROrcBombEntity(LOTREntities.ORC_BOMB, level,
                        pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, null,
                        blockItem.getBlock().defaultBlockState());
                level.addFreshEntity(bomb);
                stack.shrink(1);
            }
            return stack;
        }
    };

    /**
     * LOTRDispenseRhunFireJar: set the jar down in front if there is room --
     * without it going off, though a dispenser is powered by definition -- and
     * otherwise spit it out as an item.
     */
    private static final DefaultDispenseItemBehavior FIRE_JAR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior dropItem = new DefaultDispenseItemBehavior();

        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            ServerLevel level = source.level();
            Direction facing = source.state().getValue(DispenserBlock.FACING);
            BlockPos pos = source.pos().relative(facing);
            if (level.getBlockState(pos).canBeReplaced()) {
                LOTRKhamulsFireJarBlock.explodeOnPlace = false;
                try {
                    level.setBlockAndUpdate(pos, LOTRCombatBlocks.KHAMULS_FIRE_JAR.defaultBlockState());
                } finally {
                    LOTRKhamulsFireJarBlock.explodeOnPlace = true;
                }
                stack.shrink(1);
                return stack;
            }
            return dropItem.dispense(source, stack);
        }
    };
}
