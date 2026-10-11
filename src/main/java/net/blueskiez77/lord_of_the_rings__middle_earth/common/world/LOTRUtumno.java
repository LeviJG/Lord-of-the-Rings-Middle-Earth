package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtumnoReturnPortalBaseBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Utumno's rules, from LOTRWorldProviderUtumno, LOTREventHandler and LOTRTickHandlerServer: water
 * freezes on its ice level and boils away on its fire level, as does ice set down there, and a bucket
 * of water emptied there; its brick will not be broken but by the Pickaxe of the Underworld, and that
 * not between the levels; its ender chests may be shut by the config; and each of its servants a
 * player kills by one of its altars feeds the altar, until, at fifteen, it becomes the way out.
 */
public final class LOTRUtumno {

    /** LOTRPacketUtumnoReturn: the player is in the return portal's light, held to its middle. */
    public record ReturnPayload(double posX, double posZ) implements CustomPacketPayload {
        public static final Type<ReturnPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "utumno_return"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ReturnPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.DOUBLE, ReturnPayload::posX, ByteBufCodecs.DOUBLE, ReturnPayload::posZ, ReturnPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketUtumnoKill: a servant's death feeding an altar, drawn as flames running into it. */
    public record KillPayload(int entityID, BlockPos pos) implements CustomPacketPayload {
        public static final Type<KillPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "utumno_kill"));
        public static final StreamCodec<RegistryFriendlyByteBuf, KillPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, KillPayload::entityID, BlockPos.STREAM_CODEC, KillPayload::pos, KillPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketBlockFX (UTUMNO_EVAPORATE): smoke where water has boiled away. */
    public record EvaporatePayload(BlockPos pos) implements CustomPacketPayload {
        public static final Type<EvaporatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "utumno_evaporate"));
        public static final StreamCodec<RegistryFriendlyByteBuf, EvaporatePayload> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, EvaporatePayload::pos, EvaporatePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** The client's side of setInUtumnoReturnPortal; set by the client. */
    public static Consumer<Player> clientInReturnPortal = player -> {
    };

    private LOTRUtumno() {
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(ReturnPayload.TYPE, ReturnPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(KillPayload.TYPE, KillPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(EvaporatePayload.TYPE, EvaporatePayload.STREAM_CODEC);
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> onServantKilled(entity, source.getEntity()));
        ServerTickEvents.END_SERVER_TICK.register(LOTRUtumno::serverTick);
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            if (LOTRConfig.disableEnderChestsUtumno && isUtumno(level) && level.getBlockState(hit.getBlockPos()).is(Blocks.ENDER_CHEST)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
    }

    public static boolean isUtumno(Level level) {
        return LOTRDimension.getCurrentDimensionWithFallback(level) == LOTRDimension.UTUMNO;
    }

    /** doEvaporateFX: a hiss and a puff of smoke. */
    public static void doEvaporateFX(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f,
                2.6f + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8f);
        for (ServerPlayer player : PlayerLookup.around(level, net.minecraft.world.phys.Vec3.atCenterOf(pos), 32.0)) {
            ServerPlayNetworking.send(player, new EvaporatePayload(pos));
        }
    }

    /** UtumnoBlock: Utumno's brick, pillars, slabs, stairs and walls. */
    private static final java.util.Set<Block> UTUMNO_BLOCKS = java.util.Set.of(
            LOTRBuildingBlocks.FIRE_UTUMNO_PILLAR,
            LOTRBuildingBlocks.ICE_UTUMNO_PILLAR,
            LOTRBuildingBlocks.OBSIDIAN_UTUMNO_PILLAR,
            LOTRBuildingBlocks.FIRE_UTUMNO_BRICK,
            LOTRBuildingBlocks.UTUMNO_FIRE_TILE_BRICK,
            LOTRBuildingBlocks.ICE_UTUMNO_BRICK,
            LOTRBuildingBlocks.GLOWING_ICE_UTUMNO_BRICK,
            LOTRBuildingBlocks.UTUMNO_ICE_TILE_BRICK,
            LOTRBuildingBlocks.OBSIDIAN_UTUMNO_BRICK,
            LOTRBuildingBlocks.UTUMNO_OBSIDIAN_FIRE_BRICK,
            LOTRBuildingBlocks.UTUMNO_OBSIDIAN_TILE_BRICK,
            LOTRBuildingBlocks.UTUMNO_FIRE_TILE_BRICK_WALL,
            LOTRBuildingBlocks.UTUMNO_ICE_TILE_BRICK_WALL,
            LOTRBuildingBlocks.UTUMNO_OBSIDIAN_TILE_BRICK_WALL,
            LOTRBuildingBlocks.UTUMNO_FIRE_TILE_BRICK_SLAB,
            LOTRBuildingBlocks.UTUMNO_ICE_TILE_BRICK_SLAB,
            LOTRBuildingBlocks.UTUMNO_OBSIDIAN_TILE_BRICK_SLAB,
            LOTRBuildingBlocks.BURNING_UTUMNO_BRICK,
            LOTRBuildingBlocks.FIRE_UTUMNO_BRICK_STAIRS,
            LOTRBuildingBlocks.UTUMNO_FIRE_TILE_BRICK_STAIRS,
            LOTRBuildingBlocks.ICE_UTUMNO_BRICK_STAIRS,
            LOTRBuildingBlocks.UTUMNO_ICE_TILE_BRICK_STAIRS,
            LOTRBuildingBlocks.OBSIDIAN_UTUMNO_BRICK_STAIRS,
            LOTRBuildingBlocks.UTUMNO_OBSIDIAN_TILE_BRICK_STAIRS,
            LOTRBuildingBlocks.FIRE_UTUMNO_BRICK_SLAB,
            LOTRBuildingBlocks.ICE_UTUMNO_BRICK_SLAB,
            LOTRBuildingBlocks.OBSIDIAN_UTUMNO_BRICK_SLAB,
            LOTRBuildingBlocks.FIRE_UTUMNO_BRICK_WALL,
            LOTRBuildingBlocks.ICE_UTUMNO_BRICK_WALL,
            LOTRBuildingBlocks.OBSIDIAN_UTUMNO_BRICK_WALL,
            LOTRBuildingBlocks.FIRE_UTUMNO_PILLAR_SLAB,
            LOTRBuildingBlocks.ICE_UTUMNO_PILLAR_SLAB,
            LOTRBuildingBlocks.OBSIDIAN_UTUMNO_PILLAR_SLAB);

    public static boolean isUtumnoBlock(BlockState state) {
        return UTUMNO_BLOCKS.contains(state.getBlock());
    }

    /**
     * onBlockBreaking: in Utumno its own blocks give way only to the Pickaxe of the Underworld, and
     * not within two blocks of the solid brick between the levels.
     */
    public static boolean canMine(Player player, BlockState state, BlockPos pos) {
        if (!isUtumnoBlock(state) || !isUtumno(player.level())) {
            return true;
        }
        if (!player.getMainHandItem().is(LOTRToolItems.UTUMNO_PICKAXE)) {
            return false;
        }
        int j = pos.getY();
        int levelFuzz = 2;
        for (int l = 0; l < LOTRUtumnoLevel.values().length - 1; ++l) {
            LOTRUtumnoLevel levelUpper = LOTRUtumnoLevel.values()[l];
            LOTRUtumnoLevel levelLower = LOTRUtumnoLevel.values()[l + 1];
            if (j >= levelLower.getHighestCorridorRoof() + levelFuzz && j <= levelUpper.getLowestCorridorFloor() - levelFuzz) {
                return false;
            }
        }
        return true;
    }

    /** onBlockPlace: ice set down on the fire level melts away at once. */
    public static void onBlockPlaced(Level level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel && isUtumno(level) && LOTRUtumnoLevel.forY(pos.getY()) == LOTRUtumnoLevel.FIRE
                && state.is(Blocks.ICE)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            doEvaporateFX(serverLevel, pos);
        }
    }

    /** onFillBucket: water emptied on the fire level boils away; the bucket is emptied all the same. */
    public static boolean evaporatesBucket(Level level, BlockPos pos) {
        if (isUtumno(level) && LOTRUtumnoLevel.forY(pos.getY()) == LOTRUtumnoLevel.FIRE) {
            if (level instanceof ServerLevel serverLevel) {
                doEvaporateFX(serverLevel, pos);
            }
            return true;
        }
        return false;
    }

    /**
     * Each player in Utumno: sixty places within 32 blocks looked at each tick -- still water on the
     * ice level freezes, on the fire level it boils away.
     */
    private static void serverTick(MinecraftServer server) {
        ServerLevel utumno = server.getLevel(LOTRWorldGen.UTUMNO);
        if (utumno == null) {
            return;
        }
        RandomSource rand = utumno.getRandom();
        for (ServerPlayer player : utumno.players()) {
            int i = Mth.floor(player.getX());
            int j = Mth.floor(player.getBoundingBox().minY);
            int k = Mth.floor(player.getZ());
            int range = 32;
            for (int l = 0; l < 60; ++l) {
                int i1 = i + rand.nextInt(range) - rand.nextInt(range);
                int j1 = j + rand.nextInt(range) - rand.nextInt(range);
                int k1 = k + rand.nextInt(range) - rand.nextInt(range);
                BlockPos pos = new BlockPos(i1, j1, k1);
                LOTRUtumnoLevel level = LOTRUtumnoLevel.forY(j1);
                if (level == LOTRUtumnoLevel.ICE && isStillWater(utumno, pos)) {
                    utumno.setBlock(pos, Blocks.ICE.defaultBlockState(), Block.UPDATE_ALL);
                }
                if (level == LOTRUtumnoLevel.FIRE && isStillWater(utumno, pos)) {
                    utumno.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    doEvaporateFX(utumno, pos);
                }
            }
        }
    }

    /** Material.water at metadata 0: a water source block. */
    private static boolean isStillWater(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.WATER) && level.getFluidState(pos).isSource();
    }

    /** onLivingDeath: one of Utumno's servants, killed by a player in Utumno, feeds every altar within five. */
    private static void onServantKilled(Entity entity, Entity attacker) {
        if (!(entity.level() instanceof ServerLevel level) || !isUtumno(level) || !(attacker instanceof Player)
                || LOTRNearestAttackableTargetGoal.factionOf(entity) != LOTRFaction.UTUMNO) {
            return;
        }
        int i = Mth.floor(entity.getX());
        int j = Mth.floor(entity.getBoundingBox().minY);
        int k = Mth.floor(entity.getZ());
        int range = LOTRUtumnoReturnPortalBaseBlock.RANGE;
        for (int i1 = -range; i1 <= range; ++i1) {
            for (int j1 = -range; j1 <= range; ++j1) {
                for (int k1 = -range; k1 <= range; ++k1) {
                    BlockPos pos = new BlockPos(i + i1, j + j1, k + k1);
                    BlockState state = level.getBlockState(pos);
                    if (!state.is(LOTRUtilityBlocks.UTUMNO_RETURN_PORTAL_BASE)) {
                        continue;
                    }
                    int meta = state.getValue(LOTRUtumnoReturnPortalBaseBlock.SACRIFICE) + 1;
                    if (meta >= LOTRUtumnoReturnPortalBaseBlock.MAX_SACRIFICE) {
                        level.explode(attacker, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0f, false, Level.ExplosionInteraction.NONE);
                        level.setBlock(pos, LOTRUtilityBlocks.UTUMNO_RETURN_PORTAL.defaultBlockState(), Block.UPDATE_ALL);
                    } else {
                        level.setBlock(pos, state.setValue(LOTRUtumnoReturnPortalBaseBlock.SACRIFICE, meta), Block.UPDATE_ALL);
                    }
                    for (ServerPlayer player : PlayerLookup.around(level, net.minecraft.world.phys.Vec3.atCenterOf(pos), 32.0)) {
                        ServerPlayNetworking.send(player, new KillPayload(entity.getId(), pos));
                    }
                }
            }
        }
    }
}
