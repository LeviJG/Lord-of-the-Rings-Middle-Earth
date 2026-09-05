package net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;

/**
 * LOTREnchantmentHelper: rolling an item's modifiers, and making them count.
 *
 * <p>The roll is the original's. A plain item gets two modifiers 10% of the
 * time, one 55% of the time, and none the rest; a "skilful" one -- from a smith
 * rather than from a floor -- gets two 15% of the time and one 65%, and can
 * reach the entries marked skilful. Each candidate is then drawn by weight.
 *
 * <p>Where they are STORED is the port's own decision. The original kept a list
 * of names in the stack's NBT; here they go in {@code minecraft:custom_data}
 * under the same key, which is the nearest 26.2 has and survives without a
 * registered component type. What the modifiers DO is then written onto the
 * stack as ordinary components -- attribute modifiers, max damage, lore -- so
 * the game applies them with no further help and the tooltip lists them.
 *
 * <p>NOT ported: the reforging side. LOTRContainerAnvil could re-roll an item,
 * keeping the bane modifiers and clearing the rest, and there is no LOTR anvil
 * in the port yet. {@link #applyRandom} is the half that matters for items
 * coming into the world.
 */
public final class LOTRModifiers {

    /** The NBT key the original used: "LOTREnchants", a list of names. */
    private static final String TAG = "LOTREnchants";

    /** hasAppliedRandomEnchants: set once so an item is never rolled twice. */
    private static final String TAG_ROLLED = "LOTREnchantsApplied";

    private LOTRModifiers() {
    }

    /** getEnchantList: what this stack is wearing. */
    public static List<LOTRModifier> get(ItemStack stack) {
        List<LOTRModifier> out = new ArrayList<>();
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return out;
        }
        CompoundTag tag = data.copyTag();
        tag.getList(TAG).ifPresent(list -> {
            for (int i = 0; i < list.size(); i++) {
                list.getString(i).map(LOTRModifier::byName).ifPresent(m -> {
                    if (m != null) {
                        out.add(m);
                    }
                });
            }
        });
        return out;
    }

    /**
     * calcExtraKnockback: how many whole knockback levels this stack carries.
     *
     * <p>Read directly rather than off the attribute component, because the
     * thrown axe is not swinging at anything -- its knockback is applied by the
     * projectile itself, as LOTRWeaponStats.getRangedKnockback did.
     */
    public static int knockback(ItemStack stack) {
        int total = 0;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.effect() == LOTRModifier.Effect.KNOCKBACK) {
                total += (int) modifier.value();
            }
        }
        return total;
    }

    public static boolean hasBeenRolled(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBooleanOr(TAG_ROLLED, false);
    }

    /**
     * applyRandomEnchantments, for the half of it the port can reach: clear
     * whatever is there, roll how many, draw them by weight, then write them on.
     */
    public static void applyRandom(ItemStack stack, RandomSource random, boolean skilful) {
        LOTRModifier.Kind kind = kindOf(stack);
        if (kind == null) {
            return;
        }

        int count = 0;
        float chance = random.nextFloat();
        if (skilful) {
            if (chance < 0.15f) {
                count = 2;
            } else if (chance < 0.8f) {
                count = 1;
            }
        } else if (chance < 0.1f) {
            count = 2;
        } else if (chance < 0.65f) {
            count = 1;
        }

        List<LOTRModifier> candidates = new ArrayList<>();
        for (LOTRModifier modifier : LOTRModifier.applicableTo(kind)) {
            if (modifier.isSkilful() && !skilful) {
                continue;
            }
            candidates.add(modifier);
        }

        List<LOTRModifier> chosen = new ArrayList<>();
        for (int i = 0; i < count && !candidates.isEmpty(); i++) {
            LOTRModifier drawn = drawByWeight(candidates, random);
            if (drawn == null) {
                break;
            }
            chosen.add(drawn);
            // One of each effect at most: the original's canApply refused a
            // second modifier that fought with one already on the item.
            candidates.removeIf(other -> other.effect() == drawn.effect());
        }

        set(stack, chosen);
    }

    private static LOTRModifier drawByWeight(List<LOTRModifier> candidates, RandomSource random) {
        int total = 0;
        for (LOTRModifier modifier : candidates) {
            total += modifier.weight();
        }
        if (total <= 0) {
            return null;
        }
        int roll = random.nextInt(total);
        for (LOTRModifier modifier : candidates) {
            roll -= modifier.weight();
            if (roll < 0) {
                return modifier;
            }
        }
        return null;
    }

    /** Write a set of modifiers onto a stack, effects and all. */
    public static void set(ItemStack stack, List<LOTRModifier> modifiers) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA, data.update(tag -> {
            net.minecraft.nbt.ListTag list = new net.minecraft.nbt.ListTag();
            for (LOTRModifier modifier : modifiers) {
                list.add(net.minecraft.nbt.StringTag.valueOf(modifier.getSerializedName()));
            }
            tag.put(TAG, list);
            tag.putBoolean(TAG_ROLLED, true);
        }));
        applyEffects(stack, modifiers);
    }

    /**
     * Turn the names into things the game acts on.
     *
     * <p>Damage, speed, reach and knockback become attribute modifiers layered
     * on top of whatever the item already had; durability rescales max damage;
     * protection adds armour points. Each also adds a line of lore, which is how
     * the modifier's name reaches the tooltip -- the original printed them there
     * itself, and lore is the component that does that job now.
     */
    private static void applyEffects(ItemStack stack, List<LOTRModifier> modifiers) {
        if (modifiers.isEmpty()) {
            return;
        }

        ItemAttributeModifiers base = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,
                stack.getItem().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,
                        ItemAttributeModifiers.EMPTY));
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        // Everything the item already carried, adjusted where a modifier
        // multiplies it rather than adding to it.
        boolean hasReachEntry = false;
        for (ItemAttributeModifiers.Entry entry : base.modifiers()) {
            AttributeModifier modifier = entry.modifier();
            double amount = modifier.amount();
            for (LOTRModifier extra : modifiers) {
                amount = adjust(entry.attribute(), amount, extra);
            }
            hasReachEntry |= entry.attribute().equals(Attributes.ENTITY_INTERACTION_RANGE);
            builder.add(entry.attribute(),
                    new AttributeModifier(modifier.id(), amount, modifier.operation()),
                    entry.slot());
        }

        // A reach modifier on an item that has no reach entry to rescale.
        //
        // Only the daggers carry one of their own; createSwordAttributes and
        // createToolAttributes build ATTACK_DAMAGE and ATTACK_SPEED and nothing
        // else, and a spear's longer reach is the ATTACK_RANGE component rather
        // than an attribute. So a Long sword, cleaver, battleaxe, warhammer or
        // spear had the name and the tooltip line and no reach at all, where
        // LOTRWeaponStats.getMeleeReachFactor scaled the player's reach for ANY
        // melee weapon. The entry is added here from the bare base of 3 --
        // exactly what adjust() would have worked out for an amount of zero.
        if (!hasReachEntry) {
            for (LOTRModifier modifier : modifiers) {
                if (modifier.effect() == LOTRModifier.Effect.REACH) {
                    builder.add(Attributes.ENTITY_INTERACTION_RANGE,
                            new AttributeModifier(id(modifier),
                                    BASE_INTERACTION_RANGE * modifier.value() - BASE_INTERACTION_RANGE,
                                    AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND);
                }
            }
        }

        // And the ones that are new attributes rather than a change to an old one.
        for (LOTRModifier modifier : modifiers) {
            switch (modifier.effect()) {
                case KNOCKBACK -> builder.add(Attributes.ATTACK_KNOCKBACK,
                        new AttributeModifier(id(modifier), modifier.value(),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND);
                case ARMOR -> builder.add(Attributes.ARMOR,
                        new AttributeModifier(id(modifier), modifier.value(),
                                AttributeModifier.Operation.ADD_VALUE),
                        armorSlot(stack));
                case DURABILITY -> {
                    int max = stack.getMaxDamage();
                    if (max > 0) {
                        stack.set(DataComponents.MAX_DAMAGE, Math.round(max * modifier.value()));
                    }
                }
                default -> { }
            }
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
        nameAndDescribe(stack, modifiers);
    }

    /**
     * How a modified item reads.
     *
     * <p>Two separate things. The modifier's NAME is a PREFIX ON THE ITEM'S OWN
     * NAME -- getFullEnchantedName folds each one in through
     * {@code lotr.enchant.nameFormat}, "%s %s", walking the list in reverse --
     * so a keen, hardy bronze sword is a "Keen Hardy Bronze Sword", not a Bronze
     * Sword with two lines under it.
     *
     * <p>What belongs in the tooltip is getNamedFormattedDescription: the name
     * and what it DOES, through {@code lotr.enchant.descFormat}, "%s: %s" --
     * "Sharp: +0.5 melee damage", "Slow: x0.75 melee speed". Grey when the
     * modifier is a gift, dark grey when it is a flaw.
     *
     * <p>ITEM_NAME rather than CUSTOM_NAME: the latter is the anvil-rename
     * component and renders in italics, which this is not.
     */
    private static void nameAndDescribe(ItemStack stack, List<LOTRModifier> modifiers) {
        Component name = stack.getItem().getName(stack);
        for (int i = modifiers.size() - 1; i >= 0; i--) {
            name = Component.translatable("lotr.enchant.nameFormat",
                    Component.translatable(modifiers.get(i).translationKey()), name);
        }
        stack.set(DataComponents.ITEM_NAME, name);

        List<Component> lore = new ArrayList<>();
        for (LOTRModifier modifier : modifiers) {
            Component line = Component.translatable("lotr.enchant.descFormat",
                    Component.translatable(modifier.translationKey()),
                    Component.translatable(modifier.effect().descriptionKey(),
                            modifier.formattedValue()));
            lore.add(line.copy().withStyle(
                    modifier.isBeneficial() ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
        }
        stack.set(DataComponents.LORE, new ItemLore(lore));
    }

    /** Attributes.ATTACK_SPEED is a delta off this. */
    private static final double BASE_ATTACK_SPEED = 4.0;

    /** And Attributes.ENTITY_INTERACTION_RANGE off this. */
    private static final double BASE_INTERACTION_RANGE = 3.0;

    /** Damage adds, speed and reach multiply -- as the original's helpers did. */
    private static double adjust(net.minecraft.core.Holder<Attribute> attribute, double amount,
            LOTRModifier modifier) {
        if (attribute.equals(Attributes.ATTACK_DAMAGE) && modifier.effect() == LOTRModifier.Effect.DAMAGE) {
            return amount + modifier.value();
        }
        if (attribute.equals(Attributes.ATTACK_SPEED) && modifier.effect() == LOTRModifier.Effect.ATTACK_SPEED) {
            // The attribute is a delta off a base of 4, so the multiplier has to
            // act on the speed itself rather than on the modifier.
            return (BASE_ATTACK_SPEED + amount) * modifier.value() - BASE_ATTACK_SPEED;
        }
        if (attribute.equals(Attributes.ENTITY_INTERACTION_RANGE) && modifier.effect() == LOTRModifier.Effect.REACH) {
            // Same again, off a base of 3.
            return (BASE_INTERACTION_RANGE + amount) * modifier.value() - BASE_INTERACTION_RANGE;
        }
        return amount;
    }

    private static EquipmentSlotGroup armorSlot(ItemStack stack) {
        net.minecraft.world.item.equipment.Equippable equippable =
                stack.get(DataComponents.EQUIPPABLE);
        return equippable == null
                ? EquipmentSlotGroup.ARMOR
                : EquipmentSlotGroup.bySlot(equippable.slot());
    }

    /**
     * The attribute modifier's id.
     *
     * <p>The serialized name has to stay as the original wrote it -- "protectWeak1",
     * "meleeSpeed1" -- because that is the NBT value and the lang key. An
     * Identifier path may only contain [a-z0-9/._-], though, so the camel case
     * is broken into snake case here. Feeding the raw name straight in threw
     * IdentifierException the moment a player held a modified item.
     */
    private static Identifier id(LOTRModifier modifier) {
        StringBuilder path = new StringBuilder("modifier/");
        for (char c : modifier.getSerializedName().toCharArray()) {
            if (Character.isUpperCase(c)) {
                path.append('_').append(Character.toLowerCase(c));
            } else if (Character.isDigit(c)) {
                path.append('_').append(c);
            } else {
                path.append(c);
            }
        }
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, path.toString());
    }

    /**
     * Which family of modifiers an item can take. A throwing axe if it is one,
     * armour if it is worn, a melee weapon if it swings, and nothing otherwise.
     */
    public static LOTRModifier.Kind kindOf(ItemStack stack) {
        // Before the others: a throwing axe wears out and so would otherwise
        // read as merely BREAKABLE, which would cost it the damage and
        // knockback families the original gives it.
        if (stack.getItem() instanceof net.blueskiez77.lord_of_the_rings__middle_earth
                .common.item.LOTRThrowingAxeItem) {
            return LOTRModifier.Kind.THROWING_AXE;
        }
        if (stack.has(DataComponents.EQUIPPABLE)) {
            return LOTRModifier.Kind.ARMOR;
        }
        if (stack.has(DataComponents.WEAPON)) {
            return LOTRModifier.Kind.MELEE;
        }
        // Anything else that wears out -- a bow -- takes the durability
        // modifiers and nothing more. LOTREnchantmentType.BREAKABLE was exactly
        // "item.isDamageable()".
        if (stack.isDamageableItem()) {
            return LOTRModifier.Kind.BREAKABLE;
        }
        return null;
    }
}
