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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCornBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGrapevineBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTreasurePileBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPlaceableFoodBlock;
import net.minecraft.world.level.block.CropBlock;

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
                .add(LootItem.lootTableItem(LOTRMaterialItems.ELF_BONE).setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(LOTRMaterialItems.ORC_BONE).setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(LOTRMaterialItems.DWARF_BONE).setWeight(20)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(Items.SKELETON_SKULL).setWeight(25))
                .add(LootItem.lootTableItem(LOTRMiscItems.SILVER_COIN).setWeight(25)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 16.0f))))
                .add(LootItem.lootTableItem(LOTRMaterialItems.SILVER_NUGGET).setWeight(25)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f))))
                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(25)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f))))
                .add(LootItem.lootTableItem(LOTRMaterialItems.MITHRIL_NUGGET).setWeight(2))
                .add(LootItem.lootTableItem(LOTRMiscItems.ANCIENT_SWORD_TIP).setWeight(50))
                .add(LootItem.lootTableItem(LOTRMiscItems.ANCIENT_SWORD_BLADE).setWeight(50))
                .add(LootItem.lootTableItem(LOTRMiscItems.ANCIENT_SWORD_HILT).setWeight(50))
                .add(LootItem.lootTableItem(LOTRMiscItems.ANCIENT_ARMOR_PLATE).setWeight(120));
    }

    @Override
    public void generate() {
        // LOTRBlockStalactite.quantityDropped was the model block's: stone and
        // obsidian give themselves back, packed ice nothing; canSilkHarvest
        // was true, so silk touch always does.
        dropSelf(LOTRDecorationBlocks.GORAN);
        dropSelf(LOTRDecorationBlocks.GORAN_ROCK);
        dropSelf(LOTRDecorationBlocks.STALACTITE);
        dropSelf(LOTRDecorationBlocks.STALAGMITE);
        dropWhenSilkTouch(LOTRDecorationBlocks.ICE_STALACTITE);
        dropWhenSilkTouch(LOTRDecorationBlocks.ICE_STALAGMITE);
        dropSelf(LOTRDecorationBlocks.OBSIDIAN_STALACTITE);
        dropSelf(LOTRDecorationBlocks.OBSIDIAN_STALAGMITE);

        // LOTRBlockFallenLeaves: getItemDropped null, IShearable -- shears only.
        LOTRBlocks.ALL_FALLEN_LEAVES.forEach(b -> add(b, createShearsOnlyDrop(b)));

        // Placed food and tableware. A plate or fruit block drops its item; a
        // cake or pie comes back whole only while nobody has eaten from it.
        dropSelf(LOTRFoodBlocks.FINE_PLATE);
        dropSelf(LOTRFoodBlocks.STONEWARE_PLATE);
        dropSelf(LOTRFoodBlocks.WOODEN_PLATE);
        dropOther(LOTRFoodBlocks.BANANA_BLOCK, LOTRFoodItems.BANANA);
        dropOther(LOTRFoodBlocks.DATE_BLOCK, LOTRFoodItems.DATE);
        List.of(LOTRFoodBlocks.APPLE_CRUMBLE, LOTRFoodBlocks.BANANA_CAKE, LOTRFoodBlocks.BERRY_PIE,
                LOTRFoodBlocks.CHERRY_PIE, LOTRFoodBlocks.DALISH_PASTRY, LOTRFoodBlocks.LEMON_CAKE,
                LOTRFoodBlocks.MARCHPANE_BLOCK).forEach(this::dropWhenUneaten);

        // Crops. Flax and pipe-weed have a seed of their own (vanilla's wheat
        // shape); the four vegetables are planted from themselves (the potato
        // shape, without the poisonous potato).
        cropDrops(LOTRUtilityBlocks.FLAX_CROP, LOTRMaterialItems.FLAX, LOTRMaterialItems.FLAX_SEEDS);
        cropDrops(LOTRUtilityBlocks.PIPEWEED_CROP, LOTRMaterialItems.PIPEWEED_LEAF, LOTRMaterialItems.PIPEWEED_SEEDS);
        rootCropDrops(LOTRUtilityBlocks.LETTUCE_CROP, LOTRFoodItems.LETTUCE);
        rootCropDrops(LOTRUtilityBlocks.LEEK_CROP, LOTRFoodItems.LEEK);
        rootCropDrops(LOTRUtilityBlocks.TURNIP_CROP, LOTRFoodItems.TURNIP);
        rootCropDrops(LOTRUtilityBlocks.YAM_CROP, LOTRFoodItems.YAM);

        LOTRBlocks.ALL_GLASS.forEach(this::dropWhenSilkTouch);
        LOTRBlocks.ALL_GLASS_PANES.forEach(this::dropWhenSilkTouch);

        // Farmland has no item of its own. Vanilla farmland drops dirt; the LOTR
        // one is tilled mud, so it drops mud.
        // Vanilla dirt path and farmland both drop dirt, with no silk-touch
        // special case. The LOTR pair are worked mud, so they drop mud.
        LOTRBlocks.ALL_FARMLAND.forEach(b -> dropOther(b, LOTRBuildingBlocks.MUD));
        // LOTRBlockMechanisedRail.getDrops: the plain rail back, and the
        // mechanism that was put into it.
        LOTRBlocks.ALL_RAILS.forEach(r -> add(r, LootTable.lootTable()
                .withPool(applyExplosionCondition(r, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.RAIL))))
                .withPool(applyExplosionCondition(r, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(LOTRMiscItems.MECHANISM))))));

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
        List.of(LOTRDecorationBlocks.TALL_GRASS_SHORT, LOTRDecorationBlocks.TALL_GRASS_FLOWER,
                        LOTRDecorationBlocks.TALL_GRASS_WHEAT, LOTRDecorationBlocks.TALL_GRASS_NETTLE,
                        LOTRDecorationBlocks.TALL_GRASS_FERNSPROUT, LOTRDecorationBlocks.ARID_GRASS)
                .forEach(b -> add(b, createGrassDrops(b)));

        // getItemDropped returned null and onSheared returned the block, so
        // these three give nothing at all unless you use shears.
        List.of(LOTRDecorationBlocks.MORDOR_GRASS, LOTRDecorationBlocks.MORDOR_THORN, LOTRDecorationBlocks.DEAD_MARSH_PLANT)
                .forEach(b -> add(b, createShearsOnlyDrop(b)));

        // LOTRBlockCorruptMallorn.getDrops returned new ItemStack(LOTRMod.sapling,
        // 1, 1) -- sapling meta 1 is the mallorn -- never the corrupt block.
        dropOther(LOTRDecorationBlocks.CORRUPT_MALLORN, LOTRDecorationBlocks.MALLORN_SAPLING);

        // Everything else in the family drops itself.
        List<Block> plantsWithOwnTable = List.of(LOTRDecorationBlocks.TALL_GRASS_SHORT,
                LOTRDecorationBlocks.TALL_GRASS_FLOWER, LOTRDecorationBlocks.TALL_GRASS_WHEAT,
                LOTRDecorationBlocks.TALL_GRASS_NETTLE, LOTRDecorationBlocks.TALL_GRASS_FERNSPROUT,
                LOTRDecorationBlocks.ARID_GRASS, LOTRDecorationBlocks.MORDOR_GRASS, LOTRDecorationBlocks.MORDOR_THORN,
                LOTRDecorationBlocks.DEAD_MARSH_PLANT, LOTRDecorationBlocks.CORRUPT_MALLORN);
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
        add(LOTRDecorationBlocks.APPLE_LEAVES, withLeafFruit(LOTRDecorationBlocks.APPLE_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.APPLE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.APPLE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, Items.APPLE, LOTRFoodItems.GREEN_APPLE));
        add(LOTRDecorationBlocks.PEAR_LEAVES, withLeafFruit(LOTRDecorationBlocks.PEAR_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.PEAR_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.PEAR_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.PEAR));
        add(LOTRDecorationBlocks.CHERRY_LEAVES, withLeafFruit(LOTRDecorationBlocks.CHERRY_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.CHERRY_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.CHERRY_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                8, LOTRFoodItems.CHERRIES));
        add(LOTRDecorationBlocks.MANGO_LEAVES, withLeafFruit(LOTRDecorationBlocks.MANGO_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.MANGO_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.MANGO_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.MANGO));
        add(LOTRDecorationBlocks.MALLORN_LEAVES, withLeafFruit(LOTRDecorationBlocks.MALLORN_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.MALLORN_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.MALLORN_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                100, LOTRFoodItems.MALLORN_NUT));
        // The rest of LOTRBlockLeaves4 through 8: a conker one time in twenty,
        // citrus and plum and pomegranate one in sixteen, almond one in twelve
        // and an olive one in ten.
        add(LOTRDecorationBlocks.CHESTNUT_LEAVES, withLeafFruit(LOTRDecorationBlocks.CHESTNUT_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.CHESTNUT_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.CHESTNUT_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                20, LOTRMiscItems.CONKER));
        add(LOTRDecorationBlocks.LEMON_LEAVES, withLeafFruit(LOTRDecorationBlocks.LEMON_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.LEMON_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.LEMON_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.LEMON));
        add(LOTRDecorationBlocks.ORANGE_LEAVES, withLeafFruit(LOTRDecorationBlocks.ORANGE_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.ORANGE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.ORANGE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.ORANGE));
        add(LOTRDecorationBlocks.LIME_LEAVES, withLeafFruit(LOTRDecorationBlocks.LIME_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.LIME_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.LIME_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.LIME));
        add(LOTRDecorationBlocks.OLIVE_LEAVES, withLeafFruit(LOTRDecorationBlocks.OLIVE_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.OLIVE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.OLIVE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                10, LOTRFoodItems.OLIVES));
        add(LOTRDecorationBlocks.ALMOND_LEAVES, withLeafFruit(LOTRDecorationBlocks.ALMOND_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.ALMOND_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.ALMOND_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                12, LOTRFoodItems.ALMOND));
        add(LOTRDecorationBlocks.PLUM_LEAVES, withLeafFruit(LOTRDecorationBlocks.PLUM_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.PLUM_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.PLUM_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.PLUM));
        add(LOTRDecorationBlocks.POMEGRANATE_LEAVES, withLeafFruit(LOTRDecorationBlocks.POMEGRANATE_LEAVES,
                createLeavesDrops(LOTRDecorationBlocks.POMEGRANATE_LEAVES, LOTRBlocks.LEAVES_SAPLING.get(LOTRDecorationBlocks.POMEGRANATE_LEAVES),
                        NORMAL_LEAVES_SAPLING_CHANCES),
                16, LOTRFoodItems.POMEGRANATE));

        dropSelf(LOTRDecorationBlocks.WEB_UNGOLIANT);

        // dropSelf is right: LOTRAnimalJarBlock.getDrops stamps the caged bird
        // onto whatever the table produces.
        LOTRBlocks.ALL_ANIMAL_JARS.forEach(this::dropSelf);
        dropSelf(LOTRDecorationBlocks.WEAPON_RACK);

        // Mined by hand an orc bomb comes back whole; caught in a blast it does
        // not drop at all, which is LOTROrcBombBlock.dropFromExplosion's job.
        LOTRBlocks.ALL_ORC_BOMBS.forEach(this::dropSelf);
        dropSelf(LOTRCombatBlocks.KHAMULS_FIRE_JAR);

        // Treasure piles drop one item per layer, the way vanilla's snow layers
        // do -- see the divergence note on LOTRTreasurePileBlock.
        LOTRBlocks.ALL_TREASURE_PILES.forEach(pile -> add(pile, this::treasurePileDrops));

        // Banners: the standing form drops itself, and the wall form drops the
        // standing one, since the two share a single item.
        LOTRBlocks.ALL_BANNERS.forEach(this::dropSelf);
        LOTRBlocks.BANNER_WALL_FORM.forEach((standing, wall) -> dropOther(wall, standing));
        // Two blocks, one torch: only the lower half drops, the way vanilla's
        // double plants do.
        add(LOTRDecorationBlocks.ORC_TORCH, createSinglePropConditionTable(LOTRDecorationBlocks.ORC_TORCH,
                DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        add(LOTRDecorationBlocks.TAUREDAIN_DOUBLE_TORCH,
                createSinglePropConditionTable(LOTRDecorationBlocks.TAUREDAIN_DOUBLE_TORCH,
                        DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));

        LOTRBlocks.ALL_DOUBLE_FLOWERS.forEach(b -> add(b,
                createSinglePropConditionTable(b, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)));

        // In no family list, because its models are hand-written rather than
        // generated -- so it needs its loot table naming explicitly.
        dropSelf(LOTRUtilityBlocks.BEACON_OF_GONDOR);
        dropSelf(LOTRUtilityBlocks.HOBBIT_OVEN);
        dropSelf(LOTRUtilityBlocks.ENT_JAR);
        dropSelf(LOTRUtilityBlocks.TABLE_OF_COMMAND);
        dropSelf(LOTRUtilityBlocks.UNSMELTERY);
        dropSelf(LOTRUtilityBlocks.MILLSTONE);
        dropSelf(LOTRUtilityBlocks.ANVIL);
        dropSelf(LOTRUtilityBlocks.ORC_CHAIN);
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
        add(LOTRBuildingBlocks.NAURITE_ORE, oreDrops(LOTRBuildingBlocks.NAURITE_ORE, LOTRMaterialItems.DURNOR, 1, 2, 0));
        add(LOTRBuildingBlocks.QUENDITE_ORE, createOreDrop(LOTRBuildingBlocks.QUENDITE_ORE, LOTRMaterialItems.EDHELVIR));
        add(LOTRBuildingBlocks.GULDURIL_ORE, createOreDrop(LOTRBuildingBlocks.GULDURIL_ORE, LOTRMaterialItems.GULDURIL));
        add(LOTRBuildingBlocks.GULDURIL_MORDOR_ORE, createOreDrop(LOTRBuildingBlocks.GULDURIL_MORDOR_ORE, LOTRMaterialItems.GULDURIL));
        add(LOTRBuildingBlocks.SULFUR_ORE, oreDrops(LOTRBuildingBlocks.SULFUR_ORE, LOTRMaterialItems.SULFUR, 1, 2, 0));
        add(LOTRBuildingBlocks.SALTPETER_ORE, oreDrops(LOTRBuildingBlocks.SALTPETER_ORE, LOTRMaterialItems.NITER, 1, 2, 0));
        add(LOTRBuildingBlocks.GLOWSTONE_ORE, oreDrops(LOTRBuildingBlocks.GLOWSTONE_ORE, Items.GLOWSTONE_DUST, 2, 5, 8));
        add(LOTRBuildingBlocks.TOPAZ_ORE, gemDrops(LOTRBuildingBlocks.TOPAZ_ORE, LOTRMaterialItems.TOPAZ));
        add(LOTRBuildingBlocks.AMETHYST_ORE, gemDrops(LOTRBuildingBlocks.AMETHYST_ORE, LOTRMaterialItems.AMETHYST));
        add(LOTRBuildingBlocks.SAPPHIRE_ORE, gemDrops(LOTRBuildingBlocks.SAPPHIRE_ORE, LOTRMaterialItems.SAPPHIRE));
        add(LOTRBuildingBlocks.RUBY_ORE, gemDrops(LOTRBuildingBlocks.RUBY_ORE, LOTRMaterialItems.RUBY));
        add(LOTRBuildingBlocks.AMBER_ORE, gemDrops(LOTRBuildingBlocks.AMBER_ORE, LOTRMaterialItems.AMBER));
        add(LOTRBuildingBlocks.DIAMOND_ORE, gemDrops(LOTRBuildingBlocks.DIAMOND_ORE, LOTRMaterialItems.DIAMOND));
        add(LOTRBuildingBlocks.OPAL_ORE, gemDrops(LOTRBuildingBlocks.OPAL_ORE, LOTRMaterialItems.OPAL));
        add(LOTRBuildingBlocks.EMERALD_ORE, gemDrops(LOTRBuildingBlocks.EMERALD_ORE, LOTRMaterialItems.EMERALD));
        // LOTRBlockRedClay: four balls, as vanilla clay gives.
        add(LOTRBuildingBlocks.RED_CLAY, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.RED_CLAY,
                LOTRMaterialItems.RED_CLAY_BALL, ConstantValue.exactly(4)));

        LOTRBlocks.ALL_DOORS.forEach(door -> add(door, createDoorTable(door)));

        // LOTRBlockGrapevine.getVineDrops: seeds all the way up, and the bunch
        // once it is ripe. Vanilla's crop table is the same shape.
        add(LOTRDecorationBlocks.RED_GRAPEVINE, createCropDrops(LOTRDecorationBlocks.RED_GRAPEVINE, LOTRFoodItems.RED_GRAPES,
                LOTRMaterialItems.RED_GRAPE_SEEDS, LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(LOTRDecorationBlocks.RED_GRAPEVINE)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LOTRGrapevineBlock.AGE, LOTRGrapevineBlock.MAX_AGE))));
        add(LOTRDecorationBlocks.GREEN_GRAPEVINE, createCropDrops(LOTRDecorationBlocks.GREEN_GRAPEVINE, LOTRFoodItems.GREEN_GRAPES,
                LOTRMaterialItems.GREEN_GRAPE_SEEDS, LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(LOTRDecorationBlocks.GREEN_GRAPEVINE)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LOTRGrapevineBlock.AGE, LOTRGrapevineBlock.MAX_AGE))));

        // LOTRBlockMudGrass and LOTRBlockQuenditeGrass: mud and plain dirt, or the
        // grass itself under Silk Touch, the way vanilla's grass block works.
        add(LOTRBuildingBlocks.MUD_GRASS, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.MUD_GRASS, LOTRBuildingBlocks.MUD));
        add(LOTRBuildingBlocks.QUENDITE_GRASS, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.QUENDITE_GRASS, Blocks.DIRT));
        // LOTRBlockTermite: the infested mound dropped nothing -- it lets the
        // termites out instead -- and Silk Touch gave the plain mound.
        otherWhenSilkTouch(LOTRBuildingBlocks.INFESTED_TERMITE_MOUND, LOTRBuildingBlocks.TERMITE_MOUND);
        // LOTRBlockMordorMoss: getItemDropped null, and IShearable.
        add(LOTRDecorationBlocks.MORDOR_MOSS, createShearsOnlyDrop(LOTRDecorationBlocks.MORDOR_MOSS));
        // LOTRBlockDirtPath.damageDropped(i) = i: the mud path drops itself.
        dropSelf(LOTRBuildingBlocks.DIRT_PATH_MUD);
        // BlockContainer's default: a forge or a dart trap drops itself, its
        // contents being dropped by the block entity.
        LOTRBlocks.ALL_FORGES.forEach(this::dropSelf);
        LOTRBlocks.ALL_DART_TRAPS.forEach(this::dropSelf);

        // LOTRBlockGuldurilBrick.getDrops: the brick the crystal was set into,
        // and canSilkHarvest for the glowing brick itself.
        add(LOTRBuildingBlocks.GULDURIL_MORDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_MORDOR_BRICK, LOTRBuildingBlocks.MORDOR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_CRACKED_MORDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_CRACKED_MORDOR_BRICK, LOTRBuildingBlocks.CRACKED_MORDOR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_DOL_GULDUR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_DOL_GULDUR_BRICK, LOTRBuildingBlocks.DOL_GULDUR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_CRACKED_DOL_GULDUR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_CRACKED_DOL_GULDUR_BRICK, LOTRBuildingBlocks.CRACKED_DOL_GULDUR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_ANGMAR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_ANGMAR_BRICK, LOTRBuildingBlocks.ANGMAR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_CRACKED_ANGMAR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_CRACKED_ANGMAR_BRICK, LOTRBuildingBlocks.CRACKED_ANGMAR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_GONDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_GONDOR_BRICK, LOTRBuildingBlocks.GONDOR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_MOSSY_GONDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_MOSSY_GONDOR_BRICK, LOTRBuildingBlocks.MOSSY_GONDOR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_CRACKED_GONDOR_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_CRACKED_GONDOR_BRICK, LOTRBuildingBlocks.CRACKED_GONDOR_BRICK));
        add(LOTRBuildingBlocks.GULDURIL_NUMENOREAN_BRICK, createSingleItemTableWithSilkTouch(LOTRBuildingBlocks.GULDURIL_NUMENOREAN_BRICK, LOTRBuildingBlocks.NUMENOREAN_BRICK));

        // Berries and corn come away with the plant, as getDrops added them.
        add(LOTRDecorationBlocks.BERRY_BUSH_BLUEBERRY, berryBushDrops(LOTRDecorationBlocks.BERRY_BUSH_BLUEBERRY, LOTRFoodItems.BLUEBERRIES));
        add(LOTRDecorationBlocks.BERRY_BUSH_BLACKBERRY, berryBushDrops(LOTRDecorationBlocks.BERRY_BUSH_BLACKBERRY, LOTRFoodItems.BLACKBERRIES));
        add(LOTRDecorationBlocks.BERRY_BUSH_RASPBERRY, berryBushDrops(LOTRDecorationBlocks.BERRY_BUSH_RASPBERRY, LOTRFoodItems.RASPBERRIES));
        add(LOTRDecorationBlocks.BERRY_BUSH_CRANBERRY, berryBushDrops(LOTRDecorationBlocks.BERRY_BUSH_CRANBERRY, LOTRFoodItems.CRANBERRIES));
        add(LOTRDecorationBlocks.BERRY_BUSH_ELDERBERRY, berryBushDrops(LOTRDecorationBlocks.BERRY_BUSH_ELDERBERRY, LOTRFoodItems.ELDERBERRIES));
        add(LOTRDecorationBlocks.BERRY_BUSH_WILDBERRY, berryBushDrops(LOTRDecorationBlocks.BERRY_BUSH_WILDBERRY, LOTRFoodItems.WILDBERRIES));
        add(LOTRDecorationBlocks.CORN_STALK, cornDrops(LOTRDecorationBlocks.CORN_STALK, LOTRFoodItems.CORN));

        add(LOTRBuildingBlocks.REMAINS, remainsDrops(LOTRBuildingBlocks.REMAINS));
    }

    /** A cake-like block: itself, only at bites 0. */
    private void dropWhenUneaten(Block block) {
        add(block, LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(LootItem.lootTableItem(block))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LOTRPlaceableFoodBlock.BITES, 0))))));
    }

    private LootItemCondition.Builder mature(Block crop) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7));
    }

    private void cropDrops(Block crop, Item product, Item seeds) {
        add(crop, createCropDrops(crop, product, seeds, mature(crop)));
    }

    /** The potato table: one always, and up to three more with fortune when grown. */
    private void rootCropDrops(Block crop, Item product) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(crop, applyExplosionDecay(crop, LootTable.lootTable()
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(product)))
                .withPool(LootPool.lootPool().when(mature(crop)).add(LootItem.lootTableItem(product)
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(
                                enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286f, 3))))));
    }
}