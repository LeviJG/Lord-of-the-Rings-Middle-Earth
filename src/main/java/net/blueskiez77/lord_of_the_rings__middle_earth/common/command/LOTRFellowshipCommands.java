package net.blueskiez77.lord_of_the_rings__middle_earth.common.command;

import java.util.UUID;
import java.util.function.BiFunction;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowship;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowships;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRCommandFellowship (admins: make a fellowship for a player, or do to one anything its owner
 * could) and LOTRCommandFellowshipMessage (/fmsg: speak to a fellowship, or bind one to speak to).
 * A fellowship is named in quotes where its name has spaces; players by name, there or not.
 */
public final class LOTRFellowshipCommands {

    private static final SimpleCommandExceptionType USAGE = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.fellowship.usage"));
    private static final SimpleCommandExceptionType PLAYER_NOT_FOUND = new SimpleCommandExceptionType(
            Component.translatable("argument.player.unknown"));
    private static final SimpleCommandExceptionType FMSG_USAGE = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.fmsg.usage"));
    private static final DynamicCommandExceptionType FMSG_NOT_FOUND = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.fmsg.notFound", name));
    private static final SimpleCommandExceptionType FMSG_BOUND_NONE = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.fmsg.boundNone"));
    private static final DynamicCommandExceptionType FMSG_BOUND_NOT_MEMBER = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.fmsg.boundNotMember", name));

    private LOTRFellowshipCommands() {
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerFellowship(dispatcher, registryAccess);
            registerFellowshipMessage(dispatcher);
        });
    }

    private static UUID playerID(CommandSourceStack source, String username) throws CommandSyntaxException {
        ServerPlayer online = source.getServer().getPlayerList().getPlayerByName(username);
        if (online != null) {
            return online.getUUID();
        }
        return source.getServer().services().nameToIdCache().get(username).map(NameAndId::id)
                .orElseThrow(PLAYER_NOT_FOUND::create);
    }

    private static CommandSyntaxException error(String key, Object... args) {
        return new SimpleCommandExceptionType(Component.translatable(key, args)).create();
    }

    private static void notify(CommandSourceStack source, String key, Object... args) {
        source.sendSuccess(() -> Component.translatable(key, args), true);
    }

    // ---------------------------------------------------------------- /fellowship

    private static void registerFellowship(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("fellowship")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .executes(ctx -> {
                    throw USAGE.create();
                })
                .then(Commands.literal("create")
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(LOTRFellowshipCommands::onlineNames)
                                .then(Commands.argument("fellowship", StringArgumentType.string()).executes(ctx -> {
                                    String playerName = StringArgumentType.getString(ctx, "player");
                                    String fsName = StringArgumentType.getString(ctx, "fellowship");
                                    UUID player = playerID(ctx.getSource(), playerName);
                                    if (LOTRFellowships.getFellowshipByName(player, fsName) != null) {
                                        throw error("commands.lotr.fellowship.create.exists", playerName, fsName);
                                    }
                                    LOTRFellowships.createFellowship(player, fsName, false);
                                    notify(ctx.getSource(), "commands.lotr.fellowship.create", playerName, fsName);
                                    return 1;
                                }))))
                .then(Commands.literal("option")
                        .then(Commands.argument("owner", StringArgumentType.word()).suggests(LOTRFellowshipCommands::onlineNames)
                                .then(Commands.argument("fellowship", StringArgumentType.string())
                                        .suggests((ctx, builder) -> {
                                            try {
                                                UUID owner = playerID(ctx.getSource(), StringArgumentType.getString(ctx, "owner"));
                                                return SharedSuggestionProvider.suggest(LOTRFellowships.listAllFellowshipNames(owner, true)
                                                        .stream().map(n -> "\"" + n + "\""), builder);
                                            } catch (CommandSyntaxException e) {
                                                return builder.buildFuture();
                                            }
                                        })
                                        .then(option("disband", (ctx, fs) -> {
                                            LOTRFellowships.disbandFellowship(fs.getOwner(), fs, owner(ctx));
                                            notify(ctx.getSource(), "commands.lotr.fellowship.disband", owner(ctx), fs.getName());
                                            return 1;
                                        }))
                                        .then(Commands.literal("rename")
                                                .then(Commands.argument("name", StringArgumentType.string()).executes(ctx -> {
                                                    LOTRFellowship fs = fellowship(ctx);
                                                    String newName = StringArgumentType.getString(ctx, "name");
                                                    if (newName.isBlank()) {
                                                        throw error("commands.lotr.fellowship.rename.error");
                                                    }
                                                    String oldName = fs.getName();
                                                    LOTRFellowships.renameFellowship(fs.getOwner(), fs, newName);
                                                    notify(ctx.getSource(), "commands.lotr.fellowship.rename", owner(ctx), oldName, newName);
                                                    return 1;
                                                })))
                                        .then(Commands.literal("icon")
                                                .then(Commands.literal("clear").executes(ctx -> {
                                                    LOTRFellowship fs = fellowship(ctx);
                                                    LOTRFellowships.setFellowshipIcon(fs.getOwner(), fs, null);
                                                    notify(ctx.getSource(), "commands.lotr.fellowship.icon", owner(ctx), fs.getName(), "[none]");
                                                    return 1;
                                                }))
                                                .then(Commands.argument("item", ItemArgument.item(context)).executes(ctx -> {
                                                    LOTRFellowship fs = fellowship(ctx);
                                                    ItemStack icon = ItemArgument.getItem(ctx, "item").createItemStack(1);
                                                    LOTRFellowships.setFellowshipIcon(fs.getOwner(), fs, icon);
                                                    notify(ctx.getSource(), "commands.lotr.fellowship.icon", owner(ctx), fs.getName(),
                                                            icon.getHoverName());
                                                    return 1;
                                                })))
                                        .then(toggle("pvp", "prevent", "allow", (ctx, prevent) -> {
                                            LOTRFellowship fs = fellowship(ctx);
                                            LOTRFellowships.setFellowshipPreventPVP(fs.getOwner(), fs, prevent);
                                            notify(ctx.getSource(), prevent ? "commands.lotr.fellowship.pvp.prevent"
                                                    : "commands.lotr.fellowship.pvp.allow", owner(ctx), fs.getName());
                                            return 1;
                                        }))
                                        .then(toggle("hired-ff", "prevent", "allow", (ctx, prevent) -> {
                                            LOTRFellowship fs = fellowship(ctx);
                                            LOTRFellowships.setFellowshipPreventHiredFF(fs.getOwner(), fs, prevent);
                                            notify(ctx.getSource(), prevent ? "commands.lotr.fellowship.hiredFF.prevent"
                                                    : "commands.lotr.fellowship.hiredFF.allow", owner(ctx), fs.getName());
                                            return 1;
                                        }))
                                        .then(toggle("map-show", "on", "off", (ctx, show) -> {
                                            LOTRFellowship fs = fellowship(ctx);
                                            LOTRFellowships.setFellowshipShowMapLocations(fs.getOwner(), fs, show);
                                            notify(ctx.getSource(), show ? "commands.lotr.fellowship.mapShow.on"
                                                    : "commands.lotr.fellowship.mapShow.off", owner(ctx), fs.getName());
                                            return 1;
                                        }))
                                        .then(playerOption("invite", (ctx, fs, player, name) -> {
                                            if (fs.containsPlayer(player)) {
                                                throw error("commands.lotr.fellowship.edit.alreadyIn", owner(ctx), fs.getName(), name);
                                            }
                                            LOTRFellowships.invitePlayerToFellowship(fs.getOwner(), fs, player, owner(ctx));
                                            notify(ctx.getSource(), "commands.lotr.fellowship.invite", owner(ctx), fs.getName(), name);
                                        }))
                                        .then(playerOption("add", (ctx, fs, player, name) -> {
                                            if (fs.containsPlayer(player)) {
                                                throw error("commands.lotr.fellowship.edit.alreadyIn", owner(ctx), fs.getName(), name);
                                            }
                                            LOTRFellowships.invitePlayerToFellowship(fs.getOwner(), fs, player, owner(ctx));
                                            LOTRFellowships.acceptFellowshipInvite(player, fs, false);
                                            notify(ctx.getSource(), "commands.lotr.fellowship.add", owner(ctx), fs.getName(), name);
                                        }))
                                        .then(playerOption("remove", (ctx, fs, player, name) -> {
                                            requireMember(ctx, fs, player, name);
                                            LOTRFellowships.removePlayerFromFellowship(fs.getOwner(), fs, player, owner(ctx));
                                            notify(ctx.getSource(), "commands.lotr.fellowship.remove", owner(ctx), fs.getName(), name);
                                        }))
                                        .then(playerOption("transfer", (ctx, fs, player, name) -> {
                                            requireMember(ctx, fs, player, name);
                                            LOTRFellowships.transferFellowship(fs.getOwner(), fs, player, owner(ctx));
                                            notify(ctx.getSource(), "commands.lotr.fellowship.transfer", owner(ctx), fs.getName(), name);
                                        }))
                                        .then(playerOption("op", (ctx, fs, player, name) -> {
                                            requireMember(ctx, fs, player, name);
                                            if (fs.isAdmin(player)) {
                                                throw error("commands.lotr.fellowship.edit.alreadyOp", owner(ctx), fs.getName(), name);
                                            }
                                            LOTRFellowships.setFellowshipAdmin(fs.getOwner(), fs, player, true, owner(ctx));
                                            notify(ctx.getSource(), "commands.lotr.fellowship.op", owner(ctx), fs.getName(), name);
                                        }))
                                        .then(playerOption("deop", (ctx, fs, player, name) -> {
                                            requireMember(ctx, fs, player, name);
                                            if (!fs.isAdmin(player)) {
                                                throw error("commands.lotr.fellowship.edit.notOp", owner(ctx), fs.getName(), name);
                                            }
                                            LOTRFellowships.setFellowshipAdmin(fs.getOwner(), fs, player, false, owner(ctx));
                                            notify(ctx.getSource(), "commands.lotr.fellowship.deop", owner(ctx), fs.getName(), name);
                                        }))))));
    }

    private static java.util.concurrent.CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> onlineNames(
            CommandContext<CommandSourceStack> ctx, com.mojang.brigadier.suggestion.SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ctx.getSource().getOnlinePlayerNames(), builder);
    }

    private static String owner(CommandContext<CommandSourceStack> ctx) {
        return StringArgumentType.getString(ctx, "owner");
    }

    /** The owner's fellowship of that name, which they must own. */
    private static LOTRFellowship fellowship(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String ownerName = owner(ctx);
        String fsName = StringArgumentType.getString(ctx, "fellowship");
        UUID owner = playerID(ctx.getSource(), ownerName);
        LOTRFellowship fs = LOTRFellowships.getFellowshipByName(owner, fsName);
        if (fs == null || !fs.isOwner(owner)) {
            throw error("commands.lotr.fellowship.edit.notFound", ownerName, fsName);
        }
        return fs;
    }

    private static void requireMember(CommandContext<CommandSourceStack> ctx, LOTRFellowship fs, UUID player, String name)
            throws CommandSyntaxException {
        if (!fs.hasMember(player)) {
            throw error("commands.lotr.fellowship.edit.notMember", owner(ctx), fs.getName(), name);
        }
    }

    @FunctionalInterface
    private interface FellowshipAction {
        int run(CommandContext<CommandSourceStack> ctx, LOTRFellowship fs) throws CommandSyntaxException;
    }

    @FunctionalInterface
    private interface ToggleAction {
        int run(CommandContext<CommandSourceStack> ctx, boolean flag) throws CommandSyntaxException;
    }

    @FunctionalInterface
    private interface PlayerAction {
        void run(CommandContext<CommandSourceStack> ctx, LOTRFellowship fs, UUID player, String name) throws CommandSyntaxException;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> option(String name, FellowshipAction action) {
        return Commands.literal(name).executes(ctx -> action.run(ctx, fellowship(ctx)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> toggle(String name, String on, String off, ToggleAction action) {
        return Commands.literal(name)
                .then(Commands.literal(on).executes(ctx -> action.run(ctx, true)))
                .then(Commands.literal(off).executes(ctx -> action.run(ctx, false)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playerOption(String name, PlayerAction action) {
        return Commands.literal(name).then(Commands.argument("player", StringArgumentType.word())
                .suggests(LOTRFellowshipCommands::onlineNames).executes(ctx -> {
                    LOTRFellowship fs = fellowship(ctx);
                    String playerName = StringArgumentType.getString(ctx, "player");
                    action.run(ctx, fs, playerID(ctx.getSource(), playerName), playerName);
                    return 1;
                }));
    }

    // ---------------------------------------------------------------- /fmsg

    private static void registerFellowshipMessage(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fmsg")
                .executes(ctx -> {
                    throw FMSG_USAGE.create();
                })
                .then(Commands.literal("bind").then(Commands.argument("fellowship", StringArgumentType.string())
                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(LOTRFellowships.listAllFellowshipNames(
                                ctx.getSource().getPlayerOrException().getUUID(), false).stream().map(n -> "\"" + n + "\""), builder))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String fsName = StringArgumentType.getString(ctx, "fellowship");
                            LOTRFellowship fs = LOTRFellowships.getFellowshipByName(player.getUUID(), fsName);
                            if (fs == null) {
                                throw FMSG_NOT_FOUND.create(fsName);
                            }
                            LOTRFellowships.setChatBoundFellowship(player.getUUID(), fs);
                            player.sendSystemMessage(Component.translatable("commands.lotr.fmsg.bind", fs.getName())
                                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                            return 1;
                        })))
                .then(Commands.literal("unbind").executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    LOTRFellowship bound = LOTRFellowships.getChatBoundFellowship(player.getUUID());
                    LOTRFellowships.setChatBoundFellowship(player.getUUID(), null);
                    player.sendSystemMessage(Component.translatable("commands.lotr.fmsg.unbind",
                            bound == null ? "" : bound.getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    return 1;
                }))
                .then(Commands.argument("message", StringArgumentType.greedyString()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    String text = StringArgumentType.getString(ctx, "message");
                    LOTRFellowship fs = null;
                    if (text.startsWith("\"")) {
                        int end = text.indexOf('"', 1);
                        if (end < 0) {
                            throw error("commands.lotr.fellowship.edit.nameError");
                        }
                        String fsName = text.substring(1, end);
                        fs = LOTRFellowships.getFellowshipByName(player.getUUID(), fsName);
                        if (fs == null) {
                            throw FMSG_NOT_FOUND.create(fsName);
                        }
                        text = text.substring(end + 1).trim();
                    }
                    if (fs == null) {
                        fs = LOTRFellowships.getChatBoundFellowship(player.getUUID());
                        if (fs == null) {
                            throw FMSG_BOUND_NONE.create();
                        }
                        if (!fs.containsPlayer(player.getUUID())) {
                            throw FMSG_BOUND_NOT_MEMBER.create(fs.getName());
                        }
                    }
                    LOTRFellowships.sendFellowshipMessage(player, fs, text);
                    return 1;
                })));
    }
}
