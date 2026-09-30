package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.*;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRStructures: every structure a structure spawner can build, by the
 * original's id, with its name (for {@code lotr.structure.<name>}) and the two
 * colours its spawner is drawn in.
 *
 * <p>Registered here as each people's structures are ported (D11); villages
 * join with the village generators.
 */
public final class LOTRStructures {

    private static final Map<Integer, StructureInfo> STRUCTURES = new LinkedHashMap<>();

    /** A structure or a village a spawner builds, standing on the block above the one used. */
    public interface IStructureProvider {
        boolean generateStructure(ServerLevel level, Player player, int i, int j, int k);

        boolean isVillage();
    }

    /** StructureColorInfo, and the name and builder with it. */
    public record StructureInfo(int id, String name, IStructureProvider provider, int colorBackground,
                                int colorForeground, boolean isVillage, boolean isHidden) {
    }

    private LOTRStructures() {
    }

    public static void init() {
        registerStructure(1, LOTRHobbitHoleStructure::new, "HobbitHole", 2727977, 8997164);
        registerStructure(2, LOTRHobbitTavernStructure::new, "HobbitTavern", 9324081, 15975807);
        registerOldStructure(3, LOTRHobbitPicnicBenchStructure::new, "HobbitPicnicBench", 7032622, 13882323);
        registerStructure(4, LOTRHobbitWindmillStructure::new, "HobbitWindmill", 9324081, 15975807);
        registerStructure(5, LOTRHobbitFarmStructure::new, "HobbitFarm", 9324081, 15975807);
        registerStructure(6, LOTRHayBalesStructure::new, "HayBale", 14863437, 11499334);
        registerStructure(7, LOTRHobbitHouseStructure::new, "HobbitHouse", 9324081, 15975807);
        registerStructure(8, LOTRHobbitBurrowStructure::new, "HobbitBurrow", 9324081, 15975807);
    }

    public static int getRotationFromPlayer(Player player) {
        return Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 3;
    }

    public static @Nullable StructureInfo get(int id) {
        return STRUCTURES.get(id);
    }

    public static Iterable<StructureInfo> all() {
        return STRUCTURES.values();
    }

    public static void registerStructure(int id, java.util.function.Function<Boolean, ? extends LOTRStructureBase2> factory, String name,
                                         int colorBG, int colorFG) {
        registerStructure(id, factory, name, colorBG, colorFG, false);
    }

    /**
     * A structure built fresh each time, with notifications on, free of the
     * restrictions natural generation checks, facing the way the player does.
     */
    public static void registerStructure(int id, java.util.function.Function<Boolean, ? extends LOTRStructureBase2> factory, String name,
                                         int colorBG, int colorFG, boolean hide) {
        registerStructure(id, new IStructureProvider() {
            @Override
            public boolean generateStructure(ServerLevel level, Player player, int i, int j, int k) {
                LOTRStructureBase2 str = factory.apply(true);
                str.restrictions = false;
                str.usingPlayer = player;
                return str.generateAndFinish(level, level.getRandom(), i, j, k, str.usingPlayerRotation());
            }

            @Override
            public boolean isVillage() {
                return false;
            }
        }, name, colorBG, colorFG, hide);
    }

    /** registerStructure for a structure on the older base (LOTRWorldGenStructureBase): generate, as it lies. */
    public static void registerOldStructure(int id, java.util.function.Function<Boolean, ? extends LOTRStructureBase> factory,
                                            String name, int colorBG, int colorFG) {
        registerStructure(id, new IStructureProvider() {
            @Override
            public boolean generateStructure(ServerLevel level, Player player, int i, int j, int k) {
                LOTRStructureBase str = factory.apply(true);
                str.restrictions = false;
                str.usingPlayer = player;
                return str.generateAndFinish(level, level.getRandom(), i, j, k);
            }

            @Override
            public boolean isVillage() {
                return false;
            }
        }, name, colorBG, colorFG, false);
    }

    public static void registerStructure(int id, IStructureProvider provider, String name, int colorBG, int colorFG,
                                         boolean hide) {
        if (STRUCTURES.containsKey(id)) {
            throw new IllegalArgumentException("Structure ID " + id + " is already registered to " + name + "!");
        }
        STRUCTURES.put(id, new StructureInfo(id, name, provider, colorBG, colorFG, provider.isVillage(), hide));
    }
}
