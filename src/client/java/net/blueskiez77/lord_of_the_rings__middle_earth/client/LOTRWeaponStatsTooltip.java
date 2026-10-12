package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBlowgunItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBowItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCrossbowItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPoisonedDaggerItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRThrowingAxeItem;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * LOTRTickHandlerClient.getItemTooltip's squadron line and LOTRWeaponStats'
 * stat lines.
 *
 * <p>The melee lines (damage, speed, reach, knockback) and the armour line are
 * not drawn: 26.2's attribute modifier lines already show attack damage,
 * attack speed, entity interaction range, attack knockback and armour for the
 * same items, and LOTRModifiers writes the mod's melee and protection
 * modifiers onto those attributes. What is left is the ranged block, which
 * vanilla has no lines for, and the poisoned dagger's line.
 */
public final class LOTRWeaponStatsTooltip {

    /** LOTRWeaponStats.getRangedSpeed's base: a 20-tick draw is 100%. */
    private static final int BASE_DRAW_TICKS = 20;

    /** getRangedDamageFactor's baseArrowFactor. */
    private static final float BASE_ARROW_FACTOR = 2.0f;

    /** CrossbowItem.getChargeDuration with no Quick Charge: 1.25 seconds. */
    private static final int CROSSBOW_CHARGE_TICKS = 25;

    private LOTRWeaponStatsTooltip() {
    }

    public static void init() {
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            if (lines.isEmpty()
                    || stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).hideTooltip()) {
                return;
            }
            // LOTRSquadrons.SquadronItem: the company, straight under the name.
            if (LOTRHiredNetworking.isSquadronItem(stack)) {
                String squadron = LOTRCommandHornItem.getSquadron(stack);
                if (!squadron.isEmpty()) {
                    lines.add(1, Component.translatable("item.lotr.generic.squadron", squadron)
                            .withStyle(ChatFormatting.GRAY));
                }
            }
            if (isRangedWeapon(stack)) {
                addRangedLines(stack, lines);
            }
            // isPoisoned: a dagger with DaggerEffect.POISON.
            if (stack.getItem() instanceof LOTRPoisonedDaggerItem) {
                lines.add(Component.translatable("lotr.weaponstat.poison").withStyle(ChatFormatting.DARK_GREEN));
            }
        });
    }

    /**
     * isRangedWeapon: an ItemBow (the mod's crossbow was one), a blowgun or a
     * throwing axe. The spear's throw is gone -- the port's spears are
     * vanilla's, which do not throw -- so it has no ranged block.
     */
    private static boolean isRangedWeapon(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof BowItem || item instanceof LOTRCrossbowItem || item instanceof LOTRThrowingAxeItem;
    }

    private static void addRangedLines(ItemStack stack, List<Component> lines) {
        lines.add(Component.empty());
        Item item = stack.getItem();

        float drawSpeed = rangedSpeed(stack);
        if (drawSpeed > 0.0f) {
            lines.add(line("lotr.weaponstat.rangedSpeed", Math.round(drawSpeed * 100.0f)));
        }
        float damage = rangedDamageFactor(stack, false);
        if (damage > 0.0f) {
            lines.add(line("lotr.weaponstat.rangedDamage", Math.round(damage * 100.0f)));
            if ((item instanceof BowItem && !(item instanceof LOTRBlowgunItem)) || item instanceof LOTRCrossbowItem) {
                float range = rangedDamageFactor(stack, true);
                lines.add(line("lotr.weaponstat.range", Math.round(range * 100.0f)));
            }
        }
        int kb = rangedKnockback(stack);
        if (kb > 0) {
            lines.add(line("lotr.weaponstat.kb", kb));
        }
    }

    private static Component line(String key, int value) {
        return Component.translatable(key, value).withStyle(ChatFormatting.DARK_GREEN);
    }

    /**
     * getRangedSpeed: 20 ticks over the full draw. A bow's draw is its
     * drawTicks (vanilla's bow 20, the blowgun 5); the crossbow's is vanilla's
     * charge, Quick Charge included, where the original's was a flat 50.
     */
    private static float rangedSpeed(ItemStack stack) {
        Item item = stack.getItem();
        int time = 0;
        if (item instanceof LOTRCrossbowItem) {
            Player player = Minecraft.getInstance().player;
            time = player != null ? CrossbowItem.getChargeDuration(stack, player) : CROSSBOW_CHARGE_TICKS;
        } else if (item instanceof LOTRBowItem bow) {
            time = bow.getDrawTicks();
        } else if (item instanceof BowItem) {
            time = BowItem.MAX_DRAW_DURATION;
        }
        return time > 0 ? (float) BASE_DRAW_TICKS / time : 0.0f;
    }

    /**
     * getRangedDamageFactor: the launch speed against a plain bow's, and with
     * {@code launchSpeedOnly} false, Power's bonus and the projectile's own
     * weight too -- a bolt hits for twice an arrow (LOTRCrossbowBoltEntity's
     * base damage 4), a dart for half (LOTRDartEntity's 1).
     */
    private static float rangedDamageFactor(ItemStack stack, boolean launchSpeedOnly) {
        Item item = stack.getItem();
        float weaponFactor = 0.0f;
        if (item instanceof LOTRCrossbowItem crossbow) {
            weaponFactor = BASE_ARROW_FACTOR * crossbow.getBoltVelocityFactor();
            weaponFactor *= LOTRModifiers.rangedDamageFactor(stack);
            if (!launchSpeedOnly) {
                weaponFactor += powerBonus(stack);
                weaponFactor *= 2.0f;
            }
        } else if (item instanceof LOTRBlowgunItem) {
            weaponFactor = BASE_ARROW_FACTOR;
            if (!launchSpeedOnly) {
                weaponFactor *= 1.0f / BASE_ARROW_FACTOR;
            }
            weaponFactor *= LOTRModifiers.rangedDamageFactor(stack);
        } else if (item instanceof BowItem) {
            weaponFactor = BASE_ARROW_FACTOR;
            if (item instanceof LOTRBowItem bow) {
                weaponFactor *= bow.getVelocityFactor();
            }
            weaponFactor *= LOTRModifiers.rangedDamageFactor(stack);
            if (!launchSpeedOnly) {
                weaponFactor += powerBonus(stack);
            }
        } else if (item instanceof LOTRThrowingAxeItem axe) {
            // getRangedDamageMultiplier: (material damage + 4) * 0.5.
            weaponFactor = (axe.getMaterial().attackDamageBonus() + 4.0f) * 0.5f;
        }
        return weaponFactor > 0.0f ? weaponFactor / BASE_ARROW_FACTOR : 0.0f;
    }

    /** Power's extra arrow damage, 0.5 per level plus 0.5. */
    private static float powerBonus(ItemStack stack) {
        int power = enchantmentLevel(stack, Enchantments.POWER);
        return power > 0 ? power * 0.5f + 0.5f : 0.0f;
    }

    /**
     * getRangedKnockback: a throwing axe goes down the melee path -- its
     * knockback modifiers, which LOTRThrowingAxeEntity.doKnockback applies --
     * and a launcher adds Punch to its rangedKnockback modifiers.
     */
    private static int rangedKnockback(ItemStack stack) {
        if (stack.getItem() instanceof LOTRThrowingAxeItem) {
            return LOTRModifiers.knockback(stack);
        }
        return enchantmentLevel(stack, Enchantments.PUNCH) + LOTRModifiers.rangedKnockback(stack);
    }

    private static int enchantmentLevel(ItemStack stack, ResourceKey<Enchantment> key) {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : stack.getEnchantments().entrySet()) {
            if (entry.getKey().is(key)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }
}
