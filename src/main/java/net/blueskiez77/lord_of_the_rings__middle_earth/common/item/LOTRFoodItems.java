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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRConkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRExplodingTermiteEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMysteryWebEntity;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems.*;

/**
 * The LOTR items of tabFood: food, drinks and their vessels.
 * Split out of LOTRItems by creative tab; LOTRItems keeps the shared
 * builders and registers this class in order from its init.
 */
public final class LOTRFoodItems {

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
            props -> new net.minecraft.world.item.BlockItem(LOTRUtilityBlocks.LETTUCE_CROP, props), new Item.Properties().food(food(3, 0.4f)).useItemDescriptionPrefix());
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
            props -> new LOTRHangingFruitItem(LOTRFoodBlocks.BANANA_BLOCK, props), new Item.Properties().food(food(2, 0.5f)));
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
            props -> new LOTRHangingFruitItem(LOTRFoodBlocks.DATE_BLOCK, props), new Item.Properties().food(food(2, 0.3f)));
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
            props -> new net.minecraft.world.item.BlockItem(LOTRUtilityBlocks.LEEK_CROP, props), new Item.Properties().food(food(2, 0.3f)).useItemDescriptionPrefix());
    public static final Item LEEK_SOUP = register("leek_soup",
            Item::new, new Item.Properties().stacksTo(1).food(food(8, 0.8f)).usingConvertsTo(net.minecraft.world.item.Items.BOWL).craftRemainder(net.minecraft.world.item.Items.BOWL));
    public static final Item TURNIP = register("turnip",
            props -> new net.minecraft.world.item.BlockItem(LOTRUtilityBlocks.TURNIP_CROP, props), new Item.Properties().food(food(2, 0.3f)).useItemDescriptionPrefix());
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
            props -> new net.minecraft.world.item.BlockItem(LOTRUtilityBlocks.YAM_CROP, props), new Item.Properties().food(food(1, 0.4f),
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
        LOTRCropBlock.setSeed(LOTRUtilityBlocks.LETTUCE_CROP, LETTUCE);
        LOTRCropBlock.setSeed(LOTRUtilityBlocks.LEEK_CROP, LEEK);
        LOTRCropBlock.setSeed(LOTRUtilityBlocks.TURNIP_CROP, TURNIP);
        LOTRCropBlock.setSeed(LOTRUtilityBlocks.YAM_CROP, YAM);
    }

    private LOTRFoodItems() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRItems.init. */
    static void init() {
    }
}
