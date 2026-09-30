package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * The rug the bear, the lions and (later) the wargs and giraffe drop from
 * dropFewItems. Only when a player made the kill, and only 1 time in
 * {@code 30 - 5 * looting} (at least 1). Kept in code rather than a loot
 * table because the chance is not linear in the looting level.
 */
public final class LOTRRugDrops {

    private LOTRRugDrops() {
    }

    public static void dropRug(LivingEntity animal, ServerLevel level, DamageSource source, Item rug) {
        int looting = 0;
        if (source.getEntity() instanceof LivingEntity killer) {
            looting = EnchantmentHelper.getEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING),
                    killer);
        }
        int rugChance = 30 - looting * 5;
        if (animal.getRandom().nextInt(Math.max(rugChance, 1)) == 0) {
            animal.spawnAtLocation(level, new ItemStack(rug));
        }
    }
}
