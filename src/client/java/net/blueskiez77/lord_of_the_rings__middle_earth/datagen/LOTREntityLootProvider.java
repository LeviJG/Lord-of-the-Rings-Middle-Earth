package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * The animals' dropFewItems, as loot tables. The original's usual
 * {@code a + rand.nextInt(n) + rand.nextInt(1 + looting)} is a uniform count of
 * a..a+n-1 plus 0..1 per looting level; meat dropped cooked when the animal
 * died burning, which is the smelt-when-on-fire vanilla uses.
 */
public class LOTREntityLootProvider extends FabricEntityLootSubProvider {

    public LOTREntityLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generate() {
        // LOTREntityDeer: 0-2 leather, 0-2 venison.
        add(LOTREntities.DEER, LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 2, true, false))
                .withPool(drop(LOTRFoodItems.RAW_VENISON, 0, 2, true, true)));
        // LOTREntityWhiteOryx: the deer's drops.
        add(LOTREntities.WHITE_ORYX, LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 2, true, false))
                .withPool(drop(LOTRFoodItems.RAW_VENISON, 0, 2, true, true)));
        // LOTREntityGemsbok: 1-4 hides plus looting; a horn half the time.
        add(LOTREntities.GEMSBOK, LootTable.lootTable()
                .withPool(drop(LOTRMaterialItems.GEMSBOK_HIDE, 1, 4, true, false))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(LOTRMaterialItems.GEMSBOK_HORN))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f))));
        // LOTREntityAurochs: 2-4 leather and beef, plus looting, and a horn.
        add(LOTREntities.AUROCHS, aurochs(LOTRMaterialItems.HORN));
        // LOTREntityKineAraw: the aurochs', with the kine of Araw horn.
        add(LOTREntities.KINE_OF_ARAW, aurochs(LOTRMaterialItems.KINE_OF_ARAW_HORN));
        // LOTREntityFlamingo: getDropItem feathers, EntityLiving's 0-2 plus looting.
        add(LOTREntities.FLAMINGO, LootTable.lootTable()
                .withPool(drop(Items.FEATHER, 0, 2, true, false)));
        // LOTREntityLionBase: 2-4 fur (no looting), 1-2 lion meat plus looting.
        // The rug is LOTRRugDrops.
        add(LOTREntities.LION, lion());
        add(LOTREntities.LIONESS, lion());
        // LOTREntityBear: 1-3 fur plus looting. The rug is LOTRRugDrops.
        add(LOTREntities.BEAR, LootTable.lootTable()
                .withPool(drop(LOTRMaterialItems.FUR, 1, 3, true, false)));
        // LOTREntityCrocodile: getDropItem's rotten flesh, EntityLiving's 0-2 plus
        // looting. Its extra drops are in LOTRCrocodileEntity.dropCustomDeathLoot.
        add(LOTREntities.CROCODILE, LootTable.lootTable()
                .withPool(drop(Items.ROTTEN_FLESH, 0, 2, true, false)));
        // LOTREntityScorpion: 1-3 rotten flesh plus looting.
        add(LOTREntities.JUNGLE_SCORPION, LootTable.lootTable()
                .withPool(drop(Items.ROTTEN_FLESH, 1, 3, true, false)));
        add(LOTREntities.DESERT_SCORPION, LootTable.lootTable()
                .withPool(drop(Items.ROTTEN_FLESH, 1, 3, true, false)));
        // LOTREntityTermite dropped nothing but, to a player, its throwable self
        // (LOTRTermiteEntity.die).
        add(LOTREntities.TERMITE, LootTable.lootTable());
        // EntityHorse's leather, 0-2 plus looting: the horse, the pony and the
        // giraffe (whose rug is LOTRRugDrops).
        add(LOTREntities.HORSE, LootTable.lootTable().withPool(drop(Items.LEATHER, 0, 2, true, false)));
        add(LOTREntities.SHIRE_PONY, LootTable.lootTable().withPool(drop(Items.LEATHER, 0, 2, true, false)));
        add(LOTREntities.GIRAFFE, LootTable.lootTable().withPool(drop(Items.LEATHER, 0, 2, true, false)));
        // LOTREntityElk: the deer's drops.
        add(LOTREntities.ELK, LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 2, true, false))
                .withPool(drop(LOTRFoodItems.RAW_VENISON, 0, 2, true, true)));
        // LOTREntityCamel: 0-2 leather, 0-2 camel meat.
        add(LOTREntities.CAMEL, LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 2, true, false))
                .withPool(drop(LOTRFoodItems.RAW_CAMEL, 0, 2, true, true)));
        // LOTREntityZebra: 0-1 leather, 1-2 zebra meat.
        add(LOTREntities.ZEBRA, LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 1, true, false))
                .withPool(drop(LOTRFoodItems.RAW_ZEBRA, 1, 2, true, true)));
        // LOTREntityWildBoar: 1-3 porkchops.
        add(LOTREntities.WILD_BOAR, LootTable.lootTable()
                .withPool(drop(Items.PORKCHOP, 1, 3, true, true)));
        // LOTREntityRhino: 0-1 horns, 0-2 rhino meat.
        add(LOTREntities.RHINO, LootTable.lootTable()
                .withPool(drop(LOTRMaterialItems.RHINO_HORN, 0, 1, true, false))
                .withPool(drop(LOTRFoodItems.RAW_RHINO, 0, 2, true, true)));
        // LOTREntitySwan: 0-2 swan feathers plus looting.
        add(LOTREntities.SWAN, LootTable.lootTable()
                .withPool(drop(LOTRMaterialItems.SWAN_FEATHER, 0, 2, true, false)));
        // LOTREntityBird and its kinds: 0-2 feathers plus looting. What it stole
        // drops in LOTRBirdEntity.die.
        for (var bird : java.util.List.of(LOTREntities.BIRD, LOTREntities.CREBAIN, LOTREntities.GORCROW, LOTREntities.SEAGULL)) {
            add(bird, LootTable.lootTable().withPool(drop(Items.FEATHER, 0, 2, true, false)));
        }
        // LOTREntityButterfly and LOTREntityMidges dropped nothing.
        add(LOTREntities.BUTTERFLY, LootTable.lootTable());
        add(LOTREntities.MIDGES, LootTable.lootTable());
        // LOTREntityDikDik had no dropFewItems.
        add(LOTREntities.DIK_DIK, LootTable.lootTable());
    }

    private LootTable.Builder aurochs(Item horn) {
        return LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 2, 4, true, false))
                .withPool(drop(Items.BEEF, 2, 4, true, true))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(horn)));
    }

    private LootTable.Builder lion() {
        return LootTable.lootTable()
                .withPool(drop(LOTRMaterialItems.LION_FUR, 2, 4, false, false))
                .withPool(drop(LOTRFoodItems.RAW_LION, 1, 2, true, true));
    }

    /**
     * {@code min + nextInt(max - min + 1)} of an item, plus {@code nextInt(1 +
     * looting)} if {@code looting}, and smelted if it died burning if
     * {@code cookable}.
     */
    private LootPool.Builder drop(Item item, int min, int max, boolean looting, boolean cookable) {
        LootItem.Builder<?> entry = LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
        if (cookable) {
            entry.apply(SmeltItemFunction.smelted().when(shouldSmeltLoot()));
        }
        if (looting) {
            entry.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f)));
        }
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(entry);
    }
}
