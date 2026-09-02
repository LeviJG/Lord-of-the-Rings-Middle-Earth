package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BedPart;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTreasurePileBlock;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LOTRBlockLootProvider extends FabricBlockLootSubProvider {
    public LOTRBlockLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    /**
     * A full pile gives back one whole-block item, exactly as the original did
     * -- damageDropped returned the metadata, so metadata 7 came back as the
     * metadata 7 item. Any shallower pile gives that many carpets, since those
     * are the only two forms the item ever took.
     */
    private LootTable.Builder treasurePileDrops(Block pile) {
        Item carpet = LOTRBlocks.TREASURE_PILE_CARPETS.get(pile);
        Item whole = LOTRBlocks.TREASURE_PILE_BLOCKS.get(pile);

        LootItem.Builder<?> carpets = LootItem.lootTableItem(carpet);
        for (int layers = 2; layers < LOTRTreasurePileBlock.MAX_LAYERS; layers++) {
            carpets = carpets.apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers))
                    .when(layersAre(pile, layers)));
        }

        return LootTable.lootTable().withPool(applyExplosionCondition(pile,
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(whole)
                                .when(layersAre(pile, LOTRTreasurePileBlock.MAX_LAYERS)))
                        .add(carpets)));
    }

    private static LootItemBlockStatePropertyCondition.Builder layersAre(Block pile, int layers) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(pile)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(LOTRTreasurePileBlock.LAYERS, layers));
    }

    @Override
    public void generate() {
        LOTRBlocks.ALL_GLASS.forEach(this::dropWhenSilkTouch);
        LOTRBlocks.ALL_GLASS_PANES.forEach(this::dropWhenSilkTouch);

        // Farmland has no item of its own. Vanilla farmland drops dirt; the LOTR
        // one is tilled mud, so it drops mud.
        // Vanilla dirt path and farmland both drop dirt, with no silk-touch
        // special case. The LOTR pair are worked mud, so they drop mud.
        LOTRBlocks.ALL_FARMLAND.forEach(b -> dropOther(b, LOTRBlocks.MUD));
        dropOther(LOTRBlocks.DIRT_PATH_MUD, LOTRBlocks.MUD);
        LOTRBlocks.ALL_RAILS.forEach(this::dropSelf);

        // NOTE: crops deliberately have no loot table. They should drop their

        List<List<Block>> dropsSelf = List.of(
                LOTRBlocks.ALL_CUBES,
                LOTRBlocks.ALL_PLANKS,
                LOTRBlocks.ALL_SAPLINGS,
                LOTRBlocks.ALL_TRAPDOORS,
                LOTRBlocks.ALL_BARS,
                LOTRBlocks.ALL_CHANDELIERS,
                LOTRBlocks.ALL_LOGS,
                LOTRBlocks.ALL_BEAMS,
                LOTRBlocks.ALL_PILLARS,
                LOTRBlocks.ALL_STAIRS,
                LOTRBlocks.ALL_FENCES,
                LOTRBlocks.ALL_WALLS,
                LOTRBlocks.ALL_COLUMNS,

                LOTRBlocks.ALL_BOTTOM_TOP,
                LOTRBlocks.ALL_SOIL_COLUMNS,
                LOTRBlocks.ALL_CARPETS,
                LOTRBlocks.ALL_TORCHES,
                LOTRBlocks.ALL_CRAFTING_TABLES,
                LOTRBlocks.ALL_VINES,
                LOTRBlocks.ALL_LADDERS,
                LOTRBlocks.ALL_GATES,
                LOTRBlocks.ALL_BUSHES,
                LOTRBlocks.ALL_FENCE_GATES,
                LOTRBlocks.ALL_BUTTONS,
                LOTRBlocks.ALL_PRESSURE_PLATES);

        // Plants. Most are LOTRBlockFlower and drop themselves, which is the
        // fallback at the bottom; these are the ones that did something else.
        //
        // LOTRBlockGrass.getDrops delegated to Blocks.tallgrass.getDrops and the
        // class was IShearable, so the grasses drop wheat seeds at 1/8 scaled by
        // Fortune, or themselves when cut with shears. createGrassDrops is that
        // table. The thistle is NOT here: LOTRBlockTallGrass.getDrops special-
        // cased meta 3 to always return itself, so it stays a plain dropSelf.
        List.of(LOTRBlocks.TALL_GRASS_SHORT, LOTRBlocks.TALL_GRASS_FLOWER,
                        LOTRBlocks.TALL_GRASS_WHEAT, LOTRBlocks.TALL_GRASS_NETTLE,
                        LOTRBlocks.TALL_GRASS_FERNSPROUT, LOTRBlocks.ARID_GRASS)
                .forEach(b -> add(b, createGrassDrops(b)));

        // getItemDropped returned null and onSheared returned the block, so
        // these three give nothing at all unless you use shears.
        List.of(LOTRBlocks.MORDOR_GRASS, LOTRBlocks.MORDOR_THORN, LOTRBlocks.DEAD_MARSH_PLANT)
                .forEach(b -> add(b, createShearsOnlyDrop(b)));

        // LOTRBlockCorruptMallorn.getDrops returned new ItemStack(LOTRMod.sapling,
        // 1, 1) -- sapling meta 1 is the mallorn -- never the corrupt block.
        dropOther(LOTRBlocks.CORRUPT_MALLORN, LOTRBlocks.MALLORN_SAPLING);

        // Everything else in the family drops itself.
        List<Block> plantsWithOwnTable = List.of(LOTRBlocks.TALL_GRASS_SHORT,
                LOTRBlocks.TALL_GRASS_FLOWER, LOTRBlocks.TALL_GRASS_WHEAT,
                LOTRBlocks.TALL_GRASS_NETTLE, LOTRBlocks.TALL_GRASS_FERNSPROUT,
                LOTRBlocks.ARID_GRASS, LOTRBlocks.MORDOR_GRASS, LOTRBlocks.MORDOR_THORN,
                LOTRBlocks.DEAD_MARSH_PLANT, LOTRBlocks.CORRUPT_MALLORN);
        LOTRBlocks.ALL_FLOWERS.stream()
                .filter(b -> !plantsWithOwnTable.contains(b))
                .forEach(this::dropSelf);

        // Leaves behave like vanilla leaves: shears or Silk Touch give the leaf
        // block back, otherwise a sapling of the same species at the vanilla
        // fortune-scaled chance, plus the vanilla stick drop.
        LOTRBlocks.LEAVES_SAPLING.forEach((leaves, sapling) ->
                add(leaves, createLeavesDrops(leaves, sapling, NORMAL_LEAVES_SAPLING_CHANCES)));

        dropSelf(LOTRBlocks.WEB_UNGOLIANT);

        // dropSelf is right: LOTRAnimalJarBlock.getDrops stamps the caged bird
        // onto whatever the table produces.
        LOTRBlocks.ALL_ANIMAL_JARS.forEach(this::dropSelf);
        dropSelf(LOTRBlocks.WEAPON_RACK);

        // Treasure piles drop one item per layer, the way vanilla's snow layers
        // do -- see the divergence note on LOTRTreasurePileBlock.
        LOTRBlocks.ALL_TREASURE_PILES.forEach(pile -> add(pile, this::treasurePileDrops));

        // Banners: the standing form drops itself, and the wall form drops the
        // standing one, since the two share a single item.
        LOTRBlocks.ALL_BANNERS.forEach(this::dropSelf);
        LOTRBlocks.BANNER_WALL_FORM.forEach((standing, wall) -> dropOther(wall, standing));
        // Two blocks, one torch: only the lower half drops, the way vanilla's
        // double plants do.
        add(LOTRBlocks.ORC_TORCH, createSinglePropConditionTable(LOTRBlocks.ORC_TORCH,
                DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        add(LOTRBlocks.TAUREDAIN_DOUBLE_TORCH,
                createSinglePropConditionTable(LOTRBlocks.TAUREDAIN_DOUBLE_TORCH,
                        DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));

        // In no family list, because its models are hand-written rather than
        // generated -- so it needs its loot table naming explicitly.
        dropSelf(LOTRBlocks.BEACON_OF_GONDOR);
        dropSelf(LOTRBlocks.HOBBIT_OVEN);
        dropSelf(LOTRBlocks.ENT_JAR);
        dropSelf(LOTRBlocks.TABLE_OF_COMMAND);
        dropSelf(LOTRBlocks.UNSMELTERY);
        dropSelf(LOTRBlocks.MILLSTONE);
        dropSelf(LOTRBlocks.ORC_CHAIN);
        // getItemDropped returned null for the head half, so a bed drops one
        // item, not two -- vanilla's own beds use exactly this condition.
        LOTRBlocks.ALL_BEDS.forEach(bed -> add(bed,
                createSinglePropConditionTable(bed, BedBlock.PART, BedPart.FOOT)));
        LOTRBlocks.ALL_KEBAB_STANDS.forEach(this::dropSelf);
        LOTRBlocks.ALL_CHESTS.forEach(this::dropSelf);
        // damageDropped(i) = i & 3: each part dropped its own kind.
        LOTRBlocks.ALL_TROLL_TOTEMS.forEach(this::dropSelf);

        LOTRBlocks.TORCH_WALL.forEach((torch, wall) -> dropOther(wall, torch));

        // A double slab must drop two items, so slabs get their own table

        LOTRBlocks.ALL_SLABS.forEach(b -> add(b, this::createSlabItemTable));

        dropsSelf.forEach(family -> family.forEach(this::dropSelf));

        LOTRBlocks.ALL_DOORS.forEach(door -> add(door, createDoorTable(door)));
    }
}