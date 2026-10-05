package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRivendellHouseStructure extends LOTRHighElfHouseStructure {
    public LOTRRivendellHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRElfEntity createElf(WorldGenLevel world) {
        return create(LOTREntities.RIVENDELL_ELF, world);
    }

    @Override
    public ItemStack getElfFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetRivendell", 1, 0), LOTRLegacyItems.modStack("bodyRivendell", 1, 0), LOTRLegacyItems.modStack("legsRivendell", 1, 0), LOTRLegacyItems.modStack("bootsRivendell", 1, 0), LOTRLegacyItems.modStack("daggerRivendell", 1, 0), LOTRLegacyItems.modStack("swordRivendell", 1, 0), LOTRLegacyItems.modStack("spearRivendell", 1, 0), LOTRLegacyItems.modStack("longspearRivendell", 1, 0), LOTRLegacyItems.modStack("rivendellBow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.vanillaStack("feather", 1, 0), LOTRLegacyItems.modStack("swanFeather", 1, 0), LOTRLegacyItems.modStack("quenditeCrystal", 1, 0), LOTRLegacyItems.modStack("goldRing", 1, 0), LOTRLegacyItems.modStack("silverRing", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tableBlock = LOTRLegacyBlocks.mod("rivendellTable");
        bannerType = "RIVENDELL";
        chestContents = LOTRChestContents.RIVENDELL_HALL;
    }
}
