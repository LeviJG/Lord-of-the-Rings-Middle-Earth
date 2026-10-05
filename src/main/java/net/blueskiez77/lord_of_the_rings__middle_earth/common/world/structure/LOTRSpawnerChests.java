package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRHaradPyramidWraithEntity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * LOTRBlockSpawnerChest and LOTRTileEntitySpawnerChest: a chest a structure
 * leaves with a creature waiting in it. It looks, drops and is picked as the
 * chest it is modelled on (a plain chest, a stone chest, an Ancient Haradric
 * chest), so the port places that chest itself and keeps the creature ("MobID")
 * on its block entity. The first time it is opened it lets the creature out
 * -- in a puff of smoke, with the wraith's rising sound -- and does not open;
 * from then on it is an ordinary chest. Broken while still holding it, it
 * lets it out too. A Harad pyramid wraith comes with four desert scorpions and
 * a stroke of lightning.
 *
 * <p>Unlike the original, a spawner chest destroyed by an explosion or by
 * anything but a player keeps its creature.
 */
public final class LOTRSpawnerChests {

    public static final AttachmentType<String> SPAWNER_MOB = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "spawner_chest_mob"),
            builder -> builder.persistent(Codec.STRING));

    private LOTRSpawnerChests() {
    }

    public static void init() {
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if (be == null || !be.hasAttached(SPAWNER_MOB)) {
                return InteractionResult.PASS;
            }
            if (level instanceof ServerLevel serverLevel) {
                spawnEntity(serverLevel, hit.getBlockPos(), be);
            }
            return InteractionResult.SUCCESS;
        });
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, be) -> {
            if (be != null && be.hasAttached(SPAWNER_MOB) && level instanceof ServerLevel serverLevel) {
                spawnEntity(serverLevel, pos, be);
            }
            return true;
        });
    }

    /** setMobID. */
    public static void setMob(BlockEntity be, EntityType<?> type) {
        be.setAttached(SPAWNER_MOB, BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
        be.setChanged();
    }

    private static void spawnEntity(ServerLevel level, BlockPos pos, BlockEntity be) {
        String id = be.removeAttached(SPAWNER_MOB);
        be.setChanged();
        Identifier key = id == null ? null : Identifier.tryParse(id);
        EntityType<?> type = key == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(key).orElse(null);
        if (type == null || !(type.create(level, EntitySpawnReason.TRIGGERED) instanceof Mob mob)) {
            return;
        }
        mob.snapTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0f, 0.0f);
        mob.spawnAnim();
        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.TRIGGERED, null);
        if (mob instanceof LOTRNPCEntity npc) {
            npc.isNPCPersistent = true;
        }
        level.addFreshEntity(mob);
        level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), LOTRSounds.WRAITH_SPAWN, SoundSource.HOSTILE,
                1.0f, 0.7f + level.getRandom().nextFloat() * 0.6f);
        if (mob instanceof LOTRHaradPyramidWraithEntity) {
            for (int l = 0; l < 4; ++l) {
                Mob scorpion = LOTREntities.DESERT_SCORPION.create(level, EntitySpawnReason.MOB_SUMMONED);
                if (scorpion == null) {
                    continue;
                }
                double d = mob.getX() - level.getRandom().nextDouble() * 3.0 + level.getRandom().nextDouble() * 3.0;
                double d2 = mob.getZ() - level.getRandom().nextDouble() * 3.0 + level.getRandom().nextDouble() * 3.0;
                scorpion.snapTo(d, mob.getY(), d2, level.getRandom().nextFloat() * 360.0f, 0.0f);
                if (scorpion.checkSpawnRules(level, EntitySpawnReason.SPAWNER) && scorpion.checkSpawnObstruction(level)) {
                    level.addFreshEntity(scorpion);
                }
            }
            LightningBolt bolt = net.minecraft.world.entity.EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.snapTo(mob.getX(), mob.getY(), mob.getZ());
                level.addFreshEntity(bolt);
            }
        }
    }
}
