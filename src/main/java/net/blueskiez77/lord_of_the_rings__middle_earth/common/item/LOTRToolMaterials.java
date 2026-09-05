package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * LOTRMaterial, as far as the port needs it.
 *
 * <p>The original's LOTRMaterial carried tool AND armour figures on one object
 * and built a Forge ToolMaterial on demand. 26.2's ToolMaterial is a plain
 * record with no armour side, so only the tool half lives here; the armour
 * figures belong with the armour materials when those are ported.
 *
 * <p>The harvest level maps onto an "incorrect for" block tag: 1.7.10's level 2
 * is iron, so a Mordor blade mines what an iron pickaxe would.
 */
public final class LOTRToolMaterials {

    /**
     * MORDOR: 400 uses, 2.5 damage, harvest level 2, speed 6.0, enchantability 7.
     *
     * <p>The original repaired these with orc steel -- MORDOR.setCraftingItem
     * (LOTRMod.orcSteel). The orc steel ingot is not ported yet, so
     * {@link LOTRItemTags#REPAIRS_MORDOR_TOOLS} stands in for it: an empty tag
     * today, and the one place to fill in when the ingot exists.
     */
    public static final ToolMaterial MORDOR = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            400,
            6.0f,
            2.5f,
            7,
            LOTRItemTags.REPAIRS_MORDOR_TOOLS);

    /**
     * BRONZE: 230 uses, 1.5 damage, harvest level 2, speed 5.0, enchantability
     * 10. Repaired with bronze ingots -- BRONZE.setCraftingItem(LOTRMod.bronze)
     * -- which the port has no ingot for yet, so
     * {@link LOTRItemTags#REPAIRS_BRONZE_TOOLS} stands empty for now.
     */
    public static final ToolMaterial BRONZE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            230,
            5.0f,
            1.5f,
            10,
            LOTRItemTags.REPAIRS_BRONZE_TOOLS);

    /** assets/lotr/equipment/bronze.json, which names the two worn textures. */
    public static final ResourceKey<EquipmentAsset> BRONZE_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "bronze"));

    /**
     * The armour half of LOTRMaterial.BRONZE, which 26.2 keeps on a separate
     * record from the tool half.
     *
     * <p>Every figure is the original's, run through its own arithmetic.
     * LOTRMaterial.toArmorMaterial passed {@code Math.round(uses * 0.06f)} as
     * the durability base -- 230 * 0.06 rounds to 14, and both versions
     * multiply that by the same {11, 16, 15, 13}, so the four pieces come out
     * at 154, 224, 210 and 182 uses exactly as before.
     *
     * <p>The defence numbers come from setProtection(0.5f), which was
     * {@code round(protectionBase[i] * f * 25)} over a base of
     * {0.14, 0.4, 0.32, 0.14}: 2, 5, 4 and 2, thirteen points in all.
     *
     * <p>Toughness and knockback resistance are zero because 1.7.10's
     * ArmorMaterial had neither -- they are not omissions.
     */
    public static final ArmorMaterial BRONZE_ARMOR = new ArmorMaterial(
            14,
            Map.of(
                    ArmorType.HELMET, 2,
                    ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4,
                    ArmorType.BOOTS, 2),
            10,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0f,
            0.0f,
            LOTRItemTags.REPAIRS_BRONZE_TOOLS,
            BRONZE_ASSET);

    /** assets/lotr/equipment/mordor.json, which names the two worn textures. */
    public static final ResourceKey<EquipmentAsset> MORDOR_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mordor"));

    /**
     * The armour half of LOTRMaterial.MORDOR, worked the same way BRONZE_ARMOR
     * was: durability base {@code round(400 * 0.06)} = 24, so the four pieces
     * last 264, 384, 360 and 312 uses; defence from setProtection(0.6f), which
     * is {@code round(protectionBase[i] * 0.6 * 25)} = 2, 6, 5 and 2 -- fifteen
     * points, the same as iron.
     */
    public static final ArmorMaterial MORDOR_ARMOR = new ArmorMaterial(
            24,
            Map.of(
                    ArmorType.HELMET, 2,
                    ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5,
                    ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            7,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0f,
            0.0f,
            LOTRItemTags.REPAIRS_MORDOR_TOOLS,
            MORDOR_ASSET);

    /** GONDOR: 450 uses, 2.5 damage, harvest level 2, speed 6.0, enchantability 10. */
    public static final ToolMaterial GONDOR = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 450, 6.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_GONDOR_TOOLS);

    /**
     * MITHRIL: 2400 uses, 5.0 damage, harvest level 4, speed 9.0,
     * enchantability 8. Level 4 is past anything 1.7.10 had a name for, so it
     * takes the highest tag 26.2 offers.
     */
    public static final ToolMaterial MITHRIL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2400, 9.0f, 5.0f, 8,
            LOTRItemTags.REPAIRS_MITHRIL_TOOLS);

    public static final ResourceKey<EquipmentAsset> GONDOR_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gondor"));

    /**
     * The Gondorian helmet alone wore its own layer-1 texture -- the original
     * passed extraName "helmet" for that one piece, and getArmorName used it in
     * place of the usual "_1" suffix. An EquipmentAsset covers a whole material,
     * so the helmet needs one of its own.
     */
    public static final ResourceKey<EquipmentAsset> GONDOR_HELMET_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gondor_helmet"));

    public static final ResourceKey<EquipmentAsset> MITHRIL_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mithril"));

    /** round(450 * 0.06) = 27 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial GONDOR_ARMOR = new ArmorMaterial(
            27,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GONDOR_TOOLS, GONDOR_ASSET);

    /** The same figures as GONDOR_ARMOR, pointing at the helmet's own texture. */
    public static final ArmorMaterial GONDOR_HELMET_ARMOR = new ArmorMaterial(
            27,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GONDOR_TOOLS, GONDOR_HELMET_ASSET);

    /** round(2400 * 0.06) = 144 durability; setProtection(0.8f) gives 3/8/6/3. */
    public static final ArmorMaterial MITHRIL_ARMOR = new ArmorMaterial(
            144,
            Map.of(ArmorType.HELMET, 3, ArmorType.CHESTPLATE, 8,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 3,
                    ArmorType.BODY, 14),
            8, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_MITHRIL_TOOLS, MITHRIL_ASSET);

    /** GALADHRIM: 600 uses, 3.0 damage, harvest level 2, speed 7.0, enchantability 15. */
    public static final ToolMaterial GALADHRIM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 600, 7.0f, 3.0f, 15,
            LOTRItemTags.REPAIRS_GALADHRIM_TOOLS);

    /** MALLORN: 200 uses, 1.5 damage, harvest level 1, speed 4.0, enchantability 15. */
    public static final ToolMaterial MALLORN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL, 200, 4.0f, 1.5f, 15,
            LOTRItemTags.REPAIRS_MALLORN_TOOLS);

    public static final ResourceKey<EquipmentAsset> GALADHRIM_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "galadhrim"));

    /** The Galadhrim helmet wears its own layer, as the Gondorian one does. */
    public static final ResourceKey<EquipmentAsset> GALADHRIM_HELMET_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "galadhrim_helmet"));

    public static final ResourceKey<EquipmentAsset> FUR_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "fur"));

    /** round(600 * 0.06) = 36 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial GALADHRIM_ARMOR = new ArmorMaterial(
            36,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            15, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GALADHRIM_TOOLS, GALADHRIM_ASSET);

    /** The same figures, pointing at the helmet's own texture. */
    public static final ArmorMaterial GALADHRIM_HELMET_ARMOR = new ArmorMaterial(
            36,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            15, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GALADHRIM_TOOLS, GALADHRIM_HELMET_ASSET);

    /**
     * LOTRMaterial.FUR: 180 uses, no damage, protection 0.4, enchantability 8.
     * round(180 * 0.06) = 11 durability, and 1/4/3/1 defence -- warm, not
     * armour. Leather's equip sound rather than iron's, since it is hide.
     */
    public static final ArmorMaterial FUR_ARMOR = new ArmorMaterial(
            11,
            Map.of(ArmorType.HELMET, 1, ArmorType.CHESTPLATE, 4,
                    ArmorType.LEGGINGS, 3, ArmorType.BOOTS, 1),
            8, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_FUR_ARMOR, FUR_ASSET);

    /** DWARVEN: 700 uses, 3.0 damage, harvest level 3, speed 7.0, enchantability 10. */
    public static final ToolMaterial DWARVEN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 700, 7.0f, 3.0f, 10,
            LOTRItemTags.REPAIRS_DWARVEN_TOOLS);

    public static final ResourceKey<EquipmentAsset> DWARVEN_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dwarven"));

    /** round(700 * 0.06) = 42 durability; setProtection(0.7f) gives 2/7/6/2. */
    public static final ArmorMaterial DWARVEN_ARMOR = new ArmorMaterial(
            42,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_DWARVEN_TOOLS, DWARVEN_ASSET);

    public static final ResourceKey<EquipmentAsset> GALVORN_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "galvorn"));

    /**
     * GALVORN: 600 uses, protection 0.6, enchantability 15 -- the same figures
     * as Galadhrim, which is why its armour reads identically. Only the armour
     * half is needed so far; the tool half arrives with the galvorn weapons.
     */
    public static final ArmorMaterial GALVORN_ARMOR = new ArmorMaterial(
            36,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            15, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GALVORN_TOOLS, GALVORN_ASSET);

    /**
     * URUK: 550 uses, 3.0 damage, harvest level 2, speed 6.0, enchantability 5
     * -- brutal and badly made, which is what the low enchantability says.
     */
    public static final ToolMaterial URUK = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 550, 6.0f, 3.0f, 5,
            LOTRItemTags.REPAIRS_URUK_TOOLS);

    public static final ResourceKey<EquipmentAsset> URUK_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "uruk"));

    /** The Uruk helmet wears its own layer -- extraName "helmet", as Gondor's does. */
    public static final ResourceKey<EquipmentAsset> URUK_HELMET_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "uruk_helmet"));

    /** round(550 * 0.06) = 33 durability; setProtection(0.7f) gives 2/7/6/2. */
    public static final ArmorMaterial URUK_ARMOR = new ArmorMaterial(
            33,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 13),
            5, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_URUK_TOOLS, URUK_ASSET);

    /** The same figures, pointing at the helmet's own texture. */
    public static final ArmorMaterial URUK_HELMET_ARMOR = new ArmorMaterial(
            33,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
            5, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_URUK_TOOLS, URUK_HELMET_ASSET);

    /**
     * WOOD_ELVEN: 500 uses, 3.0 damage, harvest level 2, speed 9.0,
     * enchantability 15 -- the quickest edge in the mod bar mithril, which is
     * what a Mirkwood bow is made of.
     */
    public static final ToolMaterial WOOD_ELVEN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 500, 9.0f, 3.0f, 15,
            LOTRItemTags.REPAIRS_WOOD_ELVEN_TOOLS);

    public static final ResourceKey<EquipmentAsset> WOOD_ELVEN_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "wood_elven"));

    /** round(500 * 0.06) = 30 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial WOOD_ELVEN_ARMOR = new ArmorMaterial(
            30,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            15, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_WOOD_ELVEN_TOOLS, WOOD_ELVEN_ASSET);

    /**
     * ROHAN: 300 uses, 2.5 damage, harvest level 2, speed 6.0, enchantability
     * 10 -- Gondor's numbers on a third less steel, which is the Mark all over.
     */
    public static final ToolMaterial ROHIRRIC = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 300, 6.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_ROHIRRIC_TOOLS);

    public static final ResourceKey<EquipmentAsset> ROHIRRIC_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "rohirric"));

    /** round(300 * 0.06) = 18 durability; setProtection(0.5f) gives 2/5/4/2. */
    public static final ArmorMaterial ROHIRRIC_ARMOR = new ArmorMaterial(
            18,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 9),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_ROHIRRIC_TOOLS, ROHIRRIC_ASSET);

    /**
     * The winged Gondorian helmet wears its own layer -- extraName
     * "wingedHelmet", so getArmorName gave it gondor_wingedHelmet rather than
     * the gondor_helmet the plain one uses.
     */
    public static final ResourceKey<EquipmentAsset> GONDOR_WINGED_HELMET_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gondor_winged_helmet"));

    /** GONDOR_ARMOR's figures, pointing at the winged helmet's own texture. */
    public static final ArmorMaterial GONDOR_WINGED_HELMET_ARMOR = new ArmorMaterial(
            27,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GONDOR_TOOLS, GONDOR_WINGED_HELMET_ASSET);

    public static final ResourceKey<EquipmentAsset> RANGER_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ranger"));

    /**
     * RANGER: 350 uses, protection 0.48, enchantability 12 -- no tool half, as
     * the port has no Ranger blade. round(350 * 0.06) = 21 durability, and
     * setProtection(0.48f) gives round(0.14 * 0.48 * 25) = 2, then 5, 4 and 2.
     *
     * <p>Leather's equip sound rather than iron's: a Hood and a Tunic are soft
     * kit, the same reasoning the fur set is given, even though iron is one of
     * the two things that mends them.
     */
    public static final ArmorMaterial RANGER_ARMOR = new ArmorMaterial(
            21,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2),
            12, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_RANGER_ARMOR, RANGER_ASSET);

    public static final ResourceKey<EquipmentAsset> DUNLENDING_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dunlending"));

    /**
     * DUNLENDING: 250 uses, protection 0.5, enchantability 8. round(250 * 0.06)
     * = 15 durability and 2/5/4/2 defence -- the poorest metal set in the tab.
     *
     * <p>Armour only. Dunland's two weapons are not built on this material at
     * all: the club is vanilla WOOD and the trident vanilla IRON, exactly as
     * LOTRMod registers them.
     */
    public static final ArmorMaterial DUNLENDING_ARMOR = new ArmorMaterial(
            15,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2),
            8, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_DUNLENDING_TOOLS, DUNLENDING_ASSET);

    /** MORGUL: 450 uses, 2.5 damage, harvest level 2, speed 6.0, enchantability 10. */
    public static final ToolMaterial MORGUL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 450, 6.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_MORGUL_TOOLS);

    public static final ResourceKey<EquipmentAsset> MORGUL_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "morgul"));

    /** The Morgul helmet wears its own layer -- extraName "helmet", as Gondor's does. */
    public static final ResourceKey<EquipmentAsset> MORGUL_HELMET_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "morgul_helmet"));

    /** round(450 * 0.06) = 27 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial MORGUL_ARMOR = new ArmorMaterial(
            27,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_MORGUL_TOOLS, MORGUL_ASSET);

    /** The same figures, pointing at the helmet's own texture. */
    public static final ArmorMaterial MORGUL_HELMET_ARMOR = new ArmorMaterial(
            27,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_MORGUL_TOOLS, MORGUL_HELMET_ASSET);

    public static final ResourceKey<EquipmentAsset> WOOD_ELVEN_SCOUT_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "wood_elven_scout"));

    /**
     * WOOD_ELVEN_SCOUT: 300 uses, no damage, protection 0.4, enchantability 15
     * -- a scout's leathers, not mail, and armour only. round(300 * 0.06) = 18
     * durability and 1/4/3/1 defence, the same numbers the fur set wears.
     */
    public static final ArmorMaterial WOOD_ELVEN_SCOUT_ARMOR = new ArmorMaterial(
            18,
            Map.of(ArmorType.HELMET, 1, ArmorType.CHESTPLATE, 4,
                    ArmorType.LEGGINGS, 3, ArmorType.BOOTS, 1),
            15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_WOOD_ELVEN_SCOUT_ARMOR, WOOD_ELVEN_SCOUT_ASSET);

    /**
     * ANGMAR: 350 uses, 2.5 damage, harvest level 2, speed 6.0, enchantability
     * 8 -- Mordor's edge on a little more steel, and just as crudely made.
     */
    public static final ToolMaterial ANGMAR = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 350, 6.0f, 2.5f, 8,
            LOTRItemTags.REPAIRS_ANGMAR_TOOLS);

    public static final ResourceKey<EquipmentAsset> ANGMAR_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "angmar"));

    /** round(350 * 0.06) = 21 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial ANGMAR_ARMOR = new ArmorMaterial(
            21,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            8, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_ANGMAR_TOOLS, ANGMAR_ASSET);

    /**
     * UMBAR: 450 uses, 2.5 damage, harvest level 2, speed 6.0, enchantability
     * 10 -- Gondor's own figures, which is what a Corsair fleet is armed with.
     * Tool half only; the Umbaric armour is a separate material again.
     */
    public static final ToolMaterial UMBARIC = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 450, 6.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_UMBARIC_TOOLS);

    public static final ResourceKey<EquipmentAsset> COAST_SOUTHRON_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "coast_southron"));

    /**
     * NEAR_HARAD: 300 uses, protection 0.5, enchantability 10 -- armour only, as
     * far as the port needs it. round(300 * 0.06) = 18 and 2/5/4/2.
     */
    public static final ArmorMaterial COAST_SOUTHRON_ARMOR = new ArmorMaterial(
            18,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 9),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_COAST_SOUTHRON_ARMOR, COAST_SOUTHRON_ASSET);

    public static final ResourceKey<EquipmentAsset> GEMSBOK_HIDE_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gemsbok_hide"));

    /**
     * GEMSBOK: 180 uses, no damage, protection 0.4, enchantability 10 -- hide,
     * and the fur set's twin: round(180 * 0.06) = 11 durability and 1/4/3/1.
     * Leather's equip sound, for the same reason the fur set has it.
     */
    public static final ArmorMaterial GEMSBOK_HIDE_ARMOR = new ArmorMaterial(
            11,
            Map.of(ArmorType.HELMET, 1, ArmorType.CHESTPLATE, 4,
                    ArmorType.LEGGINGS, 3, ArmorType.BOOTS, 1),
            10, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GEMSBOK_HIDE_ARMOR, GEMSBOK_HIDE_ASSET);

    /**
     * HIGH_ELVEN: 700 uses, 3.0 damage, harvest level 2, speed 8.0,
     * enchantability 15 -- the longest-lasting elven steel in the tab.
     */
    public static final ToolMaterial LINDON = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 700, 8.0f, 3.0f, 15,
            LOTRItemTags.REPAIRS_LINDON_TOOLS);

    public static final ResourceKey<EquipmentAsset> LINDON_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "lindon"));

    /** round(700 * 0.06) = 42 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial LINDON_ARMOR = new ArmorMaterial(
            42,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            15, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_LINDON_TOOLS, LINDON_ASSET);

    /**
     * The tool half of LOTRMaterial.NEAR_HARAD: 300 uses, 2.5 damage, speed
     * 6.0, enchantability 10. The armour made from it is called Coast Southron
     * and the bow the Bow of Harad, but it is one material underneath.
     */
    public static final ToolMaterial COAST_SOUTHRON = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 300, 6.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_COAST_SOUTHRON_ARMOR);

    public static final ResourceKey<EquipmentAsset> UMBARIC_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "umbaric"));

    /**
     * The armour half of LOTRMaterial.UMBAR, which the port needs for one item
     * only: the Umbaric horse armour. Its equipment asset carries a horse_body
     * layer and nothing else, since none of the worn Umbaric pieces are ported.
     * round(450 * 0.06) = 27, protection 0.6, so the horse takes 6 + 5 = 11.
     */
    public static final ArmorMaterial UMBARIC_ARMOR = new ArmorMaterial(
            27,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                    ArmorType.BODY, 11),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_UMBARIC_TOOLS, UMBARIC_ASSET);

    /**
     * BLUE_DWARVEN: 650 uses, 3.0 damage, harvest level 3, speed 7.0,
     * enchantability 12 -- Dwarven steel, a shade less of it, and rather more
     * willing to take an enchantment.
     */
    public static final ToolMaterial BLUE_DWARVEN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 650, 7.0f, 3.0f, 12,
            LOTRItemTags.REPAIRS_BLUE_DWARVEN_TOOLS);

    public static final ResourceKey<EquipmentAsset> BLUE_DWARVEN_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "blue_dwarven"));

    /** round(650 * 0.06) = 39 durability; setProtection(0.7f) gives 2/7/6/2. */
    public static final ArmorMaterial BLUE_DWARVEN_ARMOR = new ArmorMaterial(
            39,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
            12, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_BLUE_DWARVEN_TOOLS, BLUE_DWARVEN_ASSET);

    /**
     * DOL_GULDUR: 350 uses, 2.5 damage, harvest level 2, speed 6.0,
     * enchantability 10 -- Angmar's steel with a better temper.
     */
    public static final ToolMaterial DOL_GULDUR = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 350, 6.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_DOL_GULDUR_TOOLS);

    public static final ResourceKey<EquipmentAsset> DOL_GULDUR_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dol_guldur"));

    /** round(350 * 0.06) = 21 durability; setProtection(0.6f) gives 2/6/5/2. */
    public static final ArmorMaterial DOL_GULDUR_ARMOR = new ArmorMaterial(
            21,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                    ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_DOL_GULDUR_TOOLS, DOL_GULDUR_ASSET);

    /**
     * UTUMNO: 400 uses, 3.5 damage, harvest level 2, speed 6.0, enchantability
     * 12 -- the HARDEST-hitting material in the tab bar mithril, and it does
     * not last long, which is the bargain the old dark makes.
     */
    public static final ToolMaterial UTUMNO = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 400, 6.0f, 3.5f, 12,
            LOTRItemTags.REPAIRS_UTUMNO_TOOLS);

    public static final ResourceKey<EquipmentAsset> UTUMNO_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "utumno"));

    /** round(400 * 0.06) = 24 durability; setProtection(0.7f) gives 2/7/6/2. */
    public static final ArmorMaterial UTUMNO_ARMOR = new ArmorMaterial(
            24,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
            12, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_UTUMNO_TOOLS, UTUMNO_ASSET);

    /**
     * BLACK_URUK: 550 uses, 3.0 damage, harvest level 2, speed 6.0,
     * enchantability 6 -- Uruk steel, one point better made and no easier to
     * enchant.
     */
    public static final ToolMaterial BLACK_URUK = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 550, 6.0f, 3.0f, 6,
            LOTRItemTags.REPAIRS_BLACK_URUK_TOOLS);

    public static final ResourceKey<EquipmentAsset> BLACK_URUK_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "black_uruk"));

    /** round(550 * 0.06) = 33 durability; setProtection(0.7f) gives 2/7/6/2. */
    public static final ArmorMaterial BLACK_URUK_ARMOR = new ArmorMaterial(
            33,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                    ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
            6, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_BLACK_URUK_TOOLS, BLACK_URUK_ASSET);

    /**
     * HALF_TROLL: 300 uses, 2.5 damage, harvest level 1, speed 5.0,
     * enchantability 5 -- heavy, clumsy and barely worth enchanting.
     */
    public static final ToolMaterial HALF_TROLL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL, 300, 5.0f, 2.5f, 5,
            LOTRItemTags.REPAIRS_HALF_TROLL_TOOLS);

    public static final ResourceKey<EquipmentAsset> HALF_TROLL_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "half_troll"));

    /** round(300 * 0.06) = 18 durability; setProtection(0.5f) gives 2/5/4/2. */
    public static final ArmorMaterial HALF_TROLL_ARMOR = new ArmorMaterial(
            18,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2),
            5, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_HALF_TROLL_TOOLS, HALF_TROLL_ASSET);

    /**
     * The three trimmed Dwarven sets. Same DWARVEN material, same numbers --
     * LOTRItemArmor(DWARVEN, slot, "silver_1") only changes the sheet -- so the
     * only thing new about each is its equipment asset.
     */
    public static final ResourceKey<EquipmentAsset> SILVER_TRIMMED_DWARVEN_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "silver_trimmed_dwarven"));

    public static final ResourceKey<EquipmentAsset> GOLD_TRIMMED_DWARVEN_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gold_trimmed_dwarven"));

    public static final ResourceKey<EquipmentAsset> MITHRIL_TRIMMED_DWARVEN_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mithril_trimmed_dwarven"));

    private static ArmorMaterial trimmedDwarven(ResourceKey<EquipmentAsset> asset) {
        return new ArmorMaterial(42,
                Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                        ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
                10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_DWARVEN_TOOLS, asset);
    }

    public static final ArmorMaterial SILVER_TRIMMED_DWARVEN_ARMOR =
            trimmedDwarven(SILVER_TRIMMED_DWARVEN_ASSET);
    public static final ArmorMaterial GOLD_TRIMMED_DWARVEN_ARMOR =
            trimmedDwarven(GOLD_TRIMMED_DWARVEN_ASSET);
    public static final ArmorMaterial MITHRIL_TRIMMED_DWARVEN_ARMOR =
            trimmedDwarven(MITHRIL_TRIMMED_DWARVEN_ASSET);

    /** DOL_AMROTH: 500 uses, 3.0 damage, harvest level 2, speed 6.0, ench 10. */
    public static final ToolMaterial DOL_AMROTH = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 500, 6.0f, 3.0f, 10,
            LOTRItemTags.REPAIRS_DOL_AMROTH_TOOLS);

    public static final ResourceKey<EquipmentAsset> DOL_AMROTH_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dol_amroth"));

    /** The helmet and the chestplate each wear a sheet of their own -- the swan. */
    public static final ResourceKey<EquipmentAsset> DOL_AMROTH_WINGED_HELMET_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dol_amroth_winged_helmet"));

    public static final ResourceKey<EquipmentAsset> DOL_AMROTH_WINGED_BODY_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dol_amroth_winged_body"));

    private static ArmorMaterial dolAmroth(ResourceKey<EquipmentAsset> asset) {
        // round(500 * 0.06) = 30; setProtection(0.6f) gives 2/6/5/2, and the
        // horse takes chestplate + leggings.
        return new ArmorMaterial(30,
                Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                        ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2,
                        ArmorType.BODY, 11),
                10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_DOL_AMROTH_TOOLS, asset);
    }

    public static final ArmorMaterial DOL_AMROTH_ARMOR = dolAmroth(DOL_AMROTH_ASSET);
    public static final ArmorMaterial DOL_AMROTH_WINGED_HELMET_ARMOR =
            dolAmroth(DOL_AMROTH_WINGED_HELMET_ASSET);
    public static final ArmorMaterial DOL_AMROTH_WINGED_BODY_ARMOR =
            dolAmroth(DOL_AMROTH_WINGED_BODY_ASSET);

    /** MOREDAIN: 250 uses, 2.0 damage, harvest level 2, speed 6.0, ench 10. */
    public static final ToolMaterial MORWAITH = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0f, 2.0f, 10,
            LOTRItemTags.REPAIRS_MORWAITH_TOOLS);

    /**
     * MOREDAIN_SPEAR is its OWN material: the same 250 uses, but 3.0 damage
     * rather than 2.0 and a gemsbok horn to mend it. A Morwaith spear is a
     * horn on a shaft, not a blade.
     */
    public static final ToolMaterial MORWAITH_SPEAR = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0f, 3.0f, 10,
            LOTRItemTags.REPAIRS_MORWAITH_SPEAR);

    public static final ResourceKey<EquipmentAsset> MORWAITH_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "morwaith"));

    /** round(250 * 0.06) = 15 durability; setProtection(0.48f) gives 2/5/4/2. */
    public static final ArmorMaterial MORWAITH_ARMOR = new ArmorMaterial(
            15,
            Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_MORWAITH_TOOLS, MORWAITH_ASSET);

    public static final ResourceKey<EquipmentAsset> MORWAITH_CHIEFTAIN_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "morwaith_chieftain"));

    /** The chieftain's helmet is a lion's head, and wears its own sheet. */
    public static final ResourceKey<EquipmentAsset> MORWAITH_CHIEFTAIN_HELMET_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "morwaith_chieftain_helmet"));

    private static ArmorMaterial morwaithChieftain(ResourceKey<EquipmentAsset> asset) {
        // MOREDAIN_LION_ARMOR: 300 uses, protection 0.4, enchantability 8 --
        // lion hide, so round(300 * 0.06) = 18 and 1/4/3/1, and leather's sound.
        return new ArmorMaterial(18,
                Map.of(ArmorType.HELMET, 1, ArmorType.CHESTPLATE, 4,
                        ArmorType.LEGGINGS, 3, ArmorType.BOOTS, 1),
                8, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_MORWAITH_CHIEFTAIN_ARMOR, asset);
    }

    public static final ArmorMaterial MORWAITH_CHIEFTAIN_ARMOR =
            morwaithChieftain(MORWAITH_CHIEFTAIN_ASSET);
    public static final ArmorMaterial MORWAITH_CHIEFTAIN_HELMET_ARMOR =
            morwaithChieftain(MORWAITH_CHIEFTAIN_HELMET_ASSET);

    public static final ResourceKey<EquipmentAsset> BONE_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "bone"));

    /**
     * BONE: 150 uses, protection 0.3, enchantability 10 -- the thinnest armour
     * in the tab. round(150 * 0.06) = 9 durability, and setProtection(0.3f)
     * gives round(0.14 * 0.3 * 25) = 1, then 3, 2 and 1.
     */
    public static final ArmorMaterial BONE_ARMOR = new ArmorMaterial(
            9,
            Map.of(ArmorType.HELMET, 1, ArmorType.CHESTPLATE, 3,
                    ArmorType.LEGGINGS, 2, ArmorType.BOOTS, 1),
            10, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_BONE_ARMOR, BONE_ASSET);

    /**
     * GONDOLIN: 1500 uses, 5.0 damage, harvest level 2, speed 8.0,
     * enchantability 15 -- mithril's edge on elven steel, and the finest thing
     * in the tab that is not mithril itself.
     */
    public static final ToolMaterial GONDOLIN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 1500, 8.0f, 5.0f, 15,
            LOTRItemTags.REPAIRS_GONDOLIN_TOOLS);

    public static final ResourceKey<EquipmentAsset> GONDOLIN_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gondolin"));

    /** The Gondolinian helmet wears its own layer -- extraName "helmet". */
    public static final ResourceKey<EquipmentAsset> GONDOLIN_HELMET_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gondolin_helmet"));

    private static ArmorMaterial gondolin(ResourceKey<EquipmentAsset> asset) {
        // round(1500 * 0.06) = 90 durability; setProtection(0.7f) gives 2/7/6/2.
        return new ArmorMaterial(90,
                Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 7,
                        ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 2),
                15, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_GONDOLIN_TOOLS, asset);
    }

    public static final ArmorMaterial GONDOLIN_ARMOR = gondolin(GONDOLIN_ASSET);
    public static final ArmorMaterial GONDOLIN_HELMET_ARMOR = gondolin(GONDOLIN_HELMET_ASSET);

    /**
     * MALLORN_MACE: 1500 uses, 4.5 damage, no protection and no harvest level
     * at all -- a material that exists for exactly one weapon.
     */
    public static final ToolMaterial CHARRED_MALLORN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL, 1500, 4.0f, 4.5f, 15,
            LOTRItemTags.REPAIRS_CHARRED_MALLORN_MACE);

    public static final ResourceKey<EquipmentAsset> ROHIRRIC_MARSHAL_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "rohirric_marshal"));

    /** The Marshal's helmet wears its own layer -- extraName "helmet". */
    public static final ResourceKey<EquipmentAsset> ROHIRRIC_MARSHAL_HELMET_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "rohirric_marshal_helmet"));

    private static ArmorMaterial rohirricMarshal(ResourceKey<EquipmentAsset> asset) {
        // ROHAN_MARSHAL: 400 uses, protection 0.6, enchantability 10 --
        // round(400 * 0.06) = 24 and 2/6/5/2, a clear step above the line
        // Rohirric mail it is worn over.
        return new ArmorMaterial(24,
                Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                        ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
                10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_ROHIRRIC_MARSHAL_ARMOR, asset);
    }

    public static final ArmorMaterial ROHIRRIC_MARSHAL_ARMOR =
            rohirricMarshal(ROHIRRIC_MARSHAL_ASSET);
    public static final ArmorMaterial ROHIRRIC_MARSHAL_HELMET_ARMOR =
            rohirricMarshal(ROHIRRIC_MARSHAL_HELMET_ASSET);

    /**
     * TAUREDAIN: 300 uses, 2.5 damage, harvest level 3, speed 8.0,
     * enchantability 10 -- obsidian and bronze, so it cuts stone a dwarf's
     * pick would and does not last.
     */
    public static final ToolMaterial TAURETHRIM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 300, 8.0f, 2.5f, 10,
            LOTRItemTags.REPAIRS_TAURETHRIM_TOOLS);

    public static final ResourceKey<EquipmentAsset> TAURETHRIM_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "taurethrim"));

    /** The chieftain's helmet wears its own layer -- extraName "chieftainHelmet". */
    public static final ResourceKey<EquipmentAsset> TAURETHRIM_CHIEFTAIN_HELMET_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "taurethrim_chieftain_helmet"));

    private static ArmorMaterial taurethrim(ResourceKey<EquipmentAsset> asset) {
        // round(300 * 0.06) = 18; setProtection(0.5f) gives 2/5/4/2.
        return new ArmorMaterial(18,
                Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 5,
                        ArmorType.LEGGINGS, 4, ArmorType.BOOTS, 2),
                10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_TAURETHRIM_TOOLS, asset);
    }

    public static final ArmorMaterial TAURETHRIM_ARMOR = taurethrim(TAURETHRIM_ASSET);
    public static final ArmorMaterial TAURETHRIM_CHIEFTAIN_HELMET_ARMOR =
            taurethrim(TAURETHRIM_CHIEFTAIN_HELMET_ASSET);

    /**
     * BARROW: 600 uses, 3.0 damage, harvest level 2, speed 8.0, enchantability
     * 10 -- the metal of the barrow-blades, mended with plain iron.
     */
    public static final ToolMaterial BARROW = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 600, 8.0f, 3.0f, 10,
            LOTRItemTags.REPAIRS_BARROW_TOOLS);

    public static final ResourceKey<EquipmentAsset> GALADHRIM_CLOAK_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "galadhrim_cloak"));

    /**
     * HITHLAIN: 300 uses, protection 0.3, enchantability 15 -- elven rope woven
     * into a cloak, so it is armour only and barely that. round(300 * 0.06) = 18
     * durability and 1/3/2/1, the same shelter bone gives on rather more cloth.
     */
    public static final ArmorMaterial GALADHRIM_CLOAK_ARMOR = new ArmorMaterial(
            18,
            Map.of(ArmorType.HELMET, 1, ArmorType.CHESTPLATE, 3,
                    ArmorType.LEGGINGS, 2, ArmorType.BOOTS, 1),
            15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            LOTRItemTags.REPAIRS_GALADHRIM_CLOAK, GALADHRIM_CLOAK_ASSET);

    public static final ResourceKey<EquipmentAsset> GOLDEN_TAURETHRIM_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "golden_taurethrim"));

    /** The golden helmet wears its own layer -- extraName "helmet". */
    public static final ResourceKey<EquipmentAsset> GOLDEN_TAURETHRIM_HELMET_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "golden_taurethrim_helmet"));

    private static ArmorMaterial goldenTaurethrim(ResourceKey<EquipmentAsset> asset) {
        // TAUREDAIN_GOLD: 400 uses, NO damage and no harvest level -- it is
        // regalia, not war gear -- protection 0.6, enchantability 10. So
        // round(400 * 0.06) = 24 durability and 2/6/5/2: better armour than the
        // plain Taurethrim set, on a material that arms nothing.
        return new ArmorMaterial(24,
                Map.of(ArmorType.HELMET, 2, ArmorType.CHESTPLATE, 6,
                        ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
                10, SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f,
                LOTRItemTags.REPAIRS_GOLDEN_TAURETHRIM_ARMOR, asset);
    }

    public static final ArmorMaterial GOLDEN_TAURETHRIM_ARMOR =
            goldenTaurethrim(GOLDEN_TAURETHRIM_ASSET);
    public static final ArmorMaterial GOLDEN_TAURETHRIM_HELMET_ARMOR =
            goldenTaurethrim(GOLDEN_TAURETHRIM_HELMET_ASSET);

    private LOTRToolMaterials() {
    }
}
