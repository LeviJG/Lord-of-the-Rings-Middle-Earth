package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohanManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * The NPC half of LOTREventHandler.onLivingDeath: when a player kills a LOTR
 * NPC, the kill's alignment bonus (or penalty) goes to the factions that care
 * (LOTRPlayerAlignments.addAlignment), a pledge to the victim's side or an
 * ally is strained, and the victim's friends within eight blocks turn on the
 * player -- up to five of them saying so.
 *
 * <p>NOT ported yet: credit for kills by hired units (D9b), the faction kill
 * counters and bounties (LOTRFactionData, LOTRFactionBounties -- D7/D14),
 * mini-quest kills (D14), and entities registered through LOTREntityRegistry's
 * config (D6).
 */
public final class LOTRNPCKillEvents {

    private LOTRNPCKillEvents() {
    }

    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register(LOTRNPCKillEvents::onLivingDeath);
    }

    private static void onLivingDeath(LivingEntity entity, DamageSource source) {
        if (!(entity.level() instanceof ServerLevel level) || !(source.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (entity.getType() == LOTREntities.HORSE && !player.isCreative()) {
            avengeHorse(level, player);
        }
        LOTRFaction entityFaction = LOTRNearestAttackableTargetGoal.factionOf(entity);
        if (entity instanceof LOTRNPCEntity npc) {
            LOTRAlignmentValues.AlignmentBonus bonus = new LOTRAlignmentValues.AlignmentBonus(npc.getAlignmentBonus(),
                    npc.getType().getDescriptionId());
            bonus.needsTranslation = true;
            bonus.isCivilianKill = npc.isCivilianNPC();
            if (bonus.bonus != 0.0f) {
                bonus.isKill = true;
                LOTRPlayerAlignments.addAlignment(player, bonus, entityFaction);
            }
        }
        if (entityFaction.allowPlayer) {
            LOTRFaction pledge = LOTRPlayerAlignments.get(player).pledgeFaction();
            if (pledge != null && (pledge == entityFaction || pledge.isAlly(entityFaction))) {
                LOTRPlayerAlignments.onPledgeKill(player);
            }
        }
        if (!player.isCreative() && entityFaction != LOTRFaction.UNALIGNED) {
            int sentSpeeches = 0;
            double range = 8.0;
            List<Mob> allies = level.getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(range),
                    m -> m.isAlive() && LOTRNearestAttackableTargetGoal.factionOf(m).isGoodRelation(entityFaction));
            for (Mob ally : allies) {
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
    private static void avengeHorse(ServerLevel level, ServerPlayer player) {
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
                LOTRPlayerAlignments.addAlignment(player, LOTRAlignmentValues.ROHAN_HORSE_PENALTY, LOTRFaction.ROHAN);
                penalty = true;
            }
        }
    }
}
