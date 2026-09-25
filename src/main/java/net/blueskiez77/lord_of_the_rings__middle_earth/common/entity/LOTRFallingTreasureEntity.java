package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTreasurePileBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityFallingTreasure: a treasure pile falling like sand, which on landing
 * pours itself into the pile it lands in -- up to a full block -- and sets
 * whatever is left on top. What cannot be set down is dropped, a coin item a
 * layer. Vanilla's falling block would drop a single item and lose the rest.
 */
public class LOTRFallingTreasureEntity extends FallingBlockEntity {

    private int ticksFalling;

    public LOTRFallingTreasureEntity(EntityType<? extends LOTRFallingTreasureEntity> type, Level level) {
        super(type, level);
    }

    /** LOTRBlockTreasurePile.tryFall: the pile leaves its place and falls. */
    public static void fall(ServerLevel level, BlockPos pos, BlockState state) {
        LOTRFallingTreasureEntity falling = new LOTRFallingTreasureEntity(LOTREntities.FALLING_TREASURE, level);
        falling.blockState = state;
        falling.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        falling.setDeltaMovement(Vec3.ZERO);
        falling.xo = falling.getX();
        falling.yo = falling.getY();
        falling.zo = falling.getZ();
        falling.setStartPos(falling.blockPosition());
        level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(falling);
    }

    @Override
    public void tick() {
        BlockState state = getBlockState();
        if (state.isAir() || !state.hasProperty(LOTRTreasurePileBlock.LAYERS)) {
            discard();
            return;
        }
        this.xo = getX();
        this.yo = getY();
        this.zo = getZ();
        ++this.ticksFalling;
        applyGravity();
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(0.98));
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        BlockPos pos = blockPosition();
        if (onGround()) {
            setDeltaMovement(getDeltaMovement().multiply(0.7, -0.5, 0.7));
            BlockState here = level.getBlockState(pos);
            if (here.is(Blocks.MOVING_PISTON)) {
                return;
            }
            discard();
            int layers = state.getValue(LOTRTreasurePileBlock.LAYERS);
            boolean placed = false;
            if (here.is(state.getBlock()) && here.getValue(LOTRTreasurePileBlock.LAYERS) < LOTRTreasurePileBlock.MAX_LAYERS) {
                int held = here.getValue(LOTRTreasurePileBlock.LAYERS);
                int poured = Math.min(layers, LOTRTreasurePileBlock.MAX_LAYERS - held);
                level.setBlock(pos, here.setValue(LOTRTreasurePileBlock.LAYERS, held + poured), 3);
                layers -= poured;
                placed = true;
                pos = pos.above();
            }
            if (layers > 0) {
                if (level.getBlockState(pos).canBeReplaced()) {
                    level.setBlock(pos, state.setValue(LOTRTreasurePileBlock.LAYERS, layers), 3);
                    placed = true;
                } else {
                    drop(level, state, layers);
                }
            }
            if (placed) {
                SoundType sound = state.getSoundType();
                level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                        (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
            }
        } else if (this.ticksFalling > 100
                && (pos.getY() < level.getMinY() || pos.getY() > level.getMaxY() || this.ticksFalling > 600)) {
            drop(level, state, state.getValue(LOTRTreasurePileBlock.LAYERS));
            discard();
        }
    }

    private void drop(ServerLevel level, BlockState state, int layers) {
        spawnAtLocation(level, new ItemStack(state.getBlock().asItem(), layers));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("TicksFalling", this.ticksFalling);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.ticksFalling = input.getIntOr("TicksFalling", 0);
    }
}
