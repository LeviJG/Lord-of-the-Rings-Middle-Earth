package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRCrossbowBoltEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntityRegistry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrowingAxeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRButterflyEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohanManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTROlogHaiEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargBombardierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuests;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
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
 * <p>A kill in the faction's own land is remembered towards a bounty on the
 * killer (LOTRFactionBounties). The player's own kills count towards their
 * mini-quests, and a slain player's quests learn who slew them.
 *
 * <p>The kill's achievements: slaying a butterfly; and, slaying a foe, a hired warg bombardier's or
 * olog-hai's kill, or the player's own while drunk, of a bombardier still holding its bomb, with a
 * crossbow bolt or with a dwarven throwing axe. useSpearFromFar cannot be earned: spears keep the
 * vanilla spear's mechanics and are never thrown.
 *
 * <p>Mini-quests were not told of kills during a siege, which only other mods'
 * messages ever began; there is never one here.
 *
 * <p>NOT ported yet: entities registered through LOTREntityRegistry's own
 * file, LOTR_EntityRegistry.txt (on the deferred-port tracker).
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
        killAchievements(entity, source);
    }

    private static void killAchievements(LivingEntity entity, DamageSource source) {
        if (entity instanceof LOTRButterflyEntity && source.getEntity() instanceof Player killer) {
            LOTRPlayerAchievements.addAchievement(killer, LOTRAchievement.KILL_BUTTERFLY);
        }
        Player attackingPlayer = null;
        LOTRNPCEntity attackingHiredUnit = null;
        if (source.getEntity() instanceof Player directPlayer) {
            attackingPlayer = directPlayer;
        } else if (source.getEntity() instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive
                && npc.hiredNPCInfo.getHiringPlayer() != null) {
            attackingPlayer = npc.hiredNPCInfo.getHiringPlayer();
            attackingHiredUnit = npc;
        }
        if (attackingPlayer == null
                || LOTRPlayerAlignments.getAlignment(attackingPlayer, LOTRNearestAttackableTargetGoal.factionOf(entity)) >= 0.0f) {
            return;
        }
        if (attackingHiredUnit != null) {
            if (attackingHiredUnit instanceof LOTRWargBombardierEntity) {
                LOTRPlayerAchievements.addAchievement(attackingPlayer, LOTRAchievement.HIRE_WARG_BOMBARDIER);
            }
            if (attackingHiredUnit instanceof LOTROlogHaiEntity) {
                LOTRPlayerAchievements.addAchievement(attackingPlayer, LOTRAchievement.HIRE_OLOG_HAI);
            }
            return;
        }
        if (attackingPlayer.hasEffect(MobEffects.NAUSEA)) {
            LOTRPlayerAchievements.addAchievement(attackingPlayer, LOTRAchievement.KILL_WHILE_DRUNK);
        }
        if (entity instanceof LOTROrcEntity orc && orc.isOrcBombardier() && !orc.npcItemsInv.getBomb().isEmpty()) {
            LOTRPlayerAchievements.addAchievement(attackingPlayer, LOTRAchievement.KILL_BOMBARDIER);
        }
        if (source.getDirectEntity() instanceof LOTRCrossbowBoltEntity) {
            LOTRPlayerAchievements.addAchievement(attackingPlayer, LOTRAchievement.USE_CROSSBOW);
        }
        if (source.getDirectEntity() instanceof LOTRThrowingAxeEntity axe
                && axe.getWeaponItem().is(LOTRCombatItems.DWARVEN_THROWING_AXE)) {
            LOTRPlayerAchievements.addAchievement(attackingPlayer, LOTRAchievement.USE_DWARVEN_THROWING_AXE);
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
        } else if (!wasSelfDefenceAgainstAlliedUnit && LOTREntityRegistry.get(entity) instanceof LOTREntityRegistry.RegistryInfo info) {
            LOTRAlignmentValues.AlignmentBonus bonus = info.alignmentBonus(entity);
            if (bonus.bonus != 0.0f && (!creditHiredUnit || byNearbyUnit)) {
                bonus.isKill = true;
                bonus.killByHiredUnit = creditHiredUnit;
                LOTRPlayerAlignments.addAlignment(player, bonus, entityFaction, List.of(), entity);
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
            // A kill in the faction's own land counts towards a bounty on the killer.
            if (!player.isCreative() && entityFaction.inDefinedControlZone(player,
                    Math.max(entityFaction.getControlZoneReducedRange(), 50))) {
                net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionBounties.forFaction(entityFaction)
                        .forPlayer(player).recordNewKill();
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
        // The player's mini-quests count the kill; a slain player's own quests learn who slew them.
        LOTRMiniQuests.onKill(player, entity);
        if (entity instanceof Player slainPlayer) {
            for (LOTRMiniQuest quest : new ArrayList<>(LOTRMiniQuests.forPlayer(slainPlayer.getUUID()).getMiniQuests())) {
                quest.onKilledByPlayer(slainPlayer, player);
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
