package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class LOTRDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(LOTRModelProvider::new);
        pack.addProvider(LOTRBlockLootProvider::new);
        pack.addProvider(LOTRBlockTagProvider::new);
        pack.addProvider(LOTRRecipeProvider::new);
    }
}