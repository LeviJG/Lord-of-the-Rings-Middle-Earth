package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.goal.Goal;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAINPCMate: a married couple, both grown and past their breeding
 * delay, with children still to have, meet up within 16 blocks and, three
 * seconds of hearts later, have a child who stays in the world for good.
 */
public class LOTRNPCMateGoal extends Goal {

    private final LOTRNPCEntity npc;
    private final double moveSpeed;
    private @Nullable LOTRNPCEntity spouse;
    private int spawnBabyDelay;

    public LOTRNPCMateGoal(LOTRNPCEntity npc, double speed) {
        this.npc = npc;
        this.moveSpeed = speed;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        var family = this.npc.familyInfo;
        if (this.npc.getClass() != family.marriageEntityClass || family.spouseUniqueID == null
                || family.children >= family.maxChildren || family.getAge() != 0) {
            return false;
        }
        this.spouse = family.getSpouse();
        return this.spouse != null && this.npc.distanceTo(this.spouse) < 16.0
                && this.spouse.familyInfo.children < this.spouse.familyInfo.maxChildren
                && this.spouse.familyInfo.getAge() == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.spouse.isAlive() && this.spawnBabyDelay < 60 && this.npc.familyInfo.getAge() == 0
                && this.spouse.familyInfo.getAge() == 0;
    }

    @Override
    public void stop() {
        this.spouse = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void tick() {
        this.npc.getLookControl().setLookAt(this.spouse, 10.0f, this.npc.getMaxHeadXRot());
        this.npc.getNavigation().moveTo(this.spouse, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay % 20 == 0) {
            this.npc.spawnHearts();
        }
        if (this.spawnBabyDelay >= 60 && this.npc.distanceToSqr(this.spouse) < 9.0) {
            spawnBaby(this.spouse);
        }
    }

    private void spawnBaby(LOTRNPCEntity spouse) {
        if (!(this.npc.level() instanceof ServerLevel level)
                || !(this.npc.getType().create(level, EntitySpawnReason.BREEDING) instanceof LOTRNPCEntity baby)) {
            return;
        }
        LOTRNPCEntity maleParent = this.npc.familyInfo.isMale() ? this.npc : spouse;
        LOTRNPCEntity femaleParent = this.npc.familyInfo.isMale() ? spouse : this.npc;
        baby.familyInfo.setChild();
        baby.familyInfo.setMale(baby.getRandom().nextBoolean());
        baby.familyInfo.maleParentID = maleParent.getUUID();
        baby.familyInfo.femaleParentID = femaleParent.getUUID();
        baby.createNPCChildName(maleParent, femaleParent);
        baby.snapTo(this.npc.getX(), this.npc.getY(), this.npc.getZ(), 0.0f, 0.0f);
        baby.finalizeSpawn(level, level.getCurrentDifficultyAt(baby.blockPosition()), EntitySpawnReason.BREEDING, null);
        baby.isNPCPersistent = true;
        level.addFreshEntity(baby);
        this.npc.familyInfo.setMaxBreedingDelay();
        spouse.familyInfo.setMaxBreedingDelay();
        ++this.npc.familyInfo.children;
        ++spouse.familyInfo.children;
        this.npc.spawnHearts();
        spouse.spawnHearts();
    }
}
