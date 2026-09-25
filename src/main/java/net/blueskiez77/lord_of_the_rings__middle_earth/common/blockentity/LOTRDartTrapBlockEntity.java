package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDartItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

// Taurethrim dart trap. LOTRTileEntityDartTrap extended TileEntityDispenser, so
// this extends DispenserBlockEntity: nine slots, the loot-table hooks and the
// vanilla 3x3 dispenser menu all come from vanilla unchanged. LOTRCommonProxy
// returned a plain GuiDispenser for GUI 40, so that menu is the correct one.
//
// On top of that: a proximity trigger. Every tick, if the cooldown has expired
// and a living entity stands in the line of fire, the trap dispenses and waits
// twenty ticks.
//
// AMMO: darts only, as in LOTRTileEntityDartTrap.updateEntity -- it took the
// dispenser's random non-empty slot (func_146017_i) and fired only if that
// stack was a LOTRItemDart. Anything else in the trap just sits there, and a
// tick that happens to pick it fires nothing.
public class LOTRDartTrapBlockEntity extends DispenserBlockEntity {

    /** LOTRTileEntityDartTrap: fireCooldown = 20 after every shot. */
    public static final int FIRE_COOLDOWN = 20;

    /** LOTRTileEntityDartTrap: range 16.0f. */
    public static final double TRIGGER_RANGE = 16.0;

    /** LOTRTileEntityDartTrap: front 0.55f -- where the ray starts. */
    private static final double FRONT_OFFSET = 0.55;

    /** Living, not a creative or spectating player -- LOTRMod.selectLivingExceptCreativePlayers(). */
    private static final Predicate<LivingEntity> TRIGGERS_TRAP = entity -> {
        if (!entity.isAlive()) {
            return false;
        }
        if (entity instanceof Player player) {
            return !player.isCreative() && !player.isSpectator();
        }
        return true;
    };


    private int fireCooldown;

    public LOTRDartTrapBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.DART_TRAP, pos, state);
    }

    // Each trap carries its own name -- Taurethrim, Golden Taurethrim, Obsidian
    // Taurethrim -- so the title comes off the block, not from one shared
    // container.* key. Those three block.lotr.*_dart_trap keys already exist.
    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    // ---------------------------------------------------------------- ticking

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, LOTRDartTrapBlockEntity trap) {
        if (trap.fireCooldown > 0) {
            --trap.fireCooldown;
            return;
        }

        int slot = trap.pickAmmoSlot(level.getRandom());
        if (slot < 0 || !trap.somethingInRange(level, pos, state)) {
            return;
        }

        ItemStack ammo = trap.getItem(slot);

        // Every dispense behaviour reads DispenserBlock.FACING, which is
        // DirectionalBlock.FACING (six-way). This block's state carries
        // HorizontalDirectionalBlock.FACING -- a DIFFERENT property object --
        // so handing the behaviour this block's own state makes
        // getDispensePosition throw on the property lookup. Give it a dispenser
        // state pointed the same way instead. BlockSource takes the position
        // from pos, so the synthetic state is only read for its facing.
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        BlockState dispenserState = Blocks.DISPENSER.defaultBlockState()
                .setValue(DispenserBlock.FACING, facing);

        BlockSource source = new BlockSource(level, pos, dispenserState, trap);
        DispenseItemBehavior behavior = DispenserBlock.DISPENSER_REGISTRY.get(ammo.getItem());
        if (behavior == null) {
            return;
        }


        try {
            trap.setItem(slot, behavior.dispense(source, ammo));
        } catch (RuntimeException e) {
            LOTRMod.LOGGER.error("[darttrap {}] dispense threw", pos.toShortString(), e);
            return;
        }
        // No sound here: the dart's projectile dispense behaviour plays the
        // launch sound itself, as BehaviorProjectileDispense did.
        trap.fireCooldown = FIRE_COOLDOWN;
    }

    // func_146017_i is DispenserBlockEntity.getRandomSlot: any non-empty slot, uniformly. The dart check comes after the pick, not before, exactly as in the original.
    private int pickAmmoSlot(RandomSource random) {
        int slot = getRandomSlot(random);
        if (slot < 0 || !(getItem(slot).getItem() instanceof LOTRDartItem)) {
            return -1;
        }
        return slot;
    }

    /**
     * LOTRTileEntityDartTrap.getTriggerRange, ported properly.
     *
     * The original raycasts from 0.55 in front of the block centre out to 16
     * blocks, with func_147447_a(vec1, vec2, true, true, false): stop on
     * liquids, ignore blocks with no bounding box, do not return the last
     * uncollided block. On a hit it pulls the target back to the centre of the
     * block BEFORE the one it struck. The trigger box is then this block's full
     * cube stretched out to that target.
     */
    private AABB getTriggerRange(ServerLevel level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);

        Vec3 centre = Vec3.atCenterOf(pos);
        Vec3 front = centre.add(facing.getStepX() * FRONT_OFFSET,
                facing.getStepY() * FRONT_OFFSET, facing.getStepZ() * FRONT_OFFSET);
        Vec3 target = centre.add(facing.getStepX() * TRIGGER_RANGE,
                facing.getStepY() * TRIGGER_RANGE, facing.getStepZ() * TRIGGER_RANGE);

        // Fluid.ANY matches the original's stopOnLiquid = true; Block.COLLIDER
        // matches ignoreBlockWithoutBoundingBox = true.
        BlockHitResult hit = level.clip(new ClipContext(front, target,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty()));

        if (hit.getType() != HitResult.Type.MISS) {
            BlockPos hitPos = hit.getBlockPos();
            target = new Vec3(
                    hitPos.getX() + 0.5 - facing.getStepX(),
                    hitPos.getY() + 0.5 - facing.getStepY(),
                    hitPos.getZ() + 0.5 - facing.getStepZ());
        }

        return new AABB(pos).expandTowards(
                target.x - centre.x, target.y - centre.y, target.z - centre.z);
    }

    private boolean somethingInRange(ServerLevel level, BlockPos pos, BlockState state) {
        AABB range = getTriggerRange(level, pos, state);
        return !level.getEntitiesOfClass(LivingEntity.class, range, TRIGGERS_TRAP).isEmpty();
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        fireCooldown = input.getShortOr("FireCooldown", (short) 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("FireCooldown", (short) fireCooldown);
    }
}