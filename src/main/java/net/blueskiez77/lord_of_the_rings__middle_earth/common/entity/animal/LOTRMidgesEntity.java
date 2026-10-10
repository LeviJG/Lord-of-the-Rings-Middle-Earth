package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.level.LevelAccessor;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMidges: a humming cloud of three to eight midges, drifting about
 * at most eight blocks above the ground, now and then following a player for
 * a while, and slowly breeding more swarms (1 in 500 a tick while fewer than
 * five are within 24 blocks). Each midge bobs on its own inside the two-block
 * cloud (a client-side {@link Midge}, as in the original).
 *
 * <p>The Midgewater marshes' swarms come thicker; they spawn above sea level on the biome's top block.
 *
 * <p>A swarm shot down by a hired unit earns its player shootDownMidges.
 */
public class LOTRMidgesEntity extends Mob {

    public final Midge[] midges;
    private @Nullable BlockPos currentFlightTarget;
    private @Nullable Player playerTarget;

    public LOTRMidgesEntity(EntityType<? extends LOTRMidgesEntity> type, Level level) {
        super(type, level);
        this.midges = new Midge[3 + this.random.nextInt(6)];
        for (int l = 0; l < this.midges.length; ++l) {
            this.midges[l] = new Midge();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(getDeltaMovement().multiply(1.0, 0.6, 1.0));
        for (Midge midge : this.midges) {
            midge.update();
        }
        if (this.random.nextInt(5) == 0) {
            playSound(LOTRSounds.MIDGES_SWARM, getSoundVolume(), getVoicePitch());
        }
        if (level() instanceof ServerLevel level && isAlive()) {
            // The Midgewater's swarms come thicker and closer together.
            boolean inMidgewater = net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes.of(level.getBiome(blockPosition())) instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMidgewaterBiome;
            int chance = inMidgewater ? 100 : 500;
            double range = inMidgewater ? 16.0 : 24.0;
            int threshold = inMidgewater ? 6 : 5;
            List<LOTRMidgesEntity> swarms = this.random.nextInt(chance) == 0
                    ? level.getEntitiesOfClass(LOTRMidgesEntity.class, getBoundingBox().inflate(range)) : null;
            if (swarms != null && swarms.size() < threshold) {
                LOTRMidgesEntity more = LOTREntities.MIDGES.create(level, EntitySpawnReason.BREEDING);
                if (more != null) {
                    more.snapTo(getX(), getY(), getZ(), this.random.nextFloat() * 360.0f, 0.0f);
                    more.finalizeSpawn(level, level.getCurrentDifficultyAt(blockPosition()), EntitySpawnReason.BREEDING, null);
                    level.addFreshEntity(more);
                }
            }
        }
    }

    /** updateAITasks. */
    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (this.currentFlightTarget != null && !level.isEmptyBlock(this.currentFlightTarget)) {
            this.currentFlightTarget = null;
        }
        if (this.playerTarget != null && (!this.playerTarget.isAlive() || distanceToSqr(this.playerTarget) > 256.0)) {
            this.playerTarget = null;
        }
        if (this.playerTarget != null) {
            if (this.random.nextInt(400) == 0) {
                this.playerTarget = null;
            } else {
                this.currentFlightTarget = BlockPos.containing((int) this.playerTarget.getX(),
                        (int) this.playerTarget.getY() + 3, (int) this.playerTarget.getZ());
            }
        } else if (this.random.nextInt(100) == 0) {
            Player closest = level.getNearestPlayer(this, 12.0);
            if (closest != null && this.random.nextInt(7) == 0) {
                this.playerTarget = closest;
            } else {
                int i = (int) getX() + this.random.nextInt(7) - this.random.nextInt(7);
                int j = Math.max((int) getY() + this.random.nextInt(4) - this.random.nextInt(3), 1);
                int k = (int) getZ() + this.random.nextInt(7) - this.random.nextInt(7);
                int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING, i, k);
                j = Math.min(j, height + 8);
                this.currentFlightTarget = new BlockPos(i, j, k);
            }
        }
        if (this.currentFlightTarget != null) {
            double dx = this.currentFlightTarget.getX() + 0.5 - getX();
            double dy = this.currentFlightTarget.getY() + 0.5 - getY();
            double dz = this.currentFlightTarget.getZ() + 0.5 - getZ();
            Vec3 m = getDeltaMovement();
            setDeltaMovement(m.add((Math.signum(dx) * 0.5 - m.x) * 0.1, (Math.signum(dy) * 0.7 - m.y) * 0.1,
                    (Math.signum(dz) * 0.5 - m.z) * 0.1));
            this.zza = 0.2f;
        } else {
            setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return true;
    }

    /** One midge in the cloud, bobbing about its own height. */
    public final class Midge {
        public final float posX;
        public float posY;
        public final float posZ;
        public float prevPosY;
        public final float initialPosY;
        public final float rotation;
        private int midgeTick;
        private static final int MAX_MIDGE_TICK = 80;

        Midge() {
            this.posX = -1.0f + LOTRMidgesEntity.this.random.nextFloat() * 2.0f;
            this.initialPosY = this.posY = LOTRMidgesEntity.this.random.nextFloat() * 2.0f;
            this.prevPosY = this.posY;
            this.posZ = -1.0f + LOTRMidgesEntity.this.random.nextFloat() * 2.0f;
            this.rotation = LOTRMidgesEntity.this.random.nextFloat() * 360.0f;
            this.midgeTick = LOTRMidgesEntity.this.random.nextInt(MAX_MIDGE_TICK);
        }

        void update() {
            this.prevPosY = this.posY;
            if (++this.midgeTick > MAX_MIDGE_TICK) {
                this.midgeTick = 0;
            }
            this.posY = this.initialPosY + 0.5f * Mth.sin(this.midgeTick / Mth.TWO_PI);
        }
    }

    /** onDeath: shot down -- indirect damage -- by a hired unit. */
    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel && source.getDirectEntity() != source.getEntity()
                && source.getEntity() instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive
                && npc.hiredNPCInfo.getHiringPlayer() != null) {
            LOTRPlayerAchievements.addAchievement(npc.hiredNPCInfo.getHiringPlayer(), LOTRAchievement.SHOOT_DOWN_MIDGES);
        }
    }

    /** getCanSpawnHere: above sea level, on its biome's own top block. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        int i = Mth.floor(getX());
        int j = Mth.floor(getY());
        int k = Mth.floor(getZ());
        if (j < 62) {
            return false;
        }
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome biome =
                net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes.of(level.getBiome(new BlockPos(i, j, k)));
        return biome != null && level.getBlockState(new BlockPos(i, j - 1, k)).is(biome.topBlock.getBlock())
                && super.checkSpawnRules(level, reason);
    }
}
