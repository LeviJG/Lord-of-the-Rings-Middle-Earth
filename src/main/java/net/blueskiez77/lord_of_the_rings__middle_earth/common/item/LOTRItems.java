package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCropBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrophyType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREffects;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRConkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRExplodingTermiteEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMysteryWebEntity;

/**
 * The mod's items. The items themselves live in one class per creative tab
 * (LOTRCombatItems, LOTRFoodItems, LOTRMaterialItems, LOTRToolItems,
 * LOTRMiscItems, LOTRStoryItems); this class keeps the few registered first,
 * register(), and the shared builders and constants they use, and loads the
 * tab classes from init().
 */
public final class LOTRItems {

    public static final Item MITHRIL = register("mithril",
            Item::new, new Item.Properties());
    public static final Item PIPEWEED = register("pipeweed",
            Item::new, new Item.Properties());

    // LOTRItemKebab: new LOTRItemKebab(8, 0.8f, true) -- heal 8, saturation
    // 0.8, and wolves will eat it. It is what the kebab stand turns raw meat
    // into, so the stand needs it registered.
    //
    // LOTRItemKebab: one eating in a hundred tells the player it was a good kebab.
    public static final Item KEBAB = register("kebab",
            LOTRKebabItem::new, new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).build()));

    // LOTRItemTrollStatue: places a stone troll. Stack size 1, as it was --
    // outfit and head count live in the stack, so two statues are rarely alike.
    // LOTRItemFeatherDyed: a feather in any colour, one to a stack, drawn with
    // vanilla's feather sprite tinted by its DYED_COLOR (white until dyed). No
    // creative tab, as the original set none: it is made by dyeing a feather.
    public static final Item FEATHER_DYED = register("feather_dyed", Item::new,
            new Item.Properties().stacksTo(1));

    public static final Item TROLL_STATUE = register("troll_statue",
            LOTRTrollStatueItem::new, new Item.Properties().stacksTo(1));

    // LOTRItemBossTrophy's two subtypes, one item each. Stack size 1, as it was.
    public static final Item MOUNTAIN_TROLL_CHIEFTAIN_TROPHY = register(
            LOTRTrophyType.MOUNTAIN_TROLL_CHIEFTAIN.itemName(),
            props -> new LOTRBossTrophyItem(LOTRTrophyType.MOUNTAIN_TROLL_CHIEFTAIN, props),
            new Item.Properties().stacksTo(1));
    public static final Item MALLORN_ENT_TROPHY = register(
            LOTRTrophyType.MALLORN_ENT.itemName(),
            props -> new LOTRBossTrophyItem(LOTRTrophyType.MALLORN_ENT, props),
            new Item.Properties().stacksTo(1));

    /** The item a given trophy entity gives back when it is knocked down. */
    public static Item trophyItem(LOTRTrophyType type) {
        return type == LOTRTrophyType.MALLORN_ENT
                ? MALLORN_ENT_TROPHY
                : MOUNTAIN_TROLL_CHIEFTAIN_TROPHY;
    }

    /**
     * Daggers swing twice a second and reach two and a half blocks. A deliberate
     * choice over LOTRWeaponStats' factors (1.5 and 0.75 of the sword, which
     * would be 2.4 and 2.25).
     */
    static final double DAGGER_ATTACK_SPEED = 2.0;
    static final double DAGGER_REACH = 2.5;

    /**
     * DECLARED UP HERE ON PURPOSE, above every item that uses it.
     *
     * <p>A static field is null until its own initializer runs, and initializers
     * run top to bottom. When this sat below the daggers, dagger() read it as
     * null while MORDOR_DAGGER was being built, and the reach modifier went out
     * with a null id -- which only surfaced later, as an NPE encoding the
     * update_attributes packet the moment a player held one, disconnecting them.
     * DAGGER_ATTACK_SPEED and DAGGER_REACH got away with it because a static
     * final double with a constant initializer is inlined at compile time.
     */
    static final Identifier DAGGER_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dagger_reach");

    // Up here rather than beside polearm(): Java forbids a field initialiser
    // from naming a field declared later in the class, and the polearms are.
    static final double POLEARM_SPEED = 1.6 * 0.667;
    // registerMeleeSpeed(LOTRItemBattleaxe.class, 0.75f), as a modifier on the base 4.
    static final float BATTLEAXE_SPEED = (float) (1.6 * 0.75 - 4.0);
    static final double POLEARM_REACH = 3.0 * 1.5;
    static final double LONG_POLEARM_SPEED = 1.6 * 0.5;
    static final double LONG_POLEARM_REACH = 3.0 * 2.0;

    /** Up here for the same reason DAGGER_REACH_ID is. See the note above. */
    static final Identifier POLEARM_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "polearm_reach");

    /** And this one. */
    static final Identifier POLEARM_KNOCKBACK_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "polearm_knockback");

    /** And this one: LOTRItemLance.lanceSpeedBoost. */
    static final Identifier LANCE_MOVEMENT_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "lance_movement");

    /**
     * lanceSpeedBoost: {@code new AttributeModifier(..., -0.2, 2)}, operation 2
     * being a multiplier on the total -- a lance slows its bearer by a fifth.
     */
    static final double LANCE_MOVEMENT_PENALTY = -0.2;

    /** Up here for the same reason DAGGER_REACH_ID is. The balrog whip's reach. */
    static final Identifier WHIP_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "whip_reach");

    /** registerMeleeReach(LOTRItemBalrogWhip.class, 1.5f): a polearm's reach. */
    static final double WHIP_REACH = 3.0 * 1.5;

    /** Up here for the same reason DAGGER_REACH_ID is. See the note above. */
    static final Identifier WARHAMMER_KNOCKBACK_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "warhammer_knockback");

    /**
     * A dagger's properties: like a sword, but quicker and shorter in the arm.
     *
     * <p>Built by hand rather than through Properties.sword, because a third
     * attribute has to go alongside the usual two. ENTITY_INTERACTION_RANGE has
     * a base of 3.0, so -0.5 brings a dagger's reach to 2.5 blocks. All three
     * modifiers are ordinary attributes, so the tooltip lists reach under attack
     * damage and attack speed with no extra work.
     *
     * <p>The two attack modifiers reproduce what createSwordAttributes builds:
     * the damage passed plus the material's own, and the speed as given.
     */
    static Item.Properties dagger(ToolMaterial material, float damage) {
        java.util.Objects.requireNonNull(DAGGER_REACH_ID, "DAGGER_REACH_ID");
        float attackDamage = damage + material.attackDamageBonus();
        ItemAttributeModifiers attributes = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, DAGGER_ATTACK_SPEED - 4.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(DAGGER_REACH_ID, DAGGER_REACH - 3.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
        return new Item.Properties()
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue())
                .attributes(attributes)
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    // ---- tabTools, top to bottom. ----
    //
    // Damage is the original's modifier less one, as the melee weapons are: a
    // 1.7.10 pickaxe added material + 2, an axe + 3, a shovel + 1 and a hoe
    // nothing. Swing speeds are vanilla's for each kind of tool. Mining speed,
    // tier and durability come from the material.

    static final net.minecraft.tags.TagKey<Block> MATTOCK_MINEABLE =
            LOTRBlockTags.MATTOCK_MINEABLE;

    // ---- tabFood, top to bottom. ----
    //
    // Food values go through FoodProperties.Builder.saturationModifier, which is
    // what ItemFood's second argument was: 1.7.10 multiplied it into saturation
    // the same way (nutrition x modifier x 2).

    /** A drink: one to a stack, drunk rather than eaten, in a mug unless moved to another vessel. */
    static Item.Properties drink(boolean brewable) {
        Item.Properties properties = new Item.Properties().stacksTo(1)
                .component(DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumables.DEFAULT_DRINK)
                .component(LOTRDataComponents.VESSEL, LOTRVessel.MUG);
        return brewable ? properties.component(LOTRDataComponents.DRINK_STRENGTH, 0) : properties;
    }

    static FoodProperties food(int nutrition, float saturationModifier) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build();
    }

    /** Vanilla's iron spear swings once every 0.95 seconds. */
    static final float SPEAR_PERIOD = 0.95f;

    /**
     * A spear: vanilla's spear properties, then the attack damage put back.
     *
     * @param damage what the tooltip should read, in the same terms as the
     *               swords -- the original's lotrWeaponDamage.
     */
    static Item.Properties spear(ToolMaterial material, float damage) {
        return new Item.Properties()
                .spear(material, SPEAR_PERIOD, 0.95f, 0.6f, 2.5f, 11.0f, 6.75f, 5.1f, 11.25f, 4.6f)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage - 1.0,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        // The same expression spear() uses, so the speed matches
                        // the stab animation it also set from this figure.
                        .add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
                                        1.0 / SPEAR_PERIOD - 4.0,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build());
    }

    /**
     * A hammer swings 0.85 times a second. A deliberate choice over
     * registerMeleeSpeed(LOTRItemHammer.class, 0.667f), which would be 1.067.
     */
    static final double WARHAMMER_ATTACK_SPEED = 0.85;

    /** registerMeleeExtraKnockback(LOTRItemHammer.class, 1): one knockback level. */
    static final double WARHAMMER_KNOCKBACK = 1.0;

    /**
     * A hammer: an axe's properties, slowed down, with knockback added.
     *
     * <p>Every hammer in the mod goes through here -- the five warhammers and
     * the blacksmith's, which is LOTRItemHammer(GONDOR) like the Gondorian one
     * and swings the same way.
     *
     * <p>LOTRItemHammer hits for material + 6 like a battleaxe; LOTRWeaponStats
     * set them apart with a slower swing and one level of extra knockback. The
     * swing is the port's 0.85 rather than the original's factor.
     *
     * <p>Built by hand rather than left to Properties.axe because a third
     * attribute has to go alongside the usual two, and .attributes replaces the
     * component axe() built. Everything else axe() set survives: the TOOL
     * component, and the Weapon component carrying
     * AXE_DISABLES_BLOCKING_FOR_SECONDS, so a warhammer still breaks a raised
     * shield.
     *
     * <p>The damage figure is worked out exactly as it is for the swords -- see
     * the note at the head of the combat block -- and createToolAttributes'
     * arithmetic is reproduced here: the value passed plus the material's own.
     *
     * <p>The mod's own knockback1 and knockback2 modifiers add to this rather
     * than replacing it: they carry their own modifier id, so a Hefty warhammer
     * is this plus one more level.
     */
    static Item.Properties warhammer(ToolMaterial material, float damage) {
        java.util.Objects.requireNonNull(WARHAMMER_KNOCKBACK_ID, "WARHAMMER_KNOCKBACK_ID");
        float attackDamage = damage + material.attackDamageBonus();
        float speedModifier = (float) (WARHAMMER_ATTACK_SPEED - 4.0);
        return new Item.Properties()
                .axe(material, damage, speedModifier)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, speedModifier,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_KNOCKBACK,
                                new AttributeModifier(WARHAMMER_KNOCKBACK_ID, WARHAMMER_KNOCKBACK,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build());
    }

    /**
     * A throwing axe: one to a stack, and it wears out.
     *
     * <p>setMaxStackSize(1) and setMaxDamage(material.getMaxUses()), which is
     * all the original's constructor did besides setFull3D -- the 3D-in-hand
     * pose, which is the item model's minecraft:item/handheld parent now.
     *
     * <p>No attributes at all, deliberately: LOTRItemThrowingAxe never called
     * setDamage, so swinging one does a bare fist's damage and only the throw
     * counts. Nor is it enchantable -- the original left enchantability at
     * zero, so an enchanting table has never had anything to offer it.
     */
    static Item.Properties throwingAxe(ToolMaterial material) {
        return new Item.Properties()
                .stacksTo(1)
                .durability(material.durability())
                .repairable(material.repairItems());
    }

    /**
     * A bow's properties.
     *
     * <p>Durability is the material's uses times 1.5, as setMaxDamage did.
     *
     * <p>Enchantability is NOT the material's raw figure: LOTRItemBow overrode
     * getItemEnchantability to {@code 1 + enchantability / 5}, so a mallorn or
     * Galadhrim bow sits at 4 rather than 15 and an orc bow at 2 rather than 7.
     * Passing the raw value made every bow in the mod several times easier to
     * enchant than the original ever intended.
     *
     * <p>And the repair tag names string as well as the material's ingot --
     * getIsRepairable accepted {@code Items.string} for any bow, which is lost
     * outright if the item declares no repair items at all.
     */
    static Item.Properties bow(ToolMaterial material,
            net.minecraft.tags.TagKey<Item> repairs) {
        return new Item.Properties()
                .durability((int) (material.durability() * 1.5f))
                .enchantable(1 + material.enchantmentValue() / 5)
                .repairable(repairs);
    }

    /** setMaxStackSize(1), and vanilla's own barding properties for the rest. */
    static Item registerHorseArmor(String name, ArmorMaterial material) {
        return register(name, LOTRModifiableItem::new,
                new Item.Properties().horseArmor(material));
    }

    /**
     * LOTRWeaponStats' table, read against the sword as one.
     *
     * <p>registerMeleeSpeed gave a polearm 0.667 and a long polearm or a lance
     * 0.5, where a sword is 1; the port's sword swings at vanilla's 1.6 a
     * second, so those are 1.07 and 0.8. registerMeleeReach gave 1.5 and 2.0
     * against a base of three blocks, so 4.5 and 6.
     */
    /**
     * A polearm: a sword's damage on a long shaft, so it is slow and it reaches.
     *
     * <p>Built by hand for the same reason the dagger is -- reach is a third
     * attribute Properties.sword knows nothing about -- and the damage figure is
     * the tab's usual {@code lotrWeaponDamage - 1}.
     */
    static Item.Properties polearm(ToolMaterial material, float damage,
            double attackSpeed, double reach, double knockback) {
        return polearm(material, damage, attackSpeed, reach, knockback, 0.0);
    }

    /**
     * A lance: a long polearm with one extra knockback, and LOTRItemLance's
     * movement penalty.
     *
     * <p>The original applied lanceSpeedBoost in LOTREventHandler only while
     * the holder was on foot and not in creative. A main-hand modifier is on
     * whenever the lance is held, but a mount moves by its own speed, so riding
     * is unaffected either way; creative walking is the one difference.
     */
    static Item.Properties lance(ToolMaterial material, float damage) {
        return polearm(material, damage, LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 1.0,
                LANCE_MOVEMENT_PENALTY);
    }

    static Item.Properties polearm(ToolMaterial material, float damage,
            double attackSpeed, double reach, double knockback, double movement) {
        java.util.Objects.requireNonNull(POLEARM_REACH_ID, "POLEARM_REACH_ID");
        java.util.Objects.requireNonNull(POLEARM_KNOCKBACK_ID, "POLEARM_KNOCKBACK_ID");
        ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage - 1.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed - 4.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(POLEARM_REACH_ID, reach - 3.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND);
        if (knockback > 0.0) {
            attributes.add(Attributes.ATTACK_KNOCKBACK,
                    new AttributeModifier(POLEARM_KNOCKBACK_ID, knockback,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND);
        }
        if (movement != 0.0) {
            java.util.Objects.requireNonNull(LANCE_MOVEMENT_ID, "LANCE_MOVEMENT_ID");
            attributes.add(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(LANCE_MOVEMENT_ID, movement,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                    EquipmentSlotGroup.MAINHAND);
        }
        return new Item.Properties()
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue())
                .attributes(attributes.build())
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    static Item registerArmor(String name, ArmorMaterial material, ArmorType type) {
        return register(name, LOTRModifiableItem::new, new Item.Properties().humanoidArmor(material, type));
    }

    private LOTRItems() {
    }

    public static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        T item = factory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    // Force class-load so the static fields register, the tab classes in the
    // order their sections sat in this file. Called from mod init.
    public static void init() {
        LOTRCombatItems.init();
        LOTRMiscItems.init();
        LOTRToolItems.init();
        LOTRFoodItems.init();
        LOTRMaterialItems.init();
        LOTRStoryItems.init();
    }
}