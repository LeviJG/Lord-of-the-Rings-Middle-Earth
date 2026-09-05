package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
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

public final class LOTRItems {
    // Item under the same id is a duplicate-key crash on load.

    public static final Item MITHRIL = register("mithril",
            Item::new, new Item.Properties());
    public static final Item PIPEWEED = register("pipeweed",
            Item::new, new Item.Properties());

    // LOTRItemKebab: new LOTRItemKebab(8, 0.8f, true) -- heal 8, saturation
    // 0.8, and wolves will eat it. It is what the kebab stand turns raw meat
    // into, so the stand needs it registered.
    //
    // NOT ported: onEaten had a one-in-a-hundred chance of telling the player
    // "That was a good kebab. You feel a lot better." A consumption hook like
    // that wants an item subclass; the joke is noted here so it is not lost.
    public static final Item KEBAB = register("kebab",
            Item::new, new Item.Properties()
                    .food(new FoodProperties(8, 0.8f, false)));

    // LOTRItemTrollStatue: places a stone troll. Stack size 1, as it was --
    // outfit and head count live in the stack, so two statues are rarely alike.
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

    /** Daggers swing twice a second. */
    private static final double DAGGER_ATTACK_SPEED = 2.0;

    /** And reach half a block less far than a fist. */
    private static final double DAGGER_REACH = 2.5;

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
    private static final Identifier DAGGER_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dagger_reach");

    // Up here rather than beside polearm(): Java forbids a field initialiser
    // from naming a field declared later in the class, and the polearms are.
    private static final double POLEARM_SPEED = 1.6 * 0.667;
    private static final double POLEARM_REACH = 3.0 * 1.5;
    private static final double LONG_POLEARM_SPEED = 1.6 * 0.5;
    private static final double LONG_POLEARM_REACH = 3.0 * 2.0;

    /** Up here for the same reason DAGGER_REACH_ID is. See the note above. */
    private static final Identifier POLEARM_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "polearm_reach");

    /** And this one. */
    private static final Identifier POLEARM_KNOCKBACK_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "polearm_knockback");

    /** Up here for the same reason DAGGER_REACH_ID is. See the note above. */
    private static final Identifier WARHAMMER_KNOCKBACK_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "warhammer_knockback");

    // ---- Combat ------------------------------------------------------------
    //
    // Declared in LOTRMod's registerItem order, because that is the order the
    // creative tab shows them in: a 1.7.10 tab was simply the items that named
    // it, in registration order. Tools are skipped.
    //
    // On the damage figures. LOTRItemSword set lotrWeaponDamage = material
    // damage + 4; the battleaxe adds 2 more and the dagger takes 3 off. That
    // whole figure was the attack damage modifier, and 1.7.10 showed the
    // modifier itself in the tooltip. 26.2 shows the TOTAL, base point of player
    // damage included, so the modifier passed here is lotrWeaponDamage - 1 and
    // the tooltip reads the original's number. createSwordAttributes then adds
    // the material's own damage on top, so the value in each call is
    // lotrWeaponDamage - 1 - materialDamage.
    //
    // Attack speed is NOT from the original -- 1.7.10 had no such concept. The
    // attribute's base is 4.0, so the modifier is (wanted speed - 4). Swords
    // keep vanilla's 1.6; daggers swing at 2.0 and the battleaxe at 1.0.

    // swordBronze: 1.5 + 4 = 5.5.
    public static final Item BRONZE_SWORD = register("bronze_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.BRONZE, 3.0f, -2.4f));

    public static final Item BRONZE_HELMET = registerArmor("bronze_helmet",
            LOTRToolMaterials.BRONZE_ARMOR, ArmorType.HELMET);
    public static final Item BRONZE_CHESTPLATE = registerArmor("bronze_chestplate",
            LOTRToolMaterials.BRONZE_ARMOR, ArmorType.CHESTPLATE);
    public static final Item BRONZE_LEGGINGS = registerArmor("bronze_leggings",
            LOTRToolMaterials.BRONZE_ARMOR, ArmorType.LEGGINGS);
    public static final Item BRONZE_BOOTS = registerArmor("bronze_boots",
            LOTRToolMaterials.BRONZE_ARMOR, ArmorType.BOOTS);

    // scimitarOrc: a plain LOTRItemSword, 2.5 + 4 = 6.5.
    public static final Item MORDOR_SCIMITAR = register("mordor_scimitar",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.MORDOR, 3.0f, -2.4f));

    public static final Item MORDOR_HELMET = registerArmor("mordor_helmet",
            LOTRToolMaterials.MORDOR_ARMOR, ArmorType.HELMET);
    public static final Item MORDOR_CHESTPLATE = registerArmor("mordor_chestplate",
            LOTRToolMaterials.MORDOR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item MORDOR_LEGGINGS = registerArmor("mordor_leggings",
            LOTRToolMaterials.MORDOR_ARMOR, ArmorType.LEGGINGS);
    public static final Item MORDOR_BOOTS = registerArmor("mordor_boots",
            LOTRToolMaterials.MORDOR_ARMOR, ArmorType.BOOTS);

    // battleaxeOrc: LOTRItemBattleaxe adds 2, so 2.5 + 4 + 2 = 8.5.
    //
    // Registered as an AXE rather than a sword: Properties.axe gives it the
    // Weapon component with Weapon.AXE_DISABLES_BLOCKING_FOR_SECONDS, which is
    // what lets an axe break a raised shield. createToolAttributes adds the
    // material damage the same way createSwordAttributes does, so the figure
    // below is worked out identically.
    public static final Item MORDOR_BATTLEAXE = register("mordor_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.MORDOR, 5.0f, -3.0f));

    // daggerOrc: LOTRItemDagger takes 3 off, so 2.5 + 4 - 3 = 3.5.
    public static final Item MORDOR_DAGGER = register("mordor_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.MORDOR, 0.0f));

    // daggerOrcPoisoned: the same dagger, whose cut festers.
    public static final Item POISONED_MORDOR_DAGGER = register("poisoned_mordor_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.MORDOR, 0.0f));

    // swordMithril: 5.0 + 4 = 9.0.
    public static final Item MITHRIL_SWORD = register("mithril_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.MITHRIL, 3.0f, -2.4f));

    // swordGondor: 2.5 + 4 = 6.5.
    public static final Item GONDOR_SWORD = register("gondor_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.GONDOR, 3.0f, -2.4f));

    public static final Item GONDOR_HELMET = registerArmor("gondor_helmet",
            LOTRToolMaterials.GONDOR_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item GONDOR_CHESTPLATE = registerArmor("gondor_chestplate",
            LOTRToolMaterials.GONDOR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GONDOR_LEGGINGS = registerArmor("gondor_leggings",
            LOTRToolMaterials.GONDOR_ARMOR, ArmorType.LEGGINGS);
    public static final Item GONDOR_BOOTS = registerArmor("gondor_boots",
            LOTRToolMaterials.GONDOR_ARMOR, ArmorType.BOOTS);

    public static final Item MITHRIL_HELMET = registerArmor("mithril_helmet",
            LOTRToolMaterials.MITHRIL_ARMOR, ArmorType.HELMET);
    public static final Item MITHRIL_CHESTPLATE = registerArmor("mithril_chestplate",
            LOTRToolMaterials.MITHRIL_ARMOR, ArmorType.CHESTPLATE);
    public static final Item MITHRIL_LEGGINGS = registerArmor("mithril_leggings",
            LOTRToolMaterials.MITHRIL_ARMOR, ArmorType.LEGGINGS);
    public static final Item MITHRIL_BOOTS = registerArmor("mithril_boots",
            LOTRToolMaterials.MITHRIL_ARMOR, ArmorType.BOOTS);

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
    private static Item.Properties dagger(ToolMaterial material, float damage) {
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

    // LOTRItemSpear, on vanilla's own spear: Properties.spear gives the stab
    // animation, the piercing hit, the longer AttackRange and the charge
    // (KineticWeapon) behaviour, so these act like spears rather than like
    // swords with spear textures.
    //
    // The nine floats are vanilla's tuning and are taken wholesale from the
    // IRON spear, which is the tier these sit at or above. The first is the
    // swing period in seconds -- it sets both the stab animation length and the
    // attack speed, as 1/period - 4 -- and the rest are the charge tiers.
    //
    // NONE of them is attack damage. Properties.spear sets the damage modifier
    // to the material's attackDamageBonus and nothing else, so every spear
    // would otherwise read 3.5, 3.5, 2.5 and 6.0. The attributes are therefore
    // rebuilt afterwards: .attributes replaces that component while leaving
    // everything else spear() set alone.
    public static final Item GONDOR_SPEAR = register("gondor_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.GONDOR, 5.5f));
    public static final Item MORDOR_SPEAR = register("mordor_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.MORDOR, 5.5f));
    public static final Item BRONZE_SPEAR = register("bronze_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.BRONZE, 4.5f));
    public static final Item MITHRIL_SPEAR = register("mithril_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.MITHRIL, 8.0f));

    // spearElven: 3.0 + 4 - 1 = 6.0.
    public static final Item GALADHRIM_SPEAR = register("galadhrim_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.GALADHRIM, 6.0f));

    // LOTRItemBow. Durability is the material's uses times 1.5, as the original
    // set it; the draw and the arrow speed are the two figures that separate an
    // elven bow from a plain one.
    //
    // mallornBow: a plain LOTRItemBow -- 20-tick draw, ordinary arrow speed.
    public static final Item MALLORN_BOW = register("mallorn_bow",
            props -> new LOTRBowItem(20, 1.0f, props),
            bow(LOTRToolMaterials.MALLORN, LOTRItemTags.REPAIRS_MALLORN_BOW));

    // elvenBow: setDrawTime(16) and an arrow factor of 1.25 -- quicker to draw
    // and it shoots harder and further.
    public static final Item GALADHRIM_BOW = register("galadhrim_bow",
            props -> new LOTRBowItem(16, 1.25f, props),
            bow(LOTRToolMaterials.GALADHRIM, LOTRItemTags.REPAIRS_GALADHRIM_BOW));

    public static final Item GALADHRIM_HELMET = registerArmor("galadhrim_helmet",
            LOTRToolMaterials.GALADHRIM_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item GALADHRIM_CHESTPLATE = registerArmor("galadhrim_chestplate",
            LOTRToolMaterials.GALADHRIM_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GALADHRIM_LEGGINGS = registerArmor("galadhrim_leggings",
            LOTRToolMaterials.GALADHRIM_ARMOR, ArmorType.LEGGINGS);
    public static final Item GALADHRIM_BOOTS = registerArmor("galadhrim_boots",
            LOTRToolMaterials.GALADHRIM_ARMOR, ArmorType.BOOTS);

    // helmetWarg and its fellows: LOTRMaterial.FUR, called Fur Hat, Tunic,
    // Leggings and Boots.
    public static final Item FUR_HAT = registerArmor("fur_hat",
            LOTRToolMaterials.FUR_ARMOR, ArmorType.HELMET);
    public static final Item FUR_TUNIC = registerArmor("fur_tunic",
            LOTRToolMaterials.FUR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item FUR_LEGGINGS = registerArmor("fur_leggings",
            LOTRToolMaterials.FUR_ARMOR, ArmorType.LEGGINGS);
    public static final Item FUR_BOOTS = registerArmor("fur_boots",
            LOTRToolMaterials.FUR_ARMOR, ArmorType.BOOTS);

    // orcBow: LOTRItemBow(MORDOR, 1.125) -- an ordinary 20-tick draw, but the
    // arrow leaves it an eighth faster than a plain bow's.
    public static final Item ORC_BOW = register("orc_bow",
            props -> new LOTRBowItem(20, 1.125f, props),
            bow(LOTRToolMaterials.MORDOR, LOTRItemTags.REPAIRS_ORC_BOW));

    // LOTRItemHammer adds 2, the same as a battleaxe, and like the battleaxe it
    // is registered as an AXE so it breaks a raised shield. A hammer for all
    // that, so it swings and knocks back like the warhammers do.
    public static final Item BLACKSMITH_HAMMER = register("blacksmith_hammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.GONDOR, 5.0f));

    // daggerGondor: 2.5 + 4 - 3 = 3.5.
    public static final Item GONDOR_DAGGER = register("gondor_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.GONDOR, 0.0f));

    // daggerElven: 3.0 + 4 - 3 = 4.0. setIsElvenBlade is a renderer switch and
    // is not ported; see GALADHRIM_SWORD.
    public static final Item GALADHRIM_DAGGER = register("galadhrim_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.GALADHRIM, 0.0f));

    // swordDwarven: 3.0 + 4 = 7.0.
    public static final Item DWARVEN_SWORD = register("dwarven_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.DWARVEN, 3.0f, -2.4f));

    // daggerDwarven: 3.0 + 4 - 3 = 4.0.
    public static final Item DWARVEN_DAGGER = register("dwarven_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.DWARVEN, 0.0f));

    // battleaxeDwarven: 3.0 + 4 + 2 = 9.0.
    public static final Item DWARVEN_BATTLEAXE = register("dwarven_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.DWARVEN, 5.0f, -3.0f));

    // hammerDwarven: also 9.0.
    public static final Item DWARVEN_WARHAMMER = register("dwarven_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.DWARVEN, 5.0f));

    // hammerOrc: 2.5 + 4 + 2 = 8.5.
    public static final Item MORDOR_WARHAMMER = register("mordor_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.MORDOR, 5.0f));

    public static final Item DWARVEN_HELMET = registerArmor("dwarven_helmet",
            LOTRToolMaterials.DWARVEN_ARMOR, ArmorType.HELMET);
    public static final Item DWARVEN_CHESTPLATE = registerArmor("dwarven_chestplate",
            LOTRToolMaterials.DWARVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DWARVEN_LEGGINGS = registerArmor("dwarven_leggings",
            LOTRToolMaterials.DWARVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item DWARVEN_BOOTS = registerArmor("dwarven_boots",
            LOTRToolMaterials.DWARVEN_ARMOR, ArmorType.BOOTS);

    public static final Item GALVORN_HELMET = registerArmor("galvorn_helmet",
            LOTRToolMaterials.GALVORN_ARMOR, ArmorType.HELMET);
    public static final Item GALVORN_CHESTPLATE = registerArmor("galvorn_chestplate",
            LOTRToolMaterials.GALVORN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GALVORN_LEGGINGS = registerArmor("galvorn_leggings",
            LOTRToolMaterials.GALVORN_ARMOR, ArmorType.LEGGINGS);
    public static final Item GALVORN_BOOTS = registerArmor("galvorn_boots",
            LOTRToolMaterials.GALVORN_ARMOR, ArmorType.BOOTS);

    // daggerBronze: 1.5 + 4 - 3 = 2.5.
    public static final Item BRONZE_DAGGER = register("bronze_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.BRONZE, 0.0f));

    // daggerIron: the one weapon here built on VANILLA iron rather than a LOTR
    // material -- LOTRItemDagger(Item.ToolMaterial.IRON) -- so 2.0 + 4 - 3 = 3.0.
    public static final Item IRON_DAGGER = register("iron_dagger",
            LOTRModifiableItem::new, dagger(ToolMaterial.IRON, 0.0f));

    // daggerMithril: 5.0 + 4 - 3 = 6.0.
    public static final Item MITHRIL_DAGGER = register("mithril_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.MITHRIL, 0.0f));

    // battleaxeMithril and hammerMithril: 5.0 + 4 + 2 = 11.0 apiece.
    public static final Item MITHRIL_BATTLEAXE = register("mithril_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.MITHRIL, 5.0f, -3.0f));
    public static final Item MITHRIL_WARHAMMER = register("mithril_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.MITHRIL, 5.0f));

    // hammerGondor: 2.5 + 4 + 2 = 8.5. The same figures as the blacksmith
    // hammer -- both are LOTRItemHammer(GONDOR) -- but a separate item.
    public static final Item GONDOR_WARHAMMER = register("gondor_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.GONDOR, 5.0f));

    // commandHorn: an InstrumentItem, so the blow itself comes from 26.2's goat
    // horn machinery. Stack size 1, as it was.
    public static final Item COMMAND_HORN = register("command_horn",
            LOTRCommandHornItem::new, new Item.Properties()
                    .stacksTo(1)
                    // delayedComponent, not a plain one: an Instrument lives in
                    // a datapack registry, so its Holder cannot be resolved
                    // while items are still being registered. The lookup is
                    // deferred until the registries are up.
                    .delayedComponent(DataComponents.INSTRUMENT,
                            lookup -> new InstrumentComponent(lookup
                                    .lookupOrThrow(Registries.INSTRUMENT)
                                    .getOrThrow(LOTRCommandHornItem.INSTRUMENT))));

    // LOTRItemThrowingAxe, registered between the horn and the Uruk set.
    // Damage is not an attribute on these: an axe you throw hits for
    // (material + 4) * 0.5 times its speed at impact, and that lives on the
    // entity. See LOTRThrowingAxeItem.
    public static final Item DWARVEN_THROWING_AXE = register("dwarven_throwing_axe",
            props -> new LOTRThrowingAxeItem(LOTRToolMaterials.DWARVEN, props),
            throwingAxe(LOTRToolMaterials.DWARVEN));

    // The Uruk set, LOTRMod's next block after the horn. scimitarUruk is called
    // a Cleaver, not a Scimitar -- the Mordor one keeps that name.
    public static final Item URUK_CLEAVER = register("uruk_cleaver",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.URUK, 3.0f, -2.4f));
    public static final Item URUK_DAGGER = register("uruk_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.URUK, 0.0f));
    public static final Item POISONED_URUK_DAGGER = register("poisoned_uruk_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.URUK, 0.0f));
    public static final Item URUK_BATTLEAXE = register("uruk_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.URUK, 5.0f, -3.0f));
    public static final Item URUK_WARHAMMER = register("uruk_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.URUK, 5.0f));
    public static final Item URUK_SPEAR = register("uruk_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.URUK, 6.0f));

    public static final Item URUK_HELMET = registerArmor("uruk_helmet",
            LOTRToolMaterials.URUK_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item URUK_CHESTPLATE = registerArmor("uruk_chestplate",
            LOTRToolMaterials.URUK_ARMOR, ArmorType.CHESTPLATE);
    public static final Item URUK_LEGGINGS = registerArmor("uruk_leggings",
            LOTRToolMaterials.URUK_ARMOR, ArmorType.LEGGINGS);
    public static final Item URUK_BOOTS = registerArmor("uruk_boots",
            LOTRToolMaterials.URUK_ARMOR, ArmorType.BOOTS);

    // LOTRItemCrossbowBolt, registered straight after the Uruk armour and just
    // before the three crossbows that fire it. Deliberately NOT in
    // #minecraft:arrows; see LOTRCrossbowBoltItem.
    public static final Item CROSSBOW_BOLT = register("crossbow_bolt",
            LOTRCrossbowBoltItem::new, new Item.Properties());

    // LOTRItemCrossbow. The material sets durability, enchantability, what
    // mends it, and how fast the bolt leaves -- see LOTRCrossbowItem.
    public static final Item URUK_CROSSBOW = register("uruk_crossbow",
            props -> new LOTRCrossbowItem(LOTRToolMaterials.URUK, props),
            LOTRCrossbowItem.properties(LOTRToolMaterials.URUK));

    // Built on VANILLA iron, as the iron dagger and the iron throwing axe are.
    public static final Item IRON_CROSSBOW = register("iron_crossbow",
            props -> new LOTRCrossbowItem(ToolMaterial.IRON, props),
            LOTRCrossbowItem.properties(ToolMaterial.IRON));

    public static final Item MITHRIL_CROSSBOW = register("mithril_crossbow",
            props -> new LOTRCrossbowItem(LOTRToolMaterials.MITHRIL, props),
            LOTRCrossbowItem.properties(LOTRToolMaterials.MITHRIL));

    // The Wood-elven Scout's leathers, registered just before the Mirkwood bow.
    public static final Item WOOD_ELVEN_SCOUT_HOOD = registerArmor("wood_elven_scout_hood",
            LOTRToolMaterials.WOOD_ELVEN_SCOUT_ARMOR, ArmorType.HELMET);
    public static final Item WOOD_ELVEN_SCOUT_TUNIC = registerArmor("wood_elven_scout_tunic",
            LOTRToolMaterials.WOOD_ELVEN_SCOUT_ARMOR, ArmorType.CHESTPLATE);
    public static final Item WOOD_ELVEN_SCOUT_LEGGINGS = registerArmor("wood_elven_scout_leggings",
            LOTRToolMaterials.WOOD_ELVEN_SCOUT_ARMOR, ArmorType.LEGGINGS);
    public static final Item WOOD_ELVEN_SCOUT_BOOTS = registerArmor("wood_elven_scout_boots",
            LOTRToolMaterials.WOOD_ELVEN_SCOUT_ARMOR, ArmorType.BOOTS);

    // mirkwoodBow: LOTRItemBow(WOOD_ELVEN).setDrawTime(14) -- the fastest draw
    // in the mod, and an ordinary arrow speed. NO pull sprites: the original
    // ships one icon for this bow and none of the three pull frames, so its
    // model is a single sprite where the other bows range-dispatch.
    public static final Item MIRKWOOD_BOW = register("mirkwood_bow",
            props -> new LOTRBowItem(14, 1.0f, props),
            bow(LOTRToolMaterials.WOOD_ELVEN, LOTRItemTags.REPAIRS_MIRKWOOD_BOW));

    // swordRohan: 2.5 + 4 = 6.5.
    public static final Item ROHIRRIC_SWORD = register("rohirric_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.ROHIRRIC, 3.0f, -2.4f));

    // daggerRohan: 2.5 + 4 - 3 = 3.5.
    public static final Item ROHIRRIC_DAGGER = register("rohirric_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.ROHIRRIC, 0.0f));

    // spearRohan: 2.5 + 4 - 1 = 5.5.
    public static final Item ROHIRRIC_SPEAR = register("rohirric_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.ROHIRRIC, 5.5f));

    // The Mark's mail is a Coif and a Hauberk, not a helmet and a chestplate --
    // the same reason the fur set is a Hat and a Tunic.
    public static final Item ROHIRRIC_COIF = registerArmor("rohirric_coif",
            LOTRToolMaterials.ROHIRRIC_ARMOR, ArmorType.HELMET);
    public static final Item ROHIRRIC_HAUBERK = registerArmor("rohirric_hauberk",
            LOTRToolMaterials.ROHIRRIC_ARMOR, ArmorType.CHESTPLATE);
    public static final Item ROHIRRIC_LEGGINGS = registerArmor("rohirric_leggings",
            LOTRToolMaterials.ROHIRRIC_ARMOR, ArmorType.LEGGINGS);
    public static final Item ROHIRRIC_BOOTS = registerArmor("rohirric_boots",
            LOTRToolMaterials.ROHIRRIC_ARMOR, ArmorType.BOOTS);

    // helmetGondorWinged: a Gondorian helmet with a wing up each side. The
    // stats are GONDOR's exactly -- it is the same helmet, better made -- and
    // the wings are geometry; see LOTRGondorWingedHelmetModel.
    public static final Item GONDOR_WINGED_HELMET = registerArmor("gondor_winged_helmet",
            LOTRToolMaterials.GONDOR_WINGED_HELMET_ARMOR, ArmorType.HELMET);

    // LOTRItemPebble and LOTRItemSling, registered together as they were.
    public static final Item PEBBLE = register("pebble",
            LOTRPebbleItem::new, new Item.Properties());

    public static final Item SLING = register("sling",
            LOTRSlingItem::new, new Item.Properties()
                    .stacksTo(1)
                    .durability(LOTRSlingItem.DURABILITY)
                    .repairable(LOTRItemTags.REPAIRS_SLING));

    // The Ranger's kit, and Dunland's. LOTRMod registers these between the
    // sling and the Morgul set.
    public static final Item RANGER_HOOD = registerArmor("ranger_hood",
            LOTRToolMaterials.RANGER_ARMOR, ArmorType.HELMET);
    public static final Item RANGER_TUNIC = registerArmor("ranger_tunic",
            LOTRToolMaterials.RANGER_ARMOR, ArmorType.CHESTPLATE);
    public static final Item RANGER_LEGGINGS = registerArmor("ranger_leggings",
            LOTRToolMaterials.RANGER_ARMOR, ArmorType.LEGGINGS);
    public static final Item RANGER_BOOTS = registerArmor("ranger_boots",
            LOTRToolMaterials.RANGER_ARMOR, ArmorType.BOOTS);

    public static final Item DUNLENDING_HELMET = registerArmor("dunlending_helmet",
            LOTRToolMaterials.DUNLENDING_ARMOR, ArmorType.HELMET);
    public static final Item DUNLENDING_CHESTPLATE = registerArmor("dunlending_chestplate",
            LOTRToolMaterials.DUNLENDING_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DUNLENDING_LEGGINGS = registerArmor("dunlending_leggings",
            LOTRToolMaterials.DUNLENDING_ARMOR, ArmorType.LEGGINGS);
    public static final Item DUNLENDING_BOOTS = registerArmor("dunlending_boots",
            LOTRToolMaterials.DUNLENDING_ARMOR, ArmorType.BOOTS);

    // dunlendingClub: LOTRItemHammer(Item.ToolMaterial.WOOD), so 0.0 + 4 + 2 =
    // 6.0 -- a lump of wood that hits as hard as a Gondorian sword and breaks
    // after fifty-nine swings. It goes through warhammer() because it IS a
    // hammer in the original, and so swings at 0.85 and knocks back like one.
    public static final Item DUNLENDING_CLUB = register("dunlending_club",
            LOTRModifiableItem::new, warhammer(ToolMaterial.WOOD, 5.0f));

    // dunlendingTrident: LOTRItemTrident(Item.ToolMaterial.IRON), 2.0 + 4 = 6.0.
    // On vanilla's TridentItem rather than the original's polearm; see
    // LOTRTridentItem for what that changes.
    public static final Item DUNLENDING_TRIDENT = register("dunlending_trident",
            LOTRTridentItem::new, LOTRTridentItem.properties(ToolMaterial.IRON, 6.0f));

    // morgulBlade: a plain LOTRItemSword, 2.5 + 4 = 6.5.
    public static final Item MORGUL_BLADE = register("morgul_blade",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.MORGUL, 3.0f, -2.4f));

    public static final Item MORGUL_HELMET = registerArmor("morgul_helmet",
            LOTRToolMaterials.MORGUL_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item MORGUL_CHESTPLATE = registerArmor("morgul_chestplate",
            LOTRToolMaterials.MORGUL_ARMOR, ArmorType.CHESTPLATE);
    public static final Item MORGUL_LEGGINGS = registerArmor("morgul_leggings",
            LOTRToolMaterials.MORGUL_ARMOR, ArmorType.LEGGINGS);
    public static final Item MORGUL_BOOTS = registerArmor("morgul_boots",
            LOTRToolMaterials.MORGUL_ARMOR, ArmorType.BOOTS);

    // The wood-elven set. The sword and dagger carry setIsElvenBlade in the
    // original -- the glow an elven blade gives off near orcs -- which is a
    // renderer switch with no effect on the stats and wants the orc NPCs; it is
    // left out here as it is on the Galadhrim blades.
    // swordWoodElven: 3.0 + 4 = 7.0.
    public static final Item WOOD_ELVEN_SWORD = register("wood_elven_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.WOOD_ELVEN, 3.0f, -2.4f));

    // daggerWoodElven: 3.0 + 4 - 3 = 4.0.
    public static final Item WOOD_ELVEN_DAGGER = register("wood_elven_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.WOOD_ELVEN, 0.0f));

    // spearWoodElven: 3.0 + 4 - 1 = 6.0.
    public static final Item WOOD_ELVEN_SPEAR = register("wood_elven_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.WOOD_ELVEN, 6.0f));

    public static final Item WOOD_ELVEN_HELMET = registerArmor("wood_elven_helmet",
            LOTRToolMaterials.WOOD_ELVEN_ARMOR, ArmorType.HELMET);
    public static final Item WOOD_ELVEN_CHESTPLATE = registerArmor("wood_elven_chestplate",
            LOTRToolMaterials.WOOD_ELVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item WOOD_ELVEN_LEGGINGS = registerArmor("wood_elven_leggings",
            LOTRToolMaterials.WOOD_ELVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item WOOD_ELVEN_BOOTS = registerArmor("wood_elven_boots",
            LOTRToolMaterials.WOOD_ELVEN_ARMOR, ArmorType.BOOTS);

    // LOTRItemCommandSword: a pointing stick, not a weapon. Unbreakable, no
    // enchantability, and lotrWeaponDamage forced to 1.0 -- so no attack
    // attributes at all, which leaves it at a bare fist. See the class note for
    // what it is FOR.
    public static final Item COMMAND_SWORD = register("command_sword",
            LOTRCommandSwordItem::new, new Item.Properties().stacksTo(1));

    // The poisoned daggers. Same figures as their plain counterparts -- the
    // effect is the only difference -- so each is dagger(material) again, on
    // LOTRPoisonedDaggerItem rather than LOTRModifiableItem.
    public static final Item POISONED_BRONZE_DAGGER = register("poisoned_bronze_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.BRONZE, 0.0f));
    public static final Item POISONED_IRON_DAGGER = register("poisoned_iron_dagger",
            LOTRPoisonedDaggerItem::new, dagger(ToolMaterial.IRON, 0.0f));
    public static final Item POISONED_MITHRIL_DAGGER = register("poisoned_mithril_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.MITHRIL, 0.0f));
    public static final Item POISONED_GONDOR_DAGGER = register("poisoned_gondor_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.GONDOR, 0.0f));
    public static final Item POISONED_GALADHRIM_DAGGER = register("poisoned_galadhrim_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.GALADHRIM, 0.0f));
    public static final Item POISONED_DWARVEN_DAGGER = register("poisoned_dwarven_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.DWARVEN, 0.0f));
    public static final Item POISONED_ROHIRRIC_DAGGER = register("poisoned_rohirric_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.ROHIRRIC, 0.0f));
    public static final Item POISONED_WOOD_ELVEN_DAGGER = register("poisoned_wood_elven_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.WOOD_ELVEN, 0.0f));

    // battleaxeAngmar: 2.5 + 4 + 2 = 8.5.
    public static final Item ANGMAR_BATTLEAXE = register("angmar_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.ANGMAR, 5.0f, -3.0f));

    // hammerAngmar: also 8.5, and a hammer, so it swings at 0.85 with knockback.
    public static final Item ANGMAR_WARHAMMER = register("angmar_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.ANGMAR, 5.0f));

    // spearAngmar: 2.5 + 4 - 1 = 5.5.
    public static final Item ANGMAR_SPEAR = register("angmar_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.ANGMAR, 5.5f));

    public static final Item ANGMAR_HELMET = registerArmor("angmar_helmet",
            LOTRToolMaterials.ANGMAR_ARMOR, ArmorType.HELMET);
    public static final Item ANGMAR_CHESTPLATE = registerArmor("angmar_chestplate",
            LOTRToolMaterials.ANGMAR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item ANGMAR_LEGGINGS = registerArmor("angmar_leggings",
            LOTRToolMaterials.ANGMAR_ARMOR, ArmorType.LEGGINGS);
    public static final Item ANGMAR_BOOTS = registerArmor("angmar_boots",
            LOTRToolMaterials.ANGMAR_ARMOR, ArmorType.BOOTS);

    // battleaxeRohan: 2.5 + 4 + 2 = 8.5.
    public static final Item ROHIRRIC_BATTLEAXE = register("rohirric_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.ROHIRRIC, 5.0f, -3.0f));

    // scimitarNearHarad, called an Umbaric Scimitar: LOTRItemSword(UMBAR), so
    // 2.5 + 4 = 6.5. The blade is Umbaric steel where the armour below is Coast
    // Southron -- two different materials under one faction, as the original
    // has them.
    public static final Item UMBARIC_SCIMITAR = register("umbaric_scimitar",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.UMBARIC, 3.0f, -2.4f));

    public static final Item COAST_SOUTHRON_HELMET = registerArmor("coast_southron_helmet",
            LOTRToolMaterials.COAST_SOUTHRON_ARMOR, ArmorType.HELMET);
    public static final Item COAST_SOUTHRON_CHESTPLATE = registerArmor("coast_southron_chestplate",
            LOTRToolMaterials.COAST_SOUTHRON_ARMOR, ArmorType.CHESTPLATE);
    public static final Item COAST_SOUTHRON_LEGGINGS = registerArmor("coast_southron_leggings",
            LOTRToolMaterials.COAST_SOUTHRON_ARMOR, ArmorType.LEGGINGS);
    public static final Item COAST_SOUTHRON_BOOTS = registerArmor("coast_southron_boots",
            LOTRToolMaterials.COAST_SOUTHRON_ARMOR, ArmorType.BOOTS);

    public static final Item GEMSBOK_HIDE_HELMET = registerArmor("gemsbok_hide_helmet",
            LOTRToolMaterials.GEMSBOK_HIDE_ARMOR, ArmorType.HELMET);
    public static final Item GEMSBOK_HIDE_CHESTPLATE = registerArmor("gemsbok_hide_chestplate",
            LOTRToolMaterials.GEMSBOK_HIDE_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GEMSBOK_HIDE_LEGGINGS = registerArmor("gemsbok_hide_leggings",
            LOTRToolMaterials.GEMSBOK_HIDE_ARMOR, ArmorType.LEGGINGS);
    public static final Item GEMSBOK_HIDE_BOOTS = registerArmor("gemsbok_hide_boots",
            LOTRToolMaterials.GEMSBOK_HIDE_ARMOR, ArmorType.BOOTS);

    public static final Item LINDON_HELMET = registerArmor("lindon_helmet",
            LOTRToolMaterials.LINDON_ARMOR, ArmorType.HELMET);
    public static final Item LINDON_CHESTPLATE = registerArmor("lindon_chestplate",
            LOTRToolMaterials.LINDON_ARMOR, ArmorType.CHESTPLATE);
    public static final Item LINDON_LEGGINGS = registerArmor("lindon_leggings",
            LOTRToolMaterials.LINDON_ARMOR, ArmorType.LEGGINGS);
    public static final Item LINDON_BOOTS = registerArmor("lindon_boots",
            LOTRToolMaterials.LINDON_ARMOR, ArmorType.BOOTS);

    // swordHighElven: 3.0 + 4 = 7.0. setIsElvenBlade, left out for the reason
    // the Galadhrim and wood-elven blades leave it out.
    public static final Item LINDON_SWORD = register("lindon_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.LINDON, 3.0f, -2.4f));

    // daggerHighElven: 3.0 + 4 - 3 = 4.0, and its poisoned twin.
    public static final Item LINDON_DAGGER = register("lindon_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.LINDON, 0.0f));
    public static final Item POISONED_LINDON_DAGGER = register("poisoned_lindon_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.LINDON, 0.0f));

    // spearHighElven: 3.0 + 4 - 1 = 6.0.
    public static final Item LINDON_SPEAR = register("lindon_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.LINDON, 6.0f));

    // daggerNearHarad and spearNearHarad, called Umbaric: LOTRItemDagger and
    // LOTRItemSpear on UMBAR, so 3.5 and 5.5.
    public static final Item UMBARIC_DAGGER = register("umbaric_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.UMBARIC, 0.0f));
    public static final Item POISONED_UMBARIC_DAGGER = register("poisoned_umbaric_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.UMBARIC, 0.0f));
    public static final Item UMBARIC_SPEAR = register("umbaric_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.UMBARIC, 5.5f));

    // nearHaradBow, the Bow of Harad: LOTRItemBow(NEAR_HARAD, 1.1) with
    // setDrawTime(25) -- the SLOWEST draw in the mod, five ticks longer than a
    // plain bow, in exchange for an arrow a tenth faster. Coast Southron work,
    // which is why it is built on that material.
    //
    // NO pull sprites, as with the Bow of Mirkwood: the original ships one icon
    // for this bow and none of the three pull frames.
    public static final Item HARAD_BOW = register("harad_bow",
            props -> new LOTRBowItem(25, 1.1f, props),
            bow(LOTRToolMaterials.COAST_SOUTHRON, LOTRItemTags.REPAIRS_HARAD_BOW));

    // spearDwarven: 3.0 + 4 - 1 = 6.0.
    public static final Item DWARVEN_SPEAR = register("dwarven_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.DWARVEN, 6.0f));

    // The Blue Dwarven armoury. Every figure is DWARVEN's worked on 650 uses
    // rather than 700, so the weapons hit the same and last a little less.
    public static final Item BLUE_DWARVEN_SWORD = register("blue_dwarven_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.BLUE_DWARVEN, 3.0f, -2.4f));
    public static final Item BLUE_DWARVEN_DAGGER = register("blue_dwarven_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.BLUE_DWARVEN, 0.0f));
    public static final Item POISONED_BLUE_DWARVEN_DAGGER = register("poisoned_blue_dwarven_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.BLUE_DWARVEN, 0.0f));
    public static final Item BLUE_DWARVEN_BATTLEAXE = register("blue_dwarven_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.BLUE_DWARVEN, 5.0f, -3.0f));
    public static final Item BLUE_DWARVEN_WARHAMMER = register("blue_dwarven_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.BLUE_DWARVEN, 5.0f));
    public static final Item BLUE_DWARVEN_SPEAR = register("blue_dwarven_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.BLUE_DWARVEN, 6.0f));
    public static final Item BLUE_DWARVEN_THROWING_AXE = register("blue_dwarven_throwing_axe",
            props -> new LOTRThrowingAxeItem(LOTRToolMaterials.BLUE_DWARVEN, props),
            throwingAxe(LOTRToolMaterials.BLUE_DWARVEN));

    public static final Item BLUE_DWARVEN_HELMET = registerArmor("blue_dwarven_helmet",
            LOTRToolMaterials.BLUE_DWARVEN_ARMOR, ArmorType.HELMET);
    public static final Item BLUE_DWARVEN_CHESTPLATE = registerArmor("blue_dwarven_chestplate",
            LOTRToolMaterials.BLUE_DWARVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item BLUE_DWARVEN_LEGGINGS = registerArmor("blue_dwarven_leggings",
            LOTRToolMaterials.BLUE_DWARVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item BLUE_DWARVEN_BOOTS = registerArmor("blue_dwarven_boots",
            LOTRToolMaterials.BLUE_DWARVEN_ARMOR, ArmorType.BOOTS);

    // LOTRItemMountArmor(material, Mount.HORSE). Properties.horseArmor is the
    // vanilla equivalent and reads ArmorType.BODY off the material, which is
    // where the original's chestplate + leggings figure now lives; the barding
    // texture is the horse_body layer of the material's equipment asset.
    public static final Item GONDOR_HORSE_ARMOR = registerHorseArmor("gondor_horse_armor",
            LOTRToolMaterials.GONDOR_ARMOR);
    public static final Item ROHIRRIC_HORSE_ARMOR = registerHorseArmor("rohirric_horse_armor",
            LOTRToolMaterials.ROHIRRIC_ARMOR);
    public static final Item LINDON_HORSE_ARMOR = registerHorseArmor("lindon_horse_armor",
            LOTRToolMaterials.LINDON_ARMOR);
    public static final Item GALADHRIM_HORSE_ARMOR = registerHorseArmor("galadhrim_horse_armor",
            LOTRToolMaterials.GALADHRIM_ARMOR);
    public static final Item MORGUL_HORSE_ARMOR = registerHorseArmor("morgul_horse_armor",
            LOTRToolMaterials.MORGUL_ARMOR);
    public static final Item MITHRIL_HORSE_ARMOR = registerHorseArmor("mithril_horse_armor",
            LOTRToolMaterials.MITHRIL_ARMOR);
    public static final Item COAST_SOUTHRON_HORSE_ARMOR = registerHorseArmor(
            "coast_southron_horse_armor", LOTRToolMaterials.COAST_SOUTHRON_ARMOR);
    public static final Item UMBARIC_HORSE_ARMOR = registerHorseArmor("umbaric_horse_armor",
            LOTRToolMaterials.UMBARIC_ARMOR);

    // LOTRItemMountArmor(material, Mount.WARG). Registered as PLAIN ITEMS, not
    // through Properties.horseArmor: that restricts a barding to
    // #minecraft:can_wear_horse_armor, and a warg is not a horse. There are no
    // wargs in the port at all yet, so there is nothing for these to go on --
    // they carry their name and their sprite and wait. When the warg lands they
    // want an Equippable on EquipmentSlot.BODY allowed to that entity, and a
    // warg_body layer on each material's asset.
    public static final Item ISENGARD_WARG_ARMOR = register("isengard_warg_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));
    public static final Item MORDOR_WARG_ARMOR = register("mordor_warg_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));
    public static final Item ANGMAR_WARG_ARMOR = register("angmar_warg_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));

    // LOTRItemOrcSkullStaff: a LOTRItemSword(MORDOR), so 2.5 + 4 = 6.5, whose
    // getIsRepairable OVERRIDES rather than extends -- skulls mend it and orc
    // steel does not.
    public static final Item ORC_SKULL_STAFF = register("orc_skull_staff",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.MORDOR, 3.0f, -2.4f)
                    .repairable(LOTRItemTags.REPAIRS_ORC_SKULL_STAFF));

    // The Dol Guldur armoury: 2.5 damage, so a sword is 6.5, a dagger 3.5, a
    // spear 5.5, an axe and a hammer 8.5.
    public static final Item DOL_GULDUR_SWORD = register("dol_guldur_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.DOL_GULDUR, 3.0f, -2.4f));
    public static final Item DOL_GULDUR_DAGGER = register("dol_guldur_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.DOL_GULDUR, 0.0f));
    public static final Item POISONED_DOL_GULDUR_DAGGER = register("poisoned_dol_guldur_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.DOL_GULDUR, 0.0f));
    public static final Item DOL_GULDUR_SPEAR = register("dol_guldur_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.DOL_GULDUR, 5.5f));
    public static final Item DOL_GULDUR_BATTLEAXE = register("dol_guldur_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.DOL_GULDUR, 5.0f, -3.0f));
    public static final Item DOL_GULDUR_WARHAMMER = register("dol_guldur_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.DOL_GULDUR, 5.0f));
    public static final Item DOL_GULDUR_HELMET = registerArmor("dol_guldur_helmet",
            LOTRToolMaterials.DOL_GULDUR_ARMOR, ArmorType.HELMET);
    public static final Item DOL_GULDUR_CHESTPLATE = registerArmor("dol_guldur_chestplate",
            LOTRToolMaterials.DOL_GULDUR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DOL_GULDUR_LEGGINGS = registerArmor("dol_guldur_leggings",
            LOTRToolMaterials.DOL_GULDUR_ARMOR, ArmorType.LEGGINGS);
    public static final Item DOL_GULDUR_BOOTS = registerArmor("dol_guldur_boots",
            LOTRToolMaterials.DOL_GULDUR_ARMOR, ArmorType.BOOTS);

    // Utumno: 3.5 damage, the heaviest edge short of mithril. Sword 7.5,
    // dagger 4.5, spear 6.5, axe and hammer 9.5.
    public static final Item UTUMNO_SWORD = register("utumno_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.UTUMNO, 3.0f, -2.4f));
    public static final Item UTUMNO_DAGGER = register("utumno_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.UTUMNO, 0.0f));
    public static final Item POISONED_UTUMNO_DAGGER = register("poisoned_utumno_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.UTUMNO, 0.0f));
    public static final Item UTUMNO_SPEAR = register("utumno_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.UTUMNO, 6.5f));
    public static final Item UTUMNO_BATTLEAXE = register("utumno_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.UTUMNO, 5.0f, -3.0f));
    public static final Item UTUMNO_WARHAMMER = register("utumno_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.UTUMNO, 5.0f));
    public static final Item UTUMNO_HELMET = registerArmor("utumno_helmet",
            LOTRToolMaterials.UTUMNO_ARMOR, ArmorType.HELMET);
    public static final Item UTUMNO_CHESTPLATE = registerArmor("utumno_chestplate",
            LOTRToolMaterials.UTUMNO_ARMOR, ArmorType.CHESTPLATE);
    public static final Item UTUMNO_LEGGINGS = registerArmor("utumno_leggings",
            LOTRToolMaterials.UTUMNO_ARMOR, ArmorType.LEGGINGS);
    public static final Item UTUMNO_BOOTS = registerArmor("utumno_boots",
            LOTRToolMaterials.UTUMNO_ARMOR, ArmorType.BOOTS);

    // The black Uruks: 3.0 damage, so the same figures as Isengard's, and like
    // theirs the sword is a Cleaver.
    public static final Item BLACK_URUK_CLEAVER = register("black_uruk_cleaver",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.BLACK_URUK, 3.0f, -2.4f));
    public static final Item BLACK_URUK_DAGGER = register("black_uruk_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.BLACK_URUK, 0.0f));
    public static final Item POISONED_BLACK_URUK_DAGGER = register("poisoned_black_uruk_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.BLACK_URUK, 0.0f));
    public static final Item BLACK_URUK_SPEAR = register("black_uruk_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.BLACK_URUK, 6.0f));
    public static final Item BLACK_URUK_BATTLEAXE = register("black_uruk_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.BLACK_URUK, 5.0f, -3.0f));
    public static final Item BLACK_URUK_WARHAMMER = register("black_uruk_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.BLACK_URUK, 5.0f));
    public static final Item BLACK_URUK_HELMET = registerArmor("black_uruk_helmet",
            LOTRToolMaterials.BLACK_URUK_ARMOR, ArmorType.HELMET);
    public static final Item BLACK_URUK_CHESTPLATE = registerArmor("black_uruk_chestplate",
            LOTRToolMaterials.BLACK_URUK_ARMOR, ArmorType.CHESTPLATE);
    public static final Item BLACK_URUK_LEGGINGS = registerArmor("black_uruk_leggings",
            LOTRToolMaterials.BLACK_URUK_ARMOR, ArmorType.LEGGINGS);
    public static final Item BLACK_URUK_BOOTS = registerArmor("black_uruk_boots",
            LOTRToolMaterials.BLACK_URUK_ARMOR, ArmorType.BOOTS);

    // Three more bows. rohanBow is a plain one; gondorBow shoots an eighth
    // faster; highElvenBow, the Lindon Bow, draws in sixteen ticks and shoots a
    // quarter faster -- the Galadhrim bow's figures on longer-lasting steel.
    public static final Item ROHIRRIC_BOW = register("rohirric_bow",
            props -> new LOTRBowItem(20, 1.0f, props),
            bow(LOTRToolMaterials.ROHIRRIC, LOTRItemTags.REPAIRS_ROHIRRIC_BOW));
    public static final Item GONDOR_BOW = register("gondor_bow",
            props -> new LOTRBowItem(20, 1.125f, props),
            bow(LOTRToolMaterials.GONDOR, LOTRItemTags.REPAIRS_GONDOR_BOW));
    public static final Item LINDON_BOW = register("lindon_bow",
            props -> new LOTRBowItem(16, 1.25f, props),
            bow(LOTRToolMaterials.LINDON, LOTRItemTags.REPAIRS_LINDON_BOW));

    // LOTRItemBalrogWhip: a UTUMNO blade whose damage is set to 7.0 outright
    // rather than worked out from the material, on a thousand uses. Not
    // enchantable, and mended only with balrog fire -- which is not ported, so
    // its tag is empty. See the class for what the lash does on a hit.
    public static final Item BALROG_WHIP = register("balrog_whip",
            LOTRBalrogWhipItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.UTUMNO,
                            LOTRBalrogWhipItem.ATTACK_DAMAGE - 1.0f
                                    - LOTRToolMaterials.UTUMNO.attackDamageBonus(),
                            -2.4f)
                    .durability(LOTRBalrogWhipItem.DURABILITY)
                    .repairable(LOTRItemTags.REPAIRS_BALROG_WHIP));

    // battleaxeIron: LOTRItemBattleaxe on VANILLA iron, so 2.0 + 4 + 2 = 8.0.
    public static final Item IRON_BATTLEAXE = register("iron_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(ToolMaterial.IRON, 5.0f, -3.0f));

    // battleaxeBronze: 1.5 + 4 + 2 = 7.5.
    public static final Item BRONZE_BATTLEAXE = register("bronze_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.BRONZE, 5.0f, -3.0f));

    // bronzeCrossbow: LOTRItemCrossbow(BRONZE). Bronze damage is 1.5, so
    // boltDamageFactor is 1.0 -- the expression subtracts two and clamps at
    // zero -- and this is the plainest crossbow of the four.
    public static final Item BRONZE_CROSSBOW = register("bronze_crossbow",
            props -> new LOTRCrossbowItem(LOTRToolMaterials.BRONZE, props),
            LOTRCrossbowItem.properties(LOTRToolMaterials.BRONZE));

    // LOTRItemConquestHorn, one item carrying a faction. See LOTRWarhornItem.
    public static final Item WARHORN = register("warhorn",
            LOTRWarhornItem::new, new Item.Properties().stacksTo(1));

    // The half-trolls: 2.5 damage on stone-tier steel, so a scimitar is 6.5, a
    // dagger 3.5, and the axe, hammer and mace 8.5 apiece. maceHalfTroll and
    // hammerHalfTroll are BOTH LOTRItemHammer(HALF_TROLL) -- two items, one set
    // of figures -- so both go through warhammer().
    public static final Item HALF_TROLL_SCIMITAR = register("half_troll_scimitar",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.HALF_TROLL, 3.0f, -2.4f));
    public static final Item HALF_TROLL_DAGGER = register("half_troll_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.HALF_TROLL, 0.0f));
    public static final Item POISONED_HALF_TROLL_DAGGER = register("poisoned_half_troll_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.HALF_TROLL, 0.0f));
    public static final Item HALF_TROLL_BATTLEAXE = register("half_troll_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.HALF_TROLL, 5.0f, -3.0f));
    public static final Item HALF_TROLL_WARHAMMER = register("half_troll_warhammer",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.HALF_TROLL, 5.0f));
    public static final Item HALF_TROLL_MACE = register("half_troll_mace",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.HALF_TROLL, 5.0f));
    public static final Item HALF_TROLL_HELMET = registerArmor("half_troll_helmet",
            LOTRToolMaterials.HALF_TROLL_ARMOR, ArmorType.HELMET);
    public static final Item HALF_TROLL_CHESTPLATE = registerArmor("half_troll_chestplate",
            LOTRToolMaterials.HALF_TROLL_ARMOR, ArmorType.CHESTPLATE);
    public static final Item HALF_TROLL_LEGGINGS = registerArmor("half_troll_leggings",
            LOTRToolMaterials.HALF_TROLL_ARMOR, ArmorType.LEGGINGS);
    public static final Item HALF_TROLL_BOOTS = registerArmor("half_troll_boots",
            LOTRToolMaterials.HALF_TROLL_ARMOR, ArmorType.BOOTS);

    // The three trimmed Dwarven sets: Dwarven armour to the point, wearing a
    // different sheet. Every figure is DWARVEN_ARMOR's.
    public static final Item SILVER_TRIMMED_DWARVEN_HELMET = registerArmor(
            "silver_trimmed_dwarven_helmet",
            LOTRToolMaterials.SILVER_TRIMMED_DWARVEN_ARMOR, ArmorType.HELMET);
    public static final Item SILVER_TRIMMED_DWARVEN_CHESTPLATE = registerArmor(
            "silver_trimmed_dwarven_chestplate",
            LOTRToolMaterials.SILVER_TRIMMED_DWARVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item SILVER_TRIMMED_DWARVEN_LEGGINGS = registerArmor(
            "silver_trimmed_dwarven_leggings",
            LOTRToolMaterials.SILVER_TRIMMED_DWARVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item SILVER_TRIMMED_DWARVEN_BOOTS = registerArmor(
            "silver_trimmed_dwarven_boots",
            LOTRToolMaterials.SILVER_TRIMMED_DWARVEN_ARMOR, ArmorType.BOOTS);

    public static final Item GOLD_TRIMMED_DWARVEN_HELMET = registerArmor(
            "gold_trimmed_dwarven_helmet",
            LOTRToolMaterials.GOLD_TRIMMED_DWARVEN_ARMOR, ArmorType.HELMET);
    public static final Item GOLD_TRIMMED_DWARVEN_CHESTPLATE = registerArmor(
            "gold_trimmed_dwarven_chestplate",
            LOTRToolMaterials.GOLD_TRIMMED_DWARVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GOLD_TRIMMED_DWARVEN_LEGGINGS = registerArmor(
            "gold_trimmed_dwarven_leggings",
            LOTRToolMaterials.GOLD_TRIMMED_DWARVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item GOLD_TRIMMED_DWARVEN_BOOTS = registerArmor(
            "gold_trimmed_dwarven_boots",
            LOTRToolMaterials.GOLD_TRIMMED_DWARVEN_ARMOR, ArmorType.BOOTS);

    public static final Item MITHRIL_TRIMMED_DWARVEN_HELMET = registerArmor(
            "mithril_trimmed_dwarven_helmet",
            LOTRToolMaterials.MITHRIL_TRIMMED_DWARVEN_ARMOR, ArmorType.HELMET);
    public static final Item MITHRIL_TRIMMED_DWARVEN_CHESTPLATE = registerArmor(
            "mithril_trimmed_dwarven_chestplate",
            LOTRToolMaterials.MITHRIL_TRIMMED_DWARVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item MITHRIL_TRIMMED_DWARVEN_LEGGINGS = registerArmor(
            "mithril_trimmed_dwarven_leggings",
            LOTRToolMaterials.MITHRIL_TRIMMED_DWARVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item MITHRIL_TRIMMED_DWARVEN_BOOTS = registerArmor(
            "mithril_trimmed_dwarven_boots",
            LOTRToolMaterials.MITHRIL_TRIMMED_DWARVEN_ARMOR, ArmorType.BOOTS);

    // Dol Amroth: 3.0 damage, so the sword is 7.0. The helmet and the
    // chestplate each carry the swan and so wear a sheet of their own; the
    // leggings, boots and barding take the plain one.
    public static final Item DOL_AMROTH_SWORD = register("dol_amroth_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.DOL_AMROTH, 3.0f, -2.4f));
    public static final Item DOL_AMROTH_HELMET = registerArmor("dol_amroth_helmet",
            LOTRToolMaterials.DOL_AMROTH_WINGED_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item DOL_AMROTH_CHESTPLATE = registerArmor("dol_amroth_chestplate",
            LOTRToolMaterials.DOL_AMROTH_WINGED_BODY_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DOL_AMROTH_LEGGINGS = registerArmor("dol_amroth_leggings",
            LOTRToolMaterials.DOL_AMROTH_ARMOR, ArmorType.LEGGINGS);
    public static final Item DOL_AMROTH_BOOTS = registerArmor("dol_amroth_boots",
            LOTRToolMaterials.DOL_AMROTH_ARMOR, ArmorType.BOOTS);
    public static final Item DOL_AMROTH_HORSE_ARMOR = registerHorseArmor("dol_amroth_horse_armor",
            LOTRToolMaterials.DOL_AMROTH_ARMOR);

    // The Morwaith: 2.0 damage, so a dagger is 3.0 and a battleaxe 8.0. The
    // SPEAR is built on MOREDAIN_SPEAR instead -- 3.0 damage, so 6.0 -- because
    // it is a gemsbok horn on a shaft rather than a blade.
    public static final Item MORWAITH_DAGGER = register("morwaith_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.MORWAITH, 0.0f));
    public static final Item POISONED_MORWAITH_DAGGER = register("poisoned_morwaith_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.MORWAITH, 0.0f));
    public static final Item MORWAITH_BATTLEAXE = register("morwaith_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.MORWAITH, 5.0f, -3.0f));
    public static final Item MORWAITH_SPEAR = register("morwaith_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.MORWAITH_SPEAR, 6.0f));
    public static final Item MORWAITH_HELMET = registerArmor("morwaith_helmet",
            LOTRToolMaterials.MORWAITH_ARMOR, ArmorType.HELMET);
    public static final Item MORWAITH_CHESTPLATE = registerArmor("morwaith_chestplate",
            LOTRToolMaterials.MORWAITH_ARMOR, ArmorType.CHESTPLATE);
    public static final Item MORWAITH_LEGGINGS = registerArmor("morwaith_leggings",
            LOTRToolMaterials.MORWAITH_ARMOR, ArmorType.LEGGINGS);
    public static final Item MORWAITH_BOOTS = registerArmor("morwaith_boots",
            LOTRToolMaterials.MORWAITH_ARMOR, ArmorType.BOOTS);

    // And the chieftain's lion hide, whose helmet is a lion's head and wears a
    // sheet of its own.
    public static final Item MORWAITH_CHIEFTAIN_HELMET = registerArmor(
            "morwaith_chieftain_helmet",
            LOTRToolMaterials.MORWAITH_CHIEFTAIN_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item MORWAITH_CHIEFTAIN_CHESTPLATE = registerArmor(
            "morwaith_chieftain_chestplate",
            LOTRToolMaterials.MORWAITH_CHIEFTAIN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item MORWAITH_CHIEFTAIN_LEGGINGS = registerArmor(
            "morwaith_chieftain_leggings",
            LOTRToolMaterials.MORWAITH_CHIEFTAIN_ARMOR, ArmorType.LEGGINGS);
    public static final Item MORWAITH_CHIEFTAIN_BOOTS = registerArmor(
            "morwaith_chieftain_boots",
            LOTRToolMaterials.MORWAITH_CHIEFTAIN_ARMOR, ArmorType.BOOTS);

    // Bone: the thinnest armour in the tab, at 1/3/2/1.
    public static final Item BONE_HELMET = registerArmor("bone_helmet",
            LOTRToolMaterials.BONE_ARMOR, ArmorType.HELMET);
    public static final Item BONE_CHESTPLATE = registerArmor("bone_chestplate",
            LOTRToolMaterials.BONE_ARMOR, ArmorType.CHESTPLATE);
    public static final Item BONE_LEGGINGS = registerArmor("bone_leggings",
            LOTRToolMaterials.BONE_ARMOR, ArmorType.LEGGINGS);
    public static final Item BONE_BOOTS = registerArmor("bone_boots",
            LOTRToolMaterials.BONE_ARMOR, ArmorType.BOOTS);

    // swordGondolin: 5.0 + 4 = 9.0, the same as a mithril sword, on 1500 uses.
    // setIsElvenBlade, left out for the reason the other elven blades leave it.
    public static final Item GONDOLIN_SWORD = register("gondolin_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.GONDOLIN, 3.0f, -2.4f));
    public static final Item GONDOLIN_HELMET = registerArmor("gondolin_helmet",
            LOTRToolMaterials.GONDOLIN_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item GONDOLIN_CHESTPLATE = registerArmor("gondolin_chestplate",
            LOTRToolMaterials.GONDOLIN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GONDOLIN_LEGGINGS = registerArmor("gondolin_leggings",
            LOTRToolMaterials.GONDOLIN_ARMOR, ArmorType.LEGGINGS);
    public static final Item GONDOLIN_BOOTS = registerArmor("gondolin_boots",
            LOTRToolMaterials.GONDOLIN_ARMOR, ArmorType.BOOTS);

    // maceMallornCharred: LOTRItemHammer(MALLORN_MACE), so 4.5 + 4 + 2 = 10.5 --
    // the hardest-hitting thing in the tab bar the mithril axes, on a material
    // that exists for this one weapon.
    public static final Item CHARRED_MALLORN_MACE = register("charred_mallorn_mace",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.CHARRED_MALLORN, 5.0f));

    public static final Item ROHIRRIC_MARSHAL_HELMET = registerArmor("rohirric_marshal_helmet",
            LOTRToolMaterials.ROHIRRIC_MARSHAL_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item ROHIRRIC_MARSHAL_CHESTPLATE = registerArmor(
            "rohirric_marshal_chestplate",
            LOTRToolMaterials.ROHIRRIC_MARSHAL_ARMOR, ArmorType.CHESTPLATE);
    public static final Item ROHIRRIC_MARSHAL_LEGGINGS = registerArmor("rohirric_marshal_leggings",
            LOTRToolMaterials.ROHIRRIC_MARSHAL_ARMOR, ArmorType.LEGGINGS);
    public static final Item ROHIRRIC_MARSHAL_BOOTS = registerArmor("rohirric_marshal_boots",
            LOTRToolMaterials.ROHIRRIC_MARSHAL_ARMOR, ArmorType.BOOTS);

    // The Taurethrim: 2.5 damage, so a sword is 6.5 and a dagger 3.5. Their
    // hammer is called a Bludgeon; it is a LOTRItemHammer like the rest.
    public static final Item TAURETHRIM_SWORD = register("taurethrim_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.TAURETHRIM, 3.0f, -2.4f));
    public static final Item TAURETHRIM_DAGGER = register("taurethrim_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.TAURETHRIM, 0.0f));
    public static final Item POISONED_TAURETHRIM_DAGGER = register("poisoned_taurethrim_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.TAURETHRIM, 0.0f));
    public static final Item TAURETHRIM_SPEAR = register("taurethrim_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.TAURETHRIM, 5.5f));
    public static final Item TAURETHRIM_BATTLEAXE = register("taurethrim_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.TAURETHRIM, 5.0f, -3.0f));
    public static final Item TAURETHRIM_BLUDGEON = register("taurethrim_bludgeon",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.TAURETHRIM, 5.0f));
    public static final Item TAURETHRIM_HELMET = registerArmor("taurethrim_helmet",
            LOTRToolMaterials.TAURETHRIM_ARMOR, ArmorType.HELMET);
    public static final Item TAURETHRIM_CHESTPLATE = registerArmor("taurethrim_chestplate",
            LOTRToolMaterials.TAURETHRIM_ARMOR, ArmorType.CHESTPLATE);
    public static final Item TAURETHRIM_LEGGINGS = registerArmor("taurethrim_leggings",
            LOTRToolMaterials.TAURETHRIM_ARMOR, ArmorType.LEGGINGS);
    public static final Item TAURETHRIM_BOOTS = registerArmor("taurethrim_boots",
            LOTRToolMaterials.TAURETHRIM_ARMOR, ArmorType.BOOTS);
    public static final Item TAURETHRIM_CHIEFTAIN_HELMET = registerArmor(
            "taurethrim_chieftain_helmet",
            LOTRToolMaterials.TAURETHRIM_CHIEFTAIN_HELMET_ARMOR, ArmorType.HELMET);

    // The POLEARM family. LOTRItemPolearm, PolearmLong, Pike and Lance all
    // extend LOTRItemSword and none of them touches the damage, so every one is
    // material + 4. What sets them apart is LOTRWeaponStats' own table: a
    // polearm swings at two thirds a sword's rate and reaches half again as
    // far; a pike or a lance at half the rate and twice the reach, and a lance
    // knocks back on top. See polearm() below.

    // poleaxeNearHarad: 2.5 + 4 = 6.5.
    public static final Item UMBARIC_POLEAXE = register("umbaric_poleaxe",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.UMBARIC, 6.5f, POLEARM_SPEED,
                    POLEARM_REACH, 0.0));

    // polearmOrc, the Mordor Warscythe: 2.5 + 4 = 6.5.
    public static final Item MORDOR_WARSCYTHE = register("mordor_warscythe",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.MORDOR, 6.5f, POLEARM_SPEED,
                    POLEARM_REACH, 0.0));

    // The three battlestaffs: 3.0 + 4 = 7.0 apiece.
    public static final Item LINDON_BATTLESTAFF = register("lindon_battlestaff",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.LINDON, 7.0f, POLEARM_SPEED,
                    POLEARM_REACH, 0.0));
    public static final Item GALADHRIM_BATTLESTAFF = register("galadhrim_battlestaff",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.GALADHRIM, 7.0f, POLEARM_SPEED,
                    POLEARM_REACH, 0.0));
    public static final Item WOOD_ELVEN_BATTLESTAFF = register("wood_elven_battlestaff",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.WOOD_ELVEN, 7.0f, POLEARM_SPEED,
                    POLEARM_REACH, 0.0));

    // pikeUruk: LOTRItemPike, so 3.0 + 4 = 7.0 on the LONG polearm's figures.
    public static final Item URUK_PIKE = register("uruk_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.URUK, 7.0f, LONG_POLEARM_SPEED,
                    LONG_POLEARM_REACH, 0.0));

    // lanceDolAmroth: 3.0 + 4 = 7.0, a pike's reach and speed, and a lance
    // alone gets registerMeleeExtraKnockback(1).
    public static final Item DOL_AMROTH_LANCE = register("dol_amroth_lance",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.DOL_AMROTH, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 1.0));

    // maceNearHarad: LOTRItemHammer(UMBAR), so 2.5 + 4 + 2 = 8.5 and a hammer's
    // swing -- which is where registerMeleeExtraKnockback(LOTRItemHammer, 1)
    // came from, and why every hammer in the port carries knockback.
    public static final Item UMBARIC_MACE = register("umbaric_mace",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.UMBARIC, 5.0f));

    // daggerBarrow: 3.0 + 4 - 3 = 4.0, and its poisoned twin.
    public static final Item BARROW_BLADE = register("barrow_blade",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.BARROW, 0.0f));
    public static final Item POISONED_BARROW_BLADE = register("poisoned_barrow_blade",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.BARROW, 0.0f));

    // LOTRItemBlowgun and its two darts.
    public static final Item TAURETHRIM_BLOWGUN = register("taurethrim_blowgun",
            LOTRBlowgunItem::new, new Item.Properties()
                    .durability(LOTRToolMaterials.TAURETHRIM.durability())
                    .repairable(LOTRItemTags.REPAIRS_TAURETHRIM_TOOLS));
    public static final Item TAURETHRIM_DART = register("taurethrim_dart",
            props -> new LOTRDartItem(false, props), new Item.Properties());
    public static final Item POISONED_TAURETHRIM_DART = register("poisoned_taurethrim_dart",
            props -> new LOTRDartItem(true, props), new Item.Properties());

    // rhinoArmorHalfTroll. A plain item for the reason the warg armours are
    // plain: a rhinoceros is not a horse and there are no rhinos in the port.
    public static final Item HALF_TROLL_RHINO_ARMOR = register("half_troll_rhino_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));

    // The Galadhrim cloak: a HITHLAIN set, hood and tunic rather than helmet
    // and chestplate.
    public static final Item GALADHRIM_CLOAK_HOOD = registerArmor("galadhrim_cloak_hood",
            LOTRToolMaterials.GALADHRIM_CLOAK_ARMOR, ArmorType.HELMET);
    public static final Item GALADHRIM_CLOAK_TUNIC = registerArmor("galadhrim_cloak_tunic",
            LOTRToolMaterials.GALADHRIM_CLOAK_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GALADHRIM_CLOAK_LEGGINGS = registerArmor("galadhrim_cloak_leggings",
            LOTRToolMaterials.GALADHRIM_CLOAK_ARMOR, ArmorType.LEGGINGS);
    public static final Item GALADHRIM_CLOAK_BOOTS = registerArmor("galadhrim_cloak_boots",
            LOTRToolMaterials.GALADHRIM_CLOAK_ARMOR, ArmorType.BOOTS);

    // boarArmorDwarven and boarArmorBlueDwarven. Plain items, as the warg and
    // rhino armours are: a boar is not a horse, and there are none in the port.
    public static final Item DWARVEN_BOAR_ARMOR = register("dwarven_boar_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));
    public static final Item BLUE_DWARVEN_BOAR_ARMOR = register("blue_dwarven_boar_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));

    // pikeHalfTroll and pikeIron: LOTRItemPike, so 2.5 + 4 = 6.5 and 2.0 + 4 =
    // 6.0, both on the long polearm's slow swing and six blocks of reach.
    public static final Item HALF_TROLL_PIKE = register("half_troll_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.HALF_TROLL, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    public static final Item IRON_PIKE = register("iron_pike",
            LOTRModifiableItem::new, polearm(ToolMaterial.IRON, 6.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // The golden Taurethrim regalia. Its own material, better armour than the
    // plain set and arming nothing; the helmet wears a sheet of its own.
    public static final Item GOLDEN_TAURETHRIM_HELMET = registerArmor("golden_taurethrim_helmet",
            LOTRToolMaterials.GOLDEN_TAURETHRIM_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item GOLDEN_TAURETHRIM_CHESTPLATE = registerArmor(
            "golden_taurethrim_chestplate",
            LOTRToolMaterials.GOLDEN_TAURETHRIM_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GOLDEN_TAURETHRIM_LEGGINGS = registerArmor(
            "golden_taurethrim_leggings",
            LOTRToolMaterials.GOLDEN_TAURETHRIM_ARMOR, ArmorType.LEGGINGS);
    public static final Item GOLDEN_TAURETHRIM_BOOTS = registerArmor("golden_taurethrim_boots",
            LOTRToolMaterials.GOLDEN_TAURETHRIM_ARMOR, ArmorType.BOOTS);

    // throwingAxeBronze and throwingAxeIron, registered together and much later
    // than the dwarven one -- LOTRMod's own order, which the creative tab keeps.
    public static final Item BRONZE_THROWING_AXE = register("bronze_throwing_axe",
            props -> new LOTRThrowingAxeItem(LOTRToolMaterials.BRONZE, props),
            throwingAxe(LOTRToolMaterials.BRONZE));

    // The one built on VANILLA iron, as the iron dagger is.
    public static final Item IRON_THROWING_AXE = register("iron_throwing_axe",
            props -> new LOTRThrowingAxeItem(ToolMaterial.IRON, props),
            throwingAxe(ToolMaterial.IRON));

    // swordMallorn: 1.5 + 4 = 5.5.
    public static final Item MALLORN_SWORD = register("mallorn_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.MALLORN, 3.0f, -2.4f));

    // swordElven: 3.0 + 4 = 7.0.
    //
    // NOT ported: setIsElvenBlade. It is purely a renderer switch -- it picked
    // LOTRRenderElvenBlade, the glow an elven blade gives off near orcs -- and
    // has no effect on the item's stats. It wants the orc NPCs it reacts to.
    public static final Item GALADHRIM_SWORD = register("galadhrim_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.GALADHRIM, 3.0f, -2.4f));

    /** Vanilla's iron spear swings once every 0.95 seconds. */
    private static final float SPEAR_PERIOD = 0.95f;

    /**
     * A spear: vanilla's spear properties, then the attack damage put back.
     *
     * @param damage what the tooltip should read, in the same terms as the
     *               swords -- the original's lotrWeaponDamage.
     */
    private static Item.Properties spear(ToolMaterial material, float damage) {
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

    /** A hammer swings 0.85 times a second, where a battleaxe manages 1.0. */
    private static final double WARHAMMER_ATTACK_SPEED = 0.85;

    /** And puts its weight behind the blow: one whole knockback level. */
    private static final double WARHAMMER_KNOCKBACK = 1.0;

    /**
     * A hammer: an axe's properties, slowed down, with knockback added.
     *
     * <p>Every hammer in the mod goes through here -- the five warhammers and
     * the blacksmith's, which is LOTRItemHammer(GONDOR) like the Gondorian one
     * and swings the same way.
     *
     * <p>NOT the original's figures, and deliberately so. 1.7.10 had neither an
     * attack-speed concept nor a knockback attribute, so LOTRItemHammer was a
     * plain sword-alike hitting for material + 6 and nothing else; a hammer and
     * a battleaxe of the same metal were identical. These two numbers are the
     * port's own balance choice -- slower than a battleaxe, and it sends what it
     * hits flying, which is what separates the two weapons now.
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
    private static Item.Properties warhammer(ToolMaterial material, float damage) {
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
    private static Item.Properties throwingAxe(ToolMaterial material) {
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
    private static Item.Properties bow(ToolMaterial material,
            net.minecraft.tags.TagKey<Item> repairs) {
        return new Item.Properties()
                .durability((int) (material.durability() * 1.5f))
                .enchantable(1 + material.enchantmentValue() / 5)
                .repairable(repairs);
    }

    /** setMaxStackSize(1), and vanilla's own barding properties for the rest. */
    private static Item registerHorseArmor(String name, ArmorMaterial material) {
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
    private static Item.Properties polearm(ToolMaterial material, float damage,
            double attackSpeed, double reach, double knockback) {
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
        return new Item.Properties()
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue())
                .attributes(attributes.build())
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    private static Item registerArmor(String name, ArmorMaterial material, ArmorType type) {
        return register(name, LOTRModifiableItem::new, new Item.Properties().humanoidArmor(material, type));
    }

    /**
     * One of the two forms a treasure pile is carried in -- the two-pixel carpet
     * at one layer, or the full block at eight. Called from LOTRBlocks while the
     * piles are being built; see registerTreasurePile for why the order of the
     * two calls matters.
     */
    public static Item registerTreasurePileItem(Block pile, String name, int layers) {
        return register(name,
                props -> new LOTRTreasurePileItem(pile, layers, props),
                new Item.Properties());
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

    // Force class-load so the static fields register. Called from mod init. */
    public static void init() {
    }
}