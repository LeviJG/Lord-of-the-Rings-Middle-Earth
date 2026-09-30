package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCMount;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRPlayerNPCOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMod.canPlayerAttackEntity and canNPCAttackEntity, and the
 * LivingAttackEvent handler that enforces them (LOTREventHandler
 * onLivingAttacked).
 *
 * <p>A player may not hurt their own hired units, nor -- with Friendly Fire
 * off, as it starts -- any NPC whose faction likes them and which is not
 * fighting them. An NPC may not hurt its own player or that player's other
 * units of a friendly faction, nor anything of a faction it is friendly with
 * unless it is already fighting it; and unless a player ordered it, not a
 * player with any standing with its faction who is not its target.
 *
 * <p>NOT ported yet: fellowships' PVP and hired-unit friendly-fire
 * protections and siege mode (D14), the one-time Friendly Fire notice screen
 * (LOTRGuiMessageTypes.FRIENDLY_FIRE, D16), and NPCs registered from other
 * mods' config (LOTREntityRegistry).
 */
public final class LOTRAttackRules {

    private LOTRAttackRules() {
    }

    public static void init() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            LivingEntity attacker = source.getEntity() instanceof LivingEntity living ? living : null;
            if (entity instanceof Mob mount && mount.getFirstPassenger() != null && attacker == mount.getFirstPassenger()
                    && isNPCMount(mount)) {
                return false;
            }
            if (attacker instanceof Player player && !canPlayerAttackEntity(player, entity, true)) {
                return false;
            }
            return !(attacker instanceof PathfinderMob creature) || canNPCAttackEntity(creature, entity, false);
        });
    }

    /** A mount's rider's blows never land on it. */
    private static boolean isNPCMount(Mob mob) {
        return mob instanceof LOTRNPCMount;
    }

    public static boolean canPlayerAttackEntity(Player attacker, LivingEntity target) {
        return canPlayerAttackEntity(attacker, target, false);
    }

    public static boolean canPlayerAttackEntity(Player attacker, @Nullable LivingEntity target, boolean warnFriendlyFire) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        boolean friendlyFire = false;
        Entity targetNPC = null;
        if (factionOf(target) != LOTRFaction.UNALIGNED) {
            targetNPC = target;
        } else if (factionOf(target.getFirstPassenger()) != LOTRFaction.UNALIGNED) {
            targetNPC = target.getFirstPassenger();
        }
        if (targetNPC != null) {
            LOTRFaction targetNPCFaction = factionOf(targetNPC);
            if (targetNPC instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive
                    && npc.hiredNPCInfo.getHiringPlayer() == attacker) {
                return false;
            }
            if (targetNPC instanceof Mob mob && mob.getTarget() != attacker
                    && LOTRPlayerAlignments.getAlignment(attacker, targetNPCFaction) > 0.0f) {
                friendlyFire = true;
            }
        }
        return !friendlyFire || LOTRPlayerNPCOptions.getFriendlyFire(attacker);
    }

    public static boolean canNPCAttackEntity(PathfinderMob attacker, LivingEntity target) {
        return canNPCAttackEntity(attacker, target, false);
    }

    public static boolean canNPCAttackEntity(PathfinderMob attacker, @Nullable LivingEntity target, boolean isPlayerDirected) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        LOTRFaction attackerFaction = factionOf(attacker);
        if (attacker instanceof LOTRNPCEntity npc) {
            Player hiringPlayer = npc.hiredNPCInfo.getHiringPlayer();
            if (hiringPlayer != null) {
                if (target == hiringPlayer || target.getFirstPassenger() == hiringPlayer) {
                    return false;
                }
                LOTRNPCEntity targetNPC = target instanceof LOTRNPCEntity t ? t
                        : target.getFirstPassenger() instanceof LOTRNPCEntity r ? r : null;
                if (targetNPC != null && targetNPC.hiredNPCInfo.isActive) {
                    UUID hiringPlayerUUID = npc.hiredNPCInfo.getHiringPlayerUUID();
                    if (hiringPlayerUUID != null && hiringPlayerUUID.equals(targetNPC.hiredNPCInfo.getHiringPlayerUUID())
                            && !attackerFaction.isBadRelation(factionOf(targetNPC))) {
                        return false;
                    }
                }
            }
        }
        if (attackerFaction.allowEntityRegistry) {
            LivingEntity currentTarget = attacker.getTarget();
            if (attackerFaction.isGoodRelation(factionOf(target)) && currentTarget != target) {
                return false;
            }
            Entity rider = target.getFirstPassenger();
            if (rider != null && attackerFaction.isGoodRelation(factionOf(rider)) && currentTarget != target
                    && currentTarget != rider) {
                return false;
            }
            if (!isPlayerDirected) {
                if (target instanceof Player player && LOTRPlayerAlignments.getAlignment(player, attackerFaction) >= 0.0f
                        && currentTarget != target) {
                    return false;
                }
                return !(rider instanceof Player riderPlayer)
                        || LOTRPlayerAlignments.getAlignment(riderPlayer, attackerFaction) < 0.0f
                        || currentTarget == target || currentTarget == rider;
            }
        }
        return true;
    }

    private static LOTRFaction factionOf(@Nullable Entity entity) {
        return LOTRNearestAttackableTargetGoal.factionOf(entity);
    }

    /** A mount's rider may strike the target: a player or a creature, by the rules above. */
    public static boolean riderCanAttack(Object rider, LivingEntity target) {
        if (rider instanceof Player player) {
            return canPlayerAttackEntity(player, target, false);
        }
        if (rider instanceof PathfinderMob mob) {
            return canNPCAttackEntity(mob, target, false);
        }
        return false;
    }
}
