package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class LOTRMenus {

    private static final Map<LOTRCraftingTable, MenuType<LOTRCraftingMenu>> TYPES =
            new EnumMap<>(LOTRCraftingTable.class);

    private LOTRMenus() {
    }

    public static MenuType<LOTRForgeMenu> FORGE;
    public static MenuType<LOTRHobbitOvenMenu> HOBBIT_OVEN;
    public static MenuType<LOTRUnsmelteryMenu> UNSMELTERY;
    public static MenuType<LOTRMillstoneMenu> MILLSTONE;
    public static MenuType<LOTRBarrelMenu> BARREL;
    public static MenuType<LOTRDaleCrackerMenu> DALE_CRACKER;
    public static MenuType<LOTRAnvilMenu> ANVIL;
    /** The trader menus carry the trader's entity id to the client. */
    public static ExtendedMenuType<LOTRTradeMenu, Integer> TRADE;
    public static ExtendedMenuType<LOTRCoinExchangeMenu, Integer> COIN_EXCHANGE;
    /** openGui 54: a smith's anvil, LOTRContainerAnvil's trader half. */
    public static ExtendedMenuType<LOTRAnvilMenu, Integer> SMITH;
    /** openGui 7 and 59: hiring from a captain or a mercenary. */
    public static ExtendedMenuType<LOTRUnitTradeMenu, LOTRUnitTradeMenu.OpeningData> UNIT_TRADE;
    /** openGui 46 and 22: a hired warrior's equipment, a hired farmer's seeds and harvest. */
    public static ExtendedMenuType<LOTRHiredWarriorInventoryMenu, Integer> HIRED_WARRIOR_INVENTORY;
    public static ExtendedMenuType<LOTRHiredFarmerInventoryMenu, Integer> HIRED_FARMER_INVENTORY;
    /** openGui 29: a tame rideable NPC's saddle and barding. */
    public static ExtendedMenuType<LOTRNPCMountInventoryMenu, Integer> NPC_MOUNT_INVENTORY;
    /** openGui 10: Gollum's pack. */
    public static ExtendedMenuType<LOTRGollumMenu, Integer> GOLLUM;

    public static MenuType<LOTRCraftingMenu> forTable(LOTRCraftingTable table) {
        MenuType<LOTRCraftingMenu> type = TYPES.get(table);
        if (type == null) {
            throw new IllegalStateException("Menu type requested before LOTRMenus.init(): " + table);
        }
        return type;
    }

    public static void init() {
        FORGE = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "forge"),
                new MenuType<>(LOTRForgeMenu::new, FeatureFlags.VANILLA_SET));

        HOBBIT_OVEN = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hobbit_oven"),
                new MenuType<>(LOTRHobbitOvenMenu::new, FeatureFlags.VANILLA_SET));

        UNSMELTERY = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "unsmeltery"),
                new MenuType<>(LOTRUnsmelteryMenu::new, FeatureFlags.VANILLA_SET));

        MILLSTONE = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "millstone"),
                new MenuType<>(LOTRMillstoneMenu::new, FeatureFlags.VANILLA_SET));

        BARREL = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "barrel"),
                new MenuType<>(LOTRBarrelMenu::new, FeatureFlags.VANILLA_SET));

        DALE_CRACKER = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dale_cracker"),
                new MenuType<>(LOTRDaleCrackerMenu::new, FeatureFlags.VANILLA_SET));

        ANVIL = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "anvil"),
                new MenuType<>(LOTRAnvilMenu::new, FeatureFlags.VANILLA_SET));

        TRADE = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "trade"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRTradeMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        COIN_EXCHANGE = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "coin_exchange"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRCoinExchangeMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        SMITH = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "smith"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRAnvilMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        UNIT_TRADE = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "unit_trade"),
                new ExtendedMenuType<>((containerId, inventory, data) -> new LOTRUnitTradeMenu(containerId, inventory, data),
                        LOTRUnitTradeMenu.OpeningData.STREAM_CODEC));

        HIRED_WARRIOR_INVENTORY = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hired_warrior_inventory"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRHiredWarriorInventoryMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        HIRED_FARMER_INVENTORY = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hired_farmer_inventory"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRHiredFarmerInventoryMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        NPC_MOUNT_INVENTORY = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "npc_mount_inventory"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRNPCMountInventoryMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        GOLLUM = Registry.register(BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gollum"),
                new ExtendedMenuType<>((containerId, inventory, entityId) -> new LOTRGollumMenu(containerId, inventory, entityId),
                        ByteBufCodecs.VAR_INT));

        for (LOTRCraftingTable table : LOTRCraftingTable.values()) {
            MenuType<LOTRCraftingMenu> type = new MenuType<>(
                    (containerId, inventory) -> new LOTRCraftingMenu(containerId, inventory, table),
                    FeatureFlags.VANILLA_SET);
            Registry.register(BuiltInRegistries.MENU, table.id(), type);
            TYPES.put(table, type);
        }
    }
}