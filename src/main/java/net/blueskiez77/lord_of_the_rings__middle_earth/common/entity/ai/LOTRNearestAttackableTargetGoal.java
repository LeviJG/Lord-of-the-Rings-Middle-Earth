package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntityRegistry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBanditEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCRideableEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAINearestAttackableTargetBasic with LOTRNPCTargetSelector: an NPC
 * goes for players whose alignment with its faction is below zero, and for
 * any creature of a faction it is at war with -- sparing civilians unless its
 * faction approves of war crimes, and (for neutral factions) sparing all
 * unless its hiring player is their enemy. It prefers near targets that its
 * fellows are not already fighting. A tamed mount, or one a player rides,
 * looks for no one.
 */
public class LOTRNearestAttackableTargetGoal extends TargetGoal {

    private static final TargetingConditions IN_SIGHT = TargetingConditions.forCombat();
    /** Without checkSight, a target need not be in view. */
    private static final TargetingConditions UNSEEN = TargetingConditions.forCombat().ignoreLineOfSight();

    private final Class<? extends LivingEntity> targetClass;
    private final int targetChance;
    private final Predicate<LivingEntity> selector;
    private @Nullable LivingEntity targetEntity;

    public LOTRNearestAttackableTargetGoal(PathfinderMob mob, Class<? extends LivingEntity> targetClass, int chance,
                                          boolean checkSight, @Nullable Predicate<LivingEntity> selector) {
        super(mob, checkSight, false);
        this.targetClass = targetClass;
        this.targetChance = chance;
        this.selector = selector == null ? e -> true : selector;
        setFlags(java.util.EnumSet.of(Flag.TARGET));
    }

    /** addTargetTasks(entity, index, c): one goal for players... */
    public static LOTRNearestAttackableTargetGoal forPlayers(PathfinderMob mob) {
        return new LOTRNearestAttackableTargetGoal(mob, Player.class, 0, true, null);
    }

    /** ...and one for creatures of hostile factions. */
    public static LOTRNearestAttackableTargetGoal forFactions(PathfinderMob mob) {
        return new LOTRNearestAttackableTargetGoal(mob, Mob.class, 0, true, target -> isFactionTarget(mob, target));
    }

    /** LOTRMod.getNPCFaction: an NPC's own, a creature's from LOTREntityRegistry, or none. */
    public static LOTRFaction factionOf(@Nullable Entity entity) {
        if (entity instanceof LOTRNPCEntity npc) {
            return npc.getFaction();
        }
        LOTREntityRegistry.RegistryInfo info = LOTREntityRegistry.get(entity);
        return info != null ? info.alignmentFaction() : LOTRFaction.UNALIGNED;
    }

    /** LOTRNPCTargetSelector.isEntityApplicable. */
    public static boolean isFactionTarget(Mob owner, LivingEntity target) {
        LOTRFaction ownerFaction = factionOf(owner);
        if (ownerFaction == LOTRFaction.HOSTILE && (target.getClass().isAssignableFrom(owner.getClass())
                || owner.getClass().isAssignableFrom(target.getClass()))) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        if (target instanceof LOTRNPCEntity npc) {
            if (!npc.canBeFreelyTargetedBy(owner)) {
                return false;
            }
            if (!ownerFaction.approvesWarCrimes && npc.isCivilianNPC()) {
                return false;
            }
        }
        LOTRFaction targetFaction = factionOf(target);
        if (ownerFaction.isBadRelation(targetFaction)) {
            return true;
        }
        // A neutral faction's creature: only a hired unit goes for it, if its player is that faction's enemy.
        if (ownerFaction.isNeutral(targetFaction) && owner instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive) {
            Player hiringPlayer = npc.hiredNPCInfo.getHiringPlayer();
            return hiringPlayer != null && LOTRPlayerAlignments.getAlignment(hiringPlayer, targetFaction) < 0.0f;
        }
        return false;
    }

    protected boolean isPlayerSuitableTarget(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, factionOf(this.mob)) < 0.0f;
    }

    private boolean isSuitable(LivingEntity target) {
        if (target == this.mob.getVehicle() || target == this.mob.getFirstPassenger()) {
            return false;
        }
        if (!this.selector.test(target) || !canAttack(target, this.mustSee ? IN_SIGHT : UNSEEN)) {
            return false;
        }
        if (target instanceof Player player) {
            return isPlayerSuitableTarget(player);
        }
        // Bandits are left to the hired units, which hunt them.
        if (target instanceof LOTRBanditEntity) {
            return this.mob instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive;
        }
        return true;
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        }
        // A halted hired unit, or a child, looks for no one; nor does a tamed mount, or one a player rides.
        if (this.mob instanceof LOTRNPCEntity npc
                && (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.isHalted() || npc.isBaby())) {
            return false;
        }
        if (this.mob instanceof LOTRNPCRideableEntity mount
                && (mount.isNPCTamed() || mount.getFirstPassenger() instanceof Player)) {
            return false;
        }
        double range = getFollowDistance();
        double rangeY = Math.min(range, 8.0);
        AABB box = this.mob.getBoundingBox().inflate(range, rangeY, range);
        List<? extends LivingEntity> entities = this.mob.level().getEntitiesOfClass(this.targetClass, box, this::isSuitable);
        if (entities.isEmpty()) {
            return false;
        }
        this.targetEntity = entities.stream().min(Comparator.comparingDouble(this::distanceMetricSq)).orElse(null);
        return this.targetEntity != null;
    }

    private double distanceMetricSq(LivingEntity target) {
        return targetSortMetric(this.mob, target);
    }

    /** TargetSorter: distance over 12 blocks, squared, plus the square of how many fellows already fight it. */
    public static double targetSortMetric(Mob owner, LivingEntity target) {
        double dSq = owner.distanceToSqr(target) / 144.0;
        int dupes = 0;
        for (LOTRNPCEntity nearby : owner.level().getEntitiesOfClass(LOTRNPCEntity.class,
                owner.getBoundingBox().inflate(8.0))) {
            if (nearby != owner && nearby.isAlive() && nearby.getTarget() == target) {
                ++dupes;
            }
        }
        return dSq + dupes * dupes;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }
}
