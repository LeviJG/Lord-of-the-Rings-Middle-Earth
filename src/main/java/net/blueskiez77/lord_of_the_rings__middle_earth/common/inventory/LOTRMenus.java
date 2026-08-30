package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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

        for (LOTRCraftingTable table : LOTRCraftingTable.values()) {
            MenuType<LOTRCraftingMenu> type = new MenuType<>(
                    (containerId, inventory) -> new LOTRCraftingMenu(containerId, inventory, table),
                    FeatureFlags.VANILLA_SET);
            Registry.register(BuiltInRegistries.MENU, table.id(), type);
            TYPES.put(table, type);
        }
    }
}