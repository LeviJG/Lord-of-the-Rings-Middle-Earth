package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBrokenPledgePayload;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * The alignment and pledge half of LOTRPlayerData. Alignments, the pledge and
 * the hide flag live in an immutable, persistent attachment synced to every
 * player tracking this one (sendAlignmentToAllPlayersInWorld: others' alignment
 * shows above their heads); the pledge timers in a mutable, unsynced one.
 * Both survive death, as the original's player data did.
 */
public final class LOTRPlayerAlignments {
    public static final AttachmentType<LOTRAlignmentData> ALIGNMENT_DATA =
            AttachmentRegistry.create(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_data"),
                    builder -> builder
                            .initializer(() -> LOTRAlignmentData.EMPTY)
                            .persistent(LOTRAlignmentData.CODEC)
                            .syncWith(LOTRAlignmentData.STREAM_CODEC, AttachmentSyncPredicate.all())
                            .copyOnDeath());

    public static final AttachmentType<LOTRPledgeCooldowns> PLEDGE_COOLDOWNS =
            AttachmentRegistry.create(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pledge_cooldowns"),
                    builder -> builder
                            .initializer(LOTRPledgeCooldowns::new)
                            .persistent(LOTRPledgeCooldowns.CODEC)
                            .copyOnDeath());

    /** One day: a second pledge-faction kill inside it breaks the pledge. */
    private static final int PLEDGE_KILL_COOLDOWN = 24000;

    private LOTRPlayerAlignments() {
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                tick(player, server.getTickCount());
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sendBrokenPledge(handler.player));
    }

    private static LOTRPledgeCooldowns cooldowns(Player player) {
        return player.getAttachedOrCreate(PLEDGE_COOLDOWNS);
    }

    /** onUpdate's share: handlePledgeCooldowns, then runAlignmentDraining. */
    private static void tick(ServerPlayer player, int tick) {
        LOTRPledgeCooldowns cd = cooldowns(player);
        if (cd.killCooldown > 0) {
            --cd.killCooldown;
        }
        if (cd.breakCooldown > 0) {
            setPledgeBreakCooldown(player, cd.breakCooldown - 1);
        }
        if (tick % 1000 == 0) {
            runAlignmentDraining(player);
        }
    }

    /**
     * runAlignmentDraining: holding positive alignment with two mortal enemies
     * costs both five points (never below zero) every thousand ticks. The
     * original's config switch for it defaulted on; the port has no config.
     * Its HUD notice (LOTRPacketAlignDrain) belongs to the alignment bar.
     */
    private static void runAlignmentDraining(ServerPlayer player) {
        java.util.List<LOTRFaction> drainFactions = new java.util.ArrayList<>();
        java.util.List<LOTRFaction> allFacs = LOTRFaction.getPlayableAlignmentFactions();
        for (LOTRFaction fac1 : allFacs) {
            for (LOTRFaction fac2 : allFacs) {
                if (!fac1.isMortalEnemy(fac2)
                        || getAlignment(player, fac1) <= 0.0f || getAlignment(player, fac2) <= 0.0f) {
                    continue;
                }
                if (!drainFactions.contains(fac1)) {
                    drainFactions.add(fac1);
                }
                if (!drainFactions.contains(fac2)) {
                    drainFactions.add(fac2);
                }
            }
        }
        for (LOTRFaction fac : drainFactions) {
            float align = getAlignment(player, fac);
            setAlignment(player, fac, align - Math.min(5.0f, align));
        }
    }

    public static LOTRAlignmentData get(Player player) {
        return player.getAttachedOrCreate(ALIGNMENT_DATA);
    }

    public static float getAlignment(Player player, LOTRFaction faction) {
        if (faction.hasFixedAlignment) {
            return faction.fixedAlignment;
        }
        return get(player).getAlignment(faction);
    }

    /** setAlignment: and a pledge the new figures no longer allow is broken. */
    public static void setAlignment(Player player, LOTRFaction faction, float value) {
        if (faction.isPlayableAlignmentFaction()) {
            player.setAttached(ALIGNMENT_DATA, get(player).withAlignment(faction, value));
        }
        LOTRFaction pledge = get(player).pledgeFaction();
        if (player instanceof ServerPlayer serverPlayer && pledge != null && !canPledgeTo(player, pledge)) {
            revokePledgeFaction(serverPlayer, false);
        }
    }

    public static void addAlignment(Player player, LOTRFaction faction, float delta) {
        setAlignment(player, faction, getAlignment(player, faction) + delta);
    }

    public static LOTRAlignmentBonusMap addAlignment(Player player,
                                                     LOTRAlignmentValues.AlignmentBonus source,
                                                     LOTRFaction faction) {
        float bonus = source.bonus;
        LOTRAlignmentBonusMap factionBonusMap = new LOTRAlignmentBonusMap();

        if (source.isKill) {
            for (LOTRFaction bonusFaction : faction.getBonusesForKilling()) {
                if (!bonusFaction.isPlayableAlignmentFaction()
                        || (!bonusFaction.approvesWarCrimes && source.isCivilianKill)) {
                    continue;
                }
                if (!source.killByHiredUnit) {
                    float mplier = bonusFaction.getControlZoneAlignmentMultiplier(player);
                    if (mplier > 0.0f) {
                        float alignment = getAlignment(player, bonusFaction);
                        float factionBonus = Math.abs(bonus) * mplier;
                        if (alignment >= bonusFaction.getPledgeAlignment() && !isPledgedTo(player, bonusFaction)) {
                            factionBonus *= 0.5f;
                        }
                        factionBonus = checkBonusForPledgeEnemyLimit(player, bonusFaction, factionBonus);
                        setAlignment(player, bonusFaction, alignment + factionBonus);
                        factionBonusMap.put(bonusFaction, factionBonus);
                    }
                }
            }
            for (LOTRFaction penaltyFaction : faction.getPenaltiesForKilling()) {
                if (!penaltyFaction.isPlayableAlignmentFaction() || source.killByHiredUnit) {
                    continue;
                }
                float mplier = penaltyFaction == faction ? 1.0f : penaltyFaction.getControlZoneAlignmentMultiplier(player);
                if (mplier <= 0.0f) {
                    continue;
                }
                float alignment = getAlignment(player, penaltyFaction);
                float factionPenalty = -Math.abs(bonus) * mplier;
                factionPenalty = LOTRAlignmentValues.AlignmentBonus.scalePenalty(factionPenalty, alignment);
                setAlignment(player, penaltyFaction, alignment + factionPenalty);
                factionBonusMap.put(penaltyFaction, factionPenalty);
            }
        } else if (faction.isPlayableAlignmentFaction()) {
            float alignment = getAlignment(player, faction);
            float factionBonus = bonus;
            if (factionBonus > 0.0f && alignment >= faction.getPledgeAlignment() && !isPledgedTo(player, faction)) {
                factionBonus *= 0.5f;
            }
            factionBonus = checkBonusForPledgeEnemyLimit(player, faction, factionBonus);
            setAlignment(player, faction, alignment + factionBonus);
            factionBonusMap.put(faction, factionBonus);
        }
        return factionBonusMap;
    }

    private static float checkBonusForPledgeEnemyLimit(Player player, LOTRFaction faction, float bonus) {
        if (isPledgeEnemyAlignmentLimited(player, faction)) {
            float alignment = getAlignment(player, faction);
            float limit = getPledgeEnemyAlignmentLimit(player, faction);
            if (alignment > limit) {
                bonus = 0.0f;
            } else if (alignment + bonus > limit) {
                bonus = limit - alignment;
            }
        }
        return bonus;
    }

    public static boolean doesFactionPreventPledge(LOTRFaction pledgeFac, LOTRFaction otherFac) {
        return pledgeFac.isMortalEnemy(otherFac);
    }

    public static boolean isPledgeEnemyAlignmentLimited(Player player, LOTRFaction faction) {
        LOTRFaction pledge = get(player).pledgeFaction();
        return pledge != null && doesFactionPreventPledge(pledge, faction);
    }

    public static float getPledgeEnemyAlignmentLimit(Player player, LOTRFaction faction) {
        return 0.0f;
    }

    public static boolean hasPledgeAlignment(Player player, LOTRFaction faction) {
        return getAlignment(player, faction) >= faction.getPledgeAlignment();
    }

    public static java.util.List<LOTRFaction> getFactionsPreventingPledgeTo(Player player, LOTRFaction faction) {
        java.util.List<LOTRFaction> enemies = new java.util.ArrayList<>();
        for (LOTRFaction other : LOTRFaction.values()) {
            if (!other.isPlayableAlignmentFaction()
                    || !doesFactionPreventPledge(faction, other)
                    || getAlignment(player, other) <= 0.0f) {
                continue;
            }
            enemies.add(other);
        }
        return enemies;
    }

    public static boolean canPledgeTo(Player player, LOTRFaction faction) {
        if (faction.isPlayableAlignmentFaction()) {
            return hasPledgeAlignment(player, faction)
                    && getFactionsPreventingPledgeTo(player, faction).isEmpty();
        }
        return false;
    }

    public static LOTRFactionRank getRank(Player player, LOTRFaction faction) {
        return faction.getRank(getAlignment(player, faction));
    }

    public static boolean isPledgedTo(Player player, LOTRFaction faction) {
        return get(player).isPledgedTo(faction);
    }

    /** setPledgeFaction: a fresh pledge clears the kill cooldown and sounds. */
    public static void setPledgeFaction(Player player, LOTRFaction faction) {
        player.setAttached(ALIGNMENT_DATA, get(player).withPledgeFaction(faction));
        cooldowns(player).killCooldown = 0;
        if (faction != null && !player.level().isClientSide()) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    LOTRSounds.EVENT_PLEDGE, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }

    public static boolean canMakeNewPledge(Player player) {
        return cooldowns(player).breakCooldown <= 0;
    }

    /** LOTRPacketPledgeSet's handler: pledge if allowed, or unpledge on null. */
    public static void handlePledgeRequest(ServerPlayer player, LOTRFaction faction) {
        if (faction == null) {
            if (get(player).pledgeFaction() != null) {
                revokePledgeFaction(player, true);
            }
        } else if (canPledgeTo(player, faction) && canMakeNewPledge(player)) {
            setPledgeFaction(player, faction);
        }
    }

    /**
     * revokePledgeFaction: the pledge goes, a cooldown of thirty minutes plus up
     * to 150 more (by how far above the pledge level the player stood) begins,
     * and alignment falls to two ranks down, or half the pledge level if that
     * is higher. The original placed the penalty's floating number where the
     * player was looking; the port has no such popup yet.
     */
    public static void revokePledgeFaction(ServerPlayer player, boolean intentional) {
        LOTRFaction wasPledge = get(player).pledgeFaction();
        if (wasPledge == null) {
            return;
        }
        float pledgeLvl = wasPledge.getPledgeAlignment();
        float prevAlign = getAlignment(player, wasPledge);
        float cd = Mth.clamp((prevAlign - pledgeLvl) / 5000.0f, 0.0f, 1.0f);
        setPledgeFaction(player, null);
        LOTRPledgeCooldowns cooldowns = cooldowns(player);
        cooldowns.brokenPledgeFaction = wasPledge;
        setPledgeBreakCooldown(player, 36000 + Math.round(cd * 150.0f * 60.0f * 20.0f));
        LOTRFactionRank rank = wasPledge.getRank(prevAlign);
        LOTRFactionRank rankBelow2 = wasPledge.getRankBelow(wasPledge.getRankBelow(rank));
        float alignPenalty = Math.max(rankBelow2.alignment, pledgeLvl / 2.0f) - prevAlign;
        if (alignPenalty < 0.0f) {
            addAlignment(player, LOTRAlignmentValues.createPledgePenalty(alignPenalty), wasPledge);
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                LOTRSounds.EVENT_UNPLEDGE, SoundSource.PLAYERS, 1.0f, 1.0f);
        player.sendSystemMessage(Component.translatable(
                intentional ? "chat.lotr.unpledge" : "chat.lotr.autoUnpledge", wasPledge.factionName()));
    }

    /**
     * setPledgeBreakCooldown: the client hears of it on a big change -- the
     * cooldown starting, ending, or rising past its recorded start -- or every
     * fifth tick, and the player is told when it runs out.
     */
    private static void setPledgeBreakCooldown(ServerPlayer player, int value) {
        LOTRPledgeCooldowns cd = cooldowns(player);
        int preCD = cd.breakCooldown;
        LOTRFaction preBroken = cd.brokenPledgeFaction;
        cd.breakCooldown = Math.max(0, value);
        boolean bigChange = (cd.breakCooldown == 0 || preCD == 0) && cd.breakCooldown != preCD;
        if (cd.breakCooldown > cd.breakCooldownStart) {
            cd.breakCooldownStart = cd.breakCooldown;
            bigChange = true;
        }
        if (cd.breakCooldown <= 0 && preBroken != null) {
            cd.breakCooldownStart = 0;
            cd.brokenPledgeFaction = null;
            bigChange = true;
        }
        if (bigChange || cd.breakCooldown % 5 == 0) {
            sendBrokenPledge(player);
        }
        if (cd.breakCooldown == 0 && preCD != 0) {
            Component brokenName = preBroken == null
                    ? Component.translatable("lotr.gui.factions.pledgeUnknown") : preBroken.factionName();
            player.sendSystemMessage(Component.translatable("chat.lotr.pledgeBreakCooldown", brokenName));
        }
    }

    private static void sendBrokenPledge(ServerPlayer player) {
        LOTRPledgeCooldowns cd = cooldowns(player);
        ServerPlayNetworking.send(player,
                new LOTRBrokenPledgePayload(cd.breakCooldown, cd.breakCooldownStart, cd.brokenPledgeFaction));
    }

    /**
     * onPledgeKill: killing one of your own pledge faction warns you and starts
     * a day's cooldown; a second such kill within it breaks the pledge.
     */
    public static void onPledgeKill(ServerPlayer player) {
        LOTRPledgeCooldowns cd = cooldowns(player);
        cd.killCooldown += PLEDGE_KILL_COOLDOWN;
        LOTRFaction pledge = get(player).pledgeFaction();
        if (cd.killCooldown > PLEDGE_KILL_COOLDOWN) {
            revokePledgeFaction(player, false);
        } else if (pledge != null) {
            player.sendSystemMessage(Component.translatable("chat.lotr.pledgeKillWarn", pledge.factionName()));
        }
    }

    public static boolean getHideAlignment(Player player) {
        return get(player).hideAlignment();
    }

    public static void setHideAlignment(Player player, boolean hide) {
        player.setAttached(ALIGNMENT_DATA, get(player).withHideAlignment(hide));
    }
}