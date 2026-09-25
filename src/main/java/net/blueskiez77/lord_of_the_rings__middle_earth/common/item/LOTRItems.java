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
    // LOTRItemKebab: one eating in a hundred tells the player it was a good kebab.
    public static final Item KEBAB = register("kebab",
            LOTRKebabItem::new, new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).build()));

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

    /**
     * Daggers swing twice a second and reach two and a half blocks. A deliberate
     * choice over LOTRWeaponStats' factors (1.5 and 0.75 of the sword, which
     * would be 2.4 and 2.25).
     */
    private static final double DAGGER_ATTACK_SPEED = 2.0;
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
    // registerMeleeSpeed(LOTRItemBattleaxe.class, 0.75f), as a modifier on the base 4.
    private static final float BATTLEAXE_SPEED = (float) (1.6 * 0.75 - 4.0);
    private static final double POLEARM_REACH = 3.0 * 1.5;
    private static final double LONG_POLEARM_SPEED = 1.6 * 0.5;
    private static final double LONG_POLEARM_REACH = 3.0 * 2.0;

    /** Up here for the same reason DAGGER_REACH_ID is. See the note above. */
    private static final Identifier POLEARM_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "polearm_reach");

    /** And this one. */
    private static final Identifier POLEARM_KNOCKBACK_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "polearm_knockback");

    /** And this one: LOTRItemLance.lanceSpeedBoost. */
    private static final Identifier LANCE_MOVEMENT_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "lance_movement");

    /**
     * lanceSpeedBoost: {@code new AttributeModifier(..., -0.2, 2)}, operation 2
     * being a multiplier on the total -- a lance slows its bearer by a fifth.
     */
    private static final double LANCE_MOVEMENT_PENALTY = -0.2;

    /** Up here for the same reason DAGGER_REACH_ID is. The balrog whip's reach. */
    private static final Identifier WHIP_REACH_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "whip_reach");

    /** registerMeleeReach(LOTRItemBalrogWhip.class, 1.5f): a polearm's reach. */
    private static final double WHIP_REACH = 3.0 * 1.5;

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
    // Attack speed: LOTRWeaponStats gave each class a melee-speed factor over
    // the sword (dagger 1.5, spear 0.833, battleaxe 0.75, hammer and polearm
    // 0.667, long polearm and lance 0.5) and a reach factor over three blocks
    // (dagger 0.75, spear and polearm 1.5, long polearm and lance 2.0). The
    // sword keeps vanilla's 1.6 swings a second and the factors apply to that,
    // except that the dagger (2.0/s, 2.5 blocks) and hammer (0.85/s) keep the
    // port's own figures by choice, and the spear and trident are vanilla's.
    // The attribute's base is 4.0, so the modifier is (wanted speed - 4).

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
                    .axe(LOTRToolMaterials.MORDOR, 5.0f, BATTLEAXE_SPEED));

    // daggerOrc: LOTRItemDagger takes 3 off, so 2.5 + 4 - 3 = 3.5.
    public static final Item MORDOR_DAGGER = register("mordor_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.MORDOR, 0.0f));

    // daggerOrcPoisoned: the same dagger, whose cut festers.
    public static final Item POISONED_MORDOR_DAGGER = register("poisoned_mordor_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.MORDOR, 0.0f));

    // LOTRItemSting: a Lindon dagger with a point of damage on top and Bilbo's
    // seven hundred uses. Its one trick is cutting webs -- the original gave it
    // speed 15 on Ungoliant's web as well as on the vanilla one.
    public static final Item STING = register("sting", LOTRModifiableItem::new,
            dagger(LOTRToolMaterials.LINDON, 1.0f)
                    .component(DataComponents.TOOL, new net.minecraft.world.item.component.Tool(
                            java.util.List.of(net.minecraft.world.item.component.Tool.Rule.minesAndDrops(
                                    net.minecraft.core.HolderSet.direct(
                                            net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(net.minecraft.world.level.block.Blocks.COBWEB),
                                            net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.WEB_UNGOLIANT)),
                                    15.0f)),
                            1.0f, 2, false)));

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

    // LOTRItemAnduril: iron, but the Flame of the West all the same -- 9 damage
    // and 1500 uses.
    public static final Item ANDURIL = register("anduril", LOTRModifiableItem::new,
            new Item.Properties().sword(net.minecraft.world.item.ToolMaterial.IRON, 6.0f, -2.4f).durability(1500));

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
                    .axe(LOTRToolMaterials.DWARVEN, 5.0f, BATTLEAXE_SPEED));

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
                    .axe(LOTRToolMaterials.MITHRIL, 5.0f, BATTLEAXE_SPEED));
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
                    .axe(LOTRToolMaterials.URUK, 5.0f, BATTLEAXE_SPEED));
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
            LOTRCrossbowItem.properties(LOTRToolMaterials.URUK, LOTRItemTags.REPAIRS_URUK_CROSSBOW));

    // Built on VANILLA iron, as the iron dagger and the iron throwing axe are.
    public static final Item IRON_CROSSBOW = register("iron_crossbow",
            props -> new LOTRCrossbowItem(ToolMaterial.IRON, props),
            LOTRCrossbowItem.properties(ToolMaterial.IRON, LOTRItemTags.REPAIRS_IRON_CROSSBOW));

    public static final Item MITHRIL_CROSSBOW = register("mithril_crossbow",
            props -> new LOTRCrossbowItem(LOTRToolMaterials.MITHRIL, props),
            LOTRCrossbowItem.properties(LOTRToolMaterials.MITHRIL, LOTRItemTags.REPAIRS_MITHRIL_CROSSBOW));

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

    // morgulBlade: an LOTRItemSword, 2.5 + 4 = 6.5, that withers what it cuts.
    public static final Item MORGUL_BLADE = register("morgul_blade",
            LOTRMorgulBladeItem::new, new Item.Properties()
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
                    .axe(LOTRToolMaterials.ANGMAR, 5.0f, BATTLEAXE_SPEED));

    // hammerAngmar: also 8.5, and a hammer, so it swings at 0.85 and knocks back like one.
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
                    .axe(LOTRToolMaterials.ROHIRRIC, 5.0f, BATTLEAXE_SPEED));

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
                    .axe(LOTRToolMaterials.BLUE_DWARVEN, 5.0f, BATTLEAXE_SPEED));
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
    // horseArmorDiamond: the mod's own diamond barding (see DIAMOND_HORSE_ARMOR).
    public static final Item DIAMOND_HORSE_ARMOR = registerHorseArmor("diamond_horse_armor",
            LOTRToolMaterials.DIAMOND_HORSE_ARMOR);
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
                    .axe(LOTRToolMaterials.DOL_GULDUR, 5.0f, BATTLEAXE_SPEED));
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
                    .axe(LOTRToolMaterials.UTUMNO, 5.0f, BATTLEAXE_SPEED));
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
                    .axe(LOTRToolMaterials.BLACK_URUK, 5.0f, BATTLEAXE_SPEED));
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
                    .repairable(LOTRItemTags.REPAIRS_BALROG_WHIP)
                    // getItemEnchantability() returned 0; sword() set Utumno's 12.
                    .component(DataComponents.ENCHANTABLE, null)
                    // .sword's damage and speed again, plus the whip's reach;
                    // attributes() replaces the component sword() set.
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                                            LOTRBalrogWhipItem.ATTACK_DAMAGE - 1.0,
                                            AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4,
                                            AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ENTITY_INTERACTION_RANGE,
                                    new AttributeModifier(WHIP_REACH_ID, WHIP_REACH - 3.0,
                                            AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .build()));

    // battleaxeIron: LOTRItemBattleaxe on VANILLA iron, so 2.0 + 4 + 2 = 8.0.
    public static final Item IRON_BATTLEAXE = register("iron_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(ToolMaterial.IRON, 5.0f, BATTLEAXE_SPEED));

    // battleaxeBronze: 1.5 + 4 + 2 = 7.5.
    public static final Item BRONZE_BATTLEAXE = register("bronze_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.BRONZE, 5.0f, BATTLEAXE_SPEED));

    // bronzeCrossbow: LOTRItemCrossbow(BRONZE). Bronze damage is 1.5, so
    // boltDamageFactor is 1.0 -- the expression subtracts two and clamps at
    // zero -- and this is the plainest crossbow of the four.
    public static final Item BRONZE_CROSSBOW = register("bronze_crossbow",
            props -> new LOTRCrossbowItem(LOTRToolMaterials.BRONZE, props),
            LOTRCrossbowItem.properties(LOTRToolMaterials.BRONZE, LOTRItemTags.REPAIRS_BRONZE_CROSSBOW));

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
                    .axe(LOTRToolMaterials.HALF_TROLL, 5.0f, BATTLEAXE_SPEED));
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
                    .axe(LOTRToolMaterials.MORWAITH, 5.0f, BATTLEAXE_SPEED));
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
                    .axe(LOTRToolMaterials.TAURETHRIM, 5.0f, BATTLEAXE_SPEED));
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
            LOTRModifiableItem::new, lance(LOTRToolMaterials.DOL_AMROTH, 7.0f));

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
                    .repairable(LOTRItemTags.REPAIRS_BLOWGUN));
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

    // pikeDwarven and pikeBlueDwarven: LOTRItemPike, so 3.0 + 4 = 7.0 apiece on
    // the long polearm's figures.
    public static final Item DWARVEN_PIKE = register("dwarven_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.DWARVEN, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    public static final Item BLUE_DWARVEN_PIKE = register("blue_dwarven_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.BLUE_DWARVEN, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // daggerDolAmroth: 3.0 + 4 - 3 = 4.0, and its poisoned twin.
    public static final Item DOL_AMROTH_DAGGER = register("dol_amroth_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.DOL_AMROTH, 0.0f));
    public static final Item POISONED_DOL_AMROTH_DAGGER = register("poisoned_dol_amroth_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.DOL_AMROTH, 0.0f));

    // The Gundabad Uruk armour. The helmet wears a sheet of its own -- extraName
    // "helmet" -- and a horned model; see LOTRArmorRenderers.
    public static final Item GUNDABAD_URUK_HELMET = registerArmor("gundabad_uruk_helmet",
            LOTRToolMaterials.GUNDABAD_URUK_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item GUNDABAD_URUK_CHESTPLATE = registerArmor("gundabad_uruk_chestplate",
            LOTRToolMaterials.GUNDABAD_URUK_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GUNDABAD_URUK_LEGGINGS = registerArmor("gundabad_uruk_leggings",
            LOTRToolMaterials.GUNDABAD_URUK_ARMOR, ArmorType.LEGGINGS);
    public static final Item GUNDABAD_URUK_BOOTS = registerArmor("gundabad_uruk_boots",
            LOTRToolMaterials.GUNDABAD_URUK_ARMOR, ArmorType.BOOTS);

    // lanceGondor: 2.5 + 4 = 6.5, and a lance's reach, swing and knockback.
    public static final Item GONDOR_LANCE = register("gondor_lance",
            LOTRModifiableItem::new, lance(LOTRToolMaterials.GONDOR, 6.5f));

    // The Gundabad Uruk weapons, which the original names a Cleaver, a Waraxe
    // and a Bludgeon: 3.0 damage, so the Isengard Uruks' figures throughout.
    public static final Item GUNDABAD_URUK_CLEAVER = register("gundabad_uruk_cleaver",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.GUNDABAD_URUK, 3.0f, -2.4f));
    public static final Item GUNDABAD_URUK_WARAXE = register("gundabad_uruk_waraxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.GUNDABAD_URUK, 5.0f, BATTLEAXE_SPEED));
    public static final Item GUNDABAD_URUK_BLUDGEON = register("gundabad_uruk_bludgeon",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.GUNDABAD_URUK, 5.0f));

    // helmetUrukBerserker and scimitarUrukBerserker, both on URUK. The helmet
    // is the Uruk model on a sheet of its own; the cleaver's addWeaponDamage(0)
    // leaves it the Uruk Cleaver's 3.0 + 4 = 7.0.
    public static final Item URUK_BERSERKER_HELMET = registerArmor("uruk_berserker_helmet",
            LOTRToolMaterials.URUK_BERSERKER_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item URUK_BERSERKER_CLEAVER = register("uruk_berserker_cleaver",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.URUK, 3.0f, -2.4f));

    // lanceRohan: 2.5 + 4 = 6.5.
    public static final Item ROHIRRIC_LANCE = register("rohirric_lance",
            LOTRModifiableItem::new, lance(LOTRToolMaterials.ROHIRRIC, 6.5f));

    // longspearElven, longspearHighElven and longspearWoodElven: plain
    // LOTRItemPolearmLong, so 3.0 + 4 = 7.0 apiece on the long polearm's figures.
    public static final Item GALADHRIM_LONGSPEAR = register("galadhrim_longspear",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.GALADHRIM, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    public static final Item LINDON_LONGSPEAR = register("lindon_longspear",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.LINDON, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    public static final Item WOOD_ELVEN_LONGSPEAR = register("wood_elven_longspear",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.WOOD_ELVEN, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // LOTRItemRingil: Fingolfin's sword, Lindon steel at 9 damage and 1500 uses.
    public static final Item RINGIL = register("ringil", LOTRModifiableItem::new,
            new Item.Properties().sword(LOTRToolMaterials.LINDON, 5.0f, -2.4f).durability(1500));

    // halberdMithril: LOTRItemPolearmLong(MITHRIL), so 5.0 + 4 = 9.0.
    public static final Item MITHRIL_HALBERD = register("mithril_halberd",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.MITHRIL, 9.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // The Dale set: 2.5 damage, so a 6.5 sword, a 3.5 dagger and an 8.5 axe.
    public static final Item DALE_SWORD = register("dale_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.DALE, 3.0f, -2.4f));
    public static final Item DALE_DAGGER = register("dale_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.DALE, 0.0f));
    public static final Item POISONED_DALE_DAGGER = register("poisoned_dale_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.DALE, 0.0f));
    // spearDale: 2.5 + 4 - 1 = 5.5.
    public static final Item DALE_SPEAR = register("dale_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.DALE, 5.5f));
    public static final Item DALE_BATTLEAXE = register("dale_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.DALE, 5.0f, BATTLEAXE_SPEED));
    // No extraName on the helmet, so it wears the set's own sheet.
    public static final Item DALE_HELMET = registerArmor("dale_helmet",
            LOTRToolMaterials.DALE_ARMOR, ArmorType.HELMET);
    public static final Item DALE_CHESTPLATE = registerArmor("dale_chestplate",
            LOTRToolMaterials.DALE_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DALE_LEGGINGS = registerArmor("dale_leggings",
            LOTRToolMaterials.DALE_ARMOR, ArmorType.LEGGINGS);
    public static final Item DALE_BOOTS = registerArmor("dale_boots",
            LOTRToolMaterials.DALE_ARMOR, ArmorType.BOOTS);

    // The Dorwinion sets. The men's helmet wears the set's sheet; the elves'
    // has a sheet and a crested model of its own -- see LOTRArmorRenderers.
    public static final Item DORWINION_HELMET = registerArmor("dorwinion_helmet",
            LOTRToolMaterials.DORWINION_ARMOR, ArmorType.HELMET);
    public static final Item DORWINION_CHESTPLATE = registerArmor("dorwinion_chestplate",
            LOTRToolMaterials.DORWINION_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DORWINION_LEGGINGS = registerArmor("dorwinion_leggings",
            LOTRToolMaterials.DORWINION_ARMOR, ArmorType.LEGGINGS);
    public static final Item DORWINION_BOOTS = registerArmor("dorwinion_boots",
            LOTRToolMaterials.DORWINION_ARMOR, ArmorType.BOOTS);
    public static final Item DORWINION_ELVEN_HELMET = registerArmor("dorwinion_elven_helmet",
            LOTRToolMaterials.DORWINION_ELVEN_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item DORWINION_ELVEN_CHESTPLATE = registerArmor(
            "dorwinion_elven_chestplate",
            LOTRToolMaterials.DORWINION_ELVEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DORWINION_ELVEN_LEGGINGS = registerArmor("dorwinion_elven_leggings",
            LOTRToolMaterials.DORWINION_ELVEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item DORWINION_ELVEN_BOOTS = registerArmor("dorwinion_elven_boots",
            LOTRToolMaterials.DORWINION_ELVEN_ARMOR, ArmorType.BOOTS);

    // spearBladorthin, the Spear of Bladorthin: LOTRItemSpear, 3.0 + 4 - 1 = 6.0.
    public static final Item BLADORTHIN_SPEAR = register("bladorthin_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.BLADORTHIN, 6.0f));

    // daleBow, the Dalish Longbow: new LOTRItemBow(DALE, 1.25).setDrawTime(30).
    public static final Item DALE_BOW = register("dale_bow",
            props -> new LOTRBowItem(30, 1.25f, props),
            bow(LOTRToolMaterials.DALE, LOTRItemTags.REPAIRS_DALE_BOW));

    public static final Item DALE_HORSE_ARMOR = registerHorseArmor("dale_horse_armor",
            LOTRToolMaterials.DALE_ARMOR);

    // The Ithilien rangers' set: a hood and tunic like the northern rangers'.
    public static final Item ITHILIEN_RANGER_HOOD = registerArmor("ithilien_ranger_hood",
            LOTRToolMaterials.RANGER_ITHILIEN_ARMOR, ArmorType.HELMET);
    public static final Item ITHILIEN_RANGER_TUNIC = registerArmor("ithilien_ranger_tunic",
            LOTRToolMaterials.RANGER_ITHILIEN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item ITHILIEN_RANGER_LEGGINGS = registerArmor("ithilien_ranger_leggings",
            LOTRToolMaterials.RANGER_ITHILIEN_ARMOR, ArmorType.LEGGINGS);
    public static final Item ITHILIEN_RANGER_BOOTS = registerArmor("ithilien_ranger_boots",
            LOTRToolMaterials.RANGER_ITHILIEN_ARMOR, ArmorType.BOOTS);

    // LOTRMod registers the rest of the set a long way further on, after the
    // Ithilien rangers' gear; none of what lies between is ported yet.
    public static final Item GUNDABAD_URUK_DAGGER = register("gundabad_uruk_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.GUNDABAD_URUK, 0.0f));
    public static final Item POISONED_GUNDABAD_URUK_DAGGER = register(
            "poisoned_gundabad_uruk_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.GUNDABAD_URUK, 0.0f));
    // spearGundabadUruk: 3.0 + 4 - 1 = 6.0.
    public static final Item GUNDABAD_URUK_SPEAR = register("gundabad_uruk_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.GUNDABAD_URUK, 6.0f));
    public static final Item GUNDABAD_URUK_PIKE = register("gundabad_uruk_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.GUNDABAD_URUK, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    // gundabadUrukBow: new LOTRItemBow(GUNDABAD_URUK, 1.2).setDrawTime(30) --
    // slow to draw, and it shoots a little harder than a plain bow.
    public static final Item GUNDABAD_URUK_BOW = register("gundabad_uruk_bow",
            props -> new LOTRBowItem(30, 1.2f, props),
            bow(LOTRToolMaterials.GUNDABAD_URUK, LOTRItemTags.REPAIRS_GUNDABAD_URUK_BOW));

    // swordDorwinionElf and its daggers: DORWINION_ELF, 3.0 damage. Elven blades
    // in the original; the glow is not ported for any blade yet.
    public static final Item DORWINION_ELVEN_SWORD = register("dorwinion_elven_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.DORWINION_ELVEN, 3.0f, -2.4f));
    public static final Item DORWINION_ELVEN_DAGGER = register("dorwinion_elven_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.DORWINION_ELVEN, 0.0f));
    public static final Item POISONED_DORWINION_ELVEN_DAGGER = register(
            "poisoned_dorwinion_elven_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.DORWINION_ELVEN, 0.0f));

    // pikeTauredain: LOTRItemPike, 2.5 + 4 = 6.5.
    public static final Item TAURETHRIM_PIKE = register("taurethrim_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.TAURETHRIM, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // clubMoredain, the Morwaith Club: LOTRItemHammer(MOREDAIN_WOOD), 2.0 + 4 + 2 = 8.0.
    public static final Item MORWAITH_CLUB = register("morwaith_club",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.MORWAITH_WOOD, 5.0f));

    // pikeDale, the Dalish Pitchfork: LOTRItemPike, so 2.5 + 4 = 6.5.
    public static final Item DALE_PITCHFORK = register("dale_pitchfork",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.DALE, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // rollingPin: LOTRItemSword on VANILLA wood, 0.0 + 4 = 4.0.
    public static final Item ROLLING_PIN = register("rolling_pin",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(ToolMaterial.WOOD, 3.0f, -2.4f));

    // polearmAngmar, the Angmar Poleaxe: LOTRItemPolearm, 2.5 + 4 = 6.5.
    public static final Item ANGMAR_POLEAXE = register("angmar_poleaxe",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.ANGMAR, 6.5f, POLEARM_SPEED,
                    POLEARM_REACH, 0.0));

    // pikeDolGuldur, the Dol Guldur Spike, and pikeNearHarad: LOTRItemPike, 6.5 apiece.
    public static final Item DOL_GULDUR_SPIKE = register("dol_guldur_spike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.DOL_GULDUR, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    public static final Item UMBARIC_PIKE = register("umbaric_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.UMBARIC, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // The fiefdoms of Gondor: Lossarnach, Pelargir and Pinnath Gelin armour,
    // each on the set's own sheet.
    public static final Item LOSSARNACH_HELMET = registerArmor("lossarnach_helmet",
            LOTRToolMaterials.LOSSARNACH_ARMOR, ArmorType.HELMET);
    public static final Item LOSSARNACH_CHESTPLATE = registerArmor("lossarnach_chestplate",
            LOTRToolMaterials.LOSSARNACH_ARMOR, ArmorType.CHESTPLATE);
    public static final Item LOSSARNACH_LEGGINGS = registerArmor("lossarnach_leggings",
            LOTRToolMaterials.LOSSARNACH_ARMOR, ArmorType.LEGGINGS);
    public static final Item LOSSARNACH_BOOTS = registerArmor("lossarnach_boots",
            LOTRToolMaterials.LOSSARNACH_ARMOR, ArmorType.BOOTS);
    public static final Item PELARGIR_HELMET = registerArmor("pelargir_helmet",
            LOTRToolMaterials.PELARGIR_ARMOR, ArmorType.HELMET);
    public static final Item PELARGIR_CHESTPLATE = registerArmor("pelargir_chestplate",
            LOTRToolMaterials.PELARGIR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item PELARGIR_LEGGINGS = registerArmor("pelargir_leggings",
            LOTRToolMaterials.PELARGIR_ARMOR, ArmorType.LEGGINGS);
    public static final Item PELARGIR_BOOTS = registerArmor("pelargir_boots",
            LOTRToolMaterials.PELARGIR_ARMOR, ArmorType.BOOTS);
    public static final Item PINNATH_GELIN_HELMET = registerArmor("pinnath_gelin_helmet",
            LOTRToolMaterials.PINNATH_GELIN_ARMOR, ArmorType.HELMET);
    public static final Item PINNATH_GELIN_CHESTPLATE = registerArmor("pinnath_gelin_chestplate",
            LOTRToolMaterials.PINNATH_GELIN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item PINNATH_GELIN_LEGGINGS = registerArmor("pinnath_gelin_leggings",
            LOTRToolMaterials.PINNATH_GELIN_ARMOR, ArmorType.LEGGINGS);
    public static final Item PINNATH_GELIN_BOOTS = registerArmor("pinnath_gelin_boots",
            LOTRToolMaterials.PINNATH_GELIN_ARMOR, ArmorType.BOOTS);

    // battleaxeLossarnach: 2.5 + 4 + 2 = 8.5.
    public static final Item LOSSARNACH_BATTLEAXE = register("lossarnach_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.LOSSARNACH, 5.0f, BATTLEAXE_SPEED));
    public static final Item LOSSARNACH_THROWING_AXE = register("lossarnach_throwing_axe",
            props -> new LOTRThrowingAxeItem(LOTRToolMaterials.LOSSARNACH, props),
            throwingAxe(LOTRToolMaterials.LOSSARNACH));

    // swordPelargir, the Pelargir Eket: 2.5 + 4 = 6.5.
    public static final Item PELARGIR_EKET = register("pelargir_eket",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.PELARGIR, 3.0f, -2.4f));

    // tridentPelargir: LOTRItemTrident on PELARGIR, 2.5 + 4 = 6.5, built the way
    // the Dunlending trident is; see LOTRTridentItem.
    public static final Item PELARGIR_TRIDENT = register("pelargir_trident",
            LOTRTridentItem::new, LOTRTridentItem.properties(LOTRToolMaterials.PELARGIR, 6.5f));

    // Blackroot Vale armour, and blackrootBow: a plain LOTRItemBow -- 20-tick
    // draw, ordinary arrow speed.
    public static final Item BLACKROOT_VALE_HELMET = registerArmor("blackroot_vale_helmet",
            LOTRToolMaterials.BLACKROOT_ARMOR, ArmorType.HELMET);
    public static final Item BLACKROOT_VALE_CHESTPLATE = registerArmor("blackroot_vale_chestplate",
            LOTRToolMaterials.BLACKROOT_ARMOR, ArmorType.CHESTPLATE);
    public static final Item BLACKROOT_VALE_LEGGINGS = registerArmor("blackroot_vale_leggings",
            LOTRToolMaterials.BLACKROOT_ARMOR, ArmorType.LEGGINGS);
    public static final Item BLACKROOT_VALE_BOOTS = registerArmor("blackroot_vale_boots",
            LOTRToolMaterials.BLACKROOT_ARMOR, ArmorType.BOOTS);
    // blackrootBow: setDrawTime(16).
    public static final Item BLACKROOT_BOW = register("blackroot_bow",
            props -> new LOTRBowItem(16, 1.0f, props),
            bow(LOTRToolMaterials.BLACKROOT, LOTRItemTags.REPAIRS_BLACKROOT_BOW));

    // pikeGondor: 2.5 + 4 = 6.5.
    public static final Item GONDOR_PIKE = register("gondor_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.GONDOR, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // The Dol Amroth gambeson and chaps: GAMBESON, each on its own sheet.
    public static final Item DOL_AMROTH_GAMBESON = registerArmor("dol_amroth_gambeson",
            LOTRToolMaterials.GAMBESON_DOL_AMROTH_ARMOR, ArmorType.CHESTPLATE);
    public static final Item DOL_AMROTH_CHAPS = registerArmor("dol_amroth_chaps",
            LOTRToolMaterials.GAMBESON_DOL_AMROTH_LEGS_ARMOR, ArmorType.LEGGINGS);

    // longspearDolAmroth: LOTRItemPolearmLong, 3.0 + 4 = 7.0.
    public static final Item DOL_AMROTH_LONGSPEAR = register("dol_amroth_longspear",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.DOL_AMROTH, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    public static final Item GONDOR_GAMBESON = registerArmor("gondor_gambeson",
            LOTRToolMaterials.GAMBESON_GONDOR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item LEBENNIN_GAMBESON = registerArmor("lebennin_gambeson",
            LOTRToolMaterials.GAMBESON_LEBENNIN_ARMOR, ArmorType.CHESTPLATE);

    // spearStone: LOTRItemSpear on VANILLA stone, 1.0 + 4 - 1 = 4.0.
    public static final Item STONE_SPEAR = register("stone_spear",
            LOTRModifiableItem::new, spear(ToolMaterial.STONE, 4.0f));

    // The Lamedon set, its barding, and the Lamedon jacket (JACKET, "lamedon").
    public static final Item LAMEDON_HELMET = registerArmor("lamedon_helmet",
            LOTRToolMaterials.LAMEDON_ARMOR, ArmorType.HELMET);
    public static final Item LAMEDON_CHESTPLATE = registerArmor("lamedon_chestplate",
            LOTRToolMaterials.LAMEDON_ARMOR, ArmorType.CHESTPLATE);
    public static final Item LAMEDON_LEGGINGS = registerArmor("lamedon_leggings",
            LOTRToolMaterials.LAMEDON_ARMOR, ArmorType.LEGGINGS);
    public static final Item LAMEDON_BOOTS = registerArmor("lamedon_boots",
            LOTRToolMaterials.LAMEDON_ARMOR, ArmorType.BOOTS);
    public static final Item LAMEDON_HORSE_ARMOR = registerHorseArmor("lamedon_horse_armor",
            LOTRToolMaterials.LAMEDON_ARMOR);
    public static final Item LAMEDON_JACKET = registerArmor("lamedon_jacket",
            LOTRToolMaterials.LAMEDON_JACKET_ARMOR, ArmorType.CHESTPLATE);

    // ---- The rest of tabCombat, top to bottom. ----
    //
    // The first few fill gaps earlier in the tab (the creative tab lists them in
    // place); from the Dalish gambeson on they follow LOTRMod straight through
    // to the Black Numenorean mace, the last item tabCombat held.

    // spearIron: LOTRItemSpear on VANILLA iron, 2.0 + 4 - 1 = 5.0.
    public static final Item IRON_SPEAR = register("iron_spear",
            LOTRModifiableItem::new, spear(ToolMaterial.IRON, 5.0f));

    // swordAngmar and its daggers: 2.5 damage.
    public static final Item ANGMAR_SWORD = register("angmar_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.ANGMAR, 3.0f, -2.4f));
    public static final Item ANGMAR_DAGGER = register("angmar_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.ANGMAR, 0.0f));
    public static final Item POISONED_ANGMAR_DAGGER = register("poisoned_angmar_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.ANGMAR, 0.0f));

    // elkArmorWoodElven. A plain item for the reason the warg armours are plain:
    // there are no elks in the port to wear it.
    public static final Item WOOD_ELVEN_ELK_ARMOR = register("wood_elven_elk_armor",
            LOTRModifiableItem::new, new Item.Properties().stacksTo(1));

    // blackUrukBow: LOTRItemBow(BLACK_URUK, 1.25).setDrawTime(30).
    public static final Item BLACK_URUK_BOW = register("black_uruk_bow",
            props -> new LOTRBowItem(30, 1.25f, props), bow(LOTRToolMaterials.BLACK_URUK, LOTRItemTags.REPAIRS_BLACK_URUK_BOW));

    // helmetNearHaradWarlord, the Southron Champion Helmet: its own plumed model.
    public static final Item SOUTHRON_CHAMPION_HELMET = registerArmor("southron_champion_helmet",
            LOTRToolMaterials.COAST_SOUTHRON_CHAMPION_HELMET_ARMOR, ArmorType.HELMET);

    // utumnoBow: LOTRItemBow(UTUMNO, 1.25), the default 20-tick draw.
    public static final Item UTUMNO_BOW = register("utumno_bow",
            props -> new LOTRBowItem(20, 1.25f, props), bow(LOTRToolMaterials.UTUMNO, LOTRItemTags.REPAIRS_UTUMNO_BOW));

    // bodyDaleGambeson: GAMBESON, "dale".
    public static final Item DALE_GAMBESON = registerArmor("dale_gambeson",
            LOTRToolMaterials.GAMBESON_DALE_ARMOR, ArmorType.CHESTPLATE);

    // The Arnorian set; the helmet has its winged model and a sheet of its own.
    public static final Item ARNOR_HELMET = registerArmor("arnor_helmet",
            LOTRToolMaterials.ARNOR_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item ARNOR_CHESTPLATE = registerArmor("arnor_chestplate",
            LOTRToolMaterials.ARNOR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item ARNOR_LEGGINGS = registerArmor("arnor_leggings",
            LOTRToolMaterials.ARNOR_ARMOR, ArmorType.LEGGINGS);
    public static final Item ARNOR_BOOTS = registerArmor("arnor_boots",
            LOTRToolMaterials.ARNOR_ARMOR, ArmorType.BOOTS);

    // rangerBow: a plain LOTRItemBow on RANGER.
    public static final Item RANGER_BOW = register("ranger_bow",
            props -> new LOTRBowItem(20, 1.0f, props), bow(LOTRToolMaterials.RANGER, LOTRItemTags.REPAIRS_RANGER_BOW));

    // The Rhunic set: 2.5 damage. polearmRhun is the Bardiche.
    public static final Item RHUNIC_SWORD = register("rhunic_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.RHUN, 3.0f, -2.4f));
    public static final Item RHUNIC_DAGGER = register("rhunic_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.RHUN, 0.0f));
    public static final Item POISONED_RHUNIC_DAGGER = register("poisoned_rhunic_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.RHUN, 0.0f));
    public static final Item RHUNIC_SPEAR = register("rhunic_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.RHUN, 5.5f));
    public static final Item RHUNIC_BARDICHE = register("rhunic_bardiche",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.RHUN, 6.5f,
                    POLEARM_SPEED, POLEARM_REACH, 0.0));
    public static final Item RHUNIC_PIKE = register("rhunic_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.RHUN, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));
    public static final Item RHUNIC_HELMET = registerArmor("rhunic_helmet",
            LOTRToolMaterials.RHUN_ARMOR, ArmorType.HELMET);
    public static final Item RHUNIC_CHESTPLATE = registerArmor("rhunic_chestplate",
            LOTRToolMaterials.RHUN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item RHUNIC_LEGGINGS = registerArmor("rhunic_leggings",
            LOTRToolMaterials.RHUN_ARMOR, ArmorType.LEGGINGS);
    public static final Item RHUNIC_BOOTS = registerArmor("rhunic_boots",
            LOTRToolMaterials.RHUN_ARMOR, ArmorType.BOOTS);
    // rhunBow: LOTRItemBow(RHUN).setDrawTime(16).
    public static final Item RHUNIC_BOW = register("rhunic_bow",
            props -> new LOTRBowItem(16, 1.0f, props), bow(LOTRToolMaterials.RHUN, LOTRItemTags.REPAIRS_RHUN_BOW));
    public static final Item RHUNIC_HORSE_ARMOR = registerHorseArmor("rhunic_horse_armor",
            LOTRToolMaterials.RHUN_GOLD_ARMOR);

    // The golden Rhunic set (RHUN_GOLD) and the warlord's kine-horned helmet.
    public static final Item GOLDEN_RHUNIC_HELMET = registerArmor("golden_rhunic_helmet",
            LOTRToolMaterials.RHUN_GOLD_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item GOLDEN_RHUNIC_CHESTPLATE = registerArmor("golden_rhunic_chestplate",
            LOTRToolMaterials.RHUN_GOLD_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GOLDEN_RHUNIC_LEGGINGS = registerArmor("golden_rhunic_leggings",
            LOTRToolMaterials.RHUN_GOLD_ARMOR, ArmorType.LEGGINGS);
    public static final Item GOLDEN_RHUNIC_BOOTS = registerArmor("golden_rhunic_boots",
            LOTRToolMaterials.RHUN_GOLD_ARMOR, ArmorType.BOOTS);
    public static final Item RHUNIC_WARLORD_HELMET = registerArmor("rhunic_warlord_helmet",
            LOTRToolMaterials.RHUN_WARLORD_HELMET_ARMOR, ArmorType.HELMET);

    // dorwinionElfBow: LOTRItemBow(DORWINION_ELF, 1.2).
    public static final Item DORWINION_ELVEN_BOW = register("dorwinion_elven_bow",
            props -> new LOTRBowItem(20, 1.2f, props), bow(LOTRToolMaterials.DORWINION_ELVEN, LOTRItemTags.REPAIRS_DORWINION_ELVEN_BOW));
    public static final Item RHUNIC_BATTLEAXE = register("rhunic_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.RHUN, 5.0f, BATTLEAXE_SPEED));

    // The Rivendell set: 3.0 damage; elven blades, whose glow is not ported. The
    // helmet is the High Elven model on the set's own sheet.
    public static final Item RIVENDELL_SWORD = register("rivendell_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.RIVENDELL, 3.0f, -2.4f));
    public static final Item RIVENDELL_DAGGER = register("rivendell_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.RIVENDELL, 0.0f));
    public static final Item POISONED_RIVENDELL_DAGGER = register("poisoned_rivendell_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.RIVENDELL, 0.0f));
    public static final Item RIVENDELL_SPEAR = register("rivendell_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.RIVENDELL, 6.0f));
    public static final Item RIVENDELL_HELMET = registerArmor("rivendell_helmet",
            LOTRToolMaterials.RIVENDELL_ARMOR, ArmorType.HELMET);
    public static final Item RIVENDELL_CHESTPLATE = registerArmor("rivendell_chestplate",
            LOTRToolMaterials.RIVENDELL_ARMOR, ArmorType.CHESTPLATE);
    public static final Item RIVENDELL_LEGGINGS = registerArmor("rivendell_leggings",
            LOTRToolMaterials.RIVENDELL_ARMOR, ArmorType.LEGGINGS);
    public static final Item RIVENDELL_BOOTS = registerArmor("rivendell_boots",
            LOTRToolMaterials.RIVENDELL_ARMOR, ArmorType.BOOTS);
    public static final Item RIVENDELL_HORSE_ARMOR = registerHorseArmor("rivendell_horse_armor",
            LOTRToolMaterials.RIVENDELL_ARMOR);
    public static final Item RIVENDELL_BATTLESTAFF = register("rivendell_battlestaff",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.RIVENDELL, 7.0f,
                    POLEARM_SPEED, POLEARM_REACH, 0.0));
    public static final Item RIVENDELL_LONGSPEAR = register("rivendell_longspear",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.RIVENDELL, 7.0f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // The Arnorian weapons.
    public static final Item ARNOR_SWORD = register("arnor_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.ARNOR, 3.0f, -2.4f));
    public static final Item ARNOR_DAGGER = register("arnor_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.ARNOR, 0.0f));
    public static final Item POISONED_ARNOR_DAGGER = register("poisoned_arnor_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.ARNOR, 0.0f));
    public static final Item ARNOR_SPEAR = register("arnor_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.ARNOR, 5.5f));
    // rivendellBow: LOTRItemBow(RIVENDELL, 1.25).setDrawTime(16).
    public static final Item RIVENDELL_BOW = register("rivendell_bow",
            props -> new LOTRBowItem(16, 1.25f, props), bow(LOTRToolMaterials.RIVENDELL, LOTRItemTags.REPAIRS_RIVENDELL_BOW));

    // swordMoredain, the Morwaith Sword: MOREDAIN_BRONZE, 1.5 + 4 = 5.5.
    public static final Item MORWAITH_SWORD = register("morwaith_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.MORWAITH_BRONZE, 3.0f, -2.4f));

    // The Gulfen set; the chestplate wears its horned model (extraName "body").
    public static final Item GULFEN_HELMET = registerArmor("gulfen_helmet",
            LOTRToolMaterials.GULF_HARAD_ARMOR, ArmorType.HELMET);
    public static final Item GULFEN_CHESTPLATE = registerArmor("gulfen_chestplate",
            LOTRToolMaterials.GULF_HARAD_BODY_ARMOR, ArmorType.CHESTPLATE);
    public static final Item GULFEN_LEGGINGS = registerArmor("gulfen_leggings",
            LOTRToolMaterials.GULF_HARAD_ARMOR, ArmorType.LEGGINGS);
    public static final Item GULFEN_BOOTS = registerArmor("gulfen_boots",
            LOTRToolMaterials.GULF_HARAD_ARMOR, ArmorType.BOOTS);

    // The Corsairs: swordCorsair is the Eket, spearCorsair the Harpoon.
    public static final Item CORSAIR_HELMET = registerArmor("corsair_helmet",
            LOTRToolMaterials.CORSAIR_ARMOR, ArmorType.HELMET);
    public static final Item CORSAIR_CHESTPLATE = registerArmor("corsair_chestplate",
            LOTRToolMaterials.CORSAIR_ARMOR, ArmorType.CHESTPLATE);
    public static final Item CORSAIR_LEGGINGS = registerArmor("corsair_leggings",
            LOTRToolMaterials.CORSAIR_ARMOR, ArmorType.LEGGINGS);
    public static final Item CORSAIR_BOOTS = registerArmor("corsair_boots",
            LOTRToolMaterials.CORSAIR_ARMOR, ArmorType.BOOTS);
    public static final Item CORSAIR_EKET = register("corsair_eket",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.CORSAIR, 3.0f, -2.4f));
    public static final Item CORSAIR_DAGGER = register("corsair_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.CORSAIR, 0.0f));
    public static final Item POISONED_CORSAIR_DAGGER = register("poisoned_corsair_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.CORSAIR, 0.0f));
    public static final Item CORSAIR_HARPOON = register("corsair_harpoon",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.CORSAIR, 5.5f));
    public static final Item CORSAIR_BATTLEAXE = register("corsair_battleaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .axe(LOTRToolMaterials.CORSAIR, 5.0f, BATTLEAXE_SPEED));

    // The worn Umbaric set, on the material the horse armour already uses.
    public static final Item UMBARIC_HELMET = registerArmor("umbaric_helmet",
            LOTRToolMaterials.UMBARIC_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item UMBARIC_CHESTPLATE = registerArmor("umbaric_chestplate",
            LOTRToolMaterials.UMBARIC_ARMOR, ArmorType.CHESTPLATE);
    public static final Item UMBARIC_LEGGINGS = registerArmor("umbaric_leggings",
            LOTRToolMaterials.UMBARIC_ARMOR, ArmorType.LEGGINGS);
    public static final Item UMBARIC_BOOTS = registerArmor("umbaric_boots",
            LOTRToolMaterials.UMBARIC_ARMOR, ArmorType.BOOTS);

    // The Harnennor set: a plumed helmet and a barbed chestplate, each its own model.
    public static final Item HARNENNOR_HELMET = registerArmor("harnennor_helmet",
            LOTRToolMaterials.HARNEDOR_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item HARNENNOR_CHESTPLATE = registerArmor("harnennor_chestplate",
            LOTRToolMaterials.HARNEDOR_BODY_ARMOR, ArmorType.CHESTPLATE);
    public static final Item HARNENNOR_LEGGINGS = registerArmor("harnennor_leggings",
            LOTRToolMaterials.HARNEDOR_ARMOR, ArmorType.LEGGINGS);
    public static final Item HARNENNOR_BOOTS = registerArmor("harnennor_boots",
            LOTRToolMaterials.HARNEDOR_ARMOR, ArmorType.BOOTS);

    // The Haradric weapons: NEAR_HARAD, the material the port calls Coast Southron.
    public static final Item HARADRIC_SWORD = register("haradric_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.COAST_SOUTHRON, 3.0f, -2.4f));
    public static final Item HARADRIC_DAGGER = register("haradric_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.COAST_SOUTHRON, 0.0f));
    public static final Item POISONED_HARADRIC_DAGGER = register("poisoned_haradric_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.COAST_SOUTHRON, 0.0f));
    public static final Item HARADRIC_SPEAR = register("haradric_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.COAST_SOUTHRON, 5.5f));
    public static final Item HARADRIC_PIKE = register("haradric_pike",
            LOTRModifiableItem::new, polearm(LOTRToolMaterials.COAST_SOUTHRON, 6.5f,
                    LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 0.0));

    // swordGulfHarad, the Gulfen Khopesh.
    public static final Item GULFEN_KHOPESH = register("gulfen_khopesh",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.GULF_HARAD, 3.0f, -2.4f));

    // The nomads' cap, tunic, leggings and shoes.
    public static final Item NOMAD_CAP = registerArmor("nomad_cap",
            LOTRToolMaterials.HARAD_NOMAD_ARMOR, ArmorType.HELMET);
    public static final Item NOMAD_TUNIC = registerArmor("nomad_tunic",
            LOTRToolMaterials.HARAD_NOMAD_ARMOR, ArmorType.CHESTPLATE);
    public static final Item NOMAD_LEGGINGS = registerArmor("nomad_leggings",
            LOTRToolMaterials.HARAD_NOMAD_ARMOR, ArmorType.LEGGINGS);
    public static final Item NOMAD_SHOES = registerArmor("nomad_shoes",
            LOTRToolMaterials.HARAD_NOMAD_ARMOR, ArmorType.BOOTS);

    // daggerAncientHarad, the Old-Haradric Sacrificial Dagger: 2.5 + 4 - 3 = 3.5.
    public static final Item OLD_HARADRIC_SACRIFICIAL_DAGGER = register("old_haradric_sacrificial_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.ANCIENT_HARAD, 0.0f));

    // The Black Numenoreans: a winged helmet, and maceBlackNumenorean is a
    // LOTRItemHammer, 2.5 + 4 + 2 = 8.5.
    public static final Item BLACK_NUMENOREAN_HELMET = registerArmor("black_numenorean_helmet",
            LOTRToolMaterials.BLACK_NUMENOREAN_HELMET_ARMOR, ArmorType.HELMET);
    public static final Item BLACK_NUMENOREAN_CHESTPLATE = registerArmor("black_numenorean_chestplate",
            LOTRToolMaterials.BLACK_NUMENOREAN_ARMOR, ArmorType.CHESTPLATE);
    public static final Item BLACK_NUMENOREAN_LEGGINGS = registerArmor("black_numenorean_leggings",
            LOTRToolMaterials.BLACK_NUMENOREAN_ARMOR, ArmorType.LEGGINGS);
    public static final Item BLACK_NUMENOREAN_BOOTS = registerArmor("black_numenorean_boots",
            LOTRToolMaterials.BLACK_NUMENOREAN_ARMOR, ArmorType.BOOTS);
    public static final Item BLACK_NUMENOREAN_SWORD = register("black_numenorean_sword",
            LOTRModifiableItem::new, new Item.Properties()
                    .sword(LOTRToolMaterials.BLACK_NUMENOREAN, 3.0f, -2.4f));
    public static final Item BLACK_NUMENOREAN_DAGGER = register("black_numenorean_dagger",
            LOTRModifiableItem::new, dagger(LOTRToolMaterials.BLACK_NUMENOREAN, 0.0f));
    public static final Item POISONED_BLACK_NUMENOREAN_DAGGER = register("poisoned_black_numenorean_dagger",
            LOTRPoisonedDaggerItem::new, dagger(LOTRToolMaterials.BLACK_NUMENOREAN, 0.0f));
    public static final Item BLACK_NUMENOREAN_SPEAR = register("black_numenorean_spear",
            LOTRModifiableItem::new, spear(LOTRToolMaterials.BLACK_NUMENOREAN, 5.5f));
    public static final Item BLACK_NUMENOREAN_MACE = register("black_numenorean_mace",
            LOTRModifiableItem::new, warhammer(LOTRToolMaterials.BLACK_NUMENOREAN, 5.0f));

    // LOTRItemGandalfStaffGrey: a wooden staff, 4 damage, a thousand uses.
    public static final Item GANDALF_STAFF_GREY = register("gandalf_staff_grey", LOTRModifiableItem::new,
            new Item.Properties().sword(net.minecraft.world.item.ToolMaterial.WOOD, 3.0f, -2.4f).durability(1000));

    // LOTRItemGlamdring: a Gondolin sword, with that material's own numbers.
    public static final Item GLAMDRING = register("glamdring", LOTRModifiableItem::new,
            new Item.Properties().sword(LOTRToolMaterials.GONDOLIN, 3.0f, -2.4f));

    // LOTRItemMechanism, from tabMisc: right-click a rail with one and it
    // becomes a mechanised rail -- a powered rail that needs no redstone.
    public static final Item MECHANISM = register("mechanism", LOTRMechanismItem::new, new Item.Properties());

    // ---- tabMisc, in LOTRMod's registration order. ----
    //
    // NOT registered: the pouch, which is a container item with an inventory of
    // its own, and the NPC respawner, which needs NPCs. The smoking pipe is
    // here but does nothing: its seventeen colours of smoke were particles.
    //
    // The coins keep their three 1.7.10 stack values as three items; the value
    // system that counted them is not ported. The ancient items and their parts
    // are plain too -- right-clicking one rolled a random weapon out of a chest
    // pool, and those pools are not ported.
    public static final Item GOLD_RING = register("gold_ring", Item::new, new Item.Properties());
    public static final Item SILVER_RING = register("silver_ring", Item::new, new Item.Properties());
    public static final Item MITHRIL_RING = register("mithril_ring", Item::new, new Item.Properties());
    public static final Item SMOKING_PIPE = register("smoking_pipe", LOTRSmokingPipeItem::new,
            new Item.Properties().stacksTo(1).durability(300));
    public static final Item SILVER_COIN = register("silver_coin", Item::new, new Item.Properties());
    public static final Item SILVER_COIN_STACK = register("silver_coin_stack", Item::new, new Item.Properties());
    public static final Item SILVER_COIN_PILE = register("silver_coin_pile", Item::new, new Item.Properties());
    public static final Item HOBBIT_MARRIAGE_RING = register("hobbit_marriage_ring", Item::new, new Item.Properties());
    public static final Item ANCIENT_SWORD_TIP = register("ancient_sword_tip", Item::new, new Item.Properties());
    public static final Item ANCIENT_SWORD_BLADE = register("ancient_sword_blade", Item::new, new Item.Properties());
    public static final Item ANCIENT_SWORD_HILT = register("ancient_sword_hilt", Item::new, new Item.Properties());
    public static final Item ANCIENT_ARMOR_PLATE = register("ancient_armor_plate", Item::new, new Item.Properties());
    public static final Item ANCIENT_SWORD = register("ancient_sword", Item::new, new Item.Properties());
    public static final Item ANCIENT_DAGGER = register("ancient_dagger", Item::new, new Item.Properties());
    public static final Item ANCIENT_HELMET = register("ancient_helmet", Item::new, new Item.Properties());
    public static final Item ANCIENT_CHESTPLATE = register("ancient_chestplate", Item::new, new Item.Properties());
    public static final Item ANCIENT_LEGGINGS = register("ancient_leggings", Item::new, new Item.Properties());
    public static final Item ANCIENT_BOOTS = register("ancient_boots", Item::new, new Item.Properties());
    public static final Item DWARVEN_MARRIAGE_RING = register("dwarven_marriage_ring", Item::new, new Item.Properties());
    // LOTRItemRedBook: setMaxStackSize(1).
    public static final Item RED_BOOK = register("red_book", Item::new, new Item.Properties().stacksTo(1));
    public static final Item KEY_OF_ICE = register("key_of_ice", Item::new, new Item.Properties());
    public static final Item KEY_OF_OBSIDIAN = register("key_of_obsidian", Item::new, new Item.Properties());
    public static final Item ICE_KEY_HANDLE = register("ice_key_handle", Item::new, new Item.Properties());
    public static final Item ICE_KEY_SHAFT = register("ice_key_shaft", Item::new, new Item.Properties());
    public static final Item ICE_KEY_PIN = register("ice_key_pin", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_KEY_HANDLE = register("obsidian_key_handle", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_KEY_SHAFT = register("obsidian_key_shaft", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_KEY_PIN = register("obsidian_key_pin", Item::new, new Item.Properties());
    public static final Item TAURETHRIM_AMULET = register("taurethrim_amulet", Item::new, new Item.Properties());
    // LOTRItemDaleCracker's five colours, one to a stack.
    public static final Item RED_DALISH_CRACKER = register("red_dalish_cracker", LOTRDaleCrackerItem::new, new Item.Properties().stacksTo(1));
    public static final Item BLUE_DALISH_CRACKER = register("blue_dalish_cracker", LOTRDaleCrackerItem::new, new Item.Properties().stacksTo(1));
    public static final Item GREEN_DALISH_CRACKER = register("green_dalish_cracker", LOTRDaleCrackerItem::new, new Item.Properties().stacksTo(1));
    public static final Item SILVER_DALISH_CRACKER = register("silver_dalish_cracker", LOTRDaleCrackerItem::new, new Item.Properties().stacksTo(1));
    public static final Item GOLD_DALISH_CRACKER = register("gold_dalish_cracker", LOTRDaleCrackerItem::new, new Item.Properties().stacksTo(1));
    public static final Item MYSTERY_WEB = register("mystery_web",
            props -> new LOTRThrownMiscItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMysteryWebEntity::new, 0.5f,
                    () -> net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities.MYSTERY_WEB, props),
            new Item.Properties());
    public static final Item EXPLODING_TERMITE = register("exploding_termite",
            props -> new LOTRThrownMiscItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRExplodingTermiteEntity::new, 1.5f,
                    () -> net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities.EXPLODING_TERMITE, props),
            new Item.Properties().stacksTo(16));
    public static final Item CONKER = register("conker",
            props -> new LOTRThrownMiscItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRConkerEntity::new, 1.0f,
                    () -> net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities.CONKER, props),
            new Item.Properties().stacksTo(16));
    public static final Item LEATHER_HAT = register("leather_hat", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.HEAD)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.LEATHER_HAT_ASSET)
                    .build()));
    public static final Item PARTY_HAT = register("party_hat", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.HEAD)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.PARTY_HAT_ASSET)
                    .build()));
    public static final Item HARAD_TURBAN = register("harad_turban", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.HEAD)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.HARAD_TURBAN_ASSET)
                    .build()));
    public static final Item HARAD_ROBE = register("harad_robe", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.CHEST)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.HARAD_ROBES_ASSET)
                    .build()));
    public static final Item HARAD_ROBE_LEGGINGS = register("harad_robe_leggings", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.LEGS)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.HARAD_ROBES_ASSET)
                    .build()));
    public static final Item HARAD_ROBE_SHOES = register("harad_robe_shoes", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.FEET)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.HARAD_ROBES_ASSET)
                    .build()));
    public static final Item KAFTAN = register("kaftan", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.CHEST)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.KAFTAN_ASSET)
                    .build()));
    public static final Item KAFTAN_LEGGINGS = register("kaftan_leggings", Item::new, new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.EQUIPPABLE, net.minecraft.world.item.equipment.Equippable
                    .builder(net.minecraft.world.entity.EquipmentSlot.LEGS)
                    .setEquipSound(net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER)
                    .setAsset(LOTRToolMaterials.KAFTAN_ASSET)
                    .build()));

    // ---- tabCombat's projectiles, which needed entities of their own. ----

    // rhunFirePot: stacks to four and bursts into fire where it lands.
    public static final Item RHUNIC_FIRE_POT = register("rhunic_fire_pot",
            LOTRFirePotItem::new, new Item.Properties().stacksTo(4));
    // arrowPoisoned and crossbowBoltPoisoned: the dagger's poison, on a shaft.
    public static final Item POISONED_ARROW = register("poisoned_arrow",
            LOTRPoisonedArrowItem::new, new Item.Properties());
    public static final Item POISONED_CROSSBOW_BOLT = register("poisoned_crossbow_bolt",
            props -> new LOTRCrossbowBoltItem(true, props), new Item.Properties());

    // ---- tabTools, top to bottom. ----
    //
    // Damage is the original's modifier less one, as the melee weapons are: a
    // 1.7.10 pickaxe added material + 2, an axe + 3, a shovel + 1 and a hoe
    // nothing. Swing speeds are vanilla's for each kind of tool. Mining speed,
    // tier and durability come from the material.

    private static final net.minecraft.tags.TagKey<Block> MATTOCK_MINEABLE =
            net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockTags.MATTOCK_MINEABLE;

    public static final Item BRONZE_SHOVEL = register("bronze_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.BRONZE, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item BRONZE_PICKAXE = register("bronze_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.BRONZE, 1.0f, -2.8f));
    public static final Item BRONZE_AXE = register("bronze_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.BRONZE, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item BRONZE_HOE = register("bronze_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.BRONZE, props), new Item.Properties());
    public static final Item MITHRIL_SHOVEL = register("mithril_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.MITHRIL, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item MITHRIL_PICKAXE = register("mithril_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.MITHRIL, 1.0f, -2.8f));
    public static final Item MITHRIL_AXE = register("mithril_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.MITHRIL, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item MITHRIL_HOE = register("mithril_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.MITHRIL, props), new Item.Properties());

    // LOTRItemSauronMace: a Mordor hammer of 8 damage and 1500 uses, and held
    // down it brings the Dark Lord's blow down on everything within twelve
    // blocks -- see the item class.
    public static final Item SAURON_MACE = register("sauron_mace", LOTRSauronMaceItem::new,
            warhammer(LOTRToolMaterials.MORDOR, 4.5f).durability(1500));

    // LOTRItemGandalfStaffWhite: a Lindon-grade staff of 8 damage, and held
    // down it looses the fireball.
    public static final Item GANDALF_STAFF_WHITE = register("gandalf_staff_white",
            LOTRGandalfStaffWhiteItem::new,
            new Item.Properties().sword(LOTRToolMaterials.LINDON, 4.0f, -2.4f).durability(1500));

    // The fireball's own item: never in a tab and never in a recipe -- it is
    // only what the thrown entity is drawn as (LOTRRenderGandalfFireball drew
    // frames 24-27 of the particle sheet).
    public static final Item GANDALF_FIREBALL = register("gandalf_fireball", Item::new, new Item.Properties());
    public static final Item MALLORN_SHOVEL = register("mallorn_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.MALLORN, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item MALLORN_PICKAXE = register("mallorn_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.MALLORN, 1.0f, -2.8f));
    public static final Item MALLORN_AXE = register("mallorn_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.MALLORN, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item MALLORN_HOE = register("mallorn_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.MALLORN, props), new Item.Properties());
    public static final Item GALADHRIM_SHOVEL = register("galadhrim_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.GALADHRIM, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item GALADHRIM_PICKAXE = register("galadhrim_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.GALADHRIM, 1.0f, -2.8f));
    public static final Item GALADHRIM_AXE = register("galadhrim_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.GALADHRIM, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item GALADHRIM_HOE = register("galadhrim_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.GALADHRIM, props), new Item.Properties());
    public static final Item DWARVEN_SHOVEL = register("dwarven_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.DWARVEN, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item DWARVEN_PICKAXE = register("dwarven_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.DWARVEN, 1.0f, -2.8f));
    public static final Item DWARVEN_AXE = register("dwarven_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.DWARVEN, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item DWARVEN_HOE = register("dwarven_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.DWARVEN, props), new Item.Properties());
    public static final Item MORDOR_SHOVEL = register("mordor_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.MORDOR, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item MORDOR_PICKAXE = register("mordor_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.MORDOR, 1.0f, -2.8f));
    public static final Item MORDOR_AXE = register("mordor_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.MORDOR, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item MORDOR_HOE = register("mordor_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.MORDOR, props), new Item.Properties());
    public static final Item URUK_SHOVEL = register("uruk_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.URUK, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item URUK_PICKAXE = register("uruk_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.URUK, 1.0f, -2.8f));
    public static final Item URUK_AXE = register("uruk_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.URUK, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item URUK_HOE = register("uruk_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.URUK, props), new Item.Properties());

    // mattockDwarven: LOTRItemMattock, a pickaxe that also cuts wood well.
    public static final Item DWARVEN_MATTOCK = register("dwarven_mattock",
            LOTRModifiableItem::new, new Item.Properties()
                    .tool(LOTRToolMaterials.DWARVEN, MATTOCK_MINEABLE, 1.0f, -2.8f, 0.0f));
    public static final Item WOOD_ELVEN_SHOVEL = register("wood_elven_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.WOOD_ELVEN, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item WOOD_ELVEN_PICKAXE = register("wood_elven_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.WOOD_ELVEN, 1.0f, -2.8f));
    public static final Item WOOD_ELVEN_AXE = register("wood_elven_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.WOOD_ELVEN, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item WOOD_ELVEN_HOE = register("wood_elven_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.WOOD_ELVEN, props), new Item.Properties());

    // sulfurMatch: a one-use flint and steel.
    public static final Item SULFUR_MATCH = register("sulfur_match",
            LOTRMatchItem::new, new Item.Properties());
    public static final Item ANGMAR_SHOVEL = register("angmar_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.ANGMAR, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item ANGMAR_PICKAXE = register("angmar_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.ANGMAR, 1.0f, -2.8f));
    public static final Item ANGMAR_AXE = register("angmar_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.ANGMAR, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item ANGMAR_HOE = register("angmar_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.ANGMAR, props), new Item.Properties());
    public static final Item LINDON_SHOVEL = register("lindon_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.LINDON, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item LINDON_PICKAXE = register("lindon_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.LINDON, 1.0f, -2.8f));
    public static final Item LINDON_AXE = register("lindon_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.LINDON, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item LINDON_HOE = register("lindon_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.LINDON, props), new Item.Properties());
    public static final Item BLUE_DWARVEN_SHOVEL = register("blue_dwarven_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.BLUE_DWARVEN, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item BLUE_DWARVEN_PICKAXE = register("blue_dwarven_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.BLUE_DWARVEN, 1.0f, -2.8f));
    public static final Item BLUE_DWARVEN_AXE = register("blue_dwarven_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.BLUE_DWARVEN, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item BLUE_DWARVEN_HOE = register("blue_dwarven_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.BLUE_DWARVEN, props), new Item.Properties());
    public static final Item BLUE_DWARVEN_MATTOCK = register("blue_dwarven_mattock",
            LOTRModifiableItem::new, new Item.Properties()
                    .tool(LOTRToolMaterials.BLUE_DWARVEN, MATTOCK_MINEABLE, 1.0f, -2.8f, 0.0f));
    // LOTRMod registered the Dol Guldur axe before its pickaxe.
    public static final Item DOL_GULDUR_SHOVEL = register("dol_guldur_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.DOL_GULDUR, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item DOL_GULDUR_AXE = register("dol_guldur_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.DOL_GULDUR, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item DOL_GULDUR_PICKAXE = register("dol_guldur_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.DOL_GULDUR, 1.0f, -2.8f));
    public static final Item DOL_GULDUR_HOE = register("dol_guldur_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.DOL_GULDUR, props), new Item.Properties());

    // utumnoPickaxe, the Pickaxe of the Underworld: UTUMNO, but setMaxDamage(80).
    public static final Item UTUMNO_PICKAXE = register("utumno_pickaxe",
            LOTRModifiableItem::new, new Item.Properties()
                    .pickaxe(LOTRToolMaterials.UTUMNO, 1.0f, -2.8f)
                    .durability(80));
    public static final Item TAURETHRIM_SHOVEL = register("taurethrim_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.TAURETHRIM, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item TAURETHRIM_PICKAXE = register("taurethrim_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.TAURETHRIM, 1.0f, -2.8f));
    public static final Item TAURETHRIM_AXE = register("taurethrim_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.TAURETHRIM, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item TAURETHRIM_HOE = register("taurethrim_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.TAURETHRIM, props), new Item.Properties());
    public static final Item MITHRIL_MATTOCK = register("mithril_mattock",
            LOTRModifiableItem::new, new Item.Properties()
                    .tool(LOTRToolMaterials.MITHRIL, MATTOCK_MINEABLE, 1.0f, -2.8f, 0.0f));
    public static final Item RIVENDELL_SHOVEL = register("rivendell_shovel",
            props -> new LOTRShovelItem(LOTRToolMaterials.RIVENDELL, 0.0f, -3.0f, props), new Item.Properties());
    public static final Item RIVENDELL_PICKAXE = register("rivendell_pickaxe",
            LOTRModifiableItem::new, new Item.Properties().pickaxe(LOTRToolMaterials.RIVENDELL, 1.0f, -2.8f));
    public static final Item RIVENDELL_AXE = register("rivendell_axe",
            props -> new LOTRAxeItem(LOTRToolMaterials.RIVENDELL, 2.0f, -3.0f, props), new Item.Properties());
    public static final Item RIVENDELL_HOE = register("rivendell_hoe",
            props -> new LOTRHoeItem(LOTRToolMaterials.RIVENDELL, props), new Item.Properties());

    // ---- tabFood, top to bottom. ----
    //
    // Food values go through FoodProperties.Builder.saturationModifier, which is
    // what ItemFood's second argument was: 1.7.10 multiplied it into saturation
    // the same way (nutrition x modifier x 2).

    /** A drink: one to a stack, drunk rather than eaten, in a mug unless moved to another vessel. */
    private static Item.Properties drink(boolean brewable) {
        Item.Properties properties = new Item.Properties().stacksTo(1)
                .component(DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumables.DEFAULT_DRINK)
                .component(LOTRDataComponents.VESSEL, LOTRVessel.MUG);
        return brewable ? properties.component(LOTRDataComponents.DRINK_STRENGTH, 0) : properties;
    }

    private static FoodProperties food(int nutrition, float saturationModifier) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build();
    }
    public static final Item CLAY_MUG = register("clay_mug", Item::new, new Item.Properties());
    public static final Item MUG = register("mug",
            props -> new LOTRVesselItem(LOTRVessel.MUG, props), new Item.Properties());
    public static final Item WATER = register("water",
            props -> new LOTRDrinkItem(false, false, 0.0f, props),
            drink(false));
    public static final Item MILK = register("milk",
            props -> new LOTRDrinkItem(false, false, 0.0f, props).setCuresEffects(),
            drink(false));
    public static final Item ALE = register("ale",
            props -> new LOTRDrinkItem(false, true, 0.3f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item CHOCOLATE = register("chocolate",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(6, 0.6f),
            drink(false));
    public static final Item MIRUVOR = register("miruvor",
            props -> new LOTRDrinkItem(false, true, 0.0f, props).setDrinkStats(8, 0.8f).addEffect(net.minecraft.world.effect.MobEffects.STRENGTH, 40).addEffect(net.minecraft.world.effect.MobEffects.SPEED, 40),
            drink(true));
    public static final Item ORC_DRAUGHT = register("orc_draught",
            props -> new LOTRDrinkItem(false, true, 0.0f, props).setDrinkStats(6, 0.6f).addEffect(net.minecraft.world.effect.MobEffects.STRENGTH, 60).addEffect(net.minecraft.world.effect.MobEffects.SPEED, 60).setDamageAmount(2),
            drink(true));
    public static final Item LEMBAS = register("lembas",
            Item::new, new Item.Properties().food(food(20, 2.0f)));
    public static final Item LETTUCE = register("lettuce",
            props -> new net.minecraft.world.item.BlockItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.LETTUCE_CROP, props), new Item.Properties().food(food(3, 0.4f)).useItemDescriptionPrefix());
    public static final Item GAMMON = register("gammon",
            Item::new, new Item.Properties().food(food(8, 0.8f)));
    public static final Item CLAY_PLATE = register("clay_plate", Item::new, new Item.Properties());
    public static final Item MEAD = register("mead",
            props -> new LOTRDrinkItem(false, true, 0.6f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item GREEN_APPLE = register("green_apple",
            Item::new, new Item.Properties().food(food(4, 0.3f)));
    public static final Item PEAR = register("pear",
            Item::new, new Item.Properties().food(food(4, 0.3f)));
    public static final Item CHERRIES = register("cherries",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item RED_WINE = register("red_wine",
            props -> new LOTRDrinkItem(false, true, 1.0f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item MALLORN_NUT = register("mallorn_nut",
            Item::new, new Item.Properties().food(food(4, 0.4f)));
    public static final Item CIDER = register("cider",
            props -> new LOTRDrinkItem(false, true, 0.3f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item PERRY = register("perry",
            props -> new LOTRDrinkItem(false, true, 0.3f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item CHERRY_LIQUEUR = register("cherry_liqueur",
            props -> new LOTRDrinkItem(false, true, 1.0f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item RUM = register("rum",
            props -> new LOTRDrinkItem(false, true, 1.5f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item ATHELAS_BREW = register("athelas_brew",
            props -> new LOTRDrinkItem(false, true, 0.0f, props).setDrinkStats(6, 0.6f).addEffect(net.minecraft.world.effect.MobEffects.STRENGTH, 120).addEffect(net.minecraft.world.effect.MobEffects.REGENERATION, 60).setSpecial(LOTRDrinkItem.Special.CURES_HARMFUL),
            drink(true));
    public static final Item DWARVEN_TONIC = register("dwarven_tonic",
            props -> new LOTRDrinkItem(false, true, 0.2f, props).setDrinkStats(4, 0.4f).addEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION, 240),
            drink(true));
    public static final Item ENT_DRAUGHT = register("ent_draught",
            LOTREntDraughtItem::new, new Item.Properties().stacksTo(1)
                    .component(DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumables.DEFAULT_DRINK)
                    .component(LOTRDataComponents.ENT_DRAUGHT, 0));
    public static final Item DWARVEN_ALE = register("dwarven_ale",
            props -> new LOTRDrinkItem(false, true, 0.4f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item MAGGOTY_BREAD = register("maggoty_bread",
            Item::new, new Item.Properties().food(food(4, 0.5f),
                    net.minecraft.world.item.component.Consumables.defaultFood().onConsume(
                            new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(
                                    new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.HUNGER, 400, 0), 0.4f)).build()));
    // rabbitRaw and rabbitCooked are NOT registered: vanilla has had raw and
    // cooked rabbit since 1.8, so its own items stand in for them.
    public static final Item RABBIT_STEW = register("rabbit_stew",
            Item::new, new Item.Properties().stacksTo(1).food(food(10, 0.8f)).usingConvertsTo(net.minecraft.world.item.Items.BOWL).craftRemainder(net.minecraft.world.item.Items.BOWL));
    public static final Item VODKA = register("vodka",
            props -> new LOTRDrinkItem(false, true, 1.75f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item HOBBIT_PANCAKE = register("hobbit_pancake",
            Item::new, new Item.Properties().food(food(4, 0.6f)));
    public static final Item MANGO = register("mango",
            Item::new, new Item.Properties().food(food(4, 0.3f)));
    public static final Item MANGO_JUICE = register("mango_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(6, 0.6f),
            drink(false));
    public static final Item BANANA = register("banana",
            props -> new LOTRHangingFruitItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.BANANA_BLOCK, props), new Item.Properties().food(food(2, 0.5f)));
    public static final Item BANANA_BREAD = register("banana_bread",
            Item::new, new Item.Properties().food(food(5, 0.6f)));
    public static final Item RAW_LION = register("raw_lion",
            Item::new, new Item.Properties().food(food(3, 0.3f)));
    public static final Item COOKED_LION = register("cooked_lion",
            Item::new, new Item.Properties().food(food(8, 0.8f)));
    public static final Item RAW_ZEBRA = register("raw_zebra",
            Item::new, new Item.Properties().food(food(2, 0.1f)));
    public static final Item COOKED_ZEBRA = register("cooked_zebra",
            Item::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item RAW_RHINO = register("raw_rhino",
            Item::new, new Item.Properties().food(food(2, 0.1f)));
    public static final Item COOKED_RHINO = register("cooked_rhino",
            Item::new, new Item.Properties().food(food(7, 0.4f)));
    public static final Item MAPLE_SYRUP = register("maple_syrup",
            Item::new, new Item.Properties().stacksTo(1).food(food(2, 0.1f)).usingConvertsTo(net.minecraft.world.item.Items.BOWL).craftRemainder(net.minecraft.world.item.Items.BOWL));
    public static final Item HOBBIT_PANCAKE_WITH_MAPLE_SYRUP = register("hobbit_pancake_with_maple_syrup",
            Item::new, new Item.Properties().food(food(5, 0.6f)));
    public static final Item MAPLE_BEER = register("maple_beer",
            props -> new LOTRDrinkItem(false, true, 0.4f, props).setDrinkStats(4, 0.6f),
            drink(true));
    public static final Item DATE = register("date",
            props -> new LOTRHangingFruitItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.DATE_BLOCK, props), new Item.Properties().food(food(2, 0.3f)));
    public static final Item ARAK = register("arak",
            props -> new LOTRDrinkItem(false, true, 1.4f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item CARROT_WINE = register("carrot_wine",
            props -> new LOTRDrinkItem(false, true, 0.8f, props).setDrinkStats(3, 0.4f),
            drink(true));
    public static final Item BANANA_BEER = register("banana_beer",
            props -> new LOTRDrinkItem(false, true, 0.5f, props).setDrinkStats(4, 0.6f),
            drink(true));
    public static final Item MELON_LIQUEUR = register("melon_liqueur",
            props -> new LOTRDrinkItem(false, true, 1.0f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item BLUEBERRIES = register("blueberries",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item BLACKBERRIES = register("blackberries",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item RASPBERRIES = register("raspberries",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item CRANBERRIES = register("cranberries",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item ELDERBERRIES = register("elderberries",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item ROAST_CHESTNUT = register("roast_chestnut",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item CACTUS_LIQUEUR = register("cactus_liqueur",
            props -> new LOTRDrinkItem(false, true, 0.8f, props).setDrinkStats(2, 0.3f),
            drink(true));
    public static final Item TOROG_DRAUGHT = register("torog_draught",
            props -> new LOTRDrinkItem(false, true, 0.6f, props).setDrinkStats(6, 0.6f).addEffect(net.minecraft.world.effect.MobEffects.STRENGTH, 90),
            drink(true));
    public static final Item BLUEBERRY_JUICE = register("blueberry_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item BLACKBERRY_JUICE = register("blackberry_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item RASPBERRY_JUICE = register("raspberry_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item CRANBERRY_JUICE = register("cranberry_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item ELDERBERRY_JUICE = register("elderberry_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item TOROG_STEW = register("torog_stew",
            Item::new, new Item.Properties().stacksTo(1).food(food(8, 0.6f)).usingConvertsTo(net.minecraft.world.item.Items.BOWL).craftRemainder(net.minecraft.world.item.Items.BOWL));
    public static final Item CRAM = register("cram",
            Item::new, new Item.Properties().food(food(8, 1.0f)));
    public static final Item LEMON = register("lemon",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item ORANGE = register("orange",
            Item::new, new Item.Properties().food(food(4, 0.3f)));
    public static final Item ORANGE_JUICE = register("orange_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(6, 0.6f),
            drink(false));
    public static final Item LEMON_LIQUEUR = register("lemon_liqueur",
            props -> new LOTRDrinkItem(false, true, 1.0f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item LEMONADE = register("lemonade",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.3f),
            drink(false));
    public static final Item LIME = register("lime",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item LIME_LIQUEUR = register("lime_liqueur",
            props -> new LOTRDrinkItem(false, true, 1.0f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item RAW_MUTTON = register("raw_mutton",
            Item::new, new Item.Properties().food(food(3, 0.3f)));
    public static final Item COOKED_MUTTON = register("cooked_mutton",
            Item::new, new Item.Properties().food(food(8, 0.8f)));
    public static final Item TAURETHRIM_COCOA = register("taurethrim_cocoa",
            props -> new LOTRDrinkItem(false, true, 0.0f, props).setDrinkStats(6, 0.6f).addEffect(net.minecraft.world.effect.MobEffects.STRENGTH, 40).addEffect(net.minecraft.world.effect.MobEffects.SPEED, 40),
            drink(true));
    public static final Item JUNGLE_REMEDY = register("jungle_remedy",
            props -> new LOTRDrinkItem(false, false, 0.0f, props).setSpecial(LOTRDrinkItem.Special.CURES_HARMFUL),
            drink(false));
    public static final Item CORN = register("corn",
            Item::new, new Item.Properties().food(food(2, 0.3f)));
    public static final Item CORN_LIQUOR = register("corn_liquor",
            props -> new LOTRDrinkItem(false, true, 1.0f, props).setDrinkStats(3, 0.3f),
            drink(true));
    public static final Item RAW_VENISON = register("raw_venison",
            Item::new, new Item.Properties().food(food(3, 0.3f)));
    public static final Item COOKED_VENISON = register("cooked_venison",
            Item::new, new Item.Properties().food(food(8, 0.8f)));
    public static final Item COOKED_CORN = register("cooked_corn",
            Item::new, new Item.Properties().food(food(4, 0.4f)));
    public static final Item SHISH_KEBAB = register("shish_kebab",
            LOTRKebabItem::new, new Item.Properties().food(food(8, 0.8f)));
    public static final Item LEEK = register("leek",
            props -> new net.minecraft.world.item.BlockItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.LEEK_CROP, props), new Item.Properties().food(food(2, 0.3f)).useItemDescriptionPrefix());
    public static final Item LEEK_SOUP = register("leek_soup",
            Item::new, new Item.Properties().stacksTo(1).food(food(8, 0.8f)).usingConvertsTo(net.minecraft.world.item.Items.BOWL).craftRemainder(net.minecraft.world.item.Items.BOWL));
    public static final Item TURNIP = register("turnip",
            props -> new net.minecraft.world.item.BlockItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.TURNIP_CROP, props), new Item.Properties().food(food(2, 0.3f)).useItemDescriptionPrefix());
    public static final Item RAW_CAMEL = register("raw_camel",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item COOKED_CAMEL = register("cooked_camel",
            Item::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item OLIVES = register("olives",
            Item::new, new Item.Properties().food(food(1, 0.1f)));
    public static final Item APPLE_JUICE = register("apple_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(6, 0.6f),
            drink(false));
    public static final Item OLIVE_BREAD = register("olive_bread",
            Item::new, new Item.Properties().food(food(5, 0.6f)));
    public static final Item RED_GRAPES = register("red_grapes",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item GREEN_GRAPES = register("green_grapes",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item WHITE_WINE = register("white_wine",
            props -> new LOTRDrinkItem(false, true, 0.9f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item RED_GRAPE_JUICE = register("red_grape_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item GREEN_GRAPE_JUICE = register("green_grape_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(5, 0.5f),
            drink(false));
    public static final Item ROAST_TURNIP = register("roast_turnip",
            Item::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item MELON_SOUP = register("melon_soup",
            Item::new, new Item.Properties().stacksTo(1).food(food(5, 0.5f)).usingConvertsTo(net.minecraft.world.item.Items.BOWL).craftRemainder(net.minecraft.world.item.Items.BOWL));
    public static final Item CERAMIC_MUG = register("ceramic_mug",
            props -> new LOTRVesselItem(LOTRVessel.MUG_CLAY, props), new Item.Properties());
    public static final Item ALMOND = register("almond",
            Item::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item WILDBERRIES = register("wildberries",
            LOTRPoisonousBerryItem::new, new Item.Properties().food(food(2, 0.2f)));
    public static final Item PLUM = register("plum",
            Item::new, new Item.Properties().food(food(4, 0.3f)));
    public static final Item MORGUL_DRAUGHT = register("morgul_draught",
            props -> new LOTRDrinkItem(false, true, 0.0f, props).setDrinkStats(4, 0.4f).setDamageAmount(3).addEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION, 300).setSpecial(LOTRDrinkItem.Special.MORGUL),
            drink(true));
    public static final Item PLUM_KVASS = register("plum_kvass",
            props -> new LOTRDrinkItem(false, true, 0.2f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item MARCHPANE = register("marchpane",
            Item::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item CHOCOLATE_MARCHPANE = register("chocolate_marchpane",
            Item::new, new Item.Properties().food(food(8, 0.8f)));
    public static final Item GOLDEN_GOBLET = register("golden_goblet",
            props -> new LOTRVesselItem(LOTRVessel.GOBLET_GOLD, props), new Item.Properties());
    public static final Item SILVER_GOBLET = register("silver_goblet",
            props -> new LOTRVesselItem(LOTRVessel.GOBLET_SILVER, props), new Item.Properties());
    public static final Item COPPER_GOBLET = register("copper_goblet",
            props -> new LOTRVesselItem(LOTRVessel.GOBLET_COPPER, props), new Item.Properties());
    public static final Item WOODEN_CUP = register("wooden_cup",
            props -> new LOTRVesselItem(LOTRVessel.GOBLET_WOOD, props), new Item.Properties());
    public static final Item SKULL_CUP = register("skull_cup",
            props -> new LOTRVesselItem(LOTRVessel.SKULL, props), new Item.Properties());
    public static final Item WINE_GLASS = register("wine_glass",
            props -> new LOTRVesselItem(LOTRVessel.GLASS, props), new Item.Properties());
    public static final Item WATERSKIN = register("waterskin",
            props -> new LOTRVesselItem(LOTRVessel.SKIN, props), new Item.Properties());
    public static final Item ALE_HORN = register("ale_horn",
            props -> new LOTRVesselItem(LOTRVessel.HORN, props), new Item.Properties());
    public static final Item GOLDEN_ALE_HORN = register("golden_ale_horn",
            props -> new LOTRVesselItem(LOTRVessel.HORN_GOLD, props), new Item.Properties());
    public static final Item TERMITE_TEQUILA = register("termite_tequila",
            props -> new LOTRDrinkItem(false, true, 1.5f, props).setDrinkStats(3, 0.3f).setSpecial(LOTRDrinkItem.Special.TERMITE),
            drink(true));
    public static final Item YAM = register("yam",
            props -> new net.minecraft.world.item.BlockItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.YAM_CROP, props), new Item.Properties().food(food(1, 0.4f),
                    net.minecraft.world.item.component.Consumables.defaultFood().onConsume(
                            new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(
                                    new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.HUNGER, 300, 0), 0.4f)).build()).useItemDescriptionPrefix());
    public static final Item ROAST_YAM = register("roast_yam",
            Item::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item SOURED_MILK = register("soured_milk",
            props -> new LOTRDrinkItem(false, true, 0.2f, props).setDrinkStats(5, 0.5f),
            drink(true));
    public static final Item POMEGRANATE = register("pomegranate",
            Item::new, new Item.Properties().food(food(4, 0.3f)));
    public static final Item POMEGRANATE_JUICE = register("pomegranate_juice",
            props -> new LOTRDrinkItem(true, false, 0.0f, props).setDrinkStats(6, 0.6f),
            drink(false));
    public static final Item POMEGRANATE_WINE = register("pomegranate_wine",
            props -> new LOTRDrinkItem(false, true, 0.9f, props).setDrinkStats(4, 0.4f),
            drink(true));
    public static final Item MAN_FLESH = register("man_flesh",
            LOTRManFleshItem::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item SALT = register("salt", LOTRSaltItem::new, new Item.Properties());
    public static final Item SUSPICIOUS_MEAT = register("suspicious_meat",
            Item::new, new Item.Properties().food(food(6, 0.6f)));
    public static final Item CORN_BREAD = register("corn_bread",
            Item::new, new Item.Properties().food(food(5, 0.6f)));
    public static final Item RAISINS = register("raisins",
            Item::new, new Item.Properties().food(food(1, 0.1f)));
    public static final Item MUSHROOM_PIE = register("mushroom_pie",
            Item::new, new Item.Properties().food(food(8, 0.3f)));

    // The seed foods are what their crops are planted from and drop.
    static {
        LOTRCropBlock.setSeed(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.LETTUCE_CROP, LETTUCE);
        LOTRCropBlock.setSeed(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.LEEK_CROP, LEEK);
        LOTRCropBlock.setSeed(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.TURNIP_CROP, TURNIP);
        LOTRCropBlock.setSeed(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.YAM_CROP, YAM);
    }

    // LOTRItemChisel: a hundred uses and held like a tool. Each carves its own
    // kind of sign; the moon-chisel is a chisel set with ithildin at a dwarven
    // or elven table, keeping its wear.
    public static final Item CHISEL = register("chisel",
            props -> new LOTRChiselItem(() -> net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.CARVED_SIGN, props),
            new Item.Properties().durability(100));
    public static final Item MOON_CHISEL = register("moon_chisel",
            props -> new LOTRChiselItem(() -> net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.CARVED_ITHILDIN_SIGN, props),
            new Item.Properties().durability(100));

    // ---- tabMaterials, top to bottom. ----
    //
    // In registerItem order. The mithril ingot is registered at the top of this
    // class. NOT registered: the copper ingot and the iron nugget, which vanilla
    // already has (recipes use its copper ingot).
    //
    // The Flame of Udun, Chill of Daedelos, Headhunter's Trophy, Smith's Scroll
    // and Book of True-silver are plain items: LOTRItemEnchantment named the
    // modifier each applies in LOTR's anvil, and LOTRItemModifierTemplate named
    // itself after the one it teaches -- that modifier system is NOT ported, so
    // the scroll carries no modifier and is plainly "Smith's Scroll".
    //
    // The gems, durnor, edhelvir and gulduril also set a renamed item's colour in
    // the anvil (AnvilNameColorProvider); the port has no LOTR anvil, so that is
    public static final Item TIN_INGOT = register("tin_ingot", Item::new, new Item.Properties());
    public static final Item BRONZE_INGOT = register("bronze_ingot", Item::new, new Item.Properties());
    public static final Item SILVER_INGOT = register("silver_ingot", Item::new, new Item.Properties());
    public static final Item SILVER_NUGGET = register("silver_nugget", Item::new, new Item.Properties());
    public static final Item MITHRIL_NUGGET = register("mithril_nugget", Item::new, new Item.Properties());
    public static final Item ORC_STEEL_INGOT = register("orc_steel_ingot", Item::new, new Item.Properties());
    public static final Item DURNOR = register("durnor", Item::new, new Item.Properties());
    public static final Item PIPEWEED_LEAF = register("pipeweed_leaf", Item::new, new Item.Properties());
    public static final Item PIPEWEED_SEEDS = register("pipeweed_seeds",
            props -> new net.minecraft.world.item.BlockItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.PIPEWEED_CROP, props),
            new Item.Properties().useItemDescriptionPrefix());
    // LOTRItemDye's six subtypes, one item each. The DYE component is what
    // isItemDye's ore-dictionary test became: sheep, wolf collars and signs take
    // them as vanilla dyes of the same colour, and the c:dyes/<colour> tags let
    // any recipe asking for a dye of that colour accept them.
    public static final Item YELLOW_DYE = register("yellow_dye", net.minecraft.world.item.DyeItem::new,
            new Item.Properties().component(DataComponents.DYE, net.minecraft.world.item.DyeColor.YELLOW));
    public static final Item WHITE_DYE = register("white_dye", net.minecraft.world.item.DyeItem::new,
            new Item.Properties().component(DataComponents.DYE, net.minecraft.world.item.DyeColor.WHITE));
    public static final Item BLUEBELL_BLUE = register("bluebell_blue", net.minecraft.world.item.DyeItem::new,
            new Item.Properties().component(DataComponents.DYE, net.minecraft.world.item.DyeColor.BLUE));
    public static final Item CLOVER_GREEN = register("clover_green", net.minecraft.world.item.DyeItem::new,
            new Item.Properties().component(DataComponents.DYE, net.minecraft.world.item.DyeColor.GREEN));
    public static final Item CHARCOAL_DUST = register("charcoal_dust", net.minecraft.world.item.DyeItem::new,
            new Item.Properties().component(DataComponents.DYE, net.minecraft.world.item.DyeColor.BLACK));
    public static final Item BROWN_DYE = register("brown_dye", net.minecraft.world.item.DyeItem::new,
            new Item.Properties().component(DataComponents.DYE, net.minecraft.world.item.DyeColor.BROWN));
    public static final Item MALLORN_STICK = register("mallorn_stick", Item::new, new Item.Properties());
    public static final Item FUR = register("fur", Item::new, new Item.Properties());
    public static final Item EDHELVIR = register("edhelvir", LOTRQuenditeCrystalItem::new, new Item.Properties());
    public static final Item WARG_BONE = register("warg_bone", Item::new, new Item.Properties());
    public static final Item DWARVEN_STEEL_INGOT = register("dwarven_steel_ingot", Item::new, new Item.Properties());
    public static final Item GALVORN_INGOT = register("galvorn_ingot", Item::new, new Item.Properties());
    public static final Item ORC_BONE = register("orc_bone", Item::new, new Item.Properties());
    public static final Item ELF_BONE = register("elf_bone", Item::new, new Item.Properties());
    public static final Item DWARF_BONE = register("dwarf_bone", Item::new, new Item.Properties());
    public static final Item HOBBIT_BONE = register("hobbit_bone", Item::new, new Item.Properties());
    public static final Item URUK_STEEL_INGOT = register("uruk_steel_ingot", Item::new, new Item.Properties());
    public static final Item TROLL_BONE = register("troll_bone", Item::new, new Item.Properties());
    public static final Item GULDURIL = register("gulduril", LOTRGuldurilCrystalItem::new, new Item.Properties());
    public static final Item MORGUL_STEEL_INGOT = register("morgul_steel_ingot", Item::new, new Item.Properties());
    public static final Item SULFUR = register("sulfur", Item::new, new Item.Properties());
    public static final Item NITER = register("niter", Item::new, new Item.Properties());
    public static final Item LION_FUR = register("lion_fur", Item::new, new Item.Properties());
    public static final Item RHINO_HORN = register("rhino_horn", Item::new, new Item.Properties());
    public static final Item GEMSBOK_HIDE = register("gemsbok_hide", Item::new, new Item.Properties());
    public static final Item GEMSBOK_HORN = register("gemsbok_horn", Item::new, new Item.Properties());
    public static final Item BLUE_DWARVEN_STEEL_INGOT = register("blue_dwarven_steel_ingot", Item::new, new Item.Properties());
    public static final Item FLAX_SEEDS = register("flax_seeds",
            props -> new net.minecraft.world.item.BlockItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.FLAX_CROP, props),
            new Item.Properties().useItemDescriptionPrefix());
    public static final Item FLAX = register("flax", Item::new, new Item.Properties());
    public static final Item BLACK_URUK_STEEL_INGOT = register("black_uruk_steel_ingot", Item::new, new Item.Properties());
    public static final Item ELVEN_STEEL_INGOT = register("elven_steel_ingot", Item::new, new Item.Properties());
    public static final Item SWAN_FEATHER = register("swan_feather", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_SHARD = register("obsidian_shard", Item::new, new Item.Properties());
    public static final Item HITHLAIN = register("hithlain", Item::new, new Item.Properties());
    public static final Item GATE_GEARS = register("gate_gears", Item::new, new Item.Properties());
    // LOTRItemGrapeSeeds: planted onto a bare grapevine post, never the ground.
    public static final Item RED_GRAPE_SEEDS = register("red_grape_seeds",
            props -> new LOTRGrapeSeedsItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.RED_GRAPEVINE, props),
            new Item.Properties().useItemDescriptionPrefix());
    // LOTRItemGrapeSeeds: planted onto a bare grapevine post, never the ground.
    public static final Item GREEN_GRAPE_SEEDS = register("green_grape_seeds",
            props -> new LOTRGrapeSeedsItem(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.GREEN_GRAPEVINE, props),
            new Item.Properties().useItemDescriptionPrefix());
    public static final Item KINE_OF_ARAW_HORN = register("kine_of_araw_horn", Item::new, new Item.Properties());
    public static final Item BLACKROOT_STICK = register("blackroot_stick", Item::new, new Item.Properties());
    public static final Item HORN = register("horn", Item::new, new Item.Properties());
    public static final Item ITHILDIN = register("ithildin", Item::new, new Item.Properties());
    public static final Item GILDED_IRON_INGOT = register("gilded_iron_ingot", Item::new, new Item.Properties());
    public static final Item FLAME_OF_UDUN = register("flame_of_udun", Item::new, new Item.Properties());
    public static final Item TOPAZ = register("topaz", Item::new, new Item.Properties());
    public static final Item AMETHYST = register("amethyst", Item::new, new Item.Properties());
    public static final Item SAPPHIRE = register("sapphire", Item::new, new Item.Properties());
    public static final Item RUBY = register("ruby", Item::new, new Item.Properties());
    public static final Item AMBER = register("amber", Item::new, new Item.Properties());
    public static final Item DIAMOND = register("diamond", Item::new, new Item.Properties());
    public static final Item PEARL = register("pearl", Item::new, new Item.Properties());
    public static final Item CORAL = register("coral", Item::new, new Item.Properties());
    public static final Item OPAL = register("opal", Item::new, new Item.Properties());
    public static final Item EMERALD = register("emerald", Item::new, new Item.Properties());
    public static final Item CHILL_OF_DAEDELOS = register("chill_of_daedelos", Item::new, new Item.Properties());
    public static final Item HEADHUNTERS_TROPHY = register("headhunters_trophy", Item::new, new Item.Properties());
    public static final Item SMITHS_SCROLL = register("smiths_scroll", LOTRSmithsScrollItem::new,
            new Item.Properties().stacksTo(1));
    public static final Item MITHRIL_MAIL = register("mithril_mail", Item::new, new Item.Properties());
    public static final Item BOOK_OF_TRUE_SILVER = register("book_of_true_silver", Item::new, new Item.Properties());

    // LOTRItemBottlePoison, from tabMisc: one to a stack, drunk like a potion,
    // and the glass bottle back afterwards -- or left in the grid when it
    // poisons a blade, arrows or bolts (setContainerItem). Drinking it gives
    // LOTRPoisonedDrinks' killing poison for fifteen seconds.
    public static final Item BOTTLE_OF_POISON = register("bottle_of_poison", Item::new, new Item.Properties()
            .stacksTo(1)
            .craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE)
            .component(DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumables.defaultDrink()
                    .onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(
                            new net.minecraft.world.effect.MobEffectInstance(
                                    net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREffects.DRINK_POISON, 300), 1.0F))
                    .build())
            .usingConvertsTo(net.minecraft.world.item.Items.GLASS_BOTTLE));
    public static final Item RED_CLAY_BALL = register("red_clay_ball", Item::new, new Item.Properties());

    // LOTRItemSeeds: what the pipe-weed and flax crops are planted from and drop.
    static {
        LOTRCropBlock.setSeed(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.PIPEWEED_CROP, PIPEWEED_SEEDS);
        LOTRCropBlock.setSeed(net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.FLAX_CROP, FLAX_SEEDS);
    }

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

    /**
     * A hammer swings 0.85 times a second. A deliberate choice over
     * registerMeleeSpeed(LOTRItemHammer.class, 0.667f), which would be 1.067.
     */
    private static final double WARHAMMER_ATTACK_SPEED = 0.85;

    /** registerMeleeExtraKnockback(LOTRItemHammer.class, 1): one knockback level. */
    private static final double WARHAMMER_KNOCKBACK = 1.0;

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
    private static Item.Properties lance(ToolMaterial material, float damage) {
        return polearm(material, damage, LONG_POLEARM_SPEED, LONG_POLEARM_REACH, 1.0,
                LANCE_MOVEMENT_PENALTY);
    }

    private static Item.Properties polearm(ToolMaterial material, float damage,
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

    private static Item registerArmor(String name, ArmorMaterial material, ArmorType type) {
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

    // Force class-load so the static fields register. Called from mod init. */
    public static void init() {
    }
}