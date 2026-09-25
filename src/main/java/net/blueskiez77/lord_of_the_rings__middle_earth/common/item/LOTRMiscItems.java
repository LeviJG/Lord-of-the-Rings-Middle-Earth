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

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems.*;

/**
 * The LOTR items of tabMisc: coins, rings, ancient items, horns and other odds and ends.
 * Split out of LOTRItems by creative tab; LOTRItems keeps the shared
 * builders and registers this class in order from its init.
 */
public final class LOTRMiscItems {

    // LOTRItemMechanism, from tabMisc: right-click a rail with one and it
    // becomes a mechanised rail -- a powered rail that needs no redstone.
    public static final Item MECHANISM = register("mechanism", LOTRMechanismItem::new, new Item.Properties());

    //
    // NOT registered: the pouch, which is a container item with an inventory of
    // its own, and the NPC respawner, which needs NPCs. The smoking pipe is
    // LOTRSmokingPipeItem, which puffs smoke rings.
    //
    // The coins keep their three 1.7.10 stack values as three items; the value
    // system that counted them is not ported. The ancient items roll a random
    // weapon or armour piece out of their chest pool (LOTRAncientItem); the
    // parts are plain crafting materials.
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
    public static final Item ANCIENT_SWORD = register("ancient_sword",
            props -> new LOTRAncientItem(LOTRChestContents.ANCIENT_SWORD, props), new Item.Properties());
    public static final Item ANCIENT_DAGGER = register("ancient_dagger",
            props -> new LOTRAncientItem(LOTRChestContents.ANCIENT_DAGGER, props), new Item.Properties());
    public static final Item ANCIENT_HELMET = register("ancient_helmet",
            props -> new LOTRAncientItem(LOTRChestContents.ANCIENT_HELMET, props), new Item.Properties());
    public static final Item ANCIENT_CHESTPLATE = register("ancient_chestplate",
            props -> new LOTRAncientItem(LOTRChestContents.ANCIENT_BODY, props), new Item.Properties());
    public static final Item ANCIENT_LEGGINGS = register("ancient_leggings",
            props -> new LOTRAncientItem(LOTRChestContents.ANCIENT_LEGS, props), new Item.Properties());
    public static final Item ANCIENT_BOOTS = register("ancient_boots",
            props -> new LOTRAncientItem(LOTRChestContents.ANCIENT_BOOTS, props), new Item.Properties());
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
            props -> new LOTRThrownMiscItem(LOTRMysteryWebEntity::new, 0.5f,
                    () -> LOTREntities.MYSTERY_WEB, props),
            new Item.Properties());
    public static final Item EXPLODING_TERMITE = register("exploding_termite",
            props -> new LOTRThrownMiscItem(LOTRExplodingTermiteEntity::new, 1.5f,
                    () -> LOTREntities.EXPLODING_TERMITE, props),
            new Item.Properties().stacksTo(16));
    public static final Item CONKER = register("conker",
            props -> new LOTRThrownMiscItem(LOTRConkerEntity::new, 1.0f,
                    () -> LOTREntities.CONKER, props),
            new Item.Properties().stacksTo(16));
    public static final Item LEATHER_HAT = register("leather_hat", LOTRLeatherHatItem::new, new Item.Properties()
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
                                    LOTREffects.DRINK_POISON, 300), 1.0F))
                    .build())
            .usingConvertsTo(net.minecraft.world.item.Items.GLASS_BOTTLE));

    private LOTRMiscItems() {
    }

    /** Forces class-load, so the static fields register. Called from LOTRItems.init. */
    static void init() {
    }
}
