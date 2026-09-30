package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

/**
 * The mod's own particles: the types LOTRClientProxy.spawnParticle took by name.
 * Only the ones ported blocks use are here so far; the rest of lotr.client.fx
 * belongs to Track D16. Their client providers are in LOTRParticleProviders.
 */
public final class LOTRParticles {

    /** LOTREntityMorgulPortalFX: green, full-bright spell particles. */
    public static final SimpleParticleType MORGUL_PORTAL = register("morgul_portal");
    /** LOTREntityRiverWaterFX in the Morgul Vale's water colour ("morgulWater"). */
    public static final SimpleParticleType MORGUL_WATER = register("morgul_water");
    /** LOTREntityWhiteSmokeFX. */
    public static final SimpleParticleType WHITE_SMOKE = register("white_smoke");
    /** LOTREntityQuenditeSmokeFX: blue-green smoke off quendite grass. */
    public static final SimpleParticleType QUENDITE_SMOKE = register("quendite_smoke");
    /** LOTREntityChillFX: pale blue smoke that hangs and sinks, off a Chilling blow. */
    public static final SimpleParticleType CHILL = register("chill");
    /** LOTREntityMarshFlameFX: a long-lived flame, off the marsh lights. */
    public static final SimpleParticleType MARSH_FLAME = register("marsh_flame");
    /** LOTREntityMarshLightFX: a pale grey light, off the marsh lights. */
    public static final SimpleParticleType MARSH_LIGHT = register("marsh_light");
    /** LOTREntityLargeBlockFX of stone ("largeStone"): a stone chip four times the size, off thrown rocks and mountain trolls turned to stone. */
    public static final SimpleParticleType LARGE_STONE = register("large_stone");
    /** LOTREntityBossSpawnFX ("mtcSpawn"): chips of stone, dirt, gravel or sand twice the size, as the chieftain rises. */
    public static final SimpleParticleType MTC_SPAWN = register("mtc_spawn");
    /** LOTREntityLargeBlockFX of iron ("mtcArmor"): the chieftain's armour breaking away. */
    public static final SimpleParticleType MTC_ARMOR = register("mtc_armor");
    /** LOTREntityMTCHealFX ("mtcHeal"): a red wisp drawn from a troll to the chieftain it heals. */
    public static final SimpleParticleType MTC_HEAL = register("mtc_heal");
    /** LOTREntityBossSpawnFX of dirt or mallorn wood ("mEntSpawn"): the Mallorn Ent rising. */
    public static final SimpleParticleType MALLORN_ENT_SPAWN = register("mallorn_ent_spawn");
    /** LOTREntityLargeBlockFX of mallorn wood ("mEntJumpSmash"): the Mallorn Ent landing. */
    public static final SimpleParticleType MALLORN_ENT_JUMP_SMASH = register("mallorn_ent_jump_smash");
    /**
     * LOTREntityMallornEntHealFX ("mEntHeal_block_meta"): a large, weightless
     * fragment of a leaf block, tinted as the block is, drifting through walls --
     * leaves feeding the Mallorn Ent, a summoned tree's burst, the leaf bomb.
     */
    public static final ParticleType<BlockParticleOption> MALLORN_ENT_HEAL = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mallorn_ent_heal"),
            FabricParticleTypes.complex(BlockParticleOption::codec, BlockParticleOption::streamCodec));

    private LOTRParticles() {
    }

    private static SimpleParticleType register(String name) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name), FabricParticleTypes.simple());
    }

    public static void init() {
    }
}
