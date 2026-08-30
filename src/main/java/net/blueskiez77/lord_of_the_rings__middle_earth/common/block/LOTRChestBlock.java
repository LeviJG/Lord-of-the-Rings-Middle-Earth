package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRChestBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import org.jspecify.annotations.Nullable;

/**
 * A vanilla chest with its own texture: the Lebethron Casket, Reed Basket,
 * Mallorn Box and Ancient Haradric Chest.
 *
 * <p>Everything a chest does comes from {@link ChestBlock}. The only additions
 * are {@link #variant()}, naming the sheet the renderer binds, and pinning
 * {@link ChestType} to SINGLE -- LOTRBlockChest had no pairing logic, so two
 * caskets side by side stay two containers.
 */
public class LOTRChestBlock extends ChestBlock {

    public static final MapCodec<LOTRChestBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("variant").forGetter(LOTRChestBlock::variant),
                    propertiesCodec()
            ).apply(instance, LOTRChestBlock::new));

    private static final Supplier<BlockEntityType<? extends ChestBlockEntity>> TYPE =
            () -> LOTRBlockEntities.CHEST;

    private final String variant;

    public LOTRChestBlock(String variant, Properties properties) {
        super(TYPE, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
        this.variant = variant;
    }

    /** The sheet under textures/entity/chest/ that this chest wears. */
    public String variant() {
        return variant;
    }

    @Override
    public MapCodec<? extends ChestBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRChestBlockEntity(pos, state);
    }

    @Override
    protected ChestType getChestType(Level level, BlockPos pos, Direction facing) {
        return ChestType.SINGLE;
    }

    @Override
    public boolean chestCanConnectTo(BlockState state) {
        return false;
    }
}
