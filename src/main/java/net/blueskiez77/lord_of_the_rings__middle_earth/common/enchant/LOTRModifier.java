package net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.StringRepresentable;

/**
 * LOTREnchantment: the mod's own modifiers, the ones a weapon or a piece of
 * armour turns up already wearing.
 *
 * <p>Nothing to do with vanilla enchanting. Each is a small permanent change to
 * an item's numbers, given out at random when the item is made, and named
 * rather than levelled -- a "Keen" blade, a "Crooked" one, "Lasting" armour.
 *
 * <p>ONLY the melee, armour and throwing-axe entries are here, because those
 * are the item kinds the port has. The original's list also covers bows and
 * crossbows (rangedStrong, rangedKnockback), tools (toolSpeed, toolSilk),
 * fishing rods, and the bane family that keys off NPC classes -- all of which
 * want items or entities the port does not have yet. Weights, values and names
 * below are the original's exactly, so the rest slot in beside them later.
 *
 * <p>Two melee entries are deliberately absent for now. looting1-3 has no
 * attribute to hang off in 26.2, and the bane family needs the NPC types it
 * tests against; both are noted here rather than silently dropped.
 */
public enum LOTRModifier implements StringRepresentable {

    // --- Damage, LOTREnchantmentDamage: a flat change to attack damage. -----
    //
    // Every one of these is {MELEE, THROWING_AXE} in the original, and so is
    // the knockback pair below -- LOTREnchantmentDamage and
    // LOTREnchantmentKnockback are the only two families that name a second
    // type. See the note on Kind.THROWING_AXE for what carrying one actually
    // does to a thrown axe.
    STRONG_1("strong1", Kind.MELEE, Kind.THROWING_AXE, 10, false, Effect.DAMAGE, 0.5f),
    STRONG_2("strong2", Kind.MELEE, Kind.THROWING_AXE, 5, false, Effect.DAMAGE, 1.0f),
    STRONG_3("strong3", Kind.MELEE, Kind.THROWING_AXE, 2, true, Effect.DAMAGE, 2.0f),
    STRONG_4("strong4", Kind.MELEE, Kind.THROWING_AXE, 1, true, Effect.DAMAGE, 3.0f),
    WEAK_1("weak1", Kind.MELEE, Kind.THROWING_AXE, 6, false, Effect.DAMAGE, -0.5f),
    WEAK_2("weak2", Kind.MELEE, Kind.THROWING_AXE, 4, false, Effect.DAMAGE, -1.0f),
    WEAK_3("weak3", Kind.MELEE, Kind.THROWING_AXE, 2, false, Effect.DAMAGE, -2.0f),

    // --- Durability, LOTREnchantmentDurability: a multiplier on max damage. --
    DURABLE_1("durable1", Kind.BREAKABLE, 15, false, Effect.DURABILITY, 1.25f),
    DURABLE_2("durable2", Kind.BREAKABLE, 8, false, Effect.DURABILITY, 1.5f),
    DURABLE_3("durable3", Kind.BREAKABLE, 4, true, Effect.DURABILITY, 2.0f),

    // --- Melee speed and reach: multipliers on the item's own figures. ------
    MELEE_SPEED_1("meleeSpeed1", Kind.MELEE, 6, false, Effect.ATTACK_SPEED, 1.25f),
    MELEE_SLOW_1("meleeSlow1", Kind.MELEE, 4, false, Effect.ATTACK_SPEED, 0.75f),
    MELEE_REACH_1("meleeReach1", Kind.MELEE, 6, false, Effect.REACH, 1.25f),
    MELEE_UNREACH_1("meleeUnreach1", Kind.MELEE, 4, false, Effect.REACH, 0.75f),

    // --- Knockback, in whole vanilla knockback levels. ----------------------
    KNOCKBACK_1("knockback1", Kind.MELEE, Kind.THROWING_AXE, 6, false, Effect.KNOCKBACK, 1.0f),
    KNOCKBACK_2("knockback2", Kind.MELEE, Kind.THROWING_AXE, 2, true, Effect.KNOCKBACK, 2.0f),

    // --- Protection, LOTREnchantmentProtection: flat armour points. ---------
    PROTECT_1("protect1", Kind.ARMOR, 10, false, Effect.ARMOR, 1.0f),
    PROTECT_2("protect2", Kind.ARMOR, 3, true, Effect.ARMOR, 2.0f),
    PROTECT_WEAK_1("protectWeak1", Kind.ARMOR, 5, false, Effect.ARMOR, -1.0f),
    PROTECT_WEAK_2("protectWeak2", Kind.ARMOR, 2, false, Effect.ARMOR, -2.0f);

    /** LOTREnchantmentType, cut down to the four the port's items can be. */
    public enum Kind {
        /** MELEE: anything you swing. Also BREAKABLE, since every weapon here is. */
        MELEE,
        /** ARMOR: any of the four worn pieces. */
        ARMOR,
        /**
         * THROWING_AXE: an axe meant to leave your hand.
         *
         * <p>It takes the damage and knockback families, exactly as the
         * original allows. Only the knockback half changes the throw, though:
         * LOTRItemThrowingAxe.getRangedDamageMultiplier works the damage out
         * from the material alone and never asks the modifiers, so a Keen
         * throwing axe carries the name and the tooltip line and hits for the
         * same as a plain one. That is the original's behaviour, not an
         * oversight here.
         */
        THROWING_AXE,
        /** BREAKABLE: has durability, which for now means all of the above. */
        BREAKABLE
    }

    /**
     * What the modifier actually changes, and how the original described it.
     *
     * <p>The key is the {@code .desc} line -- "%s melee damage", "%s durability"
     * -- and the format is how the number goes into it: additive with a sign
     * for a flat change, "x1.25" for a multiplier.
     */
    public enum Effect {
        DAMAGE("lotr.enchant.damage.desc", Format.ADDITIVE),
        ATTACK_SPEED("lotr.enchant.meleeSpeed.desc", Format.MULTIPLICATIVE),
        REACH("lotr.enchant.meleeReach.desc", Format.MULTIPLICATIVE),
        KNOCKBACK("lotr.enchant.knockback.desc", Format.ADDITIVE_INT),
        DURABILITY("lotr.enchant.durable.desc", Format.MULTIPLICATIVE),
        ARMOR("lotr.enchant.protect.desc", Format.ADDITIVE_INT);

        private final String descriptionKey;
        private final Format format;

        Effect(String descriptionKey, Format format) {
            this.descriptionKey = descriptionKey;
            this.format = format;
        }

        public String descriptionKey() {
            return this.descriptionKey;
        }

        public Format format() {
            return this.format;
        }
    }

    /** formatAdditive, formatAdditiveInt and formatMultiplicative. */
    public enum Format {
        ADDITIVE, ADDITIVE_INT, MULTIPLICATIVE
    }

    private final String name;

    /**
     * LOTREnchantment.itemTypes: a modifier may name more than one kind of item
     * it goes on, and applies to a stack that matches any of them.
     */
    private final Set<Kind> kinds;

    private final int weight;
    private final boolean skilful;
    private final Effect effect;
    private final float value;

    LOTRModifier(String name, Kind kind, int weight, boolean skilful, Effect effect, float value) {
        this(name, EnumSet.of(kind), weight, skilful, effect, value);
    }

    LOTRModifier(String name, Kind kind, Kind alsoKind, int weight, boolean skilful, Effect effect,
            float value) {
        this(name, EnumSet.of(kind, alsoKind), weight, skilful, effect, value);
    }

    LOTRModifier(String name, Set<Kind> kinds, int weight, boolean skilful, Effect effect,
            float value) {
        this.name = name;
        this.kinds = kinds;
        this.weight = weight;
        this.skilful = skilful;
        this.effect = effect;
        this.value = value;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public Set<Kind> kinds() {
        return this.kinds;
    }

    /** getEnchantWeight: how often it comes up. */
    public int weight() {
        return this.weight;
    }

    /** isSkilful: only reachable from a skilled smith, never from ordinary loot. */
    public boolean isSkilful() {
        return this.skilful;
    }

    public Effect effect() {
        return this.effect;
    }

    public float value() {
        return this.value;
    }

    /** lotr.enchant.&lt;name&gt; -- "Keen", "Crooked", "Lasting". */
    public String translationKey() {
        return "lotr.enchant." + this.name;
    }

    /**
     * isBeneficial: whether the modifier is a gift or a flaw. It decides the
     * colour of the description line -- grey for good, dark grey for bad.
     */
    public boolean isBeneficial() {
        return switch (this.effect) {
            case DAMAGE, KNOCKBACK, ARMOR -> this.value >= 0.0f;
            case ATTACK_SPEED, REACH, DURABILITY -> this.value >= 1.0f;
        };
    }

    /**
     * The number as the original wrote it: formatAdditive puts a sign on a flat
     * change, formatMultiplicative writes "x1.25", and a trailing ".0" is
     * dropped either way.
     */
    public String formattedValue() {
        return switch (this.effect.format()) {
            case ADDITIVE -> (this.value >= 0.0f ? "+" : "") + trim(this.value);
            case ADDITIVE_INT -> (this.value >= 0.0f ? "+" : "") + (int) this.value;
            case MULTIPLICATIVE -> "x" + trim(this.value);
        };
    }

    private static String trim(float value) {
        return value == (int) value ? String.valueOf((int) value) : String.valueOf(value);
    }

    public static LOTRModifier byName(String name) {
        for (LOTRModifier modifier : values()) {
            if (modifier.name.equals(name)) {
                return modifier;
            }
        }
        return null;
    }

    /**
     * canApply: which modifiers a given kind of item can carry. A weapon takes
     * the melee ones and the durability ones; armour takes protection and
     * durability.
     */
    public static List<LOTRModifier> applicableTo(Kind itemKind) {
        List<LOTRModifier> out = new ArrayList<>();
        for (LOTRModifier modifier : values()) {
            // BREAKABLE matches everything the port has, since a weapon, a
            // piece of armour and a throwing axe all wear out.
            if (modifier.kinds.contains(Kind.BREAKABLE) || modifier.kinds.contains(itemKind)) {
                out.add(modifier);
            }
        }
        return out;
    }
}
