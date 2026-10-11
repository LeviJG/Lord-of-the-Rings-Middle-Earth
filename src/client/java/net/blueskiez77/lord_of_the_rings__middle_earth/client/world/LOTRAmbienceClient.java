package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import java.util.ArrayList;
import java.util.Collection;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.particle.LOTRDeadMarshFaceParticle;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBarrowDownsBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRDeadMarshesBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRDolGuldurBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradCoastBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRLindonCoastBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMirkwoodCorruptedBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMorgulValeBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTROceanBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTROldForestBiome;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;

/**
 * LOTRAmbience and LOTRTickHandlerClient.spawnEnvironmentFX:
 * <ul>
 * <li>("Ambience") a wight's whisper now and then near the one it hunts, or out on the Barrow-downs,
 *     and the like in Dol Guldur, the Dead Marshes, corrupted Mirkwood, the Old Forest and Utumno (and its
 *     caves' groaning); the sea's
 *     waves heard along the coasts, rolling in as spray;</li>
 * <li>("New weather") Middle-earth's own rain and thunder, and wind high up -- higher still in fair
 *     weather, lower in rain, and low in a sandstorm;</li>
 * <li>bubbles rising from corrupted Mirkwood's and the Morgul Vale's water, the dead looking up from
 *     the Dead Marshes', and spray where falling water meets still.</li>
 * </ul>
 * (The jazz is LOTRJazzMusic.)
 */
public final class LOTRAmbienceClient {

    private static int ticksSinceWight;
    private static final Collection<SoundInstance> PLAYING_WIND_SOUNDS = new ArrayList<>();
    private static final Collection<SoundInstance> PLAYING_SEA_SOUNDS = new ArrayList<>();

    private LOTRAmbienceClient() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTRAmbienceClient::tick);
    }

    /** onPlaySound ("New weather"): in Middle-earth, its own rain and thunder in place of the game's. */
    public static SoundEvent replaceSound(SoundEvent sound) {
        if (!LOTRConfig.newWeather || !LOTREnvironmentClient.isMiddleEarth(Minecraft.getInstance().level)) {
            return sound;
        }
        if (sound == SoundEvents.WEATHER_RAIN || sound == SoundEvents.WEATHER_RAIN_ABOVE) {
            return LOTRSounds.AMBIENT_WEATHER_RAIN;
        }
        if (sound == SoundEvents.LIGHTNING_BOLT_THUNDER) {
            return LOTRSounds.AMBIENT_WEATHER_THUNDER;
        }
        return sound;
    }

    private static int randomIntegerInRange(RandomSource rand, int min, int max) {
        return min >= max ? min : rand.nextInt(max - min + 1) + min;
    }

    private static void tick(Minecraft mc) {
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;
        if (level == null || player == null || mc.isPaused()) {
            return;
        }
        if (mc.options.particles().get() != ParticleStatus.MINIMAL) {
            spawnEnvironmentFX(mc, level, player);
        }
        updateAmbience(mc, level, player);
    }

    private static void updateAmbience(Minecraft mc, ClientLevel level, LocalPlayer player) {
        double x = player.getX();
        double y = player.getBoundingBox().minY;
        double z = player.getZ();
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        boolean middleEarth = LOTREnvironmentClient.isMiddleEarth(level);
        boolean utumno = net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno.isUtumno(level);
        LOTRBiome biome = middleEarth || utumno ? LOTRBiomes.of(level.getBiome(new BlockPos(i, j, k))) : null;
        RandomSource rand = level.getRandom();
        if (LOTRConfig.enableAmbience) {
            if (ticksSinceWight > 0) {
                --ticksSinceWight;
            } else {
                boolean wights = LOTREnvironmentClient.anyWightsViewed() && rand.nextInt(20) == 0
                        || biome instanceof LOTRBarrowDownsBiome && rand.nextInt(3000) == 0;
                if (wights) {
                    level.playLocalSound(x, y, z, LOTRSounds.WIGHT_AMBIENCE, SoundSource.NEUTRAL, 1.0f, 0.8f + rand.nextFloat() * 0.4f, false);
                    ticksSinceWight = 300;
                }
            }
            boolean spookyBiomeNoise = false;
            float spookyPitch = 1.0f;
            if (biome instanceof LOTRDolGuldurBiome) {
                spookyBiomeNoise = rand.nextInt(1000) == 0;
                spookyPitch = 0.85f;
            } else if (biome instanceof LOTRDeadMarshesBiome) {
                spookyBiomeNoise = rand.nextInt(2400) == 0;
            } else if (biome instanceof LOTRMirkwoodCorruptedBiome) {
                spookyBiomeNoise = rand.nextInt(3000) == 0;
            } else if (biome instanceof LOTROldForestBiome) {
                spookyBiomeNoise = rand.nextInt(6000) == 0;
            } else if (biome instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRUtumnoBiome) {
                spookyBiomeNoise = rand.nextInt(1000) == 0;
                spookyPitch = 0.75f;
            }
            if (spookyBiomeNoise) {
                level.playLocalSound(x, y, z, LOTRSounds.WIGHT_AMBIENCE, SoundSource.NEUTRAL, 1.0f, (0.8f + rand.nextFloat() * 0.4f) * spookyPitch, false);
            }
            if (biome instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRUtumnoBiome && level.getRandom().nextInt(500) == 0) {
                level.playLocalSound(x, y, z, net.minecraft.sounds.SoundEvents.AMBIENT_CAVE.value(), SoundSource.AMBIENT, 1.0f,
                        0.8f + rand.nextFloat() * 0.2f, false);
            }
        }
        if (LOTRConfig.newWeather && middleEarth) {
            if (PLAYING_WIND_SOUNDS.size() < 4) {
                int xzRange = 16;
                int minWindHeight = 100;
                int fullWindHeight = 180;
                if (rand.nextInt(20) == 0) {
                    minWindHeight -= 10;
                    if (rand.nextInt(10) == 0) {
                        minWindHeight -= 10;
                    }
                }
                if (level.isRaining()) {
                    minWindHeight = 80;
                    fullWindHeight = 120;
                    if (rand.nextInt(20) == 0) {
                        minWindHeight -= 20;
                    }
                    if (biome != null && LOTRWeatherClient.isSandstormBiome(biome)) {
                        minWindHeight = 60;
                        fullWindHeight = 80;
                    }
                }
                for (int l = 0; l < 2; ++l) {
                    int i1 = i + randomIntegerInRange(rand, -xzRange, xzRange);
                    int k1 = k + randomIntegerInRange(rand, -xzRange, xzRange);
                    int j1 = j + randomIntegerInRange(rand, -16, 16);
                    if (j1 >= minWindHeight && level.canSeeSky(new BlockPos(i1, j1, k1))) {
                        float windiness = Mth.clamp((float) (j1 - minWindHeight) / (fullWindHeight - minWindHeight), 0.0f, 1.0f);
                        if (windiness >= rand.nextFloat()) {
                            float vol = Math.max(0.25f, windiness);
                            float pitch = 0.8f + rand.nextFloat() * 0.4f;
                            SoundInstance wind = ambientSound(LOTRSounds.AMBIENT_WEATHER_WIND, SoundSource.WEATHER, vol, pitch, i1 + 0.5, j1 + 0.5, k1 + 0.5,
                                    player, xzRange);
                            mc.getSoundManager().play(wind);
                            PLAYING_WIND_SOUNDS.add(wind);
                            break;
                        }
                    }
                }
            } else {
                PLAYING_WIND_SOUNDS.removeIf(wind -> !mc.getSoundManager().isActive(wind));
            }
        }
        if (LOTRConfig.enableAmbience) {
            if (PLAYING_SEA_SOUNDS.size() < 3) {
                if (biome instanceof LOTROceanBiome || biome instanceof LOTRLindonCoastBiome || biome instanceof LOTRFarHaradCoastBiome) {
                    playSea(mc, level, player, i, j, k, rand);
                }
            } else {
                PLAYING_SEA_SOUNDS.removeIf(sea -> !mc.getSoundManager().isActive(sea));
            }
        }
    }

    /** The sea heard at some water in sight up to 64 blocks off, and its waves rolling in towards the shore. */
    private static void playSea(Minecraft mc, ClientLevel level, LocalPlayer player, int i, int j, int k, RandomSource rand) {
        int xzRange = 64;
        float[] rangeChecks = {0.25f, 0.5f, 0.75f, 1.0f};
        for (float fr : rangeChecks) {
            int range = (int) (xzRange * fr);
            for (int l = 0; l < 8; ++l) {
                int i1 = i + randomIntegerInRange(rand, -range, range);
                int k1 = k + randomIntegerInRange(rand, -range, range);
                int j1 = j + randomIntegerInRange(rand, -16, 8);
                BlockPos pos = new BlockPos(i1, j1, k1);
                if (!level.getFluidState(pos).is(FluidTags.WATER) || j1 < level.getHeight(Heightmap.Types.OCEAN_FLOOR, i1, k1)) {
                    continue;
                }
                float pitch = 0.8f + rand.nextFloat() * 0.4f;
                SoundInstance sea = ambientSound(LOTRSounds.AMBIENT_TERRAIN_SEA, SoundSource.AMBIENT, 1.0f, pitch, i1 + 0.5, j1 + 0.5, k1 + 0.5, player, xzRange);
                mc.getSoundManager().play(sea);
                PLAYING_SEA_SOUNDS.add(sea);
                int j2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, i1, k1) - 1;
                if (level.getFluidState(new BlockPos(i1, j2, k1)).is(FluidTags.WATER)) {
                    double dx = i1 + 0.5 - player.getX();
                    double dz = k1 + 0.5 - player.getZ();
                    float angle = (float) Math.atan2(dz, dx);
                    float cos = Mth.cos(angle);
                    float sin = Mth.sin(angle);
                    float angle90 = angle - (float) (Math.PI / 2.0);
                    float cos90 = Mth.cos(angle90);
                    float sin90 = Mth.sin(angle90);
                    float waveSpeed = 0.3f + rand.nextFloat() * 0.2f;
                    int waveR = 40 + rand.nextInt(100);
                    for (int w = -waveR; w <= waveR; ++w) {
                        float f = w / 8.0f;
                        double d0 = i1 + 0.5 + f * cos90;
                        double d1 = j2 + 1.0 + (0.02f + rand.nextFloat() * 0.08f);
                        double d2 = k1 + 0.5 + f * sin90;
                        if (level.getFluidState(BlockPos.containing(d0, d1 - 1.0, d2)).is(FluidTags.WATER)) {
                            level.addParticle(LOTRParticles.WAVE, d0, d1, d2, waveSpeed * -cos, 0.0, waveSpeed * -sin);
                        }
                    }
                }
            }
        }
    }

    /** AmbientSoundNoAttentuation: heard at a volume by how near it is, not by the game's falloff. */
    private static SoundInstance ambientSound(SoundEvent sound, SoundSource source, float volume, float pitch, double x, double y, double z,
                                              LocalPlayer player, int maxRange) {
        float distFr = (float) player.position().distanceTo(new net.minecraft.world.phys.Vec3(x, y, z)) / maxRange;
        distFr = Math.min(distFr, 1.0f);
        distFr = Mth.clamp((1.0f - distFr) * 1.5f, 0.1f, 1.0f);
        return new SimpleSoundInstance(sound.location(), source, volume * distFr, pitch, SoundInstance.createUnseededRandom(), false, 0,
                SoundInstance.Attenuation.NONE, x, y, z, false);
    }

    /** spawnEnvironmentFX: a thousand blocks near the player looked at each tick. */
    private static void spawnEnvironmentFX(Minecraft mc, ClientLevel level, LocalPlayer player) {
        int i = Mth.floor(player.getX());
        int j = Mth.floor(player.getBoundingBox().minY);
        int k = Mth.floor(player.getZ());
        int range = 16;
        RandomSource rand = level.getRandom();
        boolean middleEarth = LOTREnvironmentClient.isMiddleEarth(level);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int l = 0; l < 1000; ++l) {
            int i1 = i + rand.nextInt(range) - rand.nextInt(range);
            int j1 = j + rand.nextInt(range) - rand.nextInt(range);
            int k1 = k + rand.nextInt(range) - rand.nextInt(range);
            pos.set(i1, j1, k1);
            FluidState fluid = level.getFluidState(pos);
            if (!fluid.is(FluidTags.WATER)) {
                continue;
            }
            if (middleEarth) {
                LOTRBiome biome = LOTRBiomes.of(level.getBiome(pos));
                if (biome instanceof LOTRMirkwoodCorruptedBiome && rand.nextInt(20) == 0) {
                    level.addParticle(LOTRParticles.MIRKWOOD_WATER, i1 + rand.nextFloat(), j1 + 0.75, k1 + rand.nextFloat(), 0.0, 0.05, 0.0);
                }
                if (biome instanceof LOTRMorgulValeBiome && rand.nextInt(40) == 0) {
                    level.addParticle(LOTRParticles.MORGUL_WATER, i1 + rand.nextFloat(), j1 + 0.75, k1 + rand.nextFloat(), 0.0, 0.05, 0.0);
                }
                if (biome instanceof LOTRDeadMarshesBiome && rand.nextInt(800) == 0) {
                    mc.particleEngine.add(new LOTRDeadMarshFaceParticle(level, i1 + rand.nextFloat(), j1 + 0.25 - rand.nextFloat(), k1 + rand.nextFloat()));
                }
            }
            if (!fluid.isSource() && level.getFluidState(pos.below()).is(FluidTags.WATER)) {
                for (int i2 = i1 - 1; i2 <= i1 + 1; ++i2) {
                    for (int k2 = k1 - 1; k2 <= k1 + 1; ++k2) {
                        BlockPos adj = new BlockPos(i2, j1 - 1, k2);
                        FluidState adjFluid = level.getFluidState(adj);
                        if (adjFluid.is(FluidTags.WATER) && adjFluid.isSource() && level.isEmptyBlock(adj.above())) {
                            for (int l1 = 0; l1 < 2; ++l1) {
                                double d = i1 + 0.5 + (i2 - i1) * rand.nextFloat();
                                double d1 = j1 + rand.nextFloat() * 0.2f;
                                double d2 = k1 + 0.5 + (k2 - k1) * rand.nextFloat();
                                level.addParticle(ParticleTypes.POOF, d, d1, d2, 0.0, 0.0, 0.0);
                            }
                        }
                    }
                }
            }
        }
    }
}
