package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * Datagen entrypoint. Run via the `runDatagen` Gradle task; output lands in
 * src/main/generated and is bundled as resources.
 *
 * Point fabric.mod.json's "fabric-datagen" entrypoint at this class:
 *   "fabric-datagen": [
 *     "net.blueskiez77.lord_of_the_rings__middle_earth.datagen.LOTRDataGenerator"
 *   ]
 * (If the template generated its own datagen class, either replace its body
 * with this or repoint the entrypoint here and delete the old one.)
 */
public class LOTRDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(LOTRModelProvider::new);
        pack.addProvider(LOTRBlockLootProvider::new);
        pack.addProvider(LOTRBlockTagProvider::new);
    }
}