package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Who may use a structure spawner: the server-wide ban (LOTRLevelData's
 * "StructuresBanned") and each player's own ("StructuresBanned" in their
 * player data, kept here as its own attachment), the commands that set them
 * -- /banStructures and /allowStructures, for everyone or one player, at op
 * level 3 -- and the one-second wait between spawns
 * (LOTRItemStructureSpawner.lastStructureSpawnTick, counted down each server
 * tick).
 */
public final class LOTRStructureSpawning {

    public static final AttachmentType<Boolean> STRUCTURES_BANNED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "structures_banned"),
            builder -> builder.initializer(() -> false).persistent(Codec.BOOL).copyOnDeath());

    private static final SimpleCommandExceptionType ALREADY_BANNED = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.banStructures.alreadyBanned"));
    private static final SimpleCommandExceptionType ALREADY_ALLOWED = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.allowStructures.alreadyAllowed"));

    public static int lastStructureSpawnTick;

    private LOTRStructureSpawning() {
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (lastStructureSpawnTick > 0) {
                --lastStructureSpawnTick;
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    public static boolean isPlayerBanned(Player player) {
        return player.getAttachedOrElse(STRUCTURES_BANNED, false);
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("banStructures")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .executes(ctx -> {
                    if (LOTRLevelData.structuresBanned()) {
                        throw ALREADY_BANNED.create();
                    }
                    LOTRLevelData.setStructuresBanned(true);
                    ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.banStructures.ban"), true);
                    return 1;
                })
                .then(Commands.argument("player", EntityArgument.players())
                        .executes(ctx -> setPlayers(ctx, EntityArgument.getPlayers(ctx, "player"), true))));
        dispatcher.register(Commands.literal("allowStructures")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .executes(ctx -> {
                    if (!LOTRLevelData.structuresBanned()) {
                        throw ALREADY_ALLOWED.create();
                    }
                    LOTRLevelData.setStructuresBanned(false);
                    ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.allowStructures.allow"), true);
                    return 1;
                })
                .then(Commands.argument("player", EntityArgument.players())
                        .executes(ctx -> setPlayers(ctx, EntityArgument.getPlayers(ctx, "player"), false))));
    }

    private static int setPlayers(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, boolean ban) {
        for (ServerPlayer player : players) {
            player.setAttached(STRUCTURES_BANNED, ban);
            ctx.getSource().sendSuccess(() -> Component.translatable(ban
                    ? "commands.lotr.banStructures.banPlayer" : "commands.lotr.allowStructures.allowPlayer",
                    player.getName()), true);
            player.sendSystemMessage(Component.translatable(ban ? "chat.lotr.banStructures" : "chat.lotr.allowStructures"));
        }
        return players.size();
    }
}
