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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRConkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRExplodingTermiteEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMysteryWebEntity;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems.*;

/**
 * The LOTR items of tabTools: tools, chisels, mattocks and the like.
 * Split out of LOTRItems by creative tab; LOTRItems keeps the shared
 * builders and registers this class in order from its init.
 */
public final class LOTRToolItems {

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

    // The fireball's own item: never in a tab and never in a recipe -- it is
    // only what the thrown entity is drawn as (LOTRRenderGandalfFireball drew
    // frames 24-27 of the particle sheet).
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

    // LOTRItemChisel: a hundred uses and held like a tool. Each carves its own
    // kind of sign; the moon-chisel is a chisel set with ithildin at a dwarven
    // or elven table, keeping its wear.
    public static final Item CHISEL = register("chisel",
            props -> new LOTRChiselItem(() -> LOTRUtilityBlocks.CARVED_SIGN, props),
            new Item.Properties().durability(100));
    public static final Item MOON_CHISEL = register("moon_chisel",
            props -> new LOTRChiselItem(() -> LOTRUtilityBlocks.CARVED_ITHILDIN_SIGN, props),
            new Item.Properties().durability(100));

    private LOTRToolItems() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRItems.init. */
    static void init() {
    }
}
