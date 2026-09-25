package net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRThrowingAxeItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolMaterials;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmithsScrollItem;

/**
 * LOTREnchantmentHelper: rolling an item's modifiers, and making them count.
 *
 * <p>The roll is the original's line for line. A plain item gets two modifiers
 * 10% of the time, one 55% of the time and none otherwise; a skilful one -- a
 * smith's, or one chest item in five -- gets two 15% of the time and one 65%,
 * can reach the entries marked skilful, and draws the flaws far less often
 * (getSkilfulWeight). Each candidate is drawn by weight, a tool finds the
 * modifiers meant for other things a third as often, and each draw strikes out
 * whatever cannot sit beside it.
 *
 * <p>When: LOTREnchantmentHelper.onEntityUpdate rolled every item a player
 * picked up, crafted or otherwise got into their inventory, and everything a
 * mob was carrying the first time it ticked, whether the mod's or vanilla's --
 * a vanilla iron sword could be Keen as well. {@link #init} does the same.
 *
 * <p>Where they are stored is the port's own decision: the list of names goes
 * in {@code minecraft:custom_data}. What the melee and armour modifiers DO is
 * written onto the stack as attribute modifiers when they are rolled, so the
 * game applies them and the tooltip lists them; the rest are read back by the
 * hooks in the mixin package, one for each place the original's coremod or
 * event handler asked LOTREnchantmentHelper.
 */
public final class LOTRModifiers {

    /** The list of modifier names. */
    private static final String TAG = "LOTREnchants";

    /** LOTRRandomEnch: set once so an item is never rolled twice. */
    private static final String TAG_ROLLED = "LOTREnchantsApplied";

    /** LOTREnchantInit, as an entity tag: this mob's equipment has been rolled. */
    private static final String ENTITY_ROLLED = "lotr_enchant_init";

    /** LOTRWeaponStats.MAX_MODIFIABLE_SPEED, MAX_MODIFIABLE_REACH, MAX_MODIFIABLE_KNOCKBACK. */
    private static final float MAX_MODIFIABLE_SPEED = 1.6f;
    private static final float MAX_MODIFIABLE_REACH = 2.0f;
    private static final int MAX_MODIFIABLE_KNOCKBACK = 2;

    /** Attributes.ATTACK_SPEED is a delta off this... */
    private static final double BASE_ATTACK_SPEED = 4.0;
    /** ...and the port's sword swings at this, where the original's swung at 1. */
    private static final double SWORD_ATTACK_SPEED = 1.6;
    /** Attributes.ENTITY_INTERACTION_RANGE is a delta off this. */
    private static final double BASE_INTERACTION_RANGE = 3.0;

    private LOTRModifiers() {
    }

    /** Hooks the roll up to players' inventories and to mobs' equipment. */
    /** LOTREnchantProgress, as the original named it: per bane, Kills and KillsRequired. */
    private static final String TAG_PROGRESS = "LOTREnchantProgress";

    public static void init() {
        net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (source.getEntity() instanceof net.minecraft.server.level.ServerPlayer player
                    && source.getDirectEntity() == player) {
                onKill(player, entity);
            }
        });
        // handlePlayerInventoryChanges: anything new in a player's inventory,
        // or on the cursor, is rolled.
        ServerTickEvents.END_LEVEL_TICK.register(level -> {
            for (ServerPlayer player : level.players()) {
                RandomSource random = player.getRandom();
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    tryApplyRandom(inventory.getItem(i), random);
                }
                tryApplyRandom(player.containerMenu.getCarried(), random);
            }
        });
        // tryApplyRandomEnchantsForEquipment: a mob's gear, once.
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof Mob mob && !mob.entityTags().contains(ENTITY_ROLLED)) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    tryApplyRandom(mob.getItemBySlot(slot), mob.getRandom());
                }
                mob.addTag(ENTITY_ROLLED);
            }
        });
    }

    // ------------------------------------------------------------------------
    // Reading
    // ------------------------------------------------------------------------

    /** getEnchantList: what this stack is wearing. */
    public static List<LOTRModifier> get(DataComponentGetter stack) {
        List<LOTRModifier> out = new ArrayList<>();
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return out;
        }
        data.copyTag().getList(TAG).ifPresent(list -> {
            for (int i = 0; i < list.size(); i++) {
                list.getString(i).map(LOTRModifier::byName).ifPresent(out::add);
            }
        });
        return out;
    }

    public static boolean has(DataComponentGetter stack, LOTRModifier modifier) {
        return get(stack).contains(modifier);
    }

    public static boolean hasBeenRolled(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBooleanOr(TAG_ROLLED, false);
    }

    /** calcExtraKnockback: the melee knockback levels, which a thrown axe also carries. */
    public static int knockback(ItemStack stack) {
        int total = 0;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.effect() == LOTRModifier.Effect.KNOCKBACK) {
                total += (int) modifier.value();
            }
        }
        return total;
    }

    /** calcRangedKnockback. */
    public static int rangedKnockback(ItemStack stack) {
        int total = 0;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.effect() == LOTRModifier.Effect.RANGED_KNOCKBACK) {
                total += (int) modifier.value();
            }
        }
        return total;
    }

    /** calcRangedDamageFactor: the launcher's launch speed multiplier. */
    public static float rangedDamageFactor(ItemStack stack) {
        return product(stack, LOTRModifier.Effect.RANGED_DAMAGE);
    }

    /** calcToolEfficiency. */
    public static float toolSpeedFactor(ItemStack stack) {
        return product(stack, LOTRModifier.Effect.TOOL_SPEED);
    }

    /** calcLootingLevel: added to both looting and fortune. */
    public static int lootLevel(DataComponentGetter stack) {
        int total = 0;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.effect() == LOTRModifier.Effect.LOOTING) {
                total += (int) modifier.value();
            }
        }
        return total;
    }

    /** isSilkTouch. */
    public static boolean isSilkTouch(DataComponentGetter stack) {
        return has(stack, LOTRModifier.TOOL_SILK);
    }

    /**
     * calcEntitySpecificDamage: a bane's extra damage, if the target is its foe.
     */
    public static float baneDamage(ItemStack stack, Entity target) {
        float total = 0.0f;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.baneOf() != null && target.is(modifier.baneOf())) {
                total += modifier.value();
            }
        }
        return total;
    }

    /**
     * calcSpecialArmorSetProtection: the fire, fall and projectile protection
     * points across everything worn. Damage that bypasses invulnerability --
     * the void, /kill -- goes through regardless, as canHarmInCreative did.
     */
    public static int specialProtection(LivingEntity entity, DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        }
        int total = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (LOTRModifier modifier : get(entity.getItemBySlot(slot))) {
                if (protectsAgainst(modifier.effect(), source)) {
                    total += modifier.specialProtection();
                }
            }
        }
        return total;
    }

    private static boolean protectsAgainst(LOTRModifier.Effect effect, DamageSource source) {
        return switch (effect) {
            case PROTECTION_FIRE -> source.is(DamageTypeTags.IS_FIRE);
            case PROTECTION_FALL -> source.is(DamageTypes.FALL);
            case PROTECTION_RANGED -> source.is(DamageTypeTags.IS_PROJECTILE);
            // LOTREnchantmentProtectionMithril.protectsAgainst: a melee blow from a
            // long weapon -- base reach, modifiers aside, of at least 1.3.
            case PROTECTION_MITHRIL -> source.getEntity() instanceof LivingEntity attacker
                    && attacker == source.getDirectEntity()
                    && (BASE_INTERACTION_RANGE + sum(attacker.getMainHandItem(), Attributes.ENTITY_INTERACTION_RANGE))
                            / BASE_INTERACTION_RANGE >= 1.3;
            default -> false;
        };
    }

    /** getMaxFireProtectionLevel: the best fire protection level worn. */
    public static int maxFireProtectionLevel(LivingEntity entity) {
        int max = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (LOTRModifier modifier : get(entity.getItemBySlot(slot))) {
                if (modifier.effect() == LOTRModifier.Effect.PROTECTION_FIRE) {
                    max = Math.max(max, (int) modifier.value());
                }
            }
        }
        return max;
    }

    /**
     * negateDamage, once per point of wear: how many of {@code amount} a durable
     * item shrugs off. Each point is spared with a chance of 1 - 1/factor, so a
     * Lasting item (x1.5) wears at two thirds the rate -- the durability bar
     * itself never changes.
     */
    public static int negatedDamage(ItemStack stack, RandomSource random, int amount) {
        int negated = 0;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.effect() != LOTRModifier.Effect.DURABILITY || modifier.value() <= 1.0f) {
                continue;
            }
            float keep = 1.0f / modifier.value();
            for (int i = 0; i < amount; i++) {
                if (random.nextFloat() > keep) {
                    negated++;
                }
            }
        }
        return negated;
    }

    private static float product(ItemStack stack, LOTRModifier.Effect effect) {
        float factor = 1.0f;
        for (LOTRModifier modifier : get(stack)) {
            if (modifier.effect() == effect) {
                factor *= modifier.value();
            }
        }
        return factor;
    }

    // ------------------------------------------------------------------------
    // What an item is
    // ------------------------------------------------------------------------

    /** Every LOTREnchantmentType this stack satisfies. */
    public static Set<LOTRModifier.Kind> kindsOf(ItemStack stack) {
        Set<LOTRModifier.Kind> kinds = EnumSet.noneOf(LOTRModifier.Kind.class);
        if (stack.isEmpty()) {
            return kinds;
        }
        Item item = stack.getItem();
        if (stack.isDamageableItem()) {
            kinds.add(LOTRModifier.Kind.BREAKABLE);
        }
        ArmorType armor = armorType(stack);
        if (armor != null && armorPoints(stack) > 0) {
            kinds.add(LOTRModifier.Kind.ARMOR);
            if (armor == ArmorType.BOOTS) {
                kinds.add(LOTRModifier.Kind.ARMOR_FEET);
            }
        }
        // A 1.7.10 hoe carried no weapon damage, and the command sword was
        // refused by name.
        if (baseAttackDamage(stack).isPresent() && !(item instanceof HoeItem) && item != LOTRCombatItems.COMMAND_SWORD) {
            kinds.add(LOTRModifier.Kind.MELEE);
        }
        if (isTool(stack)) {
            kinds.add(LOTRModifier.Kind.TOOL);
        }
        if (item instanceof ShearsItem) {
            kinds.add(LOTRModifier.Kind.SHEARS);
        }
        if (item instanceof BowItem || item instanceof CrossbowItem) {
            kinds.add(LOTRModifier.Kind.RANGED_LAUNCHER);
        }
        if (item instanceof LOTRThrowingAxeItem) {
            kinds.add(LOTRModifier.Kind.THROWING_AXE);
        }
        return kinds;
    }

    /**
     * getToolClasses was not empty: a pickaxe, an axe, a shovel, or the mattock.
     * A battleaxe or a warhammer mines like an axe here but was a sword in the
     * original, so only a real AxeItem counts as an axe.
     */
    private static boolean isTool(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof AxeItem || item instanceof ShovelItem) {
            return true;
        }
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null) {
            return false;
        }
        for (Tool.Rule rule : tool.rules()) {
            Optional<TagKey<Block>> tag = rule.blocks().unwrapKey();
            if (tag.isPresent() && (tag.get().equals(BlockTags.MINEABLE_WITH_PICKAXE)
                    || tag.get().equals(LOTRBlockTags.MATTOCK_MINEABLE))) {
                return true;
            }
        }
        return false;
    }

    private static ItemAttributeModifiers defaultAttributes(ItemStack stack) {
        return stack.getItem().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.EMPTY);
    }

    /** The weapon damage modifier LOTRWeaponStats.isMeleeWeapon looked for. */
    private static Optional<Double> baseAttackDamage(ItemStack stack) {
        for (ItemAttributeModifiers.Entry entry : defaultAttributes(stack).modifiers()) {
            if (entry.attribute().equals(Attributes.ATTACK_DAMAGE)
                    && entry.modifier().id().equals(Item.BASE_ATTACK_DAMAGE_ID)) {
                return Optional.of(entry.modifier().amount());
            }
        }
        return Optional.empty();
    }

    private static double sum(ItemStack stack, Holder<Attribute> attribute) {
        double total = 0.0;
        for (ItemAttributeModifiers.Entry entry : defaultAttributes(stack).modifiers()) {
            if (entry.attribute().equals(attribute)
                    && entry.modifier().operation() == AttributeModifier.Operation.ADD_VALUE) {
                total += entry.modifier().amount();
            }
        }
        return total;
    }

    /**
     * getMeleeDamageBonus: the weapon damage modifier. The port's modifier reads
     * one less than the original's figure -- the tooltip adds the fist's 1 back
     * -- so the 1 is added here too; a vanilla sword works out the same.
     */
    private static float meleeDamageBonus(ItemStack stack) {
        return baseAttackDamage(stack).map(amount -> (float) (amount + 1.0)).orElse(0.0f);
    }

    /** getMeleeSpeed, in the original's terms: a sword is 1, a dagger 1.5. */
    private static float meleeSpeed(ItemStack stack) {
        return (float) ((BASE_ATTACK_SPEED + sum(stack, Attributes.ATTACK_SPEED)) / SWORD_ATTACK_SPEED);
    }

    /** getMeleeReachFactor: a sword is 1, a long polearm 2. */
    private static float meleeReachFactor(ItemStack stack) {
        return (float) ((BASE_INTERACTION_RANGE + sum(stack, Attributes.ENTITY_INTERACTION_RANGE))
                / BASE_INTERACTION_RANGE);
    }

    /** getBaseExtraKnockback: a hammer's or a lance's one level. */
    private static int baseKnockback(ItemStack stack) {
        return (int) Math.round(sum(stack, Attributes.ATTACK_KNOCKBACK));
    }

    private static ArmorType armorType(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) {
            return null;
        }
        return switch (equippable.slot()) {
            case HEAD -> ArmorType.HELMET;
            case CHEST -> ArmorType.CHESTPLATE;
            case LEGS -> ArmorType.LEGGINGS;
            case FEET -> ArmorType.BOOTS;
            default -> null;
        };
    }

    /** ItemArmor.damageReduceAmount. */
    private static int armorPoints(ItemStack stack) {
        return (int) Math.round(sum(stack, Attributes.ARMOR));
    }

    private static boolean isMaterial(ItemStack stack, ArmorMaterial material) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.assetId().filter(material.assetId()::equals).isPresent();
    }

    // ------------------------------------------------------------------------
    // canApply
    // ------------------------------------------------------------------------

    /**
     * LOTREnchantment.canApply. "Considering" is the looser test used while
     * choosing candidates; the strict one is checked again before a chosen
     * modifier is actually put on, and only protection tells them apart -- a
     * piece may not end up better than mithril.
     */
    public static boolean canApply(LOTRModifier modifier, ItemStack stack, boolean considering) {
        return canApply(modifier, stack, kindsOf(stack), considering);
    }

    private static boolean canApply(LOTRModifier modifier, ItemStack stack, Set<LOTRModifier.Kind> kinds,
            boolean considering) {
        boolean typeMatches = false;
        for (LOTRModifier.Kind kind : modifier.kinds()) {
            if (kinds.contains(kind)) {
                typeMatches = true;
                break;
            }
        }
        if (!typeMatches) {
            return false;
        }
        return switch (modifier.effect()) {
            case DAMAGE -> meleeDamageBonus(stack) + modifier.value() > 0.0f;
            case BANE -> meleeDamageBonus(stack) > 0.0f;
            case MELEE_SPEED -> meleeSpeed(stack) * modifier.value() <= MAX_MODIFIABLE_SPEED;
            case MELEE_REACH -> meleeReachFactor(stack) * modifier.value() <= MAX_MODIFIABLE_REACH;
            case KNOCKBACK -> baseKnockback(stack) + (int) modifier.value() <= MAX_MODIFIABLE_KNOCKBACK;
            case PROTECTION -> canApplyProtection(modifier, stack, considering);
            case PROTECTION_RANGED -> !isMaterial(stack, LOTRToolMaterials.GALVORN_ARMOR);
            // LOTREnchantmentProtectionMithril: true-silver only on mithril.
            case PROTECTION_MITHRIL -> isMaterial(stack, LOTRToolMaterials.MITHRIL_ARMOR);
            // The whip is its own fire; Infernal and Chilling will not go on it.
            case WEAPON_SPECIAL -> modifier == LOTRModifier.HEADHUNTING
                    || !stack.is(LOTRCombatItems.BALROG_WHIP);
            default -> true;
        };
    }

    private static boolean canApplyProtection(LOTRModifier modifier, ItemStack stack, boolean considering) {
        ArmorType type = armorType(stack);
        if (type == null) {
            return true;
        }
        if (isMaterial(stack, LOTRToolMaterials.GALVORN_ARMOR)) {
            return false;
        }
        int total = armorPoints(stack) + (int) modifier.value();
        if (total <= 0) {
            return false;
        }
        return considering || total <= LOTRToolMaterials.MITHRIL_ARMOR.defense().getOrDefault(type, 0);
    }

    /** canApplyAnyEnchant. */
    public static boolean canApplyAny(ItemStack stack) {
        Set<LOTRModifier.Kind> kinds = kindsOf(stack);
        if (kinds.isEmpty()) {
            return false;
        }
        for (LOTRModifier modifier : LOTRModifier.values()) {
            if (canApply(modifier, stack, kinds, true)) {
                return true;
            }
        }
        return false;
    }

    /**
     * onKillEntity: every kill of a bane's foe with a weapon that could take it
     * counts. When the count reaches its target -- 100 to 250, drawn at the
     * first kill -- the weapon has earned the bane, and the whole server hears
     * of it. The Utumno and hired-unit exclusions wait on those.
     */
    private static void onKill(net.minecraft.server.level.ServerPlayer player, LivingEntity target) {
        ItemStack weapon = player.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }
        for (LOTRModifier bane : LOTRModifier.values()) {
            if (bane.effect() != LOTRModifier.Effect.BANE || bane.baneOf() == null
                    || !target.is(bane.baneOf()) || !canApply(bane, weapon, false)) {
                continue;
            }
            int[] earned = {0};
            CustomData data = weapon.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            weapon.set(DataComponents.CUSTOM_DATA, data.update(tag -> {
                net.minecraft.nbt.CompoundTag all = tag.getCompoundOrEmpty(TAG_PROGRESS);
                net.minecraft.nbt.CompoundTag progress = all.getCompoundOrEmpty(bane.getSerializedName());
                int kills = progress.getIntOr("Kills", 0) + 1;
                int required = progress.getIntOr("KillsRequired", 0);
                if (required <= 0) {
                    required = net.minecraft.util.Mth.randomBetweenInclusive(player.getRandom(), 100, 250);
                }
                progress.putInt("Kills", kills);
                progress.putInt("KillsRequired", required);
                all.put(bane.getSerializedName(), progress);
                tag.put(TAG_PROGRESS, all);
                earned[0] = kills >= required ? 1 : 0;
            }));
            List<LOTRModifier> current = new ArrayList<>(get(weapon));
            if (earned[0] == 0 || current.contains(bane)
                    || current.stream().anyMatch(m -> !m.isCompatibleWith(bane))) {
                continue;
            }
            current.add(bane);
            set(weapon, current);
            player.sendSystemMessage(Component.translatable(bane.translationKey() + ".earn",
                    weapon.getHoverName()).withStyle(ChatFormatting.YELLOW));
            for (net.minecraft.server.level.ServerPlayer other : player.level().getServer().getPlayerList().getPlayers()) {
                if (other != player) {
                    other.sendSystemMessage(Component.translatable(bane.translationKey() + ".earnName",
                            player.getName(), weapon.getHoverName()).withStyle(ChatFormatting.YELLOW));
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Rolling
    // ------------------------------------------------------------------------

    /** tryApplyRandomEnchantsForEntity: roll a stack that has never been rolled. */
    public static boolean tryApplyRandom(ItemStack stack, RandomSource random) {
        if (!stack.isEmpty() && !hasBeenRolled(stack) && canApplyAny(stack)) {
            applyRandom(stack, random, false);
            return true;
        }
        return false;
    }

    /** applyRandomEnchantments without keepBanes: a fresh roll. */
    public static void applyRandom(ItemStack stack, RandomSource random, boolean skilful) {
        applyRandom(stack, random, skilful, false);
    }

    /**
     * applyRandomEnchantments. keepBanes is the anvil's reforge: whatever
     * persistsReforge -- the banes -- stays, and only the rest is re-rolled.
     */
    public static void applyRandom(ItemStack stack, RandomSource random, boolean skilful, boolean keepBanes) {
        List<LOTRModifier> chosen = new ArrayList<>();
        if (keepBanes) {
            for (LOTRModifier kept : get(stack)) {
                if (kept.persistsReforge()) {
                    chosen.add(kept);
                }
            }
        }

        // The barrow blades are Wightbane and Sting is Spiderbane from the start.
        Item item = stack.getItem();
        if ((item == LOTRCombatItems.BARROW_BLADE || item == LOTRCombatItems.POISONED_BARROW_BLADE)
                && !chosen.contains(LOTRModifier.BANE_WIGHT)
                && canApply(LOTRModifier.BANE_WIGHT, stack, false)) {
            chosen.add(LOTRModifier.BANE_WIGHT);
        }
        if (item == LOTRStoryItems.STING && !chosen.contains(LOTRModifier.BANE_SPIDER)
                && canApply(LOTRModifier.BANE_SPIDER, stack, false)) {
            chosen.add(LOTRModifier.BANE_SPIDER);
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

        Set<LOTRModifier.Kind> kinds = kindsOf(stack);
        boolean tool = kinds.contains(LOTRModifier.Kind.TOOL);
        List<LOTRModifier> candidates = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        for (LOTRModifier modifier : LOTRModifier.values()) {
            if (modifier.weight() <= 0 || modifier.isSkilful() && !skilful
                    || !canApply(modifier, stack, kinds, true)) {
                continue;
            }
            int weight = skilful ? skilfulWeight(modifier) : modifier.weight() * 100;
            if (tool && !modifier.kinds().contains(LOTRModifier.Kind.TOOL)
                    && !modifier.kinds().contains(LOTRModifier.Kind.BREAKABLE)) {
                weight = Math.max(weight / 3, 1);
            }
            candidates.add(modifier);
            weights.add(weight);
        }

        List<LOTRModifier> drawn = new ArrayList<>();
        for (int i = 0; i < count && !candidates.isEmpty(); i++) {
            int index = drawByWeight(weights, random);
            LOTRModifier pick = candidates.get(index);
            drawn.add(pick);
            for (int j = candidates.size() - 1; j >= 0; j--) {
                if (j == index || !candidates.get(j).isCompatibleWith(pick)) {
                    candidates.remove(j);
                    weights.remove(j);
                }
            }
        }
        for (LOTRModifier modifier : drawn) {
            if (canApply(modifier, stack, false) && !chosen.contains(modifier)) {
                chosen.add(modifier);
            }
        }

        set(stack, chosen, !chosen.isEmpty() || canApplyAny(stack));
    }

    /**
     * getSkilfulWeight: a skilled smith evens the gifts out -- weight to the
     * power 0.3 -- and makes a flaw rare, at 15% of its weight.
     */
    private static int skilfulWeight(LOTRModifier modifier) {
        double weight = modifier.weight();
        if (modifier.isBeneficial()) {
            weight = Math.pow(weight, 0.3);
        }
        weight *= 100.0;
        if (!modifier.isBeneficial()) {
            weight *= 0.15;
        }
        return Math.max((int) Math.round(weight), 1);
    }

    /**
     * LOTRItemModifierTemplate.getRandomCommonTemplate: a smith's scroll for
     * any modifier that has one, drawn by the skilful weights.
     */
    public static ItemStack randomTemplate(RandomSource random) {
        List<LOTRModifier> candidates = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        for (LOTRModifier modifier : LOTRModifier.values()) {
            if (modifier.hasTemplateItem()) {
                candidates.add(modifier);
                weights.add(skilfulWeight(modifier));
            }
        }
        return LOTRSmithsScrollItem.of(
                candidates.get(drawByWeight(weights, random)));
    }

    private static int drawByWeight(List<Integer> weights, RandomSource random) {
        int total = 0;
        for (int weight : weights) {
            total += weight;
        }
        int roll = random.nextInt(total);
        for (int i = 0; i < weights.size(); i++) {
            roll -= weights.get(i);
            if (roll < 0) {
                return i;
            }
        }
        return weights.size() - 1;
    }

    /** LOTRRepairCost: what the anvil adds for this item's past repairs and combines. */
    private static final String TAG_ANVIL_COST = "LOTRRepairCost";

    public static int getAnvilCost(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()
                .getIntOr(TAG_ANVIL_COST, 0);
    }

    public static void setAnvilCost(ItemStack stack, int cost) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA, data.update(tag -> tag.putInt(TAG_ANVIL_COST, cost)));
    }

    /** isReforgeable: it has modifiers, or could take one. */
    public static boolean isReforgeable(ItemStack stack) {
        return !get(stack).isEmpty() || canApplyAny(stack);
    }

    /** setEnchantList: write the modifiers onto a stack, effects and all. */
    public static void set(ItemStack stack, List<LOTRModifier> modifiers) {
        set(stack, modifiers, true);
    }

    private static void set(ItemStack stack, List<LOTRModifier> modifiers, boolean rolled) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA, data.update(tag -> {
            ListTag list = new ListTag();
            for (LOTRModifier modifier : modifiers) {
                list.add(StringTag.valueOf(modifier.getSerializedName()));
            }
            tag.put(TAG, list);
            if (rolled) {
                tag.putBoolean(TAG_ROLLED, true);
            }
        }));
        applyEffects(stack, modifiers);
    }

    // ------------------------------------------------------------------------
    // Effects written onto the stack
    // ------------------------------------------------------------------------

    /**
     * The modifiers that are plain attribute changes, written onto the stack's
     * attributes.
     *
     * <p>Damage adds to the weapon damage modifier, and speed and reach
     * multiply the swing and the reach; knockback and protection are new
     * modifiers of their own. A weapon with no reach entry -- a sword -- has
     * one added from the base of 3, and one with no damage entry -- a throwing
     * axe held in the hand -- gets the damage change as its own modifier, as
     * func_152377_a added it to any held item.
     */
    private static void applyEffects(ItemStack stack, List<LOTRModifier> modifiers) {
        // Always from the item's own defaults, so re-applying -- a reforge, an
        // anvil combine, a bane earned -- never stacks on the last application.
        ItemAttributeModifiers base = defaultAttributes(stack);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        boolean hasDamageEntry = false;
        boolean hasReachEntry = false;
        for (ItemAttributeModifiers.Entry entry : base.modifiers()) {
            AttributeModifier modifier = entry.modifier();
            double amount = modifier.amount();
            boolean baseDamage = entry.attribute().equals(Attributes.ATTACK_DAMAGE)
                    && modifier.id().equals(Item.BASE_ATTACK_DAMAGE_ID);
            boolean baseSpeed = entry.attribute().equals(Attributes.ATTACK_SPEED)
                    && modifier.id().equals(Item.BASE_ATTACK_SPEED_ID);
            boolean reach = entry.attribute().equals(Attributes.ENTITY_INTERACTION_RANGE);
            hasDamageEntry |= baseDamage;
            hasReachEntry |= reach;
            for (LOTRModifier extra : modifiers) {
                if (baseDamage && extra.effect() == LOTRModifier.Effect.DAMAGE) {
                    amount += extra.value();
                } else if (baseSpeed && extra.effect() == LOTRModifier.Effect.MELEE_SPEED) {
                    amount = (BASE_ATTACK_SPEED + amount) * extra.value() - BASE_ATTACK_SPEED;
                } else if (reach && extra.effect() == LOTRModifier.Effect.MELEE_REACH) {
                    amount = (BASE_INTERACTION_RANGE + amount) * extra.value() - BASE_INTERACTION_RANGE;
                }
            }
            builder.add(entry.attribute(), new AttributeModifier(modifier.id(), amount, modifier.operation()),
                    entry.slot());
        }

        for (LOTRModifier modifier : modifiers) {
            switch (modifier.effect()) {
                case DAMAGE -> {
                    if (!hasDamageEntry) {
                        builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(id(modifier),
                                modifier.value(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                    }
                }
                case MELEE_REACH -> {
                    if (!hasReachEntry) {
                        builder.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(id(modifier),
                                BASE_INTERACTION_RANGE * modifier.value() - BASE_INTERACTION_RANGE,
                                AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                    }
                }
                case KNOCKBACK -> builder.add(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(id(modifier),
                        modifier.value(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                case PROTECTION -> builder.add(Attributes.ARMOR, new AttributeModifier(id(modifier),
                        modifier.value(), AttributeModifier.Operation.ADD_VALUE), armorSlot(stack));
                default -> { }
            }
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
        nameAndDescribe(stack, modifiers);
    }

    /**
     * getFullEnchantedName folded each modifier's name in front of the item's,
     * last first -- a "Keen Hardy Bronze Sword" -- and the tooltip gave each
     * getNamedFormattedDescription line, "Keen: +1.0 melee damage", grey for a
     * gift and dark grey for a flaw.
     *
     * <p>ITEM_NAME rather than CUSTOM_NAME: the latter is the anvil rename and
     * renders in italics, which this is not.
     */
    private static void nameAndDescribe(ItemStack stack, List<LOTRModifier> modifiers) {
        Component name = stack.getItem().components().getOrDefault(DataComponents.ITEM_NAME,
                stack.getItem().getName(stack.getItem().getDefaultInstance()));
        for (int i = modifiers.size() - 1; i >= 0; i--) {
            name = Component.translatable("lotr.enchant.nameFormat",
                    Component.translatable(modifiers.get(i).translationKey()), name);
        }
        stack.set(DataComponents.ITEM_NAME, name);

        boolean throwingAxe = stack.getItem() instanceof LOTRThrowingAxeItem;
        boolean melee = kindsOf(stack).contains(LOTRModifier.Kind.MELEE);
        List<Component> lore = new ArrayList<>();
        for (LOTRModifier modifier : modifiers) {
            lore.add(Component.translatable("lotr.enchant.descFormat",
                            Component.translatable(modifier.translationKey()), modifier.description(throwingAxe, melee))
                    .withStyle(modifier.isBeneficial() ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
        }
        lore.addAll(stack.getItem().components().getOrDefault(DataComponents.LORE, ItemLore.EMPTY).lines());
        stack.set(DataComponents.LORE, new ItemLore(lore));
    }

    private static EquipmentSlotGroup armorSlot(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        return equippable == null ? EquipmentSlotGroup.ARMOR : EquipmentSlotGroup.bySlot(equippable.slot());
    }

    /**
     * The attribute modifier's id: the serialized name is camel case ("protectWeak1"),
     * which an Identifier path may not be, so it is broken into snake case.
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
}
