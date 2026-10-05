package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRegistry: other creatures -- another mod's, or vanilla's -- given
 * a place in the alignment system by {@code config/LOTR_EntityRegistry.txt}:
 * a faction (which targeting, the attack rules and everything else asking an
 * entity's faction then see), whether it hunts that faction's enemies, and
 * what killing one is worth. The file is written, with its explanation, the
 * first time the mod starts without one.
 *
 * <p>One line per creature: {@code name=<id>,faction=<faction>,targetEnemies=<true|false>,bonus=<n>}.
 * The original named an entity by its 1.7.10 name ({@code modid.Name}); the
 * port names it by its entity id ({@code modid:name}). The mod's own NPCs
 * cannot be registered, as before.
 */
public final class LOTREntityRegistry {

    public record RegistryInfo(String name, LOTRFaction alignmentFaction, boolean shouldTargetEnemies, int bonus) {

        /** The kill's alignment bonus, named by the creature's own name. */
        public LOTRAlignmentValues.AlignmentBonus alignmentBonus(Entity entity) {
            LOTRAlignmentValues.AlignmentBonus alignmentBonus =
                    new LOTRAlignmentValues.AlignmentBonus(this.bonus, entity.getType().getDescriptionId());
            alignmentBonus.needsTranslation = true;
            return alignmentBonus;
        }
    }

    private static final String FILE_NAME = "LOTR_EntityRegistry.txt";
    private static final Map<String, RegistryInfo> REGISTERED_NPCS = new HashMap<>();

    private LOTREntityRegistry() {
    }

    public static @Nullable RegistryInfo get(@Nullable Entity entity) {
        if (entity == null || REGISTERED_NPCS.isEmpty()) {
            return null;
        }
        return REGISTERED_NPCS.get(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    /** loadRegisteredNPCs, and the onEntityJoinWorld half: registered target-seekers get the LOTR target tasks. */
    public static void init() {
        loadRegisteredNPCs();
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            RegistryInfo info = get(entity);
            if (info != null && info.shouldTargetEnemies() && entity instanceof PathfinderMob creature
                    && !(entity instanceof LOTRNPCEntity)) {
                creature.targetSelector.addGoal(100, LOTRNearestAttackableTargetGoal.forPlayers(creature));
                creature.targetSelector.addGoal(100, LOTRNearestAttackableTargetGoal.forFactions(creature));
            }
        });
    }

    private static void loadRegisteredNPCs() {
        Path config = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        try {
            if (!Files.exists(config)) {
                writeDefault(config);
                return;
            }
            try (BufferedReader reader = Files.newBufferedReader(config, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isEmpty() && line.charAt(0) == '﻿') {
                        line = line.substring(1);
                    }
                    RegistryInfo info = parse(line);
                    if (info != null) {
                        REGISTERED_NPCS.put(info.name(), info);
                        LOTRMod.LOGGER.info("Successfully registered entity {} with the LOTR alignment system as {}",
                                info.name(), line);
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            LOTRMod.LOGGER.error("LOTR: could not read {}", config, e);
        }
    }

    private static @Nullable RegistryInfo parse(String line) {
        if (line.isEmpty() || line.charAt(0) == '#' || !line.startsWith("name=")) {
            return null;
        }
        String s = line.substring("name=".length());
        if (s.toLowerCase(Locale.ROOT).startsWith("lotr")) {
            return null;
        }
        int i = s.indexOf(",faction=");
        int j = s.indexOf(",targetEnemies=");
        int k = s.indexOf(",bonus=");
        if (i < 0 || j < 0 || k < 0) {
            return null;
        }
        String name = s.substring(0, i);
        LOTRFaction faction = LOTRFaction.forName(s.substring(i + ",faction=".length(), j));
        if (name.isEmpty() || faction == null) {
            return null;
        }
        String targetEnemies = s.substring(j + ",targetEnemies=".length(), k);
        if (!"true".equals(targetEnemies) && !"false".equals(targetEnemies)) {
            return null;
        }
        int bonus = Integer.parseInt(s.substring(k + ",bonus=".length()));
        return new RegistryInfo(name, faction, "true".equals(targetEnemies), bonus);
    }

    private static void writeDefault(Path config) throws IOException {
        StringJoiner allFactions = new StringJoiner(", ");
        for (LOTRFaction faction : LOTRFaction.values()) {
            if (faction.allowEntityRegistry) {
                allFactions.add(faction.codeName());
            }
        }
        Files.createDirectories(config.getParent());
        try (PrintStream writer = new PrintStream(Files.newOutputStream(config), true, StandardCharsets.UTF_8)) {
            writer.println("#Lines starting with '#' will be ignored");
            writer.println("#");
            writer.println("#Use this file to register entities with the LOTR alignment system.");
            writer.println("#");
            writer.println("#An example format for registering an entity is as follows: (do not use spaces)");
            writer.println("#name=minecraft:pillager,faction=" + LOTRFaction.MORDOR.codeName() + ",targetEnemies=true,bonus=1");
            writer.println("#");
            writer.println("#'name' is the entity's id: the mod ID, a ':' and the entity name, as /summon takes it.");
            writer.println("#Vanilla entities have the mod ID \"minecraft\". The LOTR mod's own NPCs cannot be registered.");
            writer.println("#");
            writer.println("#'faction' can be " + allFactions);
            writer.println("#");
            writer.println("#'targetEnemies' can be true or false.");
            writer.println("#If true, the entity will be equipped with AI modules to target its enemies.");
            writer.println("#Actual combat behaviour may or may not be present, depending on whether the entity is designed with combat AI modules.");
            writer.println("#");
            writer.println("#'bonus' is the alignment bonus awarded to a player who kills the entity.");
            writer.println("#It can be positive, negative, or zero, in which case no bonus will be awarded.");
            writer.println("#");
        }
    }
}
