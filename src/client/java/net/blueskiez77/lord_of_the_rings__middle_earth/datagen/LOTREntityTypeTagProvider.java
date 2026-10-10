package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

/**
 * Entity type tags. {@code minecraft:arthropod} and {@code minecraft:undead}
 * stand in for 1.7.10's getCreatureAttribute() returning ARTHROPOD or UNDEAD:
 * they are what Bane of Arthropods and Smite (through their sensitive_to_
 * tags) and the arthropod and undead checks read now.
 */
public class LOTREntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {

    public LOTREntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static ResourceKey<EntityType<?>> key(EntityType<?> type) {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(type).orElseThrow();
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        // Every LOTR mount takes a vanilla saddle.
        var saddle = builder(EntityTypeTags.CAN_EQUIP_SADDLE);
        for (EntityType<?> mount : List.of(LOTREntities.HORSE, LOTREntities.SHIRE_PONY, LOTREntities.WILD_BOAR,
                LOTREntities.GIRAFFE, LOTREntities.ZEBRA, LOTREntities.RHINO, LOTREntities.CAMEL, LOTREntities.ELK)) {
            saddle.add(key(mount));
        }
        // LOTRItemMountArmor.isValid allowed horse barding on the horse, pony and
        // zebra, but the pony and zebra could wear none at all (func_110259_cr):
        // only the horse.
        builder(EntityTypeTags.CAN_WEAR_HORSE_ARMOR)
                .add(key(LOTREntities.HORSE));

        // LOTREntityScorpion, LOTREntityTermite and LOTREntitySpiderBase.
        builder(EntityTypeTags.ARTHROPOD)
                .add(key(LOTREntities.MIRKWOOD_SPIDER))
                .add(key(LOTREntities.MORDOR_SPIDER))
                .add(key(LOTREntities.JUNGLE_SCORPION))
                .add(key(LOTREntities.DESERT_SCORPION))
                .add(key(LOTREntities.TERMITE));

        // getCreatureAttribute() == UNDEAD: the wraiths and the wight -- what
        // Smite and Wightbane (through sensitive_to_smite) read now.
        builder(EntityTypeTags.UNDEAD)
                .add(key(LOTREntities.GONDOR_RUINS_WRAITH))
                .add(key(LOTREntities.ROHAN_BARROW_WRAITH))
                .add(key(LOTREntities.HARAD_PYRAMID_WRAITH))
                .add(key(LOTREntities.TAUREDAIN_PYRAMID_WRAITH))
                .add(key(LOTREntities.BARROW_WIGHT))
                .add(key(LOTREntities.MARSH_WRAITH));
    }
}
