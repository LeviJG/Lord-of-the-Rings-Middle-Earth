package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.monster.Enemy;

/**
 * 1.7.10's EnumCreatureType, with the mod's own LOTRAmbient added: the kinds of creature the spawner
 * fills, each with its cap, whether it is peaceful, whether it is an animal (spawned only every 400
 * ticks), whether it spawns in water, and what counts towards it -- and each biome's list of it.
 */
public enum LOTRCreatureType {
    MONSTER("monster", 70, false, false, false, e -> e instanceof Enemy),
    CREATURE("creature", 10, true, true, false, e -> e instanceof Animal || e instanceof AmbientCreature || e instanceof WaterAnimal),
    AMBIENT("ambient", 15, true, false, false, e -> e instanceof AmbientCreature),
    WATER_CREATURE("waterCreature", 5, true, false, true, e -> e instanceof WaterAnimal),
    LOTR_AMBIENT("LOTRAmbient", 45, true, false, false, e -> e.getType().getCategory() == MobCategory.AMBIENT && !(e instanceof AmbientCreature));

    /** The name the spawn damping and its command know it by. */
    public final String typeName;
    public final int maxNumberOfCreature;
    public final boolean peacefulCreature;
    public final boolean animal;
    public final boolean water;
    private final Predicate<Entity> counts;

    LOTRCreatureType(String typeName, int max, boolean peaceful, boolean animal, boolean water, Predicate<Entity> counts) {
        this.typeName = typeName;
        this.maxNumberOfCreature = max;
        this.peacefulCreature = peaceful;
        this.animal = animal;
        this.water = water;
        this.counts = counts;
    }

    /** isCreatureType(type, true): of this kind, and not kept from despawning. */
    public boolean counts(Entity entity) {
        return this.counts.test(entity) && !(entity instanceof net.minecraft.world.entity.Mob mob && mob.isPersistenceRequired());
    }

    /** getSpawnableList: the biome's list of this kind. */
    public List<LOTRSpawnEntry> getSpawnableList(LOTRBiome biome) {
        return switch (this) {
            case MONSTER -> biome.spawnableMonsterList;
            case CREATURE -> biome.spawnableCreatureList;
            case AMBIENT -> biome.spawnableCaveCreatureList;
            case WATER_CREATURE -> biome.spawnableWaterCreatureList;
            case LOTR_AMBIENT -> biome.spawnableLOTRAmbientList;
        };
    }

    public static LOTRCreatureType forName(String name) {
        for (LOTRCreatureType type : values()) {
            if (type.typeName.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
