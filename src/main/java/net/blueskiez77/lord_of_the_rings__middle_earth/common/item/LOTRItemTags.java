package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** Item tags the mod defines for itself. */
public final class LOTRItemTags {
    /**
     * What a weapon rack will hold. LOTRTileEntityWeaponRack.canAcceptItem
     * asked LOTRWeaponStats.isMeleeWeapon or isRangedWeapon, plus hoes and
     * fishing rods. LOTRWeaponStats is not ported, so the answer lives in a tag
     * -- which also means the mod's own weapons only have to be added to it
     * once, rather than taught to a stats table.
     */
    public static final TagKey<Item> WEAPON_RACK_HOLDABLE = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "weapon_rack_holdable"));

    /**
     * What mends a Mordor-steel tool or blade. LOTRMaterial.MORDOR set its
     * crafting item to orc steel, which the port has no ingot for yet, so this
     * tag is empty for now and is where that ingot goes when it arrives.
     */
    public static final TagKey<Item> REPAIRS_MORDOR_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_mordor_tools"));

    /**
     * What mends bronze gear. LOTRMaterial.BRONZE set its crafting item to the
     * bronze ingot, which the port has no item for yet, so this is empty until
     * it exists.
     */
    public static final TagKey<Item> REPAIRS_BRONZE_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_bronze_tools"));

    /**
     * What mends Gondorian gear: plain iron. LOTRMaterial.setCraftingItems has
     * {@code GONDOR.setCraftingItem(Items.iron_ingot)}, so this one needs
     * nothing the port lacks -- unlike its neighbours, which wait on ingots.
     */
    public static final TagKey<Item> REPAIRS_GONDOR_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_gondor_tools"));

    /** What mends mithril gear. The mithril ingot IS ported, so this one is filled in. */
    public static final TagKey<Item> REPAIRS_MITHRIL_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_mithril_tools"));

    /** What mends Galadhrim gear -- LOTRMaterial.GALADHRIM's crafting item, not ported. */
    public static final TagKey<Item> REPAIRS_GALADHRIM_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_galadhrim_tools"));

    /** What mends mallorn gear -- LOTRMaterial.MALLORN's crafting item, not ported. */
    public static final TagKey<Item> REPAIRS_MALLORN_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_mallorn_tools"));

    /** What mends fur gear -- LOTRMaterial.FUR's crafting item, not ported. */
    public static final TagKey<Item> REPAIRS_FUR_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_fur_armor"));

    /** What mends dwarven gear -- LOTRMaterial.DWARVEN's crafting item, not ported. */
    public static final TagKey<Item> REPAIRS_DWARVEN_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_dwarven_tools"));

    /** What mends galvorn gear -- LOTRMaterial.GALVORN's crafting item, not ported. */
    public static final TagKey<Item> REPAIRS_GALVORN_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_galvorn_tools"));

    /** What mends uruk gear -- LOTRMaterial.URUK's crafting item, not ported. */
    public static final TagKey<Item> REPAIRS_URUK_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_uruk_tools"));

    /** What mends wood-elven gear -- LOTRMaterial.WOOD_ELVEN's elf steel, not ported. */
    public static final TagKey<Item> REPAIRS_WOOD_ELVEN_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_wood_elven_tools"));

    /**
     * What mends Rohirric gear: plain iron, like Gondor's.
     * {@code ROHAN.setCraftingItem(Items.iron_ingot)}.
     */
    public static final TagKey<Item> REPAIRS_ROHIRRIC_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_rohirric_tools"));

    /**
     * What mends a Ranger's kit: iron and leather, which is what
     * {@code RANGER.setCraftingItems(Items.iron_ingot, Items.leather)} named --
     * both of them vanilla, so this one is filled in.
     */
    public static final TagKey<Item> REPAIRS_RANGER_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_ranger_armor"));

    /** What mends Dunlending gear: iron, as DUNLENDING.setCraftingItem named. */
    public static final TagKey<Item> REPAIRS_DUNLENDING_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_dunlending_tools"));

    /** What mends Morgul gear -- LOTRMaterial.MORGUL's morgul steel, not ported. */
    public static final TagKey<Item> REPAIRS_MORGUL_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_morgul_tools"));

    /**
     * What mends a Wood-elven Scout's kit: elf steel and leather. Only the
     * leather is ported, so this names it directly and picks the elf steel up
     * through the wood-elven tag when that ingot arrives.
     */
    public static final TagKey<Item> REPAIRS_WOOD_ELVEN_SCOUT_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_wood_elven_scout_armor"));

    /**
     * What mends Angmar gear. {@code ANGMAR.setCraftingItem(LOTRMod.orcSteel)}
     * -- the same orc steel Mordor's is mended with, so this leans on that tag
     * rather than keeping a second empty list of its own.
     */
    public static final TagKey<Item> REPAIRS_ANGMAR_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_angmar_tools"));

    /** What mends Umbaric steel: iron, as UMBAR.setCraftingItem named. */
    public static final TagKey<Item> REPAIRS_UMBARIC_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_umbaric_tools"));

    /**
     * What mends Coast Southron gear: bronze, which the port has no ingot for,
     * so this leans on the bronze tag and fills itself in when that does.
     */
    public static final TagKey<Item> REPAIRS_COAST_SOUTHRON_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_coast_southron_armor"));

    /** What mends gemsbok hide -- more gemsbok hide, which is not ported. */
    public static final TagKey<Item> REPAIRS_GEMSBOK_HIDE_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_gemsbok_hide_armor"));

    /** What mends Lindon gear -- LOTRMaterial.HIGH_ELVEN's elf steel, not ported. */
    public static final TagKey<Item> REPAIRS_LINDON_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_lindon_tools"));

    /** What mends Blue Dwarven gear -- BLUE_DWARVEN's blue dwarf steel, not ported. */
    public static final TagKey<Item> REPAIRS_BLUE_DWARVEN_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_blue_dwarven_tools"));

    /** The Bow of Harad is Coast Southron work, so it leans on that tag. */
    public static final TagKey<Item> REPAIRS_HARAD_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_harad_bow"));

    /**
     * What mends Dol Guldur and Utumno gear. Both are
     * {@code setCraftingItem(LOTRMod.orcSteel)}, the same as Mordor and Angmar,
     * so all four lean on the one tag.
     */
    public static final TagKey<Item> REPAIRS_DOL_GULDUR_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_dol_guldur_tools"));

    public static final TagKey<Item> REPAIRS_UTUMNO_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_utumno_tools"));

    /** What mends Black Uruk gear -- its own black uruk steel, not ported. */
    public static final TagKey<Item> REPAIRS_BLACK_URUK_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_black_uruk_tools"));

    /**
     * LOTRItemOrcSkullStaff.getIsRepairable took {@code Items.skull} and
     * NOTHING else -- not even the orc steel its material is made of, since it
     * overrides rather than extends. Every vanilla head counts, as they were
     * all one item with a metadata in 1.7.10.
     */
    public static final TagKey<Item> REPAIRS_ORC_SKULL_STAFF = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_orc_skull_staff"));

    /**
     * What mends the balrog whip: balrog fire, and nothing else --
     * getIsRepairable overrides rather than extends, so not even Utumno steel
     * will do it. Balrog fire is not ported, so this is empty.
     */
    public static final TagKey<Item> REPAIRS_BALROG_WHIP = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_balrog_whip"));

    /** What mends half-troll gear: flint, and gemsbok hide which is not ported. */
    public static final TagKey<Item> REPAIRS_HALF_TROLL_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_half_troll_tools"));

    /** What mends Dol Amroth gear: iron, as DOL_AMROTH.setCraftingItem named. */
    public static final TagKey<Item> REPAIRS_DOL_AMROTH_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_dol_amroth_tools"));

    /** What mends Morwaith gear -- rhino horn and gemsbok hide, neither ported. */
    public static final TagKey<Item> REPAIRS_MORWAITH_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_morwaith_tools"));

    /** MOREDAIN_SPEAR alone is mended with a gemsbok horn, which is not ported. */
    public static final TagKey<Item> REPAIRS_MORWAITH_SPEAR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_morwaith_spear"));

    /** And the chieftain's lion hide with more lion fur, also not ported. */
    public static final TagKey<Item> REPAIRS_MORWAITH_CHIEFTAIN_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_morwaith_chieftain_armor"));

    /** What mends bone: bones. BONE has no crafting item, so this is the port's. */
    public static final TagKey<Item> REPAIRS_BONE_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_bone_armor"));

    /** What mends Gondolinian gear -- GONDOLIN's elf steel, not ported. */
    public static final TagKey<Item> REPAIRS_GONDOLIN_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_gondolin_tools"));

    /** What mends a Marshal's harness: iron, as ROHAN_MARSHAL.setCraftingItem named. */
    public static final TagKey<Item> REPAIRS_ROHIRRIC_MARSHAL_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_rohirric_marshal_armor"));

    /** What mends Taurethrim gear -- obsidian shards and bronze, neither ported. */
    public static final TagKey<Item> REPAIRS_TAURETHRIM_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_taurethrim_tools"));

    /** MALLORN_MACE has no crafting item of its own. */
    public static final TagKey<Item> REPAIRS_CHARRED_MALLORN_MACE = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_charred_mallorn_mace"));

    /** What mends a barrow-blade: iron, as BARROW.setCraftingItem named. */
    public static final TagKey<Item> REPAIRS_BARROW_TOOLS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_barrow_tools"));

    /** What mends a Galadhrim cloak -- HITHLAIN's own hithlain, not ported. */
    public static final TagKey<Item> REPAIRS_GALADHRIM_CLOAK = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_galadhrim_cloak"));

    /** What a Taurethrim blowgun fires: a dart, and nothing else. */
    public static final TagKey<Item> BLOWGUN_DARTS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "blowgun_darts"));

    /** What mends golden Taurethrim regalia: gold, as TAUREDAIN_GOLD names. */
    public static final TagKey<Item> REPAIRS_GOLDEN_TAURETHRIM_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_golden_taurethrim_armor"));

    /**
     * What mends a bow. LOTRItemBow.getIsRepairable took string OR the
     * material's own crafting item, so each bow needs a tag of its own that
     * names both -- string directly, and the material's ingot through the
     * repairs_<material>_tools tag, which fills itself in when that ingot
     * arrives.
     */
    public static final TagKey<Item> REPAIRS_MALLORN_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_mallorn_bow"));

    public static final TagKey<Item> REPAIRS_GALADHRIM_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_galadhrim_bow"));

    /** The orc bow is Mordor steel, so it leans on repairs_mordor_tools. */
    public static final TagKey<Item> REPAIRS_ORC_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_orc_bow"));

    public static final TagKey<Item> REPAIRS_ROHIRRIC_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_rohirric_bow"));

    public static final TagKey<Item> REPAIRS_GONDOR_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_gondor_bow"));

    public static final TagKey<Item> REPAIRS_LINDON_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_lindon_bow"));

    /** The Bow of Mirkwood is wood-elven, so it leans on repairs_wood_elven_tools. */
    public static final TagKey<Item> REPAIRS_MIRKWOOD_BOW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_mirkwood_bow"));

    /** LOTRItemSling.getIsRepairable took leather, and nothing else. */
    public static final TagKey<Item> REPAIRS_SLING = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "repairs_sling"));

    private LOTRItemTags() {
    }
}
