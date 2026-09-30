package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;

/**
 * LOTRItemSpawnEgg. The original was one item told apart by damage value; 26.2
 * spawn eggs are one item per creature, and vanilla's SpawnEggItem already does
 * what onItemUse and LOTRDispenseSpawnEgg did -- spawn at the clicked face with
 * a random yaw, run the spawn-egg initialisation, carry over a renamed egg's
 * name, and use one up outside creative. Pick-block on the creature gives its
 * egg, as the original's getPickedResult did.
 *
 * <p>The look is the original's too: 1.7.10's shared egg and spots textures,
 * tinted with the two colours registerCreature gave the creature (the item
 * model's constant tints). The living sound the original played on hatching is
 * {@link #playHatchSound}.
 */
public class LOTRSpawnEggItem extends SpawnEggItem implements LOTRTooltipItem {

    private final int primaryColour;
    private final int secondaryColour;

    public LOTRSpawnEggItem(Properties properties, int primaryColour, int secondaryColour) {
        super(properties);
        this.primaryColour = primaryColour;
        this.secondaryColour = secondaryColour;
    }

    /** SpawnEggInfo.primaryColor: the egg. */
    public int primaryColour() {
        return this.primaryColour;
    }

    /** SpawnEggInfo.secondaryColor: the spots. */
    public int secondaryColour() {
        return this.secondaryColour;
    }

    /**
     * spawnCreature ended with playLivingSound, for an egg used by hand or
     * from a dispenser. Called from the LOTR creatures' finalizeSpawn, which
     * is where 26.2 hands a freshly hatched mob its spawn reason.
     */
    public static void playHatchSound(Mob mob, EntitySpawnReason reason) {
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE || reason == EntitySpawnReason.DISPENSER) {
            mob.playAmbientSound();
        }
    }

    /** addInformation: the creature's name under the egg's. */
    @Override
    public void addTooltip(ItemStack stack, TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        EntityType<?> type = getType(stack);
        if (type != null) {
            builder.accept(type.getDescription().copy().withStyle(ChatFormatting.GRAY));
        }
    }
}
