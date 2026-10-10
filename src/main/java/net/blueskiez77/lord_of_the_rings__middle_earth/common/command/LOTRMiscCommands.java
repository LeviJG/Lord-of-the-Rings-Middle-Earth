package net.blueskiez77.lord_of_the_rings__middle_earth.common.command;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSpawnDamping;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRInvasionSpawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRCreatureType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRCommandPledgeCooldown, LOTRCommandEnchant, LOTRCommandDate and LOTRCommandAchievement: an
 * operator's /pledgeCooldown, /lotrEnchant (the mod's modifiers, which the original called
 * enchantments), /lotrDate and /lotrAchievement.
 *
 * <p>Not ported: /lotr_summon, which only offered the mod's 1.7.10 entity names to /summon; today's
 * /summon already offers and takes {@code lotr:} entities. /strscan, the authors' tool for recording
 * structure scans in 1.7.10 block ids, has nothing to write the port's scans with.
 */
public final class LOTRMiscCommands {

    private static final SimpleCommandExceptionType NO_ITEM = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.lotrEnchant.noItem"));
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.lotrEnchant.unknown", name));
    private static final Dynamic2CommandExceptionType CANNOT_ADD = new Dynamic2CommandExceptionType(
            (name, item) -> Component.translatable("commands.lotr.lotrEnchant.cannotAdd", name, item));
    private static final Dynamic2CommandExceptionType CANNOT_REMOVE = new Dynamic2CommandExceptionType(
            (name, item) -> Component.translatable("commands.lotr.lotrEnchant.cannotRemove", name, item));
    private static final DynamicCommandExceptionType UNKNOWN_ACHIEVEMENT = new DynamicCommandExceptionType(
            name -> Component.translatable("commands.lotr.lotrAchievement.unknown", name));
    private static final Dynamic2CommandExceptionType GIVE_FAIL = new Dynamic2CommandExceptionType(
            (player, ach) -> Component.translatable("commands.lotr.lotrAchievement.give.fail", player, ach));
    private static final Dynamic2CommandExceptionType REMOVE_FAIL = new Dynamic2CommandExceptionType(
            (player, ach) -> Component.translatable("commands.lotr.lotrAchievement.remove.fail", player, ach));
    private static final SimpleCommandExceptionType OUT_OF_BOUNDS = new SimpleCommandExceptionType(
            Component.translatable("commands.lotr.lotrDate.outOfBounds"));

    private LOTRMiscCommands() {
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerPledgeCooldown(dispatcher);
            registerEnchant(dispatcher);
            registerDate(dispatcher);
            registerAchievement(dispatcher);
            registerTravel(dispatcher);
            registerSpawnDamping(dispatcher);
            registerInvasion(dispatcher);
        });
    }

    /** LOTRLevelData.getHMSTime_Ticks: "1h 2m 3s", "2m 3s" or "3s". */
    public static Component hmsTime(int ticks) {
        int hours = ticks / 72000;
        int minutes = ticks % 72000 / 1200;
        int seconds = ticks % 72000 % 1200 / 20;
        Component sHours = Component.translatable("lotr.gui.time.hours", hours);
        Component sMinutes = Component.translatable("lotr.gui.time.minutes", minutes);
        Component sSeconds = Component.translatable("lotr.gui.time.seconds", seconds);
        if (hours > 0) {
            return Component.translatable("lotr.gui.time.format.hms", sHours, sMinutes, sSeconds);
        }
        if (minutes > 0) {
            return Component.translatable("lotr.gui.time.format.ms", sMinutes, sSeconds);
        }
        return Component.translatable("lotr.gui.time.format.s", sSeconds);
    }

    // ---------------------------------------------------------------- /pledgeCooldown

    private static void registerPledgeCooldown(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pledgeCooldown")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("ticks", IntegerArgumentType.integer(0, 10000000))
                        .executes(ctx -> setPledgeCooldown(ctx, ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> setPledgeCooldown(ctx, EntityArgument.getPlayer(ctx, "player"))))));
    }

    private static int setPledgeCooldown(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        int cd = IntegerArgumentType.getInteger(ctx, "ticks");
        LOTRPlayerAlignments.setPledgeBreakCooldownByCommand(player, cd);
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.pledgeCooldown.set", player.getName(), cd,
                hmsTime(cd)), true);
        return 1;
    }

    // ---------------------------------------------------------------- /lotrEnchant

    private static ItemStack heldItem(ServerPlayer player) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            throw NO_ITEM.create();
        }
        return stack;
    }

    /** addTabCompletionOptions: what could go on the held item, or what is on it. */
    private static SuggestionProvider<CommandSourceStack> modifierSuggestions(boolean adding) {
        return (ctx, builder) -> {
            List<String> names = new ArrayList<>();
            try {
                ItemStack stack = EntityArgument.getPlayer(ctx, "player").getMainHandItem();
                if (!stack.isEmpty()) {
                    for (LOTRModifier modifier : LOTRModifier.values()) {
                        boolean has = LOTRModifiers.has(stack, modifier);
                        if (adding ? !has && LOTRModifiers.canApply(modifier, stack, false)
                                && LOTRModifiers.compatibleWithAll(stack, modifier) : has) {
                            names.add(modifier.getSerializedName());
                        }
                    }
                }
            } catch (CommandSyntaxException ignored) {
            }
            return SharedSuggestionProvider.suggest(names, builder);
        };
    }

    private static void registerEnchant(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lotrEnchant")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("add").then(Commands.argument("enchantment", StringArgumentType.word())
                                .suggests(modifierSuggestions(true)).executes(ctx -> enchant(ctx, true))))
                        .then(Commands.literal("remove").then(Commands.argument("enchantment", StringArgumentType.word())
                                .suggests(modifierSuggestions(false)).executes(ctx -> enchant(ctx, false))))
                        .then(Commands.literal("clear").executes(ctx -> {
                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                            ItemStack stack = heldItem(player);
                            LOTRModifiers.clearWithProgress(stack);
                            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.lotrEnchant.clear",
                                    player.getName(), stack.getHoverName()), true);
                            return 1;
                        }))));
    }

    private static int enchant(CommandContext<CommandSourceStack> ctx, boolean add) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        ItemStack stack = heldItem(player);
        String name = StringArgumentType.getString(ctx, "enchantment");
        LOTRModifier modifier = LOTRModifier.byName(name);
        if (modifier == null) {
            throw UNKNOWN.create(name);
        }
        List<LOTRModifier> current = new ArrayList<>(LOTRModifiers.get(stack));
        if (add) {
            if (current.contains(modifier) || !LOTRModifiers.canApply(modifier, stack, false)
                    || !LOTRModifiers.compatibleWithAll(stack, modifier)) {
                throw CANNOT_ADD.create(name, stack.getHoverName());
            }
            current.add(modifier);
        } else {
            if (!current.remove(modifier)) {
                throw CANNOT_REMOVE.create(name, stack.getHoverName());
            }
        }
        LOTRModifiers.set(stack, current);
        ctx.getSource().sendSuccess(() -> Component.translatable(add ? "commands.lotr.lotrEnchant.add"
                : "commands.lotr.lotrEnchant.remove", name, player.getName(), stack.getHoverName()), true);
        return 1;
    }

    // ---------------------------------------------------------------- /invasion

    private static final DynamicCommandExceptionType NO_INVASION_TYPE = new DynamicCommandExceptionType(type ->
            Component.translatable("commands.lotr.invasion.noType", type));

    private static final SuggestionProvider<CommandSourceStack> INVASION_TYPES = (ctx, builder) ->
            SharedSuggestionProvider.suggest(java.util.Arrays.stream(LOTRInvasions.values()).map(LOTRInvasions::codeName), builder);

    /** /invasion type [x y z [size]]: an invasion here (three blocks up) or there, 30 to 70 strong unless told. */
    private static void registerInvasion(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("invasion")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("type", StringArgumentType.word()).suggests(INVASION_TYPES)
                        .executes(ctx -> startInvasion(ctx, null, -1))
                        .then(Commands.argument("pos", net.minecraft.commands.arguments.coordinates.Vec3Argument.vec3())
                                .executes(ctx -> startInvasion(ctx, net.minecraft.commands.arguments.coordinates.Vec3Argument.getVec3(ctx, "pos"), -1))
                                .then(Commands.argument("size", IntegerArgumentType.integer(0, 10000))
                                        .executes(ctx -> startInvasion(ctx, net.minecraft.commands.arguments.coordinates.Vec3Argument.getVec3(ctx, "pos"),
                                                IntegerArgumentType.getInteger(ctx, "size")))))));
    }

    private static int startInvasion(CommandContext<CommandSourceStack> ctx, net.minecraft.world.phys.@org.jspecify.annotations.Nullable Vec3 at, int size)
            throws CommandSyntaxException {
        String typeName = StringArgumentType.getString(ctx, "type");
        LOTRInvasions type = LOTRInvasions.forName(typeName);
        if (type == null) {
            throw NO_INVASION_TYPE.create(typeName);
        }
        CommandSourceStack source = ctx.getSource();
        net.minecraft.core.BlockPos here = net.minecraft.core.BlockPos.containing(source.getPosition());
        double posX = here.getX() + 0.5;
        double posY = here.getY();
        double posZ = here.getZ() + 0.5;
        if (at != null) {
            posX = at.x;
            posY = at.y;
            posZ = at.z;
        } else {
            posY += 3.0;
        }
        LOTRInvasionSpawnerEntity invasion = LOTREntities.INVASION_SPAWNER.create(source.getLevel(), net.minecraft.world.entity.EntitySpawnReason.COMMAND);
        if (invasion == null) {
            return 0;
        }
        invasion.setInvasionType(type);
        invasion.snapTo(posX, posY, posZ, 0.0f, 0.0f);
        source.getLevel().addFreshEntity(invasion);
        invasion.selectAppropriateBonusFactions();
        invasion.startInvasion(source.getPlayer(), size);
        int x = (int) posX;
        int y = (int) posY;
        int z = (int) posZ;
        source.sendSuccess(() -> Component.translatable("commands.lotr.invasion.start", type.invasionName(), invasion.getInvasionSize(), x, y, z), true);
        return 1;
    }

    // ---------------------------------------------------------------- /spawnDamping

    private static final DynamicCommandExceptionType NO_CREATURE_TYPE = new DynamicCommandExceptionType(type ->
            Component.translatable("commands.lotr.spawnDamping.noType", type));

    private static final SuggestionProvider<CommandSourceStack> SPAWN_TYPES = (ctx, builder) -> {
        List<String> types = new ArrayList<>();
        for (LOTRCreatureType type : LOTRCreatureType.values()) {
            types.add(type.typeName);
        }
        types.add(LOTRSpawnDamping.TYPE_NPC);
        return SharedSuggestionProvider.suggest(types, builder);
    };

    private static String spawnType(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String type = StringArgumentType.getString(ctx, "type");
        if (!type.equals(LOTRSpawnDamping.TYPE_NPC) && LOTRCreatureType.forName(type) == null) {
            throw NO_CREATURE_TYPE.create(type);
        }
        return type;
    }

    /** /spawnDamping set, calc or reset: how much each kind of spawning's cap shrinks per extra player. */
    private static void registerSpawnDamping(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnDamping")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("reset").executes(ctx -> {
                    LOTRSpawnDamping.resetAll(ctx.getSource().getServer());
                    ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.spawnDamping.reset"), true);
                    return 1;
                }))
                .then(Commands.literal("set").then(Commands.argument("type", StringArgumentType.word()).suggests(SPAWN_TYPES)
                        .then(Commands.argument("damping", FloatArgumentType.floatArg(0.0f, 1.0f)).executes(ctx -> {
                            String type = spawnType(ctx);
                            float damping = FloatArgumentType.getFloat(ctx, "damping");
                            LOTRSpawnDamping.setSpawnDamping(ctx.getSource().getServer(), type, damping);
                            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.spawnDamping.set", type, damping), true);
                            return 1;
                        }))))
                .then(Commands.literal("calc").then(Commands.argument("type", StringArgumentType.word()).suggests(SPAWN_TYPES)
                        .executes(ctx -> {
                            String type = spawnType(ctx);
                            var level = ctx.getSource().getLevel();
                            float damping = LOTRSpawnDamping.getSpawnDamping(ctx.getSource().getServer(), type);
                            int players = level.players().size();
                            int expectedChunks = 196;
                            int baseCap = LOTRSpawnDamping.getBaseSpawnCapForInfo(type, level);
                            int cap = LOTRSpawnDamping.getSpawnCap(ctx.getSource().getServer(), type, baseCap, players);
                            int capXPlayers = cap * players;
                            String dimension = level.dimension().identifier().toString();
                            ctx.getSource().sendSystemMessage(Component.translatable("commands.lotr.spawnDamping.calc", dimension,
                                    dimension, type, damping, players, expectedChunks, cap, baseCap, capXPlayers)
                                    .withStyle(net.minecraft.ChatFormatting.GREEN));
                            return cap;
                        }))));
    }

    // ---------------------------------------------------------------- /lotrDate

    private static void registerDate(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lotrDate")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("get").executes(ctx -> {
                    int date = LOTRDate.ShireReckoning.currentDay;
                    String dateName = LOTRDate.ShireReckoning.getShireDate().getDateName(false);
                    ctx.getSource().sendSystemMessage(Component.translatable("commands.lotr.lotrDate.get", date, dateName));
                    return date;
                }))
                .then(Commands.literal("set").then(Commands.argument("date", IntegerArgumentType.integer())
                        .executes(ctx -> setDate(ctx, IntegerArgumentType.getInteger(ctx, "date")))))
                .then(Commands.literal("add").then(Commands.argument("date", IntegerArgumentType.integer())
                        .executes(ctx -> setDate(ctx, LOTRDate.ShireReckoning.currentDay
                                + IntegerArgumentType.getInteger(ctx, "date"))))));
    }

    private static int setDate(CommandContext<CommandSourceStack> ctx, int newDate) throws CommandSyntaxException {
        if (Math.abs(newDate) > 1000000) {
            throw OUT_OF_BOUNDS.create();
        }
        LOTRLevelData.setShireDate(newDate);
        String dateName = LOTRDate.ShireReckoning.getShireDate().getDateName(false);
        ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.lotrDate.set", newDate, dateName), true);
        return 1;
    }

    // ---------------------------------------------------------------- /lotrAchievement

    private static final SuggestionProvider<CommandSourceStack> ACHIEVEMENTS = (ctx, builder) ->
            SharedSuggestionProvider.suggest(LOTRAchievement.getAllAchievements().stream().map(LOTRAchievement::getCodeName), builder);

    private static void registerAchievement(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lotrAchievement")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("give").then(Commands.argument("achievement", StringArgumentType.word()).suggests(ACHIEVEMENTS)
                        .executes(ctx -> giveOrRemove(ctx, true, ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> giveOrRemove(ctx, true, EntityArgument.getPlayer(ctx, "player"))))))
                .then(Commands.literal("remove").then(Commands.argument("achievement", StringArgumentType.word())
                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(java.util.stream.Stream.concat(
                                java.util.stream.Stream.of("all"),
                                LOTRAchievement.getAllAchievements().stream().map(LOTRAchievement::getCodeName)), builder))
                        .executes(ctx -> giveOrRemove(ctx, false, ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> giveOrRemove(ctx, false, EntityArgument.getPlayer(ctx, "player")))))));
    }

    private static int giveOrRemove(CommandContext<CommandSourceStack> ctx, boolean give, ServerPlayer player)
            throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "achievement");
        if (!give && "all".equalsIgnoreCase(name)) {
            LOTRPlayerAchievements.clearAchievements(player);
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.lotrAchievement.removeAll", player.getName()), true);
            return 1;
        }
        LOTRAchievement ach = LOTRAchievement.findByName(name);
        if (ach == null) {
            throw UNKNOWN_ACHIEVEMENT.create(name);
        }
        boolean has = LOTRPlayerAchievements.hasAchievement(player, ach);
        if (give) {
            if (has) {
                throw GIVE_FAIL.create(player.getName(), ach.getTitle(player));
            }
            LOTRPlayerAchievements.addAchievement(player, ach);
        } else {
            if (!has) {
                throw REMOVE_FAIL.create(player.getName(), ach.getTitle(player));
            }
            LOTRPlayerAchievements.removeAchievement(player, ach);
        }
        ctx.getSource().sendSuccess(() -> Component.translatable(give ? "commands.lotr.lotrAchievement.give"
                : "commands.lotr.lotrAchievement.remove", player.getName(), ach.getTitle(player)), true);
        return 1;
    }

    /**
     * /lotrTravel [players]: an operator's way to and from Middle-earth until the Ring portal
     * (D15) -- to the same x and z in the other world, on its surface.
     */
    private static void registerTravel(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lotrTravel")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(ctx -> travel(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException())))
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> travel(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets")))));
    }

    private static int travel(CommandSourceStack source, java.util.Collection<ServerPlayer> players) {
        net.minecraft.server.MinecraftServer server = source.getServer();
        net.minecraft.server.level.ServerLevel middleEarth = server.getLevel(
                net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen.MIDDLE_EARTH);
        if (middleEarth == null) {
            source.sendFailure(Component.translatable("commands.lotr.travel.noDimension"));
            return 0;
        }
        for (ServerPlayer player : players) {
            boolean leaving = player.level() == middleEarth;
            net.minecraft.server.level.ServerLevel target = leaving ? server.overworld() : middleEarth;
            int x = player.getBlockX();
            int z = player.getBlockZ();
            int y = target.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);
            player.teleportTo(target, x + 0.5, y, z + 0.5, java.util.Set.of(), player.getYRot(), player.getXRot(), true);
            source.sendSuccess(() -> Component.translatable(leaving ? "commands.lotr.travel.left" : "commands.lotr.travel.entered",
                    player.getDisplayName()), true);
        }
        return players.size();
    }

}
