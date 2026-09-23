package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRItemMug, for a FULL vessel: a drink.
 *
 * <p>Three kinds, as the constructors had them: plain drinks (water, milk, the
 * jungle remedy), FOOD drinks (juices, chocolate) that need an appetite, and
 * BREWED drinks, which come in five strengths from weak to potent. Strength
 * scales everything a brew does -- how much it feeds (x0.5 to x1.5), how long
 * its effects last and how drunk it gets you (x0.25 to x3).
 *
 * <p>onEaten, in order: feed the drinker if they can eat; roll for drunkenness
 * (alcoholicity x strength, cut 1% per point of tolerance) and, if it takes,
 * nausea for about a minute per unit and tolerance that grows with it; apply
 * the effects; do any damage; cure effects if it is milk; hand back the empty
 * vessel. The Athelas brew and jungle remedy also clear harmful effects, the
 * Morgul-draught only works for friends of Mordor and poisons everyone else,
 * and termite tequila explodes one time in six.
 *
 * <p>Used on a block, a drink is set down there as a vessel block (tryPlaceMug).
 *
 * <p>NOT ported: the achievements, drinking by NPCs, and poisoning -- the bottle
 * of poison is not in the port yet.
 */
public class LOTRDrinkItem extends Item {

    public static final String[] STRENGTH_NAMES = {"weak", "light", "moderate", "strong", "potent"};
    private static final float[] STRENGTHS = {0.25f, 0.5f, 1.0f, 2.0f, 3.0f};
    private static final float[] FOOD_STRENGTHS = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f};

    /** The per-drink overrides the original made with subclasses and == checks. */
    public enum Special {
        NONE, CURES_HARMFUL, MORGUL, TERMITE
    }

    private final boolean foodDrink;
    private final boolean brewable;
    private final float alcoholicity;
    private int foodHeal;
    private float foodSaturation;
    private final List<MobEffectInstance> effects = new ArrayList<>();
    private int damageAmount;
    private boolean curesEffects;
    private Special special = Special.NONE;

    public LOTRDrinkItem(boolean foodDrink, boolean brewable, float alcoholicity, Properties properties) {
        super(properties);
        this.foodDrink = foodDrink;
        this.brewable = brewable;
        this.alcoholicity = alcoholicity;
    }

    public LOTRDrinkItem setDrinkStats(int heal, float saturation) {
        this.foodHeal = heal;
        this.foodSaturation = saturation;
        return this;
    }

    /** addPotionEffect(id, seconds). */
    public LOTRDrinkItem addEffect(Holder<MobEffect> effect, int seconds) {
        this.effects.add(new MobEffectInstance(effect, seconds * 20));
        return this;
    }

    public LOTRDrinkItem setDamageAmount(int damage) {
        this.damageAmount = damage;
        return this;
    }

    public LOTRDrinkItem setCuresEffects() {
        this.curesEffects = true;
        return this;
    }

    public LOTRDrinkItem setSpecial(Special special) {
        this.special = special;
        return this;
    }

    public boolean isBrewable() {
        return this.brewable;
    }

    /** canPlayerDrink: a food drink wants an appetite, anything else goes down any time. */
    public boolean canPlayerDrink(Player player) {
        return !this.foodDrink || player.canEat(false);
    }

    /** getStrengthSubtitle: "Weak" to "Potent" for a brew, nothing for anything else. */
    public static Component strengthName(ItemStack stack) {
        if (stack.getItem() instanceof LOTRDrinkItem drink && drink.brewable) {
            return Component.translatable("item.lotr.drink." + STRENGTH_NAMES[strengthIndex(stack)]);
        }
        return Component.empty();
    }

    /** onItemUse -> tryPlaceMug: set the drink down, still in its vessel. */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        return net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock.tryPlaceMug(
                context, context.getItemInHand());
    }

    /** A drink at the given strength, in a mug -- what getSubItems listed. */
    public static ItemStack stack(Item item, int strength) {
        ItemStack stack = new ItemStack(item);
        stack.set(LOTRDataComponents.DRINK_STRENGTH, strength);
        return stack;
    }

    public static LOTRVessel vessel(ItemStack stack) {
        return stack.getOrDefault(LOTRDataComponents.VESSEL, LOTRVessel.MUG);
    }

    private static int strengthIndex(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(LOTRDataComponents.DRINK_STRENGTH, 0), 0, STRENGTHS.length - 1);
    }

    private float strength(ItemStack stack) {
        return this.brewable ? STRENGTHS[strengthIndex(stack)] : 1.0f;
    }

    private float foodStrength(ItemStack stack) {
        return this.brewable ? FOOD_STRENGTHS[strengthIndex(stack)] : 1.0f;
    }

    private List<MobEffectInstance> effectsFor(float strength) {
        List<MobEffectInstance> list = new ArrayList<>();
        for (MobEffectInstance base : this.effects) {
            list.add(new MobEffectInstance(base.getEffect(), (int) (base.getDuration() * strength)));
        }
        return list;
    }

    /** canPlayerDrink: a food drink wants an appetite, anything else goes down any time. */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (this.foodDrink && !player.canEat(false)) {
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        LOTRVessel vessel = vessel(stack);
        if (entity instanceof Player player) {
            drink(stack, level, player);
        }
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return result;
        }
        ItemStack empty = vessel.emptyStack();
        if (result.isEmpty()) {
            return empty;
        }
        if (entity instanceof Player player && !player.getInventory().add(empty)) {
            player.drop(empty, false);
        }
        return result;
    }

    private void drink(ItemStack stack, Level level, Player player) {
        float strength = strength(stack);
        float foodStrength = foodStrength(stack);
        if (player.canEat(false)) {
            player.getFoodData().eat(Math.round(this.foodHeal * foodStrength), this.foodSaturation * foodStrength);
        }
        if (!(level instanceof ServerLevel server)) {
            return;
        }
        if (this.alcoholicity > 0.0f) {
            float power = this.alcoholicity * strength;
            int tolerance = LOTRAlcoholTolerance.get(player);
            if (tolerance > 0) {
                power *= (float) Math.pow(0.99, tolerance);
            }
            if (level.getRandom().nextFloat() < power) {
                int duration = (int) (60.0f * (1.0f + level.getRandom().nextFloat() * 0.5f) * power);
                if (duration >= 1) {
                    player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, duration * 20));
                    LOTRAlcoholTolerance.set(player, tolerance + Math.round(duration / 20.0f));
                }
            }
        }
        boolean applyEffects = this.special != Special.MORGUL
                || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR) > 0.0f;
        if (applyEffects) {
            for (MobEffectInstance effect : effectsFor(strength)) {
                player.addEffect(effect);
            }
        }
        if (this.damageAmount > 0) {
            player.hurtServer(server, player.damageSources().magic(), this.damageAmount * strength);
        }
        if (this.curesEffects) {
            player.removeAllEffects();
        }
        switch (this.special) {
            case CURES_HARMFUL -> {
                for (MobEffectInstance active : new ArrayList<>(player.getActiveEffects())) {
                    if (active.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                        player.removeEffect(active.getEffect());
                    }
                }
            }
            case MORGUL -> {
                if (!applyEffects) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
                }
            }
            case TERMITE -> {
                if (level.getRandom().nextInt(6) == 0) {
                    server.explode(null, player.getX(), player.getY(), player.getZ(), 3.0f,
                            Level.ExplosionInteraction.TNT);
                }
            }
            default -> {
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        if (!this.brewable) {
            return;
        }
        float strength = strength(stack);
        builder.accept(Component.translatable("item.lotr.drink." + STRENGTH_NAMES[strengthIndex(stack)]));
        if (this.alcoholicity > 0.0f) {
            float percent = this.alcoholicity * strength * 10.0f;
            ChatFormatting colour = percent < 2.0f ? ChatFormatting.GREEN : percent < 5.0f ? ChatFormatting.YELLOW
                    : percent < 10.0f ? ChatFormatting.GOLD : percent < 20.0f ? ChatFormatting.RED : ChatFormatting.DARK_RED;
            builder.accept(Component.translatable("item.lotr.drink.alcoholicity")
                    .append(String.format(": %.2f%%", percent)).withStyle(colour));
        }
        PotionContents.addPotionTooltip(effectsFor(strength), builder, 1.0f, context.tickRate());
        if (this.special == Special.TERMITE) {
            builder.accept(Component.translatable("item.lotr.drink.explode").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
