package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohanManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

/**
 * The NPC half of LOTREventHandler.onLivingDeath. A kill is the player's if
 * they dealt it, if they have the credit for it (as one who struck the
 * victim before it fell or burned to death), or if one of their hired units
 * dealt it.
 *
 * <p>The kill's alignment bonus (or penalty) goes to the factions that care
 * (LOTRPlayerAlignments.addAlignment) -- for a hired unit's kill, only if
 * the unit was within 64 blocks of its player, and only the kill's bonus,
 * never its penalties. Nothing at all is taken from a friend of the victim's
 * faction who slew a hired unit that its player had set on them.
 *
 * <p>For the player's own kill, it is counted against the victim's faction
 * and as an enemy kill for each faction that rewards it (LOTRFactionData), a
 * pledge to the victim's side or an ally is strained, and the victim's
 * friends within eight blocks turn on the player -- up to five of them
 * saying so -- save hired units: the player's own, and any other while the
 * player still stands well with the victim's faction.
 *
 * <p>NOT ported yet: bounties (LOTRFactionBounties, D14), mini-quest kills
 * (D14), the kill achievements (D7), and entities registered through
 * LOTREntityRegistry's own file, LOTR_EntityRegistry.txt (on the
 * deferred-port tracker).
 */
public final class LOTRNPCKillEvents {

    private LOTRNPCKillEvents() {
    }

    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register(LOTRNPCKillEvents::onLivingDeath);
    }

    private static void onLivingDeath(LivingEntity entity, DamageSource source) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        Player player = null;
        boolean creditHiredUnit = false;
        boolean byNearbyUnit = false;
        if (source.getEntity() instanceof Player directPlayer) {
            player = directPlayer;
        } else if (entity.getKillCredit() instanceof Player creditedPlayer) {
            player = creditedPlayer;
        } else if (source.getEntity() instanceof LOTRNPCEntity unit && unit.hiredNPCInfo.isActive
                && unit.hiredNPCInfo.getHiringPlayer() != null) {
            player = unit.hiredNPCInfo.getHiringPlayer();
            creditHiredUnit = true;
            byNearbyUnit = unit.distanceToSqr(player) <= 64.0 * 64.0;
        }
        if (player != null) {
            onKilledByPlayer(level, entity, player, creditHiredUnit, byNearbyUnit);
        }
        if (entity.getType() == LOTREntities.HORSE && source.getEntity() instanceof Player killer && !killer.isCreative()) {
            avengeHorse(level, killer, entity);
        }
    }

    private static void onKilledByPlayer(ServerLevel level, LivingEntity entity, Player player, boolean creditHiredUnit,
                                         boolean byNearbyUnit) {
        LOTRFaction entityFaction = LOTRNearestAttackableTargetGoal.factionOf(entity);
        float prevAlignment = LOTRPlayerAlignments.getAlignment(player, entityFaction);
        boolean wasSelfDefenceAgainstAlliedUnit = !creditHiredUnit && prevAlignment > 0.0f
                && entity instanceof LOTRNPCEntity victim && victim.hiredNPCInfo.isActive
                && victim.hiredNPCInfo.wasAttackCommanded;
        if (!wasSelfDefenceAgainstAlliedUnit && entity instanceof LOTRNPCEntity npc) {
            LOTRAlignmentValues.AlignmentBonus bonus = new LOTRAlignmentValues.AlignmentBonus(npc.getAlignmentBonus(),
                    npc.getType().getDescriptionId());
            bonus.needsTranslation = true;
            bonus.isCivilianKill = npc.isCivilianNPC();
            if (bonus.bonus != 0.0f && (!creditHiredUnit || byNearbyUnit)) {
                bonus.isKill = true;
                bonus.killByHiredUnit = creditHiredUnit;
                LOTRPlayerAlignments.addAlignment(player, bonus, entityFaction, npc.killBonusFactions, entity);
            }
        }
        if (creditHiredUnit) {
            return;
        }
        if (entityFaction.allowPlayer) {
            LOTRFactionData.addNPCKill(player, entityFaction);
            for (LOTRFaction enemy : entityFaction.getBonusesForKilling()) {
                LOTRFactionData.addEnemyKill(player, enemy);
            }
            LOTRFaction pledge = LOTRPlayerAlignments.get(player).pledgeFaction();
            if (pledge != null && (pledge == entityFaction || pledge.isAlly(entityFaction))
                    && player instanceof ServerPlayer serverPlayer) {
                LOTRPlayerAlignments.onPledgeKill(serverPlayer);
            }
        }
        float newAlignment = LOTRPlayerAlignments.getAlignment(player, entityFaction);
        if (!wasSelfDefenceAgainstAlliedUnit && !player.isCreative() && entityFaction != LOTRFaction.UNALIGNED) {
            int sentSpeeches = 0;
            double range = 8.0;
            List<Mob> allies = level.getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(range),
                    m -> m.isAlive() && LOTRNearestAttackableTargetGoal.factionOf(m).isGoodRelation(entityFaction));
            for (Mob ally : allies) {
                if (ally instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive
                        && (newAlignment > 0.0f || npc.hiredNPCInfo.getHiringPlayer() == player)) {
                    continue;
                }
                if (ally.getTarget() != null) {
                    continue;
                }
                ally.setTarget(player);
                String speech;
                if (ally instanceof LOTRNPCEntity npc && sentSpeeches < 5 && (speech = npc.getSpeechBank(player)) != null
                        && npc.distanceToSqr(player) < range) {
                    npc.sendSpeechBank(player, speech);
                    ++sentSpeeches;
                }
            }
        }
    }

    /**
     * A player who kills a horse (LOTREntityHorse itself, not its kin) within
     * sixteen blocks of Rohirrim is set upon by all of them but their own
     * hired men; one tells them why, and they lose a point with Rohan.
     */
    private static void avengeHorse(ServerLevel level, Player player, LivingEntity horse) {
        List<LOTRRohanManEntity> rohirrimList = level.getEntitiesOfClass(LOTRRohanManEntity.class,
                player.getBoundingBox().inflate(16.0));
        boolean sentMessage = false;
        boolean penalty = false;
        for (LOTRRohanManEntity rohirrim : rohirrimList) {
            if (rohirrim.hiredNPCInfo.isActive && rohirrim.hiredNPCInfo.getHiringPlayer() == player) {
                continue;
            }
            rohirrim.setTarget(player);
            if (!sentMessage) {
                rohirrim.sendSpeechBank(player, "rohan/warrior/avengeHorse");
                sentMessage = true;
            }
            if (!penalty) {
                LOTRPlayerAlignments.addAlignment(player, LOTRAlignmentValues.ROHAN_HORSE_PENALTY, LOTRFaction.ROHAN, horse);
                penalty = true;
            }
        }
    }
}
