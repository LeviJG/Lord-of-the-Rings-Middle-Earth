package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BedPart;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBerryBushBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCornBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGrapevineBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTreasurePileBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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

    /**
     * LOTRBlockOre.quantityDropped with quantityDroppedWithBonus's fortune
     * multiplier, which is exactly vanilla's ore_drops formula. Silk Touch gives
     * the ore itself back, as canSilkHarvest allowed. limit 0 means no cap.
     */
    private LootTable.Builder oreDrops(Block ore, Item drop, float min, float max, int limit) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootItem.Builder<?> item = LootItem.lootTableItem(drop);
        item = item.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
        item = item.apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)));
        if (limit > 0) {
            item = item.apply(LimitCount.limitCount(IntRange.upperBound(limit)));
        }
        return createSilkTouchDispatchTable(ore, applyExplosionDecay(ore, item));
    }

    /**
     * LOTRBlockOreGem.quantityDropped / quantityDroppedWithBonus: one or two,
     * then {@code rand(fortune + 1)} more -- vanilla's uniform bonus with a
     * multiplier of one, not the ore formula. Silk Touch gives the ore back.
     */
    private LootTable.Builder gemDrops(Block ore, Item gem) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootItem.Builder<?> item = LootItem.lootTableItem(gem)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE), 1));
        return createSilkTouchDispatchTable(ore, applyExplosionDecay(ore, item));
    }

    /**
     * LOTRBlockLeavesBase.calcFortuneModifiedDropChance: one in {@code base},
     * less {@code 2 << fortune} per level, never under half the base.
     */
    private static float[] leafChances(int base) {
        float[] chances = new float[4];
        for (int fortune = 0; fortune < chances.length; fortune++) {
            int chance = base;
            if (fortune > 0) {
                chance -= 2 << fortune;
                chance = Math.max(chance, base / 2);
                chance = Math.max(chance, 1);
            }
            chances[fortune] = 1.0f / chance;
        }
        return chances;
    }

    /** addSpecialLeafDrops: a fruit at one in {@code base}, fortune-scaled the same way. */
    private LootTable.Builder withLeafFruit(Block leaves, LootTable.Builder table, int base, Item... fruits) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .when(doesNotHaveShearsOrSilkTouch())
                .when(BonusLevelTableCondition.bonusLevelFlatChance(
                        enchantments.getOrThrow(Enchantments.FORTUNE), leafChances(base)));
        for (Item fruit : fruits) {
            pool.add(applyExplosionCondition(leaves, LootItem.lootTableItem(fruit)));
        }
        return table.withPool(pool);
    }


    /** LOTRBlockBerryBush.getDrops: the bush, bare, and one to four berries if it bore them. */
    private LootTable.Builder berryBushDrops(Block bush, Item berry) {
        return createSingleItemTable(bush).withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(bush)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LOTRBerryBushBlock.HAS_BERRIES, true)))
                .add(applyExplosionDecay(bush, LootItem.lootTableItem(berry)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))));
    }

    /** LOTRBlockCorn.getDrops: the stalk, and a cob -- two, one time in four -- if it bore one. */
    private LootTable.Builder cornDrops(Block corn, Item cob) {
        LootItemCondition.Builder hasCorn = LootItemBlockStatePropertyCondition.hasBlockStateProperties(corn)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LOTRCornBlock.HAS_CORN, true));
        return createSingleItemTable(corn)
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).when(hasCorn)
                        .add(applyExplosionCondition(corn, LootItem.lootTableItem(cob))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).when(hasCorn)
                        .when(LootItemRandomChanceCondition.randomChance(0.25f))
                        .add(applyExplosionCondition(corn, LootItem.lootTableItem(cob))));
    }

    /**
     * LOTRBlockRemains.getDrops: one to three picks from
     * LOTRChestContents.MARSH_REMAINS, one more half the time, and
     * nextInt(1 + 2 * fortune) more on top -- each pick a weighted entry with
     * its own stack size. Silk Touch keeps the block (Block.canSilkHarvest).
     */
    private LootTable.Builder remainsDrops(Block remains) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootTable.Builder table = LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).when(hasSilkTouch())
                        .add(LootItem.lootTableItem(remains)))
                .withPool(remainsPool(UniformGenerator.between(1.0f, 3.0f)))
                .withPool(remainsPool(ConstantValue.exactly(1.0f))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f)));
        for (int extra = 1; extra <= 6; extra++) {
            float[] chances = new float[4];
            for (int fortune = 0; fortune < chances.length; fortune++) {
                int bound = 1 + 2 * fortune;
                chances[fortune] = Math.max(0, bound - extra) / (float) bound;
            }
            table.withPool(remainsPool(ConstantValue.exactly(1.0f)).when(BonusLevelTableCondition
                    .bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), chances)));
        }
        return table;
    }

    private LootPool.Builder remainsPool(NumberProvider rolls) {
        return LootPool.lootPool().setRolls(rolls).when(doesNotHaveSilkTouch())
                .add(LootItem.lootTableItem(Items.BONE).setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(LOTRItems.ELF_BONE).setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(LOTRItems.ORC_BONE).setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(LOTRItems.DWARF_BONE).setWeight(20)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(Items.SKELETON_SKULL).setWeight(25))
                .add(LootItem.lootTableItem(LOTRItems.SILVER_COIN).setWeight(25)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 16.0f))))
                .add(LootItem.lootTableItem(LOTRItems.SILVER_NUGGET).setWeight(25)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f))))
                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(25)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f))))
                .add(LootItem.lootTableItem(LOTRItems.MITHRIL_NUGGET).setWeight(2))
                .add(LootItem.lootTableItem(LOTRItems.ANCIENT_SWORD_TIP).setWeight(50))
                .add(LootItem.lootTableItem(LOTRItems.ANCIENT_SWORD_BLADE).setWeight(50))
                .add(LootItem.lootTableItem(LOTRItems.ANCIENT_SWORD_HILT).setWeight(50))
                .add(LootItem.lootTableItem(LOTRItems.ANCIENT_ARMOR_PLATE).setWeight(120));
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
        // LOTRBlockMechanisedRail.getDrops: the plain rail back, and the
        // mechanism that was put into it.
        LOTRBlocks.ALL_RAILS.forEach(r -> add(r, LootTable.lootTable()
                .withPool(applyExplosionCondition(r, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.RAIL))))
                .withPool(applyExplosionCondition(r, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(LOTRItems.MECHANISM))))));

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

        // Leaves keep vanilla's table -- saplings at the vanilla chance, and the
        // vanilla stick drop -- with the mod's own fruit added on top below.
        LOTRBlocks.LEAVES_SAPLING.forEach((leaves, sapling) ->
                add(leaves, createLeavesDrops(leaves, sapling, NORMAL_LEAVES_SAPLING_CHANCES)));
        // addSpecialLeafDrops: LOTRBlockFruitLeaves' apples (red or green, one in
        // 16), pears (16), cherries (8) and mangoes (16), and LOTRBlockLeaves'
        // mallorn nut (100).
        add(LOTRBlocks.APPLE_LEAVES, withLeafFruit(LOTRBlocks.APPLE_LEAVES,
                createLeavesDrops(LOTRBlocks.APPLE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.APPLE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, Items.APPLE, LOTRItems.GREEN_APPLE));
        add(LOTRBlocks.PEAR_LEAVES, withLeafFruit(LOTRBlocks.PEAR_LEAVES,
                createLeavesDrops(LOTRBlocks.PEAR_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.PEAR_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.PEAR));
        add(LOTRBlocks.CHERRY_LEAVES, withLeafFruit(LOTRBlocks.CHERRY_LEAVES,
                createLeavesDrops(LOTRBlocks.CHERRY_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.CHERRY_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                8, LOTRItems.CHERRIES));
        add(LOTRBlocks.MANGO_LEAVES, withLeafFruit(LOTRBlocks.MANGO_LEAVES,
                createLeavesDrops(LOTRBlocks.MANGO_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.MANGO_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.MANGO));
        add(LOTRBlocks.MALLORN_LEAVES, withLeafFruit(LOTRBlocks.MALLORN_LEAVES,
                createLeavesDrops(LOTRBlocks.MALLORN_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.MALLORN_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                100, LOTRItems.MALLORN_NUT));
        // The rest of LOTRBlockLeaves4 through 8: a conker one time in twenty,
        // citrus and plum and pomegranate one in sixteen, almond one in twelve
        // and an olive one in ten.
        add(LOTRBlocks.CHESTNUT_LEAVES, withLeafFruit(LOTRBlocks.CHESTNUT_LEAVES,
                createLeavesDrops(LOTRBlocks.CHESTNUT_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.CHESTNUT_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                20, LOTRItems.CONKER));
        add(LOTRBlocks.LEMON_LEAVES, withLeafFruit(LOTRBlocks.LEMON_LEAVES,
                createLeavesDrops(LOTRBlocks.LEMON_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.LEMON_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.LEMON));
        add(LOTRBlocks.ORANGE_LEAVES, withLeafFruit(LOTRBlocks.ORANGE_LEAVES,
                createLeavesDrops(LOTRBlocks.ORANGE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.ORANGE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.ORANGE));
        add(LOTRBlocks.LIME_LEAVES, withLeafFruit(LOTRBlocks.LIME_LEAVES,
                createLeavesDrops(LOTRBlocks.LIME_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.LIME_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.LIME));
        add(LOTRBlocks.OLIVE_LEAVES, withLeafFruit(LOTRBlocks.OLIVE_LEAVES,
                createLeavesDrops(LOTRBlocks.OLIVE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.OLIVE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                10, LOTRItems.OLIVES));
        add(LOTRBlocks.ALMOND_LEAVES, withLeafFruit(LOTRBlocks.ALMOND_LEAVES,
                createLeavesDrops(LOTRBlocks.ALMOND_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.ALMOND_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                12, LOTRItems.ALMOND));
        add(LOTRBlocks.PLUM_LEAVES, withLeafFruit(LOTRBlocks.PLUM_LEAVES,
                createLeavesDrops(LOTRBlocks.PLUM_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.PLUM_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.PLUM));
        add(LOTRBlocks.POMEGRANATE_LEAVES, withLeafFruit(LOTRBlocks.POMEGRANATE_LEAVES,
                createLeavesDrops(LOTRBlocks.POMEGRANATE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRBlocks.POMEGRANATE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRItems.POMEGRANATE));

        dropSelf(LOTRBlocks.WEB_UNGOLIANT);

        // dropSelf is right: LOTRAnimalJarBlock.getDrops stamps the caged bird
        // onto whatever the table produces.
        LOTRBlocks.ALL_ANIMAL_JARS.forEach(this::dropSelf);
        dropSelf(LOTRBlocks.WEAPON_RACK);

        // Mined by hand an orc bomb comes back whole; caught in a blast it does
        // not drop at all, which is LOTROrcBombBlock.dropFromExplosion's job.
        LOTRBlocks.ALL_ORC_BOMBS.forEach(this::dropSelf);
        dropSelf(LOTRBlocks.KHAMULS_FIRE_JAR);

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

        LOTRBlocks.ALL_DOUBLE_FLOWERS.forEach(b -> add(b,
                createSinglePropConditionTable(b, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)));

        // In no family list, because its models are hand-written rather than
        // generated -- so it needs its loot table naming explicitly.
        dropSelf(LOTRBlocks.BEACON_OF_GONDOR);
        dropSelf(LOTRBlocks.HOBBIT_OVEN);
        dropSelf(LOTRBlocks.ENT_JAR);
        dropSelf(LOTRBlocks.TABLE_OF_COMMAND);
        dropSelf(LOTRBlocks.UNSMELTERY);
        dropSelf(LOTRBlocks.MILLSTONE);
        dropSelf(LOTRBlocks.ANVIL);
        dropSelf(LOTRBlocks.ORC_CHAIN);
        // getItemDropped returned null for the head half, so a bed drops one
        // item, not two -- vanilla's own beds use exactly this condition.
        LOTRBlocks.ALL_BEDS.forEach(bed -> add(bed,
                createSinglePropConditionTable(bed, BedBlock.PART, BedPart.FOOT)));
        // Kebab stands have none: LOTRKebabStandBlockEntity drops them with their meat.
        LOTRBlocks.ALL_CHESTS.forEach(this::dropSelf);
        // damageDropped(i) = i & 3: each part dropped its own kind.
        LOTRBlocks.ALL_TROLL_TOTEMS.forEach(this::dropSelf);

        LOTRBlocks.TORCH_WALL.forEach((torch, wall) -> dropOther(wall, torch));

        // A double slab must drop two items, so slabs get their own table

        LOTRBlocks.ALL_SLABS.forEach(b -> add(b, this::createSlabItemTable));

        dropsSelf.forEach(family -> family.forEach(this::dropSelf));

        // Ores that give up what they hold. These come AFTER the family pass on
        // purpose: add() replaces, and ALL_CUBES would otherwise have every one
        // drop itself. LOTRBlockOre.getItemDropped/quantityDropped: durnor, niter
        // and sulfur one or two, edhelvir and gulduril one, glowstone dust two to
        // five, all multiplied by the ore Fortune factor and glowstone capped at
        // eight. LOTRBlockOreGem: one or two gems plus rand(fortune + 1).
        add(LOTRBlocks.NAURITE_ORE, oreDrops(LOTRBlocks.NAURITE_ORE, LOTRItems.DURNOR, 1, 2, 0));
        add(LOTRBlocks.QUENDITE_ORE, createOreDrop(LOTRBlocks.QUENDITE_ORE, LOTRItems.EDHELVIR));
        add(LOTRBlocks.GULDURIL_ORE, createOreDrop(LOTRBlocks.GULDURIL_ORE, LOTRItems.GULDURIL));
        add(LOTRBlocks.GULDURIL_MORDOR_ORE, createOreDrop(LOTRBlocks.GULDURIL_MORDOR_ORE, LOTRItems.GULDURIL));
        add(LOTRBlocks.SULFUR_ORE, oreDrops(LOTRBlocks.SULFUR_ORE, LOTRItems.SULFUR, 1, 2, 0));
        add(LOTRBlocks.SALTPETER_ORE, oreDrops(LOTRBlocks.SALTPETER_ORE, LOTRItems.NITER, 1, 2, 0));
        add(LOTRBlocks.GLOWSTONE_ORE, oreDrops(LOTRBlocks.GLOWSTONE_ORE, Items.GLOWSTONE_DUST, 2, 5, 8));
        add(LOTRBlocks.TOPAZ_ORE, gemDrops(LOTRBlocks.TOPAZ_ORE, LOTRItems.TOPAZ));
        add(LOTRBlocks.AMETHYST_ORE, gemDrops(LOTRBlocks.AMETHYST_ORE, LOTRItems.AMETHYST));
        add(LOTRBlocks.SAPPHIRE_ORE, gemDrops(LOTRBlocks.SAPPHIRE_ORE, LOTRItems.SAPPHIRE));
        add(LOTRBlocks.RUBY_ORE, gemDrops(LOTRBlocks.RUBY_ORE, LOTRItems.RUBY));
        add(LOTRBlocks.AMBER_ORE, gemDrops(LOTRBlocks.AMBER_ORE, LOTRItems.AMBER));
        add(LOTRBlocks.DIAMOND_ORE, gemDrops(LOTRBlocks.DIAMOND_ORE, LOTRItems.DIAMOND));
        add(LOTRBlocks.OPAL_ORE, gemDrops(LOTRBlocks.OPAL_ORE, LOTRItems.OPAL));
        add(LOTRBlocks.EMERALD_ORE, gemDrops(LOTRBlocks.EMERALD_ORE, LOTRItems.EMERALD));
        // LOTRBlockRedClay: four balls, as vanilla clay gives.
        add(LOTRBlocks.RED_CLAY, createSingleItemTableWithSilkTouch(LOTRBlocks.RED_CLAY,
                LOTRItems.RED_CLAY_BALL, ConstantValue.exactly(4)));

        LOTRBlocks.ALL_DOORS.forEach(door -> add(door, createDoorTable(door)));

        // LOTRBlockGrapevine.getVineDrops: seeds all the way up, and the bunch
        // once it is ripe. Vanilla's crop table is the same shape.
        add(LOTRBlocks.RED_GRAPEVINE, createCropDrops(LOTRBlocks.RED_GRAPEVINE, LOTRItems.RED_GRAPES,
                LOTRItems.RED_GRAPE_SEEDS, LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(LOTRBlocks.RED_GRAPEVINE)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LOTRGrapevineBlock.AGE, LOTRGrapevineBlock.MAX_AGE))));
        add(LOTRBlocks.GREEN_GRAPEVINE, createCropDrops(LOTRBlocks.GREEN_GRAPEVINE, LOTRItems.GREEN_GRAPES,
                LOTRItems.GREEN_GRAPE_SEEDS, LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(LOTRBlocks.GREEN_GRAPEVINE)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LOTRGrapevineBlock.AGE, LOTRGrapevineBlock.MAX_AGE))));

        // LOTRBlockMudGrass and LOTRBlockQuenditeGrass: mud and plain dirt, or the
        // grass itself under Silk Touch, the way vanilla's grass block works.
        add(LOTRBlocks.MUD_GRASS, createSingleItemTableWithSilkTouch(LOTRBlocks.MUD_GRASS, LOTRBlocks.MUD));
        add(LOTRBlocks.QUENDITE_GRASS, createSingleItemTableWithSilkTouch(LOTRBlocks.QUENDITE_GRASS, Blocks.DIRT));
        // LOTRBlockTermite: the infested mound dropped nothing -- it lets the
        // termites out instead -- and Silk Touch gave the plain mound.
        otherWhenSilkTouch(LOTRBlocks.INFESTED_TERMITE_MOUND, LOTRBlocks.TERMITE_MOUND);
        // LOTRBlockMordorMoss: getItemDropped null, and IShearable.
        add(LOTRBlocks.MORDOR_MOSS, createShearsOnlyDrop(LOTRBlocks.MORDOR_MOSS));
        // LOTRBlockDirtPath.damageDropped(i) = i: the mud path drops itself.
        dropSelf(LOTRBlocks.DIRT_PATH_MUD);
        // BlockContainer's default: a forge or a dart trap drops itself, its
        // contents being dropped by the block entity.
        LOTRBlocks.ALL_FORGES.forEach(this::dropSelf);
        LOTRBlocks.ALL_DART_TRAPS.forEach(this::dropSelf);

        // LOTRBlockGuldurilBrick.getDrops: the brick the crystal was set into,
        // and canSilkHarvest for the glowing brick itself.
        add(LOTRBlocks.GULDURIL_MORDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_MORDOR_BRICK, LOTRBlocks.MORDOR_BRICK));
        add(LOTRBlocks.GULDURIL_CRACKED_MORDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_CRACKED_MORDOR_BRICK, LOTRBlocks.CRACKED_MORDOR_BRICK));
        add(LOTRBlocks.GULDURIL_DOL_GULDUR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_DOL_GULDUR_BRICK, LOTRBlocks.DOL_GULDUR_BRICK));
        add(LOTRBlocks.GULDURIL_CRACKED_DOL_GULDUR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_CRACKED_DOL_GULDUR_BRICK, LOTRBlocks.CRACKED_DOL_GULDUR_BRICK));
        add(LOTRBlocks.GULDURIL_ANGMAR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_ANGMAR_BRICK, LOTRBlocks.ANGMAR_BRICK));
        add(LOTRBlocks.GULDURIL_CRACKED_ANGMAR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_CRACKED_ANGMAR_BRICK, LOTRBlocks.CRACKED_ANGMAR_BRICK));
        add(LOTRBlocks.GULDURIL_GONDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_GONDOR_BRICK, LOTRBlocks.GONDOR_BRICK));
        add(LOTRBlocks.GULDURIL_MOSSY_GONDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_MOSSY_GONDOR_BRICK, LOTRBlocks.MOSSY_GONDOR_BRICK));
        add(LOTRBlocks.GULDURIL_CRACKED_GONDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_CRACKED_GONDOR_BRICK, LOTRBlocks.CRACKED_GONDOR_BRICK));
        add(LOTRBlocks.GULDURIL_NUMENOREAN_BRICK, createSingleItemTableWithSilkTouch(LOTRBlocks.GULDURIL_NUMENOREAN_BRICK, LOTRBlocks.NUMENOREAN_BRICK));

        // Berries and corn come away with the plant, as getDrops added them.
        add(LOTRBlocks.BERRY_BUSH_BLUEBERRY, berryBushDrops(LOTRBlocks.BERRY_BUSH_BLUEBERRY, LOTRItems.BLUEBERRIES));
        add(LOTRBlocks.BERRY_BUSH_BLACKBERRY, berryBushDrops(LOTRBlocks.BERRY_BUSH_BLACKBERRY, LOTRItems.BLACKBERRIES));
        add(LOTRBlocks.BERRY_BUSH_RASPBERRY, berryBushDrops(LOTRBlocks.BERRY_BUSH_RASPBERRY, LOTRItems.RASPBERRIES));
        add(LOTRBlocks.BERRY_BUSH_CRANBERRY, berryBushDrops(LOTRBlocks.BERRY_BUSH_CRANBERRY, LOTRItems.CRANBERRIES));
        add(LOTRBlocks.BERRY_BUSH_ELDERBERRY, berryBushDrops(LOTRBlocks.BERRY_BUSH_ELDERBERRY, LOTRItems.ELDERBERRIES));
        add(LOTRBlocks.BERRY_BUSH_WILDBERRY, berryBushDrops(LOTRBlocks.BERRY_BUSH_WILDBERRY, LOTRItems.WILDBERRIES));
        add(LOTRBlocks.CORN_STALK, cornDrops(LOTRBlocks.CORN_STALK, LOTRItems.CORN));

        add(LOTRBlocks.REMAINS, remainsDrops(LOTRBlocks.REMAINS));
    }
}