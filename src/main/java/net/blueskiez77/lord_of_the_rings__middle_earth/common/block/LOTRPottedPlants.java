package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;

/**
 * The mod's plants in a flower pot. LOTRBlockFlowerPot.canAcceptPlant let a
 * vanilla pot take any LOTRBlockFlower -- the flowers and herbs, the clovers,
 * and through LOTRBlockSaplingBase every one of the mod's saplings -- and
 * swapped it for its own pot with the plant inside. The mod's pot is not
 * ported (vanilla's covers it); a potted form of each plant is, as vanilla
 * pots its own: right-clicking a pot with one fills it, and structures plant
 * them through {@code pottedFor}.
 */
public final class LOTRPottedPlants {

    /** The plants, by their port names, in the original's registration order. */
    private static final List<String> PLANTS = List.of(
            "simbelmyne", "shire_pine_sapling", "mallorn_sapling", "mirk_oak_sapling", "mirk_oak_red_sapling",
            "shire_heather", "pipeweed_plant", "elanor", "niphredil", "athelas",
            "apple_sapling", "pear_sapling", "cherry_sapling", "mango_sapling", "bluebell", "morgul_shroom",
            "clover", "four_leaf_clover", "dead_marsh_plant", "asphodel",
            "lebethron_sapling", "beech_sapling", "holly_sapling", "banana_sapling", "dwarf_herb", "mordor_thorn",
            "maple_sapling", "larch_sapling", "date_palm_sapling", "mangrove_sapling",
            "fangorn_plant_green", "fangorn_plant_brown", "fangorn_plant_gold", "fangorn_plant_yellow",
            "fangorn_plant_red", "fangorn_plant_silver",
            "red_harad_flower", "yellow_harad_flower", "harad_flower_daisy", "pink_harad_flower", "flax_plant",
            "chestnut_sapling", "baobab_sapling", "cedar_sapling", "fir_sapling",
            "pine_sapling", "lemon_sapling", "orange_sapling", "lime_sapling", "corrupt_mallorn",
            "mahogany_sapling", "willow_sapling", "cypress_sapling", "olive_sapling",
            "aspen_sapling", "green_oak_sapling", "lairelosse_sapling", "almond_sapling", "morgul_flower", "blackroot",
            "plum_sapling", "redwood_sapling", "pomegranate_sapling", "palm_sapling", "marigold",
            "rhun_flower_chrys_blue", "rhun_flower_chrys_orange", "rhun_flower_chrys_pink",
            "rhun_flower_chrys_yellow", "rhun_flower_chrys_white",
            "dragon_sapling", "kanuka_sapling", "lavender");

    /** Plant to its potted form. */
    public static final Map<Block, Block> POTTED = new LinkedHashMap<>();

    private LOTRPottedPlants() {
    }

    /** After every plant is registered. */
    static void init() {
        for (String name : PLANTS) {
            Block plant = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
            if (plant == Blocks.AIR) {
                throw new IllegalStateException("No LOTR plant " + name + " to pot");
            }
            POTTED.put(plant, LOTRBlocks.register("potted_" + name, p -> new FlowerPotBlock(plant, p),
                    Blocks.flowerPotProperties(), false));
        }
    }
}
