package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

// Registers the per-player alignment Data Attachment and provides the accessor API the rest of the mod calls. This replaces the alignment get/set surface of the 1.7.10 LOTRPlayerData god-object. The attachment is: - persistent (survives server restart) via LOTRAlignmentData.CODEC - synced to the owning client via LOTRAlignmentData.STREAM_CODEC (targetOnly: a player only needs their own alignment for the HUD) - copyOnDeath (alignment shouldn't reset when you die and respawn) Because LOTRAlignmentData is immutable, setters read-modify-write: fetch current, produce a new record, store it. Storing re-triggers sync. The complex addAlignment(...) behavior (control-zone multipliers, penalty scaling, conquest bonuses) is deliberately NOT here yet — this slice is storage + simple get/set. That logic ports on top of this in a later slice.
public final class LOTRPlayerAlignments {
    public static final AttachmentType<LOTRAlignmentData> ALIGNMENT_DATA =
            AttachmentRegistry.create(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_data"),
                    builder -> builder
                            .initializer(() -> LOTRAlignmentData.EMPTY)
                            .persistent(LOTRAlignmentData.CODEC)
                            .syncWith(LOTRAlignmentData.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                            .copyOnDeath());

    private LOTRPlayerAlignments() {
    }

    public static void init() {
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

    public static void setAlignment(Player player, LOTRFaction faction, float value) {
        if (faction.hasFixedAlignment) {
            return;
        }
        LOTRAlignmentData current = get(player);
        player.setAttached(ALIGNMENT_DATA, current.withAlignment(faction, value));
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

    public static void setPledgeFaction(Player player, LOTRFaction faction) {
        player.setAttached(ALIGNMENT_DATA, get(player).withPledgeFaction(faction));
    }

    public static boolean pledgeTo(Player player, LOTRFaction faction) {
        if (!canPledgeTo(player, faction)) {
            return false;
        }
        setPledgeFaction(player, faction);
        return true;
    }

    public static boolean getHideAlignment(Player player) {
        return get(player).hideAlignment();
    }

    public static void setHideAlignment(Player player, boolean hide) {
        player.setAttached(ALIGNMENT_DATA, get(player).withHideAlignment(hide));
    }
}