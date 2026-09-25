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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRConkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRExplodingTermiteEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMysteryWebEntity;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems.*;

/**
 * The LOTR items of tabMaterials: ingots, gems, crafting parts and the special items the LOTR anvil uses.
 * Split out of LOTRItems by creative tab; LOTRItems keeps the shared
 * builders and registers this class in order from its init.
 */
public final class LOTRMaterialItems {

    //
    // In registerItem order. The mithril ingot is registered at the top of this
    // class. NOT registered: the copper ingot and the iron nugget, which vanilla
    // already has (recipes use its copper ingot).
    //
    // The Flame of Udun, Chill of Daedelos, Headhunter's Trophy and Book of
    // True-silver are LOTRItemEnchantment: each puts one modifier on in the LOTR
    // anvil (LOTRAnvilMenu.specialModifier). The Smith's Scroll carries the
    // modifier it teaches.
    //
    // The gems, durnor, edhelvir and gulduril also set a renamed item's colour in
    // the LOTR anvil (AnvilNameColorProvider; LOTRAnvilMenu.nameColour).
    public static final Item TIN_INGOT = register("tin_ingot", Item::new, new Item.Properties());
    public static final Item BRONZE_INGOT = register("bronze_ingot", Item::new, new Item.Properties());
    public static final Item SILVER_INGOT = register("silver_ingot", Item::new, new Item.Properties());
    public static final Item SILVER_NUGGET = register("silver_nugget", Item::new, new Item.Properties());
    public static final Item MITHRIL_NUGGET = register("mithril_nugget", Item::new, new Item.Properties());
    public static final Item ORC_STEEL_INGOT = register("orc_steel_ingot", Item::new, new Item.Properties());
    public static final Item DURNOR = register("durnor", Item::new, new Item.Properties());
    public static final Item PIPEWEED_LEAF = register("pipeweed_leaf", Item::new, new Item.Properties());
    public static final Item PIPEWEED_SEEDS = register("pipeweed_seeds",
            props -> new net.minecraft.world.item.BlockItem(LOTRUtilityBlocks.PIPEWEED_CROP, props),
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
            props -> new net.minecraft.world.item.BlockItem(LOTRUtilityBlocks.FLAX_CROP, props),
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
            props -> new LOTRGrapeSeedsItem(LOTRDecorationBlocks.RED_GRAPEVINE, props),
            new Item.Properties().useItemDescriptionPrefix());
    // LOTRItemGrapeSeeds: planted onto a bare grapevine post, never the ground.
    public static final Item GREEN_GRAPE_SEEDS = register("green_grape_seeds",
            props -> new LOTRGrapeSeedsItem(LOTRDecorationBlocks.GREEN_GRAPEVINE, props),
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
    public static final Item RED_CLAY_BALL = register("red_clay_ball", Item::new, new Item.Properties());

    // LOTRItemSeeds: what the pipe-weed and flax crops are planted from and drop.
    static {
        LOTRCropBlock.setSeed(LOTRUtilityBlocks.PIPEWEED_CROP, PIPEWEED_SEEDS);
        LOTRCropBlock.setSeed(LOTRUtilityBlocks.FLAX_CROP, FLAX_SEEDS);
    }

    private LOTRMaterialItems() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRItems.init. */
    static void init() {
    }
}
