package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAINPCMarry: a grown, unmarried NPC holding the people's ring
 * seeks the nearest one who can marry it within 16 blocks, walks to it
 * showing hearts and after three seconds, within three blocks, marries it:
 * the rings go on their heads, the names change, the number of children is
 * set, and whoever gave each ring gains alignment.
 *
 * <p>NOT ported: the marriage achievement.
 */
public class LOTRNPCMarryGoal extends Goal {

    private final LOTRNPCEntity npc;
    private final double moveSpeed;
    private @Nullable LOTRNPCEntity spouse;
    private int marryDelay;

    public LOTRNPCMarryGoal(LOTRNPCEntity npc, double speed) {
        this.npc = npc;
        this.moveSpeed = speed;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        var family = this.npc.familyInfo;
        if (this.npc.getClass() != family.marriageEntityClass || family.spouseUniqueID != null || family.getAge() != 0
                || !this.npc.getItemBySlot(EquipmentSlot.HEAD).isEmpty() || this.npc.getMainHandItem().isEmpty()) {
            return false;
        }
        LOTRNPCEntity best = null;
        double distanceSq = Double.MAX_VALUE;
        for (LOTRNPCEntity candidate : this.npc.level().getEntitiesOfClass(this.npc.getClass(),
                this.npc.getBoundingBox().inflate(16.0, 4.0, 16.0))) {
            if (!family.canMarryNPC(candidate) || !candidate.familyInfo.canMarryNPC(this.npc)) {
                continue;
            }
            double d = this.npc.distanceToSqr(candidate);
            if (d <= distanceSq) {
                distanceSq = d;
                best = candidate;
            }
        }
        this.spouse = best;
        return best != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.spouse != null && this.spouse.isAlive() && this.npc.familyInfo.canMarryNPC(this.spouse)
                && this.spouse.familyInfo.canMarryNPC(this.npc);
    }

    @Override
    public void stop() {
        this.spouse = null;
        this.marryDelay = 0;
    }

    @Override
    public void tick() {
        this.npc.getLookControl().setLookAt(this.spouse, 10.0f, this.npc.getMaxHeadXRot());
        this.npc.getNavigation().moveTo(this.spouse, this.moveSpeed);
        ++this.marryDelay;
        if (this.marryDelay % 20 == 0) {
            this.npc.spawnHearts();
        }
        if (this.marryDelay >= 60 && this.npc.distanceToSqr(this.spouse) < 9.0) {
            marry(this.spouse);
        }
    }

    private void marry(LOTRNPCEntity spouse) {
        this.npc.familyInfo.spouseUniqueID = spouse.getUUID();
        spouse.familyInfo.spouseUniqueID = this.npc.getUUID();
        this.npc.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.npc.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.npc.familyInfo.marriageRing));
        spouse.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        spouse.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.npc.familyInfo.marriageRing));
        this.npc.changeNPCNameForMarriage(spouse);
        spouse.changeNPCNameForMarriage(this.npc);
        int maxChildren = this.npc.familyInfo.getRandomMaxChildren();
        this.npc.familyInfo.maxChildren = maxChildren;
        spouse.familyInfo.maxChildren = maxChildren;
        this.npc.familyInfo.setMaxBreedingDelay();
        spouse.familyInfo.setMaxBreedingDelay();
        this.npc.spawnHearts();
        spouse.spawnHearts();
        rewardRingGiver(this.npc);
        rewardRingGiver(spouse);
        if (this.npc.level() instanceof ServerLevel level) {
            ExperienceOrb.award(level, this.npc.position(), this.npc.getRandom().nextInt(8) + 2);
        }
    }

    private static void rewardRingGiver(LOTRNPCEntity npc) {
        Player player = npc.familyInfo.getRingGivingPlayer();
        if (player != null) {
            LOTRPlayerAlignments.addAlignment(player, LOTRAlignmentValues.MARRIAGE_BONUS, npc.getFaction());
        }
    }
}
