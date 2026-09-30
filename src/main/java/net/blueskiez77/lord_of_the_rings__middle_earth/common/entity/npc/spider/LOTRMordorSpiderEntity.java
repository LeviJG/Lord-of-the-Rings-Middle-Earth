package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMordorSpider: a spider of Mordor, sized 1 to 3, always poisonous.
 * One time in three it spawns with a Mordor orc or orc archer on its back --
 * but not when hired.
 *
 * <p>NOT ported yet: the killMordorSpider achievement (D7).
 */
public class LOTRMordorSpiderEntity extends LOTRSpiderEntity {

    private boolean forHire;

    public LOTRMordorSpiderEntity(EntityType<? extends LOTRMordorSpiderEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected int getRandomSpiderScale() {
        return 1 + this.random.nextInt(3);
    }

    @Override
    protected int getRandomSpiderType() {
        return VENOM_POISON;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.MORDOR;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    /** initCreatureForHire: spawned as usual, only without a rider. */
    @Override
    public void initCreatureForHire(ServerLevel level) {
        this.forHire = true;
        super.initCreatureForHire(level);
        this.forHire = false;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, groupData);
        if (!this.forHire && this.random.nextInt(3) == 0) {
            ServerLevel level = accessor.getLevel();
            LOTRNPCEntity rider = (this.random.nextBoolean() ? LOTREntities.MORDOR_ORC_ARCHER : LOTREntities.MORDOR_ORC)
                    .create(level, EntitySpawnReason.JOCKEY);
            if (rider != null) {
                rider.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
                rider.finalizeSpawn(accessor, difficulty, EntitySpawnReason.JOCKEY, null);
                rider.isNPCPersistent = this.isNPCPersistent;
                level.addFreshEntity(rider);
                rider.startRiding(this, true, false);
            }
        }
        return data;
    }
}
