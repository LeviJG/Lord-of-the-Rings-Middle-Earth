package net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;

/**
 * LOTREnchantment: the mod's own modifiers, the ones an item turns up already
 * wearing -- a "Keen" blade, a "Crooked" one, "Lasting" armour, a "Handy"
 * pickaxe.
 *
 * <p>Nothing to do with vanilla enchanting. Each is a small permanent change to
 * an item's numbers, rolled at random when the item first comes into the world
 * (LOTRModifiers.applyRandom), and named rather than levelled.
 *
 * <p>Every entry the random roll can reach is here, with the original's
 * weights, values and names. So are the two banes the roll itself hands out --
 * Wightbane on the barrow blades and Spiderbane on Sting -- which have no
 * weight and so are never drawn. NOT here: the other banes, earned by killing
 * enough Elves, Orcs, Dwarves, Wargs or Trolls, which wait on those NPCs and
 * the kill counting that goes with them; "True" (protectMithril), which only
 * mithril mail gives; and the three weapon specials -- Infernal, Chilling,
 * Headhunting -- which come from the LOTR anvil's template items. None of
 * those is part of the random roll.
 */
public enum LOTRModifier implements StringRepresentable {

    // --- LOTREnchantmentDamage: a flat change to melee damage. ---------------
    STRONG_1("strong1", Effect.DAMAGE, 0.5f, 10, false),
    STRONG_2("strong2", Effect.DAMAGE, 1.0f, 5, false),
    STRONG_3("strong3", Effect.DAMAGE, 2.0f, 2, true),
    STRONG_4("strong4", Effect.DAMAGE, 3.0f, 1, true),
    WEAK_1("weak1", Effect.DAMAGE, -0.5f, 6, false),
    WEAK_2("weak2", Effect.DAMAGE, -1.0f, 4, false),
    WEAK_3("weak3", Effect.DAMAGE, -2.0f, 2, false),

    // --- LOTREnchantmentBane: extra melee damage against one kind of foe. ----
    // Weight 0: never rolled, only put on by applyRandom's special cases.
    BANE_SPIDER("baneSpider", Effect.BANE, 4.0f, 0, false, EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS),
    BANE_WIGHT("baneWight", Effect.BANE, 4.0f, 0, false, EntityTypeTags.SENSITIVE_TO_SMITE),

    // --- LOTREnchantmentDurability: the chance a use costs no durability. ----
    DURABLE_1("durable1", Effect.DURABILITY, 1.25f, 15, false),
    DURABLE_2("durable2", Effect.DURABILITY, 1.5f, 8, false),
    DURABLE_3("durable3", Effect.DURABILITY, 2.0f, 4, true),

    // --- LOTREnchantmentMeleeSpeed and MeleeReach: multipliers. --------------
    MELEE_SPEED_1("meleeSpeed1", Effect.MELEE_SPEED, 1.25f, 6, false),
    MELEE_SLOW_1("meleeSlow1", Effect.MELEE_SPEED, 0.75f, 4, false),
    MELEE_REACH_1("meleeReach1", Effect.MELEE_REACH, 1.25f, 6, false),
    MELEE_UNREACH_1("meleeUnreach1", Effect.MELEE_REACH, 0.75f, 4, false),

    // --- LOTREnchantmentKnockback: whole knockback levels. -------------------
    KNOCKBACK_1("knockback1", Effect.KNOCKBACK, 1.0f, 6, false),
    KNOCKBACK_2("knockback2", Effect.KNOCKBACK, 2.0f, 2, true),

    // --- LOTREnchantmentToolSpeed: a multiplier on digging speed. ------------
    TOOL_SPEED_1("toolSpeed1", Effect.TOOL_SPEED, 1.5f, 20, false),
    TOOL_SPEED_2("toolSpeed2", Effect.TOOL_SPEED, 2.0f, 10, false),
    TOOL_SPEED_3("toolSpeed3", Effect.TOOL_SPEED, 3.0f, 5, true),
    TOOL_SPEED_4("toolSpeed4", Effect.TOOL_SPEED, 4.0f, 2, true),
    TOOL_SLOW_1("toolSlow1", Effect.TOOL_SPEED, 0.75f, 10, false),

    // --- LOTREnchantmentSilkTouch and LOTREnchantmentLooting. ----------------
    TOOL_SILK("toolSilk", Effect.SILK_TOUCH, 1.0f, 10, true),
    LOOTING_1("looting1", Effect.LOOTING, 1.0f, 6, false),
    LOOTING_2("looting2", Effect.LOOTING, 2.0f, 2, true),
    LOOTING_3("looting3", Effect.LOOTING, 3.0f, 1, true),

    // --- LOTREnchantmentProtection: flat armour points. ----------------------
    PROTECT_1("protect1", Effect.PROTECTION, 1.0f, 10, false),
    PROTECT_2("protect2", Effect.PROTECTION, 2.0f, 3, true),
    PROTECT_WEAK_1("protectWeak1", Effect.PROTECTION, -1.0f, 5, false),
    PROTECT_WEAK_2("protectWeak2", Effect.PROTECTION, -2.0f, 2, false),

    // --- LOTREnchantmentProtectionSpecial: protection against one harm. ------
    PROTECT_FIRE_1("protectFire1", Effect.PROTECTION_FIRE, 1.0f, 5, false),
    PROTECT_FIRE_2("protectFire2", Effect.PROTECTION_FIRE, 2.0f, 2, true),
    PROTECT_FIRE_3("protectFire3", Effect.PROTECTION_FIRE, 3.0f, 1, true),
    PROTECT_FALL_1("protectFall1", Effect.PROTECTION_FALL, 1.0f, 5, false),
    PROTECT_FALL_2("protectFall2", Effect.PROTECTION_FALL, 2.0f, 2, true),
    PROTECT_FALL_3("protectFall3", Effect.PROTECTION_FALL, 3.0f, 1, true),
    PROTECT_RANGED_1("protectRanged1", Effect.PROTECTION_RANGED, 1.0f, 5, false),
    PROTECT_RANGED_2("protectRanged2", Effect.PROTECTION_RANGED, 2.0f, 2, true),
    PROTECT_RANGED_3("protectRanged3", Effect.PROTECTION_RANGED, 3.0f, 1, true),

    // --- LOTREnchantmentRangedDamage and RangedKnockback: launchers. ---------
    RANGED_STRONG_1("rangedStrong1", Effect.RANGED_DAMAGE, 1.1f, 10, false),
    RANGED_STRONG_2("rangedStrong2", Effect.RANGED_DAMAGE, 1.2f, 3, false),
    RANGED_STRONG_3("rangedStrong3", Effect.RANGED_DAMAGE, 1.3f, 1, true),
    RANGED_WEAK_1("rangedWeak1", Effect.RANGED_DAMAGE, 0.75f, 8, false),
    RANGED_WEAK_2("rangedWeak2", Effect.RANGED_DAMAGE, 0.5f, 3, false),
    RANGED_KNOCKBACK_1("rangedKnockback1", Effect.RANGED_KNOCKBACK, 1.0f, 6, false),
    RANGED_KNOCKBACK_2("rangedKnockback2", Effect.RANGED_KNOCKBACK, 2.0f, 2, true);

    /**
     * LOTREnchantmentType, less the three per-slot armour types nothing in the
     * random table names, and FISHING and RANGED, which only the absent weapon
     * specials use.
     */
    public enum Kind {
        /** item.isDamageable(). */
        BREAKABLE,
        /** An ItemArmor worn on the head, body, legs or feet that gives protection. */
        ARMOR,
        /** The same, worn on the feet. */
        ARMOR_FEET,
        /** LOTRWeaponStats.isMeleeWeapon: carries the weapon damage modifier. */
        MELEE,
        /** A pickaxe, axe, shovel or mattock -- getToolClasses was not empty. */
        TOOL,
        /** ItemShears. */
        SHEARS,
        /** ItemBow, which LOTRItemCrossbow extended too, or LOTRItemBlowgun. */
        RANGED_LAUNCHER,
        /** LOTRItemThrowingAxe. */
        THROWING_AXE
    }

    /**
     * Which original class a modifier belongs to. isCompatibleWith refused a
     * second modifier of the same class, so this is also what stops a blade
     * being both Keen and Dull.
     */
    public enum Effect {
        DAMAGE("lotr.enchant.damage.desc", Format.ADDITIVE, EnumSet.of(Kind.MELEE, Kind.THROWING_AXE)),
        BANE(null, Format.ADDITIVE, EnumSet.of(Kind.MELEE, Kind.THROWING_AXE)),
        DURABILITY("lotr.enchant.durable.desc", Format.MULTIPLICATIVE, EnumSet.of(Kind.BREAKABLE)),
        MELEE_SPEED("lotr.enchant.meleeSpeed.desc", Format.MULTIPLICATIVE, EnumSet.of(Kind.MELEE)),
        MELEE_REACH("lotr.enchant.meleeReach.desc", Format.MULTIPLICATIVE, EnumSet.of(Kind.MELEE)),
        KNOCKBACK("lotr.enchant.knockback.desc", Format.ADDITIVE_INT, EnumSet.of(Kind.MELEE, Kind.THROWING_AXE)),
        TOOL_SPEED("lotr.enchant.toolSpeed.desc", Format.MULTIPLICATIVE, EnumSet.of(Kind.TOOL, Kind.SHEARS)),
        SILK_TOUCH("lotr.enchant.toolSilk.desc", Format.NONE, EnumSet.of(Kind.TOOL)),
        LOOTING("lotr.enchant.looting.desc", Format.ADDITIVE_INT, EnumSet.of(Kind.TOOL, Kind.MELEE)),
        PROTECTION("lotr.enchant.protect.desc", Format.ADDITIVE_INT, EnumSet.of(Kind.ARMOR)),
        PROTECTION_FIRE("lotr.enchant.protectFire.desc", Format.SPECIAL_PROTECTION, EnumSet.of(Kind.ARMOR)),
        PROTECTION_FALL("lotr.enchant.protectFall.desc", Format.SPECIAL_PROTECTION, EnumSet.of(Kind.ARMOR_FEET)),
        PROTECTION_RANGED("lotr.enchant.protectRanged.desc", Format.SPECIAL_PROTECTION, EnumSet.of(Kind.ARMOR)),
        RANGED_DAMAGE("lotr.enchant.rangedDamage.desc", Format.MULTIPLICATIVE, EnumSet.of(Kind.RANGED_LAUNCHER)),
        RANGED_KNOCKBACK("lotr.enchant.rangedKnockback.desc", Format.ADDITIVE_INT, EnumSet.of(Kind.RANGED_LAUNCHER));

        private final String descriptionKey;
        private final Format format;
        private final Set<Kind> kinds;

        Effect(String descriptionKey, Format format, Set<Kind> kinds) {
            this.descriptionKey = descriptionKey;
            this.format = format;
            this.kinds = kinds;
        }

        /** LOTREnchantment.itemTypes: it goes on a stack matching any of these. */
        public Set<Kind> kinds() {
            return this.kinds;
        }

        public boolean isSpecialProtection() {
            return this == PROTECTION_FIRE || this == PROTECTION_FALL || this == PROTECTION_RANGED;
        }
    }

    /** formatAdditive, formatAdditiveInt and formatMultiplicative. */
    public enum Format {
        ADDITIVE, ADDITIVE_INT, MULTIPLICATIVE, SPECIAL_PROTECTION, NONE
    }

    private final String name;
    private final Effect effect;
    private final float value;
    private final int weight;
    private final boolean skilful;
    private final TagKey<EntityType<?>> baneOf;

    LOTRModifier(String name, Effect effect, float value, int weight, boolean skilful) {
        this(name, effect, value, weight, skilful, null);
    }

    LOTRModifier(String name, Effect effect, float value, int weight, boolean skilful,
            TagKey<EntityType<?>> baneOf) {
        this.name = name;
        this.effect = effect;
        this.value = value;
        this.weight = weight;
        this.skilful = skilful;
        this.baneOf = baneOf;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public Effect effect() {
        return this.effect;
    }

    public Set<Kind> kinds() {
        return this.effect.kinds();
    }

    /**
     * The modifier's figure: the damage boost, the multiplier, the knockback,
     * the protection level. A bane's is its extra damage.
     */
    public float value() {
        return this.value;
    }

    /** getEnchantWeight: how often it comes up. Zero means never at random. */
    public int weight() {
        return this.weight;
    }

    /** isSkilful: only reachable from a skilled smith, never from ordinary loot. */
    public boolean isSkilful() {
        return this.skilful;
    }

    /** LOTREnchantmentBane: the creatures it bites harder, or null. */
    public TagKey<EntityType<?>> baneOf() {
        return this.baneOf;
    }

    /** lotr.enchant.&lt;name&gt; -- "Keen", "Crooked", "Lasting". */
    public String translationKey() {
        return "lotr.enchant." + this.name;
    }

    /** isBeneficial: a gift or a flaw, which colours the tooltip line. */
    public boolean isBeneficial() {
        return switch (this.effect) {
            case DAMAGE, KNOCKBACK, PROTECTION, RANGED_KNOCKBACK -> this.value >= 0.0f;
            case DURABILITY, MELEE_SPEED, MELEE_REACH, TOOL_SPEED, RANGED_DAMAGE -> this.value >= 1.0f;
            case BANE, SILK_TOUCH, LOOTING, PROTECTION_FIRE, PROTECTION_FALL, PROTECTION_RANGED -> true;
        };
    }

    /**
     * isCompatibleWith, both ways round: never two of one class; the special
     * protections only go together when one of them is Fall protection
     * (isCompatibleWithOtherProtection); and Silken and looting refuse each
     * other.
     */
    public boolean isCompatibleWith(LOTRModifier other) {
        if (this.effect == other.effect) {
            return false;
        }
        if (this.effect.isSpecialProtection() && other.effect.isSpecialProtection()) {
            return this.effect == Effect.PROTECTION_FALL || other.effect == Effect.PROTECTION_FALL;
        }
        return !(this.effect == Effect.SILK_TOUCH && other.effect == Effect.LOOTING
                || this.effect == Effect.LOOTING && other.effect == Effect.SILK_TOUCH);
    }

    /**
     * LOTREnchantmentProtectionSpecial.calcIntProtection: the protection points
     * a special protection adds against its harm. Fire is 1 + level, falling is
     * 3 + level(level + 1)/2, projectiles the level alone.
     */
    public int specialProtection() {
        int level = (int) this.value;
        return switch (this.effect) {
            case PROTECTION_FIRE -> 1 + level;
            case PROTECTION_FALL -> 3 + level * (level + 1) / 2;
            case PROTECTION_RANGED -> level;
            default -> 0;
        };
    }

    /**
     * getDescription: what the modifier does, "+0.5 melee damage". A throwing
     * axe's damage line says "thrown damage" instead, a bane names its foe, and
     * Silken has no figure at all.
     */
    public Component description(boolean throwingAxe) {
        if (this.effect == Effect.BANE) {
            return Component.translatable(translationKey() + ".desc", formattedValue());
        }
        if (this.effect.format == Format.NONE) {
            return Component.translatable(this.effect.descriptionKey);
        }
        String key = this.effect == Effect.DAMAGE && throwingAxe
                ? this.effect.descriptionKey + ".throw" : this.effect.descriptionKey;
        return Component.translatable(key, formattedValue());
    }

    /**
     * The figure as the original wrote it. formatDecimalNumber kept at least
     * one decimal place, so it is "+2.0 melee damage" and "x1.5 tool speed";
     * the whole-number formats have none; and a special protection shows its
     * protection points rather than its level.
     */
    public String formattedValue() {
        return switch (this.effect.format) {
            case ADDITIVE -> (this.value >= 0.0f ? "+" : "") + decimal(this.value);
            case ADDITIVE_INT -> (this.value >= 0.0f ? "+" : "") + (int) this.value;
            case SPECIAL_PROTECTION -> "+" + specialProtection();
            case MULTIPLICATIVE -> "x" + decimal(this.value);
            case NONE -> "";
        };
    }

    private static String decimal(float value) {
        DecimalFormat format = new DecimalFormat("#,##0.###", DecimalFormatSymbols.getInstance(Locale.ROOT));
        format.setMinimumFractionDigits(1);
        return format.format(value);
    }

    public static LOTRModifier byName(String name) {
        for (LOTRModifier modifier : values()) {
            if (modifier.name.equals(name)) {
                return modifier;
            }
        }
        return null;
    }
}
