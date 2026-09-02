package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The wall-hung form of a faction banner: the same deal as
 * {@link LOTRBannerBlock}, on vanilla's {@link WallBannerBlock}.
 *
 * <p>It has no item of its own. A StandingAndWallBlockItem decides between the
 * two forms from the face that was clicked, exactly as vanilla's banners do.
 */
public class LOTRWallBannerBlock extends WallBannerBlock {
    public static final MapCodec<LOTRWallBannerBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    LOTRBannerType.CODEC.fieldOf("banner_type").forGetter(LOTRWallBannerBlock::getBannerType),
                    DyeColor.CODEC.fieldOf("color").forGetter(LOTRWallBannerBlock::getColor),
                    propertiesCodec()
            ).apply(instance, LOTRWallBannerBlock::new));

    private final LOTRBannerType type;

    public LOTRWallBannerBlock(LOTRBannerType type, DyeColor color, Properties properties) {
        super(color, properties);
        this.type = type;
    }

    public LOTRBannerType getBannerType() {
        return this.type;
    }

    /**
     * Block.codec() is declared to return MapCodec&lt;WallBannerBlock&gt; exactly, not a
     * subtype, so a narrower return type will not compile and the cast is the
     * only way to hand back a codec that actually rebuilds one of these. It is
     * sound: every value the codec produces IS a LOTRWallBannerBlock.
     */
    @SuppressWarnings("unchecked")
    @Override
    public MapCodec<WallBannerBlock> codec() {
        return (MapCodec<WallBannerBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRBannerBlockEntity(pos, state);
    }
}
