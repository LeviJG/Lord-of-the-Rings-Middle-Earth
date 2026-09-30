package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.particle.LOTRMallornEntSummonParticle;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRMallornEntEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRTreeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMallornEntHealPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMallornEntSummonPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

/** The client ends of LOTRPacketMallornEntHeal and LOTRPacketMallornEntSummon. */
public final class LOTRMallornEntClient {

    private LOTRMallornEntClient() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRMallornEntHealPayload.TYPE, (payload, context) -> {
            ClientLevel level = context.client().level;
            if (level != null && level.getEntity(payload.entityId()) instanceof LOTRMallornEntEntity ent) {
                ent.receiveClientHealing(payload);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRMallornEntSummonPayload.TYPE, (payload, context) -> {
            ClientLevel level = context.client().level;
            if (level != null && level.getEntity(payload.entityId()) instanceof LOTRMallornEntEntity ent
                    && level.getEntity(payload.summonedId()) instanceof LOTRTreeEntity tree) {
                spawnEntSummonParticles(level, ent, tree);
            }
        });
    }

    /** spawnEntSummonParticles: the arc of leaves from the Ent to the tree, and a burst of them about it. */
    private static void spawnEntSummonParticles(ClientLevel level, Entity ent, LOTRTreeEntity tree) {
        BlockState leaves = LOTRTreeEntity.leafBlock(tree.getTreeType()).defaultBlockState();
        int particles = 60;
        for (int l = 0; l < particles; ++l) {
            float t = (float) l / particles;
            Minecraft.getInstance().particleEngine.add(new LOTRMallornEntSummonParticle(level, ent, tree, t, leaves));
        }
        RandomSource random = level.getRandom();
        BlockParticleOption option = new BlockParticleOption(LOTRParticles.MALLORN_ENT_HEAL, leaves);
        for (int l = 0; l < 120; ++l) {
            level.addParticle(option, tree.getX() + (random.nextDouble() - 0.5) * tree.getBbWidth(),
                    tree.getY() + tree.getBbHeight() * 0.5,
                    tree.getZ() + (random.nextDouble() - 0.5) * tree.getBbWidth(),
                    Mth.nextDouble(random, -0.4, 0.4), Mth.nextDouble(random, -0.4, 0.4),
                    Mth.nextDouble(random, -0.4, 0.4));
        }
    }
}
