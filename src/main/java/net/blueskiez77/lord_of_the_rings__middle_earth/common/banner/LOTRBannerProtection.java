package net.blueskiez77.lord_of_the_rings__middle_earth.common.banner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBannerProtection: a standing faction banner on a block of bronze,
 * silver or gold protects the land within 8, 16 or 32 blocks of it (or a
 * range of its own, for a structure's). {@link #isProtected} asks whether a
 * spot lies in a banner's land that turns this actor away, by an
 * {@link IFilter} for the actor: a player (by the banner's whitelist or the
 * faction alignment it asks), an NPC or thrown thing (by its faction), or
 * anything at all. A player turned away is told whose land it is, at most once
 * per "Protection Warning Cooldown".
 *
 * <p>Not here: the filters for TNT and TNT minecarts (the original's explosion
 * check that used them was switched off, as explosions were in Middle-earth),
 * the invasion spawner's (D12), and the Thaumcraft golem hook.
 */
public final class LOTRBannerProtection {

    public static final int MAX_RANGE = 64;

    private static final Map<UUID, Integer> LAST_WARNING_TIMES = new HashMap<>();

    private LOTRBannerProtection() {
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> updateWarningCooldowns());
    }

    public enum Permission {
        FULL, DOORS, TABLES, CONTAINERS, PERSONAL_CONTAINERS, FOOD, BEDS, SWITCHES;

        public final int bitFlag = 1 << ordinal();
        public final String codeName = name();

        public static @Nullable Permission forName(String s) {
            for (Permission p : values()) {
                if (p.codeName.equals(s)) {
                    return p;
                }
            }
            return null;
        }
    }

    public enum ProtectType {
        NONE, FACTION, PLAYER_SPECIFIC, STRUCTURE
    }

    /** Whether a banner turns this actor away, and how to tell the actor so. */
    public interface IFilter {
        ProtectType protects(LOTRBannerBlockEntity banner);

        void warnProtection(Component message);
    }

    /** A filter that never warns. */
    private interface SilentFilter extends IFilter {
        @Override
        default void warnProtection(Component message) {
        }
    }

    // ------------------------------------------------------------ the protection blocks

    /** getProtectionRange: the range a banner standing on this block protects, or 0. */
    public static int getProtectionRange(Block block) {
        if (block == LOTRBuildingBlocks.BRONZE_BLOCK) {
            return 8;
        }
        if (block == LOTRBuildingBlocks.SILVER_BLOCK) {
            return 16;
        }
        if (block == Blocks.GOLD_BLOCK) {
            return 32;
        }
        return 0;
    }

    // ------------------------------------------------------------ filters

    public static IFilter anyBanner() {
        return (SilentFilter) banner -> banner.isStructureProtection() ? ProtectType.STRUCTURE : ProtectType.FACTION;
    }

    public static IFilter forFaction(LOTRFaction theFaction) {
        return (SilentFilter) banner -> {
            if (banner.isStructureProtection()) {
                return ProtectType.STRUCTURE;
            }
            return banner.getBannerType().faction.isBadRelation(theFaction) ? ProtectType.FACTION : ProtectType.NONE;
        };
    }

    public static IFilter forNPC(Mob entity) {
        return (SilentFilter) banner -> {
            if (banner.isStructureProtection()) {
                return ProtectType.STRUCTURE;
            }
            return banner.getBannerType().faction.isBadRelation(LOTRNearestAttackableTargetGoal.factionOf(entity))
                    ? ProtectType.FACTION : ProtectType.NONE;
        };
    }

    public static IFilter forPlayer(Player player) {
        return forPlayer(player, Permission.FULL);
    }

    public static IFilter forPlayer(Player player, Permission perm) {
        return new FilterForPlayer(player, perm);
    }

    /** forPlayer_returnMessage: as forPlayer, and the warning handed back in the array too. */
    public static IFilter forPlayerReturnMessage(Player player, Permission perm, @Nullable Component[] message) {
        IFilter inner = forPlayer(player, perm);
        return new IFilter() {
            @Override
            public ProtectType protects(LOTRBannerBlockEntity banner) {
                return inner.protects(banner);
            }

            @Override
            public void warnProtection(Component msg) {
                inner.warnProtection(msg);
                message[0] = msg;
            }
        };
    }

    /** forThrown: by whoever threw it -- a player as a player, a mob by its faction; an unthrown thing as a foe. */
    public static IFilter forThrown(Projectile projectile) {
        return (SilentFilter) banner -> {
            if (banner.isStructureProtection()) {
                return ProtectType.STRUCTURE;
            }
            Entity thrower = projectile.getOwner();
            if (thrower == null) {
                return ProtectType.FACTION;
            }
            if (thrower instanceof Player player) {
                return forPlayer(player, Permission.FULL).protects(banner);
            }
            if (thrower instanceof Mob mob) {
                return forNPC(mob).protects(banner);
            }
            return ProtectType.NONE;
        };
    }

    public static final class FilterForPlayer implements IFilter {
        private final Player thePlayer;
        private final Permission thePerm;
        private boolean ignoreCreativeMode;

        public FilterForPlayer(Player player, Permission perm) {
            this.thePlayer = player;
            this.thePerm = perm;
        }

        public FilterForPlayer ignoreCreativeMode() {
            this.ignoreCreativeMode = true;
            return this;
        }

        @Override
        public ProtectType protects(LOTRBannerBlockEntity banner) {
            if (this.thePlayer.isCreative() && !this.ignoreCreativeMode) {
                return ProtectType.NONE;
            }
            if (banner.isStructureProtection()) {
                return ProtectType.STRUCTURE;
            }
            if (banner.isPlayerSpecificProtection()) {
                return banner.isPlayerWhitelisted(this.thePlayer, this.thePerm) ? ProtectType.NONE : ProtectType.PLAYER_SPECIFIC;
            }
            return banner.isPlayerAllowedByFaction(this.thePlayer, this.thePerm) ? ProtectType.NONE : ProtectType.FACTION;
        }

        @Override
        public void warnProtection(Component message) {
            if (this.thePlayer instanceof FakePlayer) {
                return;
            }
            if (this.thePlayer instanceof ServerPlayer player) {
                player.inventoryMenu.sendAllDataToRemote();
                if (!hasWarningCooldown(player)) {
                    player.sendSystemMessage(message);
                    setWarningCooldown(player);
                }
            }
        }
    }

    // ------------------------------------------------------------ the check

    public static boolean isProtected(Level level, Entity entity, IFilter filter, boolean sendMessage) {
        BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
        return isProtected(level, pos, filter, sendMessage, 0.0);
    }

    public static boolean isProtected(Level level, BlockPos pos, IFilter filter, boolean sendMessage) {
        return isProtected(level, pos, filter, sendMessage, 0.0);
    }

    /**
     * isProtected: whether any protecting banner within reach claims this spot
     * (grown by searchExtra, for a banner about to be placed) against the
     * actor, telling the actor whose land it is if asked.
     */
    public static boolean isProtected(Level level, BlockPos pos, IFilter filter, boolean sendMessage, double searchExtra) {
        if (!LOTRConfig.allowBannerProtection) {
            return false;
        }
        Component protectorName = null;
        AABB originCube = new AABB(pos).inflate(searchExtra);
        AABB searchCube = originCube.inflate(MAX_RANGE);
        for (LOTRBannerBlockEntity banner : LOTRBannerBlockEntity.loadedIn(level)) {
            if (!banner.isProtectingTerritory()) {
                continue;
            }
            AABB protectionCube = banner.createProtectionCube();
            if (!protectionCube.intersects(searchCube) || !protectionCube.intersects(originCube)) {
                continue;
            }
            ProtectType result = filter.protects(banner);
            if (result == ProtectType.NONE) {
                continue;
            }
            if (result == ProtectType.FACTION) {
                protectorName = banner.getBannerType().faction.factionName();
                break;
            }
            if (result == ProtectType.PLAYER_SPECIFIC) {
                String owner = banner.getPlacingPlayerName();
                protectorName = Component.literal(owner == null ? "?" : owner);
                break;
            }
            protectorName = Component.translatable("chat.lotr.protectedStructure");
            break;
        }
        if (protectorName != null) {
            if (sendMessage) {
                filter.warnProtection(Component.translatable("chat.lotr.protectedLand", protectorName));
            }
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------ warnings

    public static boolean hasWarningCooldown(Player player) {
        return LAST_WARNING_TIMES.containsKey(player.getUUID());
    }

    public static void setWarningCooldown(Player player) {
        LAST_WARNING_TIMES.put(player.getUUID(), LOTRConfig.bannerWarningCooldown);
    }

    private static void updateWarningCooldowns() {
        Iterator<Map.Entry<UUID, Integer>> it = LAST_WARNING_TIMES.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Integer> e = it.next();
            int time = e.getValue() - 1;
            if (time > 0) {
                e.setValue(time);
            } else {
                it.remove();
            }
        }
    }
}
