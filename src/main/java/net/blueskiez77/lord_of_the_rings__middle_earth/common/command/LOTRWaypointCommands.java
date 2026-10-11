package net.blueskiez77.lord_of_the_rings__middle_earth.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFastTravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRPlayerWaypoints;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * The waypoint commands: /wpCooldown (LOTRCommandWaypointCooldown), the waypoints' longest or
 * shortest wait; /fastTravelClock (LOTRCommandFastTravelClock), a player's clock; and
 * /lotrWaypoints (LOTRCommandWaypoints), unlocking or locking a player's waypoint regions.
 */
public final class LOTRWaypointCommands {

    /** LOTRCommandWaypointCooldown.MAX_COOLDOWN: a day. */
    public static final int MAX_COOLDOWN = 86400;

    private static final DynamicCommandExceptionType UNKNOWN_REGION = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.lotrWaypoints.unknown", name));
    private static final Dynamic2CommandExceptionType UNLOCK_FAIL = new Dynamic2CommandExceptionType(
            (player, region) -> Component.translatable("commands.lotr.lotrWaypoints.unlock.fail", player, region));
    private static final Dynamic2CommandExceptionType LOCK_FAIL = new Dynamic2CommandExceptionType(
            (player, region) -> Component.translatable("commands.lotr.lotrWaypoints.lock.fail", player, region));

    private LOTRWaypointCommands() {
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerWaypointCooldown(dispatcher);
            registerFastTravelClock(dispatcher);
            registerWaypoints(dispatcher);
        });
    }

    // ---------------------------------------------------------------- /wpCooldown

    private static void registerWaypointCooldown(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wpCooldown")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, MAX_COOLDOWN))
                        .executes(ctx -> setCooldown(ctx, "max")))
                .then(Commands.literal("max").then(Commands.argument("seconds", IntegerArgumentType.integer(0, MAX_COOLDOWN))
                        .executes(ctx -> setCooldown(ctx, "max"))))
                .then(Commands.literal("min").then(Commands.argument("seconds", IntegerArgumentType.integer(0, MAX_COOLDOWN))
                        .executes(ctx -> setCooldown(ctx, "min")))));
    }

    private static int setCooldown(CommandContext<CommandSourceStack> ctx, String function) {
        int cooldown = IntegerArgumentType.getInteger(ctx, "seconds");
        int max = LOTRLevelData.getWaypointCooldownMax();
        int min = LOTRLevelData.getWaypointCooldownMin();
        if ("max".equals(function)) {
            boolean updatedMin = false;
            max = cooldown;
            if (max < min) {
                min = max;
                updatedMin = true;
            }
            LOTRLevelData.setWaypointCooldown(max, min);
            int finalMax = max;
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.wpCooldown.setMax", finalMax,
                    LOTRMiscCommands.hmsTime(finalMax * 20)), true);
            if (updatedMin) {
                int finalMin = min;
                ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.wpCooldown.updateMin", finalMin), true);
            }
        } else {
            boolean updatedMax = false;
            min = cooldown;
            if (min > max) {
                max = min;
                updatedMax = true;
            }
            LOTRLevelData.setWaypointCooldown(max, min);
            int finalMin = min;
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.wpCooldown.setMin", finalMin,
                    LOTRMiscCommands.hmsTime(finalMin * 20)), true);
            if (updatedMax) {
                int finalMax = max;
                ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.wpCooldown.updateMax", finalMax), true);
            }
        }
        return 1;
    }

    // ---------------------------------------------------------------- /fastTravelClock

    private static void registerFastTravelClock(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fastTravelClock")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("max")
                        .executes(ctx -> setClock(ctx, 1000000, ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> setClock(ctx, 1000000, EntityArgument.getPlayer(ctx, "player")))))
                .then(Commands.argument("seconds", IntegerArgumentType.integer(0))
                        .executes(ctx -> setClock(ctx, IntegerArgumentType.getInteger(ctx, "seconds"), ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> setClock(ctx, IntegerArgumentType.getInteger(ctx, "seconds"),
                                        EntityArgument.getPlayer(ctx, "player"))))));
    }

    private static int setClock(CommandContext<CommandSourceStack> ctx, int seconds, ServerPlayer player) {
        LOTRFastTravel.setTimeSinceFTWithUpdate(player, seconds * 20);
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.fastTravelClock.set", player.getDisplayName(),
                seconds, LOTRMiscCommands.hmsTime(seconds * 20)), true);
        return 1;
    }

    // ---------------------------------------------------------------- /lotrWaypoints

    private static void registerWaypoints(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lotrWaypoints")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(regionAction("unlock", true))
                .then(regionAction("lock", false)));
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> regionAction(String name, boolean unlock) {
        return Commands.literal(name).then(Commands.argument("region", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    builder.suggest("all");
                    return SharedSuggestionProvider.suggest(java.util.Arrays.stream(LOTRWaypoint.Region.values()).map(Enum::name), builder);
                })
                .executes(ctx -> region(ctx, unlock, ctx.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> region(ctx, unlock, EntityArgument.getPlayer(ctx, "player")))));
    }

    private static int region(CommandContext<CommandSourceStack> ctx, boolean unlock, ServerPlayer player) throws CommandSyntaxException {
        String regionName = StringArgumentType.getString(ctx, "region");
        if ("all".equalsIgnoreCase(regionName)) {
            for (LOTRWaypoint.Region region : LOTRWaypoint.Region.values()) {
                if (unlock) {
                    LOTRPlayerWaypoints.unlockFTRegion(player, region);
                } else {
                    LOTRPlayerWaypoints.lockFTRegion(player, region);
                }
            }
            ctx.getSource().sendSuccess(() -> Component.translatable(unlock ? "commands.lotr.lotrWaypoints.unlockAll"
                    : "commands.lotr.lotrWaypoints.lockAll", player.getDisplayName()), true);
            return 1;
        }
        LOTRWaypoint.Region region = LOTRWaypoint.regionForName(regionName);
        if (region == null) {
            throw UNKNOWN_REGION.create(regionName);
        }
        if (unlock) {
            if (LOTRPlayerWaypoints.isFTRegionUnlocked(player, region)) {
                throw UNLOCK_FAIL.create(player.getDisplayName(), region.name());
            }
            LOTRPlayerWaypoints.unlockFTRegion(player, region);
        } else {
            if (!LOTRPlayerWaypoints.isFTRegionUnlocked(player, region)) {
                throw LOCK_FAIL.create(player.getDisplayName(), region.name());
            }
            LOTRPlayerWaypoints.lockFTRegion(player, region);
        }
        ctx.getSource().sendSuccess(() -> Component.translatable(unlock ? "commands.lotr.lotrWaypoints.unlock"
                : "commands.lotr.lotrWaypoints.lock", player.getDisplayName(), region.name()), true);
        return 1;
    }
}
