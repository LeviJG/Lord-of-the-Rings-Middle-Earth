package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.BiConsumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBannerItem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jspecify.annotations.Nullable;

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
 *
 * <p>Territory protection (LOTREntityBanner) is on the block entity. Here:
 * the placer becomes the owner (unless a creative player sneaking keeps the
 * one the item carried); right-clicked by someone who may edit it, a
 * protecting banner opens its screen; and a protecting banner shrugs off
 * explosions, as the original entity ignored all harm but a player's.
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

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                          BlockEntityType<T> type) {
        return level.isClientSide() ? null
                : createTickerHelper(type, LOTRBlockEntities.BANNER, LOTRBannerBlockEntity::serverTick);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && placer instanceof Player player
                && level.getBlockEntity(pos) instanceof LOTRBannerBlockEntity banner
                && (banner.getPlacingPlayer() == null || !LOTRBannerItem.shouldKeepOriginalOwnerOnPlacement(player))) {
            banner.setPlacingPlayer(player);
        }
    }

    /** interactFirst: the owner's screen, for someone who may edit a protecting banner. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof LOTRBannerBlockEntity banner
                && banner.isProtectingTerritory() && banner.canPlayerEditBanner(serverPlayer)) {
            banner.sendBannerData(serverPlayer, true, true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion,
                                  BiConsumer<ItemStack, BlockPos> onHit) {
        if (level.getBlockEntity(pos) instanceof LOTRBannerBlockEntity banner && banner.isProtectingTerritory()) {
            return;
        }
        super.onExplosionHit(state, level, pos, explosion, onHit);
    }
}
