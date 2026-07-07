package net.blueskiez77.lord_of_the_rings__middle_earth.common.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Developer/testing command for the alignment system. This is the harness
 * that makes the (otherwise invisible) faction storage observable:
 *   /alignment get <faction>
 *   /alignment set <faction> <value>
 *   /alignment add <faction> <amount>
 *   /alignment list
 * Faction is a string argument matched via LOTRFaction.forName, with tab
 * suggestions for every playable faction. Requires op (permission level 2),
 * matching how the original mod gated alignment cheats.
 * Later this graduates into the real /fac or GUI controls, but as a test
 * harness it's the fastest way to verify persistence + client sync work.
 */
public final class LOTRAlignmentCommand {

    private static final SuggestionProvider<CommandSourceStack> FACTION_SUGGESTIONS =
            (context, builder) -> SharedSuggestionProvider.suggest(
                    LOTRFaction.getPlayableAlignmentFactionNames(), builder);

    private LOTRAlignmentCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("alignment")
                        .then(Commands.literal("get")
                                .then(Commands.argument("faction", StringArgumentType.word())
                                        .suggests(FACTION_SUGGESTIONS)
                                        .executes(LOTRAlignmentCommand::getAlignment)))
                        .then(Commands.literal("set")
                                .then(Commands.argument("faction", StringArgumentType.word())
                                        .suggests(FACTION_SUGGESTIONS)
                                        .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                .executes(LOTRAlignmentCommand::setAlignment))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("faction", StringArgumentType.word())
                                        .suggests(FACTION_SUGGESTIONS)
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes(LOTRAlignmentCommand::addAlignment))))
                        .then(Commands.literal("list")
                                .executes(LOTRAlignmentCommand::listAlignments))));
    }

    private static LOTRFaction resolveFaction(CommandContext<CommandSourceStack> ctx) {
        String name = StringArgumentType.getString(ctx, "faction");
        return LOTRFaction.forName(name);
    }

    private static ServerPlayer requirePlayer(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        return ctx.getSource().getPlayerOrException();
    }

    private static int getAlignment(CommandContext<CommandSourceStack> ctx) {
        LOTRFaction faction = resolveFaction(ctx);
        if (faction == null) {
            ctx.getSource().sendFailure(Component.literal("Unknown faction."));
            return 0;
        }
        try {
            ServerPlayer player = requirePlayer(ctx);
            float value = LOTRPlayerAlignments.getAlignment(player, faction);
            LOTRFactionRank rank = LOTRPlayerAlignments.getRank(player, faction);
            ctx.getSource().sendSuccess(() -> Component.translatable(faction.untranslatedFactionName())
                    .append(Component.literal(": " + value + " ("))
                    .append(rank.getDisplayName())
                    .append(Component.literal(")")), false);
            return 1;
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            ctx.getSource().sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }
    }

    private static int setAlignment(CommandContext<CommandSourceStack> ctx) {
        LOTRFaction faction = resolveFaction(ctx);
        if (faction == null) {
            ctx.getSource().sendFailure(Component.literal("Unknown faction."));
            return 0;
        }
        float value = FloatArgumentType.getFloat(ctx, "value");
        try {
            ServerPlayer player = requirePlayer(ctx);
            LOTRPlayerAlignments.setAlignment(player, faction, value);
            ctx.getSource().sendSuccess(() -> Component.literal("Set ")
                    .append(Component.translatable(faction.untranslatedFactionName()))
                    .append(Component.literal(" alignment to " + value)), false);
            return 1;
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            ctx.getSource().sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }
    }

    private static int addAlignment(CommandContext<CommandSourceStack> ctx) {
        LOTRFaction faction = resolveFaction(ctx);
        if (faction == null) {
            ctx.getSource().sendFailure(Component.literal("Unknown faction."));
            return 0;
        }
        float amount = FloatArgumentType.getFloat(ctx, "amount");
        try {
            ServerPlayer player = requirePlayer(ctx);
            LOTRPlayerAlignments.addAlignment(player, faction, amount);
            float total = LOTRPlayerAlignments.getAlignment(player, faction);
            ctx.getSource().sendSuccess(() -> Component.literal("Added " + amount + " to ")
                    .append(Component.translatable(faction.untranslatedFactionName()))
                    .append(Component.literal(" (now " + total + ")")), false);
            return 1;
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            ctx.getSource().sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }
    }

    private static int listAlignments(CommandContext<CommandSourceStack> ctx) {
        try {
            ServerPlayer player = requirePlayer(ctx);
            ctx.getSource().sendSuccess(() -> Component.literal("--- Alignments ---"), false);
            for (LOTRFaction faction : LOTRFaction.getPlayableAlignmentFactions()) {
                float value = LOTRPlayerAlignments.getAlignment(player, faction);
                if (value != 0.0f) {
                    ctx.getSource().sendSuccess(() -> Component.translatable(faction.untranslatedFactionName())
                            .append(Component.literal(": " + value)), false);
                }
            }
            return 1;
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            ctx.getSource().sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }
    }
}