package net.blueskiez77.lord_of_the_rings__middle_earth.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestGrid;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestZone;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRCommandConquest: /conquest set|add|radial &lt;faction&gt; &lt;amount&gt; [x z], /conquest clear [x z]
 * and /conquest rate [value] -- a zone's conquest strengths, and the world's conquest rate.
 */
public final class LOTRConquestCommand {

    private static final SimpleCommandExceptionType NOT_ENABLED = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.conquest.notEnabled"));
    private static final DynamicCommandExceptionType NO_FACTION = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.conquest.noFaction", name));
    private static final Dynamic2CommandExceptionType OUT_OF_BOUNDS = new Dynamic2CommandExceptionType(
            (x, z) -> Component.translatable("commands.lotr.conquest.outOfBounds", x, z));
    private static final DynamicCommandExceptionType TOO_LOW = new DynamicCommandExceptionType(
            f -> Component.translatable("commands.lotr.conquest.tooLow", f));
    private static final DynamicCommandExceptionType TOO_HIGH = new DynamicCommandExceptionType(
            f -> Component.translatable("commands.lotr.conquest.tooHigh", f));

    private LOTRConquestCommand() {
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("conquest")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("clear")
                        .executes(ctx -> clear(ctx, false))
                        .then(Commands.argument("x", IntegerArgumentType.integer()).then(Commands.argument("z", IntegerArgumentType.integer())
                                .executes(ctx -> clear(ctx, true)))))
                .then(Commands.literal("rate")
                        .executes(LOTRConquestCommand::getRate)
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 100.0))
                                .executes(LOTRConquestCommand::setRate)))
                .then(strength("set"))
                .then(strength("add"))
                .then(strength("radial")));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> strength(String function) {
        return Commands.literal(function).then(Commands.argument("faction", StringArgumentType.word())
                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(LOTRFaction.getPlayableAlignmentFactionNames(), builder))
                .then(Commands.argument("amount", FloatArgumentType.floatArg())
                        .executes(ctx -> strength(ctx, function, false))
                        .then(Commands.argument("x", IntegerArgumentType.integer()).then(Commands.argument("z", IntegerArgumentType.integer())
                                .executes(ctx -> strength(ctx, function, true))))));
    }

    private static ServerLevel enabledLevel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerLevel level = ctx.getSource().getLevel();
        if (!LOTRConquestGrid.conquestEnabled(level)) {
            throw NOT_ENABLED.create();
        }
        return level;
    }

    /** parseCoordsAndZone: the given coordinates, or the sender's, and the zone there. */
    private static int[] coords(CommandContext<CommandSourceStack> ctx, boolean given) {
        if (given) {
            return new int[]{IntegerArgumentType.getInteger(ctx, "x"), IntegerArgumentType.getInteger(ctx, "z")};
        }
        Vec3 pos = ctx.getSource().getPosition();
        return new int[]{Mth.floor(pos.x), Mth.floor(pos.z)};
    }

    private static LOTRConquestZone zone(int[] coords) throws CommandSyntaxException {
        LOTRConquestZone zone = LOTRConquestGrid.getZoneByWorldCoords(coords[0], coords[1]);
        if (zone.isDummyZone) {
            throw OUT_OF_BOUNDS.create(coords[0], coords[1]);
        }
        return zone;
    }

    private static int clear(CommandContext<CommandSourceStack> ctx, boolean given) throws CommandSyntaxException {
        ServerLevel level = enabledLevel(ctx);
        int[] coords = coords(ctx, given);
        zone(coords).clearAllFactions(level);
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.conquest.clear", coords[0], coords[1]), true);
        return 1;
    }

    private static int getRate(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        enabledLevel(ctx);
        float currentRate = LOTRLevelData.getConquestRate();
        ctx.getSource().sendSystemMessage(Component.translatable("commands.lotr.conquest.rateGet", currentRate));
        return 1;
    }

    private static int setRate(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        enabledLevel(ctx);
        double rate = DoubleArgumentType.getDouble(ctx, "value");
        LOTRLevelData.setConquestRate((float) rate);
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.conquest.rateSet", rate), true);
        return 1;
    }

    private static int strength(CommandContext<CommandSourceStack> ctx, String function, boolean given) throws CommandSyntaxException {
        ServerLevel level = enabledLevel(ctx);
        String name = StringArgumentType.getString(ctx, "faction");
        LOTRFaction fac = LOTRFaction.forName(name);
        if (fac == null) {
            throw NO_FACTION.create(name);
        }
        float amount = FloatArgumentType.getFloat(ctx, "amount");
        int[] coords = coords(ctx, given);
        LOTRConquestZone zone = zone(coords);
        float max = LOTRConquestGrid.MAX_CONQUEST_SET;
        if ("set".equals(function)) {
            if (amount < 0.0f) {
                throw TOO_LOW.create(0.0f);
            }
            if (amount > max) {
                throw TOO_HIGH.create(max);
            }
            zone.setConquestStrength(fac, amount, level);
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.conquest.set", fac.factionName(), amount,
                    coords[0], coords[1]), true);
            return 1;
        }
        if ("add".equals(function)) {
            float newStr = zone.getConquestStrength(fac, level) + amount;
            if (newStr < 0.0f) {
                throw TOO_LOW.create(0.0f);
            }
            if (newStr > max) {
                throw TOO_HIGH.create(max);
            }
            zone.addConquestStrength(fac, amount, level);
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.conquest.add", fac.factionName(), amount,
                    coords[0], coords[1]), true);
            return 1;
        }
        if (zone.getConquestStrength(fac, level) + amount > max) {
            throw TOO_HIGH.create(max);
        }
        ServerPlayer senderIfPlayer = ctx.getSource().getPlayer();
        if (amount < 0.0f) {
            LOTRConquestGrid.doRadialConquest(level, zone, senderIfPlayer, null, fac, -amount, -amount);
        } else {
            LOTRConquestGrid.doRadialConquest(level, zone, senderIfPlayer, fac, null, amount, amount);
        }
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.conquest.radial", fac.factionName(), amount,
                coords[0], coords[1]), true);
        return 1;
    }
}
