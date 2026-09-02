package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The standing form of a faction banner.
 *
 * <p>Extends vanilla's {@link BannerBlock} on purpose: placement, the sixteen
 * standing rotations, the survives-only-on-a-solid-block rule and the wall
 * variant handoff are all vanilla's and want no reimplementing. Only two things
 * change. The block entity is ours rather than a BannerBlockEntity, because a
 * faction banner has no base colour and no pattern list -- its whole design is
 * one texture, chosen by {@link #type}. And that means it needs its own
 * renderer, since vanilla's draws patterns and nothing else.
 *
 * <p>The DyeColor vanilla insists on is unused by the renderer. It is passed as
 * a rough match for each banner so that anything asking a banner its colour --
 * {@code getColor}, map shading -- gets a sensible answer rather than white.
 */
public class LOTRBannerBlock extends BannerBlock {
    public static final MapCodec<LOTRBannerBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    LOTRBannerType.CODEC.fieldOf("banner_type").forGetter(LOTRBannerBlock::getBannerType),
                    DyeColor.CODEC.fieldOf("color").forGetter(LOTRBannerBlock::getColor),
                    propertiesCodec()
            ).apply(instance, LOTRBannerBlock::new));

    private final LOTRBannerType type;

    public LOTRBannerBlock(LOTRBannerType type, DyeColor color, Properties properties) {
        super(color, properties);
        this.type = type;
    }

    public LOTRBannerType getBannerType() {
        return this.type;
    }

    /**
     * Block.codec() is declared to return MapCodec&lt;BannerBlock&gt; exactly, not a
     * subtype, so a narrower return type will not compile and the cast is the
     * only way to hand back a codec that actually rebuilds one of these. It is
     * sound: every value the codec produces IS a LOTRBannerBlock.
     */
    @SuppressWarnings("unchecked")
    @Override
    public MapCodec<BannerBlock> codec() {
        return (MapCodec<BannerBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRBannerBlockEntity(pos, state);
    }
}
