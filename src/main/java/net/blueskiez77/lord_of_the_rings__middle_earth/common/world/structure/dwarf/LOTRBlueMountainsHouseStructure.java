package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBlueMountainsHouseStructure extends LOTRDwarfHouseStructure {
    public LOTRBlueMountainsHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRDwarfEntity createDwarf(WorldGenLevel world) {
        return create(LOTREntities.BLUE_DWARF, world);
    }

    @Override
    public ItemStack getRandomOtherItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetBlueDwarven", 1, 0), LOTRLegacyItems.modStack("bodyBlueDwarven", 1, 0), LOTRLegacyItems.modStack("legsBlueDwarven", 1, 0), LOTRLegacyItems.modStack("bootsBlueDwarven", 1, 0), LOTRLegacyItems.modStack("blueDwarfSteel", 1, 0), LOTRLegacyItems.modStack("bronze", 1, 0), LOTRLegacyItems.vanillaStack("iron_ingot", 1, 0), LOTRLegacyItems.modStack("silver", 1, 0), LOTRLegacyItems.modStack("silverNugget", 1, 0), LOTRLegacyItems.vanillaStack("gold_ingot", 1, 0), LOTRLegacyItems.vanillaStack("gold_nugget", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public ItemStack getRandomWeaponItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordBlueDwarven", 1, 0), LOTRLegacyItems.modStack("daggerBlueDwarven", 1, 0), LOTRLegacyItems.modStack("hammerBlueDwarven", 1, 0), LOTRLegacyItems.modStack("battleaxeBlueDwarven", 1, 0), LOTRLegacyItems.modStack("pickaxeBlueDwarven", 1, 0), LOTRLegacyItems.modStack("mattockBlueDwarven", 1, 0), LOTRLegacyItems.modStack("throwingAxeBlueDwarven", 1, 0), LOTRLegacyItems.modStack("pikeBlueDwarven", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        stoneBlock = LOTRLegacyBlocks.vanilla("stone");
        stoneMeta = 0;
        fillerBlock = LOTRLegacyBlocks.mod("rock");
        fillerMeta = 3;
        topBlock = LOTRLegacyBlocks.mod("rock");
        topMeta = 3;
        brick2Block = LOTRLegacyBlocks.mod("brick");
        brick2Meta = 14;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 3;
        chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
        chandelierMeta = 11;
        tableBlock = LOTRLegacyBlocks.mod("blueDwarvenTable");
        barsBlock = LOTRLegacyBlocks.mod("blueDwarfBars");
        larderContents = LOTRChestContents.BLUE_DWARF_HOUSE_LARDER;
        personalContents = LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD;
        plateFoods = LOTRFoods.BLUE_DWARF;
        drinkFoods = LOTRFoods.DWARF_DRINK;
    }
}
