package net.blueskiez77.lord_of_the_rings__middle_earth.common.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRelations;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * The faction admin commands, all operator-only (permission level 2):
 * LOTRCommandAlignment ("/alignment &lt;set|add&gt; &lt;faction|all&gt; &lt;amount&gt; [player]"),
 * LOTRCommandFactionRelations ("/facRelations set &lt;f1&gt; &lt;f2&gt; &lt;relation&gt;" or
 * "reset") and LOTRCommandEnableAlignmentZones ("/alignmentZones &lt;enable|disable&gt;").
 */
public final class LOTRAlignmentCommand {

    private static final SuggestionProvider<CommandSourceStack> FACTIONS = (context, builder) ->
            SharedSuggestionProvider.suggest(LOTRFaction.getPlayableAlignmentFactionNames(), builder);
    private static final SuggestionProvider<CommandSourceStack> FACTIONS_OR_ALL = (context, builder) -> {
        List<String> list = new ArrayList<>(LOTRFaction.getPlayableAlignmentFactionNames());
        list.add("all");
        return SharedSuggestionProvider.suggest(list, builder);
    };
    private static final SuggestionProvider<CommandSourceStack> RELATIONS = (context, builder) ->
            SharedSuggestionProvider.suggest(LOTRFactionRelations.Relation.listRelationNames(), builder);

    private static final DynamicCommandExceptionType NO_FACTION = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.alignment.noFaction", name));
    private static final DynamicCommandExceptionType TOO_LOW = new DynamicCommandExceptionType(
            v -> Component.translatable("commands.lotr.alignment.tooLow", v));
    private static final DynamicCommandExceptionType TOO_HIGH = new DynamicCommandExceptionType(
            v -> Component.translatable("commands.lotr.alignment.tooHigh", v));
    private static final DynamicCommandExceptionType NO_RELATION = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.facRelations.noRelation", name));
    private static final DynamicCommandExceptionType RELATIONS_ERROR = new DynamicCommandExceptionType(
            msg -> Component.translatable("commands.lotr.facRelations.error", msg));

    private static final float MAX = LOTRAlignmentValues.MAX_ALIGNMENT;

    private LOTRAlignmentCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerAlignment(dispatcher);
            registerFacRelations(dispatcher);
            registerAlignmentZones(dispatcher);
        });
    }

    private static void registerAlignment(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("alignment")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("set").then(factionArg()
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(-MAX, MAX))
                                .executes(ctx -> set(ctx, List.of(ctx.getSource().getPlayerOrException())))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> set(ctx, List.of(EntityArgument.getPlayer(ctx, "player"))))))))
                .then(Commands.literal("add").then(factionArg()
                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                .executes(ctx -> add(ctx, ctx.getSource().getPlayerOrException()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> add(ctx, EntityArgument.getPlayer(ctx, "player"))))))));
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> factionArg() {
        return Commands.argument("faction", StringArgumentType.word()).suggests(FACTIONS_OR_ALL);
    }

    private static List<LOTRFaction> factions(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "faction");
        if ("all".equalsIgnoreCase(name)) {
            return LOTRFaction.getPlayableAlignmentFactions();
        }
        return List.of(faction(name));
    }

    private static LOTRFaction faction(String name) throws CommandSyntaxException {
        LOTRFaction faction = LOTRFaction.forName(name);
        if (faction == null) {
            throw NO_FACTION.create(name);
        }
        return faction;
    }

    private static int set(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets)
            throws CommandSyntaxException {
        List<LOTRFaction> factions = factions(ctx);
        float alignment = FloatArgumentType.getFloat(ctx, "amount");
        for (ServerPlayer player : targets) {
            for (LOTRFaction f : factions) {
                LOTRPlayerAlignments.setAlignment(player, f, alignment);
                ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.alignment.set",
                        player.getName(), f.factionName(), alignment), true);
            }
        }
        return 1;
    }

    /** add: refused outright if any faction would pass +/-10000. */
    private static int add(CommandContext<CommandSourceStack> ctx, ServerPlayer player) throws CommandSyntaxException {
        List<LOTRFaction> factions = factions(ctx);
        float alignment = FloatArgumentType.getFloat(ctx, "amount");
        for (LOTRFaction f : factions) {
            float newAlignment = LOTRPlayerAlignments.getAlignment(player, f) + alignment;
            if (newAlignment < -MAX) {
                throw TOO_LOW.create(-MAX);
            }
            if (newAlignment > MAX) {
                throw TOO_HIGH.create(MAX);
            }
        }
        for (LOTRFaction f : factions) {
            LOTRPlayerAlignments.addAlignment(player, f, alignment);
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.alignment.add",
                    alignment, player.getName(), f.factionName()), true);
        }
        return 1;
    }

    private static void registerFacRelations(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("facRelations")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("set")
                        .then(Commands.argument("faction1", StringArgumentType.word()).suggests(FACTIONS)
                                .then(Commands.argument("faction2", StringArgumentType.word()).suggests(FACTIONS)
                                        .then(Commands.argument("relation", StringArgumentType.word()).suggests(RELATIONS)
                                                .executes(LOTRAlignmentCommand::setRelation)))))
                .then(Commands.literal("reset").executes(ctx -> {
                    LOTRFactionRelations.resetAllRelations();
                    ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.facRelations.reset"), true);
                    return 1;
                })));
    }

    private static int setRelation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        LOTRFaction fac1 = faction(StringArgumentType.getString(ctx, "faction1"));
        LOTRFaction fac2 = faction(StringArgumentType.getString(ctx, "faction2"));
        String relName = StringArgumentType.getString(ctx, "relation");
        LOTRFactionRelations.Relation relation = LOTRFactionRelations.Relation.forName(relName);
        if (relation == null) {
            throw NO_RELATION.create(relName);
        }
        try {
            LOTRFactionRelations.overrideRelations(fac1, fac2, relation);
        } catch (IllegalArgumentException e) {
            throw RELATIONS_ERROR.create(e.getMessage());
        }
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.facRelations.set",
                fac1.factionName(), fac2.factionName(), relation.getDisplayName()), true);
        return 1;
    }

    private static void registerAlignmentZones(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("alignmentZones")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("enable").executes(ctx -> setZones(ctx, true)))
                .then(Commands.literal("disable").executes(ctx -> setZones(ctx, false))));
    }

    private static int setZones(CommandContext<CommandSourceStack> ctx, boolean flag) {
        LOTRLevelData.setEnableAlignmentZones(flag);
        ctx.getSource().sendSuccess(() -> Component.translatable(
                flag ? "commands.lotr.alignmentZones.enable" : "commands.lotr.alignmentZones.disable"), true);
        return 1;
    }
}
