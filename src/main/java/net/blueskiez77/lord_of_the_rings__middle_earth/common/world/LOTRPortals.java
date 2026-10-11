package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPortalBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPortalEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.portal.TeleportTransition;

import org.jspecify.annotations.Nullable;

/**
 * The making and the using of the portals, from LOTRTickHandlerServer: a gold ring burning in the
 * overworld near a player becomes the Ring portal (once only); an elanor or niphredil dropped into a
 * quendite-ringed pool by a friend of the Elves, or a bone into a gulduril-pillared lava pit by a
 * servant of the Enemy, makes the pool a portal; and a player standing in such a portal is taken
 * across once the wait of a nether portal is up.
 */
public final class LOTRPortals {

    private static final TagKey<Item> BONES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "bones"));

    public static PoiType ELVEN_PORTAL_POI;
    public static PoiType MORGUL_PORTAL_POI;

    /** The client's side of setInPortal (its wait shown as the portal's overlay); set by the client. */
    public static BiConsumer<Player, @Nullable Block> clientInPortal = (player, block) -> {
    };

    private static final Map<UUID, Integer> PLAYERS_IN_ELVEN_PORTALS = new HashMap<>();
    private static final Map<UUID, Integer> PLAYERS_IN_MORGUL_PORTALS = new HashMap<>();

    private LOTRPortals() {
    }

    public static void init() {
        ELVEN_PORTAL_POI = PoiHelper.register(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "elven_portal"), 0, 1,
                LOTRUtilityBlocks.ELVEN_PORTAL);
        MORGUL_PORTAL_POI = PoiHelper.register(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "morgul_portal"), 0, 1,
                LOTRUtilityBlocks.MORGUL_PORTAL);
        ServerTickEvents.END_SERVER_TICK.register(LOTRPortals::serverTick);
    }

    /** setInPortal / setInElvenPortal / setInMorgulPortal: the player's wait in a portal begins ({@code null}: the Ring portal). */
    public static void setInPortal(Player player, @Nullable Block portal) {
        if (player.level().isClientSide()) {
            clientInPortal.accept(player, portal);
        } else if (portal == LOTRUtilityBlocks.ELVEN_PORTAL) {
            PLAYERS_IN_ELVEN_PORTALS.putIfAbsent(player.getUUID(), 0);
        } else if (portal == LOTRUtilityBlocks.MORGUL_PORTAL) {
            PLAYERS_IN_MORGUL_PORTALS.putIfAbsent(player.getUUID(), 0);
        }
    }

    /** getMaxInPortalTime: the nether portal's wait, from the game rules. */
    public static int getMaxInPortalTime(ServerLevel level, Player player) {
        return Math.max(0, level.getGameRules().get(player.getAbilities().invulnerable
                ? GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
    }

    /** The other of the overworld and Middle-earth (the overworld from anywhere else). */
    public static @Nullable ServerLevel otherWorld(ServerLevel level) {
        return level.getServer().getLevel(level.dimension() == Level.OVERWORLD ? LOTRWorldGen.MIDDLE_EARTH : Level.OVERWORLD);
    }

    /** LOTRBlockPortal.transferEntity: a creature taken through a built portal. */
    public static void transferThroughPortal(ServerLevel level, Entity entity, LOTRPortalTeleporter teleporter) {
        ServerLevel target = otherWorld(level);
        if (target != null) {
            transferEntity(entity, teleporter.transition(target, entity));
        }
    }

    /** LOTRMod.transferEntityToDimension: never the Ring portal itself; the creature waits a while before it may go back. */
    public static void transferEntity(Entity entity, TeleportTransition transition) {
        if (entity instanceof LOTRPortalEntity || entity.isRemoved()) {
            return;
        }
        Entity moved = entity.teleport(transition);
        if (moved != null) {
            moved.setPortalCooldown();
        }
    }

    private static void serverTick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerLevel level = player.level();
            boolean overworld = level.dimension() == Level.OVERWORLD;
            boolean middleEarth = level.dimension() == LOTRWorldGen.MIDDLE_EARTH;
            if (overworld && LOTRLevelData.madePortal == 0) {
                makeRingPortal(level, player);
            }
            if (overworld || middleEarth) {
                makeBuiltPortals(level, player);
            }
            updatePlayerInPortal(player, PLAYERS_IN_ELVEN_PORTALS, (LOTRPortalBlock) LOTRUtilityBlocks.ELVEN_PORTAL);
            updatePlayerInPortal(player, PLAYERS_IN_MORGUL_PORTALS, (LOTRPortalBlock) LOTRUtilityBlocks.MORGUL_PORTAL);
        }
        PLAYERS_IN_ELVEN_PORTALS.keySet().removeIf(id -> server.getPlayerList().getPlayer(id) == null);
        PLAYERS_IN_MORGUL_PORTALS.keySet().removeIf(id -> server.getPlayerList().getPlayer(id) == null);
    }

    /** A burning gold ring: the Ring portal, three blocks above it, in a blast that leaves its place marked. */
    private static void makeRingPortal(ServerLevel level, ServerPlayer player) {
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(16.0))) {
            if (LOTRLevelData.madePortal != 0 || !item.getItem().is(LOTRMiscItems.GOLD_RING) || !item.isOnFire()) {
                continue;
            }
            LOTRLevelData.setMadePortal(1);
            LOTRLevelData.markOverworldPortalLocation(Mth.floor(item.getX()), Mth.floor(item.getY()), Mth.floor(item.getZ()));
            item.discard();
            level.explode(player, item.getX(), item.getY() + 3.0, item.getZ(), 3.0f, false, Level.ExplosionInteraction.TNT);
            LOTRPortalEntity portal = LOTREntities.PORTAL.create(level, EntitySpawnReason.TRIGGERED);
            if (portal != null) {
                portal.snapTo(item.getX(), item.getY() + 3.0, item.getZ(), 0.0f, 0.0f);
                level.addFreshEntity(portal);
            }
        }
    }

    /** The flowers and the bones: the first fit place among the 5x5 about the item becomes a 3x3 portal. */
    private static void makeBuiltPortals(ServerLevel level, ServerPlayer player) {
        boolean elfFriend = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.LOTHLORIEN) >= 1.0f
                || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.HIGH_ELF) >= 1.0f;
        boolean enemyServant = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR) >= 1.0f
                || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.ANGMAR) >= 1.0f
                || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.DOL_GULDUR) >= 1.0f;
        if (!elfFriend && !enemyServant) {
            return;
        }
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(16.0))) {
            ItemStack stack = item.getItem();
            if (stack.isEmpty() || item.isRemoved()) {
                continue;
            }
            if (elfFriend && (stack.is(LOTRDecorationBlocks.ELANOR.asItem()) || stack.is(LOTRDecorationBlocks.NIPHREDIL.asItem()))) {
                tryMakePortal(level, item, (LOTRPortalBlock) LOTRUtilityBlocks.ELVEN_PORTAL);
            }
            if (enemyServant && stack.is(BONES) && !item.isRemoved()) {
                tryMakePortal(level, item, (LOTRPortalBlock) LOTRUtilityBlocks.MORGUL_PORTAL);
            }
        }
    }

    private static void tryMakePortal(ServerLevel level, ItemEntity item, LOTRPortalBlock portal) {
        int i = Mth.floor(item.getX());
        int j = Mth.floor(item.getY());
        int k = Mth.floor(item.getZ());
        for (int i1 = i - 2; i1 <= i + 2; ++i1) {
            for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                if (!portal.isValidPortalLocation(level, i1, j, k1, false)) {
                    continue;
                }
                item.discard();
                for (int i2 = -1; i2 <= 1; ++i2) {
                    for (int k2 = -1; k2 <= 1; ++k2) {
                        level.setBlock(new BlockPos(i1 + i2, j, k1 + k2), portal.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
                return;
            }
        }
    }

    /** updatePlayerInPortal: counting on while the player stands in the portal, across when the wait is done. */
    private static void updatePlayerInPortal(ServerPlayer player, Map<UUID, Integer> players, LOTRPortalBlock portalBlock) {
        ServerLevel level = player.level();
        if (level.dimension() != Level.OVERWORLD && level.dimension() != LOTRWorldGen.MIDDLE_EARTH || !players.containsKey(player.getUUID())) {
            return;
        }
        boolean inPortal = level.getBlockState(BlockPos.containing(player.getX(), player.getBoundingBox().minY, player.getZ())).is(portalBlock);
        if (!inPortal) {
            players.remove(player.getUUID());
            return;
        }
        int i = players.get(player.getUUID()) + 1;
        players.put(player.getUUID(), i);
        if (i >= getMaxInPortalTime(level, player)) {
            players.remove(player.getUUID());
            ServerLevel target = otherWorld(level);
            if (target != null) {
                player.teleport(portalBlock.getPortalTeleporter().transition(target, player));
            }
        }
    }
}
