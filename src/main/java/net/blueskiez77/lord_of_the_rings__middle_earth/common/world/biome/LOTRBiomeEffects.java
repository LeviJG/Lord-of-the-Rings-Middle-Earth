package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitBounderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRMirkwoodSpiderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRMarshWraithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolMaterials;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentHudPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * LOTREventHandler.onLivingUpdate's biome effects: marsh wraiths rising out of the Dead Marshes'
 * water, Shire bounders turning out against the Shire's enemies, the Mirkwood and Morgul Vale
 * waters' curses, and the Near Harad sun's burning.
 */
public final class LOTRBiomeEffects {

    private LOTRBiomeEffects() {
    }

    /** Each living thing's tick, on the server. */
    public static void tick(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel world) || !entity.isAlive()) {
            return;
        }
        LOTRBiome biome = LOTRBiomes.of(world.getBiome(entity.blockPosition()));
        if (biome == null) {
            return;
        }
        if (biome instanceof LOTRDeadMarshesBiome) {
            spawnMarshWraithIfConditionsMet(world, entity);
        }
        spawnHobbitBoundersIfConditionsMet(world, entity, biome);
        applyMirkwoodCorruptionEffects(entity, biome);
        // The original applied the Morgul Vale's curse twice over, by two copies of the same method.
        applyMorgulValeCurses(world, entity, biome);
        applyMorgulValeCurses(world, entity, biome);
        handleEntityFireDamageEffects(world, entity, biome);
    }

    private static boolean isCreative(LivingEntity entity) {
        return entity instanceof Player player && player.getAbilities().instabuild;
    }

    public static void spawnMarshWraithIfConditionsMet(ServerLevel world, LivingEntity entity) {
        if (entity.isInWater() || entity.isPassenger() || isCreative(entity)
                || entity instanceof WaterAnimal || entity instanceof LOTRMarshWraithEntity) {
            return;
        }
        double expandValue = 10.0;
        if (!world.getEntitiesOfClass(LOTRMarshWraithEntity.class, entity.getBoundingBox().inflate(expandValue)).isEmpty()) {
            return;
        }
        int i2 = Mth.floor(entity.getX());
        int k = Mth.floor(entity.getZ());
        int j2 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i2, k);
        BlockState block = world.getBlockState(new BlockPos(i2, j2, k));
        while (!block.getFluidState().isEmpty() || !block.getCollisionShape(world, new BlockPos(i2, j2, k)).isEmpty()) {
            ++j2;
            block = world.getBlockState(new BlockPos(i2, j2, k));
        }
        // The original's loop never stops on water (a liquid), so the block it ends on is never water
        // and no wraith ever rose; kept as it was.
        if (j2 - entity.getBoundingBox().minY < 2.0 && block.getFluidState().is(FluidTags.WATER)) {
            int i1 = i2 + world.getRandom().nextInt(7) - 3;
            int k1 = k + world.getRandom().nextInt(7) - 3;
            int j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
            LOTRMarshWraithEntity wraith = LOTREntities.MARSH_WRAITH.create(world, EntitySpawnReason.EVENT);
            if (wraith == null) {
                return;
            }
            wraith.snapTo(i1 + 0.5, j1, k1 + 0.5, world.getRandom().nextFloat() * 360.0f, 0.0f);
            if (wraith.distanceToSqr(entity) <= 144.0) {
                world.addFreshEntity(wraith);
                wraith.setTarget(entity);
                wraith.attackTargetUUID = entity.getUUID();
                world.playSound(null, wraith, LOTRSounds.WRAITH_SPAWN, SoundSource.HOSTILE, 1.0f, 0.7f + world.getRandom().nextFloat() * 0.6f);
            }
        }
    }

    public static void spawnHobbitBoundersIfConditionsMet(ServerLevel world, LivingEntity entity, LOTRBiome biome) {
        if (!world.getGameRules().get(GameRules.SPAWN_MOBS) || !world.isBrightOutside()) {
            return;
        }
        float f = 0.0f;
        int bounders = 0;
        if (LOTRFaction.HOBBIT.isBadRelation(LOTRNearestAttackableTargetGoal.factionOf(entity))) {
            float health = entity.getMaxHealth() + entity.getArmorValue();
            f = health * 2.5f;
            int i3 = (int) (health / 15.0f);
            bounders = 2 + world.getRandom().nextInt(i3 + 1);
        } else if (entity instanceof Player player) {
            float alignment = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.HOBBIT);
            if (!player.getAbilities().instabuild && alignment < 0.0f) {
                f = -alignment;
                int i4 = (int) (f / 50.0f);
                bounders = 2 + world.getRandom().nextInt(i4 + 1);
            }
        }
        if (f <= 0.0f) {
            return;
        }
        f = Math.min(f, 2000.0f);
        int chance = (int) (2000000.0f / f);
        bounders = Math.min(bounders, 5);
        int i3 = Mth.floor(entity.getX());
        int k3 = Mth.floor(entity.getZ());
        if (world.getRandom().nextInt(chance) != 0 || !(biome instanceof LOTRShireBiome)
                || !world.getEntitiesOfClass(LOTRHobbitBounderEntity.class, entity.getBoundingBox().inflate(12.0, 6.0, 12.0)).isEmpty()) {
            return;
        }
        boolean sentMessage = false;
        boolean playedHorn = false;
        for (int l4 = 0; l4 < bounders; ++l4) {
            LOTRHobbitBounderEntity bounder = LOTREntities.HOBBIT_BOUNDER.create(world, EntitySpawnReason.EVENT);
            if (bounder == null) {
                continue;
            }
            for (int l1 = 0; l1 < 32; ++l1) {
                int i1 = i3 - world.getRandom().nextInt(12) + world.getRandom().nextInt(12);
                int k1 = k3 - world.getRandom().nextInt(12) + world.getRandom().nextInt(12);
                int j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
                BlockPos below = new BlockPos(i1, j1 - 1, k1);
                if (!world.getBlockState(below).isFaceSturdy(world, below, Direction.UP)
                        || world.getBlockState(new BlockPos(i1, j1, k1)).isRedstoneConductor(world, new BlockPos(i1, j1, k1))
                        || world.getBlockState(new BlockPos(i1, j1 + 1, k1)).isRedstoneConductor(world, new BlockPos(i1, j1 + 1, k1))) {
                    continue;
                }
                bounder.snapTo(i1 + 0.5, j1, k1 + 0.5, 0.0f, 0.0f);
                if (!bounder.checkSpawnRules(world, EntitySpawnReason.EVENT) || !bounder.checkSpawnObstruction(world)
                        || entity.distanceTo(bounder) <= 6.0) {
                    continue;
                }
                bounder.finalizeSpawn(world, world.getCurrentDifficultyAt(bounder.blockPosition()), EntitySpawnReason.EVENT, null);
                world.addFreshEntity(bounder);
                bounder.setTarget(entity);
                if (!sentMessage && entity instanceof Player player) {
                    String bank = bounder.getSpeechBank(player);
                    if (bank != null) {
                        bounder.sendSpeechBank(player, bank);
                    }
                    sentMessage = true;
                }
                if (!playedHorn) {
                    world.playSound(null, bounder, LOTRSounds.ITEM_HORN, SoundSource.NEUTRAL, 2.0f, 2.0f);
                    playedHorn = true;
                }
                break;
            }
        }
    }

    public static void applyMirkwoodCorruptionEffects(LivingEntity entity, LOTRBiome biome) {
        if (!entity.isInWater() || entity.isPassenger() || entity.tickCount % 10 != 0) {
            return;
        }
        if (isCreative(entity) || entity instanceof LOTRMirkwoodSpiderEntity) {
            return;
        }
        if (biome instanceof LOTRMirkwoodCorruptedBiome) {
            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600));
        }
    }

    /** The Morgul Vale's waters: on all but Mordor's friends -- and a player the more likely to be spared the higher in Mordor's favour. */
    public static void applyMorgulValeCurses(ServerLevel world, LivingEntity entity, LOTRBiome biome) {
        if (!entity.isInWater() || entity.isPassenger() || entity.tickCount % 10 != 0) {
            return;
        }
        boolean flag = true;
        if (entity instanceof Player player) {
            if (player.getAbilities().instabuild) {
                flag = false;
            } else {
                float alignment = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR);
                float level = 100.0f;
                if (alignment > level) {
                    flag = false;
                } else if (world.getRandom().nextInt(Math.max(Math.round(level), 1)) < alignment) {
                    flag = false;
                }
            }
        }
        if (LOTRNearestAttackableTargetGoal.factionOf(entity).isGoodRelation(LOTRFaction.MORDOR)) {
            flag = false;
        }
        if (flag && biome instanceof LOTRMorgulValeBiome) {
            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        }
    }

    private static final ResourceKey<EquipmentAsset> HARAD_NOMAD = LOTRToolMaterials.HARAD_NOMAD_ARMOR.assetId();

    /** handleEntityFireDamageEffects: the Near Harad sun burns those out under it by day, less those in leather or robes. */
    public static void handleEntityFireDamageEffects(ServerLevel world, LivingEntity entity, LOTRBiome biome) {
        if (entity.tickCount % 20 != 0 || isCreative(entity) || entity instanceof LOTRNearHaradBiome.ImmuneToHeat) {
            return;
        }
        BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
        if (biome instanceof LOTRNearHaradBiome && !entity.isInWater() && world.canSeeSky(pos) && world.isBrightOutside()) {
            int burnChance = 50;
            int burnProtection = 0;
            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}) {
                ItemStack armour = entity.getItemBySlot(slot);
                Equippable equippable = armour.get(DataComponents.EQUIPPABLE);
                ResourceKey<EquipmentAsset> asset = equippable == null ? null : equippable.assetId().orElse(null);
                if (asset == null) {
                    continue;
                }
                if (asset == EquipmentAssets.LEATHER || asset == LOTRToolMaterials.LAMEDON_JACKET_ASSET) {
                    burnProtection += 50;
                }
                if (asset == LOTRToolMaterials.HARAD_ROBES_ASSET || asset == LOTRToolMaterials.HARAD_TURBAN_ASSET) {
                    burnProtection += 400;
                }
                if (asset == HARAD_NOMAD) {
                    burnProtection += 200;
                }
            }
            burnChance += burnProtection;
            if (world.getRandom().nextInt(Math.max(burnChance, 1)) == 0 && entity.hurtServer(world, world.damageSources().onFire(), 1.0f)
                    && entity instanceof ServerPlayer player) {
                ServerPlayNetworking.send(player, new LOTRAlignmentHudPayloads.EnvironmentOverlay(LOTRAlignmentHudPayloads.EnvironmentOverlay.BURN));
            }
        }
    }
}
