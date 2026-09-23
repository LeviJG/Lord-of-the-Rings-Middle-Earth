package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRItemEntDraught: the Ents' drinks, seven of them, served in a bowl.
 *
 * <p>Green quickens, strengthens and hastens; brown is a hearty meal; gold does
 * nothing at all; yellow heals; red wards off fire; silver sees in the dark;
 * blue lets you breathe water. Anyone Fangorn counts an enemy is poisoned for
 * five seconds instead.
 */
public class LOTREntDraughtItem extends Item {

    private record Draught(int heal, float saturation, List<MobEffectInstance> effects) {
    }

    private static final List<Draught> DRAUGHTS = List.of(
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.SPEED, 120 * 20),
                    new MobEffectInstance(MobEffects.HASTE, 120 * 20), new MobEffectInstance(MobEffects.STRENGTH, 120 * 20))),
            new Draught(20, 3.0f, List.of()),
            new Draught(0, 0.0f, List.of()),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.REGENERATION, 60 * 20))),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 180 * 20))),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.NIGHT_VISION, 180 * 20))),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.WATER_BREATHING, 150 * 20))));

    public static final int COUNT = 7;

    public LOTREntDraughtItem(Properties properties) {
        super(properties);
    }

    public static ItemStack stack(Item item, int index) {
        ItemStack stack = new ItemStack(item);
        stack.set(LOTRDataComponents.ENT_DRAUGHT, index);
        return stack;
    }

    private static int index(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(LOTRDataComponents.ENT_DRAUGHT, 0), 0, COUNT - 1);
    }

    /** getUnlocalizedName(stack): item.lotr:entDraught.N. */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId() + "." + index(stack));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Draught draught = DRAUGHTS.get(index(stack));
        if (entity instanceof Player player) {
            if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.FANGORN) < 0.0f) {
                if (!level.isClientSide()) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
                }
            } else {
                if (player.canEat(false)) {
                    player.getFoodData().eat(draught.heal(), draught.saturation());
                }
                if (!level.isClientSide()) {
                    for (MobEffectInstance effect : draught.effects()) {
                        player.addEffect(new MobEffectInstance(effect));
                    }
                }
            }
        }
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return result;
        }
        return result.isEmpty() ? new ItemStack(Items.BOWL) : result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        PotionContents.addPotionTooltip(DRAUGHTS.get(index(stack)).effects(), builder, 1.0f, context.tickRate());
    }
}
